package com.mrmi.beautysalon.main.run;

import com.mrmi.beautysalon.main.gui.MainFrame;
import com.mrmi.beautysalon.main.objects.Database;

public class GUIApp {
    public void run() {
        Database database = new Database("");
        MainFrame mainFrame = new MainFrame(database);
    }
}
