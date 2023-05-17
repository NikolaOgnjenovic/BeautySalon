package com.mrmi.beautysalon.main.manager;

import com.mrmi.beautysalon.main.entity.BeautySalon;
import com.mrmi.beautysalon.main.entity.Database;

public class SalonManager {
    private final Database database;
    private final BeautySalon beautySalon;

    public SalonManager(Database database) {
        this.database = database;
        this.beautySalon = database.getBeautySalon();
    }

    public void addIncome(double income) {
        beautySalon.setSalonIncome(beautySalon.getSalonIncome() + income);
        database.updateBeautySalon(beautySalon);
    }

    public void setOpeningHour(byte hour) {
        beautySalon.setSalonOpeningHour(hour);
        database.updateBeautySalon(beautySalon);
    }

    public byte getOpeningHour() {
        return beautySalon.getSalonOpeningHour();
    }

    public void setClosingHour(byte hour) {
        beautySalon.setSalonClosingHour(hour);
        database.updateBeautySalon(beautySalon);
    }

    public byte getClosingHour() {
        return beautySalon.getSalonClosingHour();
    }

    public void setName(String name) {
        beautySalon.setName(name);
        database.updateBeautySalon(beautySalon);
    }

    public String getName() {
        return beautySalon.getName();
    }

    public void setLoyaltyThreshold(double threshold) {
        beautySalon.setLoyaltyThreshold(threshold);
        database.updateBeautySalon(beautySalon);
    }

    public double getLoyaltyThreshold() {
        return beautySalon.getLoyaltyThreshold();
    }

    public double getIncome() {
        return beautySalon.getSalonIncome();
    }

    public void setBonus(double bonus) {
        beautySalon.setBonus(bonus);
        database.updateBeautySalon(beautySalon);
    }

    public double getBonus() {
        return beautySalon.getBonus();
    }
}
