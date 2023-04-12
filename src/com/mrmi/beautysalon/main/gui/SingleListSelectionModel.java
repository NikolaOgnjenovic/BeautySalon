package com.mrmi.beautysalon.main.gui;

import javax.swing.*;

public class SingleListSelectionModel extends DefaultListSelectionModel {

    public SingleListSelectionModel () {
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public void clearSelection() {
    }

    @Override
    public void removeSelectionInterval(int index0, int index1) {
    }
}