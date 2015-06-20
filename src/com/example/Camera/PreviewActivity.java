package com.example.Camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by user on 18.06.2015.
 */
public class PreviewActivity extends Activity implements View.OnClickListener
{
    private Button okButton, cancelButton;
    private Bitmap bitmap;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Intent intent = getIntent();

        bitmap = BitmapFactory.decodeByteArray( intent.getByteArrayExtra(CameraActivity.PICTURE), 0,  intent.getByteArrayExtra(CameraActivity.PICTURE).length);

    }

    @Override
    public void onClick(View v)
    {

    }
}
