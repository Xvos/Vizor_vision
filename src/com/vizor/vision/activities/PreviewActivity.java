package com.vizor.vision.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.vizor.vision.BitmapFactoryHelper;
import com.vizor.vision.R;
import com.vizor.vision.control.SaveController;


/**
 * Created by user on 18.06.2015.
 */
public class PreviewActivity extends Activity implements View.OnClickListener
{
    private Button okButton, cancelButton;
    private Bitmap bitmap;
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

        bitmap = BitmapFactoryHelper.decodeInScreenResolution(this, SaveController.originalPicture);
        SaveController.tempBitmap = bitmap;

        // Do other stuff

        bitmapView = (SurfaceView) findViewById(R.id.BitmapView);

        image = (ImageView) findViewById(R.id.imageView1);
        image.setImageBitmap(bitmap);

        okButton = (Button) findViewById(R.id.OKButton);
        okButton.setOnClickListener(this);
        okButton.setBackgroundResource(R.drawable.ok_button);

        cancelButton = (Button) findViewById(R.id.CancelButton);
        cancelButton.setBackgroundResource(R.drawable.close_button);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if(v == cancelButton)
        {
            SaveController.tempBitmap = null;
            SaveController.originalPicture = null;

            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
            finish();
        }
        else
        if(v == okButton)
        {
            Intent intent = new Intent(this, EditActivity.class);
             startActivity(intent);
            finish();
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
