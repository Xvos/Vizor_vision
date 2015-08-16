package com.example.Camera;

import android.graphics.Bitmap;
import com.example.Camera.editor.FilterPrefab;

/**
 * Created by netherwire on 8/9/15.
 */
public class NativeUtils {
    static {
        System.loadLibrary("rosetta");
    }

    public void blend(Bitmap bitmap, FilterPrefab prefab)
    {
        blendWithColor(bitmap, bitmap.getWidth(), bitmap.getHeight(), prefab.getBlendFunction().ordinal(),
                prefab.getColor(), prefab.getOpacity());
    }

    private native void blendWithColor(Bitmap imageData, int imageWidth, int imageHeight, int blendFunction, int color, float opacity);
}