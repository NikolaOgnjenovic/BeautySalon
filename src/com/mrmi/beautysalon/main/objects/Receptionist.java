package com.mrmi.beautysalon.main.objects;

import java.util.Date;
import java.util.HashMap;

public class Receptionist extends Employee {

    public Receptionist(String password, String name, String surname, String gender, String phoneNumber, String address) {
        super(password, name, surname, gender, phoneNumber, address);
    }

    public Receptionist(String password, String name, String surname, String gender, String phoneNumber, String address, byte qualificationLevel, byte yearsOfExperience, double bonus, double monthlySalary) {
        super(password, name, surname, gender, phoneNumber, address, qualificationLevel, yearsOfExperience, bonus, monthlySalary);
    }

    public void bookTreatment(Treatment treatment, Client client, Database database) {
        double price = treatment.getPrice();
        if (client.hasLoyaltyCard()) {
            price *= 0.9;
            treatment.setPrice(price);
        }

        database.bookTreatment(treatment, database.getNextTreatmentId());
        client.changeMoneySpent(-price, database);
    }

    public HashMap<Integer, Treatment> getAllTreatments(Database database) {
        return database.getTreatments();
    }

    public void cancelTreatment(int treatmentId, Database database, String cancellationReason) {
        database.cancelTreatment(treatmentId, false, cancellationReason);
    }

    public void updateTreatment(Treatment treatment, Date date, int treatmentTypeId, String clientUsername, String beauticianUsername, Database database) {
        database.updateTreatment(treatment, date, treatmentTypeId, clientUsername, beauticianUsername);
    }

    @Override
    public String getFileString(String username) {
        return "R" + super.getFileString(username);
    }
}
