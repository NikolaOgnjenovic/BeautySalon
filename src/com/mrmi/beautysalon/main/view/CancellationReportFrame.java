package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.entity.Treatment;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class CancellationReportFrame extends JFrame {
    private final TreatmentController treatmentController;
    private JDatePickerImpl fromDatePicker;
    private JDatePickerImpl toDatePicker;
    private Date fromDate;
    private Date toDate;

    public CancellationReportFrame(TreatmentController treatmentController) {
        this.treatmentController = treatmentController;

        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setTitle("Cancellation report");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(true);
        this.setSize(800, 800);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(235, 235, 235));
        this.setLayout(new FlowLayout());

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
            refreshData();
        });
        toDatePicker.addActionListener(e -> {
            toDate = (Date) toDatePicker.getModel().getValue();
            refreshData();
        });
    }

    // TODO: table / enumerator.size * jlabel instead of temp for loop
    private void refreshData() {
        if (fromDate == null || toDate == null) {
            return;
        }

        List<Treatment> treatments = treatmentController.getTreatmentsSortedByCancellationReason();
        if (treatments.size() < 1) {
            return;
        }
        String currentReason = treatments.get(0).getCancellationReason();
        int count = 0;
        // Temporary solution : add new JLabels for everything
        for (Treatment t : treatments) {
            if (t.getScheduledDate().before(fromDate) || t.getScheduledDate().after(toDate)) {
                continue;
            }
            if (!t.getCancellationReason().equals(currentReason)) {
                if (!currentReason.equals("-")) {
                    this.add(new JLabel(count + " treatments have been cancelled with the following reason: " + currentReason));
                } else {
                    this.add(new JLabel(count + " treatments have been finished."));
                }
                currentReason = t.getCancellationReason();
            } else {
                count++;
            }
        }

        if (!currentReason.equals("-")) {
            this.add(new JLabel(count + " treatments have been cancelled with the following reason: " + currentReason));
        } else {
            this.add(new JLabel(count + " treatments have been finished."));
        }
    }
}