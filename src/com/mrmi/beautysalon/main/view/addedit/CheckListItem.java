package com.mrmi.beautysalon.main.view.addedit;

public class CheckListItem {
    private final String label;
    private boolean isSelected = false;

    public CheckListItem(String label) {
        this.label = label;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public String toString() {
        return label;
    }
}
