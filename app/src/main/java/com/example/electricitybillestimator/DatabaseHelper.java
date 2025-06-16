package com.example.electricitybillestimator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ElectricityBills.db";
    private static final int DATABASE_VERSION = 1;

    // Rename ID column to _id for SimpleCursorAdapter compatibility
    public static final String COLUMN_ID = "_id";
    public static final String TABLE_NAME = "bills_table";
    public static final String COLUMN_MONTH = "month";
    public static final String COLUMN_UNITS = "units";
    public static final String COLUMN_REBATE = "rebate";
    public static final String COLUMN_TOTAL_CHARGES = "total_charges";
    public static final String COLUMN_FINAL_COST = "final_cost";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_MONTH + " TEXT, " +
            COLUMN_UNITS + " INTEGER, " +
            COLUMN_REBATE + " REAL, " +
            COLUMN_TOTAL_CHARGES + " REAL, " +
            COLUMN_FINAL_COST + " REAL" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertBill(String month, int units, double rebate, double totalCharges, double finalCost) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MONTH, month);
        cv.put(COLUMN_UNITS, units);
        cv.put(COLUMN_REBATE, rebate);
        cv.put(COLUMN_TOTAL_CHARGES, totalCharges);
        cv.put(COLUMN_FINAL_COST, finalCost);
        return db.insert(TABLE_NAME, null, cv);
    }

    // This method will return cursor with _id, month, final_cost for SimpleCursorAdapter
    public Cursor getAllBills() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_MONTH, COLUMN_FINAL_COST},
                null, null, null, null,
                COLUMN_ID + " DESC");
    }

    // Optional: get bill by _id (for DetailActivity)
    public Cursor getBillById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME,
                null,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);
    }
}
