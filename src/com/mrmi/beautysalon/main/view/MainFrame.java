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
    private JPanel mainPanel;
    private JButton login;
    private JButton register;

    public MainFrame() {
        Database database = new Database("");
        salonManager = new SalonManager("");
        treatmentManager = new TreatmentManager(database, salonManager);
        userManager = new UserManager(database, treatmentManager, salonManager);
        authManager = new AuthManager(userManager);

        initialiseViews();
        initialiseListeners();

        this.setTitle("Beauty salon");
        this.setSize(1000, 1080);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setVisible(true);
    }

    private void initialiseViews() {
        JLabel welcome = new JLabel("Welcome");
        Utility.setFont(welcome, 30);

        login = new JButton("Login");
        Utility.setFont(login, 24);

        register = new JButton("Register");
        Utility.setFont(register, 24);

        mainPanel = new JPanel();
        // Wrap 1 -> start a new row after 1 component
        // grow the column by x and y and center the content in the column
        // grow the rows and set a gap between them
        mainPanel.setLayout(new MigLayout("wrap 1", "[grow, center]", "[grow]40[grow]40[grow]"));
        mainPanel.add(welcome);
        mainPanel.add(login);
        mainPanel.add(register);

        mainPanel.setVisible(true);
    }

    private void initialiseListeners() {
        login.addActionListener(e -> {
            this.dispose();
            LoginFrame loginFrame = new LoginFrame(treatmentManager, userManager, salonManager, authManager);
        });

        register.addActionListener(e -> {
            this.dispose();
            RegisterFrame registerFrame = new RegisterFrame(treatmentManager, userManager, salonManager, authManager, false);
        });
    }
}
