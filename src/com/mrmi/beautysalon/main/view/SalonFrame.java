package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.manager.SalonManager;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class SalonFrame extends JFrame {
    private final SalonManager salonManager;
    private JTextField textLoyaltyThreshold;
    private JTextField textSalonName;
    private JTextField textOpeningHour;
    private JTextField textClosingHour;
    private JTextField textBonus;
    private JButton buttonBack;

    public SalonFrame(SalonManager salonManager) {
        this.salonManager = salonManager;

        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap 2", "[center, grow]", "[center, grow]"));
        this.setTitle("Beauty salon - salon");
        this.setSize(1000, 1080);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setIconImage(new ImageIcon("src/images/icon.png").getImage());

        this.add(new JLabel("Loyalty threshold"), "align right");
        textLoyaltyThreshold = new JTextField(String.valueOf(salonManager.getLoyaltyThreshold()), 7);
        this.add(textLoyaltyThreshold, "align left");

        this.add(new JLabel("Salon name"), "align right");
        textSalonName = new JTextField(salonManager.getName(), 10);
        this.add(textSalonName, "align left");

        this.add(new JLabel("Opening hour"), "align right");
        textOpeningHour = new JTextField(String.valueOf(salonManager.getOpeningHour()), 2);
        this.add(textOpeningHour, "align left");

        this.add(new JLabel("Closing hour"), "align right");
        textClosingHour = new JTextField(String.valueOf(salonManager.getClosingHour()), 2);
        this.add(textClosingHour, "align left");

        this.add(new JLabel("Salary bonus"), "align right");
        textBonus = new JTextField(String.valueOf(salonManager.getBonus()), 6);
        this.add(textBonus, "align left");

        buttonBack = new JButton("Back");
        this.add(buttonBack, "span");
    }

    private void initialiseListeners() {
        textLoyaltyThreshold.addActionListener(e -> {
            float loyaltyThreshold = salonManager.getLoyaltyThreshold();
            try {
                loyaltyThreshold = Float.parseFloat(textLoyaltyThreshold.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "The loyalty threshold has to be a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (loyaltyThreshold < 0) {
                JOptionPane.showMessageDialog(null, "The loyalty threshold has to be a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            salonManager.setLoyaltyThreshold(loyaltyThreshold);
        });

        textSalonName.addActionListener(e -> {
            String name = textSalonName.getText();
            if (name.length() == 0) {
                JOptionPane.showMessageDialog(null, "The salon name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            salonManager.setName(name);
        });

        textOpeningHour.addActionListener(e -> {
            byte openingHour = salonManager.getOpeningHour();
            try {
                openingHour = Byte.parseByte(textOpeningHour.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "The opening hour must be a number between 0 and 24.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (openingHour < 0 || openingHour > 24) {
                JOptionPane.showMessageDialog(null, "The opening hour must be a number between 0 and 24.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            salonManager.setOpeningHour(openingHour);
        });

        textClosingHour.addActionListener(e -> {
            byte closingHour = salonManager.getClosingHour();
            try {
                closingHour = Byte.parseByte(textClosingHour.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "The closing hour must be a number between 0 and 24.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (closingHour < 0 || closingHour > 24) {
                JOptionPane.showMessageDialog(null, "The closing hour must be a number between 0 and 24.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            salonManager.setClosingHour(closingHour);
        });

        textBonus.addActionListener(e -> {
            float bonus = salonManager.getBonus();
            try {
                bonus = Float.parseFloat(textBonus.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "The bonus has to be a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (bonus < 0) {
                JOptionPane.showMessageDialog(null, "The bonus has to be a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            salonManager.setBonus(bonus);
        });

        buttonBack.addActionListener(e -> this.dispose());
    }
}

