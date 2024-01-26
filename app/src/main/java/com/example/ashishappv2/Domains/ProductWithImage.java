package com.example.ashishappv2.Domains;

import java.util.List;
import java.util.Map;

public class ProductWithImage {
    private String name;
    private String category;
    private String price;
    private String pieces;
    private String userEmail;
    private Map<String, String> images;

    public ProductWithImage(){};


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

    public Map<String, String> getImages() {
        return images;
    }

    public void setImages(Map<String, String> images) {
        this.images = images;
    }

    public ProductWithImage(String name, String category, String price, String pieces, String userEmail, Map<String, String> images) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.pieces = pieces;
        this.userEmail = userEmail;
        this.images = images;
    }
}
