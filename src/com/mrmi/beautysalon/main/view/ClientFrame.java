package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.manager.AuthManager;
import com.mrmi.beautysalon.main.manager.SalonManager;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import com.mrmi.beautysalon.main.entity.Client;
import com.mrmi.beautysalon.main.entity.Treatment;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.HashMap;

public class ClientFrame extends JFrame {
    private final TreatmentManager treatmentManager;
    private final UserManager userManager;
    private final SalonManager salonManager;
    private final AuthManager authManager;
    private final String clientUsername;
    private final Client client;
    private JButton logout;
    private JButton viewDueTreatments;
    private JButton viewPastTreatments;
    private JButton cancelTreatment;
    private JButton bookTreatment;

    public ClientFrame(Client client, String clientUsername, TreatmentManager treatmentManager, UserManager userManager, SalonManager salonManager, AuthManager authManager) {
        this.client = client;
        this.clientUsername = clientUsername;
        this.treatmentManager = treatmentManager;
        this.userManager = userManager;
        this.salonManager = salonManager;
        this.authManager = authManager;
        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap", "[center, grow]", "[center, grow]40"));
        this.setTitle("Beauty salon - Client");
        this.setSize(1000, 1080);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        JLabel title = new JLabel("Welcome back, " + clientUsername);
        Utility.setFont(title, 30);
        this.add(title);

        String loyaltyMessage = "You have a loyalty card which grants you a 10% discount on all treatments!";
        if (!client.hasLoyaltyCard()) {
            loyaltyMessage = "You need to spend " + (salonManager.getLoyaltyThreshold() - client.getMoneySpent()) + " more money in order to get a loyalty card.";
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
            authManager.logout();
            this.dispose();
            MainFrame mainFrame = new MainFrame();
        });

        viewDueTreatments.addActionListener(e -> {
            HashMap<Integer, Treatment> dueTreatments = userManager.getClientDueTreatments(clientUsername);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentManager, userManager, dueTreatments, false, false, false, salonManager.getLoyaltyThreshold(), true);
        });

        viewPastTreatments.addActionListener(e -> {
            HashMap<Integer, Treatment> pastTreatments = userManager.getClientPastTreatments(clientUsername);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentManager, userManager, pastTreatments, false, false, false, salonManager.getLoyaltyThreshold(), true);
        });

        cancelTreatment.addActionListener(e -> {
            HashMap<Integer, Treatment> clientTreatments = userManager.getClientTreatments(clientUsername);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentManager, userManager, clientTreatments, false, true, false, salonManager.getLoyaltyThreshold(), true);
        });

        bookTreatment.addActionListener(e -> {
            BookTreatmentFrame bookFrame = new BookTreatmentFrame(treatmentManager, userManager, salonManager, clientUsername);
        });
    }
}