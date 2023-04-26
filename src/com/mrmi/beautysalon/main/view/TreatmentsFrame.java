package com.mrmi.beautysalon.main.view;


import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.controller.UserController;
import com.mrmi.beautysalon.main.entity.Treatment;
import com.mrmi.beautysalon.main.view.table.TreatmentTableModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TreatmentsFrame extends JFrame {
    private final TableRowSorter<TableModel> tableSorter;
    private final JTextField filterText;
    public TreatmentsFrame(TreatmentController treatmentController, UserController userController, HashMap<Integer, Treatment> treatments, boolean canEdit, boolean canCancel, boolean canDelete, JFrame previousFrame, Double loyaltyThreshold, boolean isClient) {
        TreatmentTableModel tableModel = new TreatmentTableModel(treatmentController, treatments, canEdit, isClient);
        JTable table = new JTable(tableModel);
        this.add(new JScrollPane(table));
        this.setTitle("Treatments");
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

        if (canCancel) {
            JTextField cancellationReason = new JTextField();
            cancellationReason.setToolTipText("Cancellation reason");
            cancellationReason.setPreferredSize(new Dimension(250, 40));
            this.add(cancellationReason);

            JButton cancel = new JButton("Cancel treatment");
            cancel.addActionListener(e -> {
                if (cancellationReason.getText().length() < 1) {
                    // todo: warning popup
                    cancellationReason.setText("Enter a reason!");
                } else {
                    treatmentController.cancelTreatment(table.getSelectedRow(), isClient, cancellationReason.getText(), userController, loyaltyThreshold);
                    tableModel.fireTableDataChanged();
                }
            });
            this.add(cancel);
        }

        if (canDelete) {
            JButton delete = new JButton("Delete treatment");
            delete.addActionListener(e -> {
                treatmentController.deleteTreatment(new ArrayList<>(treatments.keySet()).get(table.getSelectedRow()));
                tableModel.fireTableDataChanged();
            });
            this.add(delete);
        }

        if (canEdit) {
            TableColumn statusColumn = table.getColumnModel().getColumn(6);
            JComboBox<String> comboBox = new JComboBox<>();
            for (Treatment.Status status : Treatment.Status.values()) {
                comboBox.addItem(String.valueOf(status));
            }
            statusColumn.setCellEditor(new DefaultCellEditor(comboBox));
        }

        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            this.dispose();
            previousFrame.setVisible(true);
        });
        this.add(back);

        Double totalCost = treatmentController.getTotalCost(treatments);
        JLabel total = new JLabel("Total cost: " + totalCost);
        this.add(total);
        JLabel totalRefund = new JLabel(totalCost + " will be refunded if the salon cancels the treatments");
        this.add(totalRefund);
        JLabel clientRefund = new JLabel(0.9*totalCost + " will be refunded if the client cancels the treatments");
        this.add(clientRefund);
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
