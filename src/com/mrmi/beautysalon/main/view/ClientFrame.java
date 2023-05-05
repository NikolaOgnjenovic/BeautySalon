package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.controller.AuthController;
import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.controller.UserController;
import com.mrmi.beautysalon.main.entity.BeautySalon;
import com.mrmi.beautysalon.main.entity.Client;
import com.mrmi.beautysalon.main.entity.Treatment;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.HashMap;

public class ClientFrame extends JFrame {
    private final TreatmentController treatmentController;
    private final UserController userController;
    private final BeautySalon beautySalon;
    private final AuthController authController;
    private final String clientUsername;
    private final Client client;
    private JButton logout;
    private JButton viewDueTreatments;
    private JButton viewPastTreatments;
    private JButton cancelTreatment;
    private JButton bookTreatment;

    public ClientFrame(Client client, String clientUsername, TreatmentController treatmentController, UserController userController, BeautySalon beautySalon, AuthController authController) {
        this.client = client;
        this.clientUsername = clientUsername;
        this.treatmentController = treatmentController;
        this.userController = userController;
        this.beautySalon = beautySalon;
        this.authController = authController;
        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap", "[center, grow]", "[center, grow]40"));
        this.setTitle("Beauty salon - Client");
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        JLabel title = new JLabel("Welcome back, " + clientUsername);
        Utility.setFont(title, 30);
        this.add(title);

        String loyaltyMessage = "You have a loyalty card which grants you a 10% discount on all treatments!";
        if (!client.hasLoyaltyCard()) {
            loyaltyMessage = "You need to spend " + (beautySalon.getLoyaltyThreshold() - client.getMoneySpent()) + " more money in order to get a loyalty card.";
        }
        JLabel loyaltyStatus = new JLabel(loyaltyMessage);
        Utility.setFont(loyaltyStatus, 24);
        this.add(loyaltyStatus);

        viewDueTreatments = new JButton("View due treatments");
        Utility.setFont(viewDueTreatments, 24);
        this.add(viewDueTreatments);

        viewPastTreatments = new JButton("View past treatments");
        Utility.setFont(viewPastTreatments, 24);
        this.add(viewPastTreatments);

        cancelTreatment = new JButton("Cancel a treatment");
        Utility.setFont(cancelTreatment, 24);
        this.add(cancelTreatment);

        bookTreatment = new JButton("Book treatment");
        Utility.setFont(bookTreatment, 24);
        this.add(bookTreatment);

        logout = new JButton("Logout");
        Utility.setFont(logout, 24);
        this.add(logout);
    }

    private void initialiseListeners() {
        logout.addActionListener(e -> {
            authController.logout();
            this.dispose();
            MainFrame mainFrame = new MainFrame();
        });

        viewDueTreatments.addActionListener(e -> {
            HashMap<Integer, Treatment> dueTreatments = userController.getClientDueTreatments(clientUsername);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentController, userController, dueTreatments, false, false, false,  beautySalon.getLoyaltyThreshold(), true);
        });

        viewPastTreatments.addActionListener(e -> {
            HashMap<Integer, Treatment> pastTreatments = userController.getClientPastTreatments(clientUsername);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentController, userController, pastTreatments, false, false, false, beautySalon.getLoyaltyThreshold(), true);
        });

        cancelTreatment.addActionListener(e -> {
            HashMap<Integer, Treatment> clientTreatments = userController.getClientTreatments(clientUsername);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentController, userController, clientTreatments, false, true, false, beautySalon.getLoyaltyThreshold(), true);
        });

        bookTreatment.addActionListener(e -> {
            BookTreatmentFrame bookFrame = new BookTreatmentFrame(treatmentController, userController, beautySalon, clientUsername);
        });
    }
}