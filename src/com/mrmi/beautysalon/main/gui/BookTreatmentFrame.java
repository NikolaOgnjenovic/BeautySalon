package com.mrmi.beautysalon.main.gui;

import com.mrmi.beautysalon.main.objects.Beautician;
import com.mrmi.beautysalon.main.objects.Database;
import com.mrmi.beautysalon.main.objects.Treatment;
import com.mrmi.beautysalon.main.objects.TreatmentType;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BookTreatmentFrame extends JFrame {
    private final Database database;
    private byte treatmentTypeId;
    private double treatmentPrice;
    private String beauticianUsername;
    public BookTreatmentFrame(Database database, String clientUsername) {
        this.database = database;
        this.setTitle("Book treatment");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(800, 800);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(235, 235, 235));
        this.setLayout(new FlowLayout());

        // TODO: clean up this mess
        ButtonGroup treatmentTypeGroup = new ButtonGroup();
        for (Map.Entry<Integer, TreatmentType> t : database.getTreatmentTypes().entrySet()) {
            String labelText = t.getValue().getDisplayString(t.getKey());
            this.add(new JLabel(labelText));
            JRadioButton newButton = new JRadioButton(t.getValue().getName());
            newButton.addActionListener(e -> {
                        treatmentTypeId = Byte.parseByte(labelText.substring(4, labelText.indexOf(",")));
                        treatmentPrice = Double.parseDouble(labelText.substring(labelText.lastIndexOf(", price: " ) + 9, labelText.indexOf(", duration")));
                        displayAvailableBeauticians(treatmentTypeId);
                    }
            );
            treatmentTypeGroup.add(newButton);
            this.add(newButton);
        }

        Date selectedDate = new Date();
        /*
        TODO: date & time picker
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
        JButton bookButton = new JButton("Book");
        bookButton.addActionListener(e -> database.bookTreatment(new Treatment(selectedDate, false, clientUsername, beauticianUsername, treatmentTypeId, treatmentPrice), database.getNextTreatmentId()));
        this.add(bookButton);
    }

    private void displayAvailableBeauticians(byte treatmentTypeId) {
        HashMap<String, Beautician> beauticians = database.getBeauticiansByTreatmentType(treatmentTypeId);
        //HashMap<String, Beautician> beauticians = database.getBeauticians(treatmentTypeId, Date selecteddDate);
        if (beauticians.size() < 1) {
            return;
        }
        ButtonGroup beauticianButtonGroup = new ButtonGroup();
        for (Map.Entry<String, Beautician> b : beauticians.entrySet()) {
            System.out.println("ADDING");
            JRadioButton newButton = new JRadioButton(b.toString());
            newButton.addActionListener(e -> beauticianUsername = b.getKey());
            beauticianButtonGroup.add(newButton);
            this.getContentPane().add(newButton);
        }
    }
}
