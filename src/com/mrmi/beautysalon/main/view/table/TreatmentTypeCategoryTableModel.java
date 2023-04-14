package com.mrmi.beautysalon.main.view.table;

import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.entity.Database;
import com.mrmi.beautysalon.main.entity.TreatmentTypeCategory;

import javax.swing.table.AbstractTableModel;
import java.util.HashMap;

public class TreatmentTypeCategoryTableModel extends AbstractTableModel {
    private final HashMap<Integer, TreatmentTypeCategory> treatmentTypeCategories;
    private final TreatmentController treatmentController;
    private final boolean canEdit;

    private final String[] columnNames = new String[] {
            "Name"
    };
    private final Class[] columnClass = new Class[] {
            String.class
    };

    public TreatmentTypeCategoryTableModel(TreatmentController treatmentController, HashMap<Integer, TreatmentTypeCategory> treatmentTypeCategories, boolean canEdit)
    {
        this.treatmentController = treatmentController;
        this.treatmentTypeCategories = treatmentTypeCategories;
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
        return treatmentTypeCategories.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        TreatmentTypeCategory treatmentTypeCategory = treatmentTypeCategories.values().stream().toList().get(rowIndex);
        switch (columnIndex) {
            case 0 -> {
                return treatmentTypeCategory.getName();
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
        TreatmentTypeCategory treatmentTypeCategory = treatmentTypeCategories.values().stream().toList().get(rowIndex);
        switch (columnIndex) {
            case 0 -> treatmentTypeCategory.setName(aValue.toString());
        }

        treatmentController.updateTreatmentTypeCategory(treatmentTypeCategories.keySet().stream().toList().get(rowIndex), treatmentTypeCategory);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return canEdit;
    }
}

