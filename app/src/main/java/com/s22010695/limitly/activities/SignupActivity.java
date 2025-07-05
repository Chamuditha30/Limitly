package com.s22010695.limitly.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.s22010695.limitly.R;
import com.s22010695.limitly.db_helpers.UserTableHandler;

public class SignupActivity extends AppCompatActivity {

    //declare objects
    EditText username, password;
    UserTableHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //connect database
        dbHandler = new UserTableHandler(this);

        //get inputs by id
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
    }

    //navigate to login activity using intent
    public void signup(View view) {
        //convert inputs to strings
        String user = username.getText().toString();
        String pass = password.getText().toString();

        //check user enter both
        if (user.isEmpty() || pass.isEmpty()){
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            boolean res = dbHandler.signup(user, pass);

            if (res){
                username.setText("");
                password.setText("");
                Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "You are already signed up. Please login", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }

        finish();
    }

    public void navToLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}