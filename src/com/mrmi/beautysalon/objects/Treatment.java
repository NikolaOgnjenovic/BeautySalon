package com.mrmi.beautysalon.objects;

import java.util.Date;

public class Treatment {
    private Date scheduledDate; // Zbog pojednostavljivanja, smatrati da tretmani počinju uvek na pun sat.
    private boolean cancelled;

    private String clientUsername;
    private int id;

    private String beauticianUsername;

    private TreatmentType type;

    public Treatment(Date scheduledDate, TreatmentType type, String clientUsername) {
        this.scheduledDate = scheduledDate;
        this.cancelled = false;
        this.type = type;
        this.clientUsername = clientUsername;
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

    public TreatmentType getType() {
        return type;
    }

    public void setType(TreatmentType type) {
        this.type = type;
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
}
