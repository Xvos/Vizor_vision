package com.vizor.vision;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.vizor.vision.control.SaveController;

/**
 * Created by netherwire on 8/18/15.
 */
public class BitmapFactoryHelper
{
    public static Bitmap decodeInScreenResolution(Activity activity, byte[] byteArray)
    {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;

        // Decode bounds
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, opt);


        int windowwidth = activity.getWindowManager().getDefaultDisplay().getWidth();
        int windowheight = activity.getWindowManager().getDefaultDisplay().getHeight();

        // TODO: min просто из коэффициентов а не из логов
        int downsample = Math.min(log2(opt.outWidth / windowwidth), log2(opt.outHeight / windowheight));

        // Fetch bitmap!
        opt.inJustDecodeBounds = false;
        opt.inSampleSize = (int)Math.pow(2, downsample);

        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, opt);
        float scaleFactor = (float)windowwidth / (float)bitmap.getWidth();

        Matrix matrix = new Matrix();
        matrix.postRotate(SaveController.Rotation);
        matrix.postScale(scaleFactor, scaleFactor);

        Bitmap bmpToReturn = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);

        // Destroy old bitmap
        bitmap.recycle();
        bitmap = null;
        System.gc();

        return bmpToReturn;
    }

    public static Bitmap decodeInFullResolution(byte[] byteArray)
    {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inMutable = true;

        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, opt);

        Bitmap bmpToReturn = null;

        // Если не надо поворачивать - нет смысла гнать через матрицу
        if(SaveController.Rotation != 0)
        {
            Matrix matrix = new Matrix();
            matrix.postRotate(SaveController.Rotation);

            bmpToReturn = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);

            // Destroy old bitmap
            bitmap.recycle();
            bitmap = null;
            System.gc();
        }
        else
        {
            bmpToReturn = bitmap;
        }

        return bmpToReturn;
    }

    public static int log2( int bits )
    {
        int log = 0;
        if( ( bits & 0xffff0000 ) != 0 ) { bits >>>= 16; log = 16; }
        if( bits >= 256 ) { bits >>>= 8; log += 8; }
        if( bits >= 16  ) { bits >>>= 4; log += 4; }
        if( bits >= 4   ) { bits >>>= 2; log += 2; }
        return log + ( bits >>> 1 );
    }
}
