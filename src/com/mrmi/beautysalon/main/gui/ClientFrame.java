package com.mrmi.beautysalon.main.gui;

import com.mrmi.beautysalon.main.objects.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class ClientFrame extends JFrame {
    public ClientFrame(Database database) {
        this.setTitle("Client");
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

        Client client = (Client) Database.currentUser;
        String username = Database.currentUsername;
        JButton viewDueTreatments = new JButton("View due treatments");
        viewDueTreatments.addActionListener(e -> {
            this.dispose();
            HashMap<Integer, Treatment> dueTreatments = client.getDueTreatments(database, username);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(database, dueTreatments, false, false, false, this);
        });
        this.add(viewDueTreatments);

        JButton viewPastTreatments = new JButton("View past treatments");
        viewPastTreatments.addActionListener(e -> {
            this.dispose();
            HashMap<Integer, Treatment> pastTreatments = client.getPastTreatments(database, username);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(database, pastTreatments, false, false, false, this);
        });
        this.add(viewPastTreatments);

        JButton cancelTreatment = new JButton("Cancel a treatment");
        cancelTreatment.addActionListener(e -> {
            this.dispose();
            HashMap<Integer, Treatment> clientTreatments = client.getTreatments(database, username);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(database, clientTreatments, false, true, false, this);
        });
        this.add(cancelTreatment);

        JButton bookTreatment = new JButton("Book treatment");
        bookTreatment.addActionListener(e -> {
            this.dispose();
            BookTreatmentFrame bookFrame = new BookTreatmentFrame(database, username);
        });
        this.add(bookTreatment);

        String loyaltyMessage;
        if (client.hasLoyaltyCard()) {
            loyaltyMessage = "You have a loyalty card which grants you a 10% discount on all treatments!";
        } else {
            loyaltyMessage = "You need to spend " + (database.getLoyaltyThreshold() - client.getMoneySpent()) + " more money in order to get a loyalty card.";
        }
        JLabel loyaltyStatus = new JLabel(loyaltyMessage);
        this.add(loyaltyStatus);
    }
}
