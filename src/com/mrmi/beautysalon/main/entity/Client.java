package com.mrmi.beautysalon.main.entity;

public class Client extends User implements TableCell {
    private float moneySpent;
    private boolean hasLoyaltyCard;

    // Used when adding
    public Client(String username, String password, String name, String surname, String gender, String phoneNumber, String address) {
        super(username, password, name, surname, gender, phoneNumber, address);
        this.hasLoyaltyCard = false;
        this.moneySpent = 0;
    }

    // Used when reading from files
    public Client(int id, String username, String password, String name, String surname, String gender, String phoneNumber, String address, boolean hasLoyaltyCard, Float moneySpent) {
        super(id, username, password, name, surname, gender, phoneNumber, address);
        this.hasLoyaltyCard = hasLoyaltyCard;
        this.moneySpent = moneySpent;
    }

    public float getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent (float moneySpent) {
        this.moneySpent = moneySpent;
    }

    public boolean hasLoyaltyCard() {
        return hasLoyaltyCard;
    }

    public void setHasLoyaltyCard (boolean hasLoyaltyCard) {
        this.hasLoyaltyCard = hasLoyaltyCard;
    }

    @Override
    public String toString() {
        return getUsername() + " " + getName() + " " + getSurname();
    }

    @Override
    public String getFileString() {
        return "C" + super.getFileString() + hasLoyaltyCard + "," + moneySpent;
    }

    @Override
    public Object getCell(int column, Object manager) {
        if (column != 12) {
            return super.getCell(column, manager);
        }

        return hasLoyaltyCard;
    }
}
