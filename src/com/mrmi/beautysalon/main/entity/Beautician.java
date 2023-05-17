package com.mrmi.beautysalon.main.entity;

import com.mrmi.beautysalon.main.manager.TreatmentManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Beautician extends Employee {
    private final List<Integer> treatmentTypeCategoryIDs;

    // Used when adding
    public Beautician(String username, String password, String name, String surname, String gender, String phoneNumber, String address, byte qualificationLevel, byte yearsOfExperience, double monthlySalary) {
        super(username, password, name, surname, gender, phoneNumber, address, qualificationLevel, yearsOfExperience, monthlySalary);
        this.treatmentTypeCategoryIDs = new ArrayList<>();
    }

    // Used when reading from files
    public Beautician(int id, String username, String password, String name, String surname, String gender, String phoneNumber, String address, ArrayList<Integer> treatmentTypeCategoryIDs, byte qualificationLevel, byte yearsOfExperience, double monthlySalary) {
        super(id, username, password, name, surname, gender, phoneNumber, address, qualificationLevel, yearsOfExperience, monthlySalary);
        this.treatmentTypeCategoryIDs = treatmentTypeCategoryIDs;
    }

    public List<Integer> getTreatmentTypeCategoryIDs() {
        return treatmentTypeCategoryIDs;
    }

    public void addTreatmentTypeCategoryID(int treatmentTypeCategoryId) {
        if (!treatmentTypeCategoryIDs.contains(treatmentTypeCategoryId)) {
            treatmentTypeCategoryIDs.add(treatmentTypeCategoryId);
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
        for (int category : treatmentTypeCategoryIDs) {
            sb.append(category);
            sb.append(";");
        }
        return sb.toString();
    }

    @Override
    public Object getCell(int column, Object manager) {
        if (column != 11) {
            return super.getCell(column, manager);
        }

        StringBuilder types = new StringBuilder();
        HashMap<Integer, TreatmentTypeCategory> treatmentTypeCategories = ((TreatmentManager) manager).getTreatmentTypeCategories();
        for (int id : treatmentTypeCategoryIDs) {
            TreatmentTypeCategory type = treatmentTypeCategories.get(id);
            if (type != null) {
                types.append(type.getName());
                types.append(", ");
            }
        }
        return types.toString();
    }
}
