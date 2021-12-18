/**
 * A helper class that communicates with the Philips hue bridge and API in the connection
 * process.
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue.bridgeConnection;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devankav.spotifyhue.requests.GlobalRequestQueue;
import com.devankav.spotifyhue.requests.JsonArrayBodyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class BridgeConnector {

    public static final String PREFIX = "http://";
    public static final String SUFFIX = "/api";
    public static final String DISCOVERY_URL = "https://discovery.meethue.com";

    private final GlobalRequestQueue queue;

    /**
     * The constructor
     *
     * @param context The application context. Used to make JSON requests and access shared preferences
     */
    public BridgeConnector(Context context) {
        this.queue = new GlobalRequestQueue(context); // Create a new instance of the request queue
    }

    /**
     * Attempts to reconnect to a bridge that has previously been connected to
     *
     * @param ip       The IP address of the bridge
     * @param username The username used for the bridge
     * @return The status of the bridge (whether or not it connected)
     */
    public ReconnectResult reconnect(String ip, String username) {
        final ReconnectResult result = new ReconnectResult();

        // Create a new response listener
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.has("config")) { // Check if the bridge was successfully connected to
                    result.setState(BridgeState.CONNECTED);

                    Log.d("BridgeConnector", "Successfully reconnected");
                } else {
                    result.setState(BridgeState.FAILED_TO_CONNECT);

                    if (response.has("error")) {
                        // There was an error connecting to the bridge
                        Log.d("BridgeConnector", "Failed to reconnect: " + response);
                    } else {
                        // There was an unknown response
                        Log.d("BridgeConnector", "An unknown error occurred while trying to reconnect: " + response);
                    }
                }
            }
        };

        // Create a new error listener
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                result.setState(BridgeState.FAILED_TO_CONNECT);

                // Print the error message
                if (error.getMessage() != null) {
                    Log.e("BridgeConnector", error.getMessage());
                } else {
                    Log.e("BridgeConnector", "There was an unknown error while reconnecting.");
                }
            }
        };

        String url = PREFIX + ip + SUFFIX + "/" + username; // Build the URL to reconnect to the bridge
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener);
        queue.getRequestQueue().add(jsonRequest); // Make the JSON call

        return result;
    }

    /**
     * Attempts to connect to a bridge
     *
     * @param ip The IP address of the bridge
     * @return The status of the bridge (whether or not it connected)
     */
    public ConnectResult connect(String ip, @Nullable ConnectResult bridgeStatus) {
        final ConnectResult result = bridgeStatus == null ? new ConnectResult() : bridgeStatus; // Initialize the bridge state result

        // Create a new response listener
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject body = response.getJSONObject(0); // Get the object that was returned

                    Log.d("BridgeConnector", body.toString());

                    if (body.has("error")) { // Check if there was an error
                        // TODO: Add if statements for errors
                        result.updateState(BridgeState.LINK_BUTTON_NOT_PRESSED);
                    } else if (body.has("success")) {
                        String username = body.getJSONObject("success").getString("username");

                        result.updateUsername(username); // Update the username
                        result.updateState(BridgeState.CONNECTED);
                    } else {
                        Log.e("BridgeConnector", "There was an unexpected response while trying to connect: " + body.toString());
                    }
                } catch (JSONException e) {
                    result.updateState(BridgeState.FAILED_TO_CONNECT);

                    if (e.getMessage() != null) {
                        Log.e("BridgeConnector", e.getMessage());
                    } else {
                        Log.e("BridgeConnector", "There was an unknown error while trying to connect.");
                    }
                }
            }
        };

        // Create a new error listener
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                result.updateState(BridgeState.FAILED_TO_CONNECT);

                // Print the error message
                if (error.getMessage() != null) {
                    Log.e("BridgeConnector", error.getMessage());
                } else {
                    Log.e("BridgeConnector", "There was an unknown error while connecting.");
                }
            }
        };

        try {
            // Create a new json request
            String url = PREFIX + ip + SUFFIX;

            // Create the body of the JSON call
            JSONObject body = new JSONObject();
            String device = Build.MODEL;
            body.put("devicetype", "spotify_hue#" + device);

            JsonArrayBodyRequest jsonRequest = new JsonArrayBodyRequest(Request.Method.POST, url, body, listener, errorListener);

            queue.getRequestQueue().add(jsonRequest); // Make the JSON call
        } catch (JSONException e) {
            result.updateState(BridgeState.FAILED_TO_CONNECT);

            if (e.getMessage() != null) {
                Log.e("BridgeConnector", e.getMessage());
            } else {
                Log.e("BridgeConnector", "There was an unknown error");
            }
        }

        return result;
    }

    public ConnectResult connect(String ip) {
        return connect(ip, null);
    }

    /**
     * Gets a list of all available bridges on the network
     *
     * @return A bridge result containing all of the bridges on the network
     */
    public DiscoveryResult getAllBridges() {
        final DiscoveryResult discoveryResult = new DiscoveryResult();

        // Create a new listener
        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Set<BridgeResult> results = new HashSet<>();

                for (int i = 0; i < response.length(); i++) { // Iterate over all the JSON objects in the response
                    try {
                        JSONObject current = response.getJSONObject(i); // Get the JSON object at the current index
                        String id = current.getString("id"); // Get the id of the bridge
                        String ipAddress = current.getString("internalipaddress"); // Get the ip address of the bridge

                        BridgeResult bridgeResult = new BridgeResult(id, ipAddress); // Create a new bridge result
                        results.add(bridgeResult); // Add the bridge to the set
                    } catch (JSONException e) {
                        if (e.getMessage() != null) {
                            Log.e("BridgeConnector", e.getMessage());
                        } else {
                            Log.e("BridgeConnector", "There was an unexpected error while getting all bridges");
                        }
                    }
                }

                discoveryResult.updatedBridges(results);
            }
        };

        // Create a new error listener
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null) {
                    Log.e("BridgeConnector", error.getMessage());
                } else {
                    Log.e("BridgeConnector", "There was an unknown error getting all bridges.");
                }
            }
        };

        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, DISCOVERY_URL, null, listener, errorListener);
        queue.getRequestQueue().add(jsonRequest); // Make the JSON call

        return discoveryResult;
    }
}