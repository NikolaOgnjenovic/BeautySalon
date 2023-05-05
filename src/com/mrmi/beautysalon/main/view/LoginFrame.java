package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.manager.AuthManager;
import com.mrmi.beautysalon.main.manager.SalonManager;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import com.mrmi.beautysalon.main.entity.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final TreatmentManager treatmentManager;
    private final UserManager userManager;
    private final SalonManager salonManager;
    private final AuthManager authManager;
    private JTextField username;
    private JPasswordField password;
    private JLabel failedLoginLabel;
    private JButton login;
    public LoginFrame(TreatmentManager treatmentManager, UserManager userManager, SalonManager salonManager, AuthManager authManager) {
        this.treatmentManager = treatmentManager;
        this.userManager = userManager;
        this.salonManager = salonManager;
        this.authManager = authManager;

        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap 2", "[grow][grow]", "[grow]40[grow]40[grow]10[grow]"));
        this.setTitle("Beauty salon - Login");
        this.setSize(1000, 1080);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        JLabel title = new JLabel("Login to your account");
        Utility.setFont(title, 30);
        this.add(title, "span, align center");

        JLabel usernameLabel = new JLabel("Username:");
        Utility.setFont(usernameLabel, 24);
        this.add(usernameLabel, "align right");

        username = new JTextField(18);
        Utility.setFont(username, 24);
        username.setPreferredSize(new Dimension(250, 40));
        this.add(username, "align left");

        JLabel passwordLabel = new JLabel("Password:");
        Utility.setFont(passwordLabel, 24);
        this.add(passwordLabel, "align right");

        password = new JPasswordField(18);
        password.setPreferredSize(new Dimension(250, 40));
        Utility.setFont(password, 24);
        this.add(password, "align left");

        login = new JButton("Login");
        Utility.setFont(login, 24);
        this.add(login, "span, align center");

        failedLoginLabel = new JLabel(" ");
        Utility.setFont(failedLoginLabel, 20);
        failedLoginLabel.setForeground(Color.RED);
        this.add(failedLoginLabel, "span, align center");
    }

    private void initialiseListeners() {
        login.addActionListener(e -> {
            if (!authManager.login(username.getText(), String.valueOf(password.getPassword()))) {
                failedLoginLabel.setText("Invalid credentials. Please try again!");
            } else {
                if (authManager.getCurrentUser().getClass().equals(Client.class)) {
                    this.dispose();
                    ClientFrame clientFrame = new ClientFrame((Client) authManager.getCurrentUser(), authManager.getCurrentUsername(), treatmentManager, userManager, salonManager, authManager);
                } else if (authManager.getCurrentUser().getClass().equals(Beautician.class)) {
                    this.dispose();
                    BeauticianFrame beauticianFrame = new BeauticianFrame(authManager.getCurrentUsername(), treatmentManager, userManager, salonManager);
                } else if (authManager.getCurrentUser().getClass().equals(Receptionist.class)) {
                    this.dispose();
                    ReceptionistFrame receptionistFrame = new ReceptionistFrame(treatmentManager, userManager, salonManager);
                } else if (authManager.getCurrentUser().getClass().equals(Manager.class)) {
                    this.dispose();
                    ManagerFrame managerFrame = new ManagerFrame(treatmentManager, userManager, salonManager, authManager);
                }
            }
        });
    }
}
