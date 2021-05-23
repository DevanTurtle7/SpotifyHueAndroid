/**
 * A functional interface that acts as an observer of new discovery results
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue.bridgeConnection;

import com.devankav.spotifyhue.Observer;

public interface DiscoveryObserver extends Observer<BridgeResult> {
}
