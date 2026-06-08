CREATE TABLE USERS (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE SATELLITES (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    function VARCHAR(100) NOT NULL, -- e.g., GPS, Communication, Defense
    status VARCHAR(50) DEFAULT 'Operational',
    risk_level VARCHAR(20) DEFAULT 'Low' -- Low, Medium, High, Critical
);

-- Security Events Table (Invasion attempts, failures, etc)
CREATE TABLE SECURITY_EVENTS (
    id INT PRIMARY KEY AUTO_INCREMENT,
    satellite_id INT NOT NULL,
    event_type VARCHAR(100) NOT NULL, -- e.g., 'Authentication Failure', 'Suspicious Command'
    description TEXT,
    event_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (satellite_id) REFERENCES SATELLITES(id) ON DELETE CASCADE
);

-- Alerts Table generated from events
CREATE TABLE SECURITY_ALERTS (
    id INT PRIMARY KEY AUTO_INCREMENT,
    event_id INT NOT NULL,
    severity VARCHAR(20) NOT NULL, -- e.g., 'Warning', 'Danger', 'Critical'
    resolution_status VARCHAR(50) DEFAULT 'Active', -- Active, Analyzing, Resolved
    FOREIGN KEY (event_id) REFERENCES SECURITY_EVENTS(id) ON DELETE CASCADE
);
