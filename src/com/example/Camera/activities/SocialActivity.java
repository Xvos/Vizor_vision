package com.example.Camera.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.example.Camera.R;

/**
 * Created by nikitavalavko on 04.07.15.
 */
public class SocialActivity extends Activity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.social);
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
}
