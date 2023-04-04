package com.mrmi.beautysalon.main.gui;

import com.mrmi.beautysalon.main.objects.*;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    public LoginFrame(Database database) {
        this.setTitle("Login");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(800, 800);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(235, 235, 235));
        this.setLayout(new FlowLayout());

        JTextField username = new JTextField();
        username.setToolTipText("Username");
        username.setPreferredSize(new Dimension(250, 40));

        JPasswordField password = new JPasswordField();
        password.setToolTipText("Password");
        password.setPreferredSize(new Dimension(250, 40));

        JButton login = new JButton("Login");
        this.add(username);
        this.add(password);
        this.add(login);

        login.addActionListener(e -> {
            if (!database.login(username.getText(), String.valueOf(password.getPassword()))) {
                username.setText("Wrong username");
                password.setText("Wrong password");
            } else {
                if (Database.currentUser.getClass().equals(Client.class)) {
                    this.dispose();
                    ClientFrame clientFrame = new ClientFrame(database);
                } else if (Database.currentUser.getClass().equals(Beautician.class)) {
                    this.dispose();
                    BeauticianFrame beauticianFrame = new BeauticianFrame(database);
                } else if (Database.currentUser.getClass().equals(Receptionist.class)) {
                    this.dispose();
                    ReceptionistFrame receptionistFrame = new ReceptionistFrame(database);
                } else if (Database.currentUser.getClass().equals(Manager.class)) {
                    this.dispose();
                    ManagerFrame managerFrame = new ManagerFrame(database);
                }
            }
        });
    }
}
