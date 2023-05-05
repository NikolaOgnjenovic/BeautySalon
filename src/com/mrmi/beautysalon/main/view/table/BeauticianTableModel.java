package com.mrmi.beautysalon.main.view.table;

import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.entity.Beautician;
import com.mrmi.beautysalon.main.entity.TreatmentType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;

public class BeauticianTableModel extends AbstractTableModel {
    private final HashMap<String, Beautician> beauticians;
    private final HashMap<Integer, TreatmentType> treatmentTypes;
    private final String[] columnNames = new String[]{
            "Username", "Name", "Surname", "Gender", "Qualification level",
            "Years of experience", "Known treatment types"
    };
    private final Class[] columnClass = new Class[]{
            String.class, String.class, String.class, String.class, Byte.class,
            Byte.class, String.class
    };

    public BeauticianTableModel(TreatmentController treatmentController, HashMap<String, Beautician> beauticians) {
        this.beauticians = beauticians;
        this.treatmentTypes = treatmentController.getTreatmentTypes();
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
        /*
        "Username", "Name", "Surname", "Gender", "Qualification level",
            "Years of experience", "Known treatment types"
         */
        Beautician beautician = new ArrayList<>(beauticians.values()).get(rowIndex);
        //Beautician beautician = beauticians.values().stream().toList().get(rowIndex);
        switch (columnIndex) {
            case 0:
                return new ArrayList<>(beauticians.keySet()).get(rowIndex);
            //return beauticians.keySet().stream().toList().get(rowIndex);
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
                    types.append(treatmentTypes.get(id).getName());
                    types.append(", ");
                }
                return types.toString();
            default:
                return null;
        }
    }
}