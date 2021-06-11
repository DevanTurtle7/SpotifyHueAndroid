package com.devankav.spotifyhue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devankav.spotifyhue.bridgeConnection.BridgeState;
import com.devankav.spotifyhue.bridgeConnection.BridgeStatus;
import com.devankav.spotifyhue.bridgeConnection.BridgeConnector;

public class ConnectActivity extends Activity {

    private static boolean running = false;
    private static final int WAIT_TIME = 30000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        running = true;

        Bundle bundle = getIntent().getExtras();
        String ipAddress = bundle.getString("ipAddress");
        String id = bundle.getString("id");

        TextView textView = findViewById(R.id.ipLabel);
        textView.setText(ipAddress);

        long startTime = System.currentTimeMillis(); // Get the current time

        BridgeConnector connector = new BridgeConnector(this);
        BridgeStatus bridgeStatus = connector.connect(ipAddress);

        // TODO: Optimize with scheduler
        bridgeStatus.registerObserver(updated -> {
            if (running) {
                long currentTime = System.currentTimeMillis(); // Get the current time
                long timeDiff = currentTime - startTime;

                if (timeDiff < WAIT_TIME) {
                    if (bridgeStatus.getState() != BridgeState.CONNECTED) {
                        connector.connect(ipAddress, bridgeStatus);
                    } else {
                        // TODO: Update shared prefs
                        Log.d("ConnectActivity", "connected! Username: " + bridgeStatus.getUsername());

                        Intent intent = new Intent(ConnectActivity.this, MainActivity.class);

                        // Add flags to clear activity stack
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        // Pass bridge info to main
                        intent.putExtra("ipAddress", ipAddress);
                        intent.putExtra("id", id);
                        intent.putExtra("username", bridgeStatus.getUsername());

                        // Go to main
                        startActivity(intent);
                    }
                } else {
                    this.finish();
                }
            }
        });

        Thread progressUpdater = new Thread(new Runnable() {
            @Override
            public void run() {
                ProgressBar progressBar = findViewById(R.id.connectionProgress);

                while (running) {
                    long currentTime = System.currentTimeMillis(); // Get the current time
                    long timeDiff = currentTime - startTime;
                    double percentage = timeDiff / (double) WAIT_TIME;
                    double progress = percentage * 100;

                    progressBar.setProgress((int) progress);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        progressUpdater.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        running = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        running = true;
    }
}
