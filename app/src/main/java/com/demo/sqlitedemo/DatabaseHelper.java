package com.demo.sqlitedemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DemoDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create your database tables here
        String createTableQuery = "CREATE TABLE IF NOT EXISTS TableItems ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "item_name TEXT,"
                + "item_date TEXT,"
                + "item_time TEXT,"
                + "item_quantity INTEGER);";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database schema upgrades here
        // Typically, you would drop and recreate tables or perform data migrations
        db.execSQL("DROP TABLE IF EXISTS TableItems");
        onCreate(db);
    }
}

