package com.mrmi.beautysalon.main.entity;

public class Employee extends User {
    private byte qualificationLevel; // Nivo strucne spreme [1-8]
    private byte yearsOfExperience;
    private double bonus;
    private double monthlySalary;

    public Employee(int id, String username, String password, String name, String surname, String gender, String phoneNumber, String address) {
        super(id, username, password, name, surname, gender, phoneNumber, address);
    }

    public Employee(int id, String username, String password, String name, String surname, String gender, String phoneNumber, String address, byte qualificationLevel, byte yearsOfExperience, double bonus, double monthlySalary) {
        super(id, username, password, name, surname, gender, phoneNumber, address);
        this.qualificationLevel = qualificationLevel;
        this.yearsOfExperience = yearsOfExperience;
        this.bonus = bonus;
        this.monthlySalary = monthlySalary;
    }

    public byte getQualificationLevel() {
        return qualificationLevel;
    }


    public byte getYearsOfExperience() {
        return yearsOfExperience;
    }


    public double getBonus() {
        return bonus;
    }

    public double getMonthlySalary() {
        return monthlySalary;
    }


    public double getFullSalary() {
        return (monthlySalary + bonus) * (1 + (double) yearsOfExperience / 10) * qualificationLevel;
    }

    @Override
    public String getFileString() {
        return super.getFileString() + qualificationLevel + "," + yearsOfExperience + "," + bonus + "," + monthlySalary + ",";
    }

    @Override
    public Object getCell(int column, Object manager) {
        if (column < 8) {
            return super.getCell(column, manager);
        }

        switch (column) {
            case 8:
                return qualificationLevel;
            case 9:
                return yearsOfExperience;
            case 10:
                return monthlySalary;
            case 11:
                return bonus;
        }

        return null;
    }
}
