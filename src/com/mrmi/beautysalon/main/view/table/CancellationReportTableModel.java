package com.mrmi.beautysalon.main.view.table;


import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class CancellationReportTableModel extends AbstractTableModel {
    private final ArrayList<String> reasons;
    private final ArrayList<Integer> amounts;
    public CancellationReportTableModel(ArrayList<String> reasons, ArrayList<Integer> amounts) {
        this.reasons = reasons;
        this.amounts = amounts;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return "Finished status / cancellation reason";
        }
        return "Amount";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return String.class;
        }
        return Integer.class;
    }

    @Override
    public int getColumnCount() {
        return 2;
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