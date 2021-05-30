package com.devankav.spotifyhue;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.devankav.spotifyhue.bridgeConnection.BridgeState;
import com.devankav.spotifyhue.bridgeConnection.BridgeStateObserver;
import com.devankav.spotifyhue.bridgeConnection.BridgeStatus;
import com.devankav.spotifyhue.bridgeConnection.HueConnector;

public class ConnectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        Bundle bundle = getIntent().getExtras();
        String ipAddress = bundle.getString("ipAddress");

        TextView textView = findViewById(R.id.ipLabel);
        textView.setText(ipAddress);

        HueConnector connector = new HueConnector(this);
        BridgeStatus bridgeStatus = connector.connect(ipAddress);
        bridgeStatus.registerObserver(updated -> {
            if (bridgeStatus.getState() != BridgeState.CONNECTED) {
                Log.d("ConnectActivity", bridgeStatus.getState().toString());
                connector.connect(ipAddress, bridgeStatus);
            } else {
                Log.d("ConnectActivity", "connected! Username: " + bridgeStatus.getUsername());
            }
        });
    }
}
