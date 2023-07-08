package com.example.homeappnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DashboardActivity extends AppCompatActivity {
    private TextView expenseSumTextView;
    private TextView incomeSumTextView;
private TextView goalsinProgressTextView;
    private Button expenseButton;
    private Button incomeButton;
    private Button goalButton;
    private Button statsButton;
    private Button signOutButton;
    private Button refreshButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        expenseSumTextView = findViewById(R.id.expenseSumTextView);
        incomeSumTextView = findViewById(R.id.incomeSumTextView);
        goalsinProgressTextView=findViewById(R.id.goalsInProgressTextView);

        // Initialize UI components
        expenseButton = findViewById(R.id.expenseButton);
        incomeButton = findViewById(R.id.incomeButton);
        goalButton = findViewById(R.id.goalButton);
        statsButton = findViewById(R.id.statsButton);
        signOutButton = findViewById(R.id.signOutButton);
        refreshButton=findViewById(R.id.refreshButton);
        // Initialize database helper
        dbHelper = new DatabaseHelper(this);
        loadExpenseSum();
        loadIncomeSum();
        // Set up click listeners for the buttons

        expenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the expense activity
                Intent intent = new Intent(DashboardActivity.this, ExpenseActivity.class);
                startActivity(intent);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           loadExpenseSum();
           loadIncomeSum();
           loadTotalNumberOfGoals();
            }
        });



        incomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the expense activity
                Intent intent = new Intent(DashboardActivity.this, IncomeActivity.class);

                startActivity(intent);
            }
        });

        goalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the expense activity
                Intent intent = new Intent(DashboardActivity.this, GoalActivity.class);
                startActivity(intent);
            }
        });



        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the expense activity
                Intent intent = new Intent(DashboardActivity.this, StatisticsActivity.class);
                startActivity(intent);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform sign out logic here

                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });



        // Load and display the expense sum on the dashboard

    }


    public void loadExpenseSum() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Get the user ID of the logged-in user
        int userId = dbHelper.getUserId();

        // Get a readable database instance
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define the columns to retrieve and the table to query
        String[] projection = {"SUM(" + DatabaseHelper.COLUMN_EXPENSE_AMOUNT + ") AS total"};
        String tableName = DatabaseHelper.TABLE_EXPENSES;

        // Define the WHERE clause to filter expenses for the logged-in user
        String selection = DatabaseHelper.COLUMN_EXPENSE_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };

        // Perform the query to calculate the expense sum for the user
        Cursor cursor = db.query(tableName, projection, selection, selectionArgs, null, null, null);

        // Check if the cursor has data
        if (cursor.moveToFirst()) {
            // Retrieve the expense sum column index
            int columnIndex = cursor.getColumnIndex("total");

            // Check if the column index is valid
            if (columnIndex != -1) {
                // Retrieve the expense sum from the cursor
                double expenseSum = cursor.getDouble(columnIndex);

                // Update the expense sum text view on the dashboard
                expenseSumTextView.setText(String.valueOf(expenseSum));
            }
        }

        // Close the cursor and database connection
        cursor.close();
        db.close();
    }

    public void loadIncomeSum() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Get the user ID of the logged-in user
        int userId = getUserId();

        // Get a readable database instance
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define the columns to retrieve and the table to query
        String[] projection = {"SUM(" + DatabaseHelper.COLUMN_INCOME_AMOUNT + ") AS total"};
        String tableName = DatabaseHelper.TABLE_INCOMES;

        // Define the WHERE clause to filter incomes for the logged-in user
        String selection = DatabaseHelper.COLUMN_INCOME_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };

        // Perform the query to calculate the income sum for the user
        Cursor cursor = db.query(tableName, projection, selection, selectionArgs, null, null, null);

        // Check if the cursor has data
        if (cursor.moveToFirst()) {
            // Retrieve the income sum column index
            int columnIndex = cursor.getColumnIndex("total");

            // Check if the column index is valid
            if (columnIndex != -1) {
                // Retrieve the income sum from the cursor
                double incomeSum = cursor.getDouble(columnIndex);

                // Update the income sum text view on the dashboard
                incomeSumTextView.setText(String.valueOf(incomeSum));
            }
        }

        // Close the cursor and database connection
        cursor.close();
        db.close();
    }

    public void loadTotalNumberOfGoals() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Get the user ID of the logged-in user
        int userId = dbHelper.getUserId();

        // Get a readable database instance
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define the columns to retrieve and the table to query
        String[] projection = {"COUNT(" + DatabaseHelper.COLUMN_GOAL_ID + ") AS total"};
        String tableName = DatabaseHelper.TABLE_GOALS;

        // Define the WHERE clause to filter goals for the logged-in user
        String selection = DatabaseHelper.COLUMN_GOAL_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };

        // Perform the query to calculate the total number of goals for the user
        Cursor cursor = db.query(tableName, projection, selection, selectionArgs, null, null, null);

        // Check if the cursor has data
        if (cursor.moveToFirst()) {
            // Retrieve the total number of goals column index
            int columnIndex = cursor.getColumnIndex("total");

            // Check if the column index is valid
            if (columnIndex != -1) {
                // Retrieve the total number of goals from the cursor
                int totalGoals = cursor.getInt(columnIndex);

                // Update the total number of goals text view on the dashboard
                goalsinProgressTextView.setText(String.valueOf(totalGoals));
            }
        }

        // Close the cursor and database connection
        cursor.close();
        db.close();
    }


    private int getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("userId", -1);
    }


}

