package com.mrmi.beautysalon.main.gui;

import com.mrmi.beautysalon.main.objects.Database;
import com.mrmi.beautysalon.main.objects.Receptionist;
import com.mrmi.beautysalon.main.objects.Treatment;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class ReceptionistFrame extends JFrame {
    private String clientUsername = "";
    public ReceptionistFrame(Database database) {
        this.setTitle("Receptionist");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(800, 800);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(235, 235, 235));
        this.setLayout(new FlowLayout());

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> {
            this.dispose();
            MainFrame mainFrame = new MainFrame(database);
        });
        this.add(logout);

        Receptionist receptionist = (Receptionist) Database.currentUser;
        JButton viewTreatments = new JButton("View all treatments");
        viewTreatments.addActionListener(e -> {
            this.dispose();
            HashMap<Integer, Treatment> treatments = receptionist.getAllTreatments(database);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(database, treatments, false, false, false, this);
        });
        this.add(viewTreatments);

        JTextField clientUsernameField = new JTextField("Client username");
        if (!database.getUsers().containsKey(clientUsernameField.getText())) {
            registerUser();
        }
        JButton bookTreatment = new JButton("Book treatment");
        bookTreatment.addActionListener(e -> {
            this.dispose();
            BookTreatmentFrame bookFrame = new BookTreatmentFrame(database, clientUsername);
        });
        this.add(bookTreatment);

        // Cancel & update
        JButton editTreatment = new JButton("Edit a treatment");
        editTreatment.addActionListener(e -> {
            this.dispose();
            HashMap<Integer, Treatment> clientTreatments = receptionist.getAllTreatments(database);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(database, clientTreatments, true, true, false, this);
        });
        this.add(editTreatment);
    }

    // TODO
    private void registerUser() {
        clientUsername = "abc";
    }
}
