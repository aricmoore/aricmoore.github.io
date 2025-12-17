package com.example.inventoryappariellemoore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter {

    private final Context context;
    private final String[][] items;
    private final DBHelper db;
    private long userId;

    public ItemAdapter(Context context, String[][] items, DBHelper db) {
        this.context = context;
        this.items = items;
        this.db = db;
    }

    public void setUserId(long id) {
        this.userId = id;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(items[position][0]); // item id
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View row;
        if (convertView == null) {
            row = LayoutInflater.from(context).inflate(R.layout.inventory_row, parent, false);
        } else {
            row = convertView;
        }

        String id = items[pos][0];
        String name = items[pos][1];
        String qty = items[pos][2];

        TextView nameView = row.findViewById(R.id.itemNameText);
        TextView qtyView = row.findViewById(R.id.itemQuantityText);
        Button deleteBtn = row.findViewById(R.id.deleteButton);

        nameView.setText(name);
        qtyView.setText(qty);

        // Delete button inside each row
        deleteBtn.setOnClickListener(v -> {
            db.deleteItem(Long.parseLong(id));
            // Very simple refresh approach: reload InventoryActivity
            ((DashboardActivity) context).recreate();
        });

        return row;
    }
}
