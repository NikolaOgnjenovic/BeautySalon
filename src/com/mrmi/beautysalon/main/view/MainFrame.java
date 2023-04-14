package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.controller.AuthController;
import com.mrmi.beautysalon.main.controller.SalonController;
import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.controller.UserController;
import com.mrmi.beautysalon.main.entity.BeautySalon;
import com.mrmi.beautysalon.main.entity.Database;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final BeautySalon beautySalon;

    private final TreatmentController treatmentController;
    private final UserController userController;
    private final AuthController authController;

    private JButton login;
    private JButton register;

    public MainFrame() {
        Database database = new Database("");
        SalonController salonController = new SalonController("");
        beautySalon = salonController.getBeautySalon();
        treatmentController = new TreatmentController(database, salonController);
        userController = new UserController(database, treatmentController, salonController);
        authController = new AuthController(userController);
        intialiseViews();
        initialiseListeners();
    }

    private void intialiseViews() {
        this.setTitle("Beauty salon");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(800, 800);
        this.setVisible(true);
        this.setLayout(new FlowLayout());

        login = new JButton("Login");
        this.add(login);

        register = new JButton("Register");
        this.add(register);
    }

    private void initialiseListeners() {
        register.addActionListener(e -> {
            this.dispose();
            RegisterFrame registerFrame = new RegisterFrame();
        });

        login.addActionListener(e -> {
            this.dispose();
            LoginFrame loginFrame = new LoginFrame(treatmentController, userController, beautySalon, authController);
        });
    }
}
