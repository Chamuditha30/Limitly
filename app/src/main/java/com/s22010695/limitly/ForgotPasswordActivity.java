package com.s22010695.limitly;

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

public class ForgotPasswordActivity extends AppCompatActivity {

    //declare objects
    EditText username;
    UserTableHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //connect database
        dbHandler = new UserTableHandler(this);

        //get inputs by id
        username = findViewById(R.id.username);
    }

    public void navToChangePasswordActivity(View view) {
        //convert inputs to strings
        String user = username.getText().toString();

        //check user enter username
        if (user.isEmpty()){
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            boolean res = dbHandler.checkUsername(user);

            if (res){
                username.setText("");
                Intent intent = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Invalid username", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
    }
}