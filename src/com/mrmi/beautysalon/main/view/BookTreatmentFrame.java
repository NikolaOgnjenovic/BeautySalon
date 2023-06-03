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
    private int typeId;
    private int categoryId;
    private float treatmentPrice;
    private String beauticianUsername;
    private JComboBox<Beautician> comboBoxBeauticians;
    private HashMap<Integer, Beautician> beauticians;
    private Calendar selectedDate;
    private String selectedTime;
    private Vector<String> availableTimeSlots;
    private GenericTable typeTable;
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
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setIconImage(new ImageIcon("src/images/icon.png").getImage());
        this.setLocationRelativeTo(null);

        add(new JLabel("Book a treatmnet"), "span");

        GenericTableModel tableModel = new GenericTableModel(treatmentManager.getAvailableTreatmentTypes(), treatmentManager);
        typeTable = new GenericTable(tableModel);
        this.add(new JScrollPane(typeTable), "span, growx");

        textTreatmentTypeFilter = new JTextField("Search treatment types", 20);
        this.add(textTreatmentTypeFilter);

        beauticians = new HashMap<>();
        comboBoxBeauticians = new JComboBox<>();
        this.add(comboBoxBeauticians);

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
        typeTable.getSelectionModel().addListSelectionListener(e -> {
            if (typeTable.getSelectedRow() != -1) {
                typeId = (int) typeTable.getValueAt(typeTable.getSelectedRow(), 0);
                try {
                    categoryId = treatmentManager.getTreatmentType(typeId).getCategoryId();
                    treatmentPrice = (float) typeTable.getValueAt(typeTable.getSelectedRow(), 3);
                    displayAvailableBeauticians(categoryId);
                    typeTable.clearSelection();
                } catch (TreatmentTypeNotFoundException ignored) {
                }

            } else {
                buttonBook.setVisible(false);
                treatmentAvailableTimeSlots.setVisible(false);
            }
        });

        textTreatmentTypeFilter.addActionListener(e -> typeTable.filter(textTreatmentTypeFilter.getText()));

        comboBoxBeauticians.addActionListener(e -> {
            Beautician beautician = (Beautician) comboBoxBeauticians.getSelectedItem();
            if (beautician != null) {
                beauticianUsername = beautician.getUsername();
                datePicker.setVisible(false);
                datePicker.setVisible(true);
                treatmentAvailableTimeSlots.setVisible(false);
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
            selectedDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(selectedTime.substring(0, 2)));
            selectedDate.set(Calendar.MINUTE, 0);
            selectedDate.set(Calendar.SECOND, 0);
            userManager.bookTreatment(new Treatment(selectedDate, clientUsername, beauticianUsername, typeId, treatmentPrice), treatmentManager);
            this.dispose();
        });

        buttonBack.addActionListener(e -> this.dispose());
    }

    private void displayAvailableBeauticians(int categoryId) {
        beauticians = userManager.getBeauticiansByTreatmentTypeCategory(categoryId);
        if (beauticians.size() < 1) {
            return;
        }
        comboBoxBeauticians.removeAllItems();
        for (Beautician b : beauticians.values()) {
            comboBoxBeauticians.addItem(b);
        }
    }

    private void refreshTimeComboBox() {
        availableTimeSlots = treatmentManager.getAvailableTimeSlots(userManager, beauticianUsername, selectedDate, typeId, salonManager);
        treatmentAvailableTimeSlots.removeAllItems();
        treatmentAvailableTimeSlots.setModel(new DefaultComboBoxModel<>(availableTimeSlots));
        treatmentAvailableTimeSlots.setVisible(true);
    }
}
