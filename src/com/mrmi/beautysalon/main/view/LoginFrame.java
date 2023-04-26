package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.controller.AuthController;
import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.controller.UserController;
import com.mrmi.beautysalon.main.entity.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final TreatmentController treatmentController;
    private final UserController userController;
    private final BeautySalon beautySalon;
    private final AuthController authController;
    private JTextField username;
    private JPasswordField password;
    private JLabel failedLoginLabel;
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
        this.setLayout(new MigLayout("wrap 2", "[grow][grow]", "[grow]40[grow]40[grow]10[grow]"));
        this.setTitle("Beauty salon - Login");
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        JLabel title = new JLabel("Login to your account");
        setFont(title, 30);
        this.add(title, "span, align center");

        JLabel usernameLabel = new JLabel("Username:");
        setFont(usernameLabel, 24);
        this.add(usernameLabel, "align right");

        username = new JTextField(18);
        setFont(username, 24);
        username.setPreferredSize(new Dimension(250, 40));
        this.add(username, "align left");

        JLabel passwordLabel = new JLabel("Password:");
        setFont(passwordLabel, 24);
        this.add(passwordLabel, "align right");

        password = new JPasswordField(18);
        password.setPreferredSize(new Dimension(250, 40));
        setFont(password, 24);
        this.add(password, "align left");

        login = new JButton("Login");
        setFont(login, 24);
        this.add(login, "span, align center");

        failedLoginLabel = new JLabel(" ");
        setFont(failedLoginLabel, 20);
        failedLoginLabel.setForeground(Color.RED);
        this.add(failedLoginLabel, "span, align center");
    }

    private void initialiseListeners() {
        login.addActionListener(e -> {
            if (!authController.login(username.getText(), String.valueOf(password.getPassword()))) {
                failedLoginLabel.setText("Invalid credentials. Please try again!");
            } else {
                if (authController.getCurrentUser().getClass().equals(Client.class)) {
                    this.dispose();
                    ClientFrame clientFrame = new ClientFrame((Client) authController.getCurrentUser(), authController.getCurrentUsername(), treatmentController, userController, beautySalon, authController);
                } else if (authController.getCurrentUser().getClass().equals(Beautician.class)) {
                    this.dispose();
                    BeauticianFrame beauticianFrame = new BeauticianFrame(authController.getCurrentUsername(), treatmentController, userController, beautySalon);
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

    private void setFont(JComponent component, float size) {
        component.setFont(component.getFont().deriveFont(size));
    }
}
