package com.example.inventoryappariellemoore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DBHelper manages SQLite database operations.
 * Updated to return List<InventoryItem> instead of String[][] for all item retrieval methods (Enhancement 3).
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory_app.db";
    private static final int DATABASE_VERSION = 1;

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String U_ID = "id";
    private static final String U_USERNAME = "username";
    private static final String U_PASSWORD = "password";
    private static final String U_SMS_ALLOWED = "sms_allowed";

    // Items table
    private static final String TABLE_ITEMS = "items";
    private static final String I_ID = "id";
    private static final String I_USER_ID = "user_id";
    private static final String I_NAME = "name";
    private static final String I_QUANTITY = "quantity";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                U_USERNAME + " TEXT UNIQUE NOT NULL, " +
                U_PASSWORD + " TEXT NOT NULL, " +
                U_SMS_ALLOWED + " INTEGER DEFAULT 0" +
                ");";

        String createItems = "CREATE TABLE " + TABLE_ITEMS + " (" +
                I_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                I_USER_ID + " INTEGER NOT NULL, " +
                I_NAME + " TEXT NOT NULL, " +
                I_QUANTITY + " INTEGER DEFAULT 0, " +
                "FOREIGN KEY(" + I_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + U_ID + ")" +
                ");";

        db.execSQL(createUsers);
        db.execSQL(createItems);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // User methods
    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_USERS, new String[]{U_ID},
                U_USERNAME + "=? AND " + U_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
        boolean exists = c != null && c.moveToFirst();
        if (c != null) c.close();
        return exists;
    }

    public boolean createUser(String username, String password) {
        if (getUserId(username) != -1) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(U_USERNAME, username);
        cv.put(U_PASSWORD, password);
        long id = db.insert(TABLE_USERS, null, cv);
        return id != -1;
    }

    public long getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_USERS, new String[]{U_ID},
                U_USERNAME + "=?", new String[]{username}, null, null, null);
        if (c != null && c.moveToFirst()) {
            long id = c.getLong(c.getColumnIndexOrThrow(U_ID));
            c.close();
            return id;
        }
        if (c != null) c.close();
        return -1;
    }

    public void setSmsAllowedForUser(String username, boolean allowed) {
        long id = getUserId(username);
        if (id == -1) return;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(U_SMS_ALLOWED, allowed ? 1 : 0);
        db.update(TABLE_USERS, cv, U_ID + "=?", new String[]{String.valueOf(id)});
    }

    public boolean isSmsAllowed(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_USERS, new String[]{U_SMS_ALLOWED},
                U_USERNAME + "=?", new String[]{username}, null, null, null);
        if (c != null && c.moveToFirst()) {
            int val = c.getInt(c.getColumnIndexOrThrow(U_SMS_ALLOWED));
            c.close();
            return val == 1;
        }
        if (c != null) c.close();
        return false;
    }

    // Item methods
    public long insertItem(long userId, String name, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(I_USER_ID, userId);
        cv.put(I_NAME, name);
        cv.put(I_QUANTITY, quantity);
        return db.insert(TABLE_ITEMS, null, cv);
    }

    public int updateItem(long itemId, String name, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(I_NAME, name);
        cv.put(I_QUANTITY, quantity);
        return db.update(TABLE_ITEMS, cv, I_ID + "=?", new String[]{String.valueOf(itemId)});
    }

    public int deleteItem(long itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ITEMS, I_ID + "=?", new String[]{String.valueOf(itemId)});
    }

    public List<InventoryItem> getItemsForUser(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_ITEMS, new String[]{I_ID, I_NAME, I_QUANTITY},
                I_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, I_NAME + " ASC");

        List<InventoryItem> items = new ArrayList<>();
        if (c != null) {
            while (c.moveToNext()) {
                items.add(new InventoryItem(
                        c.getLong(c.getColumnIndexOrThrow(I_ID)),
                        c.getString(c.getColumnIndexOrThrow(I_NAME)),
                        c.getInt(c.getColumnIndexOrThrow(I_QUANTITY))
                ));
            }
            c.close();
        }
        return items;
    }

    // Search and filtering methods
    public List<InventoryItem> getItemsByQuantityRange(long userId, int min, int max) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_ITEMS, new String[]{I_ID, I_NAME, I_QUANTITY},
                I_USER_ID + "=? AND " + I_QUANTITY + " BETWEEN ? AND ?",
                new String[]{String.valueOf(userId), String.valueOf(min), String.valueOf(max)},
                null, null, I_NAME + " ASC");

        List<InventoryItem> filtered = new ArrayList<>();
        if (c != null) {
            while (c.moveToNext()) {
                filtered.add(new InventoryItem(
                        c.getLong(c.getColumnIndexOrThrow(I_ID)),
                        c.getString(c.getColumnIndexOrThrow(I_NAME)),
                        c.getInt(c.getColumnIndexOrThrow(I_QUANTITY))
                ));
            }
            c.close();
        }
        return filtered;
    }

    public List<InventoryItem> searchItemsByName(long userId, String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_ITEMS, new String[]{I_ID, I_NAME, I_QUANTITY},
                I_USER_ID + "=? AND " + I_NAME + " LIKE ?",
                new String[]{String.valueOf(userId), "%" + query + "%"},
                null, null, I_NAME + " ASC");

        List<InventoryItem> results = new ArrayList<>();
        if (c != null) {
            while (c.moveToNext()) {
                results.add(new InventoryItem(
                        c.getLong(c.getColumnIndexOrThrow(I_ID)),
                        c.getString(c.getColumnIndexOrThrow(I_NAME)),
                        c.getInt(c.getColumnIndexOrThrow(I_QUANTITY))
                ));
            }
            c.close();
        }
        return results;
    }

    public void deleteUser(long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Cascade delete all items first
            db.delete(TABLE_ITEMS, I_USER_ID + "=?", new String[]{String.valueOf(userId)});
            db.delete(TABLE_USERS, U_ID + "=?", new String[]{String.valueOf(userId)});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void batchUpdateQuantities(Map<Long, Integer> updates) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Map.Entry<Long, Integer> entry : updates.entrySet()) {
                ContentValues cv = new ContentValues();
                cv.put(I_QUANTITY, entry.getValue());
                db.update(TABLE_ITEMS, cv, I_ID + "=?", new String[]{String.valueOf(entry.getKey())});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
