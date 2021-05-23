package com.devankav.spotifyhue.bridgeConnection;

public class BridgeResult {

    private String id;
    private String ipAddress;

    public BridgeResult(String id, String ipAddress) {
        this.id = id;
        this.ipAddress = ipAddress;
    }

    public String getId() {
        return this.id;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }
}
