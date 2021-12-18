package com.devankav.spotifyhue.bridgeCommunication;

public class BridgeNotFoundException extends RuntimeException {
    public BridgeNotFoundException() {
        super("Bridge could not be found");
    }

    public BridgeNotFoundException(String message) {
        super(message);
    }
}
