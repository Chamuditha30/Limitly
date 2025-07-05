package com.s22010695.limitly.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.Manifest;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.s22010695.limitly.R;

public class ForegroundService extends Service {

    private static final String CHANNEL_ID = "Foreground Service ID";

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

        //simulate work
        new Thread(() -> {
            while (true) {
                Log.e("Service", "Service is running...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
