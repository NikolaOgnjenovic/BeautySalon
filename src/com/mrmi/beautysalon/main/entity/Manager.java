package com.mrmi.beautysalon.main.entity;

public class Manager extends Employee {

    public Manager(String password, String name, String surname, String gender, String phoneNumber, String address) {
        super(password, name, surname, gender, phoneNumber, address);
    }

    public Manager(String password, String name, String surname, String gender, String phoneNumber, String address, byte qualificationLevel, byte yearsOfExperience, double bonus, double monthlySalary) {
        super(password, name, surname, gender, phoneNumber, address, qualificationLevel, yearsOfExperience, bonus, monthlySalary);
    }

    @Override
    public String getFileString(String username) {
        return "M" + super.getFileString(username);
    }
}
