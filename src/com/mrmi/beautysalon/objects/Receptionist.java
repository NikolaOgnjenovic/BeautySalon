package com.mrmi.beautysalon.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Receptionist extends Employee {

    public Receptionist(String username, String password, String name, String surname, boolean isMale, String phoneNumber, String address) {
        super(username, password, name, surname, isMale, phoneNumber, address);
    }

    public void bookTreatment(Treatment treatment, Client client) {
        double price = treatment.getType().getPrice();
        if (client.hasLoyaltyCard()) {
            price *= 0.9;
        }
        Database.changeProfit(price);
        client.changeMoneySpent(-price);
    }

    public List<Treatment> getAllTreatments() {
        return new ArrayList<>();
    }

    public void cancelTreatment(int treatmentId, Database database) {
        for (Treatment t : getAllTreatments()) {
            if (t.getId() == treatmentId) {
                t.setCancelled(true);
                database.getClientByUsername(t.getClientUsername()).changeMoneySpent(t.getType().getPrice());
                Database.changeProfit(-t.getType().getPrice());
                return;
            }
        }
        System.out.println("Treatment with id " + treatmentId + " not found.");
    }

    public void updateTreatment(Treatment treatment, Date date, TreatmentType treatmentType, String clientUsername, String beauticianUsername, Database database) {
        database.updateTreatment(treatment, date, treatmentType, clientUsername, beauticianUsername);
    }
}
