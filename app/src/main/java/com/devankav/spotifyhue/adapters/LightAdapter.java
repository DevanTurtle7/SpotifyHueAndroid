package com.devankav.spotifyhue.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.devankav.spotifyhue.R;
import com.devankav.spotifyhue.bridgeCommunication.Bridge;
import com.devankav.spotifyhue.bridgeCommunication.Light;

import java.util.ArrayList;

public class LightAdapter extends ArrayAdapter<Light> {
    private LayoutInflater inflater;

    public LightAdapter(ArrayList<Light> lights, Context context) {
        super(context, R.layout.list_item_lights, lights);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Light light = getItem(position);
        View currentView = convertView;
        light.setActive(false);
        Log.d("LightAdapter", light.isActive() + "");

        if (currentView == null) {
            currentView = inflater.inflate(R.layout.list_item_lights, parent, false);
        }

        TextView name = currentView.findViewById(R.id.lightName);
        Switch activeSwitch = currentView.findViewById(R.id.lightActiveSwitch);

        name.setText(light.getName());
        activeSwitch.setChecked(light.isActive());

        activeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("LightAdapter", "Setting light to " + b);
                //light.setActive(b);
            }
        });

        return currentView;
    }
}
