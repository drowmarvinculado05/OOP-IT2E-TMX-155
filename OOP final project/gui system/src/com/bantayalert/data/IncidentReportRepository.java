package com.bantayalert.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository for managing incident reports.
 */
public class IncidentReportRepository {
    private static final List<IncidentReport> REPORTS = new ArrayList<>();

    private IncidentReportRepository() {
    }

    /**
     * Saves a new report or updates an existing one.
     */
    public static void save(IncidentReport report) {
        // Remove existing report with same ID if present
        REPORTS.removeIf(r -> r.getId().equals(report.getId()));
        REPORTS.add(report);
    }

    /**
     * Gets all reports.
     */
    public static List<IncidentReport> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(REPORTS));
    }

    /**
     * Gets reports by status.
     */
    public static List<IncidentReport> findByStatus(String status) {
        return REPORTS.stream()
            .filter(r -> status.equals(r.getStatus()))
            .collect(Collectors.toList());
    }


    /**
     * Deletes a report by ID.
     */
    public static boolean delete(String id) {
        return REPORTS.removeIf(r -> r.getId().equals(id));
    }

    /**
     * Gets count of pending reports.
     */
    public static int countPending() {
        return (int) REPORTS.stream()
            .filter(r -> "PENDING".equals(r.getStatus()))
            .count();
    }

    /**
     * Gets count of verified reports.
     */
    public static int countVerified() {
        return (int) REPORTS.stream()
            .filter(r -> "VERIFIED".equals(r.getStatus()))
            .count();
    }

    /**
     * Gets count of responded reports.
     */
    public static int countResponded() {
        return (int) REPORTS.stream()
            .filter(r -> "RESPONDED".equals(r.getStatus()))
            .count();
    }
}

