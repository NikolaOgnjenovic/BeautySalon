package com.mrmi.beautysalon.main.run;

import com.mrmi.beautysalon.main.view.MainFrame;

import javax.swing.*;
import java.awt.*;

public class GUIApp {
    public void run() {
        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        if (defaults.get("Table.alternateRowColor") == null)
            defaults.put("Table.alternateRowColor", new Color(199, 199, 199));

        MainFrame mainFrame = new MainFrame();
    }
}
