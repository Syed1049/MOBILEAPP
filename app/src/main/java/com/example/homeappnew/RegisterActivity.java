package com.example.homeappnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_register);

        // Initialize UI components
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);

        // Set up click listener for the register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve name, email, and password
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Create a new instance of DatabaseHelper
                DatabaseHelper dbHelper = new DatabaseHelper(RegisterActivity.this);

                // Get a writable database
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                // Create a ContentValues object to store the user data
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_USER_NAME, name);
                values.put(DatabaseHelper.COLUMN_USER_EMAIL, email);
                values.put(DatabaseHelper.COLUMN_USER_PASSWORD, password);

                // Insert the user data into the database
                long newRowId = db.insert(DatabaseHelper.TABLE_USERS, null, values);

                // Close the database connection
                db.close();

                // Check if the user data was inserted successfully
                if (newRowId != -1) {
                    // Registration successful, navigate to the home activity
                    // Save user ID to shared preferences
// Save user ID to shared preferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("userId", (int) newRowId);
                    editor.apply();


                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                }
            }
        });
    }
}
