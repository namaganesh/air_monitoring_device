package com.example.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.facebook.login.LoginManager;

import java.util.Arrays;

public class HomeActivity extends AppCompatActivity {
ImageView home_icon;
    ImageView bell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        home_icon = findViewById(R.id.home_icon);


        home_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, DeviceActivity.class);
                startActivity(i);
                finish();

            }
        });
       // startActivity(new Intent(HomeActivity.this,DeviceActivity.class));
    }
}