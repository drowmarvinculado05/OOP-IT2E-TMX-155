package com.bantayalert.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Data model for incident reports.
 */
public class IncidentReport {
    private final String id;
    private final String type; // e.g., "Accident", "Other", "Fire", etc.
    private final String description;
    private final String location; // coordinates or address
    private final String photoPath; // file path to photo
    private final String videoPath; // file path to video
    private final String status; // "PENDING", "VERIFIED", "RESPONDED"
    private final String dateTime; // formatted date and time
    private final String reporterName;
    private final String reporterEmail;

    public IncidentReport(String id, String type, String description, String location,
                         String photoPath, String videoPath, String status, 
                         String dateTime, String reporterName, String reporterEmail) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.location = location;
        this.photoPath = photoPath;
        this.videoPath = videoPath;
        this.status = status;
        this.dateTime = dateTime;
        this.reporterName = reporterName;
        this.reporterEmail = reporterEmail;
    }

    public IncidentReport(String type, String description, String location,
                         String photoPath, String videoPath, String reporterName, 
                         String reporterEmail) {
        this.id = java.util.UUID.randomUUID().toString();
        this.type = type;
        this.description = description;
        this.location = location;
        this.photoPath = photoPath;
        this.videoPath = videoPath;
        this.status = "PENDING";
        this.dateTime = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("M/d/yyyy, h:mm:ss a"));
        this.reporterName = reporterName;
        this.reporterEmail = reporterEmail;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public String getStatus() {
        return status;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getReporterName() {
        return reporterName;
    }

    public String getReporterEmail() {
        return reporterEmail;
    }

    public IncidentReport withStatus(String newStatus) {
        return new IncidentReport(id, type, description, location, photoPath, 
                                 videoPath, newStatus, dateTime, reporterName, reporterEmail);
    }
}

