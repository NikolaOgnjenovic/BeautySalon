package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import com.mrmi.beautysalon.main.entity.Treatment;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.HashMap;

public class BeauticianFrame extends JFrame {
    private final TreatmentManager treatmentManager;
    private final UserManager userManager;
    private final String beauticianUsername;
    private JButton buttonLogout;
    private JButton buttonDueTreatments;
    private JButton buttonPastTreatments;

    public BeauticianFrame(TreatmentManager treatmentManager, UserManager userManager, String beauticianUsername) {
        this.treatmentManager = treatmentManager;
        this.userManager = userManager;
        this.beauticianUsername = beauticianUsername;

        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap", "[center, grow]", "[center, grow]"));
        this.setTitle("Beauty salon - Beautician");
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon("src/images/icon.png").getImage());
        this.setLocationRelativeTo(null);

        buttonDueTreatments = new JButton("Due treatments");
        this.add(buttonDueTreatments);

        buttonPastTreatments = new JButton("Past treatments");
        this.add(buttonPastTreatments);

        buttonLogout = new JButton("Logout");
        this.add(buttonLogout);
    }

    private void initialiseListeners() {
        buttonDueTreatments.addActionListener(e -> {
            HashMap<Integer, Treatment> dueTreatments = userManager.getBeauticianDueTreatments(beauticianUsername);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentManager, userManager, dueTreatments, false, false);
            treatmentsFrame.setVisible(true);
        });

        buttonPastTreatments.addActionListener(e -> {
            HashMap<Integer, Treatment> pastTreatments = userManager.getBeauticianPastTreatments(beauticianUsername);
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentManager, userManager, pastTreatments, false, false);
            treatmentsFrame.setVisible(true);
        });

        buttonLogout.addActionListener(e -> {
            this.dispose();
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
