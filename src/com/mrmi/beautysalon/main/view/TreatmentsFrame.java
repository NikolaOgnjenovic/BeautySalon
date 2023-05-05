package com.mrmi.beautysalon.main.view;


import com.mrmi.beautysalon.main.entity.TreatmentType;
import com.mrmi.beautysalon.main.entity.TreatmentTypeCategory;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import com.mrmi.beautysalon.main.entity.Treatment;
import com.mrmi.beautysalon.main.view.table.TreatmentTableModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TreatmentsFrame extends JFrame {
    private final TableRowSorter<TableModel> tableSorter;
    private final JTextField filterText;
    private final TreatmentTableModel tableModel;

    public TreatmentsFrame(TreatmentManager treatmentManager, UserManager userManager, HashMap<Integer, Treatment> treatments, boolean canEdit, boolean canCancel, boolean canDelete, Double loyaltyThreshold, boolean isClient) {
        this.setLayout(new MigLayout("wrap 1", "[center, grow]", "[center, grow]"));
        this.setTitle("Beauty salon - Treatments");
        this.setSize(1000, 1080);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);

        tableModel = new TreatmentTableModel(treatmentManager, treatments, canEdit, isClient);
        JTable table = new JTable(tableModel) {
            final DefaultTableCellRenderer renderLeft = new DefaultTableCellRenderer();
            {
                renderLeft.setHorizontalAlignment(SwingConstants.LEFT);
            }

            @Override
            public TableCellRenderer getCellRenderer (int arg0, int arg1) {
                return renderLeft;
            }
        };
        table.getTableHeader().setReorderingAllowed(false);
        Utility.setFont(table, 20);
        table.setRowHeight(22);
        this.add(new JScrollPane(table), "span, growx");
        table.setAutoCreateRowSorter(true);
        tableSorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(tableSorter);

        JLabel searchLabel = new JLabel("Search");
        Utility.setFont(searchLabel, 24);
        this.add(searchLabel, "split 2");

        filterText = new JTextField("", 20);
        filterText.addActionListener(e -> filter(filterText.getText()));
        Utility.setFont(filterText, 24);
        this.add(filterText);

        Double totalCost = treatmentManager.getTotalCost(treatments);
        JLabel total = new JLabel("Total cost: " + totalCost);
        Utility.setFont(total, 24);
        this.add(total);

        JLabel totalRefund = new JLabel(totalCost + " will be refunded if the salon cancels the treatments");
        Utility.setFont(totalRefund, 24);
        this.add(totalRefund);

        JLabel clientRefund = new JLabel(0.9*totalCost + " will be refunded if the client cancels the treatments");
        Utility.setFont(clientRefund, 24);
        this.add(clientRefund);

        if (canCancel) {
            JLabel cancellationLabel = new JLabel("Why are you cancelling the treatment?");
            Utility.setFont(cancellationLabel, 24);
            this.add(cancellationLabel, "split 3");

            JTextField cancellationReason = new JTextField();
            cancellationReason.setToolTipText("Cancellation reason");
            cancellationReason.setPreferredSize(new Dimension(250, 40));
            Utility.setFont(cancellationReason, 24);
            this.add(cancellationReason);

            JButton cancel = new JButton("Cancel");
            cancel.addActionListener(e -> {
                if (cancellationReason.getText().length() < 1) {
                    // todo: warning popup
                    cancellationReason.setText("Enter a reason!");
                } else {
                    treatmentManager.cancelTreatment(table.getSelectedRow(), isClient, cancellationReason.getText(), userManager, loyaltyThreshold);
                    tableModel.fireTableDataChanged();
                }
            });
            Utility.setFont(cancel, 24);
            this.add(cancel);
        }

        if (canDelete) {
            JButton delete = new JButton("Delete");
            delete.addActionListener(e -> {
                treatmentManager.deleteTreatment(new ArrayList<>(treatments.keySet()).get(table.getSelectedRow()));
                tableModel.fireTableDataChanged();
            });
            Utility.setFont(delete, 24);
            this.add(delete);
        }

        if (canEdit) {
            TableColumn statusColumn = table.getColumnModel().getColumn(6);
            JComboBox<String> comboBox = new JComboBox<>();
            for (Treatment.Status status : Treatment.Status.values()) {
                comboBox.addItem(String.valueOf(status));
            }
            statusColumn.setCellEditor(new DefaultCellEditor(comboBox));

            JComboBox<String> typeComboBox = new JComboBox<>();
            for (Map.Entry<Integer, TreatmentType> type: treatmentManager.getAvailableTreatmentTypes().entrySet()) {
                typeComboBox.addItem(type.getValue().getName());
            }
            Utility.setFont(typeComboBox, 20);

            TableColumn typeColumn = table.getColumnModel().getColumn(2);
            typeColumn.setCellEditor(new DefaultCellEditor(typeComboBox));
        }

        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            this.dispose();
            //previousFrame.setVisible(true);
        });
        Utility.setFont(back, 24);
        this.add(back);
    }

    private void filter(String text) {
        RowFilter<TableModel, Object> rf;
        try {
            rf = RowFilter.regexFilter(text); // (text, 0,1,2) to search columns 0,1,2
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        tableSorter.setRowFilter(rf);
    }
}
