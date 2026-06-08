package com.spaceshield;

import java.sql.Connection;

public class SpaceShieldApp {
    public static void main(String[] args) {
        System.out.println("--- SpaceShield Boot Sequence Initiated ---\n");

        try (Connection dbConnection = DatabaseConfig.getConnection()) {
            
            System.out.println("[System] H2 Database connected successfully.\n");

            AuthService authService = new AuthService(dbConnection);
            
            User adminUser = authService.registerUser("User1", "admin@spaceshield.com", "securePass123");

            System.out.println("Attempting login...");
            boolean isAcessGranted = authService.login("admin@spaceshield.com", "securePass123");

            if (!isAcessGranted) {
                System.out.println("ACCESS DENIED. Incorrect credentials.");
                return;
            }

            System.out.println("ACCESS GRANTED. Welcome, " + adminUser.getName() + ".\n");

            Monitoring system = new Monitoring();
            
            Satellite s1 = new Satellite(1, "Starlink-BR1", "Internet");
            Satellite s2 = new Satellite(2, "GeoSync-Defense", "Military Communications");
            system.registerSatellite(s1);
            system.registerSatellite(s2);

            System.out.println();

            AccessEvent ev1 = new AccessEvent(101, s2, "Suspicious Command", "Attempted orbit alteration");
            AccessEvent ev2 = new AccessEvent(102, s2, "Unauthorized Access", "Unknown IP attempted SSH login");

            system.logEvent(ev1);
            system.logEvent(ev2);

            System.out.println();

            Report.generateIncidentHistory(s2);

        } catch (Exception e) {
            System.err.println("CRITICAL FAILURE: Database connection failed.");
            e.printStackTrace();
        }
    }
}
