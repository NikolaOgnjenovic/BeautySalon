package com.mrmi.beautysalon.main.gui;


import com.mrmi.beautysalon.main.objects.Database;
import com.mrmi.beautysalon.main.objects.Treatment;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class TreatmentsFrame extends JFrame {
    public TreatmentsFrame(Database database, HashMap<Integer, Treatment> treatments, boolean canEdit, boolean canCancel, boolean canDelete, JFrame previousFrame) {
        TreatmentTableModel tableModel = new TreatmentTableModel(database, treatments, canEdit);
        JTable table = new JTable(tableModel);
        this.add(new JScrollPane(table));
        this.setTitle("Treatments");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLayout(new FlowLayout());

//        JTextField treatmentNameField = new JTextField("Enter treatment name");
//        this.add(treatmentNameField);
//        JTextField treatmentLengthField = new JTextField("Enter treatment length");
//        this.add(treatmentLengthField);
//        JTextField treatmentPriceField = new JTextField("Enter treatment price");
//        this.add(treatmentPriceField);

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
                    Treatment t = treatments.values().stream().toList().get(table.getSelectedRow());
                    t.setStatus("CANCELLED");
                    t.setCancelled(true);
                    t.setCancellationReason(cancellationReason.getText());
                    database.updateTreatment(t, treatments.keySet().stream().toList().get(table.getSelectedRow()));
                    tableModel.fireTableDataChanged();
                }
            });
            this.add(cancel);
        }

        // Svinjarija
        if (canDelete) {
            JButton delete = new JButton("Delete treatment");
            delete.addActionListener(e -> {
                database.deleteTreatment(treatments.keySet().stream().toList().get(table.getSelectedRow()));
                tableModel.fireTableDataChanged();
            });
            this.add(delete);
        }

        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            this.dispose();
            previousFrame.setVisible(true);
        });

        Double totalCost = database.getTotalCost(treatments);
        JLabel total = new JLabel("Total cost: " + totalCost);
        this.add(total);
        JLabel totalRefund = new JLabel(totalCost + " will be refunded if the salon cancels the treatments");
        this.add(totalRefund);
        JLabel clientRefund = new JLabel(0.9*totalCost + " will be refunded if the client cancels the treatments");
        this.add(clientRefund);
    }
}
