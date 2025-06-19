package com.s22010695.limitly;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ZonesTableHandler {
    //create objects
    private SQLiteDatabase db;
    private Context context;

    //create variables
    public static final String TABLE_NAME = "locations";

    public ZonesTableHandler(Context context) {
        this.context = context;
    }

    //insert zones method
    public boolean insertZone(String title, double latitude, double longitude){
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TITLE", title);
        values.put("LATITUDE", latitude);
        values.put("LONGITUDE", longitude);

        //check zone already exists, if it is ignore, if it is not add new
        long res = db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        return res != -1;
    }

    //get all zones method
    public List<ZoneInfoModel> getAllZones() {
        List<ZoneInfoModel> zoneList = new ArrayList<>();
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("TITLE"));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("LATITUDE"));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("LONGITUDE"));

                zoneList.add(new ZoneInfoModel(id, title, latitude, longitude));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return zoneList;
    }

    //delete zones method
    public boolean deleteZone(int id){
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();

        long res = db.delete(TABLE_NAME, "ID = ?", new String[]{String.valueOf(id)});
        return res != -1;
    }
}
