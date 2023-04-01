package com.mrmi.beautysalon.objects;

import java.util.Date;
import java.util.List;

public class Manager extends User {
    public static int LoyaltyThreshold;

    public Manager(String username, String password, String name, String surname, boolean isMale, String phoneNumber, String address) {
        super(username, password, name, surname, isMale, phoneNumber, address);
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
        return String.valueOf(Database.salonIncome);
    }
}
