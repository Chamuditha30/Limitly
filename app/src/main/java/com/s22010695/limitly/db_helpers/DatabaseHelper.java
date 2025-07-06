package com.s22010695.limitly.db_helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    //declare relevant variables
    public static final String DATABASE_NAME = "limitly.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_USER_NAME = "user";
    public static final String TABLE_APPS_NAME = "apps";
    public static final String TABLE_TIMER_MODE_NAME = "timer_mode";
    public static final String TABLE_FOCUS_MODE_NAME = "focus_mode";
    public static final String TABLE_LOCATIONS_NAME = "locations";
    public static final String TABLE_STUDY_MODE_NAME = "study_mode";
    public static final String TABLE_SLEEP_MODE_NAME = "sleep_mode";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create user table
        db.execSQL("CREATE TABLE " + TABLE_USER_NAME + "(ID INTEGER PRIMARY KEY, " + "USERNAME TEXT, " + "PASSWORD TEXT)");

        //create apps table
        db.execSQL("CREATE TABLE " + TABLE_APPS_NAME + "(PACKAGE_NAME TEXT PRIMARY KEY, " + "IS_BLOCKED INTEGER)");

        //create timer mode table
        db.execSQL("CREATE TABLE " + TABLE_TIMER_MODE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " + "IS_ENABLED INTEGER DEFAULT 0, " + "BLOCK_TIME_MIN INTEGER DEFAULT 15, " + "UNBLOCK_TIME_MIN INTEGER DEFAULT 30)");
        //insert default row in timer mode table
        ContentValues timerModeValues = new ContentValues();
        timerModeValues.put("IS_ENABLED", 0);
        timerModeValues.put("BLOCK_TIME_MIN", 15);
        timerModeValues.put("UNBLOCK_TIME_MIN", 30);
        db.insert(TABLE_TIMER_MODE_NAME, null, timerModeValues);

        //create focus mode table
        db.execSQL("CREATE TABLE " + TABLE_FOCUS_MODE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " + "IS_ENABLED INTEGER DEFAULT 0)");
        //insert default row in focus mode table
        ContentValues focusModeValues = new ContentValues();
        focusModeValues.put("IS_ENABLED", 0);
        db.insert(TABLE_FOCUS_MODE_NAME, null, focusModeValues);

        //create location table
        db.execSQL("CREATE TABLE " + TABLE_LOCATIONS_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " + "TITLE TEXT, " + "LATITUDE REAL, " + "LONGITUDE REAL, " + "UNIQUE(LATITUDE, LONGITUDE))");

        //create study mode table
        db.execSQL("CREATE TABLE " + TABLE_STUDY_MODE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " + "IS_ENABLED INTEGER DEFAULT 0)");
        //insert default row in focus mode table
        ContentValues studyModeValues = new ContentValues();
        studyModeValues.put("IS_ENABLED", 0);
        db.insert(TABLE_STUDY_MODE_NAME, null, studyModeValues);

        //create sleep mode table
        db.execSQL("CREATE TABLE " + TABLE_SLEEP_MODE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " + "IS_ENABLED INTEGER DEFAULT 0, " + "SLEEP_TIME_MIN INTEGER DEFAULT 0, " + "WAKE_TIME_MIN INTEGER DEFAULT 0)");
        //insert default row in timer mode table
        ContentValues sleepModeValues = new ContentValues();
        sleepModeValues.put("IS_ENABLED", 0);
        sleepModeValues.put("SLEEP_TIME_MIN", 0);
        sleepModeValues.put("WAKE_TIME_MIN", 0);
        db.insert(TABLE_SLEEP_MODE_NAME, null, sleepModeValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPS_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMER_MODE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOCUS_MODE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDY_MODE_NAME);
        onCreate(db);
    }
}