package com.mrmi.beautysalon.main.view.table;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class BeauticianProfitTableModel extends AbstractTableModel {
    private final ArrayList<String> beauticianUsernames;
    private final ArrayList<Integer> finishedTreatments;
    private final ArrayList<Double> profits;
    private final String[] columnNames = new String[]{
            "Username", "Finished treatments", "Profit"
    };
    private final Class[] columnClass = new Class[]{
            String.class, Integer.class, Double.class
    };

    public BeauticianProfitTableModel(ArrayList<String> beauticianUsernames, ArrayList<Integer> finishedTreatments, ArrayList<Double> profits) {
        this.beauticianUsernames = beauticianUsernames;
        this.finishedTreatments = finishedTreatments;
        this.profits = profits;
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
        return beauticianUsernames.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return beauticianUsernames.get(rowIndex);
            case 1:
                return finishedTreatments.get(rowIndex);
            case 2:
                return profits.get(rowIndex);
            default:
                return null;
        }
    }
}