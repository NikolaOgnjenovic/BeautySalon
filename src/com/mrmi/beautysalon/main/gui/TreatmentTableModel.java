package com.mrmi.beautysalon.main.gui;

import com.mrmi.beautysalon.main.exceptions.TreatmentTypeNotFoundException;
import com.mrmi.beautysalon.main.objects.Database;
import com.mrmi.beautysalon.main.objects.Treatment;
import com.mrmi.beautysalon.main.objects.TreatmentType;

import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.HashMap;

public class TreatmentTableModel extends AbstractTableModel {
    private final HashMap<Integer, Treatment> treatments;
    private final Database database;
    private final boolean canEdit;

    private final String[] columnNames = new String[] {
            "Date", "Category", "Type", "Price", "Client", "Beautician", "Status", "Cancelled", "Cancellation reason"
    };
    private final Class[] columnClass = new Class[] {
            Date.class, String.class, String.class, Double.class, String.class, String.class, String.class, Boolean.class, String.class
    };

    public TreatmentTableModel(Database database, HashMap<Integer, Treatment> treatments, boolean canEdit)
    {
        this.database = database;
        this.treatments = treatments;
        this.canEdit = canEdit;
    }

    @Override
    public String getColumnName(int column)
    {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return columnClass[columnIndex];
    }

    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }

    @Override
    public int getRowCount()
    {
        return treatments.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Treatment treatment = treatments.values().stream().toList().get(rowIndex);
        switch (columnIndex) {
            case 0 -> {
                return treatment.getScheduledDate();
            }
            case 1 -> {
                try {
                    return database.getTreatmentTypeById(treatment.getTreatmentTypeId()).getCategoryName();
                } catch (TreatmentTypeNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            case 2 -> {
                try {
                    return database.getTreatmentTypeById(treatment.getTreatmentTypeId()).getName();
                } catch (TreatmentTypeNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            case 3 -> {
                return treatment.getPrice();
            }
            case 4 -> {
                return treatment.getClientUsername();
            }
            case 5 -> {
                return treatment.getBeauticianUsername();
            }
            case 6 -> {
                return treatment.getStatus();
            }

            case 7 -> {
                return treatment.isCancelled();
            }

            case 8-> {
                return treatment.getCancellationReason();
            }

            default -> {
                return null;
            }
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        if (!canEdit) {
            return;
        }
        Treatment treatment = treatments.values().stream().toList().get(rowIndex);
        switch (columnIndex) {
            case 0 -> treatment.setScheduledDate((Date) aValue);
            case 3 -> treatment.setPrice((Double) aValue);
            case 4 -> treatment.setClientUsername((String) aValue);
            case 5 -> treatment.setBeauticianUsername((String) aValue);
            case 6 -> treatment.setStatus((String) aValue);
            case 7 -> treatment.setCancelled((Boolean) aValue);
            case 8 -> treatment.setCancellationReason((String) aValue);
        }

        database.updateTreatment(treatment, treatments.keySet().stream().toList().get(rowIndex));
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return canEdit;
    }
}
