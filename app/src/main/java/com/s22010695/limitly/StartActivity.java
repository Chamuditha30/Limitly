package com.s22010695.limitly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if user is new or existing using shared preferences
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        boolean isNewUser = prefs.getBoolean("isNewUser", true);

        //if user is new, navigate to welcome activity if it is not, navigate to login activity
        if(isNewUser){
            startActivity(new Intent(this, WelcomeActivity.class));

            //set isNewUser to false in shared preferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isNewUser", false);
            editor.apply();

        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}