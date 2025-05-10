package com.example.limitly;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppsFragment extends Fragment {

    private LinearLayout appList;

    private final List<String> blockedApps = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    public AppsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate the main layout
        View view = inflater.inflate(R.layout.fragment_apps, container, false);

        //get appList scrollView to assign appList
        appList = view.findViewById(R.id.scrollView2).findViewById(R.id.appList);

        //initialize SharedPreferences to store blocked apps locally
        sharedPreferences = requireContext().getSharedPreferences("LimitlyPrefs", Context.MODE_PRIVATE);

        //load saved blocked apps or create new empty set
        loadBlockedApps();

        //load and show installed apps list to ui
        loadInstalledApps();

        return view;
    }

    private void loadInstalledApps(){
        PackageManager pm = requireContext().getPackageManager();

        //get list of all installed apps
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        //query for apps with launcher intent
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);

        for(ResolveInfo resolveInfo : resolveInfos){
            ApplicationInfo appInfo = resolveInfo.activityInfo.applicationInfo;

            //inflate the app item layout (must be in res/layout/app_item.xml)
            View appItem = LayoutInflater.from(getContext()).inflate(R.layout.item_app, appList, false);

            //get views from app_item.xml
            ImageView icon = appItem.findViewById(R.id.appIcon);
            TextView name = appItem.findViewById(R.id.appName);
            SwitchMaterial toggle = appItem.findViewById(R.id.appToggle);

            // Set icon and app name
            icon.setImageDrawable(appInfo.loadIcon(pm));
            name.setText(appInfo.loadLabel(pm));

            //check if app is already blocked
            toggle.setChecked(blockedApps.contains(appInfo.packageName));

            //handle toggle switch state change
            toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (!blockedApps.contains(appInfo.packageName)) {
                        blockedApps.add(appInfo.packageName);
                    }
                } else {
                    blockedApps.remove(appInfo.packageName);
                }

                //log toggle state change
                Log.d("ToggleChange", "App: " + appInfo.packageName + " set to " + isChecked);

                //save to local storage
                saveBlockedApps();
            });

            //add app item view to list
            appList.addView(appItem);
        }

    }

    //save blocked apps in local storage
    private void saveBlockedApps(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> set = new HashSet<>(blockedApps);
        editor.putStringSet("blockedApps", set);
        editor.apply();

        //log saved state
        Log.d("BlockedApps", "Saved blocked apps: " + blockedApps);
    }

    //load blocked apps list from local storage
    private void loadBlockedApps(){
        Set<String> set = sharedPreferences.getStringSet("blockedApps", new HashSet<>());
        blockedApps.clear();
        blockedApps.addAll(set);

        //log loaded state
        Log.d("BlockedApps", "Loaded blocked apps: " + blockedApps);
    }

}
