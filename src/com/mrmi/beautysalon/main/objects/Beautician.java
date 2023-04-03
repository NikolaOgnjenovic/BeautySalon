package com.mrmi.beautysalon.main.objects;

import java.util.HashMap;
import java.util.List;

public class Beautician extends Employee {
    private List<Byte> treatmentTypeIDs;

    public Beautician(String password, String name, String surname, String gender, String phoneNumber, String address, List<Byte> treatmentTypeIDs) {
        super(password, name, surname, gender, phoneNumber, address);
        this.treatmentTypeIDs = treatmentTypeIDs;
    }

    public Beautician(String password, String name, String surname, String gender, String phoneNumber, String address, List<Byte> treatmentTypeIDs, byte qualificationLevel, byte yearsOfExperience, double bonus, double monthlySalary) {
        super(password, name, surname, gender, phoneNumber, address, qualificationLevel, yearsOfExperience, bonus, monthlySalary);
        this.treatmentTypeIDs = treatmentTypeIDs;
    }

    public HashMap<Integer, Treatment> getDueTreatments(Database database, String username) {
        return database.getBeauticianDueTreatments(username);
    }

    public HashMap<Integer, Treatment> getPastTreatments(Database database, String username) {
        return database.getBeauticianPastTreatments(username);
    }

    // TODO : GUI
    public String getSchedule() {
        return "";
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
}
