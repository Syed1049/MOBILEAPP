package com.example.homeappnew;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ExpenseActivity extends AppCompatActivity {

    private static final String TAG = "ExpenseActivity";
    private TableLayout expenseTable;
    private Button addButton;
    private EditText expenseNameEditText;
    private EditText expenseAmountEditText;
    private Button removeExpenseButton;
    private int selectedExpenseId = -1; // Variable to track the selected expense ID

    private int selectedRowIndex = -1; // Variable to track the selected row index

    private int getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("userId", -1);
    }

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        expenseTable = findViewById(R.id.expenseTable);
        addButton = findViewById(R.id.addExpenseButton);
        expenseNameEditText = findViewById(R.id.expenseNameEditText);
        expenseAmountEditText = findViewById(R.id.expenseAmountEditText);

        dbHelper = new DatabaseHelper(this);

        removeExpenseButton = findViewById(R.id.removeExpenseButton);
        removeExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedExpenseId != -1) {
                    // Remove the expense from the database
                    boolean isRemoved = dbHelper.removeExpense(selectedExpenseId);
                    if (isRemoved) {
                        Toast.makeText(ExpenseActivity.this, "Expense removed successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ExpenseActivity.this, "Failed to remove expense", Toast.LENGTH_SHORT).show();
                    }

                    // Clear the selection
                    clearSelection();

                    // Refresh the expense table
                    loadExpenses();
                    loadExpenseSum();
                } else {
                    Toast.makeText(ExpenseActivity.this, "No expense selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button updateExpenseButton = findViewById(R.id.updateExpenseButton);
        updateExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedExpenseId != -1) {
                    // Get the selected expense details from the database
                    Cursor cursor = dbHelper.getExpenseById(selectedExpenseId);
                    if (cursor.moveToFirst()) {
                        @SuppressLint("Range") String expenseTitle = cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_EXPENSE_TITLE));
                        @SuppressLint("Range") String expenseAmount = cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_EXPENSE_AMOUNT));

                        // Update the UI with the selected expense details
                        expenseNameEditText.setText(expenseTitle);
                        expenseAmountEditText.setText(expenseAmount);
                    }
                    cursor.close();
                } else {
                    Toast.makeText(ExpenseActivity.this, "No expense selected", Toast.LENGTH_SHORT).show();
                }
            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expenseTitle = expenseNameEditText.getText().toString();
                String expenseAmount = expenseAmountEditText.getText().toString();

                int userId = getUserId();

                // Check if expenseTitle and expenseAmount are not empty
                if (!expenseTitle.isEmpty() && !expenseAmount.isEmpty()) {
                    // Check if a row is selected
                    if (selectedRowIndex != -1) {
                        // Update the selected expense in the database
                        boolean isUpdated = dbHelper.updateExpense(selectedExpenseId, expenseTitle, expenseAmount, userId);

                        if (isUpdated) {
                            Toast.makeText(ExpenseActivity.this, "Expense updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ExpenseActivity.this, "Failed to update expense", Toast.LENGTH_SHORT).show();
                        }

                        // Clear the selection
                        clearSelection();
                    } else {
                        // Insert the new expense into the database
                        long newRowId = dbHelper.insertExpense(expenseTitle, expenseAmount, userId);

                        if (newRowId != -1) {
                            Toast.makeText(ExpenseActivity.this, "Expense added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ExpenseActivity.this, "Failed to add expense", Toast.LENGTH_SHORT).show();
                        }
                    }

                    // Refresh the expense table
                    loadExpenses();
                    loadExpenseSum();

                    // Clear the text fields
                    expenseNameEditText.setText("");
                    expenseAmountEditText.setText("");
                } else {
                    Toast.makeText(ExpenseActivity.this, "Expense title and amount cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadExpenses();
    }






    private void loadExpenses() {
        expenseTable.removeAllViews();

        int userId = getUserId();
        Cursor cursor = dbHelper.getExpensesForUser(userId);

        int idColumnIndex = cursor.getColumnIndex(dbHelper.COLUMN_EXPENSE_ID);
        int titleColumnIndex = cursor.getColumnIndex(dbHelper.COLUMN_EXPENSE_TITLE);
        int amountColumnIndex = cursor.getColumnIndex(dbHelper.COLUMN_EXPENSE_AMOUNT);

        if (idColumnIndex == -1 || titleColumnIndex == -1 || amountColumnIndex == -1) {
            // Handle the case where one or more columns are not found
            // You can show an error message or handle it according to your app's requirements
            return;
        }

        while (cursor.moveToNext()) {
            int expenseId = cursor.getInt(idColumnIndex);
            String expenseTitle = cursor.getString(titleColumnIndex);
            String expenseAmount = cursor.getString(amountColumnIndex);

            TableRow row = new TableRow(this);

            TextView titleTextView = new TextView(this);
            titleTextView.setText(expenseTitle);
            row.addView(titleTextView);

            TextView amountTextView = new TextView(this);
            amountTextView.setText(expenseAmount);
            row.addView(amountTextView);

            TextView actionTextView = new TextView(this);
            actionTextView.setText("Action");
            row.addView(actionTextView);

            // Add a unique identifier to each row to track the expense ID
            row.setTag(expenseId);

            // Add click listener for the row to select it
// Update the click listener for the row to select it
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TableRow clickedRow = (TableRow) v;
                    int expenseId = (int) clickedRow.getTag(); // Retrieve the expense ID

                    selectRow(clickedRow); // Select the row
                    selectedExpenseId = expenseId; // Store the selected expense ID

                    Log.d(TAG, "Selected Expense ID: " + selectedExpenseId); // Add this log message
                }
            });

            expenseTable.addView(row);
        }

        cursor.close();
    }



    private void selectRow(TableRow row) {
        // Clear the previous selection if any
        clearSelection();

        // Get the index of the selected row
        selectedRowIndex = expenseTable.indexOfChild(row);

        // Highlight the selected row if needed
        row.setBackgroundColor(getResources().getColor(R.color.selected_row_color));
    }

    private void clearSelection() {
        if (selectedRowIndex != -1) {
            // Get the previously selected row
            TableRow previousRow = (TableRow) expenseTable.getChildAt(selectedRowIndex);

            // Reset the background color
            previousRow.setBackgroundColor(getResources().getColor(android.R.color.transparent));

            // Clear the selected row index
            selectedRowIndex = -1;
        }
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
//                expenseSumTextView.setText(String.valueOf(expenseSum));
            }
        }

        // Close the cursor and database connection
        cursor.close();
        db.close();
    }


}
