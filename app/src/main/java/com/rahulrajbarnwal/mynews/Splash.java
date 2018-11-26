package com.rahulrajbarnwal.mynews;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rahulrajbarnwal.mynews.activity.Dashboard;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent in = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(in);
                finish();
            }
        }, 2000);
    }
}
