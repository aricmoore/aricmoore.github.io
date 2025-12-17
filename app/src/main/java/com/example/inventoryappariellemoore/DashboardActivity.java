package com.example.inventoryappariellemoore;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    ListView inventoryGrid;
    ArrayList<InventoryItem> items;
    InventoryAdapter adapter;
    TextView welcomeText;
    EditText itemNameField, itemQuantityField;
    Button addItemButton;
    DBHelper dbHelper;
    long userId;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dbHelper = new DBHelper(this);

        welcomeText = findViewById(R.id.welcomeText);
        inventoryGrid = findViewById(R.id.inventoryGrid);
        itemNameField = findViewById(R.id.itemNameField);
        itemQuantityField = findViewById(R.id.itemQuantityField);
        addItemButton = findViewById(R.id.addItemButton);

        username = getIntent().getStringExtra("username");
        welcomeText.setText(getString(R.string.welcome_message, username));
        userId = dbHelper.getUserId(username);

        loadItems();

        adapter = new InventoryAdapter();
        inventoryGrid.setAdapter(adapter);

        // Adds inline item from the dashboard
        addItemButton.setOnClickListener(v -> {
            String name = itemNameField.getText().toString().trim();
            String qtyStr = itemQuantityField.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Item name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            int quantity = 0;
            try { quantity = Integer.parseInt(qtyStr); } catch(NumberFormatException e){ quantity = 0; }

            long id = dbHelper.insertItem(userId, name, quantity);
            items.add(new InventoryItem(id, name, quantity));
            adapter.notifyDataSetChanged();

            // Clears fields after adding (check that this is implemented on all input fields)
            itemNameField.setText("");
            itemQuantityField.setText("");

            if (quantity == 0) Toast.makeText(this, "Warning: quantity is zero", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadItems(){
        items = new ArrayList<>();
        String[][] dbItems = dbHelper.getItemsForUser(userId);
        for(String[] row : dbItems){
            items.add(new InventoryItem(Long.parseLong(row[0]), row[1], Integer.parseInt(row[2])));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout){
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Adapter for ListView
    private class InventoryAdapter extends BaseAdapter {

        @Override
        public int getCount() { return items.size(); }

        @Override
        public Object getItem(int position) { return items.get(position); }

        @Override
        public long getItemId(int position) { return items.get(position).id; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View rowView = convertView;
            if(rowView == null){
                rowView = getLayoutInflater().inflate(R.layout.inventory_row, parent, false);
            }

            TextView itemNameText = rowView.findViewById(R.id.itemNameText);
            TextView itemQuantityText = rowView.findViewById(R.id.itemQuantityText);
            Button deleteButton = rowView.findViewById(R.id.deleteButton);

            InventoryItem item = items.get(position);
            itemNameText.setText(item.name);
            itemQuantityText.setText(String.valueOf(item.quantity));
            itemQuantityText.setTextColor(item.quantity == 0 ? Color.RED : Color.BLACK);

            deleteButton.setOnClickListener(v -> {
                dbHelper.deleteItem(item.id);
                items.remove(position);
                notifyDataSetChanged();
            });

            rowView.setOnClickListener(v -> {
                // Open edit screen
                Intent intent = new Intent(DashboardActivity.this, ItemManagementActivity.class);
                intent.putExtra("itemId", item.id);
                intent.putExtra("itemName", item.name);
                intent.putExtra("itemQuantity", item.quantity);
                intent.putExtra("username", username);   // ← REQUIRED
                startActivityForResult(intent, 1);
            });

            return rowView;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            long id = data.getLongExtra("itemId", -1);
            String name = data.getStringExtra("itemName");
            int quantity = data.getIntExtra("itemQuantity", 0);

            for(int i=0;i<items.size();i++){
                if(items.get(i).id == id){
                    items.get(i).name = name;
                    items.get(i).quantity = quantity;
                    adapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }
}
