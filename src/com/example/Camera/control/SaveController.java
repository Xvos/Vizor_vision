package com.example.Camera.control;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by user on 28.06.2015.
 */
public class SaveController
{
    public static byte[] originalPicture;

    public static byte[] lastSavedPicture;


    public static void savePicture(byte[] imageByteArray)
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

            Bitmap  bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            FileOutputStream os = new FileOutputStream(String.format(Params.FOLDER_PATH + "/%d.jpg", System.currentTimeMillis()));

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            os.write(byteArray);
            os.close();

            lastSavedPicture = byteArray;
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

    public static void postOnTelegram(Activity activity, byte[] imageByteArray, String text)
    {
        Intent waIntent = new Intent(Intent.ACTION_SEND);
        waIntent.setType("text/plain");
        waIntent.setPackage("org.telegram.messenger");
        if (waIntent != null)
        {
            waIntent.putExtra(Intent.EXTRA_TEXT, text );
            //waIntent.putExtra(Intent.EXTRA_STREAM, imageByteArray);
            activity.startActivity(Intent.createChooser(waIntent, "Share with"));
        }
        else
        {
            Toast.makeText(activity.getApplicationContext(), "Telegram is not installed", Toast.LENGTH_SHORT).show();
        }
    }
}
