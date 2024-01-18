package com.example.ashishappv2.Domains;


public class Product {
    private String name;
    private String category;
    private String price;
    private String pieces;
    private String userEmail;
    private String videoUrl;


    public Product() {
        // Default constructor required for Firebase
    }

    public Product(String name, String category, String price, String pieces, String userEmail, String videoUrl) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.pieces = pieces;
        this.userEmail = userEmail;
        this.videoUrl = videoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPieces() {
        return pieces;
    }

    public void setPieces(String pieces) {
        this.pieces = pieces;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
