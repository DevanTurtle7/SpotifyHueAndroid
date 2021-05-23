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

public class SplashScreenActivity extends Activity {

    public static final int SPLASH_SCREEN_LENGTH = 2000; // The length of the splash screen (in milliseconds)

    public long getSplashScreenDuration(long startTime) {
        long endTime = System.currentTimeMillis(); // Get the current time
        long elapsedTime = endTime - startTime; // Get the elapsed time (how long the user has been waiting on the splash screen)
        // Determine how much longer the user should wait on the splash screen, such that they are on the screen for a minimum of SPLASH_SCREEN_LENGTH
        long splashScreenDuration = SPLASH_SCREEN_LENGTH - elapsedTime;

        // Make sure the duration is not negative
        if (splashScreenDuration < 0) {
            splashScreenDuration = 0;
        }

        return splashScreenDuration;
    }

    public void exitSplashScreen(Intent intent, long splashScreenDuration) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, splashScreenDuration);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashscreen);
        long startTime = System.currentTimeMillis(); // Get the current time

        // Attempt to get the info of the last bridge that was connected
        SharedPreferences sharedPreferences = getSharedPreferences("bridgeMem", Context.MODE_PRIVATE);
        String recentIP = sharedPreferences.getString("recentIP", null);
        String recentUsername = sharedPreferences.getString("recentUsername", null);

        if (recentIP != null && recentUsername != null) { // Check if the previous info exists
            HueConnector hueConnector = new HueConnector(this); // Create a new HueConnector instance
            BridgeState bridgeState = hueConnector.connect(""); // Attempt to connect to the bridge
            // TODO: Change to reconnect() instead of connect

            bridgeState.registerObserver(new BridgeStateObserver() {
                @Override
                public void BridgeStateUpdated(BridgeStatus bridgeStatus) {
                    Intent intent = new Intent(
                            SplashScreenActivity.this,
                            bridgeStatus == BridgeStatus.CONNECTED ? MainActivity.class : ConnectActivity.class
                    );

                    long splashScreenDuration = getSplashScreenDuration(startTime); // Get the splash screen duration
                    exitSplashScreen(intent, splashScreenDuration);
                }
            });
        } else {
            Intent intent = new Intent(SplashScreenActivity.this, ConnectActivity.class);
            long splashScreenDuration = getSplashScreenDuration(startTime); // Get the splash screen duration
            exitSplashScreen(intent, splashScreenDuration);
        }
    }
}
