package com.mrmi.beautysalon.main.entity;

import java.util.Date;
import java.util.HashMap;

public class Receptionist extends Employee {

    public Receptionist(String password, String name, String surname, String gender, String phoneNumber, String address) {
        super(password, name, surname, gender, phoneNumber, address);
    }

    public Receptionist(String password, String name, String surname, String gender, String phoneNumber, String address, byte qualificationLevel, byte yearsOfExperience, double bonus, double monthlySalary) {
        super(password, name, surname, gender, phoneNumber, address, qualificationLevel, yearsOfExperience, bonus, monthlySalary);
    }

    @Override
    public String getFileString(String username) {
        return "R" + super.getFileString(username);
    }
}
