package com.mrmi.beautysalon.main.view.table;

import javax.swing.*;
import javax.swing.table.*;

public class GenericTable extends JTable {
    private final TableRowSorter<TableModel> tableSorter;
    private final DefaultTableCellRenderer renderLeft = new DefaultTableCellRenderer();
    {
        renderLeft.setHorizontalAlignment(SwingConstants.LEFT);
    }

    public GenericTable(GenericTableModel tableModel) {
        super(tableModel);

        this.getTableHeader().setReorderingAllowed(false);
        this.setRowHeight(24);
        this.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionMode(int selectionMode) {
                super.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            }
        });
        this.setAutoCreateRowSorter(true);
        tableSorter = new TableRowSorter<>(tableModel); // Enable row filtering using the TableRowSorter
        this.setRowSorter(tableSorter);

        // Hide the id column which is necessary for proper editing & deleting
        this.getColumn("Id").setMinWidth(0);
        this.getColumn("Id").setMaxWidth(0);
        this.getColumn("Id").setWidth(0);
    }

    @Override
    public TableCellRenderer getCellRenderer (int arg0, int arg1) {
        return renderLeft;
    }

    public void filter(String text) {
        RowFilter<TableModel, Object> rf;
        try {
            rf = RowFilter.regexFilter(text); // (text, 0,1,2) to search columns 0,1,2
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        tableSorter.setRowFilter(rf);
    }
}
