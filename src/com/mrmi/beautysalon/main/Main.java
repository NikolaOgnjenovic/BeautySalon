package com.mrmi.beautysalon.main;

import com.formdev.flatlaf.intellijthemes.*;
import com.mrmi.beautysalon.main.view.MainFrame;


public class Main {
    public static void main(String[] args) {
        FlatDarkPurpleIJTheme.setup();
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }
}