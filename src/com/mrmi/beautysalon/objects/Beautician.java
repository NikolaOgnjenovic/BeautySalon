package com.mrmi.beautysalon.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Beautician extends Employee {
    private List<TreatmentType> availableTreatments;

    public Beautician(String username, String password, String name, String surname, boolean isMale, String phoneNumber, String address) {
        super(username, password, name, surname, isMale, phoneNumber, address);
    }

    public List<Treatment> getDueTreatments(Database database) {
        List<Treatment> dueTreatments = new ArrayList<>();
        List<Treatment> beauticianTreatments = database.getBeauticianTreatments(this.getUsername());
        Date currentDate = new Date();
        for (Treatment t : beauticianTreatments) {
            if (t.getScheduledDate().after(currentDate)) {
                dueTreatments.add(t);
            }
        }

        return dueTreatments;
    }

    public List<Treatment> getPastTreatments(Database database) {
        List<Treatment> pastTreatments = new ArrayList<>();
        List<Treatment> beauticianTreatments = database.getBeauticianTreatments(this.getUsername());
        Date currentDate = new Date();
        for (Treatment t : beauticianTreatments) {
            if (t.getScheduledDate().before(currentDate)) {
                pastTreatments.add(t);
            }
        }

        return pastTreatments;
    }

    // TODO : GUI
    public String getSchedule() {
        return "";
    }

    public List<TreatmentType> getAvailableTreatments() {
        return availableTreatments;
    }

    public void setAvailableTreatments(List<TreatmentType> availableTreatments) {
        this.availableTreatments = availableTreatments;
    }
}
