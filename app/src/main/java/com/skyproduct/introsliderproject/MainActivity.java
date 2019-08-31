package com.skyproduct.introsliderproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_start_slider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SliderPrefManager(MainActivity.this).setStartSlider(true);
                Intent intent = new Intent(MainActivity.this,IntroSliderActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
