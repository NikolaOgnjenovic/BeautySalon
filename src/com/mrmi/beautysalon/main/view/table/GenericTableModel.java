package com.mrmi.beautysalon.main.view.table;

import com.mrmi.beautysalon.main.entity.TableCell;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;

public class GenericTableModel extends AbstractTableModel {
    private final ArrayList<Object> values;
    private final Object manager;
    public GenericTableModel(HashMap<?, ?> objects, Object manager) {
        this.values = new ArrayList<>(objects.values());
        this.manager = manager;
    }

    @Override
    public int getRowCount() {
        return values.size();
    }

    @Override
    public int getColumnCount() {
        if (values.size() == 0) {
            return 0;
        }
        return ((TableCell) values.get(0)).getColumnCount();
    }

    @Override
    public String getColumnName(int column)
    {
        if (values.size() == 0) {
            return "";
        }
        return ((TableCell) values.get(0)).getColumnName(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        if (values.size() == 0) {
            return null;
        }


        if (values.get(0) instanceof TableCell) {
            Object object = ((TableCell) values.get(0)).getCell(columnIndex, manager);
            if (object == null) {
                return Object.class;
            }
            return object.getClass();
        }

        return Object.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object object = values.get(rowIndex);
        if (object instanceof TableCell) {
            return ((TableCell) object).getCell(columnIndex, manager);
        }

        return null;
    }
}
