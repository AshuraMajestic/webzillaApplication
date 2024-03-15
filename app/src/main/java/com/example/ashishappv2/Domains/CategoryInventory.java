package com.example.ashishappv2.Domains;

import java.util.List;

public class CategoryInventory {
     private String name;
private int itemCount;
    private boolean active;
    private String image;


    public CategoryInventory(){};

    public CategoryInventory(String name, int itemCount, boolean active, String image) {
        this.name = name;
        this.itemCount = itemCount;
        this.active = active;
        this.image = image;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
