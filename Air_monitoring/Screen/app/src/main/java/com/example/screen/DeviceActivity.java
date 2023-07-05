package com.example.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

public class DeviceActivity extends AppCompatActivity {

    ImageView left_arrow;
    ImageView add;
    EditText search;
    ImageView tab_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        left_arrow = (ImageView)findViewById(R.id.left_arrow);;
        left_arrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view1) {
                Intent i = new Intent(DeviceActivity.this, MonitorActivity.class);
                startActivity(i);
            }
        });

    }
}