package com.mrmi.beautysalon.main.gui;

import com.mrmi.beautysalon.main.exceptions.UserNotFoundException;
import com.mrmi.beautysalon.main.objects.*;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BeauticianTableModel extends AbstractTableModel {
    private final HashMap<String, Beautician> beauticians;

    private final String[] columnNames = new String[] {
            "Username", "Name", "Surname", "Gender", "Qualification level",
            "Years of experience", "Known treatment types"
    };
    private final Class[] columnClass = new Class[] {
            String.class, String.class, String.class, String.class, Byte.class,
            Byte.class, String.class
    };

    public BeauticianTableModel(HashMap<String, Beautician> beauticians)
    {
        this.beauticians = beauticians;
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
        return beauticians.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        /*
        "Username", "Name", "Surname", "Gender", "Qualification level",
            "Years of experience", "Known treatment types"
         */
        Beautician beautician = beauticians.values().stream().toList().get(rowIndex);
        switch (columnIndex) {
            case 0 -> {
                return beauticians.keySet().stream().toList().get(rowIndex);
            }
            case 1 -> {
                return beautician.getName();
            }
            case 2 -> {
                return beautician.getSurname();
            }
            case 3 -> {
                return beautician.getGender();
            }
            case 4 -> {
                return beautician.getQualificationLevel();
            }
            case 5 -> {
                return beautician.getYearsOfExperience();
            }
            case 6 -> {
                return beautician.getTreatmentTypeIDs().toString();
            }
            default -> {
                return null;
            }
        }
    }
}
