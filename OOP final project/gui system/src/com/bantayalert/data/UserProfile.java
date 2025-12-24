package com.bantayalert.data;

/**
 * Simple value object for user data.
 */
public class UserProfile {
    private final String fullName;
    private final String email;
    private final String phone;
    private final String location;
    private final String password;
    private final String communityStatus; // e.g. citizen, admin
    private final String memberSince;     // e.g. date string

    public UserProfile(String fullName,
                       String email,
                       String phone,
                       String location,
                       String password,
                       String communityStatus,
                       String memberSince) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.location = location;
        this.password = password;
        this.communityStatus = communityStatus;
        this.memberSince = memberSince;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return location;
    }

    public String getPassword() {
        return password;
    }

    public String getCommunityStatus() {
        return communityStatus;
    }

    public String getMemberSince() {
        return memberSince;
    }
}

