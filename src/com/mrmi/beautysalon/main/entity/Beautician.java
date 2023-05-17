package com.mrmi.beautysalon.main.entity;

import com.mrmi.beautysalon.main.manager.TreatmentManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Beautician extends Employee {
    private final List<Integer> categoryIDs;

    // Used when adding
    public Beautician(String username, String password, String name, String surname, String gender, String phoneNumber, String address, byte qualificationLevel, byte yearsOfExperience, float monthlySalary) {
        super(username, password, name, surname, gender, phoneNumber, address, qualificationLevel, yearsOfExperience, monthlySalary);
        this.categoryIDs = new ArrayList<>();
    }

    // Used when reading from files
    public Beautician(int id, String username, String password, String name, String surname, String gender, String phoneNumber, String address, ArrayList<Integer> categoryIDs, byte qualificationLevel, byte yearsOfExperience, float monthlySalary, Calendar hiringDate) {
        super(id, username, password, name, surname, gender, phoneNumber, address, qualificationLevel, yearsOfExperience, monthlySalary, hiringDate);
        this.categoryIDs = categoryIDs;
    }

    public List<Integer> getTreatmentTypeCategoryIDs() {
        return categoryIDs;
    }

    public void addTreatmentTypeCategoryID(int categoryId) {
        if (!categoryIDs.contains(categoryId)) {
            categoryIDs.add(categoryId);
        }
    }

    @Override
    public String toString() {
        return getName() + " " + getSurname();
    }

    @Override
    public String getFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append("B");
        sb.append(super.getFileString());
        sb.append(";");
        for (int category : categoryIDs) {
            sb.append(category);
            sb.append(";");
        }
        return sb.toString();
    }

    @Override
    public Object getCell(int column, Object manager) {
        if (column != 12) {
            return super.getCell(column, manager);
        }

        StringBuilder types = new StringBuilder();
        HashMap<Integer, TreatmentTypeCategory> categories = ((TreatmentManager) manager).getTreatmentTypeCategories();
        for (int id : categoryIDs) {
            TreatmentTypeCategory type = categories.get(id);
            if (type != null) {
                types.append(type.getName());
                types.append(", ");
            }
        }
        return types.toString();
    }
}
