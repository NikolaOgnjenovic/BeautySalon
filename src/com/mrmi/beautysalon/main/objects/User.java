package com.mrmi.beautysalon.main.objects;

public abstract class User {
    private String password;
    private String name;
    private String surname;
    private String gender;
    private String phoneNumber;
    private String address;

    public User(String password, String name, String surname, String gender, String phoneNumber, String address) {
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", gender=" + gender +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public String getFileString(String username) {
        return "," + username + "," + password + "," + name + "," + surname + "," + gender + "," + phoneNumber + "," + address + ",";
    }
}
