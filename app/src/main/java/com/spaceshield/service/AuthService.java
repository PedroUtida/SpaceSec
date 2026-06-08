package com.spaceshield.service;

import com.spaceshield.model.User;
import org.mindrot.jbcrypt.BCrypt;
import java.util.ArrayList;
import java.util.List;

public class AuthService {

    private List<User> users = new ArrayList<>();

    public AuthService() {}

    public User registerUser(String name, String email, String rawPassword) {
        String hashedPass = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        int newId = users.size() + 1;
        User newUser = new User(newId, name, email, hashedPass);
        users.add(newUser);
        return newUser;
    }

    public User login(String emailAttempt, String loginPasswordAttempt) {
        for (User u : users) {
            if (u.getEmail().equals(emailAttempt)) {
                if (BCrypt.checkpw(loginPasswordAttempt, u.getPasswordHash())) {
                    return u;
                }
            }
        }
        return null;
    }
}
