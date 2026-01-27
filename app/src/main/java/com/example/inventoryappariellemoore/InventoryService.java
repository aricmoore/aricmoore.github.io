package com.example.inventoryappariellemoore;

import android.content.Context;

/**
 * Handles inventory-related business logic.
 * This service abstracts database operations away from the UI layer,
 * improving separation of concerns and testability.
 */
public class InventoryService {

    private final DBHelper dbHelper;

    public InventoryService(Context context) {
        this.dbHelper = new DBHelper(context);
    }

    /**
     * Updates an inventory item in the database.
     * Additional validation, logging, or auditing could be added here later
     * without impacting the Activity itself.
     * Clean service boundary and zero Android UI dependencies.
     */
    public void updateItem(long itemId, String name, int quantity) {
        dbHelper.updateItem(itemId, name, quantity);
    }
}
