package com.s22010695.limitly.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.Manifest;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.s22010695.limitly.R;
import com.s22010695.limitly.mode_helpers.MainFunction;

public class ForegroundService extends Service {

    //declare objects
    MainFunction mainFunction;

    //declare variables
    private static final String CHANNEL_ID = "Foreground Service ID";

    //declare handler and runnable variables for periodic checking
    private Handler handler;
    private Runnable runnable;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "LIMITLY Foreground Service",
                    NotificationManager.IMPORTANCE_LOW
            );
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }

        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("LIMITLY is running")
                    .setContentText("Your smart control service is active")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .build();
        } else {
            notification = new Notification();
        }

        //permission check for Android 14+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

                Log.e("ForegroundService", "Missing required location permissions. Stopping service.");
                stopSelf();
                return START_NOT_STICKY;
            }

            //safe to use location-type FGS on Android 14+
            startForeground(1001, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //for Android 10 to 13, use same API safely
            startForeground(1001, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION);
        } else {
            //for Android 9 and below
            startForeground(1001, notification);
        }

        //create main function
        if (mainFunction == null){
            mainFunction = new MainFunction(this);
            startMainLoop();
        }

        return START_STICKY;
    }

    // Start periodic loop using Handler every 2 seconds
    private void startMainLoop() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                mainFunction.run();
                Log.d("ForegroundService", "Service running...");
                handler.postDelayed(this, 2000);
            }
        };
        handler.post(runnable);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
