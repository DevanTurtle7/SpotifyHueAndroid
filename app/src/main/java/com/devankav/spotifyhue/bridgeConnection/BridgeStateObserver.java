/**
 * A functional interface that acts as an observer of bridge state changes
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue.bridgeConnection;

import com.devankav.spotifyhue.Observer;

public interface BridgeStateObserver extends Observer<BridgeState> {
}
