package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.controller.UserController;
import com.mrmi.beautysalon.main.entity.BeautySalon;
import com.mrmi.beautysalon.main.entity.Treatment;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class ReceptionistFrame extends JFrame {
    private final TreatmentController treatmentController;
    private final UserController userController;
    private final BeautySalon beautySalon;
    private JButton logout;
    private JButton viewTreatments;
    private JTextField clientUsernameField;
    private JButton bookTreatment;
    private JButton editTreatment;

    public ReceptionistFrame(TreatmentController treatmentController, UserController userController, BeautySalon beautySalon) {
        this.treatmentController = treatmentController;
        this.userController = userController;
        this.beautySalon = beautySalon;

        initialiseViews();
        initialiseListeners();
    }
    private void initialiseViews() {
        this.setTitle("Receptionist");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(true);
        this.setSize(800, 800);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(235, 235, 235));
        this.setLayout(new FlowLayout());

        logout = new JButton("Logout");
        this.add(logout);

        viewTreatments = new JButton("View all treatments");
        this.add(viewTreatments);

        clientUsernameField = new JTextField("Client username");

        bookTreatment = new JButton("Book treatment");
        this.add(bookTreatment);

        editTreatment = new JButton("Edit a treatment");
        this.add(editTreatment);
    }

    private void initialiseListeners() {
        logout.addActionListener(e -> {
            this.dispose();
            MainFrame mainFrame = new MainFrame();
        });

        viewTreatments.addActionListener(e -> {
            HashMap<Integer, Treatment> treatments = treatmentController.getTreatments();
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentController, userController, treatments, false, false, false, this, beautySalon.getLoyaltyThreshold(), false);
        });

        bookTreatment.addActionListener(e -> {
            if (!userController.getUsers().containsKey(clientUsernameField.getText())) {
                registerUser();
            } else {
                BookTreatmentFrame bookFrame = new BookTreatmentFrame(treatmentController, userController, beautySalon, clientUsernameField.getText());
            }
        });

        editTreatment.addActionListener(e -> {
            HashMap<Integer, Treatment> clientTreatments = treatmentController.getTreatments();
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentController, userController, clientTreatments, true, true, false, this, beautySalon.getLoyaltyThreshold(), false);
        });
    }

    // TODO
    private void registerUser() {

    }
}
