package com.mrmi.beautysalon.main.entity;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Database {
    private HashMap<Integer, User> users;
    private HashMap<Integer, TreatmentTypeCategory> treatmentTypeCategories;
    private HashMap<Integer, TreatmentType> treatmentTypes;
    private HashMap<Integer, Treatment> treatments;
    private BeautySalon beautySalon;
    private int userId;
    private int treatmentTypeCategoryId;
    private int treatmentTypeId;

    private int treatmentId;

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
        readSalonFile();
    }
    //endregion

    //region User
    public void addUser(int id, User user) {
        user.setId(id);
        users.put(id, user);
        writeUser(user);
    }

    public HashMap<Integer, User> getUsers() {
        return users;
    }

    public void updateUser(User user) {
        users.put(user.getId(), user);
        overwriteUsersFile();
    }

    public void deleteUser(int userId) {
        users.remove(userId);
        overwriteUsersFile();
    }

    public int getNextUserId() {
        userId++;
        overwriteVariablesFile();
        return userId;
    }
    //endregion

    //region Treatment Type Category
    public void addTreatmentTypeCategory(int id, TreatmentTypeCategory treatmentTypeCategory) {
        treatmentTypeCategory.setId(id);
        treatmentTypeCategories.put(id, treatmentTypeCategory);
        writeTreatmentTypeCategory(treatmentTypeCategory);
        overwriteVariablesFile();
    }

    public HashMap<Integer, TreatmentTypeCategory> getTreatmentTypeCategories() {
        return treatmentTypeCategories;
    }

    public int getNextTreatmentTypeCategoryId() {
        treatmentTypeCategoryId++;
        overwriteVariablesFile();
        return treatmentTypeCategoryId;
    }

    public void updateTreatmentTypeCategory(TreatmentTypeCategory treatmentTypeCategory) {
        treatmentTypeCategories.put(treatmentTypeCategory.getId(), treatmentTypeCategory);
        overwriteTreatmentTypeCategoriesFile();
    }
    //endregion

    //region Treatment types
    public void addTreatmentType(int id, TreatmentType type) {
        type.setId(id);
        treatmentTypes.put(id, type);
        writeTreatmentType(type);
        overwriteVariablesFile();
    }

    public HashMap<Integer, TreatmentType> getTreatmentTypes() {
        return treatmentTypes;
    }

    public int getNextTreatmentTypeId() {
        treatmentTypeId++;
        overwriteVariablesFile();
        return treatmentTypeId;
    }

    public void updateTreatmentType(int id, TreatmentType treatmentType) {
        treatmentTypes.put(id, treatmentType);
        overwriteTreatmentTypesFile();
    }
    //endregion

    //region Treatment
    public void addTreatment(int id, Treatment treatment) {
        treatment.setId(id);
        treatments.put(id, treatment);
        writeTreatment(treatment);
    }

    public HashMap<Integer, Treatment> getTreatments() {
        return treatments;
    }

    public int getNextTreatmentId() {
        treatmentId++;
        overwriteVariablesFile();
        return treatmentId;
    }

    public void updateTreatment(Treatment treatment) {
        treatments.put(treatment.getId(), treatment);
        overwriteTreatmentsFile();
    }

    public void deleteTreatment(int id) {
        this.treatments.remove(id);
        overwriteTreatmentsFile();
    }
    //endregion

    //region Beauty salon
    public BeautySalon getBeautySalon() {
        return beautySalon;
    }

    public void updateBeautySalon(BeautySalon beautySalon) {
        this.beautySalon = beautySalon;
        overwriteSalonFile();
    }
    //endregion

    //region Generic IO
    private void writeFile(String fileName, String objectFileString) {
        try {
            File file = fileCheck(fileName);
            BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
            out.write(objectFileString);
            out.write("\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion Generic IO

    //region Users IO
    private void readUsersFile() {
        users = new HashMap<>();
        String fileName = filePathPrefix + "data" + separator + "users.txt";
        try {
            File file = fileCheck(fileName);
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                String[] userData = line.split(",");
                switch (userData[0]) {
                    case "C":
                        users.put(Integer.parseInt(userData[1]), new Client(Integer.parseInt(userData[1]),
                            userData[2],
                            userData[3],
                            userData[4],
                            userData[5],
                            userData[6],
                            userData[7],
                            userData[8],
                            Boolean.parseBoolean(userData[9]),
                            Double.parseDouble(userData[10])));
                        break;
                    case "B":
                        ArrayList<Integer> treatmentTypeIDs = new ArrayList<>();
                        String[] treatmentTypes = userData[12].split(";");
                        for (String s : treatmentTypes) {
                            if (!s.equals("")) {
                                treatmentTypeIDs.add(Integer.parseInt(s));
                            }
                        }
                        users.put(Integer.parseInt(userData[1]), new Beautician(Integer.parseInt(userData[1]),
                                userData[2],
                                userData[3],
                                userData[4],
                                userData[5],
                                userData[6],
                                userData[7],
                                userData[8],
                                treatmentTypeIDs,
                                Byte.parseByte(userData[9]),
                                Byte.parseByte(userData[10]),
                                Double.parseDouble(userData[11])));
                        break;
                    case "R":
                        users.put(Integer.parseInt(userData[1]), new Receptionist(Integer.parseInt(userData[1]),
                            userData[2],
                            userData[3],
                            userData[4],
                            userData[5],
                            userData[6],
                            userData[7],
                            userData[8],
                            Byte.parseByte(userData[9]),
                            Byte.parseByte(userData[10]),
                            Double.parseDouble(userData[11])));
                        break;
                    case "M":
                        users.put(Integer.parseInt(userData[1]), new Manager(Integer.parseInt(userData[1]),
                            userData[2],
                            userData[3],
                            userData[4],
                            userData[5],
                            userData[6],
                            userData[7],
                            userData[8],
                            Byte.parseByte(userData[9]),
                            Byte.parseByte(userData[10]),
                            Double.parseDouble(userData[11])));
                        break;
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeUser(User user) {
        String fileName = filePathPrefix + "data" + separator + "users.txt";
        writeFile(fileName, user.getFileString());
    }

    private void overwriteUsersFile() {
        String fileName = filePathPrefix + "data" + separator + "users.txt";
        try {
            File file = fileCheck(fileName);
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (Map.Entry<Integer, User> u : users.entrySet()) {
                out.write(u.getValue().getFileString());
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
        try {
            File file = fileCheck(fileName);
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                String[] data = line.split(",");
                treatmentTypeCategories.put(Integer.parseInt(data[0]), new TreatmentTypeCategory(Integer.parseInt(data[0]), data[1], Boolean.parseBoolean(data[2])));
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeTreatmentTypeCategory(TreatmentTypeCategory treatmentTypeCategory) {
        String fileName = filePathPrefix + "data" + separator + "treatmentTypeCategories.txt";
        writeFile(fileName, treatmentTypeCategory.getFileString());
    }

    private void overwriteTreatmentTypeCategoriesFile() {
        String fileName = filePathPrefix + "data" + separator + "treatmentTypeCategories.txt";
        try {
            File file = fileCheck(fileName);
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (Map.Entry<Integer, TreatmentTypeCategory> t : treatmentTypeCategories.entrySet()) {
                out.write(t.getValue().getFileString());
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
        try {
            File file = fileCheck(fileName);
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                String[] data = line.split(",");
                treatmentTypes.put(Integer.parseInt(data[0]), new TreatmentType(Integer.parseInt(data[0]), data[1], Double.parseDouble(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Boolean.parseBoolean(data[5])));
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeTreatmentType(TreatmentType treatmentType) {
        String fileName = filePathPrefix + "data" + separator + "treatmentTypes.txt";
        writeFile(fileName, treatmentType.getFileString());
    }

    private void overwriteTreatmentTypesFile() {
        String fileName = filePathPrefix + "data" + separator + "treatmentTypes.txt";
        try {
            File file = fileCheck(fileName);
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (Map.Entry<Integer, TreatmentType> t : treatmentTypes.entrySet()) {
                out.write(t.getValue().getFileString());
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
        try {
            File file = fileCheck(fileName);
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            while ((line = in.readLine()) != null) {
                String[] data = line.split(",");
                Calendar scheduledDate = Calendar.getInstance();
                scheduledDate.setTime(sdf.parse(data[1]));
                treatments.put(Integer.parseInt(data[0]), new Treatment(Integer.parseInt(data[0]), scheduledDate, Boolean.parseBoolean(data[2]), data[3], data[4], Byte.parseByte(data[5]), Double.parseDouble(data[6]), Treatment.Status.valueOf(data[7]), data[8]));
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void overwriteTreatmentsFile() {
        String fileName = filePathPrefix + "data" + separator + "treatments.txt";
        try {
            File file = fileCheck(fileName);
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (Map.Entry<Integer, Treatment> t : treatments.entrySet()) {
                out.write(t.getValue().getFileString());
                out.write("\n");
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void writeTreatment(Treatment treatment) {
        String fileName = filePathPrefix + "data" + separator + "treatments.txt";
        writeFile(fileName, treatment.getFileString());
    }
    //endregion

    //region Variables IO
    private void readVariablesFile() {
        String fileName = filePathPrefix + "data" + separator + "variables.txt";
        try {
            File file = fileCheck(fileName);
            BufferedReader in = new BufferedReader(new FileReader(file));
            try {
                treatmentTypeId = Integer.parseInt(in.readLine());
                treatmentId = Integer.parseInt(in.readLine());
                treatmentTypeCategoryId = Integer.parseInt(in.readLine());
                userId = Integer.parseInt(in.readLine());
            } catch (NumberFormatException e) {
                treatmentTypeId = -1;
                treatmentId = -1;
                treatmentTypeCategoryId = -1;
                userId = -1;
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void overwriteVariablesFile() {
        String fileName = filePathPrefix + "data" + separator + "variables.txt";
        try {
            File file = fileCheck(fileName);
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(treatmentTypeId + "\n");
            out.write(treatmentId + "\n");
            out.write(treatmentTypeCategoryId + "\n");
            out.write(userId + "\n");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region Beauty salon IO
    private void readSalonFile() {
        String fileName = filePathPrefix + "data" + separator + "salon.txt";
        try {
            File file = fileCheck(fileName);
            BufferedReader in = new BufferedReader(new FileReader(file));
            byte openingHour = Byte.parseByte(in.readLine());
            byte closingHour = Byte.parseByte(in.readLine());
            double loyaltyThreshold = Double.parseDouble(in.readLine());
            double income = Double.parseDouble(in.readLine());
            double bonus = Double.parseDouble(in.readLine());
            String name = in.readLine();
            in.close();
            beautySalon = new BeautySalon(openingHour, closingHour, loyaltyThreshold, income, name, bonus);
        } catch (NumberFormatException | IOException e) {
            beautySalon = new BeautySalon();
        }
    }

    private void overwriteSalonFile() {
        String fileName = filePathPrefix + "data" + separator + "salon.txt";
        try {
            File file = fileCheck(fileName);
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(beautySalon.getSalonOpeningHour() + "\n");
            out.write(beautySalon.getSalonClosingHour() + "\n");
            out.write(beautySalon.getLoyaltyThreshold() + "\n");
            out.write(beautySalon.getSalonIncome() + "\n");
            out.write(beautySalon.getName() + "\n");
            out.write(beautySalon.getBonus() + "\n");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //endregion
    private void checkDataFile() {
        new File(filePathPrefix + "data").mkdir();
    }

    private File fileCheck(String fileName) throws IOException {
        File file = new File(fileName);
        file.createNewFile();
        return file;
    }
}