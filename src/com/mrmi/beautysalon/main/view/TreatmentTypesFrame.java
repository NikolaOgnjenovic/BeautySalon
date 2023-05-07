package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.entity.TreatmentType;
import com.mrmi.beautysalon.main.entity.TreatmentTypeCategory;
import com.mrmi.beautysalon.main.view.table.TreatmentTypeTableModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TreatmentTypesFrame extends JFrame {
    private final TableRowSorter<TableModel> tableSorter;
    private final JTextField filterText;
    public TreatmentTypesFrame(TreatmentManager treatmentManager, HashMap<Integer, TreatmentType> treatmentTypes, boolean canEdit, boolean canDelete) {
        this.setLayout(new MigLayout("wrap 1", "[center, grow]", "[center, grow]"));
        this.setTitle("Beauty salon - Treatment types");
        this.setSize(1000, 1080);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        

        TreatmentTypeTableModel tableModel = new TreatmentTypeTableModel(treatmentManager, treatmentTypes, canEdit);
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
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(22);
        this.add(new JScrollPane(table), "span, growx");

        table.setAutoCreateRowSorter(true);
        tableSorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(tableSorter);

        JLabel searchLabel = new JLabel("Search");
        this.add(searchLabel, "split 2");

        filterText = new JTextField("Search", 20);
        filterText.addActionListener(e -> filter(filterText.getText()));
        this.add(filterText);

        JLabel nameLabel = new JLabel("Name");
        this.add(nameLabel, "split 2");
        JTextField nameField = new JTextField(20);
        this.add(nameField);

        JLabel priceLabel = new JLabel("Price");
        this.add(priceLabel, "split 2");
        JTextField priceField = new JTextField(20);
        this.add(priceField);

        JLabel categoryLabel = new JLabel("Category");
        this.add(categoryLabel, "split 2");
        JComboBox<String> categoryComboBox = new JComboBox<>();
        JComboBox<String> editCategoryComboBox = new JComboBox<>();
        for (Map.Entry<Integer, TreatmentTypeCategory> category: treatmentManager.getAvailableTreatmentTypeCategories().entrySet()) {
            categoryComboBox.addItem(category.getValue().getName());
            editCategoryComboBox.addItem(category.getValue().getName());
        }
        this.add(categoryComboBox);

        if (canEdit) {
            TableColumn categoryColumn = table.getColumnModel().getColumn(0);
            categoryColumn.setCellEditor(new DefaultCellEditor(editCategoryComboBox));
        }

        JLabel durationLabel = new JLabel("Duration (minutes)");
        this.add(durationLabel, "split 2");
        JTextField durationField = new JTextField(4);
        this.add(durationField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            treatmentManager.addTreatmentType(new TreatmentType(nameField.getText(), Double.parseDouble(priceField.getText()), treatmentManager.getTreatmentTypeCategoryIdByName(categoryComboBox.getSelectedItem().toString()), Byte.parseByte(durationField.getText())));
            tableModel.fireTableDataChanged();
        });
        this.add(addButton);

        if (canDelete) {
            JButton delete = new JButton("Delete");
            delete.addActionListener(e -> {
                if (table.getSelectedRow() != -1) {
                    treatmentManager.deleteTreatmentType(new ArrayList<>(treatmentTypes.keySet()).get(table.getSelectedRow()));
                    tableModel.fireTableDataChanged();
                }
            });
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
