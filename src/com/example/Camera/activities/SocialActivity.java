package com.example.Camera.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.Camera.R;
import com.example.Camera.control.SaveController;
import com.example.Camera.control.SocialController;
import com.vk.sdk.VKSdk;

/**
 * Created by nikitavalavko on 04.07.15.
 */
public class SocialActivity extends Activity implements View.OnClickListener{

    private ImageView image;
    private EditText editText;
    private CheckBox checkBoxFaceBook, checkBoxVK, checkBoxInstagram, checkBoxTelegram, checkBoxViber;
    private Button postButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.social);
        VKSdk.initialize(this.getApplicationContext());

        Intent intent = getIntent();

        String text = intent.getExtras().getString("TEXT");

        image = (ImageView) findViewById(R.id.PostImageView);
        image.setImageBitmap(SaveController.bitmapToSave);
        editText = (EditText) findViewById(R.id.postTextField);
        editText.setText(text);

        checkBoxFaceBook = (CheckBox) findViewById(R.id.socialButton1);
        checkBoxFaceBook.setBackgroundResource(R.drawable.chekbox_res);

        checkBoxVK = (CheckBox) findViewById(R.id.socialButton2);
        checkBoxVK.setBackgroundResource(R.drawable.chekbox_res);

        checkBoxTelegram = (CheckBox) findViewById(R.id.socialButton3);
        checkBoxTelegram.setBackgroundResource(R.drawable.chekbox_res);

        checkBoxInstagram = (CheckBox) findViewById(R.id.socialButton4);
        checkBoxInstagram.setBackgroundResource(R.drawable.chekbox_res);

        checkBoxViber = (CheckBox) findViewById(R.id.socialButton5);
        checkBoxViber.setBackgroundResource(R.drawable.chekbox_res);

        checkBoxFaceBook.setOnClickListener(this);
        checkBoxVK.setOnClickListener(this);
        checkBoxTelegram.setOnClickListener(this);
        checkBoxInstagram.setOnClickListener(this);

        postButton = (Button) findViewById(R.id.postButton);
        postButton.setBackgroundResource(R.drawable.post_button);
        postButton.setOnClickListener(this);
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
            case R.id.socialButton4:
                Log.d("TAG", "SocialNet 4");
                break;
            case R.id.socialButton5:
                Log.d("TAG", "SocialNet 5");
                break;
            case R.id.postButton:
                post();
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

    public void post() {

        if (checkBoxFaceBook.isChecked()) {
            SocialController.postOnFacebook(this, editText.getText().toString());
        }

        if (checkBoxVK.isChecked()) {
            SocialController.postOnVk(this);
        }

        if (checkBoxInstagram.isChecked()) {
            SocialController.postOnInstagram(this);
        }

        if (checkBoxTelegram.isChecked()) {
            SocialController.postOnTelegram(this);
        }

        if (checkBoxViber.isChecked()) {
            SocialController.postOnViber(this);

         Intent intent = new Intent(this, CameraActivity.class);
         startActivity(intent);
         finish();
        }
    }
}
