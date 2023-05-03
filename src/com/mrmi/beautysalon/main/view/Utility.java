package com.mrmi.beautysalon.main.view;

import javax.swing.*;

public class Utility {
    public static void setFont(JComponent component, float size) {
        component.setFont(component.getFont().deriveFont(size));
    }
}
