/**
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.palette.graphics.Palette;

import com.devankav.spotifyhue.bridgeConnection.Bridge;
import com.devankav.spotifyhue.credentials.SpotifyCredentials;
import com.devankav.spotifyhue.observers.PaletteObserver;
import com.devankav.spotifyhue.services.LightSync;
import com.devankav.spotifyhue.spotifyHelpers.StateParser;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.PlayerState;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "d65cc0ee06034ea6aabec30bd2ec484d";
    private static final String REDIRECT_URI = "https://devanturtle7.github.io/SpotifyRedirect/";

    private SpotifyAppRemote appRemote;
    private AlbumArtPalette albumArtPalette;
    private Bridge bridge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, LightSync.class)); // Start the background service

        setContentView(R.layout.activity_main);

        Button switchButton = findViewById(R.id.switchButton);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DiscoveryActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Bundle bundle = getIntent().getExtras();
        String ipAddress = bundle.getString("ipAddress");
        String id = bundle.getString("id");
        String username = bundle.getString("username");

        bridge = new Bridge(ipAddress, id, username);

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
        albumArtPalette = new AlbumArtPalette(); // Create a new album art palette

        // Create a new palette observer
        PaletteObserver observer = new PaletteObserver() {
            @Override
            public void notifyObserver(Palette updated) {

                // Update swatch
                View vibrant = findViewById(R.id.vibrant);
                View vibrantDark = findViewById(R.id.vibrantDark);
                View vibrantLight = findViewById(R.id.vibrantLight);
                View muted = findViewById(R.id.muted);
                View mutedDark = findViewById(R.id.mutedDark);
                View mutedLight = findViewById(R.id.mutedLight);
                View dominant = findViewById(R.id.dominant);

                vibrant.setBackgroundColor(updated.getVibrantColor(0));
                vibrantDark.setBackgroundColor(updated.getDarkVibrantColor(0));
                vibrantLight.setBackgroundColor(updated.getLightVibrantColor(0));
                muted.setBackgroundColor(updated.getMutedColor(0));
                mutedDark.setBackgroundColor(updated.getDarkMutedColor(0));
                mutedLight.setBackgroundColor(updated.getLightMutedColor(0));
                dominant.setBackgroundColor(updated.getDominantColor(0));

                // Update views with color
                int color = AlbumArtPalette.getColor(updated);

                ConstraintLayout constraintLayout = findViewById(R.id.mainConstraintLayout);
                constraintLayout.setBackgroundColor(color);
            }
        };

        albumArtPalette.registerObserver(observer); // Register the observer
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(appRemote);
    }

    /**
     * Updates the album art display on the main page
     *
     * @param playerState The player state passed in from event updates
     */
    private void updateAlbumArt(PlayerState playerState) {
        ImageView albumArtView = findViewById(R.id.albumArt); // Get the ImageView
        String url = StateParser.getImageURL(playerState); // Get the image url

        Picasso.get().load(url).into(albumArtView); // Update the image image view
    }

    /**
     * The callback for a player event
     *
     * @param playerState The state of player
     */
    private void playerStateUpdated(PlayerState playerState) {
        updateAlbumArt(playerState); // Update the album art

        String url = StateParser.getImageURL(playerState);
        Picasso.get().load(url).into(albumArtPalette); // Update the palette
    }
}