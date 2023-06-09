package com.nnhiep.travelmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.nnhiep.travelmanager.models.DSTour;
import java.util.ArrayList;

public class Tour_database extends SQLiteOpenHelper {
    public static final String DB_NAME = "Tour.db2223s";
    public static final String TABLE_NAME = "Tour1234";
    public static final String TABLE_REGISTER = "RegisterTour23";
    public static final String COL_ID = "id";
    public static final String COL_TILTLE = "title";
    public static final String COL_PRICE = "price";
    public static final String COL_STAR = "star";
    public static final String COL_DATE = "date";
    public static final String COL_IMAGE = "image";

    public Tour_database(@Nullable Context context) {
        super(context, DB_NAME, null, 3);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT,price TEXT,star TEXT,date TEXT,image BLOB)");
        db.execSQL("create table " + TABLE_REGISTER + "(id INTEGER PRIMARY KEY ,title TEXT,price TEXT,star TEXT,date TEXT,image BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        db.execSQL("drop table if exists " + TABLE_REGISTER);
    }

    public boolean insertTour(String title, String price, String star, String date, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TILTLE, title);
        contentValues.put(COL_PRICE, price);
        contentValues.put(COL_PRICE, price);
        contentValues.put(COL_STAR, star);
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_IMAGE, image);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertTourRegister(int id, String title, String price, String star, String date, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, id);
        contentValues.put(COL_TILTLE, title);
        contentValues.put(COL_PRICE, price);
        contentValues.put(COL_PRICE, price);
        contentValues.put(COL_STAR, star);
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_IMAGE, image);
        long result = db.insert(TABLE_REGISTER, null, contentValues);
        return result != -1;
    }

    public ArrayList<DSTour> getTourData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DSTour> arrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("Select * from " + TABLE_NAME, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String price = cursor.getString(2);
            String star = cursor.getString(3);
            String date = cursor.getString(4);
            byte[] image = cursor.getBlob(5);
            DSTour dsTour = new DSTour(id, title, price, star, date, image);
            arrayList.add(dsTour);
        }

        return arrayList;
    }

    public ArrayList<DSTour> getRegisterData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DSTour> arrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("Select * from " + TABLE_REGISTER, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String price = cursor.getString(2);
            String star = cursor.getString(3);
            String date = cursor.getString(4);
            byte[] image = cursor.getBlob(5);
            DSTour dsTour = new DSTour(id, title, price, star, date, image);
            arrayList.add(dsTour);
        }

        return arrayList;
    }

    public int deleteTour(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "id =?", new String[]{id});
    }

    public int deleteResgister(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_REGISTER, "id =?", new String[]{id});
    }

    public boolean updateTour(String id, String title, String price, String star, String date, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TILTLE, title);
        contentValues.put(COL_PRICE, price);
        contentValues.put(COL_PRICE, price);
        contentValues.put(COL_STAR, star);
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_IMAGE, image);
        db.update(TABLE_NAME, contentValues, "id= ?", new String[]{id});

        return true;
    }
}
