package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.controller.UserController;
import com.mrmi.beautysalon.main.entity.BeautySalon;
import com.mrmi.beautysalon.main.entity.Client;
import com.mrmi.beautysalon.main.entity.Database;
import com.mrmi.beautysalon.main.entity.Treatment;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class ClientFrame extends JFrame {
    private final TreatmentController treatmentController;
    private final UserController userController;
    private final BeautySalon beautySalon;
    private final String clientUsername;
    private final Client client;
    private JButton logout;
    private JButton viewDueTreatments;
    private JButton viewPastTreatments;
    private JButton cancelTreatment;
    private JButton bookTreatment;

    public ClientFrame(Client client, String clientUsername, TreatmentController treatmentController, UserController userController, BeautySalon beautySalon) {
        this.client = client;
        this.clientUsername = clientUsername;
        this.treatmentController = treatmentController;
        this.userController = userController;
        this.beautySalon = beautySalon;

        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setTitle("Client");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(800, 800);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(235, 235, 235));
        this.setLayout(new FlowLayout());

        logout = new JButton("Logout");
        this.add(logout);

        viewDueTreatments = new JButton("View due treatments");
        this.add(viewDueTreatments);

        viewPastTreatments = new JButton("View past treatments");
        this.add(viewPastTreatments);

        cancelTreatment = new JButton("Cancel a treatment");
        this.add(cancelTreatment);

        bookTreatment = new JButton("Book treatment");
        this.add(bookTreatment);

        String loyaltyMessage = "You have a loyalty card which grants you a 10% discount on all treatments!";
        if (!client.hasLoyaltyCard()) {
            loyaltyMessage = "You need to spend " + (beautySalon.getLoyaltyThreshold() - client.getMoneySpent()) + " more money in order to get a loyalty card.";
        }

        JLabel loyaltyStatus = new JLabel(loyaltyMessage);
        this.add(loyaltyStatus);
    }

    private void initialiseListeners() {
        logout.addActionListener(e -> {
            this.dispose();
            MainFrame mainFrame = new MainFrame();
        });

        viewDueTreatments.addActionListener(e -> {
            this.dispose();
            HashMap<Integer, Treatment> dueTreatments = userController.getClientDueTreatments(clientUsername);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentController, dueTreatments, false, false, false, this);
        });

        viewPastTreatments.addActionListener(e -> {
            this.dispose();
            HashMap<Integer, Treatment> pastTreatments = userController.getClientPastTreatments(clientUsername);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentController, pastTreatments, false, false, false, this);
        });

        cancelTreatment.addActionListener(e -> {
            this.dispose();
            HashMap<Integer, Treatment> clientTreatments = userController.getClientTreatments(clientUsername);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentController, clientTreatments, false, true, false, this);
        });

        bookTreatment.addActionListener(e -> {
            this.dispose();
            BookTreatmentFrame bookFrame = new BookTreatmentFrame(treatmentController, userController, beautySalon, clientUsername);
        });
    }
}
