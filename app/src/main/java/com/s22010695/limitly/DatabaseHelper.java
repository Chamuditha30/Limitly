package com.s22010695.limitly;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    //declare relevant variables
    public static final String DATABASE_NAME = "limitly.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS_NAME = "users";
    public static final String TABLE_APPS_NAME = "apps";
    public static final String TABLE_SETTINGS_NAME = "settings";
    public static final String TABLE_LOCATIONS_NAME = "locations";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_APPS_NAME + "(PACKAGE_NAME TEXT PRIMARY KEY, " + "IS_BLOCKED INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPS_NAME);
        onCreate(db);
    }
}