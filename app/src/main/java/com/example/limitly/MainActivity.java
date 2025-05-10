package com.example.limitly;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //find & assign navbar id to bottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.navbar);

        //display default page as HomeFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.main, new HomeFragment()).commit();

        //mark the selected tab is home in navbar item
        bottomNavigationView.setSelectedItemId(R.id.homeNav);

        //handle page navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.homeNav) {
                fragment = new HomeFragment();
            } else if (itemId == R.id.appsNav) {
                fragment = new AppsFragment();
            } else if (itemId == R.id.settingsNav) {
                fragment = new SettingsFragment();
            }

            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main, fragment).commit();
                return true;
            }

            return false;
        });
    }
}