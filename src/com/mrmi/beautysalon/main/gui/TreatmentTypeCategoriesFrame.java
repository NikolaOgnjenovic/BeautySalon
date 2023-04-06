package com.mrmi.beautysalon.main.gui;

import com.mrmi.beautysalon.main.objects.Database;
import com.mrmi.beautysalon.main.objects.TreatmentType;
import com.mrmi.beautysalon.main.objects.TreatmentTypeCategory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TreatmentTypeCategoriesFrame extends JFrame {
    public TreatmentTypeCategoriesFrame(Database database, HashMap<Integer, TreatmentTypeCategory> treatmentTypeCategories, boolean canEdit, boolean canDelete) {
        TreatmentTypeCategoryTableModel tableModel = new TreatmentTypeCategoryTableModel(database, treatmentTypeCategories, canEdit);
        JTable table = new JTable(tableModel);
        this.add(new JScrollPane(table));

        this.setTitle("Treatment type categories");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLayout(new FlowLayout());

        // Svinjarija
        if (canDelete) {
            JButton delete = new JButton("Delete treatment type category");
            delete.addActionListener(e -> {
                database.deleteTreatmentTypeCategory(treatmentTypeCategories.keySet().stream().toList().get(table.getSelectedRow()));
                tableModel.fireTableDataChanged();
            });
            this.add(delete);
        }

        this.add(new JLabel("Name"));
        JTextField nameField = new JTextField();
        this.add(nameField);

        JButton addButton = new JButton("Add new type");
        addButton.addActionListener(e -> {
            database.addTreatmentTypeCategory(database.getNextTreatmentTypeCategoryId(), new TreatmentTypeCategory(nameField.getText(), new ArrayList<>(), 0d));
            tableModel.fireTableDataChanged();
        });
        this.add(addButton);
    }
}
