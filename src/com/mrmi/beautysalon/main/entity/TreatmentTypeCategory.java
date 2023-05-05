package com.mrmi.beautysalon.main.entity;

import java.util.List;

public class TreatmentTypeCategory {
    private String name;
    private final List<Integer> treatmentTypeIds;
    private double profit;
    private boolean isDeleted;

    public TreatmentTypeCategory(String name, List<Integer> treatmentTypeIds, Double profit, boolean isDeleted) {
        this.name = name;
        this.treatmentTypeIds = treatmentTypeIds;
        this.profit = profit;
        this.isDeleted = isDeleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public String getFileString(int id) {
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append(",");
        sb.append(name);
        sb.append(",");
        sb.append(profit);
        sb.append(",");
        sb.append(isDeleted);
        sb.append(",");
        sb.append(";");
        for (int i : treatmentTypeIds) {
            sb.append(i);
            sb.append(";");
        }
        return sb.toString();
    }

    public List<Integer> getTreatmentTypeIds() {
        return treatmentTypeIds;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
