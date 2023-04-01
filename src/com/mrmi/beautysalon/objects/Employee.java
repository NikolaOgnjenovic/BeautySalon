package com.mrmi.beautysalon.objects;

public class Employee extends User {
    private String qualifications;
    private byte yearsOfExperience;
    private int bonus;
    private int salary;

    public Employee(String username, String password, String name, String surname, boolean isMale, String phoneNumber, String address) {
        super(username, password, name, surname, isMale, phoneNumber, address);
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public byte getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(byte yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public int getSalary() {
        return salary + bonus + yearsOfExperience * Integer.parseInt(qualifications);
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return super.toString() + "Employee{" +
                "qualifications='" + qualifications + '\'' +
                ", yearsOfExperience=" + yearsOfExperience +
                ", bonus=" + bonus +
                ", salary=" + salary +
                '}';
    }
}
