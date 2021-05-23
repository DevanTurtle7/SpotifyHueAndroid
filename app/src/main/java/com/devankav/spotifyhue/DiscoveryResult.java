package com.devankav.spotifyhue;

import java.util.HashSet;
import java.util.Set;

public class DiscoveryResult extends Observable<DiscoveryObserver> {
    Set<BridgeResult> bridges;

    public DiscoveryResult() {
        this.bridges = new HashSet<>();
    }

    public void addBridge(BridgeResult bridgeResult) {
        bridges.add(bridgeResult);
        notifyObservers(bridgeResult);
    }

    public void notifyObservers(BridgeResult bridgeResult) {
        for (DiscoveryObserver observer : observers) {
            observer.notifyObserver(bridgeResult);
        }
    }
}
