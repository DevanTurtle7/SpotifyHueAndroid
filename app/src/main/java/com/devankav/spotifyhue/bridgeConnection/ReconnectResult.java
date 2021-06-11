/**
 * A listenable object that returns a BridgeState.
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue.bridgeConnection;

import com.devankav.spotifyhue.listeners.BridgeStateListener;
import com.devankav.spotifyhue.listeners.DiscoveryListener;
import com.devankav.spotifyhue.listeners.Listenable;
import com.devankav.spotifyhue.listeners.ListenerFinishedException;
import com.devankav.spotifyhue.listeners.ListenerNotFinishedException;
import com.devankav.spotifyhue.observers.BridgeStateObserver;
import com.devankav.spotifyhue.observers.Observable;

public class ReconnectResult extends Listenable<BridgeStateListener> {

    private BridgeState bridgeState;

    /**
     * The constructor
     */
    public ReconnectResult() {
        bridgeState = BridgeState.NOT_CONNECTED;
    }

    /**
     * An accessor for the status of the bridge
     * @return The status of the bridge
     */
    public BridgeState getState() {
        if (this.isFinished()) {
            return bridgeState;
        } else {
            throw new ListenerNotFinishedException();
        }
    }

    /**
     * A mutator for the current status
     * @param bridgeState The new state of the bridge
     */
    public void setState(BridgeState bridgeState) {
        if (!this.isFinished()) {
            this.finish();
            this.bridgeState = bridgeState;
            notifyListeners(bridgeState); // Notify the listeners that the event has finished
        } else {
            throw new ListenerFinishedException();
        }
    }

    /**
     * Notifies all of the registered listeners that an update has occurred
     */
    private void notifyListeners(BridgeState bridgeState) {
        for (BridgeStateListener listener : listeners) { // Iterate over each listener
            listener.finished(bridgeState); // Notify the listener of the results
        }
    }
}
