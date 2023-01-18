package com.example.r_gameshopapp;

public class Item {
    String itemListName;
    int itemListStock;
    String itemListCategory;
    double itemListPrice;

    public Item(String itemName, int itemStock, String itemCategory, double itemPrice) {
        this.itemListName = itemName;
        this.itemListStock = itemStock;
        this.itemListCategory = itemCategory;
        this.itemListPrice = itemPrice;
    }

    public String getitemName() {
        return itemListName;
    }

    public int getitemStock() {
        return itemListStock;
    }

    public String getitemCategory() {
        return itemListCategory;
    }

    public double getitemPrice() {
        return itemListPrice;
    }
}
