package com.s22010695.limitly;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//create a class for apps table methods
public class AppsTableHandler {
    //create objects
    private SQLiteDatabase db;
    private Context context;

    //create variables
    public static final String TABLE_NAME = "apps";

    public AppsTableHandler(Context context) {
        this.context = context;
    }

    //insert app state method
    public void updateAppState(String packageName, boolean isBlocked){
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PACKAGE_NAME", packageName);
        values.put("IS_BLOCKED", isBlocked ? 1 : 0);

        //check package already exists, if it is replace, if it is not add new
        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    //get all app states method
    public boolean getAppsStates(String packageName){
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{"IS_BLOCKED"}, "PACKAGE_NAME = ?", new String[]{packageName}, null, null, null);
        boolean isBlocked = false;

        if(cursor.moveToFirst()){
            isBlocked = cursor.getInt(0) == 1;
        }

        cursor.close();
        return isBlocked;
    }
}