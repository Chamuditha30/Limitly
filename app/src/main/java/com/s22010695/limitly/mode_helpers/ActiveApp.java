package com.s22010695.limitly.mode_helpers;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;

import java.util.List;

public class ActiveApp {

    //declare objects
    private final UsageStatsManager usm;

    //create constructor to pass context
    public ActiveApp(Context context){
        this.usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
    }

    //create method for get current open app in foreground
    public String activeApp(){
        //create start and end times to cal app open time
        long endTime = System.currentTimeMillis();
        long startTime = endTime - 10000;

        //get all apps list open between above time
        List<UsageStats> stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        if (stats == null || stats.isEmpty()){
            return null;
        }

        UsageStats recentStat = null;
        for(UsageStats usage: stats){
            if (recentStat == null || usage.getLastTimeUsed() > recentStat.getLastTimeUsed()){
                recentStat =usage;
            }
        }

        if (recentStat != null){
            return recentStat.getPackageName();
        }

        return null;
    }
}
