package com.devankav.spotifyhue.bridgeCommunication;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devankav.spotifyhue.listeners.LightsListener;
import com.devankav.spotifyhue.requests.GlobalRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LightUpdater {

    public static final String PREFIX = "http://";

    private final String ipAddress;
    private final String id;
    private String username;

    private final GlobalRequestQueue queue;

    public LightUpdater(String ipAddress, String id, String username, Context context) {
        this.ipAddress = ipAddress;
        this.id = id;
        this.username = username;

        this.queue = new GlobalRequestQueue(context); // Create a new instance of the request queue
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

        String url = PREFIX + ipAddress + "/api/" + username + "/lights";
        Log.d("LightUpdater", url);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener);
        queue.getRequestQueue().add(jsonRequest); // Make the JSON call

        return results;
    }

    public void updateLights() {
    }
}
