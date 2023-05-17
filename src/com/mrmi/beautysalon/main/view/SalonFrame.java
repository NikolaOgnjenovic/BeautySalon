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
        textLoyaltyThreshold.addActionListener(e -> salonManager.setLoyaltyThreshold(Float.parseFloat(textLoyaltyThreshold.getText())));
        textSalonName.addActionListener(e -> salonManager.setName(textSalonName.getText()));
        textOpeningHour.addActionListener(e -> salonManager.setOpeningHour(Byte.parseByte(textOpeningHour.getText())));
        textClosingHour.addActionListener(e -> salonManager.setClosingHour(Byte.parseByte(textClosingHour.getText())));
        textBonus.addActionListener(e -> salonManager.setBonus(Float.parseFloat(textBonus.getText().toString())));
        buttonBack.addActionListener(e -> this.dispose());
    }
}

