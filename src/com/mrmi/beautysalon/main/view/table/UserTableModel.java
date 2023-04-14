package com.mrmi.beautysalon.main.view.table;

import com.mrmi.beautysalon.main.controller.UserController;
import com.mrmi.beautysalon.main.entity.*;
import com.mrmi.beautysalon.main.exceptions.UserNotFoundException;

import javax.swing.table.AbstractTableModel;
import java.util.*;

public class UserTableModel extends AbstractTableModel {
    private final HashMap<String, User> users;
    private final UserController userController;
    private final boolean canEdit;

    private final String[] columnNames = new String[]{
            "Username", "Password", "Name", "Surname", "Gender", "Phone number", "Address", "Qualification level",
            "Years of experience", "Monthly salary", "Bonus", "Known treatment types", "Has loyalty card"
    };
    private final Class[] columnClass = new Class[]{
            String.class, String.class, String.class, String.class, String.class, String.class, String.class, Byte.class,
            Byte.class, Double.class, Double.class, String.class, Boolean.class
    };

    public UserTableModel(UserController userController, HashMap<String, User> users, boolean canEdit) {
        this.userController = userController;
        this.users = users;
        this.canEdit = canEdit;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClass[columnIndex];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return users.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        User user = new ArrayList<>(users.values()).get(rowIndex);
        //User user = users.values().stream().toList().get(rowIndex);
        switch (columnIndex) {
            case 0:
                return new ArrayList<>(users.keySet()).get(rowIndex);
            //return users.keySet().stream().toList().get(rowIndex);
            case 1:
                return user.getPassword();
            case 2:
                return user.getName();
            case 3:
                return user.getSurname();
            case 4:
                return user.getGender();
            case 5:
                return user.getPhoneNumber();
            case 6:
                return user.getAddress();
            case 7:
                if (!user.getClass().equals(Client.class)) {
                    Employee e = (Employee) user;
                    return e.getQualificationLevel();
                }
                break;
            case 8:
                if (!user.getClass().equals(Client.class)) {
                    Employee e = (Employee) user;
                    return e.getYearsOfExperience();
                }
                break;
            case 9:
                if (!user.getClass().equals(Client.class)) {
                    Employee e = (Employee) user;
                    return e.getMonthlySalary();
                }
                break;
            case 10:
                if (!user.getClass().equals(Client.class)) {
                    Employee e = (Employee) user;
                    return e.getBonus();
                }
                break;
            case 11:
                if (user.getClass().equals(Beautician.class)) {
                    Beautician b = (Beautician) user;
                    return b.getTreatmentTypeIDs().toString();
                }
                break;
            case 12:
                if (user.getClass().equals(Client.class)) {
                    Client c = (Client) user;
                    return c.hasLoyaltyCard();
                }
                break;
            default:
                return null;
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (!canEdit) {
            return;
        }
        User user = new ArrayList<>(users.values()).get(rowIndex);
        //User user = users.values().stream().toList().get(rowIndex);
        switch (columnIndex) {
            case 1:
                user.setPassword(aValue.toString());
                break;
            case 2:
                user.setName(aValue.toString());
                break;
            case 3:
                user.setSurname(aValue.toString());
                break;
            case 4:
                user.setGender(aValue.toString());
                break;
            case 5:
                user.setPhoneNumber(aValue.toString());
                break;
            case 6:
                user.setAddress(aValue.toString());
                break;
            case 7:
                if (!user.getClass().equals(Client.class)) {
                    Employee e = (Employee) user;
                    e.setQualificationLevel(Byte.parseByte(aValue.toString()));
                }
                break;
            case 8:
                if (!user.getClass().equals(Client.class)) {
                    Employee e = (Employee) user;
                    e.setYearsOfExperience(Byte.parseByte(aValue.toString()));
                }
                break;
            case 9:
                if (!user.getClass().equals(Client.class)) {
                    Employee e = (Employee) user;
                    e.setMonthlySalary(Double.parseDouble(aValue.toString()));
                }
                break;
            case 10:
                if (!user.getClass().equals(Client.class)) {
                    Employee e = (Employee) user;
                    e.setBonus(Double.parseDouble(aValue.toString()));
                }
                break;
            case 11:
                if (user.getClass().equals(Beautician.class)) {
                    Beautician b = (Beautician) user;
                    List<Byte> treatmentTypeIDs = new ArrayList<>();
                    String[] values = aValue.toString().substring(1, aValue.toString().length() - 1).split(", ");
                    for (String v : values) {
                        treatmentTypeIDs.add(Byte.parseByte(v));
                    }
                    b.setTreatmentTypeIDs(treatmentTypeIDs);
                }
                break;
        }
        try {
            userController.updateUser(new ArrayList<>(users.keySet()).get(rowIndex), user);
            //userController.updateUser(users.keySet().stream().toList().get(rowIndex), user);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit;
    }
}
