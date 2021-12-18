package com.devankav.spotifyhue;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.devankav.spotifyhue.adapters.LightAdapter;
import com.devankav.spotifyhue.bridgeCommunication.Bridge;
import com.devankav.spotifyhue.bridgeCommunication.Light;

import java.util.ArrayList;

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
            for (Light light : bridge.getLights()) {
                if (light.getType() == Light.LightType.EXTENDED_COLOR_LIGHT) {
                    lights.add(light);
                }
            }

            adapter.notifyDataSetChanged();
        });
    }
}