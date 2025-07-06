package com.s22010695.limitly.activities;

import android.annotation.SuppressLint;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.s22010695.limitly.R;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class BlockedOverlayActivity extends AppCompatActivity {

    //declare variables
    public static boolean isBlockingActive = false;
    public static BlockedOverlayActivity instance = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_blocked_overlay);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //make activity full screen and top
        getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        setContentView(R.layout.activity_blocked_overlay);

        //prevent touching outside to dismiss
        this.setFinishOnTouchOutside(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isBlockingActive = false;
        instance = null;
    }

    public static void dismissIfVisible() {
        if (instance != null) {
            instance.finish();
        }
    }

    //disable back button
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
    }
}