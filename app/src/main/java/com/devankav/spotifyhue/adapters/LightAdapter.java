package com.devankav.spotifyhue.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.devankav.spotifyhue.R;
import com.devankav.spotifyhue.bridgeCommunication.Bridge;
import com.devankav.spotifyhue.bridgeCommunication.Light;

import java.util.ArrayList;

public class LightAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Light> lights;

    public LightAdapter(ArrayList<Light> lights, Context context) {
        this.lights = lights;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<Light> getList() {
        return this.lights;
    }

    @Override
    public int getCount() {
        return lights.size();
    }

    @Override
    public Light getItem(int i) {
        return lights.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Light light = lights.get(position);
        View currentView = convertView;
        //light.setActive(false);

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
                light.setActive(b);
            }
        });

        return currentView;
    }
}
