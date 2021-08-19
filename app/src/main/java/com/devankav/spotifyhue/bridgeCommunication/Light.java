package com.devankav.spotifyhue.bridgeCommunication;

import com.devankav.spotifyhue.listeners.DiscoveryListener;
import com.devankav.spotifyhue.listeners.LightsListener;
import com.devankav.spotifyhue.listeners.Listenable;

public class Light {

    private String id;

    public Light(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
