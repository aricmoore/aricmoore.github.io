package com.example.inventoryappariellemoore;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Creates new user using DBHelper, username must be unique
public class RegisterActivity extends AppCompatActivity {
    private EditText usernameInput, passwordInput;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DBHelper(this);

        usernameInput = findViewById(R.id.registerUsernameEditText);
        passwordInput = findViewById(R.id.registerPasswordEditText);

        Button signupBtn = findViewById(R.id.registerButton);

        signupBtn.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String user = usernameInput.getText().toString().trim();
        String pass = passwordInput.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean created = db.createUser(user, pass);
        if (created) {
            Toast.makeText(this, "User created!", Toast.LENGTH_SHORT).show();
            finish(); // returns to Login
        } else {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
        }
    }
}
