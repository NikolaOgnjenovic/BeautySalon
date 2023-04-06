package com.mrmi.beautysalon.main.gui;

import com.mrmi.beautysalon.main.objects.Beautician;
import com.mrmi.beautysalon.main.objects.Database;
import com.mrmi.beautysalon.main.objects.Treatment;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class BeauticianFrame extends JFrame {
    public BeauticianFrame(Database database) {
        this.setTitle("Beautician");
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

        Beautician beautician = (Beautician) Database.currentUser;
        String username = Database.currentUsername;
        JButton viewDueTreatments = new JButton("View due treatments");
        viewDueTreatments.addActionListener(e -> {
            this.dispose();
            HashMap<Integer, Treatment> dueTreatments = beautician.getDueTreatments(database, username);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(database, dueTreatments, false, false, false, this);
        });
        this.add(viewDueTreatments);

        JButton viewPastTreatments = new JButton("View past treatments");
        viewPastTreatments.addActionListener(e -> {
            this.dispose();
            HashMap<Integer, Treatment> pastTreatments = beautician.getPastTreatments(database, username);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(database, pastTreatments, false, false, false, this);
        });
        this.add(viewPastTreatments);

        JButton viewSchedule = new JButton("View schedule");
        JLabel schedule = new JLabel("Schedule");
        viewSchedule.addActionListener(e -> schedule.setText(beautician.getSchedule(database, username)));
    }
}
