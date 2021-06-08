/**
 * A service that runs in the background, even when the app is closed. Listens to Spotify events
 * and updates Philips Hue lights accordingly.
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.devankav.spotifyhue.bridgeConnection.HueConnector;
import com.devankav.spotifyhue.credentials.SpotifyCredentials;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.PlayerState;

public class LightSync extends Service {

    private HueConnector connector;
    private SpotifyAppRemote appRemote;

    /**
     * A callback for when there was a player event
     * @param playerState
     */
    public void playerStateUpdated(PlayerState playerState) {
        Log.d("LightSync", playerState.track.name + " by " + playerState.track.artist.name);
        Log.d("LightSync", playerState.track.toString());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Setup the connection parameters for the spotify remote
        ConnectionParams connectionParams = new ConnectionParams
                .Builder(SpotifyCredentials.CLIENT_ID)
                .setRedirectUri(SpotifyCredentials.REDIRECT_URI)
                .showAuthView(true)
                .build();

        SpotifyAppRemote.connect(this, connectionParams, new Connector.ConnectionListener() {
            @Override
            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                HueConnector connector = new HueConnector(getApplicationContext()); // Create a new hue connector

                appRemote = spotifyAppRemote;
                appRemote.getPlayerApi().subscribeToPlayerState().setEventCallback(LightSync.this::playerStateUpdated);
                Log.d("LightSync", "Connected! Yay!");
            }

            @Override
            public void onFailure(Throwable throwable) {
                //TODO: Add UI to indicate spotify failure
                if (throwable.getMessage() != null) {
                    Log.e("LightSync", throwable.getMessage(), throwable);
                } else {
                    Log.e("LightSync", "An unknown error occurred while trying to connect to the spotify app remote.");
                }
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }
}