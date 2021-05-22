/**
 * The splash screen activity. Displays a splash screen and then navigates the user to another
 * activity (connection page or home page).
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class SplashScreenActivity extends Activity {

    public static final int SPLASH_SCREEN_LENGTH = 2000; // The length of the splash screen (in milliseconds)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashscreen);
        long startTime = System.currentTimeMillis(); // Get the current time

        // Attempt to get the info of the last bridge that was connected
        SharedPreferences sharedPreferences = getSharedPreferences("bridgeMem", Context.MODE_PRIVATE);
        String recentIP = sharedPreferences.getString("recentIP", null);
        String recentUsername = sharedPreferences.getString("recentUsername", null);

        HueConnector hueConnector = new HueConnector(this);
        BridgeStatus status = hueConnector.connect("192.168.254.65");

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        elapsedTime = elapsedTime > 0 ? 0 : elapsedTime;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == BridgeStatus.CONNECTED) {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                } else { // May want to add another else/if for push button state. Would probably never happen though.
                    Intent intent = new Intent(SplashScreenActivity.this, ConnectActivity.class);
                    startActivity(intent);
                }
            }
        }, SPLASH_SCREEN_LENGTH - elapsedTime);
    }
}
