package com.bantayalert.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository for managing emergency alerts.
 */
public class AlertRepository {
    private static final List<Alert> ALERTS = new ArrayList<>();

    private AlertRepository() {
    }

    /**
     * Saves a new alert or updates an existing one.
     */
    public static void save(Alert alert) {
        // Remove existing alert with same ID if present
        ALERTS.removeIf(a -> a.getId().equals(alert.getId()));
        ALERTS.add(alert);
    }

    /**
     * Gets all alerts.
     */
    public static List<Alert> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(ALERTS));
    }

    /**
     * Gets all active alerts.
     */
    public static List<Alert> findActive() {
        return ALERTS.stream()
            .filter(a -> "ACTIVE".equals(a.getStatus()))
            .collect(Collectors.toList());
    }

    /**
     * Gets alerts by type.
     */
    public static List<Alert> findByType(String type) {
        return ALERTS.stream()
            .filter(a -> type.equalsIgnoreCase(a.getType()))
            .collect(Collectors.toList());
    }


    /**
     * Deletes an alert by ID.
     */
    public static boolean delete(String id) {
        return ALERTS.removeIf(a -> a.getId().equals(id));
    }

    /**
     * Gets count of active alerts.
     */
    public static int countActive() {
        return (int) ALERTS.stream()
            .filter(a -> "ACTIVE".equals(a.getStatus()))
            .count();
    }

    /**
     * Gets count of critical alerts.
     */
    public static int countCritical() {
        return (int) ALERTS.stream()
            .filter(a -> "ACTIVE".equals(a.getStatus()) && "critical".equalsIgnoreCase(a.getSeverity()))
            .count();
    }

    /**
     * Gets count of disaster alerts.
     */
    public static int countDisasters() {
        return (int) ALERTS.stream()
            .filter(a -> "disaster".equalsIgnoreCase(a.getType()))
            .count();
    }
}

