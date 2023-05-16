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
    private JButton backButton;

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
        this.setSize(1000, 1080);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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

        backButton = new JButton("Back");
        this.add(backButton, "span");
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

        backButton.addActionListener(e -> this.dispose());
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
        int count = 0;
        for (Treatment t : treatments) {
            if (t.getScheduledDate().before(fromDate) || t.getScheduledDate().after(toDate)) {
                continue;
            }
            if (!t.getCancellationReason().equals(currentReason)) {
                if (!currentReason.equals("-")) {
                    reasons.add(currentReason);
                    amounts.add(count);
                } else {
                    reasons.add("Finished");
                    amounts.add(count);
                }
                currentReason = t.getCancellationReason();
            } else {
                count++;
            }
        }

        if (!currentReason.equals("-")) {
            reasons.add(currentReason);
            amounts.add(count);
        } else {
            reasons.add("Finished");
            amounts.add(count);
        }

        // Temporary solution
        cancellationTable.setModel(new CancellationReportTableModel(reasons, amounts));
    }
}