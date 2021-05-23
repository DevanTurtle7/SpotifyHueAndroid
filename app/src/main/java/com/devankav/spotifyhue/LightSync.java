/**
 * A service that runs in the background, even when the app is closed. Listens to Spotify events
 * and updates Philips Hue lights accordingly.
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.PlayerState;

public class LightSync extends Service {

    private SpotifyAppRemote appRemote;

    /**
     * Converts an RGB integer to the XY color space
     * @param color An RGB integer
     * @return An array of length 2, containing an x and y value
     */
    public double[] rgbToXY(int color) {
        int _R = (color >> 16) & 0xff;
        int _G = (color >> 8) & 0xff;
        int _B = (color) & 0xff;

        float R = _R / 255f;
        float G = _G / 255f;
        float B = _B / 255f;

        if (R > 0.04045) {
            R = (float) Math.pow((R + 0.055) / (1.0 + 0.055), 2.4);
        } else {
            R = (float) (R / 12.92);
        }

        if (G > 0.04045) {
            G = (float) Math.pow((G + 0.055) / (1.0 + 0.055), 2.4);
        } else {
            G = (float) (G / 12.92);
        }

        if (B > 0.04045) {
            B = (float) Math.pow((B + 0.055) / (1.0 + 0.055), 2.4);
        } else {
            B = (float) (B / 12.92);
        }

        double X = R * 0.664511 + G * 0.154324 + B * 0.162028;
        double Y = R * 0.283881 + G * 0.668433 + B * 0.047685;
        double Z = R * 0.000088 + G * 0.072310 + B * 0.986039;
        double x = X / (X + Y + Z);
        double y = Y / (X + Y + Z);


        double[] result = {x, y};

        return result;
    }

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
                appRemote = spotifyAppRemote;
                appRemote.getPlayerApi().subscribeToPlayerState().setEventCallback(LightSync.this::playerStateUpdated);
                Log.d("LightSync", "Connected! Yay!");
            }

            @Override
            public void onFailure(Throwable throwable) {
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