package com.devankav.spotifyhue;

public class BridgeState {

    BridgeStatus bridgeStatus;

    public BridgeState() {
        bridgeStatus = BridgeStatus.NOT_CONNECTED;
    }

    public BridgeStatus getStatus() {
        return bridgeStatus;
    }

    public void updateStatus(BridgeStatus bridgeStatus) {
        this.bridgeStatus = bridgeStatus;
    }
}
