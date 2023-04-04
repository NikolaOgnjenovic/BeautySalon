package com.mrmi.beautysalon.main.gui;

import com.mrmi.beautysalon.main.objects.Database;
import com.mrmi.beautysalon.main.objects.Manager;
import com.mrmi.beautysalon.main.objects.Treatment;
import com.mrmi.beautysalon.main.objects.User;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class ManagerFrame extends JFrame {
    public ManagerFrame(Database database) {
        this.setTitle("Manager");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(800, 800);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(235, 235, 235));
        this.setLayout(new FlowLayout());

        // Cancel & update
        JButton editTreatment = new JButton("Edit treatments");
        editTreatment.addActionListener(e -> {
            this.dispose();
            HashMap<Integer, Treatment> clientTreatments = database.getTreatments();
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(database, clientTreatments, true, true, true, this);
        });
        this.add(editTreatment);

        JButton editUsers = new JButton("Edit users");
        editUsers.addActionListener(e -> {
            this.dispose();
            HashMap<String, User> users = database.getUsers();
            UsersFrame usersFrame = new UsersFrame(database, users, true, true);
        });
        this.add(editUsers);

        JLabel loyaltyThresholdLabel = new JLabel("Loyalty threshold");
        this.add(loyaltyThresholdLabel);
        JTextField loyaltyThreshold = new JTextField(String.valueOf(database.getLoyaltyThreshold()));
        loyaltyThreshold.addActionListener(e -> {
            database.setLoyaltyThreshold(Double.parseDouble(loyaltyThreshold.getText()));
        });
        this.add(loyaltyThreshold);

        // TODO: graph by profit
        JButton editTreatmentTypes = new JButton("Edit treatment types");
        editTreatmentTypes.addActionListener(e -> {
            this.dispose();
            TreatmentTypesFrame treatmentTypesFrame = new TreatmentTypesFrame(database, database.getTreatmentTypes(), true, true);
        });
        this.add(editTreatmentTypes);

        // TODO: graph treatments by cancellation reason by date
        JButton editTreatments = new JButton("Edit all treatments");
        editTreatments.addActionListener(e -> {
            this.dispose();
            HashMap<Integer, Treatment> treatments = database.getTreatments();
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(database, treatments, true, true, true, this);
        });
        this.add(editTreatments);

        // TODO:
        //  salon profit & loss in a date interval
        //  registerEmployee();
        //  viewBeauticianStats();
    }
}
