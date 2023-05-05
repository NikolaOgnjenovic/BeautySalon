package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.entity.Treatment;
import com.mrmi.beautysalon.main.view.table.CancellationReportTableModel;
import com.mrmi.beautysalon.main.view.table.SingleListSelectionModel;
import net.miginfocom.swing.MigLayout;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

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
        this.setVisible(true);

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        JDatePanelImpl fromDatePanel = new JDatePanelImpl(model, p);
        JDatePanelImpl toDatePanel = new JDatePanelImpl(model, p);
        JFormattedTextField.AbstractFormatter textField = new JFormattedTextField.AbstractFormatter() {
            @Override
            public Object stringToValue(String text) {
                return null;
            }

            @Override
            public String valueToString(Object value) {
                return null;
            }
        };

        JLabel fromLabel = new JLabel("From");
        Utility.setFont(fromLabel, 24);
        this.add(fromLabel, "align right");

        fromDatePicker = new JDatePickerImpl(fromDatePanel, textField);
        Utility.setFont(fromDatePicker, 24);
        this.add(fromDatePicker, "align left");

        JLabel toLabel = new JLabel("To");
        Utility.setFont(toLabel, 24);
        this.add(toLabel, "align right");

        toDatePicker = new JDatePickerImpl(toDatePanel, textField);
        Utility.setFont(toDatePicker, 24);
        this.add(toDatePicker, "align left");

        CancellationReportTableModel cancellationReportTableModel = new CancellationReportTableModel(reasons, amounts);
        cancellationTable = new JTable(cancellationReportTableModel);
        cancellationTable.getTableHeader().setReorderingAllowed(false);
        displayTable(cancellationTable);

        backButton = new JButton("Back");
        Utility.setFont(backButton, 24);
        this.add(backButton, "span");
    }

    private void displayTable(JTable table) {
        Utility.setFont(table, 20);
        table.setRowHeight(22);
        this.add(new JScrollPane(table), "span");
        table.setAutoCreateRowSorter(true);
        table.setSelectionModel(new SingleListSelectionModel());
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