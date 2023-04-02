package com.mrmi.beautysalon.objects;

import java.util.List;

public class Beautician extends Employee {
    private List<Byte> treatmentTypeIDs;

    public Beautician(String username, String password, String name, String surname, String gender, String phoneNumber, String address, List<Byte> treatmentTypeIDs) {
        super(username, password, name, surname, gender, phoneNumber, address);
        this.treatmentTypeIDs = treatmentTypeIDs;
    }

    public Beautician(String username, String password, String name, String surname, String gender, String phoneNumber, String address, List<Byte> treatmentTypeIDs, byte qualificationLevel, byte yearsOfExperience, double bonus, double monthlySalary) {
        super(username, password, name, surname, gender, phoneNumber, address, qualificationLevel, yearsOfExperience, bonus, monthlySalary);
        this.treatmentTypeIDs = treatmentTypeIDs;
    }

    public List<Treatment> getDueTreatments(Database database) {
        return database.getBeauticianDueTreatments(this.getUsername());
    }

    public List<Treatment> getPastTreatments(Database database) {
        return database.getBeauticianPastTreatments(this.getUsername());
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
    public String getFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append("B");
        sb.append(super.getFileString());
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
