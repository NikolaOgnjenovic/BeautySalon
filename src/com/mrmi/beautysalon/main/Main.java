package com.mrmi.beautysalon.main;

import com.mrmi.beautysalon.main.view.MainFrame;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) {
        changeUIDefaults();
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }

    private static void changeUIDefaults() {
        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        if (defaults.get("Table.alternateRowColor") == null) {
            defaults.put("Table.alternateRowColor", new Color(199, 199, 199));
        }

        int fontSize = 18;
        changeDefaultFontSize(defaults, fontSize);
    }

    private static void changeDefaultFontSize(UIDefaults defaults, int fontSize) {
        Enumeration<Object> keys = defaults.keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            if ((key instanceof String) && (((String) key).endsWith(".font"))) {
                defaults.put(key, new FontUIResource(new Font("Arial", Font.PLAIN, fontSize)));
            }
        }
    }
}