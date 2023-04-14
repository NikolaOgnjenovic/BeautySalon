package com.mrmi.beautysalon.main.entity;

import java.util.HashMap;

public class Client extends User {
    private double moneySpent;
    private boolean hasLoyaltyCard;

    public Client(String password, String name, String surname, String gender, String phoneNumber, String address) {
        super(password, name, surname, gender, phoneNumber, address);
        this.hasLoyaltyCard = false;
        this.moneySpent = 0;
    }

    public Client(String password, String name, String surname, String gender, String phoneNumber, String address, boolean hasLoyaltyCard, Double moneySpent) {
        super(password, name, surname, gender, phoneNumber, address);
        this.hasLoyaltyCard = hasLoyaltyCard;
        this.moneySpent = moneySpent;
    }

    public double getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent (double moneySpent) {
        this.moneySpent = moneySpent;
    }

    public void changeMoneySpent(double moneySpent, Database database) {
        this.moneySpent += moneySpent;
    }

    public boolean hasLoyaltyCard() {
        return hasLoyaltyCard;
    }

    public void setHasLoyaltyCard (boolean hasLoyaltyCard) {
        this.hasLoyaltyCard = hasLoyaltyCard;
    }

    @Override
    public String getFileString(String username) {
        return "C" + super.getFileString(username) + hasLoyaltyCard + "," + moneySpent;
    }
}
