package com.spaceshield;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class AuthService {
    
    private final Connection connection;

    public AuthService(Connection connection) {
        this.connection = connection;
    }

    public User registerUser(String name, String email, String rawPassword) {
        String hashedPass = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        
        String sql = "INSERT INTO USERS (name, email, password_hash) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, hashedPass);
            
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int newId = rs.getInt(1);
                return new User(newId, name, email, hashedPass);
            }
        } catch (Exception e) {
            System.err.println("Database Error during registration: " + e.getMessage());
        }
        
        return null;
    }

    public boolean login(String emailAttempt, String loginPasswordAttempt) {
        String sql = "SELECT password_hash FROM USERS WHERE email = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, emailAttempt);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String savedHash = rs.getString("password_hash");
                return BCrypt.checkpw(loginPasswordAttempt, savedHash);
            }
        } catch (Exception e) {
            System.err.println("Database Error during login: " + e.getMessage());
        }
        
        return false; 
    }
}
