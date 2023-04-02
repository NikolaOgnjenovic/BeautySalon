package com.mrmi.beautysalon.main.objects;

public class TreatmentType {
    private String name;
    private double price;
    private int id;
    private int timesBooked;
    private double profit;

    public TreatmentType(String name, double price, int id) {
        this.name = name;
        this.price = price;
        this.id = id;
        this.timesBooked = 0;
        this.profit = 0;
    }

    public TreatmentType(String name, double price, int id, int timesBooked, double profit) {
        this.name = name;
        this.price = price;
        this.id = id;
        this.timesBooked = timesBooked;
        this.profit = profit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTimesBooked() {
        return timesBooked;
    }

    public void setTimesBooked(int timesBooked) {
        this.timesBooked = timesBooked;
    }

    public void changeTimesBooked(int amount) {
        this.timesBooked += amount;
    }
    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public void changeProfit(double amount) {
        this.profit += amount;
    }

    @Override
    public String toString() {
        return "TreatmentType{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", id=" + id +
                ", timesBooked=" + timesBooked +
                ", profit=" + profit +
                '}';
    }

    public String getFileString() {
        return name + "," + price + "," + id + "," + timesBooked + "," + profit;
    }
}
