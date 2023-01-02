package com.example.r_gameshopapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME_C = "Cart";
    public static final String TABLE_NAME_H = "History";
    public static final String TABLE_NAME_S = "Stock";
    public static final String TABLE_NAME_A = "Account";

    //column name
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String PRICE = "price";
    public static final String CID = "cid";
    public static final String AMOUNT = "amount";

    //Account
    public static final String PASS = "pass";

    public static final String CASH = "cash";

    //Stock
    public static final String STOCK = "stock";
    public static final String TYPE = "type";

    //Cart
    public static final String DATE = "date";
    public static final String TID = "tid";

    //create query
    private static final String CREATE_TABLE_CART =
            "create table " + TABLE_NAME_C + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    NAME + " TEXT NOT NULL," +
                    CID + " INTEGER," +
                    PRICE + " INTEGER," +
                    DATE + " STRING," +
                    TID + " INTEGER," +
                    AMOUNT + " INTEGER);";

    private static final String CREATE_TABLE_HISTORY =
            "create table " + TABLE_NAME_H + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    NAME + " TEXT NOT NULL," +
                    CID + " INTEGER," +
                    PRICE + " INTEGER," +
                    DATE + " STRING," +
                    TID + " INTEGER," +
                    AMOUNT + " INTEGER);";

    private static final String CREATE_TABLE_STOCK =
            "create table " + TABLE_NAME_S + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    NAME + " TEXT NOT NULL," +
                    TYPE + " INTEGER," +
                    PRICE + " INTEGER," +
                    STOCK + " INTEGER);";

    private static final String CREATE_TABLE_ACCOUNT =
            "create table " + TABLE_NAME_A + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    NAME + " TEXT NOT NULL," +
                    PASS + " TEXT NOT NULL," +
                    CASH + " INTEGER);";


    private static final String DB_NAME = "RGAMESHOP.DB";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CART);
        db.execSQL(CREATE_TABLE_HISTORY);
        db.execSQL(CREATE_TABLE_ACCOUNT);
        db.execSQL(CREATE_TABLE_STOCK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_C);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_H);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_A);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_S);
        onCreate(db);
    }

    public List<Stock> search(String string) {
        List<Stock> stocks = null;
        try {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_S + " WHERE " + NAME + " LIKE ?", new String[] { "%" + string + "%"});
            if (cursor.moveToFirst()) {
                stocks = new ArrayList<Stock>();
                do {
                    Stock stock = new Stock();
                    stock.setId(cursor.getInt(0));
                    stock.setName(cursor.getString(1));
                    stock.setPrice(cursor.getDouble(2));
                    stock.setStock(cursor.getInt(3));
                    stocks.add(stock);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            stocks = null;
        }
        return stocks;
    }
}
