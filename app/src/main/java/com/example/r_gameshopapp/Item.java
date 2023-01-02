package com.example.r_gameshopapp;

public class Item {
    int itemListId;
    String itemListName;
    int itemListStock;
    String itemListCategory;
    double itemListPrice;
    int itemListImage;

    public Item(int itemId, String itemName, int itemStock, String itemCategory, double itemPrice, int itemImage) {
        this.itemListId = itemId;
        this.itemListName = itemName;
        this.itemListStock = itemStock;
        this.itemListCategory = itemCategory;
        this.itemListPrice = itemPrice;
        this.itemListImage = itemImage;
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
    public int getitemImage() {
        return itemListImage;
    }
}
