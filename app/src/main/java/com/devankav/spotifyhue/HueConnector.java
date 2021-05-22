package com.devankav.spotifyhue;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HueConnector {

    public static final String PREFIX = "http://";
    public static final String SUFFIX = "/api";

    private GlobalRequestQueue queue;

    public HueConnector(Context context) {
        this.queue = new GlobalRequestQueue(context);
    }

    public void reconnect(String ip, String username) {

    }

    public void connect(String ip) {
        final BridgeStatus result;

        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("HueConnector", response.toString());
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HueConnector", error.getMessage());
            }
        };

        String url = PREFIX + ip + SUFFIX;
        Log.d("HueConnector", url);
        JSONObject body = new JSONObject();

        try {
            body.put("devicetype", "spotify_hue#android");
            JsonArrayBodyRequest jsonRequest = new JsonArrayBodyRequest(Request.Method.POST, url, body, listener, errorListener);
            queue.getRequestQueue().add(jsonRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
