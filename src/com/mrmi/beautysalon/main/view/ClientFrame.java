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
    private final SalonManager salonManager;
    private final TreatmentManager treatmentManager;
    private final UserManager userManager;
    private final AuthManager authManager;
    private final String clientUsername;
    private final Client client;
    private JButton buttonLogout;
    private JButton buttonDueTreatments;
    private JButton buttonPastTreatments;
    private JButton buttonBookTreatment;

    public ClientFrame(SalonManager salonManager, TreatmentManager treatmentManager, UserManager userManager, AuthManager authManager, String clientUsername, Client client) {
        this.salonManager = salonManager;
        this.treatmentManager = treatmentManager;
        this.userManager = userManager;
        this.authManager = authManager;
        this.clientUsername = clientUsername;
        this.client = client;

        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap", "[center, grow]", "[center, grow]"));
        this.setTitle("Beauty salon - Client");
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon("src/images/icon.png").getImage());
        this.setLocationRelativeTo(null);

        JLabel title = new JLabel("Welcome back, " + clientUsername);
        this.add(title);

        String loyaltyMessage = "You have a loyalty card which grants you a 10% discount on all treatments!";
        if (!client.hasLoyaltyCard()) {
            loyaltyMessage = "You need to spend " + (salonManager.getLoyaltyThreshold() - client.getMoneySpent()) + " more money in order to get a loyalty card.";
        }
        JLabel loyaltyStatus = new JLabel(loyaltyMessage);
        this.add(loyaltyStatus);

        buttonDueTreatments = new JButton("Due treatments");
        this.add(buttonDueTreatments);

        buttonPastTreatments = new JButton("Past treatments");
        this.add(buttonPastTreatments);

        buttonBookTreatment = new JButton("Book a treatment");
        this.add(buttonBookTreatment);

        buttonLogout = new JButton("Logout");
        
        this.add(buttonLogout);
    }

    private void initialiseListeners() {
        buttonLogout.addActionListener(e -> {
            authManager.logout();
            this.dispose();
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });

        buttonDueTreatments.addActionListener(e -> {
            HashMap<Integer, Treatment> dueTreatments = userManager.getClientDueTreatments(clientUsername);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentManager, userManager, dueTreatments, false, true);
            treatmentsFrame.setVisible(true);
        });

        buttonPastTreatments.addActionListener(e -> {
            HashMap<Integer, Treatment> pastTreatments = userManager.getClientPastTreatments(clientUsername);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentManager, userManager, pastTreatments, false, true);
            treatmentsFrame.setVisible(true);
        });

        buttonBookTreatment.addActionListener(e -> {
            BookTreatmentFrame bookFrame = new BookTreatmentFrame(salonManager, treatmentManager, userManager, clientUsername);
            bookFrame.setVisible(true);
        });
    }
}