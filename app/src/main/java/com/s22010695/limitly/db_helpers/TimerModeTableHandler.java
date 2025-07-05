package com.s22010695.limitly.db_helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TimerModeTableHandler {
    //create objects
    private Context context;

    //create variables
    public static final String TABLE_NAME = "timer_mode";

    public TimerModeTableHandler(Context context) {
        this.context = context;
    }

    //update is enable
    public void updateIsEnable(boolean isEnabled){
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("IS_ENABLED", isEnabled ? 1 : 0);

        //update row where id  = 1
        db.update(TABLE_NAME, values, "ID = 1", null);

        db.close();
    }

    //update block time
    public void updateBlockTime(int blockTime){
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("BLOCK_TIME_MIN", blockTime);

        //update row where id  = 1
        db.update(TABLE_NAME, values, "ID = 1", null);

        db.close();
    }

    //update block time
    public void updateUnblockTime(int unblockTime){
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("UNBLOCK_TIME_MIN", unblockTime);

        //update row where id  = 1
        db.update(TABLE_NAME, values, "ID = 1", null);

        db.close();
    }

    //get is enable
    public boolean getIsEnable(){
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
        boolean isEnabled = false;

        //get row id = 1
        Cursor cursor = db.query(TABLE_NAME, new String[]{"IS_ENABLED"}, "ID = ?", new String[]{String.valueOf("1")}, null, null, null);

        //get int value from 1st column
        if (cursor.moveToFirst()){
            isEnabled = cursor.getInt(0) == 1;
        }
        cursor.close();
        db.close();

        return isEnabled;
    }

    //get block time
    public int getBlockTime(){
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
        int blockTime = 15;

        //get row id = 1
        Cursor cursor = db.query(TABLE_NAME, new String[]{"BLOCK_TIME_MIN"}, "ID = ?", new String[]{String.valueOf("1")}, null, null, null);
        if (cursor.moveToFirst()){
            blockTime = cursor.getInt(0);
        }
        cursor.moveToFirst();

        cursor.close();
        db.close();

        return blockTime;

    }

    //get unblock time
    public int getUnblockTime(){
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
        int unblockTime = 30;

        Cursor cursor = db.query(TABLE_NAME, new String[]{"UNBLOCK_TIME_MIN"}, "ID = ?", new String[]{String.valueOf("1")}, null, null, null);
        if (cursor.moveToFirst()){
            unblockTime = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return unblockTime;
    }

}
