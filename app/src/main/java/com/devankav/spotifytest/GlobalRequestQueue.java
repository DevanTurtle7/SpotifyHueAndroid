package com.devankav.spotifytest;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class GlobalRequestQueue {

    private static RequestQueue queue;

    public GlobalRequestQueue(Context context) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
    }

    public RequestQueue getRequestQueue() {
        return queue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        queue.add(request);
    }
}
