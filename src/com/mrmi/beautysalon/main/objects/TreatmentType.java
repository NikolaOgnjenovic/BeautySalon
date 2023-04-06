package com.mrmi.beautysalon.main.objects;

public class TreatmentType {
    private String name;
    private double price;
    private int timesBooked;
    private double profit;
    private String categoryName;
    private byte duration;
    public TreatmentType(String name, double price, String categoryName, byte duration) {
        this.name = name;
        this.price = price;
        this.timesBooked = 0;
        this.profit = 0;
        this.categoryName = categoryName;
        this.duration = duration;
    }
    public TreatmentType(String name, double price, int timesBooked, double profit, String categoryName, byte duration) {
        this.name = name;
        this.price = price;
        this.timesBooked = timesBooked;
        this.profit = profit;
        this.categoryName = categoryName;
        this.duration = duration;
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
        return "ID: " + id + ", name: " + name + ", price: " + price + ", duration: " + duration;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getFileString(int id) {
        return id + "," + name + "," + price + "," + timesBooked + "," + profit + "," + categoryName + "," + duration;
    }

    public byte getDuration() {
        return duration;
    }

    public void setDuration(byte duration) {
        this.duration = duration;
    }
}
