package com.mrmi.beautysalon.main.gui;

import com.mrmi.beautysalon.main.objects.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.*;

public class BookTreatmentFrame extends JFrame {
    private final Database database;
    private byte treatmentTypeId;
    private double treatmentPrice;
    private String beauticianUsername;
    private BeauticianTableModel beauticianTableModel;
    private HashMap<String, Beautician> beauticians;
    private final JTable beauticianTable;
    private Date selectedDate;
    private String selectedTime;
    private Vector<String> timeWindows;
    private JComboBox<String> treatmentTimeWindows;
    public BookTreatmentFrame(Database database, String clientUsername) {
        this.database = database;
        this.setTitle("Book treatment");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(800, 800);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(235, 235, 235));
        this.setLayout(new FlowLayout());

        TreatmentTypeTableModel treatmentTableModel = new TreatmentTypeTableModel(database, database.getTreatmentTypes(), false);
        JTable treatmentTypeTable = new JTable(treatmentTableModel);
        this.add(new JScrollPane(treatmentTypeTable));
        treatmentTypeTable.setAutoCreateRowSorter(true);
        treatmentTypeTable.setSelectionModel(new SingleListSelectionModel());
        TableRowSorter<TableModel> treatmentTableSorter = new TableRowSorter<>(treatmentTypeTable.getModel());
        treatmentTypeTable.setRowSorter(treatmentTableSorter);
        JTextField filterText = new JTextField("Search", 20);
        filterText.addActionListener(e -> filter(filterText.getText(), treatmentTableSorter));
        this.add(filterText);

        treatmentTypeTable.getSelectionModel().addListSelectionListener(e -> {
            if (treatmentTypeTable.getSelectedRow() != -1) {
                treatmentTypeId = Byte.parseByte(treatmentTypeTable.getValueAt(treatmentTypeTable.getSelectedRow(), 6).toString());
                treatmentPrice = Double.parseDouble(treatmentTypeTable.getValueAt(treatmentTypeTable.getSelectedRow(), 2).toString());
                displayAvailableBeauticians(treatmentTypeId);
            }
        });

        beauticians = new HashMap<>();
        beauticianTableModel = new BeauticianTableModel(beauticians);
        beauticianTable = new JTable(beauticianTableModel);
        this.add(new JScrollPane(beauticianTable));
        beauticianTable.setAutoCreateRowSorter(true);
        beauticianTable.setSelectionModel(new SingleListSelectionModel());

        TableRowSorter<TableModel> beauticianTableSorter = new TableRowSorter<>(beauticianTable.getModel());
        beauticianTable.setRowSorter(beauticianTableSorter);
        JTextField beauticianFilterText = new JTextField("Search", 20);
        beauticianFilterText.addActionListener(e -> filter(beauticianFilterText.getText(), beauticianTableSorter));
        this.add(beauticianFilterText);

        beauticianTable.getSelectionModel().addListSelectionListener(e -> {
            beauticianUsername = beauticianTable.getValueAt(beauticianTable.getSelectedRow(), 0).toString();
        });

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new JFormattedTextField.AbstractFormatter() {
            @Override
            public Object stringToValue(String text) {
                return null;
            }

            @Override
            public String valueToString(Object value) {
                return null;
            }
        });
        selectedDate = (Date) datePicker.getModel().getValue();
        datePicker.addActionListener(e -> {
            selectedDate = (Date) datePicker.getModel().getValue();
            refreshTimeComboBox();
        });
        this.add(datePicker);

        timeWindows = database.getTreatmentTimeWindows(new Date(), 7);
        treatmentTimeWindows = new JComboBox<>(timeWindows);
        treatmentTimeWindows.addActionListener(e -> {
            if (treatmentTimeWindows.getSelectedItem() != null) {
                selectedTime = treatmentTimeWindows.getSelectedItem().toString();
            }
        });
        this.add(treatmentTimeWindows);

        JButton bookButton = new JButton("Book treatment");
        bookButton.addActionListener(e -> {
            selectedDate.setHours(Integer.parseInt(selectedTime.substring(0, 2)));
            selectedDate.setMinutes(0);
            selectedDate.setSeconds(0);
            database.bookTreatment(new Treatment(selectedDate, false, clientUsername, beauticianUsername, treatmentTypeId, treatmentPrice), database.getNextTreatmentId());
        });
        this.add(bookButton);
    }

    private void displayAvailableBeauticians(byte treatmentTypeId) {
        beauticians = database.getBeauticiansByTreatmentType(treatmentTypeId);
        if (beauticians.size() < 1) {
            return;
        }
        beauticianTableModel = new BeauticianTableModel(beauticians);
        beauticianTable.setModel(beauticianTableModel);
    }

    private void filter(String text, TableRowSorter<TableModel> tableSorter) {
        RowFilter<TableModel, Object> rf;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(text);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        tableSorter.setRowFilter(rf);
    }

    private void refreshTimeComboBox() {
        timeWindows = database.getTreatmentTimeWindows(selectedDate, treatmentTypeId);
        treatmentTimeWindows.removeAllItems();
        treatmentTimeWindows.setModel(new DefaultComboBoxModel<>(timeWindows));
    }
}
