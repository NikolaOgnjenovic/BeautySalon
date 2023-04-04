package com.mrmi.beautysalon.main.gui;

import com.mrmi.beautysalon.main.exceptions.TreatmentTypeNotFoundException;
import com.mrmi.beautysalon.main.exceptions.UserNotFoundException;
import com.mrmi.beautysalon.main.objects.*;

import javax.swing.table.AbstractTableModel;
import java.util.*;

public class UserTableModel extends AbstractTableModel {
    private final HashMap<String, User> users;
    private final Database database;
    private final boolean canEdit;

    private final String[] columnNames = new String[] {
            "Username", "Password", "Name", "Surname", "Gender", "Phone number", "Address", "Qualification level",
            "Years of experience", "Monthly salary", "Bonus", "Known treatment types"
    };
    private final Class[] columnClass = new Class[] {
            String.class, String.class, String.class, String.class, String.class, String.class, String.class, Byte.class,
            Byte.class, Double.class, Double.class, String.class
    };

    public UserTableModel(Database database, HashMap<String, User> users, boolean canEdit)
    {
        this.database = database;
        this.users = users;
        this.canEdit = canEdit;
    }

    @Override
    public String getColumnName(int column)
    {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return columnClass[columnIndex];
    }

    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }

    @Override
    public int getRowCount()
    {
        return users.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        User user = users.values().stream().toList().get(rowIndex);
        switch (columnIndex) {
            case 0 -> {
                return users.keySet().stream().toList().get(rowIndex);
            }
            case 1 -> {
                return user.getPassword();
            }
            case 2 -> {
                return user.getName();
            }
            case 3 -> {
                return user.getSurname();
            }
            case 4 -> {
                return user.getGender();
            }
            case 5 -> {
                return user.getPhoneNumber();
            }
            case 6 -> {
                return user.getAddress();
            }
            case 7 -> {
                if (!user.getClass().equals(Client.class)) {
                    Employee e = (Employee) user;
                    return e.getQualificationLevel();
                }
            }
            case 8 -> {
                if (!user.getClass().equals(Client.class)) {
                    Employee e = (Employee) user;
                    return e.getYearsOfExperience();
                }
            }
            case 9 -> {
                if (!user.getClass().equals(Client.class)) {
                    Employee e = (Employee) user;
                    return e.getMonthlySalary();
                }
            }
            case 10 -> {
                if (!user.getClass().equals(Client.class)) {
                    Employee e = (Employee) user;
                    return e.getBonus();
                }
            }
            case 11 -> {
                if (user.getClass().equals(Beautician.class)) {
                    Beautician b = (Beautician) user;
                    return b.getTreatmentTypeIDs().toString();
                }
            }
            default -> {
                return null;
            }
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        if (!canEdit) {
            return;
        }
        User user = users.values().stream().toList().get(rowIndex);
        switch (columnIndex) {
            case 1 -> user.setPassword(aValue.toString());
            case 2 -> user.setName(aValue.toString());
            case 3 -> user.setSurname(aValue.toString());
            case 4 -> user.setGender(aValue.toString());
            case 5 -> user.setPhoneNumber(aValue.toString());
            case 6 -> user.setAddress(aValue.toString());
            case 7 -> {
                if (!user.getClass().equals(Client.class)) {
                    Employee e = (Employee) user;
                    e.setQualificationLevel(Byte.parseByte(aValue.toString()));
                }
            }
            case 8 -> {
                if (!user.getClass().equals(Client.class)) {
                    Employee e = (Employee) user;
                    e.setYearsOfExperience(Byte.parseByte(aValue.toString()));
                }
            }
            case 9 -> {
                if (!user.getClass().equals(Client.class)) {
                    Employee e = (Employee) user;
                    e.setMonthlySalary(Double.parseDouble(aValue.toString()));
                }
            }
            case 10 -> {
                if (!user.getClass().equals(Client.class)) {
                    Employee e = (Employee) user;
                    e.setBonus(Double.parseDouble(aValue.toString()));
                }
            }
            case 11 -> {
                if (!user.getClass().equals(Client.class)) {
                    Beautician b = (Beautician) user;
                    List<Byte> treatmentTypeIDs = new ArrayList<>();
                    String[] values = aValue.toString().substring(1, aValue.toString().length() - 1).split(", ");
                    for (String v : values) {
                        treatmentTypeIDs.add(Byte.parseByte(v));
                    }
                    b.setTreatmentTypeIDs(treatmentTypeIDs);
                }
            }
        }

        try {
            database.updateUser(users.keySet().stream().toList().get(rowIndex), user);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return canEdit;
    }
}
