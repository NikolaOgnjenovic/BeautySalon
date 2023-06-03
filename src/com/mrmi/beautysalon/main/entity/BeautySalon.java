package com.mrmi.beautysalon.main.entity;

public class BeautySalon {
    private byte salonOpeningHour;
    private byte salonClosingHour;
    private float loyaltyThreshold;
    private String name;
    private float bonus;

    // Used when adding
    public BeautySalon() {
        this.salonOpeningHour = 8;
        this.salonClosingHour = 20;
        this.loyaltyThreshold = 15000;
        this.name = "Salon";
        this.bonus = 5000;
    }

    // Used when reading from files
    public BeautySalon(byte salonOpeningHour, byte salonClosingHour, float loyaltyThreshold, String name, float bonus) {
        this.salonOpeningHour = salonOpeningHour;
        this.salonClosingHour = salonClosingHour;
        this.loyaltyThreshold = loyaltyThreshold;
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

    public float getLoyaltyThreshold() {
        return loyaltyThreshold;
    }

    public void setLoyaltyThreshold(float loyaltyThreshold) {
        this.loyaltyThreshold = loyaltyThreshold;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBonus() {
        return bonus;
    }

    public void setBonus(float bonus) {
        this.bonus = bonus;
    }
}
