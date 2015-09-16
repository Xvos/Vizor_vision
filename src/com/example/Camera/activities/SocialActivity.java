package com.example.Camera.activities;

import android.app.Activity;
import android.content.Intent;
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
    private CheckBox checkBoxFaceBook, checkBoxVK, checkBoxInstagram, checkBoxTelegram,
            checkBoxViber, checkBoxWhatsApp;
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
        image.setImageBitmap(SaveController.tempBitmap);
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

        checkBoxWhatsApp = (CheckBox) findViewById(R.id.socialButton6);
        checkBoxWhatsApp.setBackgroundResource(R.drawable.chekbox_res);

        checkBoxFaceBook.setOnClickListener(this);
        checkBoxVK.setOnClickListener(this);
        checkBoxTelegram.setOnClickListener(this);
        checkBoxInstagram.setOnClickListener(this);
        checkBoxViber.setOnClickListener(this);
        checkBoxWhatsApp.setOnClickListener(this);

        postButton = (Button) findViewById(R.id.postButton);
        postButton.setBackgroundResource(R.drawable.post_button);
        postButton.setOnClickListener(this);

        findViewById(R.id.textFace).setOnClickListener(this);
        findViewById(R.id.textINSTA).setOnClickListener(this);
        findViewById(R.id.textTELE).setOnClickListener(this);
        findViewById(R.id.textVIBER).setOnClickListener(this);
        findViewById(R.id.textVK).setOnClickListener(this);
        findViewById(R.id.textWSAPP).setOnClickListener(this);
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
            case R.id.socialButton6:
                Log.d("TAG", "SocialNet 6");
                break;
            case R.id.postButton:
                post();
                break;

            case R.id.textFace:
            {
                checkBoxFaceBook.setChecked(!checkBoxFaceBook.isChecked());
            }
            break;
            case R.id.textINSTA:
            {
                checkBoxInstagram.setChecked(!checkBoxInstagram.isChecked());
            }
            break;
            case R.id.textTELE:
            {
                checkBoxTelegram.setChecked(!checkBoxTelegram.isChecked());
            }
            break;
            case R.id.textVIBER:
            {
                checkBoxViber.setChecked(!checkBoxViber.isChecked());
            }
            break;
            case R.id.textVK:
            {
                checkBoxVK.setChecked(!checkBoxVK.isChecked());
            }
            break;
            case R.id.textWSAPP:
            {
                checkBoxWhatsApp.setChecked(!checkBoxWhatsApp.isChecked());
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
        }

        if (checkBoxWhatsApp.isChecked()) {
            SocialController.postOnWhatsApp(this);
        }

        if(!checkBoxTelegram.isChecked() && !checkBoxViber.isChecked() && !checkBoxVK.isChecked()
                && !checkBoxInstagram.isChecked() && !checkBoxFaceBook.isChecked()
                && !checkBoxWhatsApp.isChecked())
        {
            onBackPressed();
        }

    }
}
