package com.mrmi.beautysalon.main.controller;

import com.mrmi.beautysalon.main.entity.Beautician;
import com.mrmi.beautysalon.main.entity.BeautySalon;

import java.io.*;

public class SalonController {
    private final String filePathPrefix;
    private final String separator;
    private final BeautySalon beautySalon;

    public SalonController (String filePathPrefix) {
        this.filePathPrefix = filePathPrefix;
        this.separator = System.getProperty("file.separator");
        this.beautySalon = readSalonFile();
    }

    public void changeProfit(double profit) {
        beautySalon.setSalonIncome(beautySalon.getSalonIncome() + profit);
        overwriteSalonFile();
    }

    public BeautySalon getBeautySalon() {
        return beautySalon;
    }
    private BeautySalon readSalonFile() {
        BeautySalon salon = new BeautySalon((byte) 8, (byte) 20, 15000, 0);
        String fileName = filePathPrefix + "data" + separator + "salon.txt";
        fileCheck(fileName);
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            try {
                salon.setSalonOpeningHour(Byte.parseByte(in.readLine()));
                salon.setSalonClosingHour(Byte.parseByte(in.readLine()));
                salon.setLoyaltyThreshold(Double.parseDouble(in.readLine()));
                salon.setSalonIncome(Double.parseDouble(in.readLine()));
            } catch (NumberFormatException ignored) {
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return salon;
    }

    private void overwriteSalonFile() {
        String fileName = filePathPrefix + "data" + separator + "variables.txt";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            out.write(beautySalon.getSalonOpeningHour());
            out.write(beautySalon.getSalonClosingHour());
            out.write(String.valueOf(beautySalon.getLoyaltyThreshold()));
            out.write(String.valueOf(beautySalon.getSalonIncome()));
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
}
