package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.entity.Treatment;
import com.mrmi.beautysalon.main.view.table.CancellationReportTableModel;
import net.miginfocom.swing.MigLayout;
import org.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CancellationReportFrame extends JFrame {
    private final TreatmentManager treatmentManager;
    private JDatePickerImpl fromDatePicker;
    private JDatePickerImpl toDatePicker;
    private Date fromDate;
    private Date toDate;
    private ArrayList<String> reasons;
    private ArrayList<Integer> amounts;
    private JTable cancellationTable;
    private JButton buttonBack;

    public CancellationReportFrame(TreatmentManager treatmentManager) {
        this.treatmentManager = treatmentManager;
        reasons = new ArrayList<>();
        amounts = new ArrayList<>();
        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap 2", "[center, grow]", "[center, grow]"));
        this.setTitle("Beauty salon - Treatment cancellation report");
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setIconImage(new ImageIcon("src/images/icon.png").getImage());
        this.setLocationRelativeTo(null);

        add(new JLabel("Treatment cancellation report"), "span");

        JLabel fromLabel = new JLabel("From");
        this.add(fromLabel, "align right");
        fromDatePicker = DatePicker.getDatePicker();
        this.add(fromDatePicker, "align left");

        JLabel toLabel = new JLabel("To");
        this.add(toLabel, "align right");
        toDatePicker = DatePicker.getDatePicker();
        this.add(toDatePicker, "align left");

        CancellationReportTableModel cancellationReportTableModel = new CancellationReportTableModel(reasons, amounts);
        cancellationTable = new JTable(cancellationReportTableModel);
        displayTable(cancellationTable);

        buttonBack = new JButton("Back");
        this.add(buttonBack, "span");

        setVisible(true);
    }

    private void displayTable(JTable table) {
        table.setRowHeight(22);
        this.add(new JScrollPane(table), "span");
        table.setAutoCreateRowSorter(true);
        TableRowSorter<TableModel> tableSorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(tableSorter);
    }

    private void initialiseListeners() {
        fromDatePicker.addActionListener(e -> {
            fromDate = (Date) fromDatePicker.getModel().getValue();
            refreshData();
        });
        toDatePicker.addActionListener(e -> {
            toDate = (Date) toDatePicker.getModel().getValue();
            refreshData();
        });

        buttonBack.addActionListener(e -> this.dispose());
    }

    private void refreshData() {
        if (fromDate == null || toDate == null) {
            return;
        }

        List<Treatment> treatments = treatmentManager.getTreatmentsSortedByCancellationReason();
        if (treatments.size() < 1) {
            return;
        }

        reasons = new ArrayList<>();
        amounts = new ArrayList<>();

        String currentReason = treatments.get(0).getCancellationReason();
        int counter = 1;
        // Loop through all treatments
        for (Treatment t : treatments) {
            // If a treatment does not fit the given date interval continue looping
            if (t.getScheduledDate().getTime().before(fromDate) || t.getScheduledDate().getTime().after(toDate)) {
                continue;
            }
            // If the current treatment's cancellation reason == currentReason
            if (t.getCancellationReason().equals(currentReason)) {
                counter++;
                continue;
            }

            // If not, add the counter and reason to their respective lists
            amounts.add(counter);
            if (!currentReason.equals("N/A")) {
                reasons.add(currentReason);
            } else {
                reasons.add("Finished");
            }

            // Reset the counter and current reason
            counter = 1;
            currentReason = t.getCancellationReason();
        }

        // Since the loop will break without triggering a change on the last treatment, add the data to the lists
        amounts.add(counter);
        if (!currentReason.equals("N/A")) {
            reasons.add(currentReason);
        } else {
            reasons.add("Finished");
        }

        cancellationTable.setModel(new CancellationReportTableModel(reasons, amounts));
    }
}