package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.entity.Treatment;
import com.mrmi.beautysalon.main.view.table.BeauticianProfitTableModel;
import com.mrmi.beautysalon.main.view.table.SingleListSelectionModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class BeauticianProfitFrame extends JFrame {
    private final TreatmentController treatmentController;

    private JDatePickerImpl fromDatePicker;
    private JDatePickerImpl toDatePicker;
    private JTable beauticianTable;
    private Date fromDate;
    private Date toDate;
    private ArrayList<String> beauticianUsernames;
    private ArrayList<Integer> finishedTreatments;
    private ArrayList<Double> profits;

    public BeauticianProfitFrame(TreatmentController treatmentController) {
        this.treatmentController = treatmentController;
        beauticianUsernames = new ArrayList<>();
        finishedTreatments = new ArrayList<>();
        profits = new ArrayList<>();

        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setTitle("Beautician profit");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(true);
        this.setSize(800, 800);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(235, 235, 235));
        this.setLayout(new FlowLayout());

        BeauticianProfitTableModel beauticianProfitTableModel = new BeauticianProfitTableModel(beauticianUsernames, finishedTreatments, profits);
        beauticianTable = new JTable(beauticianProfitTableModel);
        displayTable(beauticianTable);

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

        fromDatePicker = new JDatePickerImpl(fromDatePanel, textField);
        this.add(fromDatePicker);

        this.add(new JLabel("TO:"));
        toDatePicker = new JDatePickerImpl(toDatePanel, textField);
        this.add(toDatePicker);
    }

    private void initialiseListeners() {
        fromDatePicker.addActionListener(e -> {
            fromDate = (Date) fromDatePicker.getModel().getValue();
            this.add(new JLabel("From: " + fromDate));
            refreshTable();
        });
        toDatePicker.addActionListener(e -> {
            toDate = (Date) toDatePicker.getModel().getValue();
            this.add(new JLabel("To: " + toDate));
            refreshTable();
        });
    }

    private void displayTable(JTable table) {
        this.add(new JScrollPane(table));
        table.setAutoCreateRowSorter(true);
        table.setSelectionModel(new SingleListSelectionModel());
        TableRowSorter<TableModel> tableSorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(tableSorter);
    }

    private void refreshTable() {
        if (fromDate == null || toDate == null) {
            return;
        }

        List<Treatment> treatments = treatmentController.getTreatmentsSortedByBeauticians();
        if (treatments.size() < 1) {
            return;
        }

        beauticianUsernames = new ArrayList<>();
        finishedTreatments = new ArrayList<>();
        profits = new ArrayList<>();

        String currentBeautician = treatments.get(0).getBeauticianUsername();
        int count = 0;
        double profit = 0;

        for (Treatment t : treatments) {
            if (t.getScheduledDate().before(fromDate) || t.getScheduledDate().after(toDate)) {
                continue;
            }
            if (!t.getBeauticianUsername().equals(currentBeautician)) {
                beauticianUsernames.add(currentBeautician);
                finishedTreatments.add(count);
                profits.add(profit);

                currentBeautician = t.getBeauticianUsername();
                count = 0;
                profit = 0;
            } else {
                count++;
                profit += t.getPrice();
            }
        }

        beauticianUsernames.add(currentBeautician);
        finishedTreatments.add(count);
        profits.add(profit);

        // Temporary solution
        beauticianTable.setModel(new BeauticianProfitTableModel(beauticianUsernames, finishedTreatments, profits));

        // TODO: figure out why this doesn't refresh
        //beauticianProfitTableModel.fireTableDataChanged();
    }
}
