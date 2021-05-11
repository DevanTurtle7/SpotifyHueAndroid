package com.devankav.spotifytest;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.ContentApi;
import com.spotify.android.appremote.api.PlayerApi;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.android.appremote.api.UserApi;
import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.Capabilities;
import com.spotify.protocol.types.Empty;
import com.spotify.protocol.types.Item;
import com.spotify.protocol.types.LibraryState;
import com.spotify.protocol.types.ListItem;
import com.spotify.protocol.types.ListItems;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import com.spotify.protocol.types.UserStatus;

import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "d65cc0ee06034ea6aabec30bd2ec484d";
    private static final String REDIRECT_URI = "https://devanturtle7.github.io/SpotifyRedirect/";
    private SpotifyAppRemote mSpotifyAppRemote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ConnectionParams connectionParams = new ConnectionParams
                .Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(true)
                .build();

        SpotifyAppRemote.connect(this, connectionParams, new Connector.ConnectionListener() {
            @Override
            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                mSpotifyAppRemote = spotifyAppRemote;
                Log.d("MainActivity", "Connected! Yay!");

                connected();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("MainActivity", throwable.getMessage(), throwable);
            }
        });
    }

    private void connected() {
        /**
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:2kKTWsGxXYt0DXBXQkHejx");

        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                   final Track track = playerState.track;
                   if (track != null) {
                       Log.d("MainActivity", track.name + " by " + track.artist.name);
                   }
                });
         **/

        ProgressBar progressBar1 = findViewById(R.id.progressBar1);
        ProgressBar progressBar2 = findViewById(R.id.progressBar2);
        ProgressBar progressBar3 = findViewById(R.id.progressBar3);
        ProgressBar progressBar4 = findViewById(R.id.progressBar4);
        ProgressBar progressBar5 = findViewById(R.id.progressBar5);
        ProgressBar progressBar6 = findViewById(R.id.progressBar6);
        ProgressBar progressBar7 = findViewById(R.id.progressBar7);
        ProgressBar progressBar8 = findViewById(R.id.progressBar8);
        ProgressBar progressBar9 = findViewById(R.id.progressBar9);
        ProgressBar progressBar10 = findViewById(R.id.progressBar10);
        ProgressBar progressBar11 = findViewById(R.id.progressBar11);
        ProgressBar progressBar12 = findViewById(R.id.progressBar12);
        ProgressBar progressBar13 = findViewById(R.id.progressBar13);
        ProgressBar progressBar14 = findViewById(R.id.progressBar14);
        ProgressBar progressBar15 = findViewById(R.id.progressBar15);

        Map<ProgressBar, Integer> indexes = new HashMap<>();
        indexes.put(progressBar1, 0);
        indexes.put(progressBar2, 8);
        indexes.put(progressBar3, 16);
        indexes.put(progressBar4, 24);
        indexes.put(progressBar5, 32);
        indexes.put(progressBar6, 40);
        indexes.put(progressBar7, 48);
        indexes.put(progressBar8, 56);
        indexes.put(progressBar9, 64);
        indexes.put(progressBar10, 72);
        indexes.put(progressBar11, 80);
        indexes.put(progressBar12, 88);
        indexes.put(progressBar13, 96);
        indexes.put(progressBar14, 104);
        indexes.put(progressBar15, 112);

        ProgressBar[] progressBars = {progressBar1, progressBar2, progressBar3, progressBar4, progressBar5};

        Visualizer.OnDataCaptureListener listener = new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int i) {
                for (int x = 0; x < progressBars.length; x++) {
                    ProgressBar current = progressBars[x];
                    int to = (25 * (x + 1));
                    int from = (25 * x);
                    int diff = to - from;
                    int sum = 0;

                    Log.d("MainActivity", "To: " + to + " From : " + from);

                    for (int k = from; k < to; k++) {
                        sum += bytes[k];
                    }

                    double level = sum / diff;

                    double progress = level + 128;
                    progress /= 256;
                    progress *= 100;

                    current.setProgress((int) progress);
                    Log.d("MainActivity", progress + "");
                }
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int i) {
                for (ProgressBar progressBar : indexes.keySet()) {
                    int index = indexes.get(progressBar);
                    byte level = bytes[index];
                    double progress = level + 128;
                    progress /= 256;
                    progress *= 100;

                    progressBar.setProgress((int) progress);
                }
            }
        };

        Visualizer visualizer = new Visualizer(0);
        visualizer.setDataCaptureListener(listener, Visualizer.getMaxCaptureRate(), false, true);
        visualizer.setCaptureSize(128);
        visualizer.setEnabled(true);

        Log.d("MainActivity", visualizer.getCaptureSizeRange()[0] + " " + visualizer.getCaptureSizeRange()[1]);

/*
        ContentApi contentApi = mSpotifyAppRemote.getContentApi();
        CallResult<ListItems> result = contentApi.getRecommendedContentItems(ContentApi.ContentType.DEFAULT).setResultCallback((e) -> {
            Log.d("MainActivity","hello");

            for (ListItem item : e.items) {
                contentApi.getChildrenOfItem(item, 100, 0).setResultCallback((t) -> {
                    for (ListItem x : t.items) {
                        Log.d("MainActivity", item.title + ": " + x.title);
                    }
                });

                Log.d("MainActivity", item.title);
            }
        });
        */

    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }
}