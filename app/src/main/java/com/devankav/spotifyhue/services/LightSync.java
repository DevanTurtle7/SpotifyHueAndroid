/**
 * A service that runs in the background, even when the app is closed. Listens to Spotify events
 * and updates Philips Hue lights accordingly.
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import com.devankav.spotifyhue.bridgeCommunication.Bridge;
import com.devankav.spotifyhue.bridgeCommunication.BridgeStorage;
import com.devankav.spotifyhue.bridgeCommunication.Light;
import com.devankav.spotifyhue.bridgeCommunication.LightGroup;
import com.devankav.spotifyhue.bridgeConnection.BridgeConnector;
import com.devankav.spotifyhue.credentials.SpotifyCredentials;
import com.devankav.spotifyhue.observers.LightActiveObserver;
import com.devankav.spotifyhue.observers.PaletteObserver;
import com.devankav.spotifyhue.spotifyHelpers.AlbumArtPalette;
import com.devankav.spotifyhue.spotifyHelpers.StateParser;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.PlayerState;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Set;

public class LightSync extends Service {

    private BridgeConnector connector;
    private SpotifyAppRemote appRemote;
    private AlbumArtPalette albumArtPalette;
    private Bridge bridge;
    private LightGroup lightGroup;

    /**
     * A callback for when there was a player event
     * @param playerState
     */
    public void playerStateUpdated(PlayerState playerState) {
        Log.d("LightSync", playerState.track.name + " by " + playerState.track.artist.name);
        //Log.d("LightSync", playerState.track.toString());

        String url = StateParser.getImageURL(playerState);
        Picasso.get().load(url).into(albumArtPalette); // Update the palette
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        String id = bundle.getString("id");

        bridge = BridgeStorage.getBridge(id);
        Set<Light> lights = bridge.getLights();

        // Changes a light to the current color after it becomes activated
        bridge.subscribeToLightDiscovery(result -> { // Get all lights
            for (Light light : result.getLights()) { // Iterate over the lights
                // Add a new observer
                light.registerObserver(active -> {
                    if (active) { // Check if it is now active
                        Palette palette = albumArtPalette.getPalette(); // Get the current palette
                        light.updateLightColor(AlbumArtPalette.getXYColor(palette)); // Update the color
                    }
                });
            }
        });

        // Setup the connection parameters for the spotify remote
        ConnectionParams connectionParams = new ConnectionParams
                .Builder(SpotifyCredentials.CLIENT_ID)
                .setRedirectUri(SpotifyCredentials.REDIRECT_URI)
                .showAuthView(true)
                .build();

        SpotifyAppRemote.connect(this, connectionParams, new Connector.ConnectionListener() {
            @Override
            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                appRemote = spotifyAppRemote;
                appRemote.getPlayerApi().subscribeToPlayerState().setEventCallback(LightSync.this::playerStateUpdated);
                Log.d("LightSync", "Connected! Yay!");

                albumArtPalette = new AlbumArtPalette(); // Create a new album art palette

                PaletteObserver observer = updated -> {
                        for (Light light : lights) {
                            light.updateLightColor(AlbumArtPalette.getXYColor(updated));
                        }
                };

                albumArtPalette.registerObserver(observer); // Register the observer

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