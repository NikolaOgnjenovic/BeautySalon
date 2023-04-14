package com.mrmi.beautysalon.main.view;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    public RegisterFrame() {
        initialiseViews();
        initialiseListeners();
    }
    private void initialiseViews() {
        this.setTitle("Register");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(true);
        this.setSize(800, 800);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(235, 235, 235));
    }

    // TODO
    private void initialiseListeners() {

    }
}
