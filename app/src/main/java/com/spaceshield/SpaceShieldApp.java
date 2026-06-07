package com.spaceshield;

public class SpaceShieldApp {
    public static void main(String[] args) {
        System.out.println("--- SpaceShield Boot Sequence Initiated ---\n");

        // 0. AUTHENTICATION REQUIREMENT
        AuthService authService = new AuthService();
        
        // Simulating registering an admin user in the database
        User adminUser = authService.registerUser(1, "User1", "admin@spaceshield.com", "securePass123");

        System.out.println("Attempting login...");
        boolean isAcessGranted = authService.login(adminUser, "securePass123");

        if (!isAcessGranted) {
            System.out.println("ACCESS DENIED. Incorrect credentials.");
            return; // Kills the application immediately
        }

        System.out.println("ACCESS GRANTED. Welcome, " + adminUser.getName() + ".\n");

        // 1. Registering Satellites
        Monitoring system = new Monitoring();
        
        Satellite s1 = new Satellite(1, "Starlink-BR1", "Internet");
        Satellite s2 = new Satellite(2, "GeoSync-Defense", "Military Communications");
        system.registerSatellite(s1);
        system.registerSatellite(s2);

        System.out.println();

        // 2. Logging Events
        AccessEvent ev1 = new AccessEvent(101, s2, "Suspicious Command", "Attempted orbit alteration");
        AccessEvent ev2 = new AccessEvent(102, s2, "Unauthorized Access", "Unknown IP attempted SSH login");

        system.logEvent(ev1);
        system.logEvent(ev2);

        System.out.println();

        // 3. Generating Reports
        Report.generateIncidentHistory(s2);
    }
}
