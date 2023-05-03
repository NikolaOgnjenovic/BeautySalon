package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.controller.AuthController;
import com.mrmi.beautysalon.main.controller.SalonController;
import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.controller.UserController;
import com.mrmi.beautysalon.main.entity.BeautySalon;
import com.mrmi.beautysalon.main.entity.Database;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final BeautySalon beautySalon;

    private final TreatmentController treatmentController;
    private final UserController userController;
    private final AuthController authController;
    private JPanel mainPanel;
    private JButton login;
    private JButton register;

    public MainFrame() {
        Database database = new Database("");
        SalonController salonController = new SalonController("");
        beautySalon = salonController.getBeautySalon();
        treatmentController = new TreatmentController(database, salonController);
        userController = new UserController(database, treatmentController, salonController);
        authController = new AuthController(userController);

        initialiseViews();
        initialiseListeners();

        this.setTitle("Beauty salon");
        this.setSize(800, 800);
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
            LoginFrame loginFrame = new LoginFrame(treatmentController, userController, beautySalon, authController);
        });

        register.addActionListener(e -> {
            this.dispose();
            RegisterFrame registerFrame = new RegisterFrame();
        });
    }
}
