/**
 * A set of bridges that are found while searching the network for bridges to
 * connect to
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue.bridgeConnection;

import com.devankav.spotifyhue.observers.DiscoveryObserver;
import com.devankav.spotifyhue.observers.Observable;

import java.util.HashSet;
import java.util.Set;

public class DiscoveryResult extends Observable<DiscoveryObserver> {
    Set<BridgeResult> bridges;

    /**
     * The constructor
     */
    public DiscoveryResult() {
        this.bridges = new HashSet<>(); // Create a new set to store the bridges
    }

    /**
     * Adds the bridge to the set of results
     * @param bridgeResult The bridge being added
     */
    public void addBridge(BridgeResult bridgeResult) {
        bridges.add(bridgeResult); // Add the new bridge
        notifyObservers(bridgeResult); // Notify any observers that a new bridge was added
    }

    /**
     * Notifies all of the registered observers that an update has occurred
     * @param bridgeResult The new bridge that was discovered
     */
    public void notifyObservers(BridgeResult bridgeResult) {
        for (DiscoveryObserver observer : observers) { // Iterate over each observer
            observer.notifyObserver(bridgeResult); // Notify the observer of the new bridge that was discovered
        }
    }
}
