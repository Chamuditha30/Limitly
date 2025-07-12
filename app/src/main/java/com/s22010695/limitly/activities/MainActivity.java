package com.s22010695.limitly.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.s22010695.limitly.fragments.AppsFragment;
import com.s22010695.limitly.services.ForegroundService;
import com.s22010695.limitly.fragments.HomeFragment;
import com.s22010695.limitly.R;
import com.s22010695.limitly.fragments.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //declare objects
    BottomNavigationView navbar;

    //declare fragments
    HomeFragment homeFragment = new HomeFragment();
    AppsFragment appsFragment = new AppsFragment();
    SettingsFragment settingsFragment = new SettingsFragment();

    //declare variables
    private static final int REQUEST_PERMISSIONS_CODE = 100;
    private static final int REQUEST_USAGE_ACCESS_CODE = 101;
    private static final int REQUEST_BACKGROUND_LOCATION_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //get navbar by id
        navbar = findViewById(R.id.navbar);

        //set default fragment as home fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        //set navigation between home, apps, settings fragments
        navbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                    return true;

                } else if (id == R.id.apps) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, appsFragment).commit();
                    return true;

                } else if (id == R.id.settings) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, settingsFragment).commit();
                    return true;
                }
                return false;
            }
        });

        //start permission check flow
        if (!hasUsageAccess()) {
            //show explanation dialog before requesting Usage Access
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Usage Access Required")
                    .setMessage("LIMITLY needs Usage Access permission to detect app usage to track your app usage.")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, which) -> requestUsageAccess())
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        Toast.makeText(this, "Usage Access is required", Toast.LENGTH_LONG).show();
                    })
                    .show();
        } else {
            requestForegroundPermissionsIfNeeded();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (hasAllPermissions() && !isForegroundServiceRunning()) {
            requestIgnoreBatteryOptimizations();
            startForegroundService();
        }
    }

    private boolean hasUsageAccess() {
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());

        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private void requestUsageAccess() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivityForResult(intent, REQUEST_USAGE_ACCESS_CODE);
    }

    private void requestForegroundPermissionsIfNeeded() {
        List<String> permissions = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.FOREGROUND_SERVICE);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.FOREGROUND_SERVICE_LOCATION);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE_DATA_SYNC) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.FOREGROUND_SERVICE_DATA_SYNC);
            }
        }

        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissions.toArray(new String[0]),
                    REQUEST_PERMISSIONS_CODE);
        } else {
            // Foreground permissions are all granted, now request background location permission if needed
            requestBackgroundLocationPermissionIfNeeded();
        }
    }

    // New method to request background location permission
    private void requestBackgroundLocationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // Android 10+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        REQUEST_BACKGROUND_LOCATION_CODE);
            } else {
                // Background location permission already granted - start service if not running
                if (!isForegroundServiceRunning()) {
                    requestIgnoreBatteryOptimizations();
                    startForegroundService();
                }
            }
        } else {
            // Background location permission not required below Android 10
            if (!isForegroundServiceRunning()) {
                requestIgnoreBatteryOptimizations();
                startForegroundService();
            }
        }
    }

    private boolean hasAllPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE_DATA_SYNC) == PackageManager.PERMISSION_GRANTED;
    }

    private void startForegroundService() {
        Intent intent = new Intent(this, ForegroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    private boolean isForegroundServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ForegroundService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    //handle runtime permission result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                // Foreground permissions granted, now request background location
                requestBackgroundLocationPermissionIfNeeded();
            } else {
                Toast.makeText(this, "All permissions are required for the service to run.", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_BACKGROUND_LOCATION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Background location permission granted
                if (!isForegroundServiceRunning()) {
                    requestIgnoreBatteryOptimizations();
                    startForegroundService();
                }
            } else {
                Toast.makeText(this, "Background location permission is required for persistent location tracking.", Toast.LENGTH_LONG).show();
            }
        }
    }

    //handle result from usage access screen (optional)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_USAGE_ACCESS_CODE) {
            if (hasUsageAccess()) {
                //now that Usage Access is granted, show permission popup
                requestForegroundPermissionsIfNeeded();

                //request permission for overlay
                if (!Settings.canDrawOverlays(this)) {
                    new androidx.appcompat.app.AlertDialog.Builder(this)
                            .setTitle("Overlay Permission Needed")
                            .setMessage("LIMITLY needs permission to display blocking overlays on top of apps.")
                            .setCancelable(false)
                            .setPositiveButton("Allow", (dialog, which) -> {
                                Intent overlayIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:" + getPackageName()));
                                startActivity(overlayIntent);
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> {
                                Toast.makeText(this, "Overlay permission is required to block apps visually.", Toast.LENGTH_LONG).show();
                            })
                            .show();
                }
            } else {
                Toast.makeText(this, "Usage Access is required for the app to function.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void requestIgnoreBatteryOptimizations() {
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        String packageName = getPackageName();

        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);
        }
    }
}