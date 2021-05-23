/**
 * An enum to represent the state of a bridge
 *
 * @author Devan Kavalchek
 */
package com.devankav.spotifyhue;

public enum BridgeState {
    LINK_BUTTON_NOT_PRESSED, // The link button needs to be pushed to complete the connection
    CONNECTED, // The bridge is connected
    NOT_CONNECTED, // The bridge is not connected and an attempt has not been made
    FAILED_TO_CONNECT; // A connection attempt failed. The bridge is not connected.
}
