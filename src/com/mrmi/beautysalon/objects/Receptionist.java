package com.mrmi.beautysalon.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Receptionist extends Employee {

    public Receptionist(String username, String password, String name, String surname, String gender, String phoneNumber, String address) {
        super(username, password, name, surname, gender, phoneNumber, address);
    }

    public Receptionist(String username, String password, String name, String surname, String gender, String phoneNumber, String address, byte qualificationLevel, byte yearsOfExperience, double bonus, double monthlySalary) {
        super(username, password, name, surname, gender, phoneNumber, address, qualificationLevel, yearsOfExperience, bonus, monthlySalary);
    }

    public void bookTreatment(Treatment treatment, Client client, Database database) {
        double price = treatment.getPrice();
        if (client.hasLoyaltyCard()) {
            price *= 0.9;
            treatment.setPrice(price);
        }

        database.bookTreatment(treatment);
        client.changeMoneySpent(-price, database);
    }

    public List<Treatment> getAllTreatments() {
        return new ArrayList<>();
    }

    public void cancelTreatment(int treatmentId, Database database, String cancellationReason) {
        database.cancelTreatment(treatmentId, false, cancellationReason);
    }

    public void updateTreatment(Treatment treatment, Date date, int treatmentTypeId, String clientUsername, String beauticianUsername, Database database) {
        database.updateTreatment(treatment, date, treatmentTypeId, clientUsername, beauticianUsername);
    }

    @Override
    public String getFileString() {
        return "R" + super.getFileString();
    }
}
