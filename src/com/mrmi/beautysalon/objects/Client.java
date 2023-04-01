package com.mrmi.beautysalon.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Client extends User {
    private double moneySpent;
    private boolean hasLoyaltyCard;

    public Client(String username, String password, String name, String surname, String gender, String phoneNumber, String address) {
        super(username, password, name, surname, gender, phoneNumber, address);
        this.hasLoyaltyCard = false;
        this.moneySpent = 0;
    }

    public void bookTreatment(Treatment treatment, Database database) {
        double price = treatment.getPrice();
        if (hasLoyaltyCard) {
            price *= 0.9;
            treatment.setPrice(price);
        }
        database.bookTreatment(treatment);
        this.changeMoneySpent(-price);
    }

    public List<Treatment> getDueTreatments(Database database) {
        return database.getClientDueTreatments(this.getUsername());
    }

    public List<Treatment> getPastTreatments(Database database) {
        return database.getClientPastTreatments(this.getUsername());
    }

    public void cancelTreatment(int treatmentId, Database database) {
        database.cancelTreatment(treatmentId, true);
    }

    public double getMoneySpent() {
        return moneySpent;
    }

    public void changeMoneySpent(double moneySpent) {
        this.moneySpent += moneySpent;
        checkLoyaltyCard();
    }

    public boolean hasLoyaltyCard() {
        return hasLoyaltyCard;
    }

    private void checkLoyaltyCard() {
        this.hasLoyaltyCard = this.moneySpent >= Manager.LoyaltyThreshold;
    }

    @Override
    public String getFileString() {
        return "C" + super.getFileString();
    }
}
