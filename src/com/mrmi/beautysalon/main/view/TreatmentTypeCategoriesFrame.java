package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.entity.TreatmentTypeCategory;
import com.mrmi.beautysalon.main.view.table.TreatmentTypeCategoryTableModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.util.ArrayList;
import java.util.HashMap;

public class TreatmentTypeCategoriesFrame extends JFrame {
    public TreatmentTypeCategoriesFrame(TreatmentController treatmentController, HashMap<Integer, TreatmentTypeCategory> treatmentTypeCategories, boolean canEdit, boolean canDelete) {
        this.setLayout(new MigLayout("wrap 1", "[center, grow]", "[center, grow]"));
        this.setTitle("Beauty salon - Treatment type categories");
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);

        TreatmentTypeCategoryTableModel tableModel = new TreatmentTypeCategoryTableModel(treatmentController, treatmentTypeCategories, canEdit);
        JTable table = new JTable(tableModel){
            final DefaultTableCellRenderer renderLeft = new DefaultTableCellRenderer();

            { // initializer block
                renderLeft.setHorizontalAlignment(SwingConstants.LEFT);
            }

            @Override
            public TableCellRenderer getCellRenderer (int arg0, int arg1) {
                return renderLeft;
            }
        };
        Utility.setFont(table, 20);
        table.setRowHeight(22);
        this.add(new JScrollPane(table), "span, growx");

        JLabel nameLabel = new JLabel("Category name");
        Utility.setFont(nameLabel, 24);
        this.add(nameLabel, "split 3");
        JTextField nameField = new JTextField(20);
        Utility.setFont(nameField, 24);
        this.add(nameField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            treatmentController.addTreatmentTypeCategory(new TreatmentTypeCategory(nameField.getText(), new ArrayList<>(), 0d));
            tableModel.fireTableDataChanged();
        });
        Utility.setFont(addButton, 24);
        this.add(addButton);

        if (canDelete) {
            JButton deleteButton = new JButton("Delete");
            deleteButton.addActionListener(e -> {
                treatmentController.deleteTreatmentTypeCategory(new ArrayList<>(treatmentTypeCategories.keySet()).get(table.getSelectedRow()));
                tableModel.fireTableDataChanged();
            });
            Utility.setFont(deleteButton, 24);
            this.add(deleteButton);
        }

        JButton back = new JButton("Back");
        back.addActionListener(e -> this.dispose());
        Utility.setFont(back, 24);
        this.add(back);
    }
}
