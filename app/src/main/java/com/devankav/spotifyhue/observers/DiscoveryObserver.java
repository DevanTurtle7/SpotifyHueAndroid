/**
 * A functional interface that acts as an observer of new discovery results
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue.observers;

import com.devankav.spotifyhue.bridgeConnection.BridgeResult;

public interface DiscoveryObserver extends Observer<BridgeResult> {
}
