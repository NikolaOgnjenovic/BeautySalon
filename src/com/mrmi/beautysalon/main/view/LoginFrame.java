package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.controller.AuthController;
import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.controller.UserController;
import com.mrmi.beautysalon.main.entity.*;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final TreatmentController treatmentController;
    private final UserController userController;
    private final BeautySalon beautySalon;
    private final AuthController authController;
    private JTextField username;
    private JPasswordField password;
    private JButton login;
    public LoginFrame(TreatmentController treatmentController, UserController userController, BeautySalon beautySalon, AuthController authController) {
        this.treatmentController = treatmentController;
        this.userController = userController;
        this.beautySalon = beautySalon;
        this.authController = authController;

        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setTitle("Login");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(800, 800);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(235, 235, 235));
        this.setLayout(new FlowLayout());

        username = new JTextField();
        username.setToolTipText("Username");
        username.setPreferredSize(new Dimension(250, 40));

        password = new JPasswordField();
        password.setToolTipText("Password");
        password.setPreferredSize(new Dimension(250, 40));

        login = new JButton("Login");
        this.add(username);
        this.add(password);
        this.add(login);
    }

    private void initialiseListeners() {
        login.addActionListener(e -> {
            if (!authController.login(username.getText(), String.valueOf(password.getPassword()))) {
                username.setText("Wrong username");
                password.setText("Wrong password");
            } else {
                if (authController.getCurrentUser().getClass().equals(Client.class)) {
                    this.dispose();
                    ClientFrame clientFrame = new ClientFrame((Client) authController.getCurrentUser(), authController.getCurrentUsername(), treatmentController, userController, beautySalon);
                } else if (authController.getCurrentUser().getClass().equals(Beautician.class)) {
                    this.dispose();
                    BeauticianFrame beauticianFrame = new BeauticianFrame(authController.getCurrentUsername(), treatmentController, userController);
                } else if (authController.getCurrentUser().getClass().equals(Receptionist.class)) {
                    this.dispose();
                    ReceptionistFrame receptionistFrame = new ReceptionistFrame(treatmentController, userController, beautySalon);
                } else if (authController.getCurrentUser().getClass().equals(Manager.class)) {
                    this.dispose();
                    ManagerFrame managerFrame = new ManagerFrame(treatmentController, userController, beautySalon);
                }
            }
        });
    }
}