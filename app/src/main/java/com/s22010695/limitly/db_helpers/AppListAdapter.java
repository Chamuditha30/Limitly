package com.s22010695.limitly.db_helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.s22010695.limitly.R;

import java.util.List;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.AppViewHolder> {

    //declare objects
    private List<AppInfoModel> appList;
    private AppsTableHandler dbHelper;

    //create constructor
    public AppListAdapter(List<AppInfoModel> appList, AppsTableHandler dbHelper) {
        this.appList = appList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public AppListAdapter.AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //create new item for each row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_row, parent, false);

        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppListAdapter.AppViewHolder holder, int position) {

        //set data into view given position (app name, icon, state)
        AppInfoModel app = appList.get(position);

        holder.appName.setText(app.getAppName());
        holder.appIcon.setImageDrawable(app.getAppIcon());
        holder.blockSwitch.setOnCheckedChangeListener(null);
        holder.blockSwitch.setChecked(app.isBlocked());

        //get switch block/unblock state
        holder.blockSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            app.setBlocked(isChecked);
            dbHelper.updateAppState(app.getPackageName(), isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    static class AppViewHolder extends RecyclerView.ViewHolder {

        //declare objects
        ImageView appIcon;
        TextView appName;
        SwitchMaterial blockSwitch;

        //get row elements by id
        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            appIcon = itemView.findViewById(R.id.appIcon);
            appName = itemView.findViewById(R.id.appName);
            blockSwitch = itemView.findViewById(R.id.blockSwitch);
        }
    }
}
