package com.mrmi.beautysalon.main.entity;

public interface TableCell {
    Object getCell(int column, Object manager);

    String getColumnName(int column);

    int getColumnCount();
}
