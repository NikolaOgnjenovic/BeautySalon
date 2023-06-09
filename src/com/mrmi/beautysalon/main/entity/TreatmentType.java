package com.mrmi.beautysalon.main.entity;

import com.mrmi.beautysalon.main.exceptions.TreatmentTypeCategoryNotFoundException;
import com.mrmi.beautysalon.main.manager.TreatmentManager;

public class TreatmentType implements TableCell {
    private int id;
    private String name;
    private float price;
    private int categoryId;
    private int duration;
    private boolean isDeleted;

    // Used when adding
    public TreatmentType(String name, float price, int categoryId, int duration) {
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.duration = duration;
        this.isDeleted = false;
    }

    // Used when reading from files
    public TreatmentType(int id, String name, float price, int categoryId, int duration, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.duration = duration;
        this.isDeleted = isDeleted;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getFileString() {
        return id + "," + name + "," + price + "," + categoryId + "," + duration + "," + isDeleted;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public Object getCell(int column, Object manager) {
        TreatmentManager treatmentManager = (TreatmentManager) manager;
        switch (column) {
            case 0:
                return id;
            case 1:
                return name;
            case 2:
                try {
                    return treatmentManager.getTreatmentTypeCategoryName(categoryId);
                } catch (TreatmentTypeCategoryNotFoundException e) {
                    return "Deleted treatment type category";
                }
            case 3:
                return price;
            case 4:
                return duration;
            case 5:
                return treatmentManager.getTimesBooked(id);
            case 6:
                return treatmentManager.getProfit(id);
            case 7:
                return isDeleted;
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Id";
            case 1:
                return "Name";
            case 2:
                return "Category";
            case 3:
                return "Price";
            case 4:
                return "Duration";
            case 5:
                return "Times booked";
            case 6:
                return "Total profit";
            case 7:
                return "Is deleted";
            default:
                return null;
        }
    }

    @Override
    public int getColumnCount() {
        return 8;
    }
}
