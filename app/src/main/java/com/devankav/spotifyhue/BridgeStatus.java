/**
 * An object that displays the status of a given bridge
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue;

import java.util.HashSet;
import java.util.Set;

public class BridgeStatus extends Observable<BridgeStateObserver> {
    private BridgeState bridgeState;

    /**
     * The constructor
     */
    public BridgeStatus() {
        bridgeState = BridgeState.NOT_CONNECTED;
    }

    /**
     * An accessor for the current status
     * @return
     */
    public BridgeState getState() {
        return bridgeState;
    }

    /**
     * A mutator for the current status
     * @param bridgeState The new status
     */
    public void updateState(BridgeState bridgeState) {
        this.bridgeState = bridgeState;
        notifyObservers(bridgeState); // Notify the observers of an update
    }

    /**
     * Notifies all of the registered observers that an update has occurred
     * @param bridgeState
     */
    public void notifyObservers(BridgeState bridgeState) {
        for (BridgeStateObserver observer : observers) { // Iterate over each observer
            observer.notifyObserver(bridgeState); // Notify the observer
        }
    }
}
