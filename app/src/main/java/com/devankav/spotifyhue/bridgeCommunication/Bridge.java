package com.devankav.spotifyhue.bridgeCommunication;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devankav.spotifyhue.listeners.LightsListener;
import com.devankav.spotifyhue.requests.GlobalRequestQueue;
import com.devankav.spotifyhue.requests.JsonArrayBodyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Bridge {

    public static final String PREFIX = "http://";

    private final String ipAddress;
    private final String id;
    private String username;
    private LightGroup lights;

    private final GlobalRequestQueue queue;
    private final String lightsEndpoint;

    public Bridge(String ipAddress, String id, String username, Context context) {
        this.ipAddress = ipAddress;
        this.id = id;
        this.username = username;
        this.lights = new LightGroup();

        this.queue = new GlobalRequestQueue(context); // Create a new instance of the request queue
        this.lightsEndpoint = PREFIX + ipAddress + "/api/" + username + "/lights";

        discoverAllLights();
    }

    public String getLightsEndpoint() {
        return this.lightsEndpoint;
    }

    private void discoverAllLights() {
        Bridge bridge = this;

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Set<Light> discovered = new HashSet<>();
                Iterator<String> keys = response.keys();

                while(keys.hasNext()) {
                    String id = keys.next();

                    try {
                        JSONObject body = (JSONObject) response.getJSONObject(id);
                        String name = body.getString("name");
                        String type = body.getString("type");
                        Light.LightType lightType = Light.LightType.classifyType(type);

                        Light light = new Light(id, name, lightType, bridge);
                        discovered.add(light);
                    } catch (JSONException e) {
                        Log.d("Bridge", "Something went wrong...");
                    }
                }

                lights.updateLights(discovered);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, lightsEndpoint, null, listener, errorListener);
        queue.getRequestQueue().add(jsonRequest); // Make the JSON call
    }

    /**
     * Used to wait for the initial discovery of lights
     * @param listener The listener that is waiting
     */
    public void subscribeToLightDiscovery(LightsListener listener) {
        lights.registerListener(listener);

        if (lights.hasResults()) {
            listener.finished(lights);
        }
    }

    public Set<Light> getLights() {
        return lights.getLights();
    }

    public void updateLight(String id, JSONObject body) {
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Bridge", response.toString());
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Bridge", error.toString());
            }
        };

        String url = lightsEndpoint + "/" + id + "/state";
        JsonArrayBodyRequest jsonRequest = new JsonArrayBodyRequest(Request.Method.PUT, url, body, listener, errorListener);
        queue.getRequestQueue().add(jsonRequest); // Make the JSON call
    }

    public void addToQueue(JsonArrayBodyRequest jsonRequest) {
        queue.getRequestQueue().add(jsonRequest);
    }

    public void addToQueue(JsonObjectRequest jsonRequest) {
        queue.getRequestQueue().add(jsonRequest);
    }

    public void addToQueue(JsonArrayRequest jsonRequest) {
        queue.getRequestQueue().add(jsonRequest);
    }
}
