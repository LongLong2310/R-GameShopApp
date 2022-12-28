package com.example.r_gameshopapp;

public class Item {

    String itemListName;
    int itemListImage;

    public Item(String itemName,int itemImage)
    {
        this.itemListImage = itemImage;
        this.itemListName = itemName;
    }
    public String getitemName() {
        return itemListName;
    }
    public int getitemImage() {
        return itemListImage;
    }
}
