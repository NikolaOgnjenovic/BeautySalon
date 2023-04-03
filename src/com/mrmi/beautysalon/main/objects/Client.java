package com.mrmi.beautysalon.main.objects;

import java.util.HashMap;
import java.util.List;

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

    public void bookTreatment(Treatment treatment, Database database) {
        double price = treatment.getPrice();
        if (hasLoyaltyCard) {
            price *= 0.9;
            treatment.setPrice(price);
        }
        database.bookTreatment(treatment, database.getNextTreatmentId());
        this.changeMoneySpent(-price, database);
    }

    public HashMap<Integer, Treatment> getTreatments(Database database, String username) {
        return database.getClientTreatments(username);
    }
    public HashMap<Integer, Treatment> getDueTreatments(Database database, String username) {
        return database.getClientDueTreatments(username);
    }

    public HashMap<Integer, Treatment> getPastTreatments(Database database, String username) {
        return database.getClientPastTreatments(username);
    }

    public void cancelTreatment(int treatmentId, Database database, String cancellationReason) {
        database.cancelTreatment(treatmentId, true, cancellationReason);
    }

    public double getMoneySpent() {
        return moneySpent;
    }

    public void changeMoneySpent(double moneySpent, Database database) {
        this.moneySpent += moneySpent;
        checkLoyaltyCard(database);
    }

    public boolean hasLoyaltyCard() {
        return hasLoyaltyCard;
    }

    private void checkLoyaltyCard(Database database) {
        this.hasLoyaltyCard = this.moneySpent >= database.getLoyaltyThreshold();
    }

    @Override
    public String getFileString(String username) {
        return "C" + super.getFileString(username) + hasLoyaltyCard + "," + moneySpent;
    }
}
