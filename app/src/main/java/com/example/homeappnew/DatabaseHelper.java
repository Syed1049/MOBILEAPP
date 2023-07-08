package com.example.homeappnew;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app_database";
    private static final int DATABASE_VERSION = 1;

    // User table
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";

    // Expense table
    public static final String TABLE_EXPENSES = "expenses";
    public static final String COLUMN_EXPENSE_ID = "expense_id";
    public static final String COLUMN_EXPENSE_TITLE = "title";
    public static final String COLUMN_EXPENSE_AMOUNT = "amount";
    public static final String COLUMN_EXPENSE_USER_ID = "user_id";

    // Income table
    public static final String TABLE_INCOMES = "incomes";
    public static final String COLUMN_INCOME_ID = "income_id";
    public static final String COLUMN_INCOME_NAME = "name";
    public static final String COLUMN_INCOME_AMOUNT = "amount";
    public static final String COLUMN_INCOME_USER_ID = "user_id";

    public static final String TABLE_GOALS = "goals";
    public static final String COLUMN_GOAL_ID = "goal_id";
    public static final String COLUMN_GOAL_NAME = "name";
    public static final String COLUMN_GOAL_AMOUNT = "amount";

    public static final String COLUMN_GOAL_USER_ID = "user_id";
    public static final String COLUMN_GOAL_STATUS = "status";

    private static final String CREATE_GOALS_TABLE = "CREATE TABLE " + TABLE_GOALS + "("
            + COLUMN_GOAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_GOAL_NAME + " TEXT,"
            + COLUMN_GOAL_AMOUNT + " TEXT,"
            + COLUMN_GOAL_USER_ID + " INTEGER,"
            + COLUMN_GOAL_STATUS + " TEXT,"
            + "FOREIGN KEY(" + COLUMN_GOAL_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")"
            + ")";



    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT"
            + ")";

    private static final String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_EXPENSES + "("
            + COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_EXPENSE_TITLE + " TEXT,"
            + COLUMN_EXPENSE_AMOUNT + " TEXT,"
            + COLUMN_EXPENSE_USER_ID + " INTEGER,"
            + "FOREIGN KEY(" + COLUMN_EXPENSE_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")"
            + ")";

    private static final String CREATE_INCOMES_TABLE = "CREATE TABLE " + TABLE_INCOMES + "("
            + COLUMN_INCOME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_INCOME_NAME + " TEXT,"
            + COLUMN_INCOME_AMOUNT + " TEXT,"
            + COLUMN_INCOME_USER_ID + " INTEGER,"
            + "FOREIGN KEY(" + COLUMN_INCOME_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the users table
        db.execSQL(CREATE_USERS_TABLE);

        // Create the expenses table
        db.execSQL(CREATE_EXPENSES_TABLE);

        // Create the incomes table
        db.execSQL(CREATE_INCOMES_TABLE);
        db.execSQL(CREATE_GOALS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing tables if needed and recreate them
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Method to insert a user into the database
    public long insertUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);
        return db.insert(TABLE_USERS, null, values);
    }

    // Method to insert an expense into the database
    public long insertExpense(String title, String amount, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPENSE_TITLE, title);
        values.put(COLUMN_EXPENSE_AMOUNT, amount);
        values.put(COLUMN_EXPENSE_USER_ID, userId);
        return db.insert(TABLE_EXPENSES, null, values);
    }

    // Method to insert an income into the database
    public long insertIncome(String name, String amount, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_INCOME_NAME, name);
        values.put(COLUMN_INCOME_AMOUNT, amount);
        values.put(COLUMN_INCOME_USER_ID, userId);
        return db.insert(TABLE_INCOMES, null, values);
    }

    // Method to update an expense in the database
    public boolean updateExpense(int expenseId, String expenseTitle, String expenseAmount, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPENSE_TITLE, expenseTitle);
        values.put(COLUMN_EXPENSE_AMOUNT, expenseAmount);

        // Define the WHERE clause to update the specific expense
        String selection = COLUMN_EXPENSE_ID + " = ? AND " + COLUMN_EXPENSE_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(expenseId), String.valueOf(userId) };

        Log.d(TAG, "Update query: expenseId = " + expenseId + ", userId = " + userId);

        int rowsAffected = db.update(TABLE_EXPENSES, values, selection, selectionArgs);
        db.close();

        if (rowsAffected > 0) {
            Log.d(TAG, "Expense updated successfully: expenseId = " + expenseId);
            return true;
        } else {
            Log.d(TAG, "Failed to update expense: expenseId = " + expenseId);
            return false;
        }
    }



    // Method to update an income in the database
    public boolean updateIncome(int incomeId, String incomeName, String incomeAmount, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_INCOME_NAME, incomeName);
        values.put(COLUMN_INCOME_AMOUNT, incomeAmount);

        // Define the WHERE clause to update the specific income
        String selection = COLUMN_INCOME_ID + " = ? AND " + COLUMN_INCOME_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(incomeId), String.valueOf(userId) };

        int rowsAffected = db.update(TABLE_INCOMES, values, selection, selectionArgs);
        db.close();

        return rowsAffected > 0;
    }

    // Method to delete an expense from the database
    public int deleteExpense(int expenseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_EXPENSE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(expenseId)};
        return db.delete(TABLE_EXPENSES, selection, selectionArgs);
    }

    public boolean removeExpense(int expenseId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Define the WHERE clause to remove the specific expense
        String selection = COLUMN_EXPENSE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(expenseId)};

        int rowsAffected = db.delete(TABLE_EXPENSES, selection, selectionArgs);
        db.close();

        return rowsAffected > 0;
    }
    // Method to delete an income from the database
    public int deleteIncomes(int incomeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_INCOME_ID + " = ?";
        String[] selectionArgs = {String.valueOf(incomeId)};
        return db.delete(TABLE_INCOMES, selection, selectionArgs);
    }
    public boolean deleteIncome(int incomeId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete("incomes", "id = ? AND userid = ?", new String[]{String.valueOf(incomeId), String.valueOf(userId)});
        return rowsAffected > 0;
    }

    // Method to retrieve all expenses from the database
    public Cursor getAllExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_EXPENSES, null, null, null, null, null, null);
    }

    // Method to retrieve all incomes from the database
    public Cursor getAllIncomes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_INCOMES, null, null, null, null, null, null);
    }

    // Method to retrieve expenses for a specific user from the database
    public Cursor getExpensesForUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                COLUMN_EXPENSE_ID,
                COLUMN_EXPENSE_TITLE,
                COLUMN_EXPENSE_AMOUNT
        };
        String selection = COLUMN_EXPENSE_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };
        return db.query(TABLE_EXPENSES, projection, selection, selectionArgs, null, null, null);
    }

    // Method to retrieve incomes for a specific user from the database
    public Cursor getIncomesForUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                COLUMN_INCOME_ID,
                COLUMN_INCOME_NAME,
                COLUMN_INCOME_AMOUNT
        };
        String selection = COLUMN_INCOME_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };
        return db.query(TABLE_INCOMES, projection, selection, selectionArgs, null, null, null);
    }
    // Method to retrieve incomes for a specific user from the database
    public List<Income> getIncomesByUserId(int userId) {
        List<Income> incomes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_INCOME_ID,
                COLUMN_INCOME_NAME,
                COLUMN_INCOME_AMOUNT
        };

        String selection = COLUMN_INCOME_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };

        Cursor cursor = db.query(TABLE_INCOMES, projection, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int incomeId = cursor.getInt(cursor.getColumnIndex(COLUMN_INCOME_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_NAME));
                @SuppressLint("Range") String amount = cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_AMOUNT));

                // Create an Income object with the retrieved data
                Income income = new Income(incomeId, name, amount, userId);
                incomes.add(income);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return incomes;
    }

    @SuppressLint("Range")
    public int getUserId() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String orderBy = COLUMN_USER_ID + " DESC";
        String limit = "1";

        Cursor cursor = db.query(TABLE_USERS, columns, null, null, null, null, orderBy, limit);
        int userId = -1;

        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return userId;
    }

    // Method to check if a user exists in the database
    public int checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_USER_NAME + " = ? AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int userId = -1;

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_USER_ID);
            if (columnIndex != -1) {
                userId = cursor.getInt(columnIndex);
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return userId;
    }



    // Method to insert a goal into the database
    public long insertGoal(String name, String amount, int userId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GOAL_NAME, name);
        values.put(COLUMN_GOAL_AMOUNT, amount);
        values.put(COLUMN_GOAL_USER_ID, userId);
        values.put(COLUMN_GOAL_STATUS, status);
        return db.insert(TABLE_GOALS, null, values);
    }

    // Method to update a goal in the database
    public boolean updateGoal(int goalId, String name, String amount, int userId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GOAL_NAME, name);
        values.put(COLUMN_GOAL_AMOUNT, amount);
        values.put(COLUMN_GOAL_USER_ID, userId);
        values.put(COLUMN_GOAL_STATUS, status);

        // Define the WHERE clause to update the specific goal
        String selection = COLUMN_GOAL_ID + " = ? AND " + COLUMN_GOAL_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(goalId), String.valueOf(userId) };

        int rowsAffected = db.update(TABLE_GOALS, values, selection, selectionArgs);
        db.close();

        return rowsAffected > 0;
    }

    // Method to delete a goal from the database
    public int deleteGoal(int goalId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_GOAL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(goalId)};
        return db.delete(TABLE_GOALS, selection, selectionArgs);
    }

    // Method to retrieve goals for a specific user from the database
    public Cursor getGoalsForUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                COLUMN_GOAL_ID,
                COLUMN_GOAL_NAME,
                COLUMN_GOAL_AMOUNT,

                COLUMN_GOAL_STATUS
        };
        String selection = COLUMN_GOAL_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };
        return db.query(TABLE_GOALS, projection, selection, selectionArgs, null, null, null);
    }
    public Cursor getExpenseById(int expenseId) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                COLUMN_EXPENSE_ID,
                COLUMN_EXPENSE_TITLE,
                COLUMN_EXPENSE_AMOUNT
        };

        String selection = COLUMN_EXPENSE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(expenseId)};

        return db.query(
                TABLE_EXPENSES,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }

}





