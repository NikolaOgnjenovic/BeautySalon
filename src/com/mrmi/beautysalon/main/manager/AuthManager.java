package com.mrmi.beautysalon.main.manager;

import com.mrmi.beautysalon.main.entity.User;

import java.util.HashMap;

public class AuthManager {
    private String currentUsername;
    private User currentUser;
    private final HashMap<Integer, User> users;

    public AuthManager(UserManager userManager) {
        this.users = userManager.getUsers();
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean login(String username, String password) {
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
