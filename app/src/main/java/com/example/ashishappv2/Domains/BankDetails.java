package com.example.ashishappv2.Domains;

public class BankDetails {
    public String accountNumber;
    public String ifscCode;
    public String userName;
    public String email; // New field to store the user's email

    public BankDetails() {
        // Default constructor required for DataSnapshot.getValue(BankDetails.class)
    }

    public BankDetails(String accountNumber, String ifscCode, String userName, String email) {
        this.accountNumber = accountNumber;
        this.ifscCode = ifscCode;
        this.userName = userName;
        this.email = email;
    }
}
