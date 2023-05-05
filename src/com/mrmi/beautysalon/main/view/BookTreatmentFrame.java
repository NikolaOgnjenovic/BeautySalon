package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.controller.UserController;
import com.mrmi.beautysalon.main.entity.Beautician;
import com.mrmi.beautysalon.main.entity.BeautySalon;
import com.mrmi.beautysalon.main.entity.Treatment;
import com.mrmi.beautysalon.main.view.table.BeauticianTableModel;
import com.mrmi.beautysalon.main.view.table.SingleListSelectionModel;
import com.mrmi.beautysalon.main.view.table.TreatmentTypeTableModel;
import net.miginfocom.swing.MigLayout;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
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
    private JButton backButton;

    public BookTreatmentFrame(TreatmentController treatmentController, UserController userController, BeautySalon beautySalon, String clientUsername) {
        this.treatmentController = treatmentController;
        this.userController = userController;
        this.beautySalon = beautySalon;
        this.clientUsername = clientUsername;
        initialiseViews();
        initialiseListeners();
    }
    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap 1", "[center, grow]", "[center, grow]"));
        this.setTitle("Beauty salon - Book treatment");
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);

        TreatmentTypeTableModel treatmentTableModel = new TreatmentTypeTableModel(treatmentController, treatmentController.getTreatmentTypes(), false);
        treatmentTypeTable = new JTable(treatmentTableModel);
        displayTable(treatmentTypeTable);

        beauticians = new HashMap<>();
        beauticianTableModel = new BeauticianTableModel(treatmentController, beauticians);
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

        Utility.setFont(datePicker, 24);
        this.add(datePicker);
        datePicker.setVisible(false);

        timeWindows = new Vector<>();
        treatmentTimeWindows = new JComboBox<>(timeWindows);
        Utility.setFont(treatmentTimeWindows, 24);
        this.add(treatmentTimeWindows);
        treatmentTimeWindows.setVisible(false);

        bookButton = new JButton("Book treatment");
        Utility.setFont(bookButton, 24);
        this.add(bookButton);
        bookButton.setVisible(false);

        backButton = new JButton("Back");
        Utility.setFont(backButton, 24);
        this.add(backButton);
    }

    private void displayTable(JTable table) {
        Utility.setFont(table, 20);
        table.setRowHeight(22);
        this.add(new JScrollPane(table), "growx");
        table.setAutoCreateRowSorter(true);
        table.setSelectionModel(new SingleListSelectionModel());
        TableRowSorter<TableModel> tableSorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(tableSorter);

        JTextField filterText = new JTextField("Search", 20);
        filterText.addActionListener(e -> filter(filterText.getText(), tableSorter));
        Utility.setFont(filterText, 24);
        this.add(filterText);
    }

    private void initialiseListeners() {
        treatmentTypeTable.getSelectionModel().addListSelectionListener(e -> {
            if (treatmentTypeTable.getSelectedRow() != -1) {
                treatmentTypeId = (byte) treatmentController.getTreatmentTypeIdByName(treatmentTypeTable.getValueAt(treatmentTypeTable.getSelectedRow(), 1).toString());
                treatmentPrice = Double.parseDouble(treatmentTypeTable.getValueAt(treatmentTypeTable.getSelectedRow(), 2).toString());
                displayAvailableBeauticians(treatmentTypeId);
            } else {
                bookButton.setVisible(false);
                treatmentTimeWindows.setVisible(false);
            }
        });

        beauticianTable.getSelectionModel().addListSelectionListener(e -> {
            beauticianUsername = beauticianTable.getValueAt(beauticianTable.getSelectedRow(), 0).toString();
            datePicker.setVisible(true);
        });

        selectedDate = (Date) datePicker.getModel().getValue();
        datePicker.addActionListener(e -> {
            selectedDate = (Date) datePicker.getModel().getValue();
            refreshTimeComboBox();
        });

        treatmentTimeWindows.addActionListener(e -> {
            if (treatmentTimeWindows.getSelectedItem() != null) {
                selectedTime = treatmentTimeWindows.getSelectedItem().toString();
                bookButton.setVisible(true);
            } else {
                bookButton.setVisible(false);
                treatmentTimeWindows.setVisible(false);
            }
        });

        bookButton.addActionListener(e -> {
            selectedDate.setHours(Integer.parseInt(selectedTime.substring(0, 2)));
            selectedDate.setMinutes(0);
            selectedDate.setSeconds(0);
            userController.bookTreatment(new Treatment(selectedDate, false, clientUsername, beauticianUsername, treatmentTypeId, treatmentPrice), beautySalon.getLoyaltyThreshold());
        });

        backButton.addActionListener(e -> {
            this.dispose();
            //previousFrame.setVisible(true);
        });
    }

    private void displayAvailableBeauticians(byte treatmentTypeId) {
        beauticians = userController.getBeauticiansByTreatmentType(treatmentTypeId);
        if (beauticians.size() < 1) {
            return;
        }
        beauticianTableModel = new BeauticianTableModel(treatmentController, beauticians);
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
        treatmentTimeWindows.setVisible(true);
    }
}
