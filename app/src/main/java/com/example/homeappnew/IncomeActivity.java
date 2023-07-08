package com.example.homeappnew;//package com.example.homeappnew;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
////public class IncomeActivity extends AppCompatActivity {
////
////    private EditText incomeNameEditText;
////    private EditText incomeAmountEditText;
////    private Button addIncomeButton;
////
////    private DatabaseHelper dbHelper;
////    private int userId;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_income);
////
////        // Initialize views
////        incomeNameEditText = findViewById(R.id.incomeNameEditText);
////        incomeAmountEditText = findViewById(R.id.incomeAmountEditText);
////        addIncomeButton = findViewById(R.id.addIncomeButton);
////
////        // Create instance of DatabaseHelper
////        dbHelper = new DatabaseHelper(this);
////
////        // Get user ID from SharedPreferences
////        userId = getUserId();
////
////        // Set click listener for add button
////        addIncomeButton.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                addIncome();
////            }
////        });
////    }
////
////    private void addIncome() {
////        // Retrieve name and amount
////        String name = incomeNameEditText.getText().toString().trim();
////        String amount = incomeAmountEditText.getText().toString().trim();
////
////        // Validate input
////        if (name.isEmpty() || amount.isEmpty()) {
////            Toast.makeText(this, "Please enter name and amount", Toast.LENGTH_SHORT).show();
////            return;
////        }
////
////        // Insert income into database
////        long newRowId = dbHelper.insertIncome(name, amount, userId);
////
////        if (newRowId != -1) {
////            Toast.makeText(this, "Income added successfully", Toast.LENGTH_SHORT).show();
////            // Clear input fields
////            incomeNameEditText.getText().clear();
////            incomeAmountEditText.getText().clear();
////        } else {
////            Toast.makeText(this, "Failed to add income", Toast.LENGTH_SHORT).show();
////        }
////    }
////
////    private int getUserId() {
////        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
////        return sharedPreferences.getInt("userId", -1);
////    }
////}
//
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class IncomeActivity extends AppCompatActivity {

    private EditText incomeNameEditText;
    private EditText incomeAmountEditText;
    private Button addIncomeButton;
    private ListView incomeListView;

    private DatabaseHelper dbHelper;
    private int userId;

    private List<String> incomeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        // Initialize views
        incomeNameEditText = findViewById(R.id.incomeNameEditText);
        incomeAmountEditText = findViewById(R.id.incomeAmountEditText);
        addIncomeButton = findViewById(R.id.addIncomeButton);
        incomeListView = findViewById(R.id.incomeListView);

        // Create instance of DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Get user ID from SharedPreferences
        userId = getUserId();

        // Initialize incomeList
        incomeList = new ArrayList<>();

        // Set click listener for add button
        addIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIncome();
            }

        });
        refreshIncomeListView();
    }

    private void addIncome() {
        // Retrieve name and amount
        String name = incomeNameEditText.getText().toString().trim();
        String amount = incomeAmountEditText.getText().toString().trim();

        // Validate input
        if (name.isEmpty() || amount.isEmpty()) {
            Toast.makeText(this, "Please enter name and amount", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert income into database
        long newRowId = dbHelper.insertIncome(name, amount, userId);

        if (newRowId != -1) {
            Toast.makeText(this, "Income added successfully", Toast.LENGTH_SHORT).show();
            // Add income to the list
            incomeList.add(name + " - $" + amount);
            // Refresh the ListView
            refreshIncomeListView();
            // Clear input fields
            incomeNameEditText.getText().clear();
            incomeAmountEditText.getText().clear();
        } else {
            Toast.makeText(this, "Failed to add income", Toast.LENGTH_SHORT).show();
        }
    }

    private int getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("userId", -1);
    }

    private void refreshIncomeListView() {
        // Fetch incomes from the database
        List<Income> incomes = dbHelper.getIncomesByUserId(userId);

        // Clear the existing income list
        incomeList.clear();

        // Add the fetched incomes to the incomeList
        for (Income income : incomes) {
            String name = income.getName();
            String amount = income.getAmount();
            incomeList.add(name + " - $" + amount);
        }

        // Create an array of income strings
        String[] incomeArray = new String[incomeList.size()];
        incomeArray = incomeList.toArray(incomeArray);

        // Set the array as the data source for the ListView
        incomeListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, incomeArray));
    }

}
