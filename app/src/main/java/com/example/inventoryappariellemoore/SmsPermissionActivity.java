package com.example.inventoryappariellemoore;

import com.example.inventoryappariellemoore.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SmsPermissionActivity extends AppCompatActivity {
    TextView permissionText;
    Button allowButton, denyButton;
    DBHelper dbHelper;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_permission);

        dbHelper = new DBHelper(this);
        username = getIntent().getStringExtra("username");

        permissionText = findViewById(R.id.permissionText);
        allowButton = findViewById(R.id.allowButton);
        denyButton = findViewById(R.id.denyButton);

        allowButton.setOnClickListener(v -> {
            dbHelper.setSmsAllowedForUser(username, true);
            Toast.makeText(this, getString(R.string.permission_granted), Toast.LENGTH_SHORT).show();
            goToDashboard();
        });

        denyButton.setOnClickListener(v -> {
            dbHelper.setSmsAllowedForUser(username, false);
            Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            goToDashboard();
        });
    }

    private void goToDashboard() {
        Intent intent = new Intent(SmsPermissionActivity.this, DashboardActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}
