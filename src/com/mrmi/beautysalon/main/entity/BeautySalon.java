package com.mrmi.beautysalon.main.entity;

public class BeautySalon {
    private byte salonOpeningHour;
    private byte salonClosingHour;
    private double loyaltyThreshold;
    private double salonIncome;

    public BeautySalon(byte salonOpeningHour, byte salonClosingHour, double loyaltyThreshold, double salonIncome) {
        this.salonOpeningHour = salonOpeningHour;
        this.salonClosingHour = salonClosingHour;
        this.loyaltyThreshold = loyaltyThreshold;
        this.salonIncome = salonIncome;
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
}
