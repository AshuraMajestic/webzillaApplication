package com.example.ashishappv2.Domains;

public class DeliveryData {
    private String warehouseName;
    private String contactName;
    private String mobileNumber;
    private String flatNumber;
    private String area;
    private String pinCode;
    private String city;
    private String state;
    private String gstNumber;

    public DeliveryData() {
        // Default constructor required for Firebase
    }

    public DeliveryData(String warehouseName, String contactName, String mobileNumber, String flatNumber,
                        String area, String pinCode, String city, String state, String gstNumber) {
        this.warehouseName = warehouseName;
        this.contactName = contactName;
        this.mobileNumber = mobileNumber;
        this.flatNumber = flatNumber;
        this.area = area;
        this.pinCode = pinCode;
        this.city = city;
        this.state = state;
        this.gstNumber = gstNumber;
    }

    // Getters and setters
    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }
}
