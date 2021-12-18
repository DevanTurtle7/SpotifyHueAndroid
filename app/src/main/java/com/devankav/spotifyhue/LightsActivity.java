package com.devankav.spotifyhue;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.devankav.spotifyhue.adapters.LightAdapter;
import com.devankav.spotifyhue.bridgeCommunication.Bridge;
import com.devankav.spotifyhue.bridgeCommunication.Light;
import com.devankav.spotifyhue.bridgeCommunication.LightGroup;
import com.devankav.spotifyhue.listeners.LightsListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class LightsActivity extends AppCompatActivity {

    private Bridge bridge;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lights);

        Bundle bundle = getIntent().getExtras();
        String ipAddress = bundle.getString("ipAddress");
        String id = bundle.getString("id");
        String username = bundle.getString("username");
        bridge = new Bridge(ipAddress, id, username, this);

        ArrayList<Light> lights = new ArrayList<>();
        LightAdapter adapter = new LightAdapter(lights, this);
        ListView lightsList = findViewById(R.id.lightsList);
        lightsList.setAdapter(adapter);

        bridge.subscribeToLightDiscovery(result -> {
            Log.d("LightsActivity", result.getLights().size() + "");
            lights.addAll(result.getLights());
            adapter.notifyDataSetChanged();
        });
    }
}