/**
 * A bridge that is found while searching the network for bridges
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue.bridgeConnection;

public class BridgeResult {

    private String id;
    private String ipAddress;

    /**
     * The constructor
     * @param id The id of the bridge
     * @param ipAddress The IP address of the bridge
     */
    public BridgeResult(String id, String ipAddress) {
        this.id = id;
        this.ipAddress = ipAddress;
    }

    /**
     * An accessor for the bridge's id
     * @return The bridge's id
     */
    public String getId() {
        return this.id;
    }

    /**
     * An accessor for the bridge's IP address
     * @return The bridge's IP address
     */
    public String getIpAddress() {
        return this.ipAddress;
    }
}
