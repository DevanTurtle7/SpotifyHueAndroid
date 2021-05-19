/**
 * A request queue for making JSON requests
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhueandroid;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class GlobalRequestQueue {

    private static RequestQueue queue;

    /**
     * The constructor
     * @param context The application context
     */
    public GlobalRequestQueue(Context context) {
        // Only create 1 instance of the queue
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
    }

    /**
     * An accessor for the queue
     * @return The request queue
     */
    public RequestQueue getRequestQueue() {
        return queue;
    }

    /**
     * Adds a request to the queue
     * @param request The request being added
     * @param <T> The type of request
     */
    public <T> void addToRequestQueue(Request<T> request) {
        queue.add(request);
    }
}
