package com.spaceshield;

import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    public User registerUser(int id, String name, String email, String rawPassword) {
        String hashedPass = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        return new User(id, name, email, hashedPass);
    }

    public boolean login(User userFromDatabase, String loginPasswordAttempt) {
        return BCrypt.checkpw(loginPasswordAttempt, userFromDatabase.getPasswordHash());
    }
}
