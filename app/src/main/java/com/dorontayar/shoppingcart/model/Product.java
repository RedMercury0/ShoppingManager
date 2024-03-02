package com.dorontayar.shoppingcart.model;

public class Product {
    private String name;
    private int quantity;

    public Product() {
        // Default constructor required for Firebase
    }

    public Product(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
