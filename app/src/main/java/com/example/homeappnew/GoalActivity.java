package com.example.homeappnew;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GoalActivity extends Activity {

    private EditText goalNameEditText;
    private EditText goalAmountEditText;
    private Button addGoalButton;
    private ListView goalListView;

    private DatabaseHelper databaseHelper;
    private List<Goal> goalList;
    private ArrayAdapter<Goal> goalAdapter;
    private int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);

        // Initialize views
        goalNameEditText = findViewById(R.id.goalNameEditText);
        goalAmountEditText = findViewById(R.id.goalAmountEditText);
        addGoalButton = findViewById(R.id.addGoalButton);
        goalListView = findViewById(R.id.goalListView);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);
        userId = getUserId();
        // Set up the list view
        goalList = new ArrayList<>();
        goalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, goalList);
        goalListView.setAdapter(goalAdapter);

        // Set an item click listener for the list view
        goalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Goal goal = goalList.get(position);
                // Handle item click action here
                Toast.makeText(GoalActivity.this, "Clicked on goal: " + goal.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        // Set a click listener for the add goal button
        addGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGoal();
            }
        });

        // Load goals from the database
        loadGoals();
    }

    private void addGoal() {
        String goalName = goalNameEditText.getText().toString().trim();
        String goalAmount = goalAmountEditText.getText().toString().trim();

        if (goalName.isEmpty() || goalAmount.isEmpty()) {
            Toast.makeText(this, "Please enter a goal name and amount", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert the goal into the database
        long goalId = databaseHelper.insertGoal(goalName, goalAmount,userId,"not completed");

        if (goalId != -1) {
            // Clear input fields
            goalNameEditText.setText("");
            goalAmountEditText.setText("");

            // Create a new Goal object and add it to the list
            Goal goal = new Goal(goalId, goalName, Double.parseDouble(goalAmount),userId);
            goalList.add(goal);

            // Notify the adapter that the data has changed
            goalAdapter.notifyDataSetChanged();

            Toast.makeText(this, "Goal added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add goal", Toast.LENGTH_SHORT).show();
        }
    }
    private int getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("userId", -1);
    }

    private void loadGoals() {
        // Clear the list
        goalList.clear();

        // Retrieve goals from the database
        Cursor cursor = databaseHelper.getGoalsForUser(userId);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int goalId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_GOAL_ID));
                @SuppressLint("Range") String goalName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_GOAL_NAME));
                @SuppressLint("Range") String goalAmount = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_GOAL_AMOUNT));

                // Create a new Goal object and add it to the list
                Goal goal = new Goal(goalId, goalName, Double.parseDouble(goalAmount),userId);
                goalList.add(goal);
            } while (cursor.moveToNext());
        }

        // Close the cursor
        cursor.close();

        // Notify the adapter that the data has changed
        goalAdapter.notifyDataSetChanged();
    }
}
