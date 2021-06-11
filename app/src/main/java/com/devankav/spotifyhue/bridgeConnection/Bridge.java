/**
 * A model of the philips hue bridge that is connected. Makes calls to the bridge on
 * the network to update it.
 *
 * @author Devan Kavalcheks
 */

package com.devankav.spotifyhue.bridgeConnection;

import com.devankav.spotifyhue.observers.Observable;

public class Bridge {

    private String ipAddress;
    private String id;
    private String username;
    private final String url;

    /**
     * The constructor
     * @param ipAddress The IP Address of the bridge
     * @param id The id of the bridge
     * @param username The username used to connect to the bridge
     */
    public Bridge(String ipAddress, String id, String username) {
        this.ipAddress = ipAddress;
        this.id = id;
        this.username = username;

        url = "http://" + ipAddress + "/api/" + username; // Set the URL that is used to access the bridge
    }

    /**
     * An accessor for the ip address
     * @return The ip address
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * An accessor for the id of this bridge
     * @return The bridges id
     */
    public String getId() {
        return id;
    }
}
