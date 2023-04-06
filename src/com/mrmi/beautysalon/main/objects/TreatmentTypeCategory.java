package com.mrmi.beautysalon.main.objects;

import java.util.List;

public class TreatmentTypeCategory {
    private String name;
    private List<Integer> treatmentTypeIds;
    private double profit;

    public TreatmentTypeCategory(String name, List<Integer> treatmentTypeIds, Double profit) {
        this.name = name;
        this.treatmentTypeIds = treatmentTypeIds;
        this.profit = profit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getTreatmentTypeIds() {
        return treatmentTypeIds;
    }

    public void setTreatmentTypeIds(List<Integer> treatmentTypeIds) {
        this.treatmentTypeIds = treatmentTypeIds;
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
        sb.append(";");
        for (int i : treatmentTypeIds) {
            sb.append(i);
            sb.append(";");
        }
        return sb.toString();
    }
}
