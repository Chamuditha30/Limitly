package com.s22010695.limitly;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    //declare objects
    BottomNavigationView navbar;

    //declare fragments
    HomeFragment homeFragment = new HomeFragment();
    AppsFragment appsFragment = new AppsFragment();
    SettingsFragment settingsFragment = new SettingsFragment();

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
    }
}