package com.mrmi.beautysalon.objects;

public class TreatmentType {
    private String name;
    private double price;
    private int id;

    public TreatmentType(String name, double price) {
        this.name = name;
        this.price = price;
        this.id = Database.treatmentTypeId++;
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

    @Override
    public String toString() {
        return "TreatmentType{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", id=" + id +
                '}';
    }
}
