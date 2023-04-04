package com.mrmi.beautysalon.main.gui;

import com.mrmi.beautysalon.main.exceptions.UserNotFoundException;
import com.mrmi.beautysalon.main.objects.*;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TreatmentTypeTableModel extends AbstractTableModel {
    private final HashMap<Integer, TreatmentType> treatmentTypes;
    private final Database database;
    private final boolean canEdit;

    /*
     "name='" + name + '\'' +
                ", price=" + price +
                ", timesBooked=" + timesBooked +
                ", profit=" + profit +
     */
    private final String[] columnNames = new String[] {
            "Name", "Price", "Times booked", "Profit"
    };
    private final Class[] columnClass = new Class[] {
            String.class, Double.class, Integer.class, Double.class
    };

    public TreatmentTypeTableModel(Database database, HashMap<Integer, TreatmentType> treatmentTypes, boolean canEdit)
    {
        this.database = database;
        this.treatmentTypes = treatmentTypes;
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
        return treatmentTypes.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        TreatmentType treatmentType = treatmentTypes.values().stream().toList().get(rowIndex);
        switch (columnIndex) {
            case 0 -> {
                return treatmentType.getName();
            }
            case 1 -> {
                return treatmentType.getPrice();
            }
            case 2 -> {
                return treatmentType.getTimesBooked();
            }
            case 3 -> {
                return treatmentType.getProfit();
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
        TreatmentType treatmentType = treatmentTypes.values().stream().toList().get(rowIndex);
        switch (columnIndex) {
            case 0 -> treatmentType.setName(aValue.toString());
            case 1 -> treatmentType.setPrice(Double.parseDouble(aValue.toString()));
            case 2 -> treatmentType.setTimesBooked(Integer.parseInt(aValue.toString()));
            case 3 -> treatmentType.setProfit(Double.parseDouble(aValue.toString()));
        }

        database.updateTreatmentType(treatmentTypes.keySet().stream().toList().get(rowIndex), treatmentType);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return canEdit;
    }
}