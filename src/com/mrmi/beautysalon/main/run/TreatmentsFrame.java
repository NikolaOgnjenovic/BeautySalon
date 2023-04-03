package com.mrmi.beautysalon.main.run;


import com.mrmi.beautysalon.main.objects.Database;
import com.mrmi.beautysalon.main.objects.Employee;
import com.mrmi.beautysalon.main.objects.Treatment;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class TreatmentsFrame extends JFrame {
    public TreatmentsFrame(Database database, String title, HashMap<Integer, Treatment> treatments, boolean canEdit, boolean canCancel) {
        TreatmentTableModel tableModel = new TreatmentTableModel(database, treatments.values().stream().toList(), canEdit);
        JTable table = new JTable(tableModel);
        this.add(new JScrollPane(table));

        this.setTitle("Treatments");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLayout(new FlowLayout());

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
                    database.updateTreatment(t);
                }
            });
            this.add(cancel);
        }
    }
}
