package com.mrmi.beautysalon.main.entity;


public class TreatmentTypeCategory implements TableCell {
    private int id;
    private String name;
    private boolean isDeleted;

    public TreatmentTypeCategory(int id, String name) {
        this.id = id;
        this.name = name;
        this.isDeleted = false;
    }
    public TreatmentTypeCategory(int id, String name, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.isDeleted = isDeleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileString() {
        return id + "," + name + "," + isDeleted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public Object getCell(int column, Object manager) {
        switch (column) {
            case 0:
                return id;
            case 1:
                return name;
            case 2:
                return isDeleted;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Id";
            case 1:
                return "Name";
            case 2:
                return "Is deleted";
            default:
                return null;
        }
    }

    @Override
    public int getColumnCount() {
        return 3;
    }
}
