package com.s22010695.limitly.mode_helpers;

public class BlockedApp {

    //declare variable
    private static String blockedApp = null;

    //create method to set blocked app
    public static void setBlockedApp(String packageName) {
        blockedApp = packageName;
    }

    //create method to get blocked app
    public static String getBlockedApp() {
        return blockedApp;
    }

    //create method to clear blocked app
    public static void clearBlockedApp() {
        blockedApp = null;
    }

    //create method to return block or not
    public static boolean isAppBlocked(String packageName) {
        return blockedApp != null && blockedApp.equals(packageName);
    }
}
