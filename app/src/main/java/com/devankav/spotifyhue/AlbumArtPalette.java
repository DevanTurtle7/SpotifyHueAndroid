package com.devankav.spotifyhue;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashSet;
import java.util.Set;

public class AlbumArtPalette implements Target {

    private Set<PaletteObserver> observers;

    /**
     * The constructor
     */
    public AlbumArtPalette() {
        this.observers = new HashSet<PaletteObserver>();
    }

    public void registerObserver(PaletteObserver observer) {
        observers.add(observer);
    }

    public void deregisterObserver(PaletteObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Palette palette) {
        for (PaletteObserver observer : observers) {
            observer.paletteUpdated(palette);
        }
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                notifyObservers(palette);
            }
        });
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
