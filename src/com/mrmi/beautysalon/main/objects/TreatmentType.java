package com.mrmi.beautysalon.main.objects;

public class TreatmentType {
    private String name;
    private double price;
    //private int id;
    private int timesBooked;
    private double profit;

public TreatmentType(String name, double price) {
    this.name = name;
    this.price = price;
    this.timesBooked = 0;
    this.profit = 0;
}
    public TreatmentType(String name, double price, int timesBooked, double profit) {
        this.name = name;
        this.price = price;
        this.timesBooked = timesBooked;
        this.profit = profit;
    }

    public double getPrice() {
        return price;
    }

    public void changeTimesBooked(int amount) {
        this.timesBooked += amount;
    }

    public void changeProfit(double amount) {
        this.profit += amount;
    }

    public String getDisplayString(int id) {
        return "ID: " + id + ", name: " + name + ", price: " + price;
    }
    @Override
    public String toString() {
        return "TreatmentType{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", timesBooked=" + timesBooked +
                ", profit=" + profit +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTimesBooked() {
        return timesBooked;
    }

    public void setTimesBooked(int timesBooked) {
        this.timesBooked = timesBooked;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public String getFileString(int id) {
        return id + "," + name + "," + price + "," + timesBooked + "," + profit;
    }
}
