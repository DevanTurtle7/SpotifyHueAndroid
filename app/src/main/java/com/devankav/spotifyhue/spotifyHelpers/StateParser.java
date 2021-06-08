/**
 * Parses info from a player state. Functions can be called statically or
 * non-statically.
 *
 * @author Devan Kavalchek
 */
package com.devankav.spotifyhue.spotifyHelpers;

import com.spotify.protocol.types.ImageUri;
import com.spotify.protocol.types.PlayerState;

public class StateParser {

    private static final String IMAGE_PREFIX = "https://i.scdn.co/image/";

    private PlayerState playerState;

    public StateParser(PlayerState playerState) {
        this.playerState = playerState;
    }

    /**
     * Returns the URL of the album art of the current playing song
     *
     * @param playerState The player state, given by the player api
     * @return A URL to the album art of the current song on spotify
     */
    public static String getImageURL(PlayerState playerState) {
        ImageUri imageUri = playerState.track.imageUri; // Get the image uri from the player state
        String[] tokens = imageUri.toString().split(":"); // Split the uri on colons
        String endingCode = tokens[tokens.length - 1]; // Get the last token
        tokens = endingCode.split("'"); // Strip the end of the uri off
        String imageCode = tokens[0];
        String url = IMAGE_PREFIX + imageCode; // Assemble the url

        return url;
    }

    /**
     * Returns the URL of the album art of the current playing song
     *
     * @return A URL to the album art of the current song on spotify
     */
    public String getImageURL() {
        return getImageURL(this.playerState);
    }
}
