/**
 * A service that runs in the background, even when the app is closed. Listens to Spotify events
 * and updates Philips Hue lights accordingly.
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.palette.graphics.Palette;

import com.devankav.spotifyhue.R;
import com.devankav.spotifyhue.bridgeConnection.Bridge;
import com.devankav.spotifyhue.bridgeConnection.BridgeConnector;
import com.devankav.spotifyhue.bridgeConnection.LightUpdater;
import com.devankav.spotifyhue.credentials.SpotifyCredentials;
import com.devankav.spotifyhue.observers.PaletteObserver;
import com.devankav.spotifyhue.spotifyHelpers.AlbumArtPalette;
import com.devankav.spotifyhue.spotifyHelpers.StateParser;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Album;
import com.spotify.protocol.types.PlayerState;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

public class LightSync extends Service {

    private BridgeConnector connector;
    private SpotifyAppRemote appRemote;
    private AlbumArtPalette albumArtPalette;
    private LightUpdater lightUpdater;

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
        String ipAddress = bundle.getString("ipAddress");
        String id = bundle.getString("id");
        String username = bundle.getString("username");

        Bridge bridge = new Bridge(ipAddress, id, username);
        lightUpdater = new LightUpdater(ipAddress, id, username, this);

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


                // Create a new palette observer
                PaletteObserver observer = new PaletteObserver() {
                    @Override
                    public void notifyObserver(Palette updated) {
                        Log.d("LightSync", Arrays.toString(AlbumArtPalette.getXYColor(updated)));
                        lightUpdater.getLights();
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