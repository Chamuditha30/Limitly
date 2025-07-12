package com.s22010695.limitly.mode_helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.s22010695.limitly.activities.BlockedOverlayActivity;
import com.s22010695.limitly.db_helpers.AppsTableHandler;

import java.util.List;

public class MainFunction {
    //declare objects
    private final Context context;
    private final ActiveApp activeApp;
    private final TimerModeHelper timerMode;
    private final SleepModeHelper sleepMode;
    private final StudyModeHelper studyMode;
    private final FocusModeHelper focusMode;
    private final AppsTableHandler dbHelper;

    //declare list and variable for track selected apps and last used app
    private List<String> selectedApps;
    private static MainFunction instance;
    private String lastUsedApp = null;

    //create variable to hold block apps state
    private boolean isBlockApps = false;

    //create constructor to initialize all objects and variables
    public MainFunction(Context context){
        this.context = context;
        this.activeApp = new ActiveApp(context);
        this.dbHelper = new AppsTableHandler(context);
        this.selectedApps = dbHelper.getAllBlockedApps();
        this.timerMode = new TimerModeHelper(context);
        this.sleepMode = new SleepModeHelper(context);
        this.studyMode = new StudyModeHelper(context);
        this.focusMode = new FocusModeHelper(context);
    }

    public void run(){
        refreshSelectedApps();

        //get current active app
        String currentApp = activeApp.activeApp();
        Log.d("MainFunction", "Current active app: " + currentApp);

        //return if not app running
        if (currentApp == null) {
            Log.d("MainFunction", "No active app detected");
            return;
        }

        //skip if current app is your overlay itself
        if (currentApp.equals("com.s22010695.limitly")) return;

        //check active app in user selected apps
        if (!selectedApps.contains(currentApp)){
            Log.d("MainFunction", "All selected apps " + selectedApps);
            Log.d("MainFunction", "Active app not in selected apps: " + currentApp);
            return;
        }

        //check if user switch app
        if (lastUsedApp != null && !lastUsedApp.equals(currentApp)){
            //notify to timer mode user exited the previous app
            timerMode.onAppExit(lastUsedApp);
        }

        //upgrade the used app
        lastUsedApp = currentApp;

        //create shared pref for save and update the current activated mode
        SharedPreferences prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String currentMode = prefs.getString("activatedMode", "none");

        //if a new day, reset blockCount
        resetBlockCountIfNewDay(prefs);

        //check if focus mode is enabled, if it is not check other modes
        if (focusMode.isEnable()) {
            checkFocusModeAndContinue(currentApp, currentMode, prefs);
            return;
        }

        //if focus mode not enabled check other modes
        continueModeCheck(currentApp, currentMode, prefs);
    }

    //async focus mode handler
    private void checkFocusModeAndContinue(String currentApp, String currentMode, SharedPreferences prefs) {
        focusMode.apply(isInZone -> {
            if (isInZone) {
                Log.d("MainFunction", "FocusMode is enabled and activated.");
                setModeAndUpdateOverlay("focus", true, currentMode, prefs);
            } else {
                //not in any zone continue checking other modes
                continueModeCheck(currentApp, currentMode, prefs);
            }
        });
    }

    //continue mode check for Study, Sleep & Timer
    private void continueModeCheck(String currentApp, String currentMode, SharedPreferences prefs) {
        boolean block = false;
        String newMode = "none";

        //study Mode
        if (studyMode.isEnable()) {
            studyMode.start();
            if (studyMode.apply()) {
                Log.d("MainFunction", "StudyMode is enabled and activated.");
                newMode = "study";
                block = true;
            }
        } else {
            studyMode.stop();
        }

        //sleep Mode
        if (!block && sleepMode.isEnable()) {
            if (sleepMode.apply()) {
                Log.d("MainFunction", "SleepMode is enabled and activated.");
                newMode = "sleep";
                block = true;
            }
        }

        //timer Mode
        if (!block && timerMode.isEnable()) {
            if (timerMode.apply(currentApp)) {
                Log.d("MainFunction", "TimerMode is enabled and activated.");
                newMode = "timer";
                block = true;

                //increment block count
                SharedPreferences.Editor editor = prefs.edit();
                int currentCount = prefs.getInt("blockCount", 0);
                editor.putInt("blockCount", currentCount + 1);
                editor.apply();
            }
        }

        //apply mode and show or hide overlay
        setModeAndUpdateOverlay(newMode, block, currentMode, prefs);
    }

    //update mode in SharedPreferences and control overlay
    private void setModeAndUpdateOverlay(String newMode, boolean block, String currentMode, SharedPreferences prefs) {
        if (!newMode.equals(currentMode)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("activatedMode", newMode);
            editor.apply();
        }

        isBlockApps = block;
        updateBlockScreen(context);
    }

    //show or hide the block screen overlay
    private void updateBlockScreen(Context context) {
        if (isBlockApps) {
            if (!BlockedOverlayActivity.isBlockingActive) {
                Log.d("MainFunction", "Showing overlay...");
                Intent intent = new Intent(context, BlockedOverlayActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } else {
            if (BlockedOverlayActivity.isBlockingActive) {
                Log.d("MainFunction", "Dismissing overlay...");
                BlockedOverlayActivity.dismissIfVisible();
            }
        }
    }

    private void resetBlockCountIfNewDay(SharedPreferences prefs) {
        String lastResetDate = prefs.getString("lastResetDate", "");
        String todayDate = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(new java.util.Date());

        if (!todayDate.equals(lastResetDate)) {
            //reset blockCount
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("blockCount", 0);
            editor.putString("lastResetDate", todayDate);
            editor.apply();
            Log.d("MainFunction", "blockCount reset for new day: " + todayDate);
        }
    }

    //if user reload selected apps change it live
    public void refreshSelectedApps(){
        this.selectedApps = dbHelper.getAllBlockedApps();
    }

}
