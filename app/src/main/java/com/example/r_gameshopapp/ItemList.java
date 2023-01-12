package com.example.r_gameshopapp;

import java.io.Serializable;
import java.util.List;

public class ItemList implements Serializable {
    private List<Item> itemList;

    public ItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public ItemList() {
    }

    public List<Item> getItemList() {
        return itemList;
    }
}
