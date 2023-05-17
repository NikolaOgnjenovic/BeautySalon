package com.mrmi.beautysalon.main.entity;

public class Manager extends Employee {

    // Used when adding
    public Manager(String username, String password, String name, String surname, String gender, String phoneNumber, String address, byte qualificationLevel, byte yearsOfExperience, double monthlySalary) {
        super(username, password, name, surname, gender, phoneNumber, address, qualificationLevel, yearsOfExperience, monthlySalary);
    }

    // Used when reading from files
    public Manager(int id, String username, String password, String name, String surname, String gender, String phoneNumber, String address, byte qualificationLevel, byte yearsOfExperience, double monthlySalary) {
        super(id, username, password, name, surname, gender, phoneNumber, address, qualificationLevel, yearsOfExperience, monthlySalary);
    }

    @Override
    public String getFileString() {
        return "M" + super.getFileString();
    }
}
