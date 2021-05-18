package com.devankav.spotifytest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.palette.graphics.Palette;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.ImageUri;
import com.spotify.protocol.types.PlayerState;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "d65cc0ee06034ea6aabec30bd2ec484d";
    private static final String REDIRECT_URI = "https://devanturtle7.github.io/SpotifyRedirect/";
    private static final String IMAGE_PREFIX = "https://i.scdn.co/image/";

    private SpotifyAppRemote appRemote;

    private Palette albumArtPalette;
    private Object paletteLock = new Object();
    private Thread colorUpdater = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, LightSync.class)); // Start the background service

        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Setup the connection parameters for the spotify remote
        ConnectionParams connectionParams = new ConnectionParams
                .Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(true)
                .build();

        // Connect to the Spotify remote
        SpotifyAppRemote.connect(this, connectionParams, new Connector.ConnectionListener() {
            @Override
            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                appRemote = spotifyAppRemote; // Get the app remote
                connected();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("MainActivity", throwable.getMessage(), throwable);
            }
        });
    }

    private void connected() {
        /*
        GlobalRequestQueue queue = new GlobalRequestQueue(getApplicationContext());

        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Log.d("MainActivity", response.getJSONObject(i).getString("id"));
                    } catch (JSONException e) {
                        Log.e("MainActivity", "Caught JSON exception: " + e.getMessage());
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MainActivity", error.getMessage());
            }
        };

        String url = "https://discovery.meethue.com";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, listener, errorListener);
        queue.getRequestQueue().add(jsonArrayRequest);
         */

        appRemote.getPlayerApi().subscribeToPlayerState().setEventCallback(this::playerEventCallback); // Subscribe to the player state

        if (colorUpdater == null) {
            colorUpdater = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        synchronized (paletteLock) {
                            try {
                                Log.d("MainActivity", "Waiting");
                                paletteLock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        Palette palette = getAlbumArtPalette();

                        Log.d("MainActivity", "Updating color");
                        ConstraintLayout constraintLayout = findViewById(R.id.mainConstraintLayout);
                        constraintLayout.setBackgroundColor(palette.getVibrantColor(0));
                    }
                }
            });

            colorUpdater.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(appRemote);
    }

    public Palette getAlbumArtPalette() {
        return albumArtPalette;
    }

    private String getImageURL(PlayerState playerState) {
        ImageUri imageUri = playerState.track.imageUri; // Get the image uri from the player state
        String[] tokens = imageUri.toString().split(":"); // Split the uri on colons
        String endingCode = tokens[tokens.length - 1]; // Get the last token
        tokens = endingCode.split("'"); // Strip the end of the uri off
        String imageCode = tokens[0];
        String url = IMAGE_PREFIX + imageCode; // Assemble the url

        return url;
    }

    public void updatePalette(String url) {
        Picasso.get().load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Palette palette = Palette.from(bitmap).generate();
                albumArtPalette = palette;

                synchronized (paletteLock) {
                    paletteLock.notifyAll();
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    private void updateAlbumArt(PlayerState playerState) {
        ImageView albumArtView = findViewById(R.id.albumArt); // Get the ImageView
        String url = getImageURL(playerState); // Get the image url

        Picasso.get().load(url).into(albumArtView); // Update the image image view
    }

    private void playerEventCallback(PlayerState playerState) {
        updateAlbumArt(playerState);

        String url = getImageURL(playerState);
        updatePalette(url);
    }
}