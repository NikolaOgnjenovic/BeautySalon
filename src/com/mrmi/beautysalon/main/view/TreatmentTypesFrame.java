package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.entity.TreatmentType;
import com.mrmi.beautysalon.main.view.table.TreatmentTypeTableModel;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.HashMap;

public class TreatmentTypesFrame extends JFrame {
    private final TableRowSorter<TableModel> tableSorter;
    private final JTextField filterText;
    public TreatmentTypesFrame(TreatmentController treatmentController, HashMap<Integer, TreatmentType> treatmentTypes, boolean canEdit, boolean canDelete) {
        TreatmentTypeTableModel tableModel = new TreatmentTypeTableModel(treatmentController, treatmentTypes, canEdit);
        JTable table = new JTable(tableModel);
        this.add(new JScrollPane(table));

        this.setTitle("Treatment types");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLayout(new FlowLayout());

        table.setAutoCreateRowSorter(true);
        tableSorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(tableSorter);

        filterText = new JTextField("Search", 20);
        filterText.addActionListener(e -> filter(filterText.getText()));
        this.add(filterText);

        // Svinjarija
        if (canDelete) {
            JButton delete = new JButton("Delete treatment type");
            delete.addActionListener(e -> {
                treatmentController.deleteTreatmentType(treatmentTypes.keySet().stream().toList().get(table.getSelectedRow()));
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
        this.add(new JLabel("Duration:"));
        JTextField durationField = new JTextField();
        this.add(durationField);
        JButton addButton = new JButton("Add new type");
        addButton.addActionListener(e -> {
            //TODO: parse category enum combo box
            treatmentController.addTreatmentType(new TreatmentType(nameField.getText(), Double.parseDouble(priceField.getText()), Integer.parseInt(categoryField.getText()), Byte.parseByte(durationField.getText())));
            tableModel.fireTableDataChanged();
        });
        this.add(addButton);
    }

    private void filter(String text) {
        RowFilter<TableModel, Object> rf;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(text, 0, 1, 2);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        tableSorter.setRowFilter(rf);
    }
}
