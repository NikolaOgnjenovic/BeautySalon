package com.mrmi.beautysalon.main.entity;

import com.mrmi.beautysalon.main.exceptions.UserNotFoundException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Database {
    private HashMap<String, User> users;
    private HashMap<Integer, TreatmentTypeCategory> treatmentTypeCategories;
    private HashMap<Integer, TreatmentType> treatmentTypes;
    private HashMap<Integer, Treatment> treatments;
    private int treatmentTypeCategoryId = 0;
    private int treatmentTypeId = 0;

    private int treatmentId = 0;

    private final String filePathPrefix; // Used to differentiate test & regular files
    private final String separator;

    //region Database data & static variables
    public Database(String filePathPrefix) {
        this.filePathPrefix = filePathPrefix;
        this.separator = System.getProperty("file.separator");
        loadData();
    }

    private void loadData() {
        checkDataFile();
        readUsersFile();
        readTreatmentsFile();
        readTreatmentTypesFile();
        readTreatmentTypeCategoriesFile();
        readVariablesFile();
    }
    //endregion

    //region User
    public void addUser(String username, User user) {
        users.put(username, user);
        writeUser(user, username);
    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public void updateUser(String username, User user) throws UserNotFoundException {
        if (!users.containsKey(username)) {
            throw new UserNotFoundException("User with username " + username + " not found.");
        }
        users.put(username, user);
        overwriteUsersFile();
    }

    public void deleteUser(String username) throws UserNotFoundException {
        if (!users.containsKey(username)) {
            throw new UserNotFoundException("User with username " + username + " not found.");
        }
        users.remove(username);
        overwriteUsersFile();
    }
    //endregion

    //region Treatment Type Category
    public HashMap<Integer, TreatmentTypeCategory> getTreatmentTypeCategories() {
        return treatmentTypeCategories;
    }

    public void addTreatmentTypeCategory(TreatmentTypeCategory treatmentTypeCategory) {
        int id = getNextTreatmentTypeCategoryId();
        treatmentTypeCategories.put(id, treatmentTypeCategory);
        writeTreatmentTypeCategory(treatmentTypeCategory, id);
        overwriteVariablesFile();
    }

    public int getNextTreatmentTypeCategoryId() {
        return treatmentTypeCategoryId++;
    }

    public void updateTreatmentTypeCategory(int id, TreatmentTypeCategory treatmentTypeCategory) {
        treatmentTypeCategories.put(id, treatmentTypeCategory);
        overwriteTreatmentTypeCategoriesFile();
    }

    public void deleteTreatmentTypeCategory(int id) {
        treatmentTypeCategories.remove(id);
        overwriteTreatmentTypeCategoriesFile();
    }
    //endregion

    //region Treatment types
    public HashMap<Integer, TreatmentType> getTreatmentTypes() {
        return treatmentTypes;
    }

    public void addTreatmentType(TreatmentType type) {
        int id = getNextTreatmentTypeId();
        treatmentTypes.put(id, type);
        writeTreatmentType(type, id);
        overwriteVariablesFile();
    }

    public int getNextTreatmentTypeId() {
        return treatmentTypeId++;
    }

    public void updateTreatmentType(int id, TreatmentType treatmentType) {
        treatmentTypes.put(id, treatmentType);
        overwriteTreatmentTypesFile();
    }

    public void deleteTreatmentType(int id) {
        treatmentTypes.remove(id);
        overwriteTreatmentTypesFile();
    }
    //endregion

    //region Treatment
    public void updateTreatment(Treatment treatment, Date date, int treatmentTypeId, String clientUsername, String beauticianUsername) {
        treatment.setScheduledDate(date);
        treatment.setTreatmentTypeId(treatmentTypeId);
        treatment.setClientUsername(clientUsername);
        treatment.setBeauticianUsername(beauticianUsername);
        overwriteTreatmentsFile();
    }

    public void updateTreatment(Treatment treatment, int id) {
        treatments.put(id, treatment);
        overwriteTreatmentsFile();
    }

    public void addTreatment(Treatment treatment) {
        int id = getNextTreatmentId();
        treatments.put(id, treatment);
        writeTreatment(treatment, id);
    }

    public int getNextTreatmentId() {
        return treatmentId++;
    }

    public HashMap<Integer, Treatment> getTreatments() {
        return treatments;
    }

    public void deleteTreatment(int id) {
        this.treatments.remove(id);
        overwriteTreatmentsFile();
    }
    //endregion

    //region Users IO
    private void readUsersFile() {
        users = new HashMap<>();
        String fileName = filePathPrefix + "data" + separator + "users.txt";
        fileCheck(fileName);
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                String[] userData = line.split(",");
                switch (userData[0]) {
                    case "C":
                        users.put(userData[1], new Client(
                            userData[2],
                            userData[3],
                            userData[4],
                            userData[5],
                            userData[6],
                            userData[7],
                            Boolean.parseBoolean(userData[8]),
                            Double.parseDouble(userData[9])));
                        break;
                    case "B":
                        List<Byte> treatmentTypeIDs = new ArrayList<>();
                        String[] treatmentTypes = userData[13].split(";");
                        for (String s : treatmentTypes) {
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
                                Double.parseDouble(userData[11]),
                                Integer.parseInt(userData[12])));
                        break;
                    case "R":
                        users.put(userData[1], new Receptionist(
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
                        break;
                    case "M":
                        users.put(userData[1], new Manager(
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
                        break;
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeUser(User user, String username) {
        String fileName = filePathPrefix + "data" + separator + "users.txt";
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
        String fileName = filePathPrefix + "data" + separator + "users.txt";
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

    //region TreatmentTypeCategory IO
    private void readTreatmentTypeCategoriesFile() {
        treatmentTypeCategories = new HashMap<>();
        String fileName = filePathPrefix + "data" + separator + "treatmentTypeCategories.txt";
        fileCheck(fileName);
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                String[] data = line.split(",");
                String[] treatmentTypeArr = data[3].split(";");
                ArrayList<Integer> treatmentTypeIDs = new ArrayList<>();
                for (String s : treatmentTypeArr) {
                    treatmentTypeIDs.add(Integer.parseInt(s));
                }
                treatmentTypeCategories.put(Integer.parseInt(data[0]), new TreatmentTypeCategory(data[1], treatmentTypeIDs, Double.parseDouble(data[2])));
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeTreatmentTypeCategory(TreatmentTypeCategory treatmentTypeCategory, int id) {
        String fileName = filePathPrefix + "data" + separator + "treatmentTypeCategories.txt";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
            out.write(treatmentTypeCategory.getFileString(id));
            out.write("\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void overwriteTreatmentTypeCategoriesFile() {
        String fileName = filePathPrefix + "data" + separator + "treatmentTypeCategories.txt";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            for (Map.Entry<Integer, TreatmentTypeCategory> t : treatmentTypeCategories.entrySet()) {
                out.write(t.getValue().getFileString(t.getKey()));
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
        String fileName = filePathPrefix + "data" + separator + "treatmentTypes.txt";
        fileCheck(fileName);
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                String[] data = line.split(",");
                treatmentTypes.put(Integer.parseInt(data[0]), new TreatmentType(data[1], Double.parseDouble(data[2]), Integer.parseInt(data[3]), Double.parseDouble(data[4]), Integer.parseInt(data[5]), Byte.parseByte(data[6])));
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeTreatmentType(TreatmentType treatmentType, int id) {
        String fileName = filePathPrefix + "data" + separator + "treatmentTypes.txt";
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
        String fileName = filePathPrefix + "data" + separator + "treatmentTypes.txt";
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
        String fileName = filePathPrefix + "data" + separator + "treatments.txt";
        fileCheck(fileName);
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String line;
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            while ((line = in.readLine()) != null) {
                String[] data = line.split(",");
                treatments.put(Integer.parseInt(data[0]), new Treatment(sdf.parse(data[1]), Boolean.parseBoolean(data[2]), data[3], data[4], Byte.parseByte(data[5]), Double.parseDouble(data[6]), Treatment.Status.valueOf(data[7]), data[8]));
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void overwriteTreatmentsFile() {
        String fileName = filePathPrefix + "data" + separator + "treatments.txt";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            for (Map.Entry<Integer, Treatment> t : treatments.entrySet()) {
                out.write(t.getValue().getFileString(t.getKey()));
                out.write("\n");
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeTreatment(Treatment treatment, int id) {
        String fileName = filePathPrefix + "data" + separator + "treatments.txt";
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
        String fileName = filePathPrefix + "data" + separator + "variables.txt";
        fileCheck(fileName);
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            try {
                treatmentTypeId = Integer.parseInt(in.readLine());
                treatmentId = Integer.parseInt(in.readLine());
                treatmentTypeCategoryId = Integer.parseInt(in.readLine());
            } catch (NumberFormatException e) {
                treatmentTypeId = 0;
                treatmentId = 0;
                treatmentTypeCategoryId = 0;
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void overwriteVariablesFile() {
        String fileName = filePathPrefix + "data" + separator + "variables.txt";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            out.write(treatmentTypeId + "\n");
            out.write(treatmentId + "\n");
            out.write(treatmentTypeCategoryId + "\n");
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
