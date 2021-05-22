package com.devankav.spotifyhue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashscreen);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("bridgeMem", Context.MODE_PRIVATE);
        String recent = sharedPreferences.getString("recent", null);
        boolean connected = false;

        if (recent != null && connected) {
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SplashScreenActivity.this, ConnectActivity.class);
            startActivity(intent);
        }
    }
}
