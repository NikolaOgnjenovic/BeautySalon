package com.mrmi.beautysalon.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Database {
    private List<User> users;
    private List<Treatment> treatments;
    public static int salonIncome = 0;

    public static User CurrentUser;

    private List<TreatmentType> treatmentTypes;

    public static int treatmentTypeId = 0;
    public enum TREATMENT_STATUSES {
        CANCELLED,
        FINISHED,
        CANCELLED_BY_CLIENT,
        CANCELLED_BY_SALON,
        DID_NOT_ARRIVE
    }

    //region Database data & static variables
    public Database() {
        // TODO: replace with .csv data
        loadHardcodedData();
    }

    private void loadHardcodedData() {
        this.users = new ArrayList<>();
        this.users.add(new Manager("manager", "manager", "Manager", "McGee", false, "1111", "ManagerStreet 50"));
        this.treatments = new ArrayList<>();
        this.treatmentTypes = new ArrayList<>();
    }

    public static void changeProfit(double profit) {
        salonIncome += profit;
    }

    public void login(String username, String password) {
        for (User u : users) {
            if (u.getPassword().equals(password) && u.getUsername().equals(username)) {
                CurrentUser = u;
                return;
            }
        }

        CurrentUser = null;
    }

    public void logout() {
        CurrentUser = null;
    }
    //endregion

    //region User
    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }

    public void editUser(User user) {
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) {
                u = user;
                return;
            }
        }
    }
    //endregion

    //region Manager
    //endregion

    //region Employee
    public List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList<>();
        for (User u : users) {
            if (u.getClass().isInstance(Employee.class)) {
                employees.add((Employee) u);
            }
        }
        return employees;
    }
    //endregion

    //region Beautician
    public List<Treatment> getBeauticianTreatments(String beauticianUsername) {
        List<Treatment> beauticianTreatments = new ArrayList<>();
        for (Treatment t : treatments) {
            if (t.getBeauticianUsername().equals(beauticianUsername)) {
                beauticianTreatments.add(t);
            }
        }

        return beauticianTreatments;
    }
    //endregion

    //region Receptionist
    //endregion

    //region Client
    public boolean clientExists(String username) {
        for (User u : users) {
            if (u.getClass().equals(Client.class) && u.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public Client getClientByUsername(String username) {
        for (User u : users) {
            if (u.getClass().equals(Client.class) && u.getUsername().equals(username)) {
                return (Client) u;
            }
        }
        return null;
    }

    public List<Client> getLoyalClients() {
        List<Client> clients = new ArrayList<>();
        for(User u : users) {
            if (u.getClass().equals(Client.class)) {
                clients.add((Client) u);
            }
        }

        return clients;
    }

    public List<Treatment> getClientTreatments(String clientUsername) {
        List<Treatment> clientTreatments = new ArrayList<>();
        for (Treatment t : treatments) {
            if (t.getClientUsername().equals(clientUsername)) {
                clientTreatments.add(t);
            }
        }
        return clientTreatments;
    }
    //endregion

    //region Treatment
    public Treatment getTreatmentById (int id) {
        for (Treatment t : treatments) {
            if (t.getId() == id) {
                return t;
            }
        }

        return null;
    }

    public void updateTreatment(Treatment treatment, Date date, TreatmentType treatmentType, String clientUsername, String beauticianUsername) {
        treatment.setScheduledDate(date);
        treatment.setType(treatmentType);
        treatment.setClientUsername(clientUsername);
        treatment.setBeauticianUsername(beauticianUsername);
        // TODO: SAVE
    }
    //endregion

    //region Treatment types
    public List<TreatmentType> getTreatmentTypes() {
        return treatmentTypes;
    }

    public TreatmentType getTreatmentTypeById(int id) {
        for (TreatmentType t : treatmentTypes) {
            if (t.getId() == id) {
                return t;
            }
        }

        return null;
    }
    //endregion
}
