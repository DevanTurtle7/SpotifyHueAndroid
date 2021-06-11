/**
 * A set of bridges that are found while searching the network for bridges to
 * connect to
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue.bridgeConnection;

import com.devankav.spotifyhue.listeners.DiscoveryListener;
import com.devankav.spotifyhue.listeners.Listenable;
import com.devankav.spotifyhue.listeners.Listener;
import com.devankav.spotifyhue.listeners.ListenerFinishedException;
import com.devankav.spotifyhue.observers.DiscoveryObserver;
import com.devankav.spotifyhue.observers.Observable;

import java.util.HashSet;
import java.util.Set;

public class DiscoveryResult extends Listenable<DiscoveryListener> {
    Set<BridgeResult> bridges;

    /**
     * The constructor
     */
    public DiscoveryResult() {
        this.bridges = new HashSet<>(); // Create a new set to store the bridges
    }

    /**
     * Updates the set of bridges with the results of the search. Can only be executed once.
     * @param bridgeResults The bridges discovered
     */
    public void updatedBridges(Set<BridgeResult> bridgeResults) {
        if (!this.isFinished()) {
            this.finish();
            this.bridges.addAll(bridgeResults); // Add all the results
            notifyListeners(); // Notify the listeners
        } else {
            throw new ListenerFinishedException("Attempted to update listenable object after it finished.");
        }
    }

    /**
     * An accessor for bridges
     * @return A set of all bridge results
     */
    public Set<BridgeResult> getBridges() {
        return this.bridges;
    }

    /**
     * Notifies all of the registered listeners that an update has occurred
     */
    public void notifyListeners() {
        for (DiscoveryListener listener : listeners) { // Iterate over each listener
            listener.finished(this); // Notify the listener of the results
        }
    }
}
