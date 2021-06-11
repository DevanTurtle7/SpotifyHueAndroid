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
import android.util.Log;

import com.devankav.spotifyhue.bridgeConnection.BridgeConnector;
import com.devankav.spotifyhue.bridgeConnection.BridgeState;
import com.devankav.spotifyhue.bridgeConnection.BridgeStatusListenable;
import com.devankav.spotifyhue.listeners.BridgeStateListener;
import com.devankav.spotifyhue.observers.BridgeStateObserver;
import com.devankav.spotifyhue.bridgeConnection.BridgeStatusObservable;

public class SplashScreenActivity extends Activity {

    public static final int SPLASH_SCREEN_LENGTH = 2000; // The length of the splash screen (in milliseconds)

    /**
     * Exits out of the splash screen in a given amount of time and navigates to a given page. The splash screen should
     * last a minimum of SPLASH_SCREEN_LENGTH
     *
     * @param intent    The new page being navigated to
     * @param startTime When the splash screen was started
     */
    public void exitSplashScreen(Intent intent, long startTime) {
        long endTime = System.currentTimeMillis(); // Get the current time
        long elapsedTime = endTime - startTime; // Get the elapsed time (how long the user has been waiting on the splash screen)
        // Determine how much longer the user should wait on the splash screen, such that they are on the screen for a minimum of SPLASH_SCREEN_LENGTH
        long splashScreenDuration = SPLASH_SCREEN_LENGTH - elapsedTime;

        // Make sure the duration is not negative
        if (splashScreenDuration < 0) {
            splashScreenDuration = 0;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent); // Start the new activity
            }
        }, splashScreenDuration); // Wait the given time
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashscreen);
        long startTime = System.currentTimeMillis(); // Get the current time

        // Attempt to get the info of the last bridge that was connected
        SharedPreferences sharedPreferences = getSharedPreferences("recentBridge", Context.MODE_PRIVATE);
        String ipAddress = sharedPreferences.getString("ipAddress", null);
        String id = sharedPreferences.getString("id", null);
        String username = sharedPreferences.getString("username", null);

        Log.d("SplashScreen", "ip: " + ipAddress);
        Log.d("SplashScreen", "id: " + id);
        Log.d("SplashScreen", "user: " + username);

        if (ipAddress != null && username != null && id != null) { // Check if the previous info exists
            BridgeConnector bridgeConnector = new BridgeConnector(this); // Create a new BridgeConnector instance
            BridgeStatusListenable bridgeState = bridgeConnector.reconnect(ipAddress, username); // Attempt to connect to the bridge

            // Create and register a new bridge state observer
            bridgeState.registerListener(new BridgeStateListener() {
                @Override
                public void finished(BridgeState result) {
                    // Navigate to the home page if the bridge was connected. Navigate to the connect page otherwise.
                    boolean connected = result == BridgeState.CONNECTED;

                    Intent intent = new Intent(
                            SplashScreenActivity.this,
                            connected ? MainActivity.class : DiscoveryActivity.class
                    );

                    if (connected) { // If going to main, pass bridge info
                        intent.putExtra("ipAddress", ipAddress);
                        intent.putExtra("id", id);
                        intent.putExtra("username", username);
                    }

                    exitSplashScreen(intent, startTime); // Exit the splash screen
                }
            });
        } else {
            Intent intent = new Intent(SplashScreenActivity.this, DiscoveryActivity.class);
            exitSplashScreen(intent, startTime); // Exit the splash screen
        }
    }
}
