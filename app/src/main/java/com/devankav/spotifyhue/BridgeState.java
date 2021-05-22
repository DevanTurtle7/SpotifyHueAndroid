/**
 * An object that displays the status of a given bridge
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue;

public class BridgeState {

    BridgeStatus bridgeStatus;

    /**
     * The constructor
     */
    public BridgeState() {
        bridgeStatus = BridgeStatus.NOT_CONNECTED;
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
    }
}
