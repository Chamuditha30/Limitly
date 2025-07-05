package com.s22010695.limitly.mode_helpers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.s22010695.limitly.activities.BlockedOverlayActivity;
import com.s22010695.limitly.db_helpers.TimerModeTableHandler;

import java.util.HashMap;
import java.util.List;

public class TimerModeHelper {
    //declare objects
    private final Context context;
    private final TimerModeTableHandler dbHelper;

    //declare hashmaps to track start time, store unblock time for blocked apps and last time user exited the app of each app in milliseconds
    private static final HashMap<String, Long> appStartTimes = new HashMap<>();
    private static final HashMap<String, Long> appBlockUntil = new HashMap<>();
    private static final HashMap<String, Long> lastAppExitTime = new HashMap<>();

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
    public void apply(String activeApp) {
        Log.d("TimerMode", "apply() called for: " + activeApp);

        //get current time in milliseconds
        long now = System.currentTimeMillis();

        //check if app currently blocked
        if (appBlockUntil.containsKey(activeApp)){
            long unblockAt = appBlockUntil.get(activeApp);
            if (now < unblockAt){
                Log.d("TimerMode", activeApp + " is blocked until " + unblockAt);

                //start blocking overlay activity on top
                showBlockScreen();

                return;
            }else {
                appBlockUntil.remove(activeApp);
                Log.d("TimerMode", activeApp + " is now unblocked");
            }
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
            appBlockUntil.put(activeApp, now + unblockMilli);
            appStartTimes.remove(activeApp);
            Log.d("TimerMode", "Blocked " + activeApp + " for " + unblockMin + " mins");

            //start blocking overlay activity on top
            showBlockScreen();
        }
    }

    private void showBlockScreen() {
        Intent intent = new Intent(context, BlockedOverlayActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void onAppExit(String app) {
        appStartTimes.remove(app);
        lastAppExitTime.put(app, System.currentTimeMillis());
        Log.d("TimerMode", "App exited: " + app);
    }

}
