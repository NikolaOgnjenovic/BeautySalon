package com.mrmi.beautysalon.main.entity;

import java.util.Date;

public class Treatment {
    private Date scheduledDate; // Zbog pojednostavljivanja, smatrati da tretmani počinju uvek na pun sat.
    private boolean cancelled;

    private String clientUsername;

    private String beauticianUsername;

    private int treatmentTypeId;

    private Double price; // Although treatmentType holds price, this price can be affected by a loyalty discount
    private Status status;

    public enum Status {
        SCHEDULED,
        FINISHED,
        CANCELLED_BY_CLIENT,
        CANCELLED_BY_SALON,
        DID_NOT_ARRIVE
    }

    private String cancellationReason;

    public Treatment(Date scheduledDate, boolean cancelled, String clientUsername, String beauticianUsername, int treatmentTypeId, double price) {
        this.scheduledDate = scheduledDate;
        this.cancelled = cancelled;
        this.clientUsername = clientUsername;
        this.beauticianUsername = beauticianUsername;
        this.treatmentTypeId = treatmentTypeId;
        this.price = price;
        this.status = Status.SCHEDULED;
        this.cancellationReason = "-";
    }

    public Treatment(Date scheduledDate, boolean cancelled, String clientUsername, String beauticianUsername, int treatmentTypeId, double price, Status status, String cancellationReason) {
        this.scheduledDate = scheduledDate;
        this.cancelled = cancelled;
        this.clientUsername = clientUsername;
        this.beauticianUsername = beauticianUsername;
        this.treatmentTypeId = treatmentTypeId;
        this.price = price;
        this.status = status;
        this.cancellationReason = cancellationReason;
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

    public String getClientUsername() {
        return clientUsername;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public String getBeauticianUsername() {
        return beauticianUsername;
    }

    public void setBeauticianUsername(String beauticianUsername) {
        this.beauticianUsername = beauticianUsername;
    }

    public int getTreatmentTypeId() {
        return treatmentTypeId;
    }

    public void setTreatmentTypeId(int treatmentTypeId) {
        this.treatmentTypeId = treatmentTypeId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getFileString(int id) {
        return id + "," + scheduledDate + "," + cancelled + "," + clientUsername + "," + beauticianUsername + "," + treatmentTypeId + "," + price + "," + status + "," + cancellationReason;
    }
}
