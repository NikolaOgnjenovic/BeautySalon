package com.mrmi.beautysalon.main.objects;

public class Employee extends User {
    private byte qualificationLevel; // Nivo strucne spreme [1-8]
    private byte yearsOfExperience;
    private double bonus;
    private double monthlySalary;

    public Employee(String password, String name, String surname, String gender, String phoneNumber, String address) {
        super(password, name, surname, gender, phoneNumber, address);
    }

    public Employee(String password, String name, String surname, String gender, String phoneNumber, String address, byte qualificationLevel, byte yearsOfExperience, double bonus, double monthlySalary) {
        super(password, name, surname, gender, phoneNumber, address);
        this.qualificationLevel = qualificationLevel;
        this.yearsOfExperience = yearsOfExperience;
        this.bonus = bonus;
        this.monthlySalary = monthlySalary;
    }

    public byte getQualificationLevel() {
        return qualificationLevel;
    }

    public void setQualificationLevel(byte qualificationLevel) {
        this.qualificationLevel = qualificationLevel;
    }

    public byte getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(byte yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public double getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(double monthlySalary) {
        this.monthlySalary = monthlySalary;
    }

    // TODO: map qualification level to index
    public double getFullSalary() {
        return (monthlySalary + bonus) * (1 + (double) yearsOfExperience / 10) * qualificationLevel;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "qualificationLevel=" + qualificationLevel +
                ", yearsOfExperience=" + yearsOfExperience +
                ", bonus=" + bonus +
                ", monthlySalary=" + monthlySalary +
                '}';
    }

    @Override
    public String getFileString(String username) {
        return super.getFileString(username) + qualificationLevel + "," + yearsOfExperience + "," + bonus + "," + monthlySalary + ",";
    }
}
