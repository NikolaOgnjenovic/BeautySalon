package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.entity.TreatmentTypeCategory;
import com.mrmi.beautysalon.main.view.table.TreatmentTypeCategoryTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TreatmentTypeCategoriesFrame extends JFrame {
    public TreatmentTypeCategoriesFrame(TreatmentController treatmentController, HashMap<Integer, TreatmentTypeCategory> treatmentTypeCategories, boolean canEdit, boolean canDelete) {
        TreatmentTypeCategoryTableModel tableModel = new TreatmentTypeCategoryTableModel(treatmentController, treatmentTypeCategories, canEdit);
        JTable table = new JTable(tableModel);
        this.add(new JScrollPane(table));

        this.setTitle("Treatment type categories");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLayout(new FlowLayout());

        // Svinjarija
        if (canDelete) {
            JButton delete = new JButton("Delete treatment type category");
            delete.addActionListener(e -> {
                treatmentController.deleteTreatmentTypeCategory(new ArrayList<>(treatmentTypeCategories.keySet()).get(table.getSelectedRow()));
                //treatmentController.deleteTreatmentTypeCategory(treatmentTypeCategories.keySet().stream().toList().get(table.getSelectedRow()));
                tableModel.fireTableDataChanged();
            });
            this.add(delete);
        }

        this.add(new JLabel("Name"));
        JTextField nameField = new JTextField();
        this.add(nameField);

        JButton addButton = new JButton("Add new type");
        addButton.addActionListener(e -> {
            treatmentController.addTreatmentTypeCategory(new TreatmentTypeCategory(nameField.getText(), new ArrayList<>(), 0d));
            tableModel.fireTableDataChanged();
        });
        this.add(addButton);
    }
}
