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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConnectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connect);

        // Button for testing
        Button switchButton = findViewById(R.id.switchButton);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConnectActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        HueConnector connector = new HueConnector(this); // Create a new connector instance
        ArrayList<String> bridges = new ArrayList<>(); // Create a new list
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bridges); // Create a new array adapter to update the list view
        connector.getAllBridges(adapter, bridges); // Get all the available bridges on the network and update the list with them

        ListView bridgeList = findViewById(R.id.bridgeSelection);
        bridgeList.setAdapter(adapter); // Set the lists adapter to the one created
    }
}
