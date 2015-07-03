package com.example.Camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import java.io.FileOutputStream;

import java.io.File;


/**
 * Created by user on 18.06.2015.
 */
public class PreviewActivity extends Activity implements View.OnClickListener
{
    private Button okButton, cancelButton;
    private Bitmap bitmap;
    private byte[] pictureByteArray;
    private SurfaceView bitmapView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.preview);

        Intent intent = getIntent();
        pictureByteArray = intent.getByteArrayExtra(CameraActivity.PICTURE);
        bitmap = BitmapFactory.decodeByteArray( intent.getByteArrayExtra(CameraActivity.PICTURE), 0,  intent.getByteArrayExtra(CameraActivity.PICTURE).length);

        bitmapView = (SurfaceView) findViewById(R.id.BitmapView);

        Canvas c = new Canvas();
        c.drawBitmap(bitmap, 0, 0, null);
        bitmapView.draw(c);

        okButton = (Button) findViewById(R.id.OKButton);
        okButton.setText("OK");
        okButton.setOnClickListener(this);

        cancelButton = (Button) findViewById(R.id.CancelButton);
        cancelButton.setOnClickListener(this);
        cancelButton.setText("X");
        cancelButton.setX(200);

    }

    @Override
    public void onClick(View v)
    {
        if(v == cancelButton)
        {
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
        }
        else if(v == okButton)
        {
            try
            {
                File saveDir = new File("/sdcard/CameraExample/");

                if (!saveDir.exists())
                {
                    saveDir.mkdirs();
                }

                FileOutputStream os = new FileOutputStream(String.format("/sdcard/CameraExample/%d.jpg", System.currentTimeMillis()));
                os.write(pictureByteArray);
                os.close();

            }

            catch (Exception e)
            {

            }
        }
    }
}
