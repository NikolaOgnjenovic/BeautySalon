package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.entity.Treatment;
import com.mrmi.beautysalon.main.view.table.BeauticianProfitTableModel;
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

public class BeauticianProfitFrame extends JFrame {
    private final TreatmentManager treatmentManager;

    private JDatePickerImpl fromDatePicker;
    private JDatePickerImpl toDatePicker;
    private JTable beauticianTable;
    private JButton backButton;
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
        

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

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
        
        this.add(fromLabel, "align right");

        JDatePanelImpl fromDatePanel = new JDatePanelImpl(model, p);
        fromDatePicker = new JDatePickerImpl(fromDatePanel, textField);
        
        this.add(fromDatePicker, "align left");

        JLabel toLabel = new JLabel("To");
        
        this.add(toLabel, "align right");

        JDatePanelImpl toDatePanel = new JDatePanelImpl(model, p);
        toDatePicker = new JDatePickerImpl(toDatePanel, textField);
        
        this.add(toDatePicker, "align left");

        BeauticianProfitTableModel beauticianProfitTableModel = new BeauticianProfitTableModel(beauticianUsernames, finishedTreatments, profits);
        beauticianTable = new JTable(beauticianProfitTableModel);
        beauticianTable.getTableHeader().setReorderingAllowed(false);
        
        displayTable(beauticianTable);

        backButton = new JButton("Back");
        
        this.add(backButton, "span");
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

        backButton.addActionListener(e -> this.dispose());
    }

    private void displayTable(JTable table) {
        
        table.setRowHeight(22);
        this.add(new JScrollPane(table), "span");
        table.setAutoCreateRowSorter(true);
        table.setSelectionModel(new SingleListSelectionModel());
        TableRowSorter<TableModel> tableSorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(tableSorter);
    }

    private void refreshTable() {
        if (fromDate == null || toDate == null) {
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

        // Temporary solution
        beauticianTable.setModel(new BeauticianProfitTableModel(beauticianUsernames, finishedTreatments, profits));

        // TODO: figure out why this doesn't refresh
        //beauticianProfitTableModel.fireTableDataChanged();
    }
}
