package com.mrmi.beautysalon.main.entity;


public abstract class User implements TableCell {
    private int id;
    private final String username;
    private final String password;
    private final String name;
    private final String surname;
    private final String gender;
    private final String phoneNumber;
    private final String address;

    public User(int id, String username, String password, String name, String surname, String gender, String phoneNumber, String address) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getGender() {
        return gender;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getAddress() {
        return address;
    }

    public String getFileString() {
        return "," + id + "," + username + "," + password + "," + name + "," + surname + "," + gender + "," + phoneNumber + "," + address + ",";
    }

    @Override
    public Object getCell(int column, Object manager) {
        switch (column) {
            case 0:
                return id;
            case 1:
                return username;
            case 2:
                return password;
            case 3:
                return name;
            case 4:
                return surname;
            case 5:
                return gender;
            case 6:
                return phoneNumber;
            case 7:
                return address;
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Id";
            case 1:
                return "Username";
            case 2:
                return "Password";
            case 3:
                return "Name";
            case 4:
                return "Surname";
            case 5:
                return "Gender";
            case 6:
                return "Phone number";
            case 7:
                return "Address";
            case 8:
                return "Qualification level";
            case 9:
                return "Years of experience";
            case 10:
                return "Monthly salary";
            case 11:
                return "Bonus";
            case 12:
                return "Known treatment type categories";
            case 13:
                return "Has loyalty card";
            default:
                return null;
        }
    }

    @Override
    public int getColumnCount() {
        return 14;
    }
}
