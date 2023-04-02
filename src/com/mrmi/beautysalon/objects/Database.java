package com.mrmi.beautysalon.objects;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Database {
    private List<User> users;
    private List<TreatmentType> treatmentTypes;
    private List<Treatment> treatments;
    public static User CurrentUser;

    private double salonIncome = 0;
    private int treatmentTypeId = 0;

    private int treatmentId = 0;
    private double loyaltyThreshold;
    public enum TREATMENT_STATUSES {
        CANCELLED,
        FINISHED,
        CANCELLED_BY_CLIENT,
        CANCELLED_BY_SALON,
        DID_NOT_ARRIVE
    }

    //region Database data & static variables
    public Database() {
        this.users = new ArrayList<>();
        this.treatments = new ArrayList<>();
        this.treatmentTypes = new ArrayList<>();
        loadData();
    }

    private void loadData() {
        checkDataFile();
        //testWrite();
        readUsersFile();
        readTreatmentsFile();
        readTreatmentTypesFile();
        readVariablesFile();
    }

    private void testWrite() {
        addUser(new Manager("manager", "manager", "Manager", "McGee", "F", "1234", "ManagerStreet 50", (byte) 7, (byte) 5, 0, 100000));
    }

    public void changeProfit(double profit) {
        salonIncome += profit;
        overwriteVariablesFile();
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

    public double getLoyaltyThreshold() {
        return loyaltyThreshold;
    }
    public void setLoyaltyThreshold(double loyaltyThreshold) {
        this.loyaltyThreshold = loyaltyThreshold;
        overwriteVariablesFile();
    }
    //endregion

    //region User
    public void addUser(User user) {
        users.add(user);
        writeUser(user);
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

    public List<Treatment> getClientDueTreatments(String clientUsername) {
        List<Treatment> dueTreatments = new ArrayList<>();
        List<Treatment> userTreatments = getClientTreatments(clientUsername);
        Date currentDate = new Date();
        for (Treatment t : userTreatments) {
            if (t.getScheduledDate().after(currentDate)) {
                dueTreatments.add(t);
            }
        }

        return dueTreatments;
    }

    public List<Treatment> getClientPastTreatments(String clientUsername) {
        List<Treatment> pastTreatments = new ArrayList<>();
        List<Treatment> userTreatments = getClientTreatments(clientUsername);
        Date currentDate = new Date();
        for (Treatment t : userTreatments) {
            if (t.getScheduledDate().before(currentDate)) {
                pastTreatments.add(t);
            }
        }

        return pastTreatments;
    }
    //endregion

    //region Employee
    public List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList<>();
        for (User u : users) {
            if (!u.getClass().isInstance(User.class)) {
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

    public List<Treatment> getBeauticianDueTreatments(String beauticianUsername) {
        List<Treatment> dueTreatments = new ArrayList<>();
        List<Treatment> beauticianTreatments = getBeauticianTreatments(beauticianUsername);
        Date currentDate = new Date();
        for (Treatment t : beauticianTreatments) {
            if (t.getScheduledDate().after(currentDate)) {
                dueTreatments.add(t);
            }
        }

        return dueTreatments;
    }

    public List<Treatment> getBeauticianPastTreatments(String beauticianUsername) {
        List<Treatment> pastTreatments = new ArrayList<>();
        List<Treatment> beauticianTreatments = getBeauticianTreatments(beauticianUsername);
        Date currentDate = new Date();
        for (Treatment t : beauticianTreatments) {
            if (t.getScheduledDate().before(currentDate)) {
                pastTreatments.add(t);
            }
        }

        return pastTreatments;
    }

    public List<Beautician> getBeauticians() {
        List<Beautician> beauticians = new ArrayList<>();
        for (User u : users) {
            if (u.getClass().equals(Beautician.class)) {
                beauticians.add((Beautician) u);
            }
        }
        return beauticians;
    }
    //endregion

    //region Receptionist
    //endregion

    //region Manager
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

    public void updateTreatment(Treatment treatment, Date date, int treatmentTypeId, String clientUsername, String beauticianUsername) {
        treatment.setScheduledDate(date);
        treatment.setTreatmentTypeId(treatmentTypeId);
        treatment.setClientUsername(clientUsername);
        treatment.setBeauticianUsername(beauticianUsername);
        overwriteTreatmentsFile();
    }

    public void addTreatment(Treatment treatment) {
        treatments.add(treatment);
    }

    public void bookTreatment(Treatment treatment) {
        addTreatment(treatment);
        writeTreatment(treatment);
        changeProfit(treatment.getPrice());
    }

    public int getNextTreatmentId() {
        return treatmentId++;
    }


    public void cancelTreatment(int treatmentId, boolean clientCancelled) {
        for (Treatment t : treatments) {
            if (t.getId() == treatmentId) {
                t.setCancelled(true);
                Client client = getClientByUsername(t.getClientUsername());

                /*
                Posle otkazanog tretmana na zahtev klijenta,
                klijentu se vraća 90% uplaćenog novca dok 10% kozmetički salon zadržava u cilju pokrivanja gubitaka.
                Ukoliko salon otkaže, klijentu se vraća 100% novca.
                Ukoliko se klijent ne pojavi, salon zadržava sav uplaćen novac.
                 */
                if (clientCancelled) {
                    client.changeMoneySpent(t.getPrice() * 0.9, this);
                    changeProfit(-t.getPrice() * 0.1);
                } else {
                    client.changeMoneySpent(t.getPrice(), this);
                    changeProfit(-t.getPrice());
                }
            }
        }
        overwriteTreatmentsFile();
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

    public void addTreatmentType(TreatmentType type) {
        treatmentTypes.add(type);
        writeTreatmentType(type);
        overwriteVariablesFile();
    }

    public int getNextTreatmentTypeId() {
        return treatmentTypeId++;
    }
    //endregion

    //region Users IO
    private void readUsersFile() {
        users = new ArrayList<>();
        String fileName = "data/users.txt";
        fileCheck(fileName);
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                String[] userData = line.split(",");
                switch (userData[0]) {
                    case "C" -> users.add(new Client(
                            userData[1].trim(),
                            userData[2].trim(),
                            userData[3].trim(),
                            userData[4].trim(),
                            userData[5].trim(),
                            userData[6].trim(),
                            userData[7].trim(),
                            Boolean.parseBoolean(userData[8]),
                            Double.parseDouble(userData[9])));
                    case "B" -> {
                        List<Byte> treatmentTypeIDs = new ArrayList<>();
                        String[] treatmentTypes = userData[12].split(";");
                        for(String s : treatmentTypes) {
                            treatmentTypeIDs.add(Byte.parseByte(s));
                        }
                        users.add(new Beautician(
                                userData[1].trim(),
                                userData[2].trim(),
                                userData[3].trim(),
                                userData[4].trim(),
                                userData[5].trim(),
                                userData[6].trim(),
                                userData[7].trim(),
                                treatmentTypeIDs,
                                Byte.parseByte(userData[8]),
                                Byte.parseByte(userData[9]),
                                Double.parseDouble(userData[10]),
                                Double.parseDouble(userData[11])));
                    }
                    case "R" -> users.add(new Receptionist(
                            userData[1].trim(),
                            userData[2].trim(),
                            userData[3].trim(),
                            userData[4].trim(),
                            userData[5].trim(),
                            userData[6].trim(),
                            userData[7].trim(),
                            Byte.parseByte(userData[8]),
                            Byte.parseByte(userData[9]),
                            Double.parseDouble(userData[10]),
                            Double.parseDouble(userData[11])));
                    case "M" -> users.add(new Manager(
                            userData[1].trim(),
                            userData[2].trim(),
                            userData[3].trim(),
                            userData[4].trim(),
                            userData[5].trim(),
                            userData[6].trim(),
                            userData[7].trim(),
                            Byte.parseByte(userData[8]),
                            Byte.parseByte(userData[9]),
                            Double.parseDouble(userData[10]),
                            Double.parseDouble(userData[11])));
                }
            }
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void writeUser(User user) {
        String fileName = "data/users.txt";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
            out.write(user.getFileString());
            out.write("\n");
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void overwriteUsersFile() {
        String fileName = "data/users.txt";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            for (User user : users) {
                out.write(user.getFileString());
                out.write("\n");
            }
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //endregion

    //region TreatmentTypes IO
    private void readTreatmentTypesFile() {
        treatmentTypes = new ArrayList<>();
        String fileName = "data/treatmentTypes.txt";
        fileCheck(fileName);
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                String[] data = line.split(",");
                treatmentTypes.add(new TreatmentType(data[0], Double.parseDouble(data[1]), Integer.parseInt(data[2])));
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeTreatmentType(TreatmentType treatmentType) {
        String fileName = "data/treatmentTypes.txt";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
            out.write(treatmentType.getFileString());
            out.write("\n");
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //endregion

    //region Treatments IO
    private void readTreatmentsFile() {
        treatments = new ArrayList<>();
        String fileName = "data/treatments.txt";
        fileCheck(fileName);
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String line;
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            while ((line = in.readLine()) != null) {
                String[] data = line.split(",");
                treatments.add(new Treatment(Integer.parseInt(data[0]), sdf.parse(data[1]), Boolean.parseBoolean(data[2]), data[3], data[4], Byte.parseByte(data[5]), Double.parseDouble(data[6])));
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void overwriteTreatmentsFile() {
        String fileName = "data/treatments.txt";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            for (Treatment t: treatments) {
                out.write(t.getFileString());
                out.write("\n");
            }
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void writeTreatment(Treatment treatment) {
        String fileName = "data/treatments.txt";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
            out.write(treatment.getFileString());
            out.write("\n");
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //endregion

    //region Variables IO
    private void readVariablesFile() {
        String fileName = "data/variables.txt";
        fileCheck(fileName);
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            try {
                salonIncome = Double.parseDouble(in.readLine());
                treatmentTypeId = Integer.parseInt(in.readLine());
                treatmentId = Integer.parseInt(in.readLine());
                loyaltyThreshold = Double.parseDouble(in.readLine());
            } catch (NumberFormatException e) {
                salonIncome = 0;
                treatmentTypeId = 0;
                treatmentId = 0;
                loyaltyThreshold = 5000;
            }
            in.close();
        } catch (Exception ignored) {
        }
    }

    private void overwriteVariablesFile() {
        String fileName = "data/variables.txt";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            out.write(salonIncome + "\n");
            out.write(treatmentTypeId + "\n");
            out.write(treatmentId + "\n");
            out.write(loyaltyThreshold + "\n");
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //endregion

    private void checkDataFile() {
        try {
            new File("data").mkdir();
        } catch (Exception ignored) {
        }
    }
    private void fileCheck(String fileName) {
        try {
            new File(fileName).createNewFile();
        } catch (Exception ignored) {
        }
    }
}
