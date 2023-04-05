package com.mrmi.beautysalon.main.objects;

import java.util.Date;
import java.util.HashMap;

public class Manager extends Employee {

    public Manager(String password, String name, String surname, String gender, String phoneNumber, String address) {
        super(password, name, surname, gender, phoneNumber, address);
    }

    public Manager(String password, String name, String surname, String gender, String phoneNumber, String address, byte qualificationLevel, byte yearsOfExperience, double bonus, double monthlySalary) {
        super(password, name, surname, gender, phoneNumber, address, qualificationLevel, yearsOfExperience, bonus, monthlySalary);
    }

    public HashMap<String, Client> getLoyalClients(Database database) {
        return database.getLoyalClients();
    }

    public HashMap<String, Employee> getEmployees(Database database) {
        return database.getEmployees();
    }

    //ishodi i rashodi za period
    // TODO: uradi kako treba
    public String getIncome(Date lowerBound, Date upperBound){
        //return String.valueOf(Database.salonIncome);
        return "";
    }

    public void addEmployee(Employee e, String username, Database database) {
        database.addUser(e, username);
    }

    @Override
    public String getFileString(String username) {
        return "M" + super.getFileString(username);
    }
}
