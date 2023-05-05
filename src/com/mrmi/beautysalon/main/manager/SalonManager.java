package com.mrmi.beautysalon.main.manager;

import com.mrmi.beautysalon.main.entity.BeautySalon;

import java.io.*;

public class SalonManager {
    private final String filePathPrefix;
    private final String separator;
    private final BeautySalon beautySalon;

    public SalonManager(String filePathPrefix) {
        this.filePathPrefix = filePathPrefix;
        this.separator = System.getProperty("file.separator");
        this.beautySalon = readSalonFile();
    }

    public void changeProfit(double profit) {
        beautySalon.setSalonIncome(beautySalon.getSalonIncome() + profit);
        overwriteSalonFile();
    }

    private BeautySalon readSalonFile() {
        String fileName = filePathPrefix + "data" + separator + "salon.txt";
        try {
            fileCheck(fileName);
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            byte openingHour = Byte.parseByte(in.readLine());
            byte closingHour = Byte.parseByte(in.readLine());
            double loyaltyThreshold = Double.parseDouble(in.readLine());
            double income = Double.parseDouble(in.readLine());
            String name = in.readLine();
            in.close();
            return new BeautySalon(openingHour, closingHour, loyaltyThreshold, income, name);
        } catch (NumberFormatException | IOException ignored) {
            return new BeautySalon();
        }
    }

    private void overwriteSalonFile() {
        String fileName = filePathPrefix + "data" + separator + "salon.txt";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            out.write(beautySalon.getSalonOpeningHour() + "\n");
            out.write(beautySalon.getSalonClosingHour() + "\n");
            out.write(beautySalon.getLoyaltyThreshold() + "\n");
            out.write(beautySalon.getSalonIncome() + "\n");
            out.write(beautySalon.getName() + "\n");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fileCheck(String fileName) {
        try {
            new File(fileName).createNewFile();
        } catch (IOException ignored) {
        }
    }

    public void setOpeningHour(byte hour) {
        beautySalon.setSalonOpeningHour(hour);
        overwriteSalonFile();
    }

    public byte getOpeningHour() {
        return beautySalon.getSalonOpeningHour();
    }

    public void setClosingHour(byte hour) {
        beautySalon.setSalonClosingHour(hour);
        overwriteSalonFile();
    }

    public byte getClosingHour() {
        return beautySalon.getSalonClosingHour();
    }

    public void setName(String name) {
        beautySalon.setName(name);
        overwriteSalonFile();
    }

    public String getName() {
        return beautySalon.getName();
    }

    public void setLoyaltyThreshold(double threshold) {
        beautySalon.setLoyaltyThreshold(threshold);
        overwriteSalonFile();
    }

    public double getLoyaltyThreshold() {
        return beautySalon.getLoyaltyThreshold();
    }
}
