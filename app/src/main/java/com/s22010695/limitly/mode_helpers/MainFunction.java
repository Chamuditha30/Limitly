package com.s22010695.limitly.mode_helpers;

import android.content.Context;
import android.content.Intent;
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
    private final AppsTableHandler dbHelper;

    //declare list and variable for track selected apps and last used app
    private List<String> selectedApps;
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
    }

    public void run(){
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

        //create flag to check block or not
        boolean block = false;

        //priority: Focus > Study > Sleep > Timer
        //sleep mode
        if (sleepMode.isEnable()) {
            boolean result = sleepMode.apply();
            if (result) {
                Log.d("MainFunction", "SleepMode is enabled and active.");
                block = true;
            }
        }

        //timer mode
        if (!block && timerMode.isEnable()) {
            boolean result = timerMode.apply(currentApp);
            if (result) {
                Log.d("MainFunction", "TimerMode is enabled and active.");
                block = true;
            }
        }

        //if no mode is active, remove overlay
        isBlockApps = block;
        updateBlockScreen(context);
    }

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

    //if user reload selected apps change it live
    public void refreshSelectedApps(){
        this.selectedApps = dbHelper.getAllBlockedApps();
    }

}
