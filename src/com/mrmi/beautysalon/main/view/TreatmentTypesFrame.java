package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.entity.Treatment;
import com.mrmi.beautysalon.main.entity.TreatmentType;
import com.mrmi.beautysalon.main.entity.TreatmentTypeCategory;
import com.mrmi.beautysalon.main.view.table.TreatmentTypeTableModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TreatmentTypesFrame extends JFrame {
    private final TableRowSorter<TableModel> tableSorter;
    private final JTextField filterText;
    public TreatmentTypesFrame(TreatmentController treatmentController, HashMap<Integer, TreatmentType> treatmentTypes, boolean canEdit, boolean canDelete) {
        this.setLayout(new MigLayout("wrap 1", "[center, grow]", "[center, grow]"));
        this.setTitle("Beauty salon - Treatment types");
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);

        TreatmentTypeTableModel tableModel = new TreatmentTypeTableModel(treatmentController, treatmentTypes, canEdit);
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

        table.setAutoCreateRowSorter(true);
        tableSorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(tableSorter);

        JLabel searchLabel = new JLabel("Search");
        Utility.setFont(searchLabel, 24);
        this.add(searchLabel, "split 2");

        filterText = new JTextField("Search", 20);
        filterText.addActionListener(e -> filter(filterText.getText()));
        Utility.setFont(filterText, 24);
        this.add(filterText);

        JLabel nameLabel = new JLabel("Name");
        Utility.setFont(nameLabel, 24);
        this.add(nameLabel, "split 2");
        JTextField nameField = new JTextField(20);
        Utility.setFont(nameField, 24);
        this.add(nameField);

        JLabel priceLabel = new JLabel("Price");
        Utility.setFont(priceLabel, 24);
        this.add(priceLabel, "split 2");
        JTextField priceField = new JTextField(20);
        Utility.setFont(priceField, 24);
        this.add(priceField);

        JLabel categoryLabel = new JLabel("Category");
        Utility.setFont(categoryLabel, 24);
        this.add(categoryLabel, "split 2");
        JComboBox<String> categoryComboBox = new JComboBox<>();
        JComboBox<String> editCategoryComboBox = new JComboBox<>();
        for (Map.Entry<Integer, TreatmentTypeCategory> category: treatmentController.getTreatmentTypeCategories().entrySet()) {
            categoryComboBox.addItem(category.getValue().getName());
            editCategoryComboBox.addItem(category.getValue().getName());
        }
        Utility.setFont(categoryComboBox, 20);
        this.add(categoryComboBox);

        if (canEdit) {
            TableColumn categoryColumn = table.getColumnModel().getColumn(0);
            categoryColumn.setCellEditor(new DefaultCellEditor(editCategoryComboBox));
        }

        JLabel durationLabel = new JLabel("Duration (hours)");
        Utility.setFont(durationLabel, 24);
        this.add(durationLabel, "split 2");
        JTextField durationField = new JTextField(4);
        Utility.setFont(durationField, 24);
        this.add(durationField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            treatmentController.addTreatmentType(new TreatmentType(nameField.getText(), Double.parseDouble(priceField.getText()), treatmentController.getTreatmentTypeCategoryIdByName(categoryComboBox.getSelectedItem().toString()), Byte.parseByte(durationField.getText())));
            tableModel.fireTableDataChanged();
        });
        Utility.setFont(addButton, 24);
        this.add(addButton);

        if (canDelete) {
            JButton delete = new JButton("Delete");
            delete.addActionListener(e -> {
                treatmentController.deleteTreatmentType(new ArrayList<>(treatmentTypes.keySet()).get(table.getSelectedRow()));
                tableModel.fireTableDataChanged();
            });
            Utility.setFont(delete, 24);
            this.add(delete);
        }
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
