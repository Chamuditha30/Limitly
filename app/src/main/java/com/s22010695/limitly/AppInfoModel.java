package com.s22010695.limitly;

import android.graphics.drawable.Drawable;

//create app model class to store each app info
public class AppInfoModel {

    //declare variables
    private String appName;
    private String packageName;
    private Drawable appIcon;
    private boolean isBlocked;

    //set constructor
    public AppInfoModel(String appName, String packageName, Drawable appIcon) {
        this.appName = appName;
        this.packageName = packageName;
        this.appIcon = appIcon;
        this.isBlocked = false;
    }

    //create getter and setter methods
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {this.appIcon = appIcon; }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
