package com.mrmi.beautysalon.main.entity;

import com.mrmi.beautysalon.main.exceptions.TreatmentTypeCategoryNotFoundException;
import com.mrmi.beautysalon.main.exceptions.TreatmentTypeNotFoundException;
import com.mrmi.beautysalon.main.manager.TreatmentManager;

import java.util.Calendar;

public class Treatment implements TableCell {
    public int id;
    private Calendar scheduledDate;
    private boolean cancelled;

    private String clientUsername;

    private String beauticianUsername;

    private int typeId;

    private float price; // Although type holds price, this price can be affected by a loyalty discount
    private Status status;

    public enum Status {
        SCHEDULED,
        FINISHED,
        CANCELLED_BY_CLIENT,
        CANCELLED_BY_SALON,
        DID_NOT_ARRIVE
    }

    private String cancellationReason;

    // Used when adding
    public Treatment(Calendar scheduledDate, String clientUsername, String beauticianUsername, int typeId, float price) {
        this.scheduledDate = scheduledDate;
        this.cancelled = false;
        this.clientUsername = clientUsername;
        this.beauticianUsername = beauticianUsername;
        this.typeId = typeId;
        this.price = price;
        this.status = Status.SCHEDULED;
        this.cancellationReason = "N/A";
    }

    // Used when reading from files
    public Treatment(int id, Calendar scheduledDate, boolean cancelled, String clientUsername, String beauticianUsername, int typeId, float price, Status status, String cancellationReason) {
        this.id = id;
        this.scheduledDate = scheduledDate;
        this.cancelled = cancelled;
        this.clientUsername = clientUsername;
        this.beauticianUsername = beauticianUsername;
        this.typeId = typeId;
        this.price = price;
        this.status = status;
        this.cancellationReason = cancellationReason;
    }

    public Calendar getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Calendar scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
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
        return typeId;
    }

    public void setTreatmentTypeId(int typeId) {
        this.typeId = typeId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
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

    public String getFileString() {
        return id + "," + scheduledDate.getTime() + "," + cancelled + "," + clientUsername + "," + beauticianUsername + "," + typeId + "," + price + "," + status + "," + cancellationReason;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public Object getCell(int column, Object manager) {
        TreatmentManager treatmentManager = (TreatmentManager) manager;
        switch (column) {
            case 0:
                return id;
            case 1:
                try {
                    return treatmentManager.getTreatmentTypeName(typeId);
                } catch (TreatmentTypeNotFoundException e) {
                    return "Treatment type not found";
                }
            case 2:
                try {
                    return treatmentManager.getTreatmentTypeCategoryNameByType(typeId);
                } catch (TreatmentTypeCategoryNotFoundException e) {
                    return "Treatment type category not found";
                } catch (TreatmentTypeNotFoundException e) {
                    return "Treatment type not found";
                }
            case 3:
                return scheduledDate.getTime();
            case 4:
                return ((int) price * 100) / 100f;
            case 5:
                return clientUsername;
            case 6:
                return beauticianUsername;
            case 7:
                return status;
            case 8:
                return cancelled;
            case 9:
                return cancellationReason;
            case 10:
                return ((int) (0.9 * price) * 100) / 100f;
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Id";
            case 1:
                return "Type";
            case 2:
                return "Category";
            case 3:
                return "Date";
            case 4:
                return "Price";
            case 5:
                return "Client";
            case 6:
                return "Beautician";
            case 7:
                return "Status";
            case 8:
                return "Is cancelled";
            case 9:
                return "Cancellation reason";
            case 10:
                return "Refundable amount";
            default:
                return null;
        }
    }

    @Override
    public int getColumnCount() {
        return 11;
    }
}
