package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.manager.SalonManager;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import com.mrmi.beautysalon.main.entity.Treatment;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class ReceptionistFrame extends JFrame {
    private final TreatmentManager treatmentManager;
    private final UserManager userManager;
    private final SalonManager salonManager;
    private JButton logout;
    private JButton viewTreatments;
    private JTextField clientUsernameField;
    private JButton bookTreatment;
    private JButton editTreatment;

    public ReceptionistFrame(TreatmentManager treatmentManager, UserManager userManager, SalonManager salonManager) {
        this.treatmentManager = treatmentManager;
        this.userManager = userManager;
        this.salonManager = salonManager;

        initialiseViews();
        initialiseListeners();
    }
    private void initialiseViews() {
        this.setTitle("Receptionist");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(true);
        this.setSize(1000, 1080);
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
            HashMap<Integer, Treatment> treatments = treatmentManager.getTreatments();
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentManager, userManager, treatments, false, false, false, salonManager.getLoyaltyThreshold(), false);
        });

        bookTreatment.addActionListener(e -> {
            if (!userManager.getUsers().containsKey(clientUsernameField.getText())) {
                registerUser();
            } else {
                BookTreatmentFrame bookFrame = new BookTreatmentFrame(treatmentManager, userManager, salonManager, clientUsernameField.getText());
            }
        });

        editTreatment.addActionListener(e -> {
            HashMap<Integer, Treatment> clientTreatments = treatmentManager.getTreatments();
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentManager, userManager, clientTreatments, true, true, false, salonManager.getLoyaltyThreshold(), false);
        });
    }

    // TODO
    private void registerUser() {

    }
}
