/**
 * A helper class that communicates with the Philips hue bridge and API.
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HueConnector {

    public static final String PREFIX = "http://";
    public static final String SUFFIX = "/api";
    public static final String DISCOVERY_URL = "https://discovery.meethue.com";

    private GlobalRequestQueue queue;
    private SharedPreferences sharedPreferences;

    /**
     * The constructor
     * @param context The application context. Used to make JSON requests and access shared preferences
     */
    public HueConnector(Context context) {
        this.queue = new GlobalRequestQueue(context); // Create a new instance of the request queue
        this.sharedPreferences = context.getSharedPreferences("bridgeMem", Context.MODE_PRIVATE); // Get the shared preferences instance
    }

    /**
     * Attempts to reconnect to a bridge that has previously been connected to
     * @param ip The IP address of the bridge
     * @param username The username used for the bridge
     */
    public void reconnect(String ip, String username) {

    }

    /**
     * Attempts to connect to a bridge
     * @param ip The IP address of the bridge
     * @return The status of the bridge (whether or not it connected)
     */
    public BridgeStatus[] connect(String ip) {
        final BridgeStatus[] result = {null}; // Initialize the bridge state result

        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject body = response.getJSONObject(0);

                    if (body.get("error") != null) {
                        result[0] = BridgeStatus.LINK_BUTTON_NOT_PRESSED;
                    } else {
                        result[0] = BridgeStatus.CONNECTED;

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        // TODO: Write bridge info to shared prefs
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    result[0] = BridgeStatus.FAILED_TO_CONNECT;
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                result[0] = BridgeStatus.FAILED_TO_CONNECT;
                if (error.getMessage() != null) {
                    Log.e("HueConnector", error.getMessage());
                } else {
                    Log.e("HueConnector", "There was an unknown error getting all bridges.");
                }
            }
        };

        try {
            String url = PREFIX + ip + SUFFIX;
            JSONObject body = new JSONObject();
            body.put("devicetype", "spotify_hue#android");
            JsonArrayBodyRequest jsonRequest = new JsonArrayBodyRequest(Request.Method.POST, url, body, listener, errorListener);
            queue.getRequestQueue().add(jsonRequest);
        } catch (JSONException e) {
            result[0] = BridgeStatus.FAILED_TO_CONNECT;
            e.printStackTrace();
        }

        return result;
    }

    public Map<String, String> getAllBridges(@Nullable ArrayAdapter adapter, @Nullable ArrayList<String> arrayList) {
        Map<String, String> bridges = new HashMap<String, String>();

        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject current = response.getJSONObject(i);
                        String id = current.getString("id");
                        String ipAddress = current.getString("internalipaddress");

                        bridges.put(id, ipAddress);

                        if (adapter != null && arrayList != null) {
                            arrayList.add(ipAddress);
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null) {
                    Log.e("HueConnector", error.getMessage());
                } else {
                    Log.e("HueConnector", "There was an unknown error getting all bridges.");
                }
            }
        };

        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, DISCOVERY_URL, null, listener, errorListener);
        queue.getRequestQueue().add(jsonRequest);

        return bridges;
    }
}
