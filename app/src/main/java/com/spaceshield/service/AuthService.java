package com.spaceshield.service;

import com.spaceshield.model.User;
import org.mindrot.jbcrypt.BCrypt;
import java.util.ArrayList;
import java.util.List;

public class AuthService {
    
    // In-memory database
    private List<User> users = new ArrayList<>();

    // No connection parameter needed!
    public AuthService() {}

    public User registerUser(String name, String email, String rawPassword) {
        String hashedPass = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        User newUser = new User((int)(Math.random() * 1000), name, email, hashedPass);
        users.add(newUser);
        return newUser;
    }

    public boolean login(String emailAttempt, String loginPasswordAttempt) {
        for (User u : users) {
            if (u.getEmail().equals(emailAttempt)) {
                return BCrypt.checkpw(loginPasswordAttempt, u.getPasswordHash());
            }
        }
        return false;
    }
}
