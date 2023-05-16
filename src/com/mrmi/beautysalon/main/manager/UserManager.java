package com.mrmi.beautysalon.main.manager;

import com.mrmi.beautysalon.main.entity.*;
import com.mrmi.beautysalon.main.exceptions.UserNotFoundException;

import java.util.*;

public class UserManager {
    private final Database database;
    private final HashMap<Integer, User> users;
    private final HashMap<Integer, Treatment> treatments;
    private final SalonManager salonManager;

    public UserManager(Database database, SalonManager salonManager) {
        this.database = database;
        this.treatments = database.getTreatments();
        this.users = database.getUsers();
        this.salonManager = salonManager;
    }

    //region User
    public void addUser(User user) {
        database.addUser(user);
    }

    public HashMap<Integer, User> getUsers() {
        return database.getUsers();
    }

    public void updateUser(int id, User user) throws UserNotFoundException {
        database.updateUser(id, user);
    }

    public void deleteUser(int id) throws UserNotFoundException {
        database.deleteUser(id);
    }
    //endregion

    //region Client
    public ArrayList<Client> getClients() {
        ArrayList<Client> clients = new ArrayList<>();
        for (User user : users.values()) {
            if (user instanceof Client) {
                clients.add((Client) user);
            }
        }

        return clients;
    }

    public Client getClient(int id) throws UserNotFoundException {
        if (!users.containsKey(id) || !(users.get(id) instanceof Client)) {
            throw new UserNotFoundException("User with id " + id + " not found.");
        }
        return (Client) users.get(id);
    }

    public int getClientIdByUsername(String username) throws UserNotFoundException {
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            if (entry.getValue() instanceof Client && entry.getValue().getUsername().equals(username)) {
                return entry.getKey();
            }
        }
        throw new UserNotFoundException("User with username " + username + " not found.");
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
        Calendar calendar = Calendar.getInstance();
        for (Map.Entry<Integer, Treatment> t : userTreatments.entrySet()) {
            if (t.getValue().getScheduledDate().after(calendar)) {
                dueTreatments.put(t.getKey(), t.getValue());
            }
        }

        return dueTreatments;
    }

    public HashMap<Integer, Treatment> getClientPastTreatments(String clientUsername) {
        HashMap<Integer, Treatment> pastTreatments = new HashMap<>();
        HashMap<Integer, Treatment> userTreatments = getClientTreatments(clientUsername);
        Calendar calendar = Calendar.getInstance();
        for (Map.Entry<Integer, Treatment> t : userTreatments.entrySet()) {
            if (t.getValue().getScheduledDate().before(calendar)) {
                pastTreatments.put(t.getKey(), t.getValue());
            }
        }

        return pastTreatments;
    }

    public void changeMoneySpent(int clientId, Client client, double refundedPrice) {
        try {
            client.setMoneySpent(client.getMoneySpent() + refundedPrice);
            client.setHasLoyaltyCard(client.getMoneySpent() >= salonManager.getLoyaltyThreshold());
            database.updateUser(clientId, client);
        } catch (UserNotFoundException ignored) {
        }
    }

    public void bookTreatment(Treatment treatment) {
        int clientId = -1;
        Client client = null;
        for (Map.Entry<Integer, User> entry : database.getUsers().entrySet()) {
            if (entry.getValue().getUsername().equals(treatment.getClientUsername())) {
                clientId = entry.getKey();
                client = (Client) entry.getValue();
                break;
            }
        }
        if (clientId == -1 || client == null) {
            return;
        }

        double price = treatment.getPrice();
        if (client.hasLoyaltyCard()) {
            price *= 0.9;
            treatment.setPrice(price);
        }

        database.addTreatment(treatment);
        salonManager.changeProfit(treatment.getPrice());
        changeMoneySpent(clientId, client, price);
    }
    //endregion

    //region Beautician
    public ArrayList<Beautician> getBeauticians() {
        ArrayList<Beautician> beauticians = new ArrayList<>();
        for (Map.Entry<Integer, User> u : users.entrySet()) {
            if (u.getValue().getClass().equals(Beautician.class)) {
                beauticians.add((Beautician) u.getValue());
            }
        }
        return beauticians;
    }

    public Beautician getBeautician(int id) throws UserNotFoundException {
        if (!users.containsKey(id) || !(users.get(id) instanceof Beautician)) {
            throw new UserNotFoundException("User with id " + id + " not found.");
        }
        return (Beautician) users.get(id);
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
        Calendar calendar = Calendar.getInstance();
        for (Map.Entry<Integer, Treatment> t : beauticianTreatments.entrySet()) {
            if (t.getValue().getScheduledDate().after(calendar)) {
                dueTreatments.put(t.getKey(), t.getValue());
            }
        }

        return dueTreatments;
    }

    public HashMap<Integer, Treatment> getBeauticianPastTreatments(String beauticianUsername) {
        HashMap<Integer, Treatment> pastTreatments = new HashMap<>();
        HashMap<Integer, Treatment> beauticianTreatments = getBeauticianTreatments(beauticianUsername);
        Calendar calendar = Calendar.getInstance();
        for (Map.Entry<Integer, Treatment> t : beauticianTreatments.entrySet()) {
            if (t.getValue().getScheduledDate().before(calendar)) {
                pastTreatments.put(t.getKey(), t.getValue());
            }
        }

        return pastTreatments;
    }

    public HashMap<Integer, Beautician> getBeauticiansByTreatmentTypeCategory(int treatmentTypeCategoryId) {
        HashMap<Integer, Beautician> beauticians = new HashMap<>();
        for (Map.Entry<Integer, User> u : users.entrySet()) {
            if (u.getValue().getClass().equals(Beautician.class)) {
                if (((Beautician) u.getValue()).getTreatmentTypeCategoryIDs().contains(treatmentTypeCategoryId)) {
                    beauticians.put(u.getKey(), (Beautician) u.getValue());
                }
            }
        }
        return beauticians;
    }

    public void teachTreatment(int id, byte treatmentTypeCategoryId) throws UserNotFoundException {
        Beautician beautician = getBeautician(id);
        beautician.addTreatmentTypeCategoryID(treatmentTypeCategoryId);
        database.updateUser(id, beautician);
    }

    public int getFinishedTreatments(String beauticianUsername) {
        int counter = 0;
        for (Treatment treatment : database.getTreatments().values()) {
            if (treatment.getBeauticianUsername().equals(beauticianUsername) && treatment.getStatus() == Treatment.Status.FINISHED) {
                counter++;
            }
        }

        return counter;
    }

    //endregion
}
