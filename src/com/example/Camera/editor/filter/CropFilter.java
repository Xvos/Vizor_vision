package com.example.Camera.editor.filter;

import android.graphics.Bitmap;

/**
 * Created by netherwire on 7/1/15.
 */
public class CropFilter implements Filter {

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    // X and y coordinates on an output bitmap
    private final int destX;
    private final int destY;

    public CropFilter(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.destX = 0;
        this.destY = 0;
    }

    public CropFilter(int x, int y, int width, int height, int destX, int destY) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.destX = destX;
        this.destY = destY;
    }

    @Override
    public void process(Bitmap source, Bitmap destination) {

        int destWidth = destination.getWidth();
        int destHeight = destination.getHeight();

        int shiftWidth = Math.min(source.getWidth() - x, Math.min(destination.getWidth(), width));
        int shiftHeight = Math.min(source.getHeight() - y, Math.min(destination.getHeight(), height));

        int[] pixels = new int[shiftWidth * shiftHeight];
        source.getPixels(pixels, 0, shiftWidth, x, y, shiftWidth, shiftHeight);

        shiftWidth = Math.min(shiftWidth, destination.getWidth() - destX);
        shiftHeight = Math.min(shiftHeight, destination.getHeight() - destY);

        destination.setPixels(pixels, 0, shiftWidth, destX, destY, shiftWidth, shiftHeight);
    }
}
