package com.mrmi.beautysalon.main.entity;

public class BeautySalon {
    private byte salonOpeningHour;
    private byte salonClosingHour;
    private double loyaltyThreshold;
    private double salonIncome;
    private String name;
    private double bonus;

    // Used when adding
    public BeautySalon() {
        this.salonOpeningHour = 8;
        this.salonClosingHour = 20;
        this.loyaltyThreshold = 15000;
        this.salonIncome = 0;
        this.name = "Salon";
        this.bonus = 5000;
    }

    // Used when reading from files
    public BeautySalon(byte salonOpeningHour, byte salonClosingHour, double loyaltyThreshold, double salonIncome, String name, double bonus) {
        this.salonOpeningHour = salonOpeningHour;
        this.salonClosingHour = salonClosingHour;
        this.loyaltyThreshold = loyaltyThreshold;
        this.salonIncome = salonIncome;
        this.name = name;
        this.bonus = bonus;
    }

    public byte getSalonOpeningHour() {
        return salonOpeningHour;
    }

    public void setSalonOpeningHour(byte salonOpeningHour) {
        this.salonOpeningHour = salonOpeningHour;
    }

    public byte getSalonClosingHour() {
        return salonClosingHour;
    }

    public void setSalonClosingHour(byte salonClosingHour) {
        this.salonClosingHour = salonClosingHour;
    }

    public double getLoyaltyThreshold() {
        return loyaltyThreshold;
    }

    public void setLoyaltyThreshold(double loyaltyThreshold) {
        this.loyaltyThreshold = loyaltyThreshold;
    }

    public double getSalonIncome() {
        return salonIncome;
    }

    public void setSalonIncome(double salonIncome) {
        this.salonIncome = salonIncome;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }
}
