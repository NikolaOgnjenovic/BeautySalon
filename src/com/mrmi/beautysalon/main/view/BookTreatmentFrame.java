package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.exceptions.TreatmentTypeNotFoundException;
import com.mrmi.beautysalon.main.manager.SalonManager;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import com.mrmi.beautysalon.main.entity.Beautician;
import com.mrmi.beautysalon.main.entity.Treatment;
import com.mrmi.beautysalon.main.view.table.GenericTable;
import com.mrmi.beautysalon.main.view.table.GenericTableModel;
import net.miginfocom.swing.MigLayout;
import org.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import java.util.*;

public class BookTreatmentFrame extends JFrame {
    private final SalonManager salonManager;
    private final TreatmentManager treatmentManager;
    private final UserManager userManager;
    private final String clientUsername;
    private int treatmentTypeId;
    private int treatmentTypeCategoryId;
    private double treatmentPrice;
    private String beauticianUsername;
    private JComboBox<Beautician> comboBoxBeauticians;
    private HashMap<Integer, Beautician> beauticians;
    private Calendar selectedDate;
    private String selectedTime;
    private Vector<String> availableTimeSlots;
    private GenericTable treatmentTypeTable;
    private JComboBox<String> treatmentAvailableTimeSlots;
    private JDatePickerImpl datePicker;
    private JButton buttonBook;
    private JButton buttonBack;
    private JTextField textTreatmentTypeFilter;

    public BookTreatmentFrame(SalonManager salonManager, TreatmentManager treatmentManager, UserManager userManager, String clientUsername) {
        this.salonManager = salonManager;
        this.treatmentManager = treatmentManager;
        this.userManager = userManager;
        this.clientUsername = clientUsername;

        initialiseViews();
        initialiseListeners();
    }
    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap 1", "[center, grow]", "[center, grow]"));
        this.setTitle("Beauty salon - Book treatment");
        this.setSize(1000, 1080);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GenericTableModel tableModel = new GenericTableModel(treatmentManager.getAvailableTreatmentTypes(), treatmentManager);
        treatmentTypeTable = new GenericTable(tableModel);
        this.add(new JScrollPane(treatmentTypeTable), "span, growx");

        textTreatmentTypeFilter = new JTextField("Search treatment types", 20);
        this.add(textTreatmentTypeFilter);

        beauticians = new HashMap<>();
        comboBoxBeauticians = new JComboBox<>();
        this.add(comboBoxBeauticians);

        JTextField textBeauticianFilter = new JTextField("Search beauticians", 20);
        this.add(textBeauticianFilter);

        datePicker = DatePicker.getDatePicker();
        this.add(datePicker);
        datePicker.setVisible(false);

        availableTimeSlots = new Vector<>();
        treatmentAvailableTimeSlots = new JComboBox<>(availableTimeSlots);
        treatmentAvailableTimeSlots.setVisible(false);
        
        this.add(treatmentAvailableTimeSlots);
        treatmentAvailableTimeSlots.setVisible(false);

        buttonBook = new JButton("Book treatment");
        this.add(buttonBook);
        buttonBook.setVisible(false);

        buttonBack = new JButton("Back");
        this.add(buttonBack);
    }

    private void initialiseListeners() {
        treatmentTypeTable.getSelectionModel().addListSelectionListener(e -> {
            if (treatmentTypeTable.getSelectedRow() != -1) {
                treatmentTypeId = (int) treatmentTypeTable.getValueAt(treatmentTypeTable.getSelectedRow(), 0);
                try {
                    treatmentTypeCategoryId = treatmentManager.getTreatmentType(treatmentTypeId).getCategoryId();
                    treatmentPrice = (double) treatmentTypeTable.getValueAt(treatmentTypeTable.getSelectedRow(), 3);
                    displayAvailableBeauticians(treatmentTypeCategoryId);
                    treatmentTypeTable.clearSelection();
                } catch (TreatmentTypeNotFoundException ignored) {
                }

            } else {
                buttonBook.setVisible(false);
                treatmentAvailableTimeSlots.setVisible(false);
            }
        });

        textTreatmentTypeFilter.addActionListener(e -> treatmentTypeTable.filter(textTreatmentTypeFilter.getText()));

        comboBoxBeauticians.addActionListener(e -> {
            Beautician beautician = (Beautician) comboBoxBeauticians.getSelectedItem();
            if (beautician != null) {
                beauticianUsername = beautician.getUsername();
                datePicker.setVisible(true);
            }
        });

        selectedDate = Calendar.getInstance();
        datePicker.addActionListener(e -> {
            Date date = (Date) datePicker.getModel().getValue();
            selectedDate.setTime(date);
            refreshTimeComboBox();
        });

        treatmentAvailableTimeSlots.addActionListener(e -> {
            if (treatmentAvailableTimeSlots.getSelectedItem() != null) {
                selectedTime = treatmentAvailableTimeSlots.getSelectedItem().toString();
                buttonBook.setVisible(true);
            } else {
                buttonBook.setVisible(false);
                treatmentAvailableTimeSlots.setVisible(false);
            }
        });

        buttonBook.addActionListener(e -> {
            selectedDate.set(Calendar.HOUR, Integer.parseInt(selectedTime.substring(0, 2)));
            selectedDate.set(Calendar.MINUTE, 0);
            selectedDate.set(Calendar.SECOND, 0);
            userManager.bookTreatment(new Treatment(selectedDate, clientUsername, beauticianUsername, treatmentTypeId, treatmentPrice), treatmentManager);
            this.dispose();
        });

        buttonBack.addActionListener(e -> this.dispose());
    }

    private void displayAvailableBeauticians(int treatmentTypeCategoryId) {
        beauticians = userManager.getBeauticiansByTreatmentTypeCategory(treatmentTypeCategoryId);
        if (beauticians.size() < 1) {
            return;
        }
        comboBoxBeauticians.removeAllItems();
        for (Beautician b : beauticians.values()) {
            comboBoxBeauticians.addItem(b);
        }
    }

    private void refreshTimeComboBox() {
        availableTimeSlots = treatmentManager.getAvailableTimeSlots(selectedDate, treatmentTypeId, salonManager);
        treatmentAvailableTimeSlots.removeAllItems();
        treatmentAvailableTimeSlots.setModel(new DefaultComboBoxModel<>(availableTimeSlots));
        treatmentAvailableTimeSlots.setVisible(true);
    }
}
