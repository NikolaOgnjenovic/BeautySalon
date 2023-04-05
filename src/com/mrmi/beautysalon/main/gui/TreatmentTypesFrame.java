package com.mrmi.beautysalon.main.gui;

import com.mrmi.beautysalon.main.objects.Database;
import com.mrmi.beautysalon.main.objects.TreatmentType;
import com.mrmi.beautysalon.main.objects.TreatmentTypeCategory;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.HashMap;

public class TreatmentTypesFrame extends JFrame {
    public TreatmentTypesFrame(Database database, HashMap<Integer, TreatmentType> treatmentTypes, boolean canEdit, boolean canDelete) {
        TreatmentTypeTableModel tableModel = new TreatmentTypeTableModel(database, treatmentTypes, canEdit);
        JTable table = new JTable(tableModel);
        this.add(new JScrollPane(table));

        this.setTitle("Treatment types");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLayout(new FlowLayout());

        // Svinjarija
        if (canDelete) {
            JButton delete = new JButton("Delete treatment type");
            delete.addActionListener(e -> {
                database.deleteTreatmentType(treatmentTypes.keySet().stream().toList().get(table.getSelectedRow()));
                tableModel.fireTableDataChanged();
            });
            this.add(delete);
        }

        this.add(new JLabel("Name"));
        JTextField nameField = new JTextField();
        this.add(nameField);
        this.add(new JLabel("Price"));
        JTextField priceField = new JTextField();
        this.add(priceField);
        this.add(new JLabel("Treatment type category name"));
        JTextField categoryField = new JTextField();
        this.add(categoryField);

        JButton addButton = new JButton("Add new type");
        addButton.addActionListener(e -> {
            database.addTreatmentType(new TreatmentType(nameField.getText(), Double.parseDouble(priceField.getText()), categoryField.getText()), database.getNextTreatmentTypeId());
            tableModel.fireTableDataChanged();
        });
        this.add(addButton);
    }
}
