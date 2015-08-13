package com.example.Camera;

import android.graphics.Bitmap;

import com.example.Camera.editor.FilterType;

/**
 * Created by netherwire on 8/9/15.
 */
public class NativeUtils {
    static {
        System.loadLibrary("rosetta");
    }

    public void blend(Bitmap bitmap, FilterType filterType) {
        blendWithColor(bitmap, bitmap.getWidth(), bitmap.getHeight(), filterType.getValue());
    }

    private native void blendWithColor(Bitmap imageData, int imageWidth, int imageHeight, int blendFunction);
}