package com.mrmi.beautysalon.main.view.table;

import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.exceptions.TreatmentTypeNotFoundException;
import com.mrmi.beautysalon.main.entity.Database;
import com.mrmi.beautysalon.main.entity.Treatment;

import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.HashMap;

public class TreatmentTableModel extends AbstractTableModel {
    private final HashMap<Integer, Treatment> treatments;
    private final TreatmentController treatmentController;
    private final boolean canEdit;

    private final String[] columnNames = new String[] {
            "Date", "Category", "Type", "Price", "Client", "Beautician", "Status", "Cancelled", "Cancellation reason"
    };
    private final Class[] columnClass = new Class[] {
            Date.class, String.class, String.class, Double.class, String.class, String.class, String.class, Boolean.class, String.class
    };

    public TreatmentTableModel(TreatmentController treatmentController, HashMap<Integer, Treatment> treatments, boolean canEdit)
    {
        this.treatmentController = treatmentController;
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
                    return treatmentController.getTreatmentTypeCategoryName(treatmentController.getTreatmentTypeById(treatment.getTreatmentTypeId()).getCategoryId());
                } catch (TreatmentTypeNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            case 2 -> {
                try {
                    return treatmentController.getTreatmentTypeById(treatment.getTreatmentTypeId()).getName();
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

        treatmentController.updateTreatment(treatment, treatments.keySet().stream().toList().get(rowIndex));
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return canEdit;
    }
}
