package com.mrmi.beautysalon.objects;

import java.util.Date;
import java.util.List;

public class Manager extends Employee {

    public Manager(String username, String password, String name, String surname, String gender, String phoneNumber, String address) {
        super(username, password, name, surname, gender, phoneNumber, address);
    }

    public Manager(String username, String password, String name, String surname, String gender, String phoneNumber, String address, byte qualificationLevel, byte yearsOfExperience, double bonus, double monthlySalary) {
        super(username, password, name, surname, gender, phoneNumber, address, qualificationLevel, yearsOfExperience, bonus, monthlySalary);
    }

    public List<Client> getLoyalClients(Database database) {
        return database.getLoyalClients();
    }

    public List<Employee> getEmployees(Database database) {
        return database.getEmployees();
    }

    //ishodi i rashodi za period
    // TODO: uradi kako treba
    public String getIncome(Date lowerBound, Date upperBound){
        //return String.valueOf(Database.salonIncome);
        return "";
    }

    public void addEmployee(Employee e, Database database) {
        database.addUser(e);
    }

    @Override
    public String getFileString() {
        return "M" + super.getFileString();
    }
}
