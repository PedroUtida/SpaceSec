package com.spaceshield.model;

import java.util.ArrayList;
import java.util.List;

public class Satellite {
    private int id;
    private String name;
    private String function;
    private String status;
    private String riskLevel; 
    private List<AccessEvent> events; 

    public Satellite(int id, String name, String function) {
        this.id = id;
        this.name = name;
        this.function = function;
        this.status = "Operational";
        this.riskLevel = "Low";
        this.events = new ArrayList<>();
    }

    public void addEvent(AccessEvent event) {
        this.events.add(event);
        updateRiskLevel();
    }

    private void updateRiskLevel() {
        if (events.size() > 5) {
            this.riskLevel = "High";
        } else if (events.size() > 2) {
            this.riskLevel = "Medium";
        }
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getFunction() { return function; }
    public String getStatus() { return status; }
    public String getRiskLevel() { return riskLevel; }
    public List<AccessEvent> getEvents() { return events; }
    public void setId(int id) { this.id = id; }
}
