package com.mrmi.beautysalon.main.entity;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Database {
    private HashMap<Integer, User> users;
    private HashMap<Integer, TreatmentTypeCategory> categories;
    private HashMap<Integer, TreatmentType> types;
    private HashMap<Integer, Treatment> treatments;
    private BeautySalon beautySalon;
    private int userId;
    private int categoryId;
    private int typeId;

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
    public void addTreatmentTypeCategory(int id, TreatmentTypeCategory category) {
        category.setId(id);
        categories.put(id, category);
        writeTreatmentTypeCategory(category);
        overwriteVariablesFile();
    }

    public HashMap<Integer, TreatmentTypeCategory> getTreatmentTypeCategories() {
        return categories;
    }

    public int getNextTreatmentTypeCategoryId() {
        categoryId++;
        overwriteVariablesFile();
        return categoryId;
    }

    public void updateTreatmentTypeCategory(TreatmentTypeCategory category) {
        categories.put(category.getId(), category);
        overwriteTreatmentTypeCategoriesFile();
    }
    //endregion

    //region Treatment types
    public void addTreatmentType(int id, TreatmentType type) {
        type.setId(id);
        types.put(id, type);
        writeTreatmentType(type);
        overwriteVariablesFile();
    }

    public HashMap<Integer, TreatmentType> getTreatmentTypes() {
        return types;
    }

    public int getNextTreatmentTypeId() {
        typeId++;
        overwriteVariablesFile();
        return typeId;
    }

    public void updateTreatmentType(int id, TreatmentType type) {
        types.put(id, type);
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
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            Calendar hiringDate = Calendar.getInstance();
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
                            userData[8]));
                        break;
                    case "B":
                        ArrayList<Integer> typeIDs = new ArrayList<>();
                        String[] types = userData[13].split(";");
                        for (String s : types) {
                            if (!s.equals("")) {
                                typeIDs.add(Integer.parseInt(s));
                            }
                        }
                        hiringDate.setTime(sdf.parse(userData[12]));
                        users.put(Integer.parseInt(userData[1]), new Beautician(Integer.parseInt(userData[1]),
                                userData[2],
                                userData[3],
                                userData[4],
                                userData[5],
                                userData[6],
                                userData[7],
                                userData[8],
                                typeIDs,
                                Byte.parseByte(userData[9]),
                                Byte.parseByte(userData[10]),
                                Float.parseFloat(userData[11]),
                                hiringDate));
                        break;
                    case "R":
                        hiringDate.setTime(sdf.parse(userData[12]));
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
                            Float.parseFloat(userData[11]),
                                hiringDate));
                        break;
                    case "M":
                        hiringDate.setTime(sdf.parse(userData[12]));
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
                            Float.parseFloat(userData[11]),
                                hiringDate));
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
        categories = new HashMap<>();
        String fileName = filePathPrefix + "data" + separator + "categories.txt";
        try {
            File file = fileCheck(fileName);
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                String[] data = line.split(",");
                categories.put(Integer.parseInt(data[0]), new TreatmentTypeCategory(Integer.parseInt(data[0]), data[1], Boolean.parseBoolean(data[2])));
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeTreatmentTypeCategory(TreatmentTypeCategory category) {
        String fileName = filePathPrefix + "data" + separator + "categories.txt";
        writeFile(fileName, category.getFileString());
    }

    private void overwriteTreatmentTypeCategoriesFile() {
        String fileName = filePathPrefix + "data" + separator + "categories.txt";
        try {
            File file = fileCheck(fileName);
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (Map.Entry<Integer, TreatmentTypeCategory> t : categories.entrySet()) {
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
        types = new HashMap<>();
        String fileName = filePathPrefix + "data" + separator + "types.txt";
        try {
            File file = fileCheck(fileName);
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                String[] data = line.split(",");
                types.put(Integer.parseInt(data[0]), new TreatmentType(Integer.parseInt(data[0]), data[1], Float.parseFloat(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Boolean.parseBoolean(data[5])));
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeTreatmentType(TreatmentType type) {
        String fileName = filePathPrefix + "data" + separator + "types.txt";
        writeFile(fileName, type.getFileString());
    }

    private void overwriteTreatmentTypesFile() {
        String fileName = filePathPrefix + "data" + separator + "types.txt";
        try {
            File file = fileCheck(fileName);
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (Map.Entry<Integer, TreatmentType> t : types.entrySet()) {
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
                Calendar cancelledDate = null;
                if (data.length == 9) {
                    cancelledDate = Calendar.getInstance();
                    cancelledDate.setTime(sdf.parse(data[8]));
                }

                treatments.put(Integer.parseInt(data[0]), new Treatment(Integer.parseInt(data[0]), scheduledDate, data[2], data[3], Byte.parseByte(data[4]), Float.parseFloat(data[5]), Treatment.Status.valueOf(data[6]), data[7], cancelledDate));
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
                typeId = Integer.parseInt(in.readLine());
                treatmentId = Integer.parseInt(in.readLine());
                categoryId = Integer.parseInt(in.readLine());
                userId = Integer.parseInt(in.readLine());
            } catch (NumberFormatException e) {
                typeId = -1;
                treatmentId = -1;
                categoryId = -1;
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
            out.write(typeId + "\n");
            out.write(treatmentId + "\n");
            out.write(categoryId + "\n");
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
            float loyaltyThreshold = Float.parseFloat(in.readLine());
            String name = in.readLine();
            float bonus = Float.parseFloat(in.readLine());
            in.close();
            beautySalon = new BeautySalon(openingHour, closingHour, loyaltyThreshold, name, bonus);
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