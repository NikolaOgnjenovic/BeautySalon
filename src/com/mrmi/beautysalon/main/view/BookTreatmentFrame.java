package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.controller.UserController;
import com.mrmi.beautysalon.main.entity.Beautician;
import com.mrmi.beautysalon.main.entity.BeautySalon;
import com.mrmi.beautysalon.main.entity.Treatment;
import com.mrmi.beautysalon.main.view.table.BeauticianTableModel;
import com.mrmi.beautysalon.main.view.table.SingleListSelectionModel;
import com.mrmi.beautysalon.main.view.table.TreatmentTypeTableModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.*;

public class BookTreatmentFrame extends JFrame {
    private final TreatmentController treatmentController;
    private final UserController userController;
    private final BeautySalon beautySalon;
    private final String clientUsername;
    private byte treatmentTypeId;
    private double treatmentPrice;
    private String beauticianUsername;
    private BeauticianTableModel beauticianTableModel;
    private HashMap<String, Beautician> beauticians;
    private Date selectedDate;
    private String selectedTime;
    private Vector<String> timeWindows;
    private JTable treatmentTypeTable;
    private JTable beauticianTable;
    private JComboBox<String> treatmentTimeWindows;
    private JDatePickerImpl datePicker;
    private JButton bookButton;

    public BookTreatmentFrame(TreatmentController treatmentController, UserController userController, BeautySalon beautySalon, String clientUsername) {
        this.treatmentController = treatmentController;
        this.userController = userController;
        this.beautySalon = beautySalon;
        this.clientUsername = clientUsername;
        initialiseViews();
        initialiseListeners();
    }
    private void initialiseViews() {
        this.setTitle("Book treatment");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(true);
        this.setSize(800, 800);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(235, 235, 235));
        this.setLayout(new FlowLayout());

        TreatmentTypeTableModel treatmentTableModel = new TreatmentTypeTableModel(treatmentController, treatmentController.getTreatmentTypes(), false);
        treatmentTypeTable = new JTable(treatmentTableModel);
        displayTable(treatmentTypeTable);

        beauticians = new HashMap<>();
        beauticianTableModel = new BeauticianTableModel(beauticians);
        beauticianTable = new JTable(beauticianTableModel);
        displayTable(beauticianTable);

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new JFormattedTextField.AbstractFormatter() {
            @Override
            public Object stringToValue(String text) {
                return null;
            }

            @Override
            public String valueToString(Object value) {
                return null;
            }
        });

        this.add(datePicker);

        timeWindows = new Vector<>();
        treatmentTimeWindows = new JComboBox<>(timeWindows);
        this.add(treatmentTimeWindows);

        bookButton = new JButton("Book treatment");
        this.add(bookButton);
    }

    private void displayTable(JTable table) {
        this.add(new JScrollPane(table));
        table.setAutoCreateRowSorter(true);
        table.setSelectionModel(new SingleListSelectionModel());
        TableRowSorter<TableModel> tableSorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(tableSorter);
        JTextField filterText = new JTextField("Search", 20);
        filterText.addActionListener(e -> filter(filterText.getText(), tableSorter));
        this.add(filterText);
    }

    private void initialiseListeners() {
        treatmentTypeTable.getSelectionModel().addListSelectionListener(e -> {
            if (treatmentTypeTable.getSelectedRow() != -1) {
                treatmentTypeId = Byte.parseByte(treatmentTypeTable.getValueAt(treatmentTypeTable.getSelectedRow(), 6).toString());
                treatmentPrice = Double.parseDouble(treatmentTypeTable.getValueAt(treatmentTypeTable.getSelectedRow(), 2).toString());
                displayAvailableBeauticians(treatmentTypeId);
            }
        });

        beauticianTable.getSelectionModel().addListSelectionListener(e -> beauticianUsername = beauticianTable.getValueAt(beauticianTable.getSelectedRow(), 0).toString());

        selectedDate = (Date) datePicker.getModel().getValue();
        datePicker.addActionListener(e -> {
            selectedDate = (Date) datePicker.getModel().getValue();
            refreshTimeComboBox();
        });

        treatmentTimeWindows.addActionListener(e -> {
            if (treatmentTimeWindows.getSelectedItem() != null) {
                selectedTime = treatmentTimeWindows.getSelectedItem().toString();
            }
        });

        bookButton.addActionListener(e -> {
            selectedDate.setHours(Integer.parseInt(selectedTime.substring(0, 2)));
            selectedDate.setMinutes(0);
            selectedDate.setSeconds(0);
            userController.bookTreatment(new Treatment(selectedDate, false, clientUsername, beauticianUsername, treatmentTypeId, treatmentPrice), beautySalon.getLoyaltyThreshold());
        });
    }

    private void displayAvailableBeauticians(byte treatmentTypeId) {
        beauticians = userController.getBeauticiansByTreatmentType(treatmentTypeId);
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
        timeWindows = treatmentController.getTreatmentTimeWindows(selectedDate, treatmentTypeId, beautySalon);
        treatmentTimeWindows.removeAllItems();
        treatmentTimeWindows.setModel(new DefaultComboBoxModel<>(timeWindows));
    }
}
