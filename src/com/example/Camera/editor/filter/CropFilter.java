package com.example.Camera.editor.filter;

import android.graphics.Bitmap;

/**
 * Created by netherwire on 7/1/15.
 */
public class CropFilter implements Filter {

    private final int x;
    private final int y;
    private final int newWidth;
    private final int newHeight;

    public CropFilter(int x, int y, int newWidth, int newHeight)
    {
        this.x = x;
        this.y = y;
        this.newWidth = newWidth;
        this.newHeight = newHeight;
    }

    @Override
    public void process(Bitmap source, Bitmap destination)
    {

    }

    public static Bitmap centerCrop(Bitmap srcBmp)
    {
//        return srcBmp.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap bmp = null;
        if (srcBmp.getWidth() >= srcBmp.getHeight())
        {
            bmp = Bitmap.createBitmap(
                    srcBmp,
                    srcBmp.getWidth()/2 - srcBmp.getHeight()/2,
                    0,
                    srcBmp.getHeight(),
                    srcBmp.getHeight()
            );
        }
        else
        {
            bmp = Bitmap.createBitmap(
                    srcBmp,
                    0,
                    srcBmp.getHeight()/2 - srcBmp.getWidth()/2,
                    srcBmp.getWidth(),
                    srcBmp.getWidth()
            );
        }
        return bmp.copy(Bitmap.Config.ARGB_8888, true);
    }
}
