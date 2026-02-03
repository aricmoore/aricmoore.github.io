package com.example.inventoryappariellemoore;

/**
 * Represents a single inventory item with ID, name, and quantity.
 * Now compatible with DBHelper and DashboardActivity.
 */
public class InventoryItem {
    public long id;
    public String name;
    public int quantity;

    public InventoryItem(long id, String name, int quantity){
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    // Getter methods for tests, also for better encapsulation
    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString(){
        return name + " - " + quantity;
    }
}
