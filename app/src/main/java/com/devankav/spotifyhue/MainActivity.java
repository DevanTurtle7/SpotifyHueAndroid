/**
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue;

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
import android.graphics.ColorSpace;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "d65cc0ee06034ea6aabec30bd2ec484d";
    private static final String REDIRECT_URI = "https://devanturtle7.github.io/SpotifyRedirect/";
    private static final String IMAGE_PREFIX = "https://i.scdn.co/image/";

    private SpotifyAppRemote appRemote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, LightSync.class)); // Start the background service

        setContentView(R.layout.activity_main);

        Button switchButton = findViewById(R.id.switchButton);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Setup the connection parameters for the spotify remote
        ConnectionParams connectionParams = new ConnectionParams
                .Builder(SpotifyCredentials.CLIENT_ID)
                .setRedirectUri(SpotifyCredentials.REDIRECT_URI)
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

    /**
     * Runs when the spotify remote is successfully connected to
     */
    private void connected() {
        appRemote.getPlayerApi().subscribeToPlayerState().setEventCallback(this::playerStateUpdated); // Subscribe to the player state
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(appRemote);
    }

    /**
     * Returns the URL of the album art of the current playing song
     * @param playerState The player state, given by the player api
     * @return A URL to the album art of the current song on spotify
     */
    private String getImageURL(PlayerState playerState) {
        ImageUri imageUri = playerState.track.imageUri; // Get the image uri from the player state
        String[] tokens = imageUri.toString().split(":"); // Split the uri on colons
        String endingCode = tokens[tokens.length - 1]; // Get the last token
        tokens = endingCode.split("'"); // Strip the end of the uri off
        String imageCode = tokens[0];
        String url = IMAGE_PREFIX + imageCode; // Assemble the url

        return url;
    }

    /**
     * Gets the most vibrant color from a given palette
     *
     * @param palette The palette being searched
     * @return The most vibrant color in the palette
     */
    public int getColor(Palette palette) {
        int color = palette.getVibrantColor(0);

        if (color == 0) {
            color = palette.getLightVibrantColor(0);
        }
        if (color == 0) {
            color = palette.getDarkVibrantColor(0);
        }
        if (color == 0) {
            color = palette.getDominantColor(0);
        }

        return color;
    }

    /**
     * Updates the album art palette
     *
     * @param url The url of the album art image
     */
    private void updatePalette(String url) {
        Picasso.get().load(url).into(new Target() { // Load the bitmap of the image
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        // Update swatch
                        View vibrant = findViewById(R.id.vibrant);
                        View vibrantDark = findViewById(R.id.vibrantDark);
                        View vibrantLight = findViewById(R.id.vibrantLight);
                        View muted = findViewById(R.id.muted);
                        View mutedDark = findViewById(R.id.mutedDark);
                        View mutedLight = findViewById(R.id.mutedLight);
                        View dominant = findViewById(R.id.dominant);

                        vibrant.setBackgroundColor(palette.getVibrantColor(0));
                        vibrantDark.setBackgroundColor(palette.getDarkVibrantColor(0));
                        vibrantLight.setBackgroundColor(palette.getLightVibrantColor(0));
                        muted.setBackgroundColor(palette.getMutedColor(0));
                        mutedDark.setBackgroundColor(palette.getDarkMutedColor(0));
                        mutedLight.setBackgroundColor(palette.getLightMutedColor(0));
                        dominant.setBackgroundColor(palette.getDominantColor(0));

                        // Update views with color
                        int color = getColor(palette);

                        ConstraintLayout constraintLayout = findViewById(R.id.mainConstraintLayout);
                        constraintLayout.setBackgroundColor(color);
                    }
                });
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    /**
     * Updates the album art display on the main page
     *
     * @param playerState The player state passed in from event updates
     */
    private void updateAlbumArt(PlayerState playerState) {
        ImageView albumArtView = findViewById(R.id.albumArt); // Get the ImageView
        String url = getImageURL(playerState); // Get the image url

        Picasso.get().load(url).into(albumArtView); // Update the image image view
    }

    /**
     * The callback for a player event
     *
     * @param playerState The state of player
     */
    private void playerStateUpdated(PlayerState playerState) {
        updateAlbumArt(playerState); // Update the album art

        String url = getImageURL(playerState);
        updatePalette(url); // Update the palette
    }
}