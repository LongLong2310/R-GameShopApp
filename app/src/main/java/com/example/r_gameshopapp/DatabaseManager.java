package com.example.r_gameshopapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DatabaseManager(Context c) {
        context = c;
    }

    public DatabaseManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insertStock(String name, String type, int stock, double price) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME, name);
        contentValue.put(DatabaseHelper.TYPE, type);
        contentValue.put(DatabaseHelper.PRICE, "$" + price);
        contentValue.put(DatabaseHelper.STOCK, stock);
        database.insert(DatabaseHelper.TABLE_NAME_S, null, contentValue);
    }

    public void insertAccount(String username, String pass, double cash) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME, username);
        contentValue.put(DatabaseHelper.PASS, pass);
        contentValue.put(DatabaseHelper.CASH, "$" + cash);
        database.insert(DatabaseHelper.TABLE_NAME_A, null, contentValue);
    }

    public void insertCart(int cusID, List<Item> productList, double total) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.CID, cusID);
        contentValue.put(DatabaseHelper.NAME, new Gson().toJson(productList));
        contentValue.put(DatabaseHelper.DATE, LocalDateTime.now().toString());
        contentValue.put(DatabaseHelper.TOTAL, total);
        database.insert(DatabaseHelper.TABLE_NAME_C, null, contentValue);
    }

//    private String toJson(Object object) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            return objectMapper.writeValueAsString(object);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void insertHistory(int cusID, List<Item> productList, double total) {
        String DATE_FORMATTER= "yyyy-MM-dd";
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        String formatDateTime = date.format(formatter);

        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.CID, cusID);
        contentValue.put(DatabaseHelper.PRODUCTLIST, new Gson().toJson(productList));
        contentValue.put(DatabaseHelper.DATE, formatDateTime);
        contentValue.put(DatabaseHelper.TOTAL, total + "$");
        database.insert(DatabaseHelper.TABLE_NAME_H, null, contentValue);
    }

    public int updateAccount(long _id, String username, String pass, double cash) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME, username);
        contentValue.put(DatabaseHelper.PASS, pass);
        contentValue.put(DatabaseHelper.CASH, "$" + cash);
        int i = database.update(DatabaseHelper.TABLE_NAME_A,
                contentValue,
                DatabaseHelper.ID + " =" + _id, null);
        return i;
    }

    public int updateStock(long _id, String name, String type, int stock, double price) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME, name);
        contentValue.put(DatabaseHelper.TYPE, type);
        contentValue.put(DatabaseHelper.PRICE, "$" + price);
        contentValue.put(DatabaseHelper.STOCK, stock);
        int i = database.update(DatabaseHelper.TABLE_NAME_S,
                contentValue,
                DatabaseHelper.ID + " =" + _id, null);
        return i;
    }

    public int updateCart(long _id, String name, int cid, int price, int amount) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME, name);
        contentValue.put(DatabaseHelper.CID, cid);
        contentValue.put(DatabaseHelper.PRICE, price);
        contentValue.put(DatabaseHelper.AMOUNT, amount);
        int i = database.update(DatabaseHelper.TABLE_NAME_C,
                contentValue,
                DatabaseHelper.ID + " =" + _id, null);
        return i;
    }

    public int updateHistory(long _id, String name, int cid, int price, int amount) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME, name);
        contentValue.put(DatabaseHelper.CID, cid);
        contentValue.put(DatabaseHelper.PRICE, price);
        contentValue.put(DatabaseHelper.AMOUNT, amount);
        int i = database.update(DatabaseHelper.TABLE_NAME_H,
                contentValue,
                DatabaseHelper.ID + " =" + _id, null);
        return i;
    }


    public void deleteAccount(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME_A,
                DatabaseHelper.ID + " =" + _id, null);
        // When deleting or adding rows with AUTOINCREMENT, use this to
        // reserve the biggest primary key in the table
        database.delete("SQLITE_SEQUENCE", "NAME = ?", new String[]{
                DatabaseHelper.TABLE_NAME_A});
    }

    public void deleteStock(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME_S,
                DatabaseHelper.ID + " =" + _id, null);
        // When deleting or adding rows with AUTOINCREMENT, use this to
        // reserve the biggest primary key in the table
        database.delete("SQLITE_SEQUENCE", "NAME = ?", new String[]{
                DatabaseHelper.TABLE_NAME_S});
    }

    public void deleteCart(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME_C,
                DatabaseHelper.ID + " =" + _id, null);
        // When deleting or adding rows with AUTOINCREMENT, use this to
        // reserve the biggest primary key in the table
        database.delete("SQLITE_SEQUENCE", "NAME = ?", new String[]{
                DatabaseHelper.TABLE_NAME_C});
    }

    public void deleteHistory(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME_H,
                DatabaseHelper.ID + " =" + _id, null);
        // When deleting or adding rows with AUTOINCREMENT, use this to
        // reserve the biggest primary key in the table
        database.delete("SQLITE_SEQUENCE", "NAME = ?", new String[]{
                DatabaseHelper.TABLE_NAME_H});
    }

    public Cursor searchName(String name) {
        database = this.dbHelper.getReadableDatabase();

        String queryString = "SELECT * FROM " + DatabaseHelper.TABLE_NAME_A + " WHERE " + DatabaseHelper.NAME + " LIKE '%" + name + "%'";
        Cursor cursor = database.rawQuery(queryString, null);
        return cursor;
    }


    public String searchAccount(long _id) {

        String queryString = "SELECT * FROM " + DatabaseHelper.TABLE_NAME_A + " WHERE ID='" + _id + "'";

        String returnPatientModel = "";

        database = this.dbHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            String accountName = cursor.getString(1);
            String accountPass = cursor.getString(2);
            int accountCash = cursor.getInt(3);

            returnPatientModel = accountName + " " + accountPass + " " + accountCash;
        } else {
            //Fail -> do not add anything
            System.out.println("No Record Found");
        }

        cursor.close();
        database.close();
        return returnPatientModel;
    }



    //calculate total price of cart=amount*price of each item
    public int total() {
        int total = 0;
        String[] columns = new String[]{
                DatabaseHelper.ID,
                DatabaseHelper.NAME,
                DatabaseHelper.CID,
                DatabaseHelper.PRICE,
                DatabaseHelper.AMOUNT,
        };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_C, columns,
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        while (cursor.isAfterLast() == false) {
            total = total + cursor.getInt(3) * cursor.getInt(4);
            cursor.moveToNext();
        }
        cursor.close();
        return total;
    }
    public int buy(String name,int amount) {

        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.STOCK, amount);
        int i = database.update(DatabaseHelper.TABLE_NAME_S,
                contentValue,
                DatabaseHelper.NAME + " ='" + name +"'", null);

        return i;

    }
    public int changeBalance(String name,double amount) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.CASH,"$" + amount);
        int i = database.update(DatabaseHelper.TABLE_NAME_A,
                contentValue,
                DatabaseHelper.NAME + " ='" + name +"'", null);
        database.close();
        return i;

    }
    public int BuyBalance(String name,double amount) {
        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.CASH,"$" + amount);
        int i = database.update(DatabaseHelper.TABLE_NAME_A,
                contentValue,
                DatabaseHelper.NAME + " ='" + name +"'", null);
        return i;

    }

    public Cursor selectAllStock() {
        String[] columns = new String[]{
                DatabaseHelper.ID,
                DatabaseHelper.NAME,
                DatabaseHelper.TYPE,
                DatabaseHelper.STOCK,
                DatabaseHelper.PRICE
        };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_S, columns,
                null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor searchStockName(String string) {

        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_S + " WHERE " + DatabaseHelper.NAME + " LIKE ?", new String[]{"%" + string + "%"});

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor searchStockID(String string) {

        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_S + " WHERE " + DatabaseHelper.ID + " LIKE ?", new String[]{"%" + string + "%"});

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor searchStockType(String string) {

        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_S + " WHERE " + DatabaseHelper.TYPE + " LIKE ?", new String[]{"%" + string + "%"});

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor searchAccountName(String string) {

        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_A + " WHERE " + DatabaseHelper.NAME + " LIKE ?", new String[]{"%" + string + "%"});

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor searchAccountID(String string) {

        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_A + " WHERE " + DatabaseHelper.ID + " LIKE ?", new String[]{"%" + string + "%"});

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    public Cursor selectAllHistory() {
        String[] columns = new String[]{
                DatabaseHelper.ID,
                DatabaseHelper.CID,
                DatabaseHelper.PRODUCTLIST,
                DatabaseHelper.DATE,
                DatabaseHelper.TOTAL
        };

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_H, columns,
                null, null, null, null, null);


        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor selectUserPurchaseHistory(String CustomerID) {
        String[] columns = new String[]{
                DatabaseHelper.ID,
                DatabaseHelper.CID,
                DatabaseHelper.PRODUCTLIST,
                DatabaseHelper.DATE,
                DatabaseHelper.TOTAL
        };
//        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_H, columns,
//                null, null, null, null, null);


        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME_H + " WHERE " + DatabaseHelper.CID + " LIKE ?", new String[]{"%" + CustomerID + "%"});


        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor selectAllAccount() {
        String[] columns = new String[]{
                DatabaseHelper.ID,
                DatabaseHelper.NAME,
                DatabaseHelper.PASS,
                DatabaseHelper.CASH
        };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_A, columns,
                null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public boolean checkUser(String username) {
        if (username.equals("admin")){return true;}
        // array of columns to fetch
        String[] columns = {
                DatabaseHelper.ID
        };
        ;
        // selection criteria
        String selection = DatabaseHelper.NAME + " = ?";
        // selection argument
        String[] selectionArgs = {username};
        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_name = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_A, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public boolean checkUser(String username, String password) {
        // array of columns to fetch
        String[] columns = {
                DatabaseHelper.ID
        };
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();

        // selection criteria
        String selection = DatabaseHelper.NAME + " = ?" + " AND " + DatabaseHelper.PASS + " = ?";
        // selection arguments
        String[] selectionArgs = {username, password};
        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_name= AND user_password = 'qwerty';
         */
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_A, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        database.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
    public boolean checkStock(String name) {

        // array of columns to fetch
        String[] columns = {
                DatabaseHelper.ID
        };
        ;
        // selection criteria
        String selection = DatabaseHelper.NAME + " = ?";
        // selection argument
        String[] selectionArgs = {name};
        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_name = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_S, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public Cursor loginInfo(String username) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        String[] columns = new String[]{
                DatabaseHelper.ID,
                DatabaseHelper.NAME,
                DatabaseHelper.PASS,
                DatabaseHelper.CASH
        };
        // selection criteria
        String selection = DatabaseHelper.NAME + " = ?";
        // selection argument
        String[] selectionArgs = {username};
        // query user table with condition
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_A, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        database.close();
        return cursor;
    }

    public ArrayList<Item> getAllItem() {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        String[] columns = new String[]{
                DatabaseHelper.ID,
                DatabaseHelper.NAME,
                DatabaseHelper.TYPE,
                DatabaseHelper.STOCK,
                DatabaseHelper.PRICE
        };
        ArrayList<Item> item = new ArrayList<Item>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_S, columns,
                null, null, null, null, null);


        if (cursor != null) {
            while (cursor.moveToNext()) {

                Item it = new Item(cursor.getString(1), cursor.getInt(3), cursor.getString(2), Double.parseDouble(cursor.getString(4).replaceAll("[$]", "")));

                item.add(it);
            }
        }
        database.close();
        return item;

    }
}

