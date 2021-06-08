/**
 * A target that can load bitmaps and generates palettes. Supports palette observers. Contains
 * static functions for analyzing and converting colors.
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import com.devankav.spotifyhue.observers.Observable;
import com.devankav.spotifyhue.observers.PaletteObserver;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class AlbumArtPalette extends Observable<PaletteObserver> implements Target {

    /**
     * The constructor
     */
    public AlbumArtPalette() {
    }

    /**
     * Gets the most vibrant color from a given palette
     *
     * @param palette The palette being searched
     * @return The most vibrant color in the palette
     */
    public static int getColor(Palette palette) {
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
     * Converts an RGB integer to the XY color space
     * @param color An RGB integer
     * @return An array of length 2, containing an x and y value
     */
    public static double[] rgbToXY(int color) {
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
     * Notifies all of the registered observers that an update has occurred
     * @param palette The palette being updated
     */
    public void notifyObservers(Palette palette) {
        for (PaletteObserver observer : observers) { // Iterate over each observer
            observer.notifyObserver(palette); // Notify the observer
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
