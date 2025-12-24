package com.bantayalert.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository for managing evacuation centers.
 */
public class EvacuationCenterRepository {
    private static final List<EvacuationCenter> CENTERS = new ArrayList<>();

    private EvacuationCenterRepository() {
    }

    /**
     * Saves a new center or updates an existing one.
     */
    public static void save(EvacuationCenter center) {
        // Remove existing center with same ID if present
        CENTERS.removeIf(c -> c.getId().equals(center.getId()));
        CENTERS.add(center);
    }

    /**
     * Gets all centers.
     */
    public static List<EvacuationCenter> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(CENTERS));
    }

    /**
     * Gets all open centers.
     */
    public static List<EvacuationCenter> findOpen() {
        return CENTERS.stream()
            .filter(c -> "OPEN".equals(c.getStatus()))
            .collect(Collectors.toList());
    }

    /**
     * Gets all full centers.
     */
    public static List<EvacuationCenter> findFull() {
        return CENTERS.stream()
            .filter(c -> "FULL".equals(c.getStatus()))
            .collect(Collectors.toList());
    }


    /**
     * Deletes a center by ID.
     */
    public static boolean delete(String id) {
        return CENTERS.removeIf(c -> c.getId().equals(id));
    }

    /**
     * Gets count of open centers.
     */
    public static int countOpen() {
        return (int) CENTERS.stream()
            .filter(c -> "OPEN".equals(c.getStatus()))
            .count();
    }

    /**
     * Gets count of full centers.
     */
    public static int countFull() {
        return (int) CENTERS.stream()
            .filter(c -> "FULL".equals(c.getStatus()))
            .count();
    }

    /**
     * Gets total evacuees across all centers.
     */
    public static int totalEvacuees() {
        return CENTERS.stream()
            .mapToInt(EvacuationCenter::getCurrentEvacuees)
            .sum();
    }

    /**
     * Gets total capacity across all centers.
     */
    public static int totalCapacity() {
        return CENTERS.stream()
            .mapToInt(EvacuationCenter::getCapacity)
            .sum();
    }
}

