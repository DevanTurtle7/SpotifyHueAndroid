/**
 * An object that displays the status of a given bridge
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue;

import java.util.HashSet;
import java.util.Set;

public class BridgeStatus {

    BridgeState bridgeState;
    Set<BridgeStateObserver> observers;

    /**
     * The constructor
     */
    public BridgeStatus() {
        bridgeState = BridgeState.NOT_CONNECTED;
        observers = new HashSet<>();
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
     * Registers an observer to be notified whenever there is an update
     * @param observer The observer being registered
     */
    public void registerObserver(BridgeStateObserver observer) {
        this.observers.add(observer);
    }

    /**
     * Deregisters an observer
     * @param observer The observer being deregistered
     */
    public void deregisterObserver(BridgeStateObserver observer) {
        this.observers.remove(observer);
    }

    /**
     * Notifies all of the registered observers that an update has occurred
     * @param bridgeState
     */
    public void notifyObservers(BridgeState bridgeState) {
        for (BridgeStateObserver observer : observers) { // Iterate over each observer
            observer.BridgeStateUpdated(bridgeState); // Notify the observer
        }
    }
}
