package com.mrmi.beautysalon.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Receptionist extends Employee {

    public Receptionist(String username, String password, String name, String surname, String gender, String phoneNumber, String address) {
        super(username, password, name, surname, gender, phoneNumber, address);
    }

    public void bookTreatment(Treatment treatment, Client client, Database database) {
        double price = treatment.getPrice();
        if (client.hasLoyaltyCard()) {
            price *= 0.9;
            treatment.setPrice(price);
        }

        database.bookTreatment(treatment);
        client.changeMoneySpent(-price);
    }

    public List<Treatment> getAllTreatments() {
        return new ArrayList<>();
    }

    public void cancelTreatment(int treatmentId, Database database) {
        database.cancelTreatment(treatmentId, false);
    }

    public void updateTreatment(Treatment treatment, Date date, int treatmentTypeId, String clientUsername, String beauticianUsername, Database database) {
        database.updateTreatment(treatment, date, treatmentTypeId, clientUsername, beauticianUsername);
    }

    @Override
    public String getFileString() {
        return "R" + super.getFileString();
    }
}
