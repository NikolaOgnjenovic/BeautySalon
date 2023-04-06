package com.mrmi.beautysalon.main.objects;

import java.util.Comparator;
import java.util.Date;
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

    public String getSchedule(Database database, String username) {
        List<Treatment> treatments = database.getBeauticianTreatments(username).values().stream().sorted(Comparator.comparing(Treatment::getScheduledDate)).toList();
        if (treatments.size() < 1) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Date currentDate = treatments.get(0).getScheduledDate();
        sb.append(currentDate);
        sb.append("\n");
        for (Treatment t : treatments) {
            if (t.getScheduledDate().getDate() > currentDate.getDate()) {
                currentDate = t.getScheduledDate();
                sb.append(currentDate);
            }
            sb.append(t);
            sb.append("\n");
        }

        return sb.toString();
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
