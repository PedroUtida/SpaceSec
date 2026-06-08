package com.spaceshield;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Monitoring {
    
    private final Connection connection;

    private List<Satellite> registeredSatellites = new ArrayList<>();
    private List<SecurityAlert> activeAlerts = new ArrayList<>();

    public Monitoring(Connection connection) {
        this.connection = connection;
    }

    public void registerSatellite(Satellite satellite) {
        String sql = "INSERT INTO SATELLITES (name, function, status, risk_level) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, satellite.getName());
            pstmt.setString(2, satellite.getFunction());
            pstmt.setString(3, satellite.getStatus());
            pstmt.setString(4, satellite.getRiskLevel());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                satellite.setId(rs.getInt(1)); 
            }
            
            registeredSatellites.add(satellite);
            System.out.println("Satellite " + satellite.getName() + " saved to database.");
            
        } catch (Exception e) {
            System.err.println("DB Error registering satellite: " + e.getMessage());
        }
    }

    public Satellite findSatellite(int id) {
        return registeredSatellites.stream()
            .filter(s -> s.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public void logEvent(AccessEvent event) {
        event.getSatellite().addEvent(event);

        String insertSql = "INSERT INTO SECURITY_EVENTS (satellite_id, event_type, description) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, event.getSatellite().getId());
            pstmt.setString(2, event.getEventType());
            pstmt.setString(3, event.getDescription());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                event.setId(rs.getInt(1)); 
            }
            System.out.println("Event logged to DB for satellite " + event.getSatellite().getName());

        } catch (Exception e) {
            System.err.println("DB Error logging event: " + e.getMessage());
        }

        String updateRiskSql = "UPDATE SATELLITES SET risk_level = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateRiskSql)) {
            pstmt.setString(1, event.getSatellite().getRiskLevel());
            pstmt.setInt(2, event.getSatellite().getId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("DB Error updating risk level: " + e.getMessage());
        }

        if (event.getEventType().contains("Unauthorized") || event.getEventType().contains("Invasion") || event.getEventType().contains("Suspicious")) {
            generateAlert(event, "Critical");
        }
    }

    private void generateAlert(AccessEvent event, String severity) {
        String sql = "INSERT INTO SECURITY_ALERTS (event_id, severity, resolution_status) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, event.getId());
            pstmt.setString(2, severity);
            pstmt.setString(3, "Active");
            pstmt.executeUpdate();

            int dbAlertId = 0;
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                dbAlertId = rs.getInt(1);
            }

            SecurityAlert alert = new SecurityAlert(dbAlertId, event, severity);
            activeAlerts.add(alert);
            System.out.println("!!! NEW ALERT SAVED TO DB !!! -> " + alert.toString());
            
        } catch (Exception e) {
            System.err.println("DB Error generating alert: " + e.getMessage());
        }
    }

    public List<SecurityAlert> getActiveAlerts() { return activeAlerts; }
    public List<Satellite> getSatellites() { return registeredSatellites; }
}
