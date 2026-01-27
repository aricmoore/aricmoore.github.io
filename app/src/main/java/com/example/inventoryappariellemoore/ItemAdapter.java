package com.example.inventoryappariellemoore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Enhanced ItemAdapter demonstrating algorithms and data structure skills:
 * - Uses List<InventoryItem> instead of String[][]
 * - Efficient deletion via notifyDataSetChanged()
 * - Now supports sorting and filtering
 */
public class ItemAdapter extends BaseAdapter {

    private final Context context;
    private final List<InventoryItem> items;
    private final DBHelper db;

    public ItemAdapter(Context context, List<InventoryItem> items, DBHelper db) {
        this.context = context;
        this.items = items;
        this.db = db;
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
        View row = convertView != null ? convertView :
                LayoutInflater.from(context).inflate(R.layout.inventory_row, parent, false);

        InventoryItem item = items.get(pos);

        TextView nameView = row.findViewById(R.id.itemNameText);
        TextView qtyView = row.findViewById(R.id.itemQuantityText);
        Button deleteBtn = row.findViewById(R.id.deleteButton);

        nameView.setText(item.name);
        qtyView.setText(String.valueOf(item.quantity));
        qtyView.setTextColor(item.quantity == 0 ? 0xFFFF0000 : 0xFF000000); // Red if zero

        // Removes item and notify adapter
        deleteBtn.setOnClickListener(v -> {
            db.deleteItem(item.id);
            items.remove(item);
            notifyDataSetChanged();
        });

        return row;
    }

    // Sorts items alphabetically by name
    public void sortByName() {
        Collections.sort(items, Comparator.comparing(i -> i.name.toLowerCase()));
        notifyDataSetChanged();
    }

    // Sorts items by quantity (ascending)
    public void sortByQuantity() {
        Collections.sort(items, Comparator.comparingInt(i -> i.quantity));
        notifyDataSetChanged();
    }

    // Filters items with quantity <= threshold
    public void filterLowStock(int threshold) {
        List<InventoryItem> filtered = new ArrayList<>();
        for (InventoryItem item : items) {
            if (item.quantity <= threshold) filtered.add(item);
        }
        items.clear();
        items.addAll(filtered);
        notifyDataSetChanged();
    }
}
