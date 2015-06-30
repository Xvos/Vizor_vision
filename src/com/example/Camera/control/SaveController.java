package com.example.Camera.control;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.Camera.CameraActivity;
import com.example.Camera.EditActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by user on 28.06.2015.
 */
public class SaveController
{
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
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            os.write(byteArray);
            os.close();
        }

        catch (Exception e)
        {

        }
    }

    public static byte[] convertBitmapToByteArray(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
