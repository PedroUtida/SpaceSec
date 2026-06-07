package com.spaceshield;

public class SecurityAlert {
    private int id;
    private AccessEvent event;
    private String severity;
    private String resolutionStatus;

    public SecurityAlert(int id, AccessEvent event, String severity) {
        this.id = id;
        this.event = event;
        this.severity = severity;
        this.resolutionStatus = "Active";
    }

    public void resolveAlert() { this.resolutionStatus = "Resolved"; }

    public int getId() { return id; }
    public AccessEvent getEvent() { return event; }
    public String getSeverity() { return severity; }
    public String getResolutionStatus() { return resolutionStatus; }

    @Override
    public String toString() {
        return "ALERT [" + severity + "] - Satellite: " + event.getSatellite().getName() + " | Threat: " + event.getEventType();
    }
}
