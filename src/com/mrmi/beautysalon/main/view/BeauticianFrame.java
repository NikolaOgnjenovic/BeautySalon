package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.manager.SalonManager;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import com.mrmi.beautysalon.main.entity.Treatment;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class BeauticianFrame extends JFrame {
    private final TreatmentManager treatmentManager;
    private final UserManager userManager;
    private final SalonManager salonManager;
    private final String username;
    private JButton logout;
    private JButton viewDueTreatments;
    private JButton viewPastTreatments;
    private JButton viewSchedule;

    public BeauticianFrame(String beauticianUsername, TreatmentManager treatmentManager, UserManager userManager, SalonManager salonManager) {
        username = beauticianUsername;
        this.treatmentManager = treatmentManager;
        this.userManager = userManager;
        this.salonManager = salonManager;
        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setTitle("Beautician");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(true);
        this.setSize(1000, 1080);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(235, 235, 235));
        this.setLayout(new FlowLayout());

        logout = new JButton("Logout");
        this.add(logout);

        viewDueTreatments = new JButton("View due treatments");
        this.add(viewDueTreatments);

        viewPastTreatments = new JButton("View past treatments");
        this.add(viewPastTreatments);

        viewSchedule = new JButton("View schedule");
    }

    private void initialiseListeners() {
        logout.addActionListener(e -> {
            this.dispose();
            MainFrame mainFrame = new MainFrame();
        });

        viewDueTreatments.addActionListener(e -> {
            HashMap<Integer, Treatment> dueTreatments = userManager.getBeauticianDueTreatments(username);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentManager, userManager, dueTreatments, false, false, false, salonManager.getLoyaltyThreshold(), false);
        });

        viewPastTreatments.addActionListener(e -> {
            HashMap<Integer, Treatment> pastTreatments = userManager.getBeauticianPastTreatments(username);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentManager, userManager, pastTreatments, false, false, false, salonManager.getLoyaltyThreshold(), false);
        });

        viewSchedule.addActionListener(e -> {
            // TODO
        });
    }
}
