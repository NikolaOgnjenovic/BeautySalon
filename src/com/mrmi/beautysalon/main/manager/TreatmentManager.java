package com.mrmi.beautysalon.main.manager;

import com.mrmi.beautysalon.main.exceptions.TreatmentNotFoundException;
import com.mrmi.beautysalon.main.exceptions.TreatmentTypeNotFoundException;
import com.mrmi.beautysalon.main.exceptions.UserNotFoundException;
import com.mrmi.beautysalon.main.entity.*;

import java.util.*;
import java.util.stream.Collectors;

public class TreatmentManager {
    private final Database database;
    private final SalonManager salonManager;

    public TreatmentManager(Database database,SalonManager salonManager) {
        this.database = database;
        this.salonManager = salonManager;
    }

    //region Treatment type category
    public void addTreatmentTypeCategory(String name) {
        TreatmentTypeCategory category = new TreatmentTypeCategory(-1, name);
        database.addTreatmentTypeCategory(category);
    }

    public HashMap<Integer, TreatmentTypeCategory> getTreatmentTypeCategories() {
        return database.getTreatmentTypeCategories();
    }

    public TreatmentTypeCategory getTreatmentTypeCategory(int id) {
        return database.getTreatmentTypeCategories().get(id);
    }

    public HashMap<Integer, TreatmentTypeCategory> getAvailableTreatmentTypeCategories() {
        return (HashMap<Integer, TreatmentTypeCategory>) database.getTreatmentTypeCategories().entrySet()
                .stream()
                .filter(category -> !category.getValue().isDeleted())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    public String getTreatmentTypeCategoryName(int treatmentTypeCategoryId) {
        TreatmentTypeCategory category = database.getTreatmentTypeCategories().get(treatmentTypeCategoryId);
        if (category == null) {
            return "Deleted treatment category";
        }
        return category.getName();
    }

    public String getTreatmentTypeCategoryNameByTypeName(int treatmentTypeId) {
        TreatmentType treatmentType = database.getTreatmentTypes().get(treatmentTypeId);
        if (treatmentType == null) {
            return "Deleted treatment type";
        }

        TreatmentTypeCategory treatmentTypeCategory = database.getTreatmentTypeCategories().get(treatmentType.getCategoryId());
        if (treatmentTypeCategory == null) {
            return "Deleted treatment type category";
        }

        return treatmentTypeCategory.getName();
    }

    public int getTreatmentTypeCategoryIdByName(String name) {
        for (Map.Entry<Integer, TreatmentTypeCategory> entry : getTreatmentTypeCategories().entrySet()) {
            if (entry.getValue().getName().equals(name)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public void updateTreatmentTypeCategory(int id, TreatmentTypeCategory treatmentTypeCategory) {
        database.updateTreatmentTypeCategory(id, treatmentTypeCategory);
    }

    public void updateTreatmentTypeCategory(int id, TreatmentTypeCategory treatmentTypeCategory, String name) {
        treatmentTypeCategory.setName(name);
        database.updateTreatmentTypeCategory(id, treatmentTypeCategory);
    }

    public void deleteTreatmentTypeCategory(int id) {
        TreatmentTypeCategory category = database.getTreatmentTypeCategories().get(id);
        category.setDeleted(true);
        updateTreatmentTypeCategory(id, category);

        for (Map.Entry<Integer, TreatmentType> entry : database.getTreatmentTypes().entrySet()) {
            if (entry.getValue().getCategoryId() == id) {
                entry.getValue().setDeleted(true);
                updateTreatmentType(entry.getKey(), entry.getValue());
            }
        }
    }

    public int[] getCategoryProfitByMonths(int treatmentTypeCategoryId) {
        HashMap<Integer, TreatmentType> treatmentTypes = database.getTreatmentTypes();
        HashMap<Integer, Treatment> treatments = database.getTreatments();

        int[] profit = new int[12];
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        for (Treatment t : treatments.values()) {
            if (treatmentTypeCategoryId == treatmentTypes.get(t.getTreatmentTypeId()).getCategoryId()) {
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
        database.addTreatmentType(new TreatmentType(-1, name, price, treatmentTypeCategoryId, duration));
    }
    public HashMap<Integer, TreatmentType> getTreatmentTypes() {
        return database.getTreatmentTypes();
    }

    public HashMap<Integer, TreatmentType> getAvailableTreatmentTypes() {
        return (HashMap<Integer, TreatmentType>) database.getTreatmentTypes().entrySet()
                .stream()
                .filter(type -> !type.getValue().isDeleted())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public TreatmentType getTreatmentTypeById(int id) throws TreatmentTypeNotFoundException {
        HashMap<Integer, TreatmentType> treatmentTypes = database.getTreatmentTypes();
        if (!treatmentTypes.containsKey(id)) {
            throw new TreatmentTypeNotFoundException("Treatment type with id " + id + " not found.");

        }
        return treatmentTypes.get(id);
    }

    public void updateTreatmentType(int id, TreatmentType type) {
        database.updateTreatmentType(id, type);
    }

    public void updateTreatmentType(int id, TreatmentType type, String name, double price, int treatmentTypeCategoryId, int duration) {
        type.setName(name);
        type.setPrice(price);
        type.setCategoryId(treatmentTypeCategoryId);
        type.setDuration(duration);
        database.updateTreatmentType(id, type);
    }

    public void deleteTreatmentType(int id) {
        TreatmentType type = database.getTreatmentTypes().get(id);
        type.setDeleted(true);
        updateTreatmentType(id, type);
    }
    public List<Treatment> getTreatmentsSortedByBeauticians() {
        List<Treatment> list = new ArrayList<>(database.getTreatments().values());
        list.sort(Comparator.comparing(Treatment::getBeauticianUsername));
        return list;
    }

    public List<Treatment> getTreatmentsSortedByCancellationReason() {
        List<Treatment> list = new ArrayList<>(database.getTreatments().values());
        list.sort(Comparator.comparing(Treatment::getCancellationReason));
        return list;
    }

    public int getTreatmentTypeIdByName(String name) {
        for (Map.Entry<Integer, TreatmentType> entry : getTreatmentTypes().entrySet()) {
            if (entry.getValue().getName().equals(name)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public String getTreatmentTypeName(int treatmentTypeId) {
       TreatmentType treatmentType = database.getTreatmentTypes().get(treatmentTypeId);
       if (treatmentType == null) {
           return "Deleted treatment type";
       }
       return treatmentType.getName();
    }

    public Vector<String> getTreatmentTimeWindows(Calendar calendar, int treatmentTypeId, SalonManager salonManager) {
        HashMap<Integer, TreatmentType> treatmentTypes = database.getTreatmentTypes();
        HashMap<Integer, Treatment> treatments = database.getTreatments();

        if (!treatmentTypes.containsKey(treatmentTypeId)) {
            return new Vector<>();
        }
        int duration = treatmentTypes.get(treatmentTypeId).getDuration();
        int roundedDuration = (duration / 60 + 1);
        Vector<String> timeWindows = new Vector<>();
        Treatment previousTreatment = null;
        List<Treatment> sortedTreatments = new ArrayList<>(treatments.values());
        sortedTreatments.sort(Comparator.comparing(Treatment::getScheduledDate));
        sortedTreatments = sortedTreatments.stream().filter(t -> t.getScheduledDate().get(Calendar.DATE) == calendar.get(Calendar.DATE)).collect(Collectors.toList());
        if (sortedTreatments.size() > 0) {
            for (Treatment treatment : sortedTreatments) {
                if (previousTreatment != null) {
                    int previousHour = previousTreatment.getScheduledDate().get(Calendar.HOUR);
                    int currentHour = treatment.getScheduledDate().get(Calendar.HOUR);
                    if (currentHour - previousHour >= roundedDuration) {
                        for (int i = previousHour; duration + i <= currentHour && duration + i <= salonManager.getClosingHour(); i++) {
                            timeWindows.add(hourToString(i) + ":00 - " + hourToString(i + duration / 60) + ":" + hourToString(duration % 60));
                        }
                    }
                }
                previousTreatment = treatment;
            }
        } else {
            for (int i = salonManager.getOpeningHour(); roundedDuration + i <= salonManager.getClosingHour(); i++) {
                timeWindows.add(hourToString(i) + ":00 - " + hourToString(i + duration / 60) + ":" + hourToString(duration % 60));
            }
        }

        return timeWindows;
    }

    private String hourToString(int h) {
        if (h >= 10) {
            return String.valueOf(h);
        }
        return "0" + h;
    }

    public int getTimesBooked(int treatmentTypeId) {
        Collection<Treatment> treatments = database.getTreatments().values();
        int counter = 0;
        for (Treatment t : treatments) {
            if (t.getTreatmentTypeId() == treatmentTypeId) {
                counter++;
            }
        }

        return counter;
    }

    public double getProfit(int treatmentTypeId) {
        Collection<Treatment> treatments = database.getTreatments().values();
        double profit = 0;
        for (Treatment t : treatments) {
            if (t.getTreatmentTypeId() == treatmentTypeId) {
                profit += t.getPrice();
            }
        }

        return profit;
    }
    //endregion

    //region Treatment

    public HashMap<Integer, Treatment> getTreatments() {
        return database.getTreatments();
    }

    public Treatment getTreatment(int id) throws TreatmentNotFoundException {
        HashMap<Integer, Treatment> treatments = database.getTreatments();
        if (!treatments.containsKey(id)) {
            throw new TreatmentNotFoundException("Treatment with id " + id + " not found.");
        }
        return treatments.get(id);
    }

    public void updateTreatment(int id, Treatment treatment) throws TreatmentNotFoundException {
        if (!database.getTreatments().containsKey(id)) {
            throw new TreatmentNotFoundException("Treatment with id " + id + " not found.");
        }
        database.updateTreatment(treatment, id);
    }

    public void updateTreatment(int id, Treatment treatment, int treatmentTypeId, Calendar scheduledDate, Double price, String clientUsername, String beauticianUsername, Treatment.Status status) {
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

        database.updateTreatment(treatment, id);
    }

    public void deleteTreatment(int id) throws TreatmentNotFoundException {
        if (!database.getTreatments().containsKey(id)) {
            throw new TreatmentNotFoundException("Treatment with id " + id + " not found.");
        }
        database.deleteTreatment(id);
    }

    public HashMap<Treatment.Status, Integer> getStatusCountMap() {
        ArrayList<Treatment> treatmentsByStatus = new ArrayList<>(database.getTreatments().values());
        treatmentsByStatus.sort(Comparator.comparing(Treatment::getStatus));

        HashMap<Treatment.Status, Integer> statusCountMap = new HashMap<>();
        if (treatmentsByStatus.size() < 1) {
            return statusCountMap;
        }

        Treatment.Status currentStatus = treatmentsByStatus.get(0).getStatus();
        int counter = 1;
        for (int i = 0; i < treatmentsByStatus.size(); i++) {
            if (currentStatus != treatmentsByStatus.get(0).getStatus()) {
                statusCountMap.put(currentStatus, counter);
                currentStatus = treatmentsByStatus.get(0).getStatus();
                counter = 0;
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

    public void cancelTreatment(int clientId, int treatmentId, boolean clientCancelled, String cancellationReason, UserManager userManager) {
        Treatment t;
        try {
            t = getTreatment(treatmentId);
        } catch (TreatmentNotFoundException e) {
            e.printStackTrace();
            return;
        }
        t.setCancelled(true);
        t.setCancellationReason(cancellationReason);

        Client client;
        try {
            client = userManager.getClientByUsername(t.getClientUsername());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return;
        }

        double refundedPrice = t.getPrice();
        if (clientCancelled) {
            salonManager.changeProfit(refundedPrice * 0.1);
            refundedPrice *= 0.9;
            t.setStatus(Treatment.Status.CANCELLED_BY_CLIENT);
        } else {
            salonManager.changeProfit(refundedPrice);
            t.setStatus(Treatment.Status.CANCELLED_BY_SALON);
        }

        try {
            updateTreatment(treatmentId, t);
        } catch (TreatmentNotFoundException ignored) {

        }

        userManager.changeMoneySpent(clientId, client, refundedPrice);
        try {
            TreatmentType treatmentType = getTreatmentTypeById(t.getTreatmentTypeId());
            updateTreatmentType(t.getTreatmentTypeId(), treatmentType);
        } catch (TreatmentTypeNotFoundException e) {
            e.printStackTrace();
        }
    }
    //endregion
}
