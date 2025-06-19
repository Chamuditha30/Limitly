package com.s22010695.limitly;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserTableHandler {
    //create objects
    private SQLiteDatabase db;
    private Context context;

    //create variables
    public static final String TABLE_NAME = "user";

    public UserTableHandler(Context context) {
        this.context = context;
    }

    //signup user method
    public boolean signup(String username, String password){
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID", 1);
        values.put("USERNAME", username);
        values.put("PASSWORD", password);

        //check user already exists, if it is ignore, if it is not add new
        long res = db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        return res != -1;
    }

    //login user method
    public boolean login(String username, String password){
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();

        //check if username & password db
        Cursor cursor = db.rawQuery("SELECT USERNAME, PASSWORD FROM user WHERE ID = 1", null);

        if(cursor.moveToFirst()){
            String user = cursor.getString(cursor.getColumnIndexOrThrow("USERNAME"));
            String pass = cursor.getString(cursor.getColumnIndexOrThrow("PASSWORD"));

            //compare given usernames and passwords with data
            return user.equals(username) && pass.equals(password);
        }

        cursor.close();
        return false;
    }

    //check username
    public boolean checkUsername(String username){
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();

        //check if username & password db
        Cursor cursor = db.rawQuery("SELECT USERNAME FROM user WHERE ID = 1", null);

        if(cursor.moveToFirst()){
            String answer = cursor.getString(cursor.getColumnIndexOrThrow("USERNAME"));

            //compare given usernames and passwords with data
            return answer.equals(username);
        }

        cursor.close();
        return false;
    }

    //update password
    public boolean updatePassword(String password){
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PASSWORD", password);

        //update the user with ID = 1
        int res = db.update(TABLE_NAME, values, "ID = ?", new String[]{"1"});
        return res != -1;
    }
}
