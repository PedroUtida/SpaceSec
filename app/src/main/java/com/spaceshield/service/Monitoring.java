package com.spaceshield.service;

import com.spaceshield.model.AccessEvent;
import com.spaceshield.model.Satellite;
import com.spaceshield.model.SecurityAlert;
import java.util.ArrayList;
import java.util.List;

public class Monitoring {

    // In-memory databases
    private List<Satellite> registeredSatellites = new ArrayList<>();
    private List<SecurityAlert> activeAlerts = new ArrayList<>();

    // No connection parameter needed!
    public Monitoring() {}

    public void registerSatellite(Satellite satellite) {
        registeredSatellites.add(satellite);
        System.out.println("Satellite " + satellite.getName() + " registered successfully.");
    }

    public Satellite findSatellite(int id) {
        return registeredSatellites.stream()
            .filter(s -> s.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public void logEvent(AccessEvent event) {
        event.getSatellite().addEvent(event);
        System.out.println("Event logged on satellite " + event.getSatellite().getName());

        if (event.getEventType().contains("Unauthorized") || event.getEventType().contains("Invasion") || event.getEventType().contains("Suspicious")) {
            generateAlert(event, "Critical");
        }
    }

    private void generateAlert(AccessEvent event, String severity) {
        SecurityAlert alert = new SecurityAlert((int)(Math.random() * 1000), event, severity);
        activeAlerts.add(alert);
        System.out.println("!!! NEW ALERT GENERATED !!! -> " + alert.toString());
    }

    public List<SecurityAlert> getActiveAlerts() { return activeAlerts; }
    public List<Satellite> getSatellites() { return registeredSatellites; }
}
