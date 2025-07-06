package com.s22010695.limitly.mode_helpers;

import android.content.Context;
import android.util.Log;

import com.s22010695.limitly.db_helpers.TimerModeTableHandler;

import java.util.HashMap;

public class TimerModeHelper {
    //declare objects
    private final Context context;
    private final TimerModeTableHandler dbHelper;

    //declare hashmaps to track start time, store unblock time for blocked apps and last time user exited the app of each app in milliseconds
    private static final HashMap<String, Long> appStartTimes = new HashMap<>();
    private static final HashMap<String, Long> lastAppExitTime = new HashMap<>();

    //declare variable for block all selected apps until this time
    private static long blockUntil = 0;

    //declare variable for reset app usage after 5 min
    private final long RESTART_THRESHOLD = 5 * 60 * 1000;

    public TimerModeHelper(Context context) {
        this.context = context;
        this.dbHelper = new TimerModeTableHandler(context);
    }

    //create method for get timer mode is enable or not
    public boolean isEnable(){
        return dbHelper.getIsEnable();
    }

    //create method for timer mode was active ot not
    public boolean isActive(){
        return true;
    }

    //create method for apply timer mode
    public boolean apply(String activeApp) {
        if (!dbHelper.getIsEnable()) return false;

        Log.d("TimerMode", "apply() called for: " + activeApp);

        //get current time in milliseconds
        long now = System.currentTimeMillis();

        //check if app currently blocked
        if (now < blockUntil) {
            Log.d("TimerMode", "Timer mode still blocking all apps.");
            return true;
        }

        //if not track this app yet
        if (!appStartTimes.containsKey(activeApp)){
            Long lastExit = lastAppExitTime.get(activeApp);
            boolean restart = lastExit == null || (now - lastExit) > RESTART_THRESHOLD;

            if (restart) {
                appStartTimes.put(activeApp, now); // Start new timer
                Log.d("TimerMode", "Started new usage timer for: " + activeApp);
            } else {
                Log.d("TimerMode", "Resuming previous usage timer for: " + activeApp);
            }

        }

        //get block and unblock times from db
        int blockMin = dbHelper.getBlockTime();
        int unblockMin = dbHelper.getUnblockTime();

        //convert block and unblock times to milliseconds
        long blockMilli = blockMin * 60 * 1000L;
        long unblockMilli = unblockMin * 60 * 1000L;

        //cal usage and check the need of block
        long usageDuration = now - appStartTimes.getOrDefault(activeApp, now);
        if (usageDuration >= blockMilli) {
            blockUntil = now + unblockMilli;
            //stop timers for all apps
            appStartTimes.clear();
            Log.d("TimerMode", "Timer mode activated. All selected apps blocked until: " + blockUntil);
            return true;
        }

        return false;
    }

    public void onAppExit(String app) {
        appStartTimes.remove(app);
        lastAppExitTime.put(app, System.currentTimeMillis());
        Log.d("TimerMode", "App exited: " + app);
    }

}
