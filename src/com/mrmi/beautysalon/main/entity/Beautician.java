package com.mrmi.beautysalon.main.entity;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Beautician extends Employee {
    private List<Byte> treatmentTypeIDs;
    private int finishedTreatments;

    public Beautician(String password, String name, String surname, String gender, String phoneNumber, String address, List<Byte> treatmentTypeIDs, int finishedTreatments) {
        super(password, name, surname, gender, phoneNumber, address);
        this.treatmentTypeIDs = treatmentTypeIDs;
        this.finishedTreatments = finishedTreatments;
    }

    public Beautician(String password, String name, String surname, String gender, String phoneNumber, String address, List<Byte> treatmentTypeIDs, byte qualificationLevel, byte yearsOfExperience, double bonus, double monthlySalary, int finishedTreatments) {
        super(password, name, surname, gender, phoneNumber, address, qualificationLevel, yearsOfExperience, bonus, monthlySalary);
        this.treatmentTypeIDs = treatmentTypeIDs;
        this.finishedTreatments = finishedTreatments;
    }

    public List<Byte> getTreatmentTypeIDs() {
        return treatmentTypeIDs;
    }

    public void setTreatmentTypeIDs(List<Byte> treatmentTypeIDs) {
        this.treatmentTypeIDs = treatmentTypeIDs;
    }

    @Override
    public String getFileString(String username) {
        StringBuilder sb = new StringBuilder();
        sb.append("B");
        sb.append(super.getFileString(username));
        sb.append(finishedTreatments);
        sb.append(",");
        for (Byte type : treatmentTypeIDs) {
            sb.append(type);
            sb.append(";");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return super.toString() + "Beautician{" +
                "treatmentTypeIDs=" + treatmentTypeIDs +
                '}';
    }

    public int getFinishedTreatments() {
        return finishedTreatments;
    }

    public void setFinishedTreatments(int finishedTreatments) {
        this.finishedTreatments = finishedTreatments;
    }
}
