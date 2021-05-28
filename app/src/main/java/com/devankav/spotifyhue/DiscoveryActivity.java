/**
 * The page that allows the user to connect to Philips hue bridges.
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.devankav.spotifyhue.bridgeConnection.BridgeResult;
import com.devankav.spotifyhue.bridgeConnection.DiscoveryObserver;
import com.devankav.spotifyhue.bridgeConnection.DiscoveryResult;
import com.devankav.spotifyhue.bridgeConnection.HueConnector;

import java.util.ArrayList;

public class DiscoveryActivity extends Activity {

    private boolean connecting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connect);

        // Button for testing
        Button switchButton = findViewById(R.id.switchButton);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiscoveryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        HueConnector connector = new HueConnector(this); // Create a new connector instance
        DiscoveryResult discoveryResult = connector.getAllBridges(); // Get all the available bridges on the network and update the list with them

        ArrayList<BridgeResult> bridges = new ArrayList<>(); // Create a new list
        ArrayAdapter<BridgeResult> adapter = new ArrayAdapter<BridgeResult>(this, android.R.layout.simple_list_item_1, bridges); // Create a new array adapter to update the list view
        ListView bridgeList = findViewById(R.id.bridgeSelection);
        bridgeList.setAdapter(adapter); // Set the lists adapter to the one created

        // Create and register a new discovery observer
        discoveryResult.registerObserver(new DiscoveryObserver() {
            @Override
            public void notifyObserver(BridgeResult updated) {
                bridges.add(updated); // Add the bridge to the list
                adapter.notifyDataSetChanged(); // Notify the adapter
            }
        });

        bridgeList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!connecting) {
                    //connecting = true;
                    BridgeResult bridge = bridges.get(i);
                    connector.connect(bridge.getIpAddress());
                }
            }
        });
    }
}
