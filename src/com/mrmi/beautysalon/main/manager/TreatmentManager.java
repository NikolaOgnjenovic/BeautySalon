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
    public TreatmentManager(Database database, SalonManager salonManager) {
        this.database = database;
        this.salonManager = salonManager;
    }

    //region Treatment type category
    public void addTreatmentTypeCategory(TreatmentTypeCategory treatmentTypeCategory) {
        database.addTreatmentTypeCategory(treatmentTypeCategory);
    }

    public HashMap<Integer, TreatmentTypeCategory> getTreatmentTypeCategories() {
        return database.getTreatmentTypeCategories();
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
    public void updateTreatmentTypeCategory(int id, TreatmentTypeCategory treatmentTypeCategory) {
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
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        for (Treatment t : treatments.values()) {
            if (treatmentTypeCategoryId == treatmentTypes.get(t.getTreatmentTypeId()).getCategoryId()) {
                if (t.getScheduledDate().after(cal.getTime())) {
                    profit[t.getScheduledDate().getMonth()] += t.getPrice();
                }
            }
        }

        return profit;
    }

    public int getTreatmentTypeCategoryIdByName(String name) {
        for (Map.Entry<Integer, TreatmentTypeCategory> entry : getTreatmentTypeCategories().entrySet()) {
            if (entry.getValue().getName().equals(name)) {
                return entry.getKey();
            }
        }
        return -1;
    }
    //endregion

    //region Treatment type
    public void addTreatmentType(TreatmentType type) {
        database.addTreatmentType(type);
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
    public HashMap<Integer, TreatmentType> getTreatmentTypesByCategoryList(List<Integer> treatmentTypeCategoryIDs) {
        HashMap<Integer, TreatmentType> treatmentTypes = new HashMap<>();
        for (int i : treatmentTypeCategoryIDs) {
            treatmentTypes.put(i, treatmentTypes.get(i));
        }
        return treatmentTypes;
    }

    public int getTreatmentTypeIdByName(String name) {
        for (Map.Entry<Integer, TreatmentType> entry : getTreatmentTypes().entrySet()) {
            if (entry.getValue().getName().equals(name)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public Vector<String> getTreatmentTimeWindows(Date date, int treatmentTypeId, SalonManager salonManager) {
        HashMap<Integer, TreatmentType> treatmentTypes = database.getTreatmentTypes();
        HashMap<Integer, Treatment> treatments = database.getTreatments();

        if (!treatmentTypes.containsKey(treatmentTypeId)) {
            return new Vector<>();
        }
        byte duration = treatmentTypes.get(treatmentTypeId).getDuration();
        int roundedDuration = (duration / 60 + 1);
        //int roundedDuration = (duration + (duration / 60 + 1) * 60 - duration) / 60; // 45 minutes -> 60 minutes (45 + (0 + 1) * 60 - 45)
        Vector<String> timeWindows = new Vector<>();
        Treatment previousTreatment = null;
        List<Treatment> sortedTreatments = new ArrayList<>(treatments.values());
        sortedTreatments.sort(Comparator.comparing(Treatment::getScheduledDate));
        sortedTreatments = sortedTreatments.stream().filter(t -> t.getScheduledDate().getDate() == date.getDate()).collect(Collectors.toList());
        if (sortedTreatments.size() > 0) {
            for (Treatment treatment : sortedTreatments) {
                if (previousTreatment != null) {
                    int previousHour = previousTreatment.getScheduledDate().getHours();
                    int currentHour = treatment.getScheduledDate().getHours();
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
    //endregion

    //region Treatment
    public void addTreatment(Treatment treatment) {
        database.addTreatment(treatment);
    }

    public Treatment getTreatment(int id) throws TreatmentNotFoundException {
        HashMap<Integer, Treatment> treatments = database.getTreatments();
        if (!treatments.containsKey(id)) {
            throw new TreatmentNotFoundException("Treatment with id " + id + " not found.");
        }
        return treatments.get(id);
    }

    public HashMap<Integer, Treatment> getTreatments() {
        return database.getTreatments();
    }

    public void updateTreatment(Treatment treatment, int id) {
        database.updateTreatment(treatment, id);
    }

    public void deleteTreatment(int id) {
        database.deleteTreatment(id);
    }
    public HashMap<Treatment.Status, Integer> getStatusCountMap() {
        HashMap<Integer, Treatment> treatments = database.getTreatments();
        HashMap<Treatment.Status, Integer> statusCountMap = new HashMap<>();
        long currentTime = new Date().getTime();
        currentTime -= 30L * 24 * 3600 * 1000;
        for (Treatment t : treatments.values()) {
            if (t.getScheduledDate().getTime() > currentTime) {
                if (!statusCountMap.containsKey(t.getStatus())) {
                    statusCountMap.put(t.getStatus(), 0);
                } else {
                    statusCountMap.put(t.getStatus(), statusCountMap.get(t.getStatus()) + 1);
                }
            }
        }

        return statusCountMap;
    }

    public Double getTotalCost(HashMap<Integer, Treatment> treatments) {
        Double cost = 0d;
        for (Treatment t : treatments.values()) {
            cost += t.getPrice();
        }
        return cost;
    }

    public void cancelTreatment(int treatmentId, boolean clientCancelled, String cancellationReason, UserManager userManager, Double loyaltyThreshold) {
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

        updateTreatment(t, treatmentId);
        userManager.changeMoneySpent(t.getClientUsername(), client, refundedPrice, loyaltyThreshold);
        try {
            TreatmentType treatmentType = getTreatmentTypeById(t.getTreatmentTypeId());
            treatmentType.changeTimesBooked(-1);
            treatmentType.changeProfit(-refundedPrice);
            updateTreatmentType(t.getTreatmentTypeId(), treatmentType);
        } catch (TreatmentTypeNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void bookTreatment(Treatment treatment, Beautician beautician, SalonManager salonManager) {
        HashMap<Integer, TreatmentTypeCategory> treatmentTypeCategories = database.getTreatmentTypeCategories();
        try {
            TreatmentType treatmentType = getTreatmentTypeById(treatment.getTreatmentTypeId());
            treatmentType.changeTimesBooked(1);
            treatmentType.changeProfit(treatment.getPrice());
            TreatmentTypeCategory category = treatmentTypeCategories.get(treatmentType.getCategoryId());
            category.setProfit(category.getProfit() + treatment.getPrice());
            database.updateTreatmentType(treatment.getTreatmentTypeId(), treatmentType);
            beautician.setFinishedTreatments(beautician.getFinishedTreatments() + 1);
        } catch (TreatmentTypeNotFoundException e) {
            e.printStackTrace();
            return;
        }

        database.addTreatment(treatment);
        salonManager.changeProfit(treatment.getPrice());
    }
    //endregion
}
