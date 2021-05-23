/**
 * A functional interface that acts as an observer of bridge state changes
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue;

public interface BridgeStateObserver {
    public void BridgeStateUpdated(BridgeState bridgeStatus);
}
