package com.mrmi.beautysalon.objects;

import java.util.Date;

public class Treatment {
    private int id;
    private Date scheduledDate; // Zbog pojednostavljivanja, smatrati da tretmani počinju uvek na pun sat.
    private boolean cancelled;

    private String clientUsername;

    private String beauticianUsername;

    private int treatmentTypeId;

    private Double price; // Although treatmentType holds price, this price can be affected by a loyalty discount

    public Treatment(Date scheduledDate, int treatmentTypeId, String clientUsername, int id, double price) {
        this.scheduledDate = scheduledDate;
        this.cancelled = false;
        this.treatmentTypeId = treatmentTypeId;
        this.clientUsername = clientUsername;
        this.price = price;
        this.id = id;
    }

    public Date getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTreatmentTypeId() {
        return treatmentTypeId;
    }

    public void setTreatmentTypeId(int treatmentTypeId) {
        this.treatmentTypeId = treatmentTypeId;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public int getId() {
        return id;
    }

    public String getBeauticianUsername() {
        return beauticianUsername;
    }

    public void setBeauticianUsername(String beauticianUsername) {
        this.beauticianUsername = beauticianUsername;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getFileString() {
        return id + "," + scheduledDate + "," + cancelled + "," + clientUsername + "," + beauticianUsername + "," + treatmentTypeId + "," + price;
    }
}
