package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.entity.Treatment;
import com.mrmi.beautysalon.main.view.table.BeauticianProfitTableModel;
import net.miginfocom.swing.MigLayout;
import org.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BeauticianProfitFrame extends JFrame {
    private final TreatmentManager treatmentManager;

    private JDatePickerImpl fromDatePicker;
    private JDatePickerImpl toDatePicker;
    private JTable beauticianTable;
    private JButton buttonBack;
    private Date fromDate;
    private Date toDate;
    private ArrayList<String> beauticianUsernames;
    private ArrayList<Integer> finishedTreatments;
    private ArrayList<Double> profits;

    public BeauticianProfitFrame(TreatmentManager treatmentManager) {
        this.treatmentManager = treatmentManager;
        beauticianUsernames = new ArrayList<>();
        finishedTreatments = new ArrayList<>();
        profits = new ArrayList<>();

        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap 2", "[center, grow]", "[center, grow]"));
        this.setTitle("Beauty salon - Beautician profit graph");
        this.setSize(1000, 1080);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel fromLabel = new JLabel("From");
        this.add(fromLabel, "align right");
        fromDatePicker = DatePicker.getDatePicker();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        fromDatePicker.getModel().setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_YEAR));
        fromDatePicker.getModel().setSelected(true);
        this.add(fromDatePicker, "align left");

        JLabel toLabel = new JLabel("To");
        this.add(toLabel, "align right");
        toDatePicker = DatePicker.getDatePicker();
        calendar.add(Calendar.DAY_OF_YEAR, 30);
        toDatePicker.getModel().setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_YEAR));
        toDatePicker.getModel().setSelected(true);
        this.add(toDatePicker, "align left");

        BeauticianProfitTableModel beauticianProfitTableModel = new BeauticianProfitTableModel(beauticianUsernames, finishedTreatments, profits);
        beauticianTable = new JTable(beauticianProfitTableModel);
        displayTable(beauticianTable);

        refreshTable();

        buttonBack = new JButton("Back");
        this.add(buttonBack, "span");
    }

    private void initialiseListeners() {
        fromDatePicker.addActionListener(e -> {
            fromDate = (Date) fromDatePicker.getModel().getValue();
            refreshTable();
        });

        toDatePicker.addActionListener(e -> {
            toDate = (Date) toDatePicker.getModel().getValue();
            refreshTable();
        });

        buttonBack.addActionListener(e -> this.dispose());
    }

    private void displayTable(JTable table) {
        table.setRowHeight(22);
        this.add(new JScrollPane(table), "span");
        table.setAutoCreateRowSorter(true);
        TableRowSorter<TableModel> tableSorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(tableSorter);
    }

    private void refreshTable() {
        beauticianTable.removeAll();
        if (fromDate == null || toDate == null || fromDate.after(toDate)) {
            return;
        }

        List<Treatment> treatments = treatmentManager.getTreatmentsSortedByBeauticians();
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

        beauticianTable.setModel(new BeauticianProfitTableModel(beauticianUsernames, finishedTreatments, profits));
    }
}
