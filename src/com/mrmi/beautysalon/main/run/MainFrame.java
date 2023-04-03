package com.mrmi.beautysalon.main.run;

import com.mrmi.beautysalon.main.objects.Database;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame(Database database) {
        this.setTitle("Beauty salon");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(800, 800);
        this.setVisible(true);
        this.setLayout(new FlowLayout());

        JButton register = new JButton("Register");
        JButton login = new JButton("Login");
        this.add(register);
        this.add(login);

        register.addActionListener(e -> {
            this.dispose();
            Register registerFrame = new Register();
        });

        login.addActionListener(e -> {
            this.dispose();
            Login loginFrame = new Login(database);
        });
    }
}
