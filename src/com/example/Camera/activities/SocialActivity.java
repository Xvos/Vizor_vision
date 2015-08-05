package com.example.Camera.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.Camera.R;
import com.example.Camera.control.SaveController;

/**
 * Created by nikitavalavko on 04.07.15.
 */
public class SocialActivity extends Activity implements View.OnClickListener{

    private ImageView image;
    private EditText editText;
    private CheckBox checkBoxFaceBook, checkBoxVK, checkBoxInstagram, checkBoxTelegram;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.social);

        Intent intent = getIntent();

        String text = intent.getExtras().getString("TEXT");
        Bitmap bitmap = BitmapFactory.decodeByteArray(SaveController.lastSavedPicture, 0, SaveController.lastSavedPicture.length);

        image = (ImageView) findViewById(R.id.PostImageView);
        image.setImageBitmap(bitmap);
        editText = (EditText) findViewById(R.id.PostText);
        editText.setText(text);

        checkBoxFaceBook = (CheckBox) findViewById(R.id.socialButton1);
        checkBoxFaceBook.setBackgroundResource(R.drawable.chekbox_res);

        checkBoxVK = (CheckBox) findViewById(R.id.socialButton2);
        checkBoxVK.setBackgroundResource(R.drawable.chekbox_res);

        checkBoxTelegram = (CheckBox) findViewById(R.id.socialButton3);
        checkBoxTelegram.setBackgroundResource(R.drawable.chekbox_res);

        checkBoxInstagram = (CheckBox) findViewById(R.id.socialButton4);
        checkBoxInstagram.setBackgroundResource(R.drawable.chekbox_res);


        checkBoxFaceBook.setOnClickListener(this);
        checkBoxVK.setOnClickListener(this);
        checkBoxTelegram.setOnClickListener(this);
        checkBoxInstagram.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.socialButton1:
                Log.d("TAG", "SocialNet 1");
            break;
            case R.id.socialButton2:
                Log.d("TAG", "SocialNet 2");
                break;
            case R.id.socialButton3:
                Log.d("TAG", "SocialNet 3");
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
}
