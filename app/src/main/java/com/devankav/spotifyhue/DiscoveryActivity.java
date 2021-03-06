/**
 * The page that allows the user to connect to Philips hue bridges.
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.devankav.spotifyhue.bridgeConnection.BridgeConnector;
import com.devankav.spotifyhue.bridgeConnection.BridgeResult;
import com.devankav.spotifyhue.bridgeConnection.DiscoveryResult;
import com.devankav.spotifyhue.listeners.DiscoveryListener;

import java.util.ArrayList;

public class DiscoveryActivity extends Activity {

    private boolean connecting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_discovery);

        // Button for testing
        Button switchButton = findViewById(R.id.switchButton);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiscoveryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        BridgeConnector connector = new BridgeConnector(this); // Create a new connector instance
        DiscoveryResult discoveryResult = connector.getAllBridges(); // Get all the available bridges on the network and update the list with them

        ArrayList<BridgeResult> bridges = new ArrayList<>(); // Create a new list
        ArrayAdapter<BridgeResult> adapter = new ArrayAdapter<BridgeResult>(this, android.R.layout.simple_list_item_1, bridges); // Create a new array adapter to update the list view
        ListView bridgeList = findViewById(R.id.bridgeSelection);
        bridgeList.setAdapter(adapter); // Set the lists adapter to the one created

        // Create and register a new discovery listener
        discoveryResult.registerListener(new DiscoveryListener() {
            @Override
            public void finished(DiscoveryResult result) {
                if (result.hasResults()) {
                    bridges.addAll(result.getBridges()); // Add the bridges to the list
                    adapter.notifyDataSetChanged(); // Notify the adapter
                } else {
                    // TODO: Add UI for when there are no bridges discovered
                    Log.d("DiscoveryActivity", "No bridges found on the network");
                }
            }
        });

        bridgeList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!connecting) {
                    //connecting = true;
                    BridgeResult bridge = bridges.get(i);

                    /*
                    connector.connect(bridge.getIpAddress());
                     */
                    Intent intent = new Intent(DiscoveryActivity.this, ConnectActivity.class);
                    intent.putExtra("ipAddress", bridge.getIpAddress());
                    intent.putExtra("id", bridge.getId());
                    startActivity(intent);
                }
            }
        });
    }
}
