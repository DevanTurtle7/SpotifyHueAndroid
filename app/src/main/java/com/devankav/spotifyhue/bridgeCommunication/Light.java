package com.devankav.spotifyhue.bridgeCommunication;

import com.devankav.spotifyhue.listeners.DiscoveryListener;
import com.devankav.spotifyhue.listeners.LightsListener;
import com.devankav.spotifyhue.listeners.Listenable;

public class Light {

    public enum LightType {
        EXTENDED_COLOR_LIGHT,
        COLOR_TEMPERATURE_LIGHT,
        OTHER;
    }

    private String id;
    private String name;
    private LightType type;

    public Light(String id, String name, LightType type) {
        this.id = id;
        this.name = name;
        this.type = type;
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
}
