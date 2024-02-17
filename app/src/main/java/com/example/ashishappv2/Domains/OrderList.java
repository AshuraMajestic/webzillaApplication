package com.example.ashishappv2.Domains;

import java.util.List;

public class OrderList {
        private String accepted;
        private Cart cart;
        private String date;
        private FormData formData;
        private int orderNumber;
        private String time;
public OrderList(){};
    public OrderList(String accepted, Cart cart, String date, FormData formData, int orderNumber, String time) {
        this.accepted = accepted;
        this.cart = cart;
        this.date = date;
        this.formData = formData;
        this.orderNumber = orderNumber;
        this.time = time;
    }

    // Getters and Setters
        public String getAccepted() {
            return accepted;
        }

        public void setAccepted(String accepted) {
            this.accepted = accepted;
        }

        public Cart getCart() {
            return cart;
        }

        public void setCart(Cart cart) {
            this.cart = cart;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public FormData getFormData() {
            return formData;
        }

        public void setFormData(FormData formData) {
            this.formData = formData;
        }

        public int getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(int orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    class Cart {
        private List<Item> items;
        public Cart(){};
        public Cart(List<Item> items) {
            this.items = items;
        }

        // Getter and Setter for items
        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }
    }

    class Item {
        private String productImage;
        private String productName;
        private int productPrice;
        private int quantity;

        // Getters and Setters
        public String getProductImage() {
            return productImage;
        }

        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(int productPrice) {
            this.productPrice = productPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    class FormData {
        private String AcceptEmail;
        private String City;
        private String Email;
        private String State;
        private String ZipCode;
        private String address;
        private String country;
        private String firstName;
        private String lastName;
        private String link;
        private String number;
        private String paymentOption;
        private String saveForNextTime;

        public FormData(){};
        public FormData(String acceptEmail, String city, String email, String state, String zipCode, String address, String country, String firstName, String lastName, String link, String number, String paymentOption, String saveForNextTime) {
            AcceptEmail = acceptEmail;
            City = city;
            Email = email;
            State = state;
            ZipCode = zipCode;
            this.address = address;
            this.country = country;
            this.firstName = firstName;
            this.lastName = lastName;
            this.link = link;
            this.number = number;
            this.paymentOption = paymentOption;
            this.saveForNextTime = saveForNextTime;
        }

        // Getters and Setters
        public String getAcceptEmail() {
            return AcceptEmail;
        }

        public void setAcceptEmail(String acceptEmail) {
            AcceptEmail = acceptEmail;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String city) {
            City = city;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String email) {
            Email = email;
        }

        public String getState() {
            return State;
        }

        public void setState(String state) {
            State = state;
        }

        public String getZipCode() {
            return ZipCode;
        }

        public void setZipCode(String zipCode) {
            ZipCode = zipCode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getPaymentOption() {
            return paymentOption;
        }

        public void setPaymentOption(String paymentOption) {
            this.paymentOption = paymentOption;
        }

        public String getSaveForNextTime() {
            return saveForNextTime;
        }

        public void setSaveForNextTime(String saveForNextTime) {
            this.saveForNextTime = saveForNextTime;
        }
    }