package com.example.homeappnew;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StatisticsActivity extends Activity {

    private Spinner filterSpinner;
    private ListView statisticsListView;
    private ArrayAdapter<Expense> statisticsAdapter;
private int userId;
    private List<Expense> expenseList;

    private DatabaseHelper  dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Initialize views
        filterSpinner = findViewById(R.id.filterSpinner);
        statisticsListView = findViewById(R.id.statisticsListView);
         userId=getUserId();
        // Set up the filter spinner
        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(this,
                R.array.filter_options_array, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterAdapter);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilter(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set up the list view
        expenseList = new ArrayList<>();
        statisticsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseList);
        statisticsListView.setAdapter(statisticsAdapter);

        // Initialize DBHelper
        dbHelper = new DatabaseHelper(this);

        // Load expenses
        loadExpenses();
    }

    private void loadExpenses() {
        expenseList.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String userIdString = String.valueOf(userId);


        Cursor cursor = db.rawQuery("SELECT * FROM expenses WHERE user_id = ?", new String[]{userIdString});


        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String amount = cursor.getString(cursor.getColumnIndex("amount"));
                @SuppressLint("Range") int id= cursor.getInt(cursor.getColumnIndex( "expense_id"));
                expenseList.add(new Expense(name, amount,id));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        statisticsAdapter.notifyDataSetChanged();
    }

    private void applyFilter(int filterPosition) {
        switch (filterPosition) {
            case 0:
                // No filter
                break;
            case 1:
                // Sort expenses by high amount to low
                Collections.sort(expenseList, new Comparator<Expense>() {
                    @Override
                    public int compare(Expense expense1, Expense expense2) {
                        double amount1 = Double.parseDouble(expense1.getAmount());
                        double amount2 = Double.parseDouble(expense2.getAmount());
                        return Double.compare(amount2, amount1);
                    }
                });
                break;
            case 2:
                // Sort expenses by low amount to high
                Collections.sort(expenseList, new Comparator<Expense>() {
                    @Override
                    public int compare(Expense expense1, Expense expense2) {
                        double amount1 = Double.parseDouble(expense1.getAmount());
                        double amount2 = Double.parseDouble(expense2.getAmount());
                        return Double.compare(amount1, amount2);
                    }
                });
                break;
        }

        // Notify the adapter that the data has changed
        statisticsAdapter.notifyDataSetChanged();
    }
    private int getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("userId", -1);
    }
}
