package com.s22010695.limitly.mode_helpers;

import android.content.Context;
import android.util.Log;

import com.s22010695.limitly.db_helpers.AppsTableHandler;

import java.util.List;

public class MainFunction {
    //declare objects
    private final ActiveApp activeApp;
    private final TimerModeHelper timerMode;
    private final AppsTableHandler dbHelper;

    //declare list and variable for track selected apps and last used app
    private List<String> selectedApps;
    private String lastUsedApp = null;

    //create constructor to initialize all objects and variables
    public MainFunction(Context context){
        this.activeApp = new ActiveApp(context);
        this.dbHelper = new AppsTableHandler(context);
        this.selectedApps = dbHelper.getAllBlockedApps();
        this.timerMode = new TimerModeHelper(context);
    }

    public void run(){
        //get current active app
        String currentApp = activeApp.activeApp();

        //return if not app running
        if (currentApp == null) return;

        //check active app in user selected apps
        if (!selectedApps.contains(currentApp)){
            return;
        }

        //check if user switch app
        if (lastUsedApp != null && !lastUsedApp.equals(currentApp)){
            //notify to timer mode user exited the previous app
            timerMode.onAppExit(lastUsedApp);
        }

        //upgrade the used app
        lastUsedApp = currentApp;

        //activate modes base on priority
//        if (focusMode.isEnabled() && focusMode.shouldActivate()) {
//            focusMode.apply(currentApp);
//        } else if (studyMode.isEnabled() && studyMode.shouldActivate()) {
//            studyMode.apply(currentApp);
//        } else if (sleepMode.isEnabled() && sleepMode.shouldActivate()) {
//            sleepMode.apply(currentApp);
//        } else if (timerMode.isEnabled()) {
//            timerMode.apply(currentApp);
//        }
        if (timerMode.isEnable() && timerMode.isActive()){
            Log.d("MainFunction", "TimerMode is enabled and active. Applying...");
            timerMode.apply(currentApp);
        }
    }

    //if user reload selected apps change it live
    public void refreshSelectedApps(){
        this.selectedApps = dbHelper.getAllBlockedApps();
    }

}
