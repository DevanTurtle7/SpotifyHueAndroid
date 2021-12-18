/**
 * A functional interface that acts as an observer of bridge state changes
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue.observers;

import com.devankav.spotifyhue.bridgeConnection.BridgeState;

public interface BridgeStateObserver extends Observer<BridgeState> {
}
