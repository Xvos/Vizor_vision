package com.vizor.vision.editor.filter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * Created by netherwire on 7/1/15.
 */
public class SimpleGrayscaleFilter implements Filter {

    @Override
    public void process(Bitmap source, Bitmap destination)
    {
        Canvas c = new Canvas(destination);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        //cm.set();
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);

        c.drawBitmap(source, 0, 0, paint);
    }

}
