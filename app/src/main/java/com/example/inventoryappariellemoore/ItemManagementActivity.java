package com.example.inventoryappariellemoore;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

// Adds SMS alert when quantity becomes 0 (only if user granted SMS permission)
public class ItemManagementActivity extends AppCompatActivity {

    EditText itemNameText, itemQuantityEdit;
    Button saveButton, cancelButton;
    long itemId;
    DBHelper dbHelper;

    String username;  // Needed to check SMS permission

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_management);

        itemNameText = findViewById(R.id.itemNameText);
        itemQuantityEdit = findViewById(R.id.itemQuantityEdit);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);

        itemQuantityEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        itemQuantityEdit.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(6) });

        dbHelper = new DBHelper(this);

        itemId = getIntent().getLongExtra("itemId", -1);
        String name = getIntent().getStringExtra("itemName");
        int quantity = getIntent().getIntExtra("itemQuantity", 0);
        username = getIntent().getStringExtra("username"); // Passed from previous screen

        itemNameText.setText(name != null ? name : "");
        itemQuantityEdit.setText(String.valueOf(quantity));

        saveButton.setOnClickListener(v -> {

            String newName = itemNameText.getText().toString().trim();
            String qtyStr = itemQuantityEdit.getText().toString().trim();

            if (newName.isEmpty()) {
                Toast.makeText(this, "Item name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            int newQty;
            try {
                newQty = Integer.parseInt(qtyStr);
            } catch (NumberFormatException e) {
                newQty = 0;
            }

            // Updates DB first
            dbHelper.updateItem(itemId, newName, newQty);

            // SMS Trigger: quantity = 0
            if (newQty == 0) {
                handleZeroQuantitySMS(newName);
            }

            Intent result = new Intent();
            result.putExtra("itemId", itemId);
            result.putExtra("itemName", newName);
            result.putExtra("itemQuantity", newQty);
            setResult(RESULT_OK, result);
            finish();
        });

        cancelButton.setOnClickListener(v -> finish());

        // Requests SMS permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.SEND_SMS},
                    100
            );
        }
    }

    // Sends SMS alert when item quantity becomes 0 (only if user allowed SMS permission)
    private void handleZeroQuantitySMS(String itemName) {

        // Checks if user allowed SMS in database
        boolean smsAllowed = dbHelper.isSmsAllowed(username);

        if (!smsAllowed) {
            Toast.makeText(this, "SMS alerts are disabled for this user.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Checks Android permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "SMS permission denied. Cannot send alert.", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(
                    "5551234567",  // Dummy number for now
                    null,
                    "Alert: '" + itemName + "' is now OUT OF STOCK.",  // ACK
                    null,
                    null
            );

            Toast.makeText(this, "SMS alert sent.", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS permission granted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "SMS permission denied. Cannot send alert.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
