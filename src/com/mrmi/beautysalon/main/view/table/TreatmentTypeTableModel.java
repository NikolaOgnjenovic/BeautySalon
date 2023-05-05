package com.mrmi.beautysalon.main.view.table;

import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.entity.TreatmentType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;

public class TreatmentTypeTableModel extends AbstractTableModel {
    private final HashMap<Integer, TreatmentType> treatmentTypes;
    private final TreatmentManager treatmentManager;
    private final boolean canEdit;

    private final String[] columnNames = new String[]{
            "Category name", "Name", "Price", "Times booked", "Profit", "Duration", "Is deleted"
    };
    private final Class[] columnClass = new Class[]{
            String.class, String.class, Double.class, Integer.class, Double.class, Byte.class, Boolean.class
    };

    public TreatmentTypeTableModel(TreatmentManager treatmentManager, HashMap<Integer, TreatmentType> treatmentTypes, boolean canEdit) {
        this.treatmentManager = treatmentManager;
        this.treatmentTypes = treatmentTypes;
        this.canEdit = canEdit;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClass[columnIndex];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return treatmentTypes.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TreatmentType treatmentType = new ArrayList<>(treatmentTypes.values()).get(rowIndex);
        switch (columnIndex) {
            case 0:
                return treatmentManager.getTreatmentTypeCategoryName(treatmentType.getCategoryId());
            case 1:
                return treatmentType.getName();
            case 2:
                return treatmentType.getPrice();
            case 3:
                return treatmentType.getTimesBooked();
            case 4:
                return treatmentType.getProfit();
            case 5:
                return treatmentType.getDuration();
            case 6:
                return treatmentType.isDeleted();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (!canEdit) {
            return;
        }
        TreatmentType treatmentType = new ArrayList<>(treatmentTypes.values()).get(rowIndex);
        switch (columnIndex) {
            case 0:
                treatmentType.setCategoryId(treatmentManager.getTreatmentTypeCategoryIdByName(aValue.toString()));
            case 1:
                treatmentType.setName(aValue.toString());
                break;
            case 2:
                treatmentType.setPrice(Double.parseDouble(aValue.toString()));
                break;
            case 3:
                treatmentType.setTimesBooked(Integer.parseInt(aValue.toString()));
                break;
            case 4:
                treatmentType.setProfit(Double.parseDouble(aValue.toString()));
                break;
            case 5:
                treatmentType.setDuration(Byte.parseByte(aValue.toString()));
                break;
        }
        treatmentManager.updateTreatmentType(new ArrayList<>(treatmentTypes.keySet()).get(rowIndex), treatmentType);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit;
    }
}
