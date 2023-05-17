package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.entity.Client;
import com.mrmi.beautysalon.main.manager.AuthManager;
import com.mrmi.beautysalon.main.manager.SalonManager;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import com.mrmi.beautysalon.main.entity.Treatment;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ReceptionistFrame extends JFrame {
    private final SalonManager salonManager;
    private final TreatmentManager treatmentManager;
    private final UserManager userManager;
    private final AuthManager authManager;

    private JButton buttonLogout;
    private JButton buttonViewTreatments;
    private JComboBox<Client> comboBoxClient;
    private JButton buttonBookTreatment;
    private JButton buttonRegister;

    public ReceptionistFrame(SalonManager salonManager, TreatmentManager treatmentManager, UserManager userManager, AuthManager authManager) {
        this.salonManager = salonManager;
        this.treatmentManager = treatmentManager;
        this.userManager = userManager;
        this.authManager = authManager;

        initialiseViews();
        initialiseListeners();
    }
    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap", "[center, grow]", "[center, grow]"));
        this.setTitle("Beauty salon - Receptionist");
        this.setSize(1000, 1080);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon("src/images/icon.png").getImage());

        buttonViewTreatments = new JButton("All treatments");
        this.add(buttonViewTreatments);

        buttonBookTreatment = new JButton("Book treatment");
        this.add(buttonBookTreatment);

        comboBoxClient = new JComboBox<>();
        refreshClients();
        this.add(comboBoxClient);

        buttonRegister = new JButton("Register client");
        this.add(buttonRegister);

        buttonLogout = new JButton("Logout");
        this.add(buttonLogout);
    }

    private void initialiseListeners() {
        buttonViewTreatments.addActionListener(e -> {
            HashMap<Integer, Treatment> treatments = treatmentManager.getTreatments();
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentManager, userManager, treatments, true, false);
            treatmentsFrame.setVisible(true);
        });

        buttonBookTreatment.addActionListener(e -> {
            Client client = (Client) comboBoxClient.getSelectedItem();
            if (client != null) {
                BookTreatmentFrame bookFrame = new BookTreatmentFrame(salonManager, treatmentManager, userManager, client.getUsername());
                bookFrame.setVisible(true);
            }
        });

        buttonRegister.addActionListener(e -> {
            RegisterFrame registerFrame = new RegisterFrame(salonManager, treatmentManager, userManager, authManager, false, null, false);
            registerFrame.setVisible(true);
        });

        buttonLogout.addActionListener(e -> {
            this.dispose();
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }

    public void refreshClients() {
        ArrayList<Client> clients = userManager.getClients();
        comboBoxClient.removeAllItems();
        for (Client client : clients) {
            comboBoxClient.addItem(client);
        }
    }
}