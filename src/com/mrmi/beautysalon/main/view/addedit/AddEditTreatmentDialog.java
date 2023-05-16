package com.mrmi.beautysalon.main.view.addedit;

import com.mrmi.beautysalon.main.entity.Treatment;
import com.mrmi.beautysalon.main.entity.TreatmentType;
import com.mrmi.beautysalon.main.exceptions.TreatmentTypeNotFoundException;
import com.mrmi.beautysalon.main.exceptions.UserNotFoundException;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import com.mrmi.beautysalon.main.view.DatePicker;
import com.mrmi.beautysalon.main.view.TreatmentsFrame;
import net.miginfocom.swing.MigLayout;
import org.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class AddEditTreatmentDialog extends JDialog {
    private final JFrame parent;
    private final TreatmentManager treatmentManager;
    private final UserManager userManager;
    private final Treatment treatment;
    private final int id;
    private final boolean isClient;

    public AddEditTreatmentDialog(JFrame parent, TreatmentManager treatmentManager, UserManager userManager, Treatment treatment, int id, boolean isClient) {
        super(parent, true);
        this.parent = parent;
        
        if (treatment != null) {
            setTitle("Edit treatment");
        } else {
            setTitle("Add treatment");
        }

        this.treatmentManager = treatmentManager;
        this.userManager = userManager;
        this.treatment = treatment;
        this.id = id;
        this.isClient = isClient;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initialiseViews();
        pack();
    }

    private void initialiseViews() {
        setLayout(new MigLayout("wrap 2", "", "[]10"));

        JComboBox<TreatmentType> comboBoxType = new JComboBox<>();
        for (Map.Entry<Integer, TreatmentType> type: treatmentManager.getAvailableTreatmentTypes().entrySet()) {
            comboBoxType.addItem(type.getValue());
        }
        add(new JLabel("Treatment type"));
        add(comboBoxType);

        add(new JLabel("Scheduled date"));
        JDatePickerImpl datePicker = DatePicker.getDatePicker();
        add(datePicker);

        add(new JLabel("Price"));
        JTextField textPrice = new JTextField(5);
        add(textPrice);

        add(new JLabel("Client username"));
        JTextField textClientUsername = new JTextField(20);
        add(textClientUsername);

        add(new JLabel("Beautician username"));
        JTextField textBeauticianUsername = new JTextField(20);
        add(textBeauticianUsername);

        JTextField textCancellationReason = new JTextField(20);
        JComboBox<Treatment.Status> comboBoxStatus = new JComboBox<>();
        for (Treatment.Status status : Treatment.Status.values()) {
            comboBoxStatus.addItem(status);
        }
        comboBoxStatus.addActionListener(e -> textCancellationReason.setEnabled(isClient || comboBoxStatus.getSelectedItem() == Treatment.Status.CANCELLED_BY_CLIENT || comboBoxStatus.getSelectedItem() == Treatment.Status.CANCELLED_BY_SALON));
        add(new JLabel("Status"));
        add(comboBoxStatus);

        add(new JLabel("Cancellation reason"));
        textCancellationReason.setEnabled(isClient || comboBoxStatus.getSelectedItem() == Treatment.Status.CANCELLED_BY_CLIENT || comboBoxStatus.getSelectedItem() == Treatment.Status.CANCELLED_BY_SALON);
        add(textCancellationReason);

        JButton buttonCancel = new JButton("Cancel");
        add(buttonCancel);

        JButton buttonOK = new JButton("OK");
        add(buttonOK);

        // If a treatment is being edited then the treatment parameter of the constructor != null
        if (treatment != null) {
            try {
                comboBoxType.setSelectedItem(treatmentManager.getTreatmentTypeById(treatment.getTreatmentTypeId()));
            } catch (TreatmentTypeNotFoundException ignored) {
            }

            datePicker.getModel().setDate(treatment.getScheduledDate().get(Calendar.YEAR), treatment.getScheduledDate().get(Calendar.MONTH), treatment.getScheduledDate().get(Calendar.DAY_OF_MONTH));
            datePicker.getModel().setSelected(true);
            textPrice.setText(String.valueOf(treatment.getPrice()));
            textClientUsername.setText(treatment.getClientUsername());
            textBeauticianUsername.setText(treatment.getBeauticianUsername());
            comboBoxStatus.setSelectedItem(treatment.getStatus());
            textCancellationReason.setText(treatment.getCancellationReason());
        }

        buttonOK.addActionListener(e -> {
            if (treatment != null) {
                if (comboBoxType.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Invalid treatment type", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int treatmentTypeId = treatmentManager.getTreatmentTypeIdByName(comboBoxType.getSelectedItem().toString());

                Calendar scheduledDate = Calendar.getInstance();
                Date date = (Date) datePicker.getModel().getValue();
                if (date == null) {
                    JOptionPane.showMessageDialog(null, "Invalid date", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                scheduledDate.setTime(date);

                double price = Double.parseDouble(textPrice.getText().trim());
                if (price <= 0) {
                    JOptionPane.showMessageDialog(null, "Invalid price", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String clientUsername = textClientUsername.getText().trim();
                String beauticianUsername = textBeauticianUsername.getText().trim();
                Treatment.Status status = (Treatment.Status) comboBoxStatus.getSelectedItem();
                if (status == null) {
                    JOptionPane.showMessageDialog(null, "Invalid price", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String cancellationReason = textCancellationReason.getText();
                if (comboBoxStatus.getSelectedItem() == Treatment.Status.CANCELLED_BY_CLIENT || comboBoxStatus.getSelectedItem() == Treatment.Status.CANCELLED_BY_SALON) {
                    if (!cancellationReason.equals("")) {
                        int clientId;
                        try {
                            clientId = userManager.getClientIdByUsername(clientUsername);
                        } catch (UserNotFoundException ex) {
                            JOptionPane.showMessageDialog(null, "Invalid client username", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        treatmentManager.cancelTreatment(clientId, id, false, cancellationReason, userManager);
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid cancellation reason", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                treatmentManager.updateTreatment(id, treatment, treatmentTypeId, scheduledDate, price, clientUsername, beauticianUsername, status);
            }

            ((TreatmentsFrame) parent).refreshData();
            this.dispose();
        });

        buttonCancel.addActionListener(e -> this.dispose());
    }
}