package com.s22010695.limitly.db_helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SleepModeTableHandler {
    //create objects
    private final SQLiteDatabase db;

    //create variables
    public static final String TABLE_NAME = "sleep_mode";

    public SleepModeTableHandler(Context context) {
        db = DatabaseHelper.getInstance(context).getWritableDatabase();
    }


    //update is enable
    public void updateIsEnable(boolean isEnabled){
        ContentValues values = new ContentValues();
        values.put("IS_ENABLED", isEnabled ? 1 : 0);

        //update row where id  = 1
        db.update(TABLE_NAME, values, "ID = 1", null);
    }

    //update sleep time
    public void updateSleepTime(int sleepTime){
        ContentValues values = new ContentValues();
        values.put("SLEEP_TIME_MIN", sleepTime);

        //update row where id  = 1
        db.update(TABLE_NAME, values, "ID = 1", null);
    }

    //update wake time
    public void updateWakeTime(int wakeTime){
        ContentValues values = new ContentValues();
        values.put("WAKE_TIME_MIN", wakeTime);

        //update row where id  = 1
        db.update(TABLE_NAME, values, "ID = 1", null);
    }

    //get is enable
    public boolean getIsEnable(){
        boolean isEnabled = false;

        //get row id = 1
        Cursor cursor = db.query(TABLE_NAME, new String[]{"IS_ENABLED"}, "ID = ?", new String[]{String.valueOf("1")}, null, null, null);

        //get int value from 1st column
        if (cursor.moveToFirst()){
            isEnabled = cursor.getInt(0) == 1;
        }
        cursor.close();

        return isEnabled;
    }

    //get sleep time
    public int getSleepTime(){
        int sleepTime = 0;

        //get row id = 1
        Cursor cursor = db.query(TABLE_NAME, new String[]{"SLEEP_TIME_MIN"}, "ID = ?", new String[]{String.valueOf("1")}, null, null, null);
        if (cursor.moveToFirst()){
            sleepTime = cursor.getInt(0);
        }
        cursor.close();

        return sleepTime;

    }

    //get wake time
    public int getWakeTime(){
        int wakeTime = 0;

        //get row id = 1
        Cursor cursor = db.query(TABLE_NAME, new String[]{"WAKE_TIME_MIN"}, "ID = ?", new String[]{String.valueOf("1")}, null, null, null);
        if (cursor.moveToFirst()){
            wakeTime = cursor.getInt(0);
        }
        cursor.close();

        return wakeTime;

    }
}
