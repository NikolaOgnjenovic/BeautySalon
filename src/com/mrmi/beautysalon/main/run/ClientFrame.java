package com.mrmi.beautysalon.main.run;

import com.mrmi.beautysalon.main.objects.Client;
import com.mrmi.beautysalon.main.objects.Database;
import com.mrmi.beautysalon.main.objects.Treatment;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

public class ClientFrame extends JFrame {
    public ClientFrame(Database database, Client client, String username) {
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

        JButton viewDueTreatments = new JButton("View due treatments");
        viewDueTreatments.addActionListener(e -> {
            this.dispose();
            HashMap<Integer, Treatment> dueTreatments = client.getDueTreatments(database, username);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(database, "Due treatments", dueTreatments, false, false);
        });
        this.add(viewDueTreatments);

        JButton viewPastTreatments = new JButton("View past treatments");
        viewPastTreatments.addActionListener(e -> {
            this.dispose();
            HashMap<Integer, Treatment> pastTreatments = client.getPastTreatments(database, username);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(database, "Past treatments", pastTreatments, false, false);
        });
        this.add(viewPastTreatments);

        JButton cancelTreatment = new JButton("Cancel a treatment");
        cancelTreatment.addActionListener(e -> {
            this.dispose();
            HashMap<Integer, Treatment> clientTreatments = client.getTreatments(database, username);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(database, "Client's treatments", clientTreatments, false, true);
        });
        this.add(cancelTreatment);

        JButton bookTreatment = new JButton("Book treatment");
        bookTreatment.addActionListener(e -> {
            this.dispose();
            /*
            Treatment treatment = inputTreatment(clientUsername);
        if (treatment != null) {
            try {
                receptionist.bookTreatment(treatment, database.getClientByUsername(clientUsername), database);
            } catch (UserNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        private Treatment inputTreatment(String clientUsername) {
        System.out.println("Pick a treatment type");
        byte treatmentTypeId;
        System.out.println("Available treatment types: ");
        for (TreatmentType t : database.getTreatmentTypes().values()) {
            System.out.println(t);
        }
        System.out.println("Enter the new treatment type id");
        treatmentTypeId = Byte.parseByte(scanner.nextLine());

        TreatmentType treatmentType;
        try {
            treatmentType = database.getTreatmentTypeById(treatmentTypeId);
        } catch (TreatmentTypeNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        }

        HashMap<String, Beautician> beauticians = database.getBeauticiansByTreatmentType(treatmentTypeId);
        if (beauticians.size() < 1) {
            System.out.println("No beauticians available");
            return null;
        }
        System.out.println("Available beauticians:");
        for (Beautician b : beauticians.values()) {
            System.out.println(b);
        }

        System.out.println("Pick a beautician by entering one's username or get a random one by inserting enter");
        String beauticianUsername = scanner.nextLine();
        if (beauticianUsername.length() < 1) {
            for (Map.Entry<String, Beautician> b : beauticians.entrySet()) {
                if (b.getValue().getTreatmentTypeIDs().contains(treatmentTypeId)) {
                    beauticianUsername = b.getKey();
                    break;
                }
            }
        }

        TODO:
        Zatim korisnik bira termin – datum i vreme (od dostupnih termina kada je dostupan
        odabrani kozmetičar, u toku radnog vremena kozmetičkog salona).
        Zbog pojednostavljivanja, smatrati da tretmani počinju uvek na pun sat.

            System.out.println("Enter date in dd.MM.yyyy format");
            Date scheduledDate;
            try {
                scheduledDate = sdf.parse(scanner.nextLine());
            } catch (ParseException e) {
                System.out.println("Invalid date");
                return null;
            }

            return new Treatment(scheduledDate, false, clientUsername, beauticianUsername, treatmentTypeId, treatmentType.getPrice());
        }
             */
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
