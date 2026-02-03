package com.example.inventoryappariellemoore;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Updated for Enhancement 3:
    * Adapter for displaying inventory items in a ListView or GridView.
    * Updated to support enhanced DBHelper methods for filtering, searching,
    * and transactional integrity.
    * This adapter now works with InventoryItem objects instead of String arrays,
    * simplifying interaction with the rest of the app and the enhanced DBHelper methods.
 */
public class ItemAdapter extends BaseAdapter {

    private final Context context;
    private List<InventoryItem> items; // Store InventoryItem objects for type safety
    private final DBHelper db;
    private long userId;

    /**
     * Constructor
     * @param context activity context
     * @param items initial list of InventoryItem objects
     * @param db reference to DBHelper
     */
    public ItemAdapter(Context context, List<InventoryItem> items, DBHelper db) {
        this.context = context;
        this.items = new ArrayList<>(items); // Uses a mutable list
        this.db = db;
    }

    public void setUserId(long id) {
        this.userId = id;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).id;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View row = (convertView == null) ?
                LayoutInflater.from(context).inflate(R.layout.inventory_row, parent, false) :
                convertView;

        InventoryItem item = items.get(pos);

        TextView nameView = row.findViewById(R.id.itemNameText);
        TextView qtyView = row.findViewById(R.id.itemQuantityText);
        Button deleteBtn = row.findViewById(R.id.deleteButton);

        // Displays item info
        nameView.setText(item.name);
        qtyView.setText(String.valueOf(item.quantity));
        // Highlights items with zero quantity
        qtyView.setTextColor(item.quantity == 0 ? Color.RED : Color.BLACK);

        // Delete button: removes item from DB and update adapter
        deleteBtn.setOnClickListener(v -> {
            db.deleteItem(item.id);  // Ensures DB remains consistent
            items.remove(pos);       // Removes from UI immediately
            notifyDataSetChanged();  // Refreshes ListView/GridView
        });

        return row;
    }

    /**
     * Refreshes the adapter with the full dataset for the current user.
     * Converts DBHelper's String[][] result to InventoryItem objects.
     */
    public void refreshItems() {
        // Pulls all items for user, sorted by name
        items = db.getItemsForUser(userId);  // now returns List<InventoryItem>
        notifyDataSetChanged();
    }

    /**
     * Filters items by a quantity range.
     * Uses DBHelper's enhanced getItemsByQuantityRange method.
//     * @param min minimum quantity
//     * @param max maximum quantity
     */
    public void filterByQuantity(int min, int max) {
        // Directly assigns the filtered list from DBHelper
        items = db.getItemsByQuantityRange(userId, min, max);
        notifyDataSetChanged(); // Updates UI to show filtered results
    }

    /**
     * Search items by name substring.
     * Uses DBHelper's enhanced searchItemsByName method.
     * @param query substring to match
     */
    public void searchByName(String query) {
        items = db.searchItemsByName(userId, query);
        notifyDataSetChanged(); // Updates UI to show search results
    }
}
