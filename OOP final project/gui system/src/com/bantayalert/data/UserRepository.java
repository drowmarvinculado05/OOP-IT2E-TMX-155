package com.bantayalert.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory store for demo users.
 */
public class UserRepository {
    private static final Map<String, UserProfile> USERS = new HashMap<>();

    static {
        save(new UserProfile(
            "Admin User",
            "admin@gmail.com",
            "09123456789",
            "Central (City Proper/Poblacion)",
            "12345",
            "admin",
            "2024-01-01"
        ));
    }

    private UserRepository() {
    }

    public static void save(UserProfile profile) {
        USERS.put(profile.getEmail().toLowerCase(), profile);
    }

    public static Optional<UserProfile> authenticate(String email, String password) {
        if (email == null || password == null) {
            return Optional.empty();
        }
        UserProfile profile = USERS.get(email.toLowerCase());
        if (profile != null && password.equals(profile.getPassword())) {
            return Optional.of(profile);
        }
        return Optional.empty();
    }

    public static Map<String, UserProfile> snapshot() {
        return Collections.unmodifiableMap(USERS);
    }

    /**
     * Updates an existing user profile with new information.
     * Creates a new UserProfile with updated values and saves it.
     */
    public static void updateProfile(String email, String newFullName, String newPhone, String newLocation) {
        UserProfile existing = USERS.get(email.toLowerCase());
        if (existing != null) {
            UserProfile updated = new UserProfile(
                newFullName,
                existing.getEmail(),
                newPhone,
                newLocation,
                existing.getPassword(),
                existing.getCommunityStatus(),
                existing.getMemberSince()
            );
            USERS.put(email.toLowerCase(), updated);
        }
    }

    /**
     * Gets a user profile by email.
     */
    public static Optional<UserProfile> findByEmail(String email) {
        return Optional.ofNullable(USERS.get(email.toLowerCase()));
    }
}

