package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.manager.AuthManager;
import com.mrmi.beautysalon.main.manager.SalonManager;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import com.mrmi.beautysalon.main.entity.Database;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class MainFrame extends JFrame {
    private final SalonManager salonManager;
    private final TreatmentManager treatmentManager;
    private final UserManager userManager;
    private final AuthManager authManager;
    private JButton buttonLogin;
    private JButton buttonRegister;

    public MainFrame() {
        Database database = new Database("");
        salonManager = new SalonManager(database);
        treatmentManager = new TreatmentManager(database, salonManager);
        userManager = new UserManager(database);
        authManager = new AuthManager(userManager);
        treatmentManager.finishTreatments();

        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setTitle("Beauty salon");
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new MigLayout("wrap 1", "[grow, center]", "[grow]"));
        this.setIconImage(new ImageIcon("src/images/icon.png").getImage());
        this.setLocationRelativeTo(null);

        JLabel welcome = new JLabel("Welcome");
        this.add(welcome);

        buttonLogin = new JButton("Login");
        this.add(buttonLogin);

        buttonRegister = new JButton("Register");
        this.add(buttonRegister);

        setVisible(true);
    }

    private void initialiseListeners() {
        buttonLogin.addActionListener(e -> {
            LoginFrame loginFrame = new LoginFrame(salonManager, treatmentManager, userManager, authManager);
            this.dispose();
        });

        buttonRegister.addActionListener(e -> {
            RegisterFrame registerFrame = new RegisterFrame(salonManager, treatmentManager, userManager, authManager, false, null, true);
            this.dispose();
        });
    }
}
