package com.mrmi.beautysalon.main.controller;

import com.mrmi.beautysalon.main.exceptions.TreatmentNotFoundException;
import com.mrmi.beautysalon.main.exceptions.TreatmentTypeNotFoundException;
import com.mrmi.beautysalon.main.exceptions.UserNotFoundException;
import com.mrmi.beautysalon.main.entity.*;

import java.util.*;
import java.util.stream.Stream;

public class TreatmentController {
    private final Database database;
    private final SalonController salonController;
    public TreatmentController (Database database, SalonController salonController) {
        this.database = database;
        this.salonController = salonController;
    }

    //region Treatment type category
    public void addTreatmentTypeCategory(TreatmentTypeCategory treatmentTypeCategory) {
        database.addTreatmentTypeCategory(treatmentTypeCategory);
    }

    public HashMap<Integer, TreatmentTypeCategory> getTreatmentTypeCategories() {
        return database.getTreatmentTypeCategories();
    }
    public String getTreatmentTypeCategoryName(int treatmentTypeCategoryId) {
        return database.getTreatmentTypeCategories().get(treatmentTypeCategoryId).getName();
    }
    public void updateTreatmentTypeCategory(int id, TreatmentTypeCategory treatmentTypeCategory) {
        database.updateTreatmentTypeCategory(id, treatmentTypeCategory);
    }
    public void deleteTreatmentTypeCategory(int id) {
        database.deleteTreatmentTypeCategory(id);
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
    //endregion

    //region Treatment type
    public void addTreatmentType(TreatmentType type) {
        database.addTreatmentType(type);
    }
    public HashMap<Integer, TreatmentType> getTreatmentTypes() {
        return database.getTreatmentTypes();
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
        database.deleteTreatmentType(id);
    }
    public List<Treatment> getTreatmentsSortedByBeauticians() {
        return database.getTreatments().values().stream().sorted(Comparator.comparing(Treatment::getBeauticianUsername)).toList();
    }

    public List<Treatment> getTreatmentsSortedByCancellationReason() {
        return database.getTreatments().values().stream().sorted(Comparator.comparing(Treatment::getCancellationReason)).toList();
    }
    public HashMap<Integer, TreatmentType> getTreatmentTypesByCategoryList(List<Integer> treatmentTypeCategoryIDs) {
        HashMap<Integer, TreatmentType> treatmentTypes = new HashMap<>();
        for (int i : treatmentTypeCategoryIDs) {
            treatmentTypes.put(i, treatmentTypes.get(i));
        }
        return treatmentTypes;
    }

    public Vector<String> getTreatmentTimeWindows(Date date, int treatmentTypeId, BeautySalon beautySalon) {
        HashMap<Integer, TreatmentType> treatmentTypes = database.getTreatmentTypes();
        HashMap<Integer, Treatment> treatments = database.getTreatments();

        if (!treatmentTypes.containsKey(treatmentTypeId)) {
            return new Vector<>();
        }
        byte duration = treatmentTypes.get(treatmentTypeId).getDuration();
        Vector<String> timeWindows = new Vector<>();
        Treatment previousTreatment = null;
        List<Treatment> sortedTreatments = treatments.values()
                .stream()
                .sorted(Comparator.comparing(Treatment::getScheduledDate))
                .filter(t -> t.getScheduledDate().getDate() == date.getDate())
                .toList();
        if (sortedTreatments.size() > 0) {
            for (Treatment treatment : sortedTreatments) {
                if (previousTreatment != null ) {
                    int previousHour = previousTreatment.getScheduledDate().getHours();
                    int currentHour = treatment.getScheduledDate().getHours();
                    if (currentHour - previousHour >= duration) {
                        for (int i = previousHour; duration + i <= currentHour && duration + i <= beautySalon.getSalonClosingHour(); i++) {
                            timeWindows.add(hourToString(i) + ":00 - " + hourToString(i + duration) + ":00");
                        }
                    }
                }
                previousTreatment = treatment;
            }
        } else {
            for (int i = beautySalon.getSalonOpeningHour(); duration + i <= beautySalon.getSalonClosingHour(); i++) {
                timeWindows.add(hourToString(i) + ":00 - " + hourToString(i + duration) + ":00");
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
    public void updateTreatment(Treatment treatment, Date date, int treatmentTypeId, String clientUsername, String beauticianUsername) {
        database.updateTreatment(treatment, date, treatmentTypeId, clientUsername, beauticianUsername);
    }

    public void updateTreatment(Treatment treatment, int id) {
        database.updateTreatment(treatment, id);
    }

    public void deleteTreatment(int id) {
        database.deleteTreatment(id);
    }
    public HashMap<String, Integer> getStatusCountMap() {
        HashMap<Integer, Treatment> treatments = database.getTreatments();
        HashMap<String, Integer> statusCountMap = new HashMap<>();
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

    public void cancelTreatment(int treatmentId, boolean clientCancelled, String cancellationReason, UserController userController, Double loyaltyThreshold) {
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
            client = userController.getClientByUsername(t.getClientUsername());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return;
        }

        double refundedPrice = t.getPrice();
        if (clientCancelled) {
            salonController.changeProfit(refundedPrice * 0.1);
            refundedPrice *= 0.9;
            t.setStatus("Cancelled by client");
        } else {
            salonController.changeProfit(refundedPrice);
            t.setStatus("Cancelled by salon");
        }

        updateTreatment(t, treatmentId);
        userController.changeMoneySpent(t.getClientUsername(), client, refundedPrice, loyaltyThreshold);
        try {
            TreatmentType treatmentType = getTreatmentTypeById(t.getTreatmentTypeId());
            treatmentType.changeTimesBooked(-1);
            treatmentType.changeProfit(-refundedPrice);
            updateTreatmentType(t.getTreatmentTypeId(), treatmentType);
        } catch (TreatmentTypeNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void bookTreatment(Treatment treatment, Beautician beautician, SalonController salonController) {
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
        addTreatment(treatment);
        salonController.changeProfit(treatment.getPrice());
    }
    //endregion
}
