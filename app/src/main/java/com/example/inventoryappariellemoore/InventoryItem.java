package com.example.inventoryappariellemoore;

public class InventoryItem {
    public long id;
    public String name;
    public int quantity;

    public InventoryItem(long id, String name, int quantity){
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public String toString(){
        return name + " - " + quantity;
    }
}
