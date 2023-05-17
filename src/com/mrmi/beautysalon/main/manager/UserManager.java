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
        int id = database.getNextUserId();
        users.put(id, user);
        database.addUser(id, user);
    }

    public HashMap<Integer, User> getUsers() {
        return users;
    }

    public void updateUser(User user) {
        users.put(user.getId(), user);
        database.updateUser(user);
    }

    public void deleteUser(int id) throws UserNotFoundException {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("User with id " + id + " not found.");
        }
        users.remove(id);
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

    public void changeMoneySpent(Client client, double refundedPrice) {
        client.setMoneySpent(client.getMoneySpent() + refundedPrice);
        client.setHasLoyaltyCard(client.getMoneySpent() >= salonManager.getLoyaltyThreshold());
        updateUser(client);
    }

    public void bookTreatment(Treatment treatment, TreatmentManager treatmentManager) {
        Client client = null;
        for (User user : users.values()) {
            if (user.getUsername().equals(treatment.getClientUsername()) && user instanceof Client) {
                client = (Client) user;
                break;
            }
        }
        if (client == null) {
            return;
        }

        double price = treatment.getPrice();
        if (client.hasLoyaltyCard()) {
            price *= 0.9;
            treatment.setPrice(price);
        }

        treatmentManager.addTreatment(treatment);
        salonManager.addIncome(treatment.getPrice());
        changeMoneySpent(client, price);
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
        database.updateUser(beautician);
    }

    public int getFinishedTreatments(String beauticianUsername) {
        int counter = 0;
        for (Treatment treatment : treatments.values()) {
            if (treatment.getBeauticianUsername().equals(beauticianUsername) && treatment.getStatus() == Treatment.Status.FINISHED) {
                counter++;
            }
        }

        return counter;
    }

    //endregion
}
