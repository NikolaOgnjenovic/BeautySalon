package com.mrmi.beautysalon.main.manager;

import com.mrmi.beautysalon.main.exceptions.TreatmentNotFoundException;
import com.mrmi.beautysalon.main.exceptions.TreatmentTypeCategoryNotFoundException;
import com.mrmi.beautysalon.main.exceptions.TreatmentTypeNotFoundException;
import com.mrmi.beautysalon.main.entity.*;

import java.util.*;
import java.util.stream.Collectors;

public class TreatmentManager {
    private final Database database;
    private final SalonManager salonManager;
    private final HashMap<Integer, TreatmentTypeCategory> categories;
    private final HashMap<Integer, TreatmentType> types;
    private final HashMap<Integer, Treatment> treatments;

    public TreatmentManager(Database database, SalonManager salonManager) {
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

    public String getTreatmentTypeCategoryName(int categoryId) throws TreatmentTypeCategoryNotFoundException {
        TreatmentTypeCategory category = getTreatmentTypeCategory(categoryId);
        return category.getName();
    }

    public String getTreatmentTypeCategoryNameByType(int typeId) throws TreatmentTypeNotFoundException, TreatmentTypeCategoryNotFoundException {
        TreatmentType type = getTreatmentType(typeId);

        return getTreatmentTypeCategoryName(type.getCategoryId());
    }

    public void updateTreatmentTypeCategory(TreatmentTypeCategory category) {
        categories.put(category.getId(), category);
        database.updateTreatmentTypeCategory(category);
    }

    public void updateTreatmentTypeCategoryName(TreatmentTypeCategory category, String name) {
        category.setName(name);
        database.updateTreatmentTypeCategory(category);
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

    public int[] getCategoryProfitByMonths(int categoryId) {
        int[] profit = new int[12];
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        for (Treatment t : treatments.values()) {
            if (categoryId == types.get(t.getTreatmentTypeId()).getCategoryId()) {
                if (t.getScheduledDate().after(calendar)) {
                    profit[t.getScheduledDate().get(Calendar.MONTH)] += t.getPrice();
                }
            }
        }

        return profit;
    }
    //endregion

    //region Treatment type
    public void addTreatmentType(String name, float price, int categoryId, int duration) {
        int id = database.getNextTreatmentTypeId();
        database.addTreatmentType(id, new TreatmentType(name, price, categoryId, duration));
    }

    public HashMap<Integer, TreatmentType> getTreatmentTypes() {
        return types;
    }

    /**
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

    public void updateTreatmentType(TreatmentType type, String name, float price, int categoryId, int duration) {
        type.setName(name);
        type.setPrice(price);
        type.setCategoryId(categoryId);
        type.setDuration(duration);
        database.updateTreatmentType(type.getId(), type);
    }

    public void deleteTreatmentType(int id) throws TreatmentTypeNotFoundException {
        TreatmentType type = getTreatmentType(id);
        type.setDeleted(true);
        updateTreatmentType(type);
    }

    public String getTreatmentTypeName(int typeId) throws TreatmentTypeNotFoundException {
        TreatmentType type = getTreatmentType(typeId);
        return type.getName();
    }

    /**
     * @param selectedDate The date on which the time slots are being calculated
     * @param typeId       The id of the treatment type
     * @param salonManager Used for the opening & closing hour
     * @return a Vector of Strings which contains available time slots for the treatment
     */
    public Vector<String> getAvailableTimeSlots(UserManager userManager, String beauticianUsername, Calendar selectedDate, int typeId, SalonManager salonManager) {
        if (!types.containsKey(typeId)) {
            return new Vector<>();
        }

        // Round the treatment duration (minutes) to hours
        int duration = types.get(typeId).getDuration();
        int roundedDurationHour = duration / 60 + 1;
        Vector<String> availableTimeSlots = new Vector<>();

        // Get all treatments for the given beautician, filter them by the given date, sort them by their date,
        List<Treatment> sortedTreatments = new ArrayList<>(userManager.getBeauticianTreatments(beauticianUsername).values())
                .stream().filter(t -> t.getScheduledDate().get(Calendar.DATE) == selectedDate.get(Calendar.DATE))
                .sorted(Comparator.comparing(Treatment::getScheduledDate)).collect(Collectors.toList());

        // Loop from the salon's opening hour to the closing hour, increasing i by the duration of the treatment in hours
        //for (int i = salonManager.getOpeningHour(); i < salonManager.getClosingHour(); i += roundedDurationHour) {
        for (int i = salonManager.getOpeningHour(); i < salonManager.getClosingHour(); i += 1) {
            int currentHour = i;
            // If any of the filtered treatments' duration overlaps with the current hour, continue
            if (sortedTreatments.stream().anyMatch(treatment -> {
                    int hourOfDay = treatment.getScheduledDate().get(Calendar.HOUR_OF_DAY);
                    return (hourOfDay == currentHour) || ((hourOfDay < currentHour) && ((hourOfDay + roundedDurationHour) > currentHour));
                }
            )) {
                continue;
            }
            // Else add the current time slot for the treatment
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

    public int getTimesBooked(int typeId) {
        int counter = 0;
        for (Treatment t : treatments.values()) {
            if (t.getTreatmentTypeId() == typeId) {
                counter++;
            }
        }

        return counter;
    }

    public float getProfit(int typeId) {
        float profit = 0;
        for (Treatment t : treatments.values()) {
            if (t.getTreatmentTypeId() == typeId) {
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

    public void updateTreatment(Treatment treatment, int typeId, Calendar scheduledDate, Float price, String clientUsername, String beauticianUsername, Treatment.Status status) throws TreatmentNotFoundException {
        treatment.setTreatmentTypeId(typeId);
        treatment.setScheduledDate(scheduledDate);
        treatment.setPrice(price);
        treatment.setClientUsername(clientUsername);
        treatment.setBeauticianUsername(beauticianUsername);
        treatment.setStatus(status);
        if (status != Treatment.Status.CANCELLED_BY_CLIENT && status != Treatment.Status.CANCELLED_BY_SALON) {
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

    public float getTotalCost(HashMap<Integer, Treatment> treatments) {
        float cost = 0;
        for (Treatment t : treatments.values()) {
            cost += t.getPrice();
        }
        return ((int) cost * 100) / 100f;
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

    public void cancelTreatment(int treatmentId, boolean clientCancelled, String cancellationReason) throws TreatmentNotFoundException {
        Treatment treatment = getTreatment(treatmentId);
        treatment.setCancellationReason(cancellationReason);
        treatment.setCancellationDate(Calendar.getInstance());

        if (clientCancelled) {
            treatment.setStatus(Treatment.Status.CANCELLED_BY_CLIENT);
        } else {
            treatment.setStatus(Treatment.Status.CANCELLED_BY_SALON);
        }

        updateTreatment(treatment);
    }

    /**
     * Finishes treatments that aren't marked as cancelled and that were scheduled before the current date
     */
    public void finishTreatments() {
        Calendar currentDate = Calendar.getInstance();
        for (Treatment treatment : treatments.values()) {
            if (treatment.getScheduledDate().before(currentDate) && !treatment.getStatus().equals(Treatment.Status.CANCELLED_BY_CLIENT) && !treatment.getStatus().equals(Treatment.Status.CANCELLED_BY_SALON)) {
                treatment.setStatus(Treatment.Status.FINISHED);
            }
        }
    }
    //endregion

    // Used for tables
    public float getBonus() {
        return salonManager.getBonus();
    }

    public float getMoneySpent(String clientUsername) {
        float total = 0;
        for (Treatment treatment : treatments.values()) {
            if (treatment.getClientUsername().equals(clientUsername)) {
                switch (treatment.getStatus()) {
                    case CANCELLED_BY_SALON:
                        //total -= treatment.getPrice();
                        break;
                    case CANCELLED_BY_CLIENT:
                        total += 0.1 * treatment.getPrice();
                        //total -= treatment.getPrice() * 0.9;
                        break;
                    default:
                        total += treatment.getPrice();
                        break;
                }
            }
        }

        return total;
    }

    public boolean hasLoyaltyCard(Client client) {
        return getMoneySpent(client.getUsername()) >= salonManager.getLoyaltyThreshold();
    }
}
