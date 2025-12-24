package com.bantayalert.data;

/**
 * Data model for evacuation centers.
 */
public class EvacuationCenter {
    private final String id;
    private final String name;
    private final String address;
    private final String barangay;
    private final int capacity;
    private final String facilities; // comma-separated
    private final String contactPerson;
    private final String contactNumber;
    private final String status; // "OPEN" or "FULL"
    private int currentEvacuees;

    public EvacuationCenter(String id, String name, String address, String barangay, 
                           int capacity, String facilities, String contactPerson, 
                           String contactNumber, String status, int currentEvacuees) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.barangay = barangay;
        this.capacity = capacity;
        this.facilities = facilities;
        this.contactPerson = contactPerson;
        this.contactNumber = contactNumber;
        this.status = status;
        this.currentEvacuees = currentEvacuees;
    }

    public EvacuationCenter(String name, String address, String barangay, 
                           int capacity, String facilities, String contactPerson, 
                           String contactNumber) {
        this.id = java.util.UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
        this.barangay = barangay;
        this.capacity = capacity;
        this.facilities = facilities;
        this.contactPerson = contactPerson;
        this.contactNumber = contactNumber;
        this.status = "OPEN";
        this.currentEvacuees = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getBarangay() {
        return barangay;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getFacilities() {
        return facilities;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getStatus() {
        return status;
    }

    public int getCurrentEvacuees() {
        return currentEvacuees;
    }

    public EvacuationCenter withStatus(String newStatus) {
        return new EvacuationCenter(id, name, address, barangay, capacity, facilities, 
                                   contactPerson, contactNumber, newStatus, currentEvacuees);
    }

    public EvacuationCenter withEvacuees(int newEvacuees) {
        String newStatus = newEvacuees >= capacity ? "FULL" : "OPEN";
        return new EvacuationCenter(id, name, address, barangay, capacity, facilities, 
                                   contactPerson, contactNumber, newStatus, newEvacuees);
    }
}

