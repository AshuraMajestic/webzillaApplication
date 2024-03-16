package com.example.ashishappv2.Domains;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Map;

public class ProductInventory {

    private String name;
    private String category;
    private String price;
    private String DiscountedPrice;
    private String unit;
    private String per;

    private boolean available;
    private Map<String, String> images;
    private List<Sizes> sizes;
    private List<String> color;


    public ProductInventory(){};

    public ProductInventory(String name, String category, String price, String discountedPrice, String unit, String per, boolean available, Map<String, String> images, List<Sizes> sizes, List<String> color) {
        this.name = name;
        this.category = category;
        this.price = price;
        DiscountedPrice = discountedPrice;
        this.unit = unit;
        this.per = per;
        this.available = available;
        this.images = images;
        this.sizes = sizes;
        this.color = color;
    }

    public String getDiscountedPrice() {
        return DiscountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        DiscountedPrice = discountedPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPer() {
        return per;
    }

    public void setPer(String per) {
        this.per = per;
    }

    public List<String> getColor() {
        return color;
    }

    public void setColor(List<String> color) {
        this.color = color;
    }

    public List<Sizes> getSizes() {
        return sizes;
    }

    public void setSizes(List<Sizes> sizes) {
        this.sizes = sizes;
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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Map<String, String> getImages() {
        return images;
    }

    public void setImages(Map<String, String> images) {
        this.images = images;
    }
}
class Sizes implements Parcelable {
    private String size;
    private long price;
    private long sellingPrice;

    public Sizes() {}

    public Sizes(String size, long price, long sellingPrice) {
        this.size = size;
        this.price = price;
        this.sellingPrice = sellingPrice;
    }

    protected Sizes(Parcel in) {
        size = in.readString();
        price = in.readLong();
        sellingPrice = in.readLong();
    }

    public static final Creator<Sizes> CREATOR = new Creator<Sizes>() {
        @Override
        public Sizes createFromParcel(Parcel in) {
            return new Sizes(in);
        }

        @Override
        public Sizes[] newArray(int size) {
            return new Sizes[size];
        }
    };

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(long sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(size);
        dest.writeLong(price);
        dest.writeLong(sellingPrice);
    }
}