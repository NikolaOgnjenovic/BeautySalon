package com.mrmi.beautysalon.main.view.table;

import com.mrmi.beautysalon.main.entity.TreatmentTypeCategory;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.entity.Beautician;
import com.mrmi.beautysalon.main.entity.TreatmentType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;

public class BeauticianTableModel extends AbstractTableModel {
    private final HashMap<String, Beautician> beauticians;
    private final HashMap<Integer, TreatmentTypeCategory> treatmentTypeCategories;
    private final String[] columnNames = new String[]{
            "Username", "Name", "Surname", "Gender", "Qualification level",
            "Years of experience", "Known treatment types"
    };
    private final Class[] columnClass = new Class[]{
            String.class, String.class, String.class, String.class, Byte.class,
            Byte.class, String.class
    };

    public BeauticianTableModel(TreatmentManager treatmentManager, HashMap<String, Beautician> beauticians) {
        this.beauticians = beauticians;
        this.treatmentTypeCategories = treatmentManager.getTreatmentTypeCategories();
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
        return beauticians.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Beautician beautician = new ArrayList<>(beauticians.values()).get(rowIndex);
        switch (columnIndex) {
            case 0:
                return new ArrayList<>(beauticians.keySet()).get(rowIndex);
            case 1:
                return beautician.getName();
            case 2:
                return beautician.getSurname();
            case 3:
                return beautician.getGender();
            case 4:
                return beautician.getQualificationLevel();
            case 5:
                return beautician.getYearsOfExperience();
            case 6:
                Beautician b = new ArrayList<>(beauticians.values()).get(rowIndex);
                StringBuilder types = new StringBuilder();
                for (int id : b.getTreatmentTypeIDs()) {
                    TreatmentTypeCategory category = treatmentTypeCategories.get(id);
                    if (category != null) {
                        types.append(category.getName());
                        types.append(", ");
                    }
                }
                return types.toString();
            default:
                return null;
        }
    }
}