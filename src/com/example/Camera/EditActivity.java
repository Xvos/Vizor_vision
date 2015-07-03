package com.example.Camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.FaceDetector;
import android.os.Bundle;
import android.util.Log;
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
    private Button saveButton, faceDetectButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit);

        Intent intent = getIntent();
        pictureByteArray = intent.getByteArrayExtra(CameraActivity.PICTURE);
        bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.length);


        Matrix matrix = new Matrix();

        matrix.postScale(1, 1);
        matrix.postRotate(270);

        Bitmap bitmapToSave = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        image = (ImageView) findViewById(R.id.editImage);
        image.setImageBitmap(bitmapToSave);

        saveButton = (Button) findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(this);

        faceDetectButton = (Button) findViewById(R.id.FaceDetectButton);
        faceDetectButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.SaveButton:
            {
                SaveController.savePicture(pictureByteArray);
            }
            break;
            case R.id.FaceDetectButton:
            {
                Bitmap  bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.length);

                FaceDetector fd = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), 5);
                FaceDetector.Face[] faces = new FaceDetector.Face[5];
                int c = fd.findFaces(bitmap, faces);
                for (int i = 0; i < c; i++)
                {
                    Log.d("TAG", Float.toString(faces[i].eyesDistance()));
                }
            }
            break;
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
