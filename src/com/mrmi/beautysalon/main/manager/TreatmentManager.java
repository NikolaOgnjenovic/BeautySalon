package com.mrmi.beautysalon.main.manager;

import com.mrmi.beautysalon.main.exceptions.TreatmentNotFoundException;
import com.mrmi.beautysalon.main.exceptions.TreatmentTypeCategoryNotFoundException;
import com.mrmi.beautysalon.main.exceptions.TreatmentTypeNotFoundException;
import com.mrmi.beautysalon.main.exceptions.UserNotFoundException;
import com.mrmi.beautysalon.main.entity.*;

import java.util.*;
import java.util.stream.Collectors;

public class TreatmentManager {
    private final Database database;
    private final SalonManager salonManager;
    private final HashMap<Integer, TreatmentTypeCategory> categories;
    private final HashMap<Integer, TreatmentType> types;
    private final HashMap<Integer, Treatment> treatments;

    public TreatmentManager(Database database,SalonManager salonManager) {
        this.database = database;
        this.salonManager = salonManager;
        this.categories = database.getTreatmentTypeCategories();
        this.types = database.getTreatmentTypes();
        this.treatments = database.getTreatments();
    }

    //region Treatment type category
    public void addTreatmentTypeCategory(String name) {
        int id = database.getNextTreatmentTypeCategoryId();
        TreatmentTypeCategory category = new TreatmentTypeCategory(name);
        categories.put(id, category);
        database.addTreatmentTypeCategory(id, category);
    }

    public HashMap<Integer, TreatmentTypeCategory> getTreatmentTypeCategories() {
        return categories;
    }

    public TreatmentTypeCategory getTreatmentTypeCategory(int id) throws TreatmentTypeCategoryNotFoundException {
        if (!categories.containsKey(id)) {
            throw new TreatmentTypeCategoryNotFoundException("Treatment type category with id " + id + " not found.");
        }
        return categories.get(id);
    }

    public HashMap<Integer, TreatmentTypeCategory> getAvailableTreatmentTypeCategories() {
        return (HashMap<Integer, TreatmentTypeCategory>) categories.entrySet()
                .stream()
                .filter(category -> !category.getValue().isDeleted())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    public String getTreatmentTypeCategoryName(int treatmentTypeCategoryId) throws TreatmentTypeCategoryNotFoundException {
        TreatmentTypeCategory category = getTreatmentTypeCategory(treatmentTypeCategoryId);
        return category.getName();
    }

    public String getTreatmentTypeCategoryNameByType(int treatmentTypeId) throws TreatmentTypeNotFoundException, TreatmentTypeCategoryNotFoundException {
        TreatmentType treatmentType = getTreatmentType(treatmentTypeId);

        return getTreatmentTypeCategoryName(treatmentType.getCategoryId());
    }

    public void updateTreatmentTypeCategory(TreatmentTypeCategory treatmentTypeCategory) {
        categories.put(treatmentTypeCategory.getId(), treatmentTypeCategory);
        database.updateTreatmentTypeCategory(treatmentTypeCategory);
    }

    public void updateTreatmentTypeCategoryName(TreatmentTypeCategory treatmentTypeCategory, String name) {
        treatmentTypeCategory.setName(name);
        database.updateTreatmentTypeCategory(treatmentTypeCategory);
    }

    public void deleteTreatmentTypeCategory(int id) throws TreatmentTypeCategoryNotFoundException {
        TreatmentTypeCategory category = getTreatmentTypeCategory(id);
        category.setDeleted(true);
        updateTreatmentTypeCategory(category);

        for (Map.Entry<Integer, TreatmentType> entry : types.entrySet()) {
            if (entry.getValue().getCategoryId() == id) {
                entry.getValue().setDeleted(true);
                updateTreatmentType(entry.getValue());
            }
        }
    }

    public int[] getCategoryProfitByMonths(int treatmentTypeCategoryId) {
        int[] profit = new int[12];
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        for (Treatment t : treatments.values()) {
            if (treatmentTypeCategoryId == types.get(t.getTreatmentTypeId()).getCategoryId()) {
                if (t.getScheduledDate().after(calendar)) {
                    profit[t.getScheduledDate().get(Calendar.MONTH)] += t.getPrice();
                }
            }
        }

        return profit;
    }
    //endregion

    //region Treatment type
    public void addTreatmentType(String name, double price, int treatmentTypeCategoryId, int duration) {
        int id = database.getNextTreatmentTypeId();
        database.addTreatmentType(id, new TreatmentType(name, price, treatmentTypeCategoryId, duration));
    }
    public HashMap<Integer, TreatmentType> getTreatmentTypes() {
        return types;
    }

    /**
     *
     * @return HashMap of treatments types that aren't marked as deleted
     */
    public HashMap<Integer, TreatmentType> getAvailableTreatmentTypes() {
        return (HashMap<Integer, TreatmentType>) types.entrySet()
                .stream()
                .filter(type -> !type.getValue().isDeleted())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public TreatmentType getTreatmentType(int id) throws TreatmentTypeNotFoundException {
        if (!types.containsKey(id)) {
            throw new TreatmentTypeNotFoundException("Treatment type with id " + id + " not found.");

        }
        return types.get(id);
    }

    public void updateTreatmentType(TreatmentType type) {
        database.updateTreatmentType(type.getId(), type);
    }

    public void updateTreatmentType(TreatmentType type, String name, double price, int treatmentTypeCategoryId, int duration) {
        type.setName(name);
        type.setPrice(price);
        type.setCategoryId(treatmentTypeCategoryId);
        type.setDuration(duration);
        database.updateTreatmentType(type.getId(), type);
    }

    public void deleteTreatmentType(int id) throws TreatmentTypeNotFoundException {
        TreatmentType type = getTreatmentType(id);
        type.setDeleted(true);
        updateTreatmentType(type);
    }

    public String getTreatmentTypeName(int treatmentTypeId) throws TreatmentTypeNotFoundException {
        TreatmentType treatmentType = getTreatmentType(treatmentTypeId);
        return treatmentType.getName();
    }

    /**
     *
     * @param selectedDate The date on which the time slots are being calculated
     * @param treatmentTypeId The id of the treatment type
     * @param salonManager Used for the opening & closing hour
     * @return a Vector of Strings which contains available time slots for the treatment
     */
    public Vector<String> getAvailableTimeSlots(Calendar selectedDate, int treatmentTypeId, SalonManager salonManager) {
        if (!types.containsKey(treatmentTypeId)) {
            return new Vector<>();
        }

        // Round the treatment duration (minutes) to hours
        int duration = types.get(treatmentTypeId).getDuration();
        int roundedDurationHour = duration / 60 + 1;
        Vector<String> availableTimeSlots = new Vector<>();

        // Get all treatments
        List<Treatment> sortedTreatments = new ArrayList<>(treatments.values());
        // Sort them by their date
        sortedTreatments.sort(Comparator.comparing(Treatment::getScheduledDate));
        // Filter the treatments by the given scheduled date
        sortedTreatments = sortedTreatments.stream().filter(t -> t.getScheduledDate().get(Calendar.DATE) == selectedDate.get(Calendar.DATE)).collect(Collectors.toList());

        // Loop from the salon's opening hour to the closing hour, increasing i by the duration of the treatment in hours
        for (int i = salonManager.getOpeningHour(); i < salonManager.getClosingHour(); i += roundedDurationHour) {
            int currentHour = i;
            // If any of the filtered treatments' scheduled hour is == i continue looping
            if (sortedTreatments.stream().anyMatch(treatment -> treatment.getScheduledDate().get(Calendar.HOUR) == currentHour)) {
                continue;
            }
            // Else print the current time slot for the treatment
            availableTimeSlots.add(hourToString(i) + ":00 - " + hourToString(i + duration / 60) + ":" + hourToString(duration % 60));
        }

        return availableTimeSlots;
    }

    private String hourToString(int h) {
        if (h >= 10) {
            return String.valueOf(h);
        }
        return "0" + h;
    }

    public int getTimesBooked(int treatmentTypeId) {
        int counter = 0;
        for (Treatment t : treatments.values()) {
            if (t.getTreatmentTypeId() == treatmentTypeId) {
                counter++;
            }
        }

        return counter;
    }

    public double getProfit(int treatmentTypeId) {
        double profit = 0;
        for (Treatment t : treatments.values()) {
            if (t.getTreatmentTypeId() == treatmentTypeId) {
                profit += t.getPrice();
            }
        }

        return profit;
    }
    //endregion

    //region Treatment

    public HashMap<Integer, Treatment> getTreatments() {
        return treatments;
    }

    public void addTreatment(Treatment treatment) {
        int id = database.getNextTreatmentId();
        treatments.put(id, treatment);
        database.addTreatment(id, treatment);
    }

    public Treatment getTreatment(int id) throws TreatmentNotFoundException {
        if (!treatments.containsKey(id)) {
            throw new TreatmentNotFoundException("Treatment with id " + id + " not found.");
        }
        return treatments.get(id);
    }

    public void updateTreatment(Treatment treatment) throws TreatmentNotFoundException {
        if (!treatments.containsKey(treatment.getId())) {
            throw new TreatmentNotFoundException("Treatment with id " + treatment.getId() + " not found.");
        }
        database.updateTreatment(treatment);
    }

    public void updateTreatment(Treatment treatment, int treatmentTypeId, Calendar scheduledDate, Double price, String clientUsername, String beauticianUsername, Treatment.Status status) throws TreatmentNotFoundException {
        treatment.setTreatmentTypeId(treatmentTypeId);
        treatment.setScheduledDate(scheduledDate);
        treatment.setPrice(price);
        treatment.setClientUsername(clientUsername);
        treatment.setBeauticianUsername(beauticianUsername);
        treatment.setStatus(status);
        if (status != Treatment.Status.CANCELLED_BY_CLIENT && status != Treatment.Status.CANCELLED_BY_SALON) {
            treatment.setCancelled(false);
            treatment.setCancellationReason("N/A");
        }

        updateTreatment(treatment);
    }

    public void deleteTreatment(int id) throws TreatmentNotFoundException {
        if (!treatments.containsKey(id)) {
            throw new TreatmentNotFoundException("Treatment with id " + id + " not found.");
        }
        database.deleteTreatment(id);
    }

    public HashMap<Treatment.Status, Integer> getStatusCountMap() {
        ArrayList<Treatment> treatmentsByStatus = new ArrayList<>(treatments.values());
        treatmentsByStatus.sort(Comparator.comparing(Treatment::getStatus));

        HashMap<Treatment.Status, Integer> statusCountMap = new HashMap<>();
        if (treatmentsByStatus.size() < 1) {
            return statusCountMap;
        }

        Treatment.Status currentStatus = treatmentsByStatus.get(0).getStatus();
        int counter = 1;
        for (Treatment treatment : treatmentsByStatus) {
            if (currentStatus != treatment.getStatus()) {
                statusCountMap.put(currentStatus, counter);
                currentStatus = treatment.getStatus();
                counter = 1;
            } else {
                counter++;
            }
        }

        statusCountMap.put(currentStatus, counter);
        return statusCountMap;
    }

    public double getTotalCost(HashMap<Integer, Treatment> treatments) {
        double cost = 0d;
        for (Treatment t : treatments.values()) {
            cost += t.getPrice();
        }
        return cost;
    }

    public List<Treatment> getTreatmentsSortedByBeauticians() {
        List<Treatment> list = new ArrayList<>(treatments.values());
        list.sort(Comparator.comparing(Treatment::getBeauticianUsername));
        return list;
    }

    public List<Treatment> getTreatmentsSortedByCancellationReason() {
        List<Treatment> list = new ArrayList<>(treatments.values());
        list.sort(Comparator.comparing(Treatment::getCancellationReason));
        return list;
    }

    public void cancelTreatment(int clientId, int treatmentId, boolean clientCancelled, String cancellationReason, UserManager userManager) throws TreatmentNotFoundException {
        Treatment treatment = getTreatment(treatmentId);
        treatment.setCancelled(true);
        treatment.setCancellationReason(cancellationReason);

        Client client;
        try {
            client = userManager.getClient(clientId);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return;
        }

        double refundedPrice = treatment.getPrice();
        if (clientCancelled) {
            salonManager.addIncome(refundedPrice * 0.1);
            refundedPrice *= 0.9;
            treatment.setStatus(Treatment.Status.CANCELLED_BY_CLIENT);
        } else {
            salonManager.addIncome(refundedPrice);
            treatment.setStatus(Treatment.Status.CANCELLED_BY_SALON);
        }

        userManager.changeMoneySpent(client, refundedPrice);
        updateTreatment(treatment);
    }

    /**
     *     Finishes treatments that aren't marked as cancelled and that were scheduled before the current date
     */
    public void finishTreatments() {
        Calendar currentDate = Calendar.getInstance();
        for (Treatment treatment : treatments.values()) {
            if (treatment.getScheduledDate().before(currentDate) && !treatment.isCancelled()) {
                treatment.setStatus(Treatment.Status.FINISHED);
            }
        }
    }
    //endregion
}
