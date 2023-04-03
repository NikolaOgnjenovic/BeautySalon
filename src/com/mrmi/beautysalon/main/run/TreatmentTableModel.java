package com.mrmi.beautysalon.main.run;

import com.mrmi.beautysalon.main.objects.Client;
import com.mrmi.beautysalon.main.objects.Database;
import com.mrmi.beautysalon.main.objects.Employee;
import com.mrmi.beautysalon.main.objects.Treatment;

import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TreatmentTableModel extends AbstractTableModel {
    private final List<Treatment> treatments;
    private final Database database;
    private final boolean canEdit;

    private final String[] columnNames = new String[] {
            "Date", "Type", "Price", "Client", "Beautician", "Status", "Cancelled", "Cancellation reason"
    };
    private final Class[] columnClass = new Class[] {
            Date.class, Integer.class, Double.class, String.class, String.class, String.class, Boolean.class, String.class
    };

    public TreatmentTableModel(Database database, List<Treatment> treatments, boolean canEdit)
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
        Treatment treatment = treatments.get(rowIndex);
        switch (columnIndex) {
            case 0 -> {
                return treatment.getScheduledDate();
            }
            case 1 -> {
                return treatment.getTreatmentTypeId();
            }
            case 2 -> {
                return treatment.getPrice();
            }
            case 3 -> {
                return treatment.getClientUsername();
            }
            case 4 -> {
                return treatment.getBeauticianUsername();
            }
            case 5 -> {
                return treatment.getStatus();
            }

            case 6 -> {
                return treatment.isCancelled();
            }

            case 7 -> {
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
        Treatment treatment = treatments.get(rowIndex);
        switch (columnIndex) {
            case 0 -> {
                treatment.setScheduledDate((Date) aValue);
            }
            case 1 -> {
                treatment.setTreatmentTypeId((Integer) aValue);
            }
            case 2 -> {
                treatment.setPrice((Double) aValue);
            }
            case 3 -> {
                treatment.setClientUsername((String) aValue);
            }
            case 4 -> {
                treatment.setBeauticianUsername((String) aValue);
            }
            case 5 -> {
                treatment.setStatus((String) aValue);
            }

            case 6 -> {
                treatment.setCancelled((Boolean) aValue);
            }
            case 7 -> {
                treatment.setCancellationReason((String) aValue);
            }
        }

        database.updateTreatment(treatment);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return canEdit;
    }
}
