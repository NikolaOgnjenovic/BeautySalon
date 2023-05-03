package com.mrmi.beautysalon.main.controller;

import com.mrmi.beautysalon.main.entity.*;
import com.mrmi.beautysalon.main.exceptions.UserNotFoundException;

import java.util.*;

public class UserController {
    private final Database database;
    private final HashMap<String, User> users;
    private final HashMap<Integer, Treatment> treatments;
    private final TreatmentController treatmentController;
    private final SalonController salonController;

    public UserController(Database database, TreatmentController treatmentController, SalonController salonController) {
        this.database = database;
        this.treatments = database.getTreatments();
        this.users = database.getUsers();
        this.treatmentController = treatmentController;
        this.salonController = salonController;
    }

    //region User
    public void addUser(String username, User user) {
        database.addUser(username, user);
    }

    public HashMap<String, User> getUsers() {
        return database.getUsers();
    }

    public void updateUser(String username, User user) throws UserNotFoundException {
        database.updateUser(username, user);
    }

    public void deleteUser(String username) throws UserNotFoundException {
        database.deleteUser(username);
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }
    //endregion

    //region Client
    public Client getClientByUsername(String username) throws UserNotFoundException {
        if (!users.containsKey(username)) {
            throw new UserNotFoundException("Client with username " + username + " not found.");
        }
        return (Client) users.get(username);
    }

    public HashMap<Integer, Treatment> getClientTreatments(String clientUsername) {
        HashMap<Integer, Treatment> clientTreatments = new HashMap<>();
        for (Map.Entry<Integer, Treatment> t : treatments.entrySet()) {
            if (t.getValue().getClientUsername().equals(clientUsername)) {
                clientTreatments.put(t.getKey(), t.getValue());
            }
        }
        return clientTreatments;
    }

    public HashMap<Integer, Treatment> getClientDueTreatments(String clientUsername) {
        HashMap<Integer, Treatment> dueTreatments = new HashMap<>();
        HashMap<Integer, Treatment> userTreatments = getClientTreatments(clientUsername);
        Date currentDate = new Date();
        for (Map.Entry<Integer, Treatment> t : userTreatments.entrySet()) {
            if (t.getValue().getScheduledDate().after(currentDate)) {
                dueTreatments.put(t.getKey(), t.getValue());
            }
        }

        return dueTreatments;
    }

    public HashMap<Integer, Treatment> getClientPastTreatments(String clientUsername) {
        HashMap<Integer, Treatment> pastTreatments = new HashMap<>();
        HashMap<Integer, Treatment> userTreatments = getClientTreatments(clientUsername);
        Date currentDate = new Date();
        for (Map.Entry<Integer, Treatment> t : userTreatments.entrySet()) {
            if (t.getValue().getScheduledDate().before(currentDate)) {
                pastTreatments.put(t.getKey(), t.getValue());
            }
        }

        return pastTreatments;
    }

    public void changeMoneySpent(String clientUsername, Client client, double refundedPrice, double loyaltyThreshold) {
        try {
            client.setMoneySpent(client.getMoneySpent() + refundedPrice);
            client.setHasLoyaltyCard(client.getMoneySpent() >= loyaltyThreshold);
            database.updateUser(clientUsername, client);
        } catch (UserNotFoundException ignored) {

        }
    }

    public void bookTreatment(Treatment treatment, double loyaltyThreshold) {
        Client client = (Client) users.get(treatment.getClientUsername());
        Beautician beautician = (Beautician) users.get(treatment.getBeauticianUsername());
        double price = treatment.getPrice();
        if (client.hasLoyaltyCard()) {
            price *= 0.9;
            treatment.setPrice(price);
        }

        treatmentController.bookTreatment(treatment, beautician, salonController);
        changeMoneySpent(treatment.getClientUsername(), client, price, loyaltyThreshold);
    }
    //endregion

    //region Employee
    public void addEmployee(String username, Employee employee) {
        database.addUser(username, employee);
    }
    //endregion

    //region Beautician
    public HashMap<String, Beautician> getBeauticians() {
        HashMap<String, Beautician> beauticians = new HashMap<>();
        for (Map.Entry<String, User> u : users.entrySet()) {
            if (u.getValue().getClass().equals(Beautician.class)) {
                beauticians.put(u.getKey(), (Beautician) u.getValue());
            }
        }
        return beauticians;
    }

    public HashMap<Integer, Treatment> getBeauticianTreatments(String beauticianUsername) {
        HashMap<Integer, Treatment> beauticianTreatments = new HashMap<>();
        for (Map.Entry<Integer, Treatment> t : treatments.entrySet()) {
            if (t.getValue().getBeauticianUsername().equals(beauticianUsername)) {
                beauticianTreatments.put(t.getKey(), t.getValue());
            }
        }

        return beauticianTreatments;
    }

    public HashMap<Integer, Treatment> getBeauticianDueTreatments(String beauticianUsername) {
        HashMap<Integer, Treatment> dueTreatments = new HashMap<>();
        HashMap<Integer, Treatment> beauticianTreatments = getBeauticianTreatments(beauticianUsername);
        Date currentDate = new Date();
        for (Map.Entry<Integer, Treatment> t : beauticianTreatments.entrySet()) {
            if (t.getValue().getScheduledDate().after(currentDate)) {
                dueTreatments.put(t.getKey(), t.getValue());
            }
        }

        return dueTreatments;
    }

    public HashMap<Integer, Treatment> getBeauticianPastTreatments(String beauticianUsername) {
        HashMap<Integer, Treatment> pastTreatments = new HashMap<>();
        HashMap<Integer, Treatment> beauticianTreatments = getBeauticianTreatments(beauticianUsername);
        Date currentDate = new Date();
        for (Map.Entry<Integer, Treatment> t : beauticianTreatments.entrySet()) {
            if (t.getValue().getScheduledDate().before(currentDate)) {
                pastTreatments.put(t.getKey(), t.getValue());
            }
        }

        return pastTreatments;
    }

    public HashMap<String, Beautician> getBeauticiansByTreatmentType(byte treatmentTypeId) {
        HashMap<String, Beautician> beauticians = new HashMap<>();
        for (Map.Entry<String, User> u : users.entrySet()) {
            if (u.getValue().getClass().equals(Beautician.class)) {
                if (((Beautician) u.getValue()).getTreatmentTypeIDs().contains(treatmentTypeId)) {
                    beauticians.put(u.getKey(), (Beautician) u.getValue());
                }
            }
        }
        return beauticians;
    }

    public void teachTreatment(String username, byte treatmentTypeId) {
        Beautician b = (Beautician) users.get(username);
        b.addTreatmentTypeID(treatmentTypeId);
        try {
            database.updateUser(username, b);
        } catch (UserNotFoundException ignored) {
        }
    }
    // TODO
    public String getSchedule(String username) {
//        List<Treatment> treatments = database.getBeauticianTreatments(username).values().stream().sorted(Comparator.comparing(Treatment::getScheduledDate)).toList();
//        if (treatments.size() < 1) {
//            return "";
//        }
//        StringBuilder sb = new StringBuilder();
//        Date currentDate = treatments.get(0).getScheduledDate();
//        sb.append(currentDate);
//        sb.append("\n");
//        for (Treatment t : treatments) {
//            if (t.getScheduledDate().getDate() > currentDate.getDate()) {
//                currentDate = t.getScheduledDate();
//                sb.append(currentDate);
//            }
//            sb.append(t);
//            sb.append("\n");
//        }
//
//        return sb.toString();
        return "";
    }
    //endregion
}
