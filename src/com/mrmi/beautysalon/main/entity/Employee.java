package com.mrmi.beautysalon.main.entity;

public class Employee extends User {
    private final byte qualificationLevel; // [1-8]
    private final byte yearsOfExperience;
    private final float monthlySalary;

    // Used when adding
    public Employee(String username, String password, String name, String surname, String gender, String phoneNumber, String address, byte qualificationLevel, byte yearsOfExperience, float monthlySalary) {
        super(username, password, name, surname, gender, phoneNumber, address);
        this.qualificationLevel = qualificationLevel;
        this.yearsOfExperience = yearsOfExperience;
        this.monthlySalary = monthlySalary;
    }

    // Used when reading from files
    public Employee(int id, String username, String password, String name, String surname, String gender, String phoneNumber, String address, byte qualificationLevel, byte yearsOfExperience, float monthlySalary) {
        super(id, username, password, name, surname, gender, phoneNumber, address);
        this.qualificationLevel = qualificationLevel;
        this.yearsOfExperience = yearsOfExperience;
        this.monthlySalary = monthlySalary;
    }

    public byte getQualificationLevel() {
        return qualificationLevel;
    }


    public byte getYearsOfExperience() {
        return yearsOfExperience;
    }



    public float getMonthlySalary() {
        return monthlySalary;
    }


    @Override
    public String getFileString() {
        return super.getFileString() + qualificationLevel + "," + yearsOfExperience + "," + monthlySalary + ",";
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
        }

        return null;
    }
}
