package com.example.Camera.control;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by user on 28.06.2015.
 */
public class SaveController
{
    public static byte[] originalPicture;

    public static Bitmap tempBitmap;

    public static Uri pictureUri;

    public static void savePicture(Bitmap picture)
    {
        // сохраняем полученные jpg в папке /sdcard/CameraExample/
        // имя файла - System.currentTimeMillis()
        try
        {
            File saveDir = new File(Params.FOLDER_PATH);

            if (!saveDir.exists())
            {
                saveDir.mkdirs();
            }

            String filename = String.format(Params.FOLDER_PATH + "/%d.jpg", System.currentTimeMillis());
            FileOutputStream os = new FileOutputStream(filename);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            os.write(byteArray);
            os.close();

            tempBitmap = picture;

            pictureUri = Uri.parse("file://" + filename);
        }

        catch (Exception e)
        {

        }
    }

    public static byte[] convertBitmapToByteArray(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }
}
