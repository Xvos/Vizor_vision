package com.example.Camera.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.FaceDetector;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.Camera.R;
import com.example.Camera.control.SaveController;
import com.example.Camera.editor.filter.CropFilter;

/**
 * Created by user on 28.06.2015.
 */
public class EditActivity extends Activity implements View.OnClickListener
{
    private Bitmap bitmap, originalBitmap;
    private byte[] pictureByteArray;
    private ImageView image;
    private Button saveButton, faceDetectButton, textButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit);

        Intent intent = getIntent();
        pictureByteArray = intent.getByteArrayExtra(CameraActivity.PICTURE);
        bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.length);


        Bitmap bitmapToSave = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), null, true);

        originalBitmap  = bitmapToSave.copy(bitmapToSave.getConfig(), true);

        image = (ImageView) findViewById(R.id.editImage);
        image.setImageBitmap(bitmapToSave);

        saveButton = (Button) findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(this);

        faceDetectButton = (Button) findViewById(R.id.FaceDetectButton);
        faceDetectButton.setOnClickListener(this);

        textButton = (Button) findViewById(R.id.AddText);
        textButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.SaveButton:
            {
                parseBitmapAndSave();
                Intent intent = new Intent(this, SocialActivity.class);
                startActivity(intent);
                finish();
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
            case R.id.CropButton:
            {
                //CropFilter
            }
            break;
            case  R.id.AddText:
            {
                EditText text = new EditText(getApplicationContext());
                FrameLayout layout = (FrameLayout)findViewById(R.id.GalleryLayout);
                layout.addView(text);

                text.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                text.setTextColor(Color.WHITE);
                text.setText("#Vizor");
                text.setGravity(Gravity.CENTER);

              /*  Canvas canvas = new Canvas(bitmap);
                // new antialised Paint
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                // text color - #3D3D3D
                paint.setColor(Color.rgb(61, 61, 61));
                // text size in pixels
                paint.setTextSize((int) (14 * scale));
                // text shadow
                paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

                // draw text to the Canvas center
                Rect bounds = new Rect();
                paint.getTextBounds(gText, 0, gText.length(), bounds);
                int x = (bitmap.getWidth() - bounds.width())/2;
                int y = (bitmap.getHeight() + bounds.height())/2;

                canvas.drawText(gText, x, y, paint);*/
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

    private void parseBitmapAndSave()
    {
        SaveController.savePicture(pictureByteArray);

    }
}
