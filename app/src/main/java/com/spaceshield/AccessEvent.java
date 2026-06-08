package com.spaceshield;

import java.time.LocalDateTime;

public class AccessEvent {
    private int id;
    private Satellite satellite;
    private String eventType;
    private String description;
    private LocalDateTime timestamp;

    public AccessEvent(int id, Satellite satellite, String eventType, String description) {
        this.id = id;
        this.satellite = satellite;
        this.eventType = eventType;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }
    
    public int getId() { return id; }
    public Satellite getSatellite() { return satellite; }
    public String getEventType() { return eventType; }
    public String getDescription() { return description; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setId(int id) { this.id = id; }
}
