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
        String recentIP = sharedPreferences.getString("recentIP", null);
        String recentUsername = sharedPreferences.getString("recentUsername", null);

        HueConnector hueConnector = new HueConnector(getApplicationContext());
        BridgeStatus status = hueConnector.connect("192.168.254.65");

        if (status == BridgeStatus.CONNECTED) {
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
        } else { // May want to add another else/if for push button state. Would probably never happen though.
            Intent intent = new Intent(SplashScreenActivity.this, ConnectActivity.class);
            startActivity(intent);
        }
    }
}
