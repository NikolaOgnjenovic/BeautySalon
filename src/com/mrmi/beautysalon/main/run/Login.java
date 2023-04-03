package com.mrmi.beautysalon.main.run;

import com.mrmi.beautysalon.main.objects.Client;
import com.mrmi.beautysalon.main.objects.Database;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Login extends JFrame {
    public Login(Database database) {
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
                    ClientFrame clientFrame = new ClientFrame(database, (Client) Database.currentUser, username.getText());
                }
            }
        });
    }
}
