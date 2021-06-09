package com.devankav.spotifyhue.bridgeConnection;

import com.devankav.spotifyhue.observers.Observable;

public class Bridge {

    private String ipAddress;
    private String id;
    private String username;
    private String url;

    /**
     * The constructor
     * @param ipAddress The IP Address of the bridge
     * @param id The id of the bridge
     * @param username The username used to connect to the bridge
     */
    public Bridge(String ipAddress, String id, String username) {
        this.ipAddress = ipAddress;
        this.id = id;
        this.username = username;

        url = "http://" + ipAddress + "/api/" + username;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getId() {
        return id;
    }
}
