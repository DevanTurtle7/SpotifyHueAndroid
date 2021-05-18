package com.devankav.spotifytest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.spotify.android.appremote.api.AppRemote;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.PlayerState;

public class LightSync extends Service {

    private static final String CLIENT_ID = "d65cc0ee06034ea6aabec30bd2ec484d";
    private static final String REDIRECT_URI = "https://devanturtle7.github.io/SpotifyRedirect/";
    private SpotifyAppRemote appRemote;

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
        ConnectionParams connectionParams = new ConnectionParams
                .Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
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
                Log.e("LightSync", throwable.getMessage(), throwable);
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }
}