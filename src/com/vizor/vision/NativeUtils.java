package com.vizor.vision;

import android.graphics.Bitmap;
import com.vizor.vision.editor.FilterPrefab;

/**
 * Created by netherwire on 8/9/15.
 */
public class NativeUtils {
    static {
        System.loadLibrary("rosetta");
    }

    public void blend(Bitmap bitmap, FilterPrefab prefab)
    {
        blendWithColor(bitmap, prefab.getBlendFunction().ordinal(), prefab.getColor(), prefab.getOpacity());
    }

    public void blend(Bitmap bitmap1, Bitmap bitmap2)
    {
        blendWithImage(bitmap1, bitmap2);
    }

    public void grayscale(Bitmap bitmap)
    {
        makeGrayscale(bitmap);
    }

    private native void blendWithColor(Bitmap bitmap, int blendFunction, int color, float opacity);

    private native void blendWithImage(Bitmap bitmap1, Bitmap bitmap2);

    private native void makeGrayscale(Bitmap bitmap);
}