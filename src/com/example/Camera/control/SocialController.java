package com.example.Camera.control;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.Camera.activities.SocialActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SocialController {

    public static void postOnFacebook(Activity activity, byte[] imageByteArray, String text) {
    }

    public static void postOnVk(Activity activity, byte[] imageByteArray, String text) {
    }

    public static void postOnInstagram(Activity activity, byte[] imageByteArray, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.setPackage("com.instagram.android");
        if (intent != null)
        {
            intent.putExtra(Intent.EXTRA_STREAM, SaveController.pictureUri);
            activity.startActivity(Intent.createChooser(intent, "Share with"));
        }
        else
        {
            Toast.makeText(activity.getApplicationContext(), "Instagram is not installed", Toast.LENGTH_SHORT).show();
        }
    }

    public static void postOnTelegram(Activity activity, byte[] imageByteArray, String text)
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.setPackage("org.telegram.messenger");
        if (intent != null)
        {
            intent.putExtra(Intent.EXTRA_STREAM, SaveController.pictureUri);
            activity.startActivity(Intent.createChooser(intent, "Share with"));
        }
        else
        {
            Toast.makeText(activity.getApplicationContext(), "Telegram is not installed", Toast.LENGTH_SHORT).show();
        }
    }

}
