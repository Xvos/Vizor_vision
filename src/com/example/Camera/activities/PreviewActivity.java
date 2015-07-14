package com.example.Camera.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.example.Camera.R;


/**
 * Created by user on 18.06.2015.
 */
public class PreviewActivity extends Activity implements View.OnClickListener
{
    private Button okButton, cancelButton;
    private Bitmap bitmap;
    private byte[] pictureByteArray;
    private SurfaceView bitmapView;
    private ImageView image;

    private Bitmap bitmapToSave;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.preview);

        Intent intent = getIntent();
        pictureByteArray = intent.getByteArrayExtra(CameraActivity.PICTURE);
        bitmap = BitmapFactory.decodeByteArray(intent.getByteArrayExtra(CameraActivity.PICTURE), 0, intent.getByteArrayExtra(CameraActivity.PICTURE).length);

        bitmapView = (SurfaceView) findViewById(R.id.BitmapView);

        image = (ImageView) findViewById(R.id.imageView1);
        image.setImageBitmap(bitmap);

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
            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra(CameraActivity.PICTURE, pictureByteArray);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    }
}
