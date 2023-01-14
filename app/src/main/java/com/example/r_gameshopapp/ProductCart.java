package com.example.r_gameshopapp;

public class ProductCart {
    private String Name;
    private int Quantity;
    private double price;

    public ProductCart(String name, int quantity, double price) {
        Name = name;
        Quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
