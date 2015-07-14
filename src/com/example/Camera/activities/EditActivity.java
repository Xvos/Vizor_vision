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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.example.Camera.R;
import com.example.Camera.control.SaveController;
import com.example.Camera.editor.filter.CropFilter;

/**
 * Created by user on 28.06.2015.
 */
public class EditActivity extends Activity implements View.OnClickListener, Animation.AnimationListener
{
    private Bitmap bitmap, originalBitmap;
    private byte[] pictureByteArray;
    private ImageView image;
    private Button saveButton, faceDetectButton, textButton, filtersButton;
    private Animation moveUpAnim, moveDownAnim;
    private HorizontalScrollView buttonScroll;
    private EditText text;

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

        filtersButton = (Button) findViewById(R.id.FiltersButton);
        filtersButton.setOnClickListener(this);

        moveUpAnim = AnimationUtils.loadAnimation(this, R.anim.anim);
        moveUpAnim.setAnimationListener(this);
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
                text = new EditText(getApplicationContext());
                FrameLayout layout = (FrameLayout)findViewById(R.id.GalleryLayout);
                layout.addView(text);

                text.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                text.setTextColor(Color.WHITE);
                text.setText("#Vizor");
                text.setGravity(Gravity.CENTER);
                text.setHeight(30);

            }
            break;
            case R.id.FiltersButton:
            {
                buttonScroll = (HorizontalScrollView) findViewById(R.id.funcScroll);
                buttonScroll.startAnimation(moveUpAnim);
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

    @Override
    public void onAnimationEnd(Animation animation){}

    @Override
    public void onAnimationRepeat(Animation animation){}

    @Override
    public void onAnimationStart(Animation animation)
    {
        // This is the key...
        //set the coordinates for the bounds (left, top, right, bottom) based on the offset value (50px) in a resource XML
        //LL.layout(0, -(int)this.getResources().getDimension(R.dimen.quickplay_offset),
        //        LL.getWidth(), LL.getHeight() + (int)this.getResources().getDimension(R.dimen.quickplay_offset));
    }
}
