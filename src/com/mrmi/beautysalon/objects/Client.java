package com.mrmi.beautysalon.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Client extends User {
    private double moneySpent;
    private boolean hasLoyaltyCard;

    public Client(String username, String password, String name, String surname, boolean isMale, String phoneNumber, String address) {
        super(username, password, name, surname, isMale, phoneNumber, address);
        this.hasLoyaltyCard = false;
        this.moneySpent = 0;
    }

    public void bookTreatment(Treatment treatment) {
        double price = treatment.getType().getPrice();
        if (this.hasLoyaltyCard) {
            price *= 0.9;
        }
        Database.changeProfit(price);
        this.changeMoneySpent(-price);
    }

    public List<Treatment> getDueTreatments(Database database) {
        List<Treatment> dueTreatments = new ArrayList<>();
        List<Treatment> userTreatments = database.getClientTreatments(this.getUsername());
        Date currentDate = new Date();
        for (Treatment t : userTreatments) {
            if (t.getScheduledDate().after(currentDate)) {
                dueTreatments.add(t);
            }
        }

        return dueTreatments;
    }

    public List<Treatment> getPastTreatments(Database database) {
        List<Treatment> pastTreatments = new ArrayList<>();
        List<Treatment> userTreatments = database.getClientTreatments(this.getUsername());
        Date currentDate = new Date();
        for (Treatment t : userTreatments) {
            if (t.getScheduledDate().before(currentDate)) {
                pastTreatments.add(t);
            }
        }

        return pastTreatments;
    }

    /*
     Posle otkazanog tretmana na zahtev
klijenta, klijentu se vraća 90% uplaćenog novca dok 10% kozmetički salon zadržava
u cilju pokrivanja gubitaka. Ukoliko salon otkaže, klijentu se vraća 100% novca.
Ukoliko se klijent ne pojavi, salon zadržava sav uplaćen novac.
     */
    public void cancelTreatment(int treatmentId, Database database) {
        List<Treatment> treatments = getDueTreatments(database);
        for (Treatment t : treatments) {
            if (t.getId() == treatmentId) {
                t.setCancelled(true);
                changeMoneySpent(-0.9 * t.getType().getPrice());
                return;
            }
        }
        System.out.println("Treatment with id " + treatmentId + " not found.");
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
}
