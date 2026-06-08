package com.spaceshield.service;

import com.spaceshield.model.AccessEvent;
import com.spaceshield.model.Satellite;

public class Report {
    public static void generateIncidentHistory(Satellite satellite) {
        System.out.println("=== INCIDENT REPORT: " + satellite.getName() + " ===");
        System.out.println("Current Risk Level: " + satellite.getRiskLevel());
        for (AccessEvent e : satellite.getEvents()) {
            System.out.println("- [" + e.getEventType() + "] " + e.getDescription());
        }
        System.out.println("=========================================");
    }
}
