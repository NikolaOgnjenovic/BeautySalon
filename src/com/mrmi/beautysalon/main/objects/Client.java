package com.mrmi.beautysalon.main.objects;

import java.util.List;

public class Client extends User {
    private double moneySpent;
    private boolean hasLoyaltyCard;

    public Client(String username, String password, String name, String surname, String gender, String phoneNumber, String address) {
        super(username, password, name, surname, gender, phoneNumber, address);
        this.hasLoyaltyCard = false;
        this.moneySpent = 0;
    }

    public Client(String username, String password, String name, String surname, String gender, String phoneNumber, String address, boolean hasLoyaltyCard, Double moneySpent) {
        super(username, password, name, surname, gender, phoneNumber, address);
        this.hasLoyaltyCard = hasLoyaltyCard;
        this.moneySpent = moneySpent;
    }

    public void bookTreatment(Treatment treatment, Database database) {
        double price = treatment.getPrice();
        if (hasLoyaltyCard) {
            price *= 0.9;
            treatment.setPrice(price);
        }
        database.bookTreatment(treatment);
        this.changeMoneySpent(-price, database);
    }

    public List<Treatment> getDueTreatments(Database database) {
        return database.getClientDueTreatments(this.getUsername());
    }

    public List<Treatment> getPastTreatments(Database database) {
        return database.getClientPastTreatments(this.getUsername());
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
    public String getFileString() {
        return "C" + super.getFileString() + hasLoyaltyCard + "," + moneySpent;
    }
}
