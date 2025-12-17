package com.example.inventoryappariellemoore.models;

public class Item {
    public long id;
    public String name;
    public int quantity;

    public Item(long id, String name, int qty) {
        this.id = id;
        this.name = name;
        this.quantity = qty;
    }
}
