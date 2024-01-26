package com.example.ashishappv2.Domains;

public class UpiDetails {
    public String upiId;
    public String email; // New field to store the user's email

    public UpiDetails() {
        // Default constructor required for DataSnapshot.getValue(UpiDetails.class)
    }

    public UpiDetails(String upiId, String email) {
        this.upiId = upiId;
        this.email = email;
    }
}
