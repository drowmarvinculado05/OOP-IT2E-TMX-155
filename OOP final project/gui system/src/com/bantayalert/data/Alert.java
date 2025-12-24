package com.bantayalert.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Data model for emergency alerts.
 */
public class Alert {
    private final String id;
    private final String title;
    private final String message;
    private final String type; // e.g., "disaster", "announcement"
    private final String severity; // e.g., "low", "medium", "high", "critical"
    private final String affectedBarangays; // comma-separated list
    private final String status; // "ACTIVE" or "INACTIVE"
    private final String dateCreated;

    public Alert(String id, String title, String message, String type, String severity, 
                 String affectedBarangays, String status, String dateCreated) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.type = type;
        this.severity = severity;
        this.affectedBarangays = affectedBarangays;
        this.status = status;
        this.dateCreated = dateCreated;
    }

    public Alert(String title, String message, String type, String severity, String affectedBarangays) {
        this.id = java.util.UUID.randomUUID().toString();
        this.title = title;
        this.message = message;
        this.type = type;
        this.severity = severity;
        this.affectedBarangays = affectedBarangays;
        this.status = "ACTIVE";
        this.dateCreated = LocalDate.now().format(DateTimeFormatter.ofPattern("M/d/yyyy"));
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public String getSeverity() {
        return severity;
    }

    public String getAffectedBarangays() {
        return affectedBarangays;
    }

    public String getStatus() {
        return status;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public Alert withStatus(String newStatus) {
        return new Alert(id, title, message, type, severity, affectedBarangays, newStatus, dateCreated);
    }
}

