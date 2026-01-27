package com.example.inventoryappariellemoore;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * DashboardActivity using the newly-enhanced ItemAdapter
 * Now supports adding, deleting, editing, sorting, and filtering inventory items
 */
public class DashboardActivity extends AppCompatActivity {

    private ListView inventoryGrid;
    private List<InventoryItem> items;
    private ItemAdapter adapter;
    private TextView welcomeText;
    private EditText itemNameField, itemQuantityField;
    private Button addItemButton;
    private DBHelper dbHelper;
    private long userId;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dbHelper = new DBHelper(this);

        // Binds UI elements
        welcomeText = findViewById(R.id.welcomeText);
        inventoryGrid = findViewById(R.id.inventoryGrid);
        itemNameField = findViewById(R.id.itemNameField);
        itemQuantityField = findViewById(R.id.itemQuantityField);
        addItemButton = findViewById(R.id.addItemButton);

        username = getIntent().getStringExtra("username");
        welcomeText.setText(getString(R.string.welcome_message, username));
        userId = dbHelper.getUserId(username);

        // Loads items from DB
        loadItems();

        // Initialises adapter
        adapter = new ItemAdapter(this, items, dbHelper);
        inventoryGrid.setAdapter(adapter);

        // Adds a new item
        addItemButton.setOnClickListener(v -> addItem());

        // Long-press menu for sorting/filtering
        inventoryGrid.setOnItemLongClickListener((parent, view, position, id) -> {
            showSortFilterMenu();
            return true;
        });
    }

    private void loadItems() {
        items = new ArrayList<>();
        String[][] dbItems = dbHelper.getItemsForUser(userId);
        for (String[] row : dbItems) {
            items.add(new InventoryItem(
                    Long.parseLong(row[0]),
                    row[1],
                    Integer.parseInt(row[2])
            ));
        }
    }

    private void addItem() {
        String name = itemNameField.getText().toString().trim();
        String qtyStr = itemQuantityField.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Item name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity = 0;
        try { quantity = Integer.parseInt(qtyStr); } catch(NumberFormatException ignored) {}

        long id = dbHelper.insertItem(userId, name, quantity);
        items.add(new InventoryItem(id, name, quantity));
        adapter.notifyDataSetChanged();

        itemNameField.setText("");
        itemQuantityField.setText("");

        if (quantity == 0) Toast.makeText(this, "Warning: quantity is zero", Toast.LENGTH_SHORT).show();
    }

    private void showSortFilterMenu() {
        PopupMenu popup = new PopupMenu(this, inventoryGrid);
        popup.getMenuInflater().inflate(R.menu.sort_filter_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.sort_name) {
                adapter.sortByName();
            } else if (id == R.id.sort_quantity) {
                adapter.sortByQuantity();
            } else if (id == R.id.filter_low_stock) {
                adapter.filterLowStock(5); // example threshold
            } else if (id == R.id.show_all) {
                loadItems();
                adapter.notifyDataSetChanged();
            } else {
                return false;
            }
            return true;
        });

        popup.show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            long id = data.getLongExtra("itemId", -1);
            String name = data.getStringExtra("itemName");
            int quantity = data.getIntExtra("itemQuantity", 0);

            for (InventoryItem item : items) {
                if(item.id == id){
                    item.name = name;
                    item.quantity = quantity;
                    adapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }
}
