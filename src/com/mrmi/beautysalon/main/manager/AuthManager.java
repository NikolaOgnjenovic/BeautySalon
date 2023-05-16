package com.mrmi.beautysalon.main.manager;

import com.mrmi.beautysalon.main.entity.User;

import java.util.HashMap;

public class AuthManager {
    private final UserManager userManager;
    private String currentUsername;
    private User currentUser;

    public AuthManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean login(String username, String password) {
        HashMap<Integer, User> users = userManager.getUsers();
        for (User user : users.values()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                currentUsername = username;
                return true;
            }
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
