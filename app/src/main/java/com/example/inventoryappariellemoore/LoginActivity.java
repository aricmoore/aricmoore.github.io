package com.example.inventoryappariellemoore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText usernameField, passwordField;
    Button loginButton, createAccountButton;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DBHelper(this);

        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);
        createAccountButton = findViewById(R.id.createAccountButton);

        loginButton.setOnClickListener(v -> {
            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if(username.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            if(dbHelper.checkUserCredentials(username, password)){
                goToSmsPermission(username);
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });

        createAccountButton.setOnClickListener(v -> {
            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if(username.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Enter both username and password to create an account", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean created = dbHelper.createUser(username, password);
            if(created){
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();
                // Clears fields after successful creation
                usernameField.setText("");
                passwordField.setText("");
                goToSmsPermission(username);
            } else {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToSmsPermission(String username){
        Intent intent = new Intent(LoginActivity.this, SmsPermissionActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
