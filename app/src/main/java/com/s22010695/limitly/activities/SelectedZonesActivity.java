package com.s22010695.limitly.activities;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.s22010695.limitly.R;
import com.s22010695.limitly.db_helpers.ZoneInfoModel;
import com.s22010695.limitly.db_helpers.ZoneListAdapter;
import com.s22010695.limitly.db_helpers.ZonesTableHandler;

import java.util.ArrayList;
import java.util.List;

public class SelectedZonesActivity extends AppCompatActivity {

    //declare objects
    private List<ZoneInfoModel> zones = new ArrayList<>();
    private ZonesTableHandler dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_selected_zones);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //get recycler view and display zones
        RecyclerView recyclerView = findViewById(R.id.zonesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //get zones from db
        dbHelper = new ZonesTableHandler(this);
        zones = dbHelper.getAllZones();

        //connect recycler view with adapter
        ZoneListAdapter adapter = new ZoneListAdapter(zones, dbHelper);
        recyclerView.setAdapter(adapter);
    }

    //navigate back to settings fragment
    public void navBackToSettingsFragment(View view) {
        finish();
    }
}