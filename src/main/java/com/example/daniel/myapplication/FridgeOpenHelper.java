package com.example.daniel.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class FridgeOpenHelper extends SQLiteOpenHelper {
    //Database Version
    private static final int DATABASE_VERSION = 2;

    //Database Name
    private static final String DATABASE_NAME = "food";

    //Fridge table name
    private static final String TABLE_FRIDGE = "fridge";
    private static final String TABLE_PANTRY = "pantry";
    private static final String TABLE_LIST = "list";


    //Table Column's names
    private static final String KEY_ID= "id";
    private static final String KEY_ITEM = "item";


    FridgeOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String FRIDGE_TABLE_CREATE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_FRIDGE +
                        " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_ITEM + " TEXT NOT NULL" + ");";
        String PANTRY_TABLE_CREATE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_PANTRY +
                        " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_ITEM + " TEXT NOT NULL" + ");";
        String LIST_TABLE_CREATE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_LIST +
                        " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_ITEM + " TEXT NOT NULL" + ");";
        db.execSQL(FRIDGE_TABLE_CREATE);
        db.execSQL(PANTRY_TABLE_CREATE);
        db.execSQL(LIST_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIDGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST);

        // Create tables again
        onCreate(db);
    }

    @Override
    public String getDatabaseName(){
        return DATABASE_NAME;
    }

    public void addItemFridge(String item){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM, item);

        db.insert(TABLE_FRIDGE, null, values);
    }

    public void addItemPantry(String item){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM, item);

        db.insert(TABLE_PANTRY, null, values);
    }

    public void addItemList(String item){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM, item);

        db.insert(TABLE_LIST, null, values);
    }

    public void deleteItemFridge(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FRIDGE, KEY_ITEM + "=?", new String[]{item});
    }

    public void deleteItemPantry(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PANTRY, KEY_ITEM + "=?", new String[]{item});
    }

    public void deleteItemList(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LIST, KEY_ITEM + "=?", new String[]{item});
    }


    public Cursor getAllItemsFridge() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT id AS _id, item FROM " + TABLE_FRIDGE, null);
        return cursor;
    }

    public Cursor getAllItemsPantry() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT id AS _id, item FROM " + TABLE_PANTRY, null);
        return cursor;
    }

    public Cursor getAllItemsList() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT id AS _id, item FROM " + TABLE_LIST, null);
        return cursor;
    }
}
