package com.example.talegateinstagram.utils;

import android.graphics.Bitmap;

import org.jetbrains.annotations.NotNull;

public class BitmapScaler {

    public static Bitmap scaleToFitWidth (@NotNull Bitmap b, int width) {
        float factor = width / (float) b.getWidth();
        return Bitmap.createScaledBitmap(b, width, (int) (b.getHeight() * factor), true);
    }

    public static Bitmap scaleToFitHeight (@NotNull Bitmap b, int height) {
        float factor = height / (float) b.getHeight();
        return Bitmap.createScaledBitmap(b, height, (int) (b.getWidth() * factor), true);
    }
}
