/**
 * An object that displays the status of a given bridge
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue;

import java.util.HashSet;
import java.util.Set;

public class BridgeState {

    BridgeStatus bridgeStatus;
    Set<BridgeStateObserver> observers;

    /**
     * The constructor
     */
    public BridgeState() {
        bridgeStatus = BridgeStatus.NOT_CONNECTED;
        observers = new HashSet<>();
    }

    /**
     * An accessor for the current status
     * @return
     */
    public BridgeStatus getStatus() {
        return bridgeStatus;
    }

    /**
     * A mutator for the current status
     * @param bridgeStatus The new status
     */
    public void updateStatus(BridgeStatus bridgeStatus) {
        this.bridgeStatus = bridgeStatus;
        notifyObservers(bridgeStatus); // Notify the observers of an update
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
     * @param bridgeStatus
     */
    public void notifyObservers(BridgeStatus bridgeStatus) {
        for (BridgeStateObserver observer : observers) { // Iterate over each observer
            observer.BridgeStateUpdated(bridgeStatus); // Notify the observer
        }
    }
}
