package com.mrmi.beautysalon.main.entity;

import com.mrmi.beautysalon.main.manager.TreatmentManager;

public class Client extends User implements TableCell {

    // Used when adding
    public Client(String username, String password, String name, String surname, String gender, String phoneNumber, String address) {
        super(username, password, name, surname, gender, phoneNumber, address);
    }

    // Used when reading from files
    public Client(int id, String username, String password, String name, String surname, String gender, String phoneNumber, String address) {
        super(id, username, password, name, surname, gender, phoneNumber, address);
    }

    @Override
    public String toString() {
        return getUsername() + " " + getName() + " " + getSurname();
    }

    @Override
    public String getFileString() {
        return "C" + super.getFileString();
    }

    @Override
    public Object getCell(int column, Object manager) {
        if (column < 13) {
            return super.getCell(column, manager);
        }

        if (column == 13) {
            return ((TreatmentManager) manager).hasLoyaltyCard(this);
        }

        return ((TreatmentManager) manager).getMoneySpent(this.getUsername());
    }
}
