package com.example.Camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;

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

        return Bitmap.createScaledBitmap(bitmap, windowwidth, (int)(bitmap.getHeight() * scaleFactor), false);
    }

    public static Bitmap decodeInFullResolution(byte[] byteArray)
    {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inMutable = true;

        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, opt);
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
