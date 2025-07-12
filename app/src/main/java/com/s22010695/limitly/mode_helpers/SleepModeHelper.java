package com.s22010695.limitly.mode_helpers;

import android.content.Context;
import android.util.Log;

import com.s22010695.limitly.db_helpers.SleepModeTableHandler;

import java.util.Calendar;

public class SleepModeHelper {
    //declare objects
    private final SleepModeTableHandler dbHelper;

    //declare variable to track active or not
    private boolean isActive = false;

    //create constructor
    public SleepModeHelper(Context context) {
        this.dbHelper = new SleepModeTableHandler(context);
    }

    public boolean isEnable() {
        return dbHelper.getIsEnable();
    }

    public boolean apply() {
        if (!dbHelper.getIsEnable()) return false;

        int sleepMin = dbHelper.getSleepTime();
        int wakeMin = dbHelper.getWakeTime();
        int nowMin = getCurrentTimeInMinutes();

        Log.d("SleepMode", "sleepMin: " + sleepMin + ", wakeMin: " + wakeMin + ", nowMin: " + nowMin);

        if (sleepMin < wakeMin) {
            //sleep and wake on same day (22:00 – 06:00)
            isActive = nowMin >= sleepMin && nowMin < wakeMin;
        } else {
            //sleep crosses midnight (23:00 – 07:00)
            isActive = nowMin >= sleepMin || nowMin < wakeMin;
        }

        Log.d("SleepMode", "Current time: " + nowMin + " min, Block: " + isActive);
        return isActive;
    }

    private int getCurrentTimeInMinutes() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return hour * 60 + minute;
    }
}
