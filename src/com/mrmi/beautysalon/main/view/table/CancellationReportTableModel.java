package com.mrmi.beautysalon.main.view.table;


import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class CancellationReportTableModel extends AbstractTableModel {
    private final ArrayList<String> reasons;
    private final ArrayList<Integer> amounts;
    private final String[] columnNames = new String[]{
            "Finished status / cancellation reason", "Amount"
    };
    private final Class[] columnClass = new Class[]{
            String.class, Integer.class
    };

    public CancellationReportTableModel(ArrayList<String> reasons, ArrayList<Integer> amounts) {
        this.reasons = reasons;
        this.amounts = amounts;
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
        return amounts.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return reasons.get(rowIndex);
            case 1:
                return amounts.get(rowIndex);
            default:
                return null;
        }
    }
}