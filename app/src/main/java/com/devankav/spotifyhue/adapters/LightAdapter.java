package com.devankav.spotifyhue.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.devankav.spotifyhue.R;
import com.devankav.spotifyhue.bridgeCommunication.Light;

import java.util.ArrayList;

public class LightAdapter extends ArrayAdapter<Light> {
    private LayoutInflater inflater;

    public LightAdapter(ArrayList<Light> lights, Context context) {
        super(context, R.layout.list_item_lights, lights);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Light light = getItem(position);
        View currentView = convertView;

        if (currentView == null) {
            currentView = inflater.inflate(R.layout.list_item_lights, parent, false);
        }

        TextView name = currentView.findViewById(R.id.lightName);

        name.setText(light.getName());

        return currentView;
    }
}
