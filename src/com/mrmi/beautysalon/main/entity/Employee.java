package com.mrmi.beautysalon.main.entity;

import com.mrmi.beautysalon.main.manager.TreatmentManager;

import java.util.Calendar;

public class Employee extends User {
    private final byte qualificationLevel; // [1-8]
    private final byte yearsOfExperience;
    private final float monthlySalary;
    private final Calendar hiringDate;

    // Used when adding
    public Employee(String username, String password, String name, String surname, String gender, String phoneNumber, String address, byte qualificationLevel, byte yearsOfExperience, float monthlySalary) {
        super(username, password, name, surname, gender, phoneNumber, address);
        this.qualificationLevel = qualificationLevel;
        this.yearsOfExperience = yearsOfExperience;
        this.monthlySalary = monthlySalary;
        this.hiringDate = Calendar.getInstance();
    }

    // Used when reading from files
    public Employee(int id, String username, String password, String name, String surname, String gender, String phoneNumber, String address, byte qualificationLevel, byte yearsOfExperience, float monthlySalary, Calendar hiringDate) {
        super(id, username, password, name, surname, gender, phoneNumber, address);
        this.qualificationLevel = qualificationLevel;
        this.yearsOfExperience = yearsOfExperience;
        this.monthlySalary = monthlySalary;
        this.hiringDate = hiringDate;
    }

    public byte getQualificationLevel() {
        return qualificationLevel;
    }


    public byte getYearsOfExperience() {
        return yearsOfExperience;
    }

    public float getFullSalary(float bonusIncome) {
        Calendar today = Calendar.getInstance();
        // The salary increases every 6 months by the entered bonus
        float bonus = today.get(Calendar.YEAR) * 12 + today.get(Calendar.MONTH) - hiringDate.get(Calendar.YEAR) * 12 - hiringDate.get(Calendar.MONTH) / 6f;
        bonus *= bonusIncome;
        return monthlySalary + qualificationLevel * 3000 + yearsOfExperience * 1000 + bonus;
    }
    public float getMonthlySalary() {
        return monthlySalary;
    }

    @Override
    public String getFileString() {
        return super.getFileString() + qualificationLevel + "," + yearsOfExperience + "," + monthlySalary + "," + hiringDate + ",";
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
               return getFullSalary(((TreatmentManager) manager).getBonus());
            case 11:
                return hiringDate.getTime();
        }

        return null;
    }
}
