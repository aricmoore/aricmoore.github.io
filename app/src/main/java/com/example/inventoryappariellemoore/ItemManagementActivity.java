package com.example.inventoryappariellemoore;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * A controller that coordinates user interaction and delegates
 * business logic to dedicated services.
 * Includes zero business logic, making this Activity a true controller.
 */
public class ItemManagementActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 100;

    EditText itemNameText, itemQuantityEdit;
    Button saveButton, cancelButton;

    long itemId;
    String username;

    InventoryService inventoryService;
    SmsNotificationService smsNotificationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_management);

        itemNameText = findViewById(R.id.itemNameText);
        itemQuantityEdit = findViewById(R.id.itemQuantityEdit);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);

        itemQuantityEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        itemQuantityEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});

        inventoryService = new InventoryService(this);
        smsNotificationService = new SmsNotificationService(this);

        itemId = getIntent().getLongExtra("itemId", -1);
        username = getIntent().getStringExtra("username");

        String name = getIntent().getStringExtra("itemName");
        int quantity = getIntent().getIntExtra("itemQuantity", 0);

        itemNameText.setText(name != null ? name : "");
        itemQuantityEdit.setText(String.valueOf(quantity));

        saveButton.setOnClickListener(v -> handleSave());
        cancelButton.setOnClickListener(v -> finish());

        requestSmsPermissionIfNeeded();
    }

    // Handles save action and delegates business logic to services
    private void handleSave() {
        String newName = itemNameText.getText().toString().trim();
        String qtyStr = itemQuantityEdit.getText().toString().trim();

        if (newName.isEmpty()) {
            Toast.makeText(this,
                    "Item name cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int newQty;
        try {
            newQty = Integer.parseInt(qtyStr);
        } catch (NumberFormatException e) {
            newQty = 0;
        }

        inventoryService.updateItem(itemId, newName, newQty);

        if (newQty == 0) {
            smsNotificationService.sendOutOfStockAlert(username, newName);
        }

        Intent result = new Intent();
        result.putExtra("itemId", itemId);
        result.putExtra("itemName", newName);
        result.putExtra("itemQuantity", newQty);
        setResult(RESULT_OK, result);
        finish();
    }

    // Requests SMS permission if it has not already been granted
    private void requestSmsPermissionIfNeeded() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSION_REQUEST_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this,
                        "SMS permission granted.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "SMS permission denied. Alerts disabled.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
