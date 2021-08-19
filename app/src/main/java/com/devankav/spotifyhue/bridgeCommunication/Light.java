package com.devankav.spotifyhue.bridgeCommunication;

public class Light {

    public static enum LightType {
        EXTENDED_COLOR_LIGHT,
        COLOR_TEMPERATURE_LIGHT,
        OTHER;

        public static LightType classifyType(String typeString) {
            if (typeString.equals("Extended color light")) {
                return EXTENDED_COLOR_LIGHT;
            } else if (typeString.equals("Color temperature light")) {
                return COLOR_TEMPERATURE_LIGHT;
            } else {
                return OTHER;
            }
        }
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

    @Override
    public String toString() {
        return "Light " + id + ": name: " + name + ", type: " + type;
    }
}
