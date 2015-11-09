package com.vizor.vision.control;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

public class SocialController {

    public static void postOnFacebook(Activity activity, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setPackage("com.facebook.katana");
        if (intent != null)
        {
            intent.putExtra(Intent.EXTRA_STREAM, SaveController.pictureUri);
            intent.setType("image/jpeg");
            activity.startActivity(Intent.createChooser(intent, "Share with"));
        }
        else
        {
            Toast.makeText(activity.getApplicationContext(), "Facebook application is not installed",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static void postOnVk(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setPackage("com.vkontakte.android");
        if (intent != null)
        {
            intent.putExtra(Intent.EXTRA_STREAM, SaveController.pictureUri);
            intent.setType("image/jpeg");
            activity.startActivity(Intent.createChooser(intent, "Share with"));
        }
        else
        {
            Toast.makeText(activity.getApplicationContext(), "VK application is not installed",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static void postOnInstagram(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setPackage("com.instagram.android");
        if (intent != null)
        {
            intent.putExtra(Intent.EXTRA_STREAM, SaveController.pictureUri);
            intent.setType("image/jpeg");
            activity.startActivity(Intent.createChooser(intent, "Share with"));
        }
        else
        {
            Toast.makeText(activity.getApplicationContext(), "Instagram application is not installed",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static void postOnTelegram(Activity activity)
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setPackage("org.telegram.messenger");
        if (intent != null)
        {
            intent.putExtra(Intent.EXTRA_STREAM, SaveController.pictureUri);
            intent.setType("image/jpeg");
            activity.startActivity(Intent.createChooser(intent, "Share with"));
        }
        else
        {
            Toast.makeText(activity.getApplicationContext(), "Telegram application is not installed",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static void postOnViber(Activity activity)
    {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("image/png");
        share.setPackage("com.viber.voip");
        if (share != null)
        {
            share.putExtra(Intent.EXTRA_STREAM, SaveController.pictureUri);
            activity.startActivity(share.createChooser(share, "Share With"));
        }
        else
        {
            Toast.makeText(activity.getApplicationContext(), "Viber application is not installed",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static void postOnWhatsApp(Activity activity)
    {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.setPackage("com.whatsapp");
        if (share != null)
        {
            share.putExtra(Intent.EXTRA_STREAM, SaveController.pictureUri);
            activity.startActivity(share.createChooser(share, "Share With"));
        }
        else
        {
            Toast.makeText(activity.getApplicationContext(), "WhatsApp application is not installed",
                    Toast.LENGTH_SHORT).show();
        }
    }
}