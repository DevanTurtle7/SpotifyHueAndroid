/**
 * The result of a connection attempt. An observable object that returns a BridgeState.
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue.bridgeConnection;

import com.devankav.spotifyhue.observers.BridgeStateObserver;
import com.devankav.spotifyhue.observers.Observable;

public class ConnectResult extends Observable<BridgeStateObserver> {

    private BridgeState bridgeState;
    private String username;

    /**
     * The constructor
     */
    public ConnectResult() {
        bridgeState = BridgeState.NOT_CONNECTED;
        username = null;
    }

    /**
     * An accessor for the current status
     * @return The current status of the bridge
     */
    public BridgeState getState() {
        return bridgeState;
    }

    /**
     * A mutator for the current status
     * @param bridgeState The new state of the bridge
     */
    public void updateState(BridgeState bridgeState) {
        this.bridgeState = bridgeState;
        notifyObservers(bridgeState); // Notify the observers of an update
    }

    /**
     * Notifies all of the registered observers that an update has occurred
     * @param bridgeState The new state of the bridge
     */
    public void notifyObservers(BridgeState bridgeState) {
        for (BridgeStateObserver observer : observers) { // Iterate over each observer
            observer.notifyObserver(bridgeState); // Notify the observer
        }
    }

    /**
     * Updates the username used to access the bridge
     * @param username The new username
     */
    public void updateUsername(String username) {
        this.username = username;
    }

    /**
     * An accessor for the bridge's username
     * @return The username used to access the bridge
     */
    public String getUsername() {
        return username;
    }
}
