package com.devankav.spotifyhue.bridgeCommunication;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devankav.spotifyhue.observers.LightActiveObserver;
import com.devankav.spotifyhue.requests.JsonArrayBodyRequest;
import com.devankav.spotifyhue.spotifyHelpers.AlbumArtPalette;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Observer;

public class Light {

    public static enum LightType {
        EXTENDED_COLOR_LIGHT,
        COLOR_TEMPERATURE_LIGHT,
        OTHER;

        public static LightType classifyType(String typeString) {
            if (typeString.equals("Extended color light")) {
                return EXTENDED_COLOR_LIGHT;
            } else if (typeString.equals("Color temperature light")) {
                return COLOR_TEMPERATURE_LIGHT;
            } else {
                return OTHER;
            }
        }
    }

    private String id;
    private String name;
    private LightType type;
    private boolean updatingBrightness;
    private Bridge bridge;
    private boolean isActive;

    private HashSet<LightActiveObserver> observers;

    public Light(String id, String name, LightType type, Bridge bridge, boolean isActive) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.updatingBrightness = false;
        this.bridge = bridge;
        this.isActive = isActive;

        this.observers = new HashSet<>();
    }

    public Light(String id, String name, LightType type, Bridge bridge) {
        this(id, name, type, bridge, true);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LightType getType() {
        return type;
    }

    public boolean isActive() {
        return isActive;
    }

    public Bridge getBridge() {
        return this.bridge;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
        this.activeChanged();
    }

    public void updateLightColor(double[] xyColor) {
        if (isActive) {
            if (type == LightType.EXTENDED_COLOR_LIGHT) {
                try {
                    String bodyString = "{\"xy\": " + Arrays.toString(xyColor) + "}";
                    JSONObject body = new JSONObject(bodyString);

                    bridge.updateLight(id, body);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.d("Light", "Cannot change color, inactive");
        }
    }

    public void updateLightColor(int color) {
        if (isActive) {
            double[] xyColor = AlbumArtPalette.rgbToXY(color);
            updateLightColor(xyColor);
        }
    }

    public void updateLightOn(String id, boolean on) {
        if (isActive) {
            try {
                String bodyString = "{\"on\": " + on + "}";
                JSONObject body = new JSONObject(bodyString);

                bridge.updateLight(id, body);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateLightBrightness(int brightness) {
        if (isActive) {
            updateLightBrightness(brightness, false);
        }
    }

    public void updateLightBrightness(int brightness, boolean force) {
        if (isActive) {
            if (!updatingBrightness || force) {
                try {
                    updatingBrightness = true;
                    String bodyString = "{\"bri\": " + brightness + "}";
                    JSONObject body = new JSONObject(bodyString);

                    Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("Bridge", response.toString());
                            updatingBrightness = false; // The job is finished. Mark updating as false
                        }
                    };

                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Bridge", error.toString());
                            updatingBrightness = false; // The job is finished. Mark updating as false
                        }
                    };

                    String lightsEndpoint = bridge.getLightsEndpoint();
                    String url = lightsEndpoint + "/" + id + "/state";
                    JsonArrayBodyRequest jsonRequest = new JsonArrayBodyRequest(Request.Method.PUT, url, body, listener, errorListener);
                    bridge.addToQueue(jsonRequest); // Make the JSON call
                } catch (JSONException e) {
                    e.printStackTrace();
                    updatingBrightness = false; // The job is finished. Mark updating as false
                }
            }
        }
    }

    public void registerObserver(LightActiveObserver observer) {
        this.observers.add(observer);
    }

    private void activeChanged() {
        for (LightActiveObserver observer : this.observers) {
            observer.notifyObserver(this.isActive);
        }
    }

    @Override
    public String toString() {
        return "Light " + id + ": name: " + name + ", type: " + type;
    }
}
