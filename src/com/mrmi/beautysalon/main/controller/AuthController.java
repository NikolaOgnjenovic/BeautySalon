package com.mrmi.beautysalon.main.controller;

import com.mrmi.beautysalon.main.entity.User;

import java.util.HashMap;

public class AuthController {
    private final UserController userController;
    private User currentUser;
    private String currentUsername;

    public AuthController(UserController userController) {
        this.userController = userController;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public boolean login(String username, String password) {
        HashMap<String, User> users = userController.getUsers();
        if (!users.containsKey(username)) {
            currentUser = null;
            currentUsername = "";
            return false;
        }
        if (users.get(username).getPassword().equals(password)) {
            currentUser = users.get(username);
            currentUsername = username;
            return true;
        }

        currentUser = null;
        currentUsername = "";
        return false;
    }

    public void logout() {
        currentUser = null;
        currentUsername = "";
    }
}
