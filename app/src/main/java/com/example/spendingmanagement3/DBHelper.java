package com.example.spendingmanagement3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {
    public static final String TAG = "DataBaseHelper";
    public static final String TABLE_NAME = "HistoryTable";
    public static final String COL1 = "ID";
    public static final String COL2 = "string";
    public static final String COL3 = "balance";
    public static final String COL4 = "spendingType";
    public static final String COL5 = "date";
    public static final String COL6 = "amount";

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT, " + COL3 + " FLOAT, " + COL4 + " TEXT, " + COL5 + " TEXT, " + COL6 + " FLOAT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public DBHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    public void addData(String s, float balance, String spendingType, String date, float amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, s);
        contentValues.put(COL3, balance);
        contentValues.put(COL4, spendingType);
        contentValues.put(COL5, date);
        contentValues.put(COL6, amount);
        db.insert(TABLE_NAME, null, contentValues);
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
}
