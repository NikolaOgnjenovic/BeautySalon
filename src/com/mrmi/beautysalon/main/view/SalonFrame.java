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
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setIconImage(new ImageIcon("src/images/icon.png").getImage());
        this.setLocationRelativeTo(null);

        add(new JLabel("Salon properties"), "span");

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

        setVisible(true);
    }

    private void initialiseListeners() {
        textLoyaltyThreshold.addActionListener(e -> {
            if (!textLoyaltyThreshold.getText().matches("[0-9]+")) {
                JOptionPane.showMessageDialog(null, "The loyalty threshold has to be a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            float loyaltyThreshold = Float.parseFloat(textLoyaltyThreshold.getText());
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
            if (!textOpeningHour.getText().matches("([01]?[0-9])|2[0-3]")) {
                JOptionPane.showMessageDialog(null, "The opening hour must be a number between 0 and 24.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            byte openingHour = Byte.parseByte(textOpeningHour.getText());
            salonManager.setOpeningHour(openingHour);
        });

        textClosingHour.addActionListener(e -> {
            if (!textClosingHour.getText().matches("([01]?[0-9])|2[0-3]")) {
                JOptionPane.showMessageDialog(null, "The closing hour must be a number between 0 and 24.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            byte closingHour = Byte.parseByte(textClosingHour.getText());
            salonManager.setClosingHour(closingHour);
        });

        textBonus.addActionListener(e -> {
            if (!textBonus.getText().matches("[0-9]+")) {
                JOptionPane.showMessageDialog(null, "The bonus has to be a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            float bonus = Float.parseFloat(textBonus.getText());
            salonManager.setBonus(bonus);
        });

        buttonBack.addActionListener(e -> this.dispose());
    }
}

