package com.mrmi.beautysalon.main.objects;

import com.mrmi.beautysalon.main.exceptions.TreatmentNotFoundException;
import com.mrmi.beautysalon.main.exceptions.TreatmentTypeNotFoundException;
import com.mrmi.beautysalon.main.exceptions.UserNotFoundException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Database {
    private HashMap<String, User> users;
    private HashMap<Integer, TreatmentType> treatmentTypes;
    private HashMap<Integer, Treatment> treatments;
    public static User currentUser;
    public static String currentUsername;

    private double salonIncome = 0;
    private int treatmentTypeId = 0;

    private int treatmentId = 0;
    private double loyaltyThreshold;

    private final String filePathPrefix; // Used to differentiate test & regular files

    //region Database data & static variables
    public Database(String filePathPrefix) {
        this.filePathPrefix = filePathPrefix;
        this.users = new HashMap<>();
        this.treatments = new HashMap<>();
        this.treatmentTypes = new HashMap<>();
        loadData();
    }

    private void loadData() {
        checkDataFile();
        readUsersFile();
        readTreatmentsFile();
        readTreatmentTypesFile();
        readVariablesFile();
    }

    public void changeProfit(double profit) {
        salonIncome += profit;
        overwriteVariablesFile();
    }

    public boolean login(String username, String password) {
        for (Map.Entry<String, User> u : users.entrySet()) {
            if (u.getKey().equals(username) && u.getValue().getPassword().equals(password)) {
                currentUsername = u.getKey();
                currentUser = u.getValue();
                return true;
            }
        }
        currentUsername = "";
        currentUser = null;
        return false;
    }

    public void logout() {
        currentUsername = "";
        currentUser = null;
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
    public void addUser(User user, String username) {
        users.put(username, user);
        writeUser(user, username);
    }

    public void deleteUser(String username) throws UserNotFoundException {
        if (!users.containsKey(username)) {
            throw new UserNotFoundException("User with username " + username + " not found.");
        }
        users.remove(username);
    }

    public void editUser(User user, String username) throws UserNotFoundException {
        if (!users.containsKey(username)) {
            throw new UserNotFoundException("User with username " + username + " not found.");
        }
        users.put(username, user);
    }

    public HashMap<String, User> getUsers() {
        return users;
    }
    //endregion

    //region Client
    public boolean clientExists(String username) {
        return users.containsKey(username);
    }

    public Client getClientByUsername(String username) throws UserNotFoundException{
        if (!users.containsKey(username)) {
            throw new UserNotFoundException("Client with username " + username + " not found.");
        }
        return (Client) users.get(username);
    }

    public HashMap<String, Client> getLoyalClients() {
        HashMap<String, Client> clients = new HashMap<>();
        for(Map.Entry<String, User> u : users.entrySet()) {
            if (u.getValue().getClass().equals(Client.class)) {
                Client c = (Client) u.getValue();
                if (c.hasLoyaltyCard()) {
                    clients.put(u.getKey(), c);
                }
            }
        }

        return clients;
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
    //endregion

    //region Employee
    public HashMap<String, Employee> getEmployees() {
        HashMap<String, Employee> employees = new HashMap<>();
        for (Map.Entry<String, User> u : users.entrySet()) {
            if (!u.getValue().getClass().isInstance(User.class)) {
                employees.put(u.getKey(), (Employee) u);
            }
        }
        return employees;
    }
    //endregion

    //region Beautician
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

    public HashMap<String, Beautician> getBeauticians() {
        HashMap<String, Beautician> beauticians = new HashMap<>();
        for (Map.Entry<String, User> u : users.entrySet()) {
            if (u.getValue().getClass().equals(Beautician.class)) {
                beauticians.put(u.getKey(), (Beautician) u.getValue());
            }
        }
        return beauticians;
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
    //endregion

    //region Receptionist
    //endregion

    //region Manager
    //endregion

    //region Treatment
    public Treatment getTreatmentById (int id) throws TreatmentNotFoundException {
        if (!treatments.containsKey(id)) {
            throw new TreatmentNotFoundException("Treatment with id " + id + " not found.");
        }
        return treatments.get(id);
    }

    public void updateTreatment(Treatment treatment, Date date, int treatmentTypeId, String clientUsername, String beauticianUsername) {
        treatment.setScheduledDate(date);
        treatment.setTreatmentTypeId(treatmentTypeId);
        treatment.setClientUsername(clientUsername);
        treatment.setBeauticianUsername(beauticianUsername);
        overwriteTreatmentsFile();
    }

    public void updateTreatment(Treatment treatment) {
        overwriteTreatmentsFile();
    }

    public void addTreatment(Treatment treatment, int id) {
        treatments.put(id, treatment);
    }

    public void bookTreatment(Treatment treatment, int id) {
        try {
            TreatmentType treatmentType = getTreatmentTypeById(treatment.getTreatmentTypeId());
            treatmentType.changeTimesBooked(1);
            treatmentType.changeProfit(treatment.getPrice());
            overwriteTreatmentTypesFile();
        } catch (TreatmentTypeNotFoundException e) {
            e.printStackTrace();
            return;
        }

        addTreatment(treatment, id);
        writeTreatment(treatment, id);
        changeProfit(treatment.getPrice());
    }

    public int getNextTreatmentId() {
        return treatmentId++;
    }
    public void cancelTreatment(int treatmentId, boolean clientCancelled, String cancellationReason) {
        Treatment t;
        try {
            t = getTreatmentById(treatmentId);
        } catch (TreatmentNotFoundException e) {
            e.printStackTrace();
            return;
        }
        t.setCancelled(true);
        t.setCancellationReason(cancellationReason);

        Client client;
        try {
            client = getClientByUsername(t.getClientUsername());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return;
        }

        double refundedPrice = t.getPrice();
        if (clientCancelled) {
            changeProfit(refundedPrice * 0.1);
            refundedPrice *= 0.9;
            t.setStatus("Cancelled by client");
        } else {
            changeProfit(refundedPrice);
            t.setStatus("Cancelled by salon");
        }

        overwriteTreatmentsFile();
        client.changeMoneySpent(refundedPrice, this);
        overwriteUsersFile();

        try {
            TreatmentType treatmentType = getTreatmentTypeById(t.getTreatmentTypeId());
            treatmentType.changeTimesBooked(-1);
            treatmentType.changeProfit(-refundedPrice);
            overwriteTreatmentTypesFile();
        } catch (TreatmentTypeNotFoundException e) {
            e.printStackTrace();
        }
    }

    public HashMap<Integer, Treatment> getTreatments() {
        return treatments;
    }
    //endregion

    //region Treatment types
    public HashMap<Integer, TreatmentType> getTreatmentTypes() {
        return treatmentTypes;
    }
    public TreatmentType getTreatmentTypeById(int id) throws TreatmentTypeNotFoundException {
        if (!treatmentTypes.containsKey(id)) {
            throw new TreatmentTypeNotFoundException("Treatment type with id " + id + " not found.");

        }
        return treatmentTypes.get(id);
    }

    public void addTreatmentType(TreatmentType type, int id) {
        treatmentTypes.put(id, type);
        writeTreatmentType(type, id);
        overwriteVariablesFile();
    }

    public int getNextTreatmentTypeId() {
        return treatmentTypeId++;
    }

    // TODO: sort -> to list vs counting sort?d
    public List<Treatment> getTreatmentsSortedByBeauticians() {
        return treatments.values().stream().sorted(Comparator.comparing(Treatment::getBeauticianUsername)).toList();
    }

    public List<Treatment> getTreatmentsSortedByCancellationReason() {
        return treatments.values().stream().sorted(Comparator.comparing(Treatment::getCancellationReason)).toList();
    }
    //endregion

    //region Users IO
    private void readUsersFile() {
        users = new HashMap<>();
        String fileName = filePathPrefix + "data/users.txt";
        fileCheck(fileName);
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                String[] userData = line.split(",");
                switch (userData[0]) {
                    case "C" -> users.put(userData[1], new Client(
                            userData[2],
                            userData[3],
                            userData[4],
                            userData[5],
                            userData[6],
                            userData[7],
                            Boolean.parseBoolean(userData[8]),
                            Double.parseDouble(userData[9])));
                    case "B" -> {
                        List<Byte> treatmentTypeIDs = new ArrayList<>();
                        String[] treatmentTypes = userData[12].split(";");
                        for(String s : treatmentTypes) {
                            treatmentTypeIDs.add(Byte.parseByte(s));
                        }
                        users.put(userData[1], new Beautician(
                                userData[2],
                                userData[3],
                                userData[4],
                                userData[5],
                                userData[6],
                                userData[7],
                                treatmentTypeIDs,
                                Byte.parseByte(userData[8]),
                                Byte.parseByte(userData[9]),
                                Double.parseDouble(userData[10]),
                                Double.parseDouble(userData[11])));
                    }
                    case "R" -> users.put(userData[1], new Receptionist(
                            userData[2],
                            userData[3],
                            userData[4],
                            userData[5],
                            userData[6],
                            userData[7],
                            Byte.parseByte(userData[8]),
                            Byte.parseByte(userData[9]),
                            Double.parseDouble(userData[10]),
                            Double.parseDouble(userData[11])));
                    case "M" -> users.put(userData[1], new Manager(
                            userData[2],
                            userData[3],
                            userData[4],
                            userData[5],
                            userData[6],
                            userData[7],
                            Byte.parseByte(userData[8]),
                            Byte.parseByte(userData[9]),
                            Double.parseDouble(userData[10]),
                            Double.parseDouble(userData[11])));
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeUser(User user, String username) {
        String fileName = filePathPrefix + "data/users.txt";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
            out.write(user.getFileString(username));
            out.write("\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void overwriteUsersFile() {
        String fileName = filePathPrefix + "data/users.txt";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            for (Map.Entry<String, User> u : users.entrySet()) {
                out.write(u.getValue().getFileString(u.getKey()));
                out.write("\n");
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region TreatmentTypes IO
    private void readTreatmentTypesFile() {
        treatmentTypes = new HashMap<>();
        String fileName = filePathPrefix + "data/treatmentTypes.txt";
        fileCheck(fileName);
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                String[] data = line.split(",");
                treatmentTypes.put(Integer.parseInt(data[0]), new TreatmentType(data[1], Double.parseDouble(data[2]), Integer.parseInt(data[3]), Double.parseDouble(data[4])));
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeTreatmentType(TreatmentType treatmentType, int id) {
        String fileName = filePathPrefix + "data/treatmentTypes.txt";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
            out.write(treatmentType.getFileString(id));
            out.write("\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void overwriteTreatmentTypesFile() {
        String fileName = filePathPrefix + "data/treatmentTypes.txt";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            for (Map.Entry<Integer, TreatmentType> t : treatmentTypes.entrySet()) {
                out.write(t.getValue().getFileString(t.getKey()));
                out.write("\n");
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region Treatments IO
    private void readTreatmentsFile() {
        treatments = new HashMap<>();
        String fileName = filePathPrefix + "data/treatments.txt";
        fileCheck(fileName);
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String line;
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            while ((line = in.readLine()) != null) {
                String[] data = line.split(",");
                treatments.put(Integer.parseInt(data[0]), new Treatment(sdf.parse(data[1]), Boolean.parseBoolean(data[2]), data[3], data[4], Byte.parseByte(data[5]), Double.parseDouble(data[6]), data[7], data[8]));
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void overwriteTreatmentsFile() {
        String fileName = filePathPrefix + "data/treatments.txt";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            for (Map.Entry<Integer, Treatment> t: treatments.entrySet()) {
                out.write(t.getValue().getFileString(t.getKey()));
                out.write("\n");
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeTreatment(Treatment treatment, int id) {
        String fileName = filePathPrefix + "data/treatments.txt";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
            out.write(treatment.getFileString(id));
            out.write("\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region Variables IO
    private void readVariablesFile() {
        String fileName = filePathPrefix + "data/variables.txt";
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void overwriteVariablesFile() {
        String fileName = filePathPrefix + "data/variables.txt";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            out.write(salonIncome + "\n");
            out.write(treatmentTypeId + "\n");
            out.write(treatmentId + "\n");
            out.write(loyaltyThreshold + "\n");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    private void checkDataFile() {
        new File(filePathPrefix + "data").mkdir();
    }
    private void fileCheck(String fileName) {
        try {
            new File(fileName).createNewFile();
        } catch (IOException ignored) {
        }
    }
}
