/**
 * A functional interface that acts as an observer of new discovery results
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue.observers;

import com.devankav.spotifyhue.bridgeConnection.BridgeResult;
import com.devankav.spotifyhue.observers.Observer;

public interface DiscoveryObserver extends Observer<BridgeResult> {
}
