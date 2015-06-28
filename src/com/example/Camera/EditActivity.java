package com.example.Camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import com.example.Camera.control.SaveController;

/**
 * Created by user on 28.06.2015.
 */
public class EditActivity extends Activity implements View.OnClickListener
{
    private Bitmap bitmap;
    private byte[] pictureByteArray;
    private ImageView image;
    private Button saveButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit);

        Intent intent = getIntent();
        pictureByteArray = intent.getByteArrayExtra(CameraActivity.PICTURE);
        bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.length);

        image = (ImageView) findViewById(R.id.editImage);
        image.setImageBitmap(bitmap);

        saveButton = (Button) findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
       if(v == saveButton)
       {
           SaveController.savePicture(pictureByteArray);
       }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
        finish();
    }
}
