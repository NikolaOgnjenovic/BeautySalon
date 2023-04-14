package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.controller.UserController;
import com.mrmi.beautysalon.main.entity.Beautician;
import com.mrmi.beautysalon.main.entity.BeautySalon;
import com.mrmi.beautysalon.main.entity.Database;
import com.mrmi.beautysalon.main.entity.Treatment;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class BeauticianFrame extends JFrame {
    private final TreatmentController treatmentController;
    private final UserController userController;
    private final String username;
    private JButton logout;
    private JButton viewDueTreatments;
    private JButton viewPastTreatments;
    private JButton viewSchedule;

    public BeauticianFrame(String beauticianUsername, TreatmentController treatmentController, UserController userController) {
        username = beauticianUsername;
        this.treatmentController = treatmentController;
        this.userController = userController;
        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setTitle("Beautician");
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

        viewSchedule = new JButton("View schedule");
    }

    private void initialiseListeners() {
        logout.addActionListener(e -> {
            this.dispose();
            MainFrame mainFrame = new MainFrame();
        });

        viewDueTreatments.addActionListener(e -> {
            this.dispose();
            HashMap<Integer, Treatment> dueTreatments = userController.getBeauticianDueTreatments(username);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentController, dueTreatments, false, false, false, this);
        });

        viewPastTreatments.addActionListener(e -> {
            this.dispose();
            HashMap<Integer, Treatment> pastTreatments = userController.getBeauticianPastTreatments(username);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentController, pastTreatments, false, false, false, this);
        });

        viewSchedule.addActionListener(e -> {
            // TODO
        });
    }
}
