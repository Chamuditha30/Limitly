package com.s22010695.limitly.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.s22010695.limitly.db_helpers.AppInfoModel;
import com.s22010695.limitly.db_helpers.AppListAdapter;
import com.s22010695.limitly.db_helpers.AppsTableHandler;
import com.s22010695.limitly.R;

import java.util.ArrayList;
import java.util.List;

public class AppsFragment extends Fragment {

    //declare objects
    private List<AppInfoModel> userInstalledApps = new ArrayList<>();
    private AppsTableHandler dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apps, container, false);

        //get recycler view and display apps
        RecyclerView recyclerView = view.findViewById(R.id.appsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        //create db
        dbHelper = new AppsTableHandler(requireContext());

        //clear and get user installed apps
        userInstalledApps.clear();
        getUserInstalledApps();

        //connect recycler view with adapter
        AppListAdapter adapter = new AppListAdapter(userInstalledApps, dbHelper);
        recyclerView.setAdapter(adapter);

        return view;
    }

    //get user installed apps
    private void getUserInstalledApps(){
        //get user installed apps using PackageManager
        PackageManager packageManager = requireContext().getPackageManager();

        //create intent for get launchable apps
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        //get all launchable apps using intent
        List<ResolveInfo> resolvedApps = packageManager.queryIntentActivities(intent, 0);

        //get each app info in resolvedApps
        for(ResolveInfo resolveInfo : resolvedApps){
            String appName = resolveInfo.loadLabel(packageManager).toString();
            String packageName = resolveInfo.activityInfo.packageName;
            Drawable appIcon = resolveInfo.loadIcon(packageManager);

            //prevent get this app
            if(appName.equals("Limitly")){
                continue;
            };

            //fetch app state from db and apply
            boolean isBlocked = dbHelper.getAppsStates(packageName);
            AppInfoModel app = new AppInfoModel(appName, packageName, appIcon);
            app.setBlocked(isBlocked);
            userInstalledApps.add(app);
        }
    }
}