package com.spaceshield.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseViewer {
    public static void main(String[] args) {
        System.out.println("====================================================================================================");
        System.out.println("                                    SPACESHIELD DATABASE VIEWER                                     ");
        System.out.println("====================================================================================================\n");

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            // --- 1. PRINT USERS TABLE ---
            System.out.println(">>> TABLE: USERS");
            ResultSet rsUsers = stmt.executeQuery("SELECT id, name, email FROM USERS");
            System.out.printf("%-5s | %-15s | %-25s\n", "ID", "NAME", "EMAIL");
            System.out.println("--------------------------------------------------");
            while (rsUsers.next()) {
                System.out.printf("%-5d | %-15s | %-25s\n",
                        rsUsers.getInt("id"),
                        rsUsers.getString("name"),
                        rsUsers.getString("email"));
            }
            System.out.println("\n");

            // --- 2. PRINT SATELLITES TABLE ---
            System.out.println(">>> TABLE: SATELLITES");
            ResultSet rsSat = stmt.executeQuery("SELECT id, name, function, status, risk_level FROM SATELLITES");
            System.out.printf("%-5s | %-20s | %-20s | %-15s | %-10s\n", "ID", "NAME", "FUNCTION", "STATUS", "RISK");
            System.out.println("--------------------------------------------------------------------------------");
            while (rsSat.next()) {
                System.out.printf("%-5d | %-20s | %-20s | %-15s | %-10s\n",
                        rsSat.getInt("id"),
                        rsSat.getString("name"),
                        rsSat.getString("function"),
                        rsSat.getString("status"),
                        rsSat.getString("risk_level"));
            }
            System.out.println("\n");

            // --- 3. PRINT SECURITY EVENTS TABLE ---
            System.out.println(">>> TABLE: SECURITY_EVENTS");
            ResultSet rsEvents = stmt.executeQuery("SELECT id, satellite_id, event_type, description, event_time FROM SECURITY_EVENTS");
            System.out.printf("%-5s | %-6s | %-25s | %-35s | %-20s\n", "ID", "SAT_ID", "EVENT_TYPE", "DESCRIPTION", "TIMESTAMP");
            System.out.println("----------------------------------------------------------------------------------------------------");
            while (rsEvents.next()) {
                System.out.printf("%-5d | %-6d | %-25s | %-35s | %-20s\n",
                        rsEvents.getInt("id"),
                        rsEvents.getInt("satellite_id"),
                        rsEvents.getString("event_type"),
                        rsEvents.getString("description"),
                        rsEvents.getString("event_time"));
            }
            System.out.println("\n");

            // --- 4. PRINT SECURITY ALERTS TABLE ---
            System.out.println(">>> TABLE: SECURITY_ALERTS");
            ResultSet rsAlerts = stmt.executeQuery("SELECT id, event_id, severity, resolution_status FROM SECURITY_ALERTS");
            System.out.printf("%-5s | %-8s | %-15s | %-20s\n", "ID", "EVENT_ID", "SEVERITY", "STATUS");
            System.out.println("----------------------------------------------------------");
            while (rsAlerts.next()) {
                System.out.printf("%-5d | %-8d | %-15s | %-20s\n",
                        rsAlerts.getInt("id"),
                        rsAlerts.getInt("event_id"),
                        rsAlerts.getString("severity"),
                        rsAlerts.getString("resolution_status"));
            }
            System.out.println();

        } catch (Exception e) {
            System.err.println("Failed to read database: " + e.getMessage());
        }
    }
}
