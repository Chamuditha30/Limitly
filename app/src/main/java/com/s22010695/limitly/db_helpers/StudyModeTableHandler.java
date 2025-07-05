package com.s22010695.limitly.db_helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class StudyModeTableHandler {
    //create objects
    private Context context;

    //create variables
    public static final String TABLE_NAME = "study_mode";

    public StudyModeTableHandler(Context context) {
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
}
