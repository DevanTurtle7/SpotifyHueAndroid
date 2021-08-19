package com.devankav.spotifyhue.bridgeCommunication;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.palette.graphics.Palette;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devankav.spotifyhue.listeners.LightsListener;
import com.devankav.spotifyhue.requests.GlobalRequestQueue;
import com.devankav.spotifyhue.requests.JsonArrayBodyRequest;
import com.devankav.spotifyhue.spotifyHelpers.AlbumArtPalette;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class LightUpdater {

    public static final String PREFIX = "http://";

    private final String ipAddress;
    private final String id;
    private String username;

    private final GlobalRequestQueue queue;
    private final String lightsEndpoint;

    public LightUpdater(String ipAddress, String id, String username, Context context) {
        this.ipAddress = ipAddress;
        this.id = id;
        this.username = username;

        this.queue = new GlobalRequestQueue(context); // Create a new instance of the request queue
        this.lightsEndpoint = PREFIX + ipAddress + "/api/" + username + "/lights";
    }

    public LightGroup getLights() {
        LightGroup results = new LightGroup();

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<Light> lights = new ArrayList<>();
                Iterator<String> keys = response.keys();

                while(keys.hasNext()) {
                    String id = keys.next();

                    try {
                        JSONObject body = (JSONObject) response.getJSONObject(id);
                        String name = body.getString("name");
                        String type = body.getString("type");
                        Light.LightType lightType = Light.LightType.classifyType(type);

                        Light light = new Light(id, name, lightType);
                        lights.add(light);
                    } catch (JSONException e) {
                        Log.d("LightUpdater", "Something went wrong...");
                    }
                }

                results.updateLights(lights);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, lightsEndpoint, null, listener, errorListener);
        queue.getRequestQueue().add(jsonRequest); // Make the JSON call

        return results;
    }

    public void updateLight(String id, JSONObject body) {
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("LightUpdater", response.toString());
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("LightUpdater", error.toString());
            }
        };

        String url = lightsEndpoint + "/" + id + "/state";
        JsonArrayBodyRequest jsonRequest = new JsonArrayBodyRequest(Request.Method.PUT, url, body, listener, errorListener);
        queue.getRequestQueue().add(jsonRequest); // Make the JSON call
    }

    public void updateLightColor(String id, double[] xyColor) {
        try {
            String bodyString = "{\"xy\": " + Arrays.toString(xyColor) + "}";
            JSONObject body = new JSONObject(bodyString);

            updateLight(id, body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateLightColor(String id, int color) {
        double[] xyColor = AlbumArtPalette.rgbToXY(color);
        updateLightColor(id, xyColor);
    }

    public void updateLightOn(String id, boolean on) {
        try {
            String bodyString = "{\"on\": " + on + "}";
            JSONObject body = new JSONObject(bodyString);

            updateLight(id, body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateLightBrightness(String id, int brightness) {
        try {
            String bodyString = "{\"bri\": " + brightness + "}";
            JSONObject body = new JSONObject(bodyString);

            updateLight(id, body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
