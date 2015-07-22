package com.example.Camera.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Camera.R;
import com.example.Camera.control.SaveController;
import com.example.Camera.editor.filter.SimpleGrayscaleFilter;

import java.util.ArrayList;

/**
 * Created by user on 28.06.2015.
 */
public class EditActivity extends Activity implements View.OnClickListener, View.OnTouchListener, TextView.OnEditorActionListener
{
    int windowwidth;
    int windowheight;

    private Bitmap bitmap, originalBitmap;
    private byte[] pictureByteArray;
    private ImageView image;
    private Button saveButton, faceDetectButton, textButton, filtersButton, cropButton,
            grayScaleButton, clearFilerButton, imagesButton;
    private Button image1Button;
    private HorizontalScrollView buttonScroll, filterList, imageList;
    private EditText text;
    private Boolean _isFiltersSelected = false;
    private Boolean _isImagesSelected = false;
    private ArrayList<Button> buttons = new ArrayList<Button>();
    private ArrayList<ImageView> images = new ArrayList<ImageView>();
    private Boolean dragging = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit);

        windowwidth = getWindowManager().getDefaultDisplay().getWidth();
        windowheight = getWindowManager().getDefaultDisplay().getHeight();

        Intent intent = getIntent();
        pictureByteArray = intent.getByteArrayExtra(CameraActivity.PICTURE);
        bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.length);

        Bitmap bitmapToSave = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), null, true);

        originalBitmap  = bitmapToSave.copy(bitmapToSave.getConfig(), true);

        image = (ImageView) findViewById(R.id.editImage);
        image.setImageBitmap(bitmapToSave);

        //Lists
        buttonScroll = (HorizontalScrollView) findViewById(R.id.funcScroll);
        filterList = (HorizontalScrollView) findViewById(R.id.filterView);
        imageList = (HorizontalScrollView) findViewById(R.id.imageScrollView);

        //Buttons
        saveButton = (Button) findViewById(R.id.SaveButton);
        faceDetectButton = (Button) findViewById(R.id.FaceDetectButton);
        textButton = (Button) findViewById(R.id.AddText);
        cropButton = (Button) findViewById(R.id.CropButton);
        filtersButton = (Button) findViewById(R.id.FiltersButton);
        grayScaleButton  = (Button) findViewById(R.id.grayScaleButton);
        clearFilerButton = (Button) findViewById(R.id.noFilterButton);
        imagesButton = (Button) findViewById(R.id.imagesButton);

        //Edit Text
        text = (EditText) findViewById(R.id.editText);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) text.getLayoutParams();
        layoutParams.topMargin = windowheight * 4/5;
        text.setLayoutParams(layoutParams);
        text.setVisibility(View.INVISIBLE);
        text.setTextColor(Color.WHITE);
        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        //Image Buttons
        image1Button = (Button) findViewById(R.id.image1Button);

        //Setting button listeners
        saveButton.setOnClickListener(this);
        faceDetectButton.setOnClickListener(this);
        textButton.setOnClickListener(this);
        cropButton.setOnClickListener(this);
        filtersButton.setOnClickListener(this);
        grayScaleButton.setOnClickListener(this);
        imagesButton.setOnClickListener(this);
        clearFilerButton.setOnClickListener(this);

        image1Button.setOnClickListener(this);
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
                int i = 0;
                Bitmap bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.length);
                FrameLayout layout = (FrameLayout)findViewById(R.id.GalleryLayout);

                while(!buttons.isEmpty())
                {
                    layout.removeView(buttons.get(0));
                    buttons.remove(0);
                }

                FaceDetector fd = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), 5);
                FaceDetector.Face[] faces = new FaceDetector.Face[5];
                int c = fd.findFaces(bitmap, faces);
                for (i = 0; i < c; i++)
                {
                    Log.d("TAG", Float.toString(faces[i].eyesDistance()));
                    Log.d("TAG", Float.toString(image.getScaleX()));
                    Log.d("TAG", Float.toString(image.getScaleY()));

                    Float scaleX = Float.valueOf(image.getWidth()) / bitmap.getWidth();
                    Float scaleY = Float.valueOf(image.getHeight()) / bitmap.getHeight();

                    PointF midPoint = new PointF();
                    faces[i].getMidPoint(midPoint);
                    Button b = new Button(getApplicationContext());
                    b.setX((int) (midPoint.x - faces[i].eyesDistance()) * scaleX);
                    b.setY((int) (midPoint.y - faces[i].eyesDistance()) * scaleY);
                    b.setWidth((int) (faces[i].eyesDistance() * scaleX));
                    b.setHeight((int) (faces[i].eyesDistance() * scaleY));
                    b.setText("Face");
                    b.setAlpha((float) 0.6);

                    layout.addView(b);
                }

            }
            break;
            case R.id.CropButton:
            {
                //CropFilter
                //buttonScroll.startAnimation(controlMoveDownAnim);
                ObjectAnimator objectAnimator= ObjectAnimator.ofFloat(buttonScroll, "translationY", buttonScroll.getY(), buttonScroll.getY() - 150);
                objectAnimator.setDuration(500);
                objectAnimator.start();
            }
            break;
            case  R.id.AddText:
            {
                text.setVisibility(View.VISIBLE);
                text.setText("#Vizor8");
                text.setOnEditorActionListener(this);

            }
            break;
            case R.id.FiltersButton:
            {
                Float buttonScrollYTo, filterScrollYTo, imageScrollYTo;
                if(_isImagesSelected)
                {
                    imageScrollYTo = imageList.getY() + filterList.getHeight() * 2;
                    ObjectAnimator imageScrollAnimator= ObjectAnimator.ofFloat(imageList, "translationY",
                            imageList.getY(), imageScrollYTo);
                    imageScrollAnimator.setDuration(500);
                    imageScrollAnimator.start();
                }

                if(_isFiltersSelected)
                {
                    buttonScrollYTo = buttonScroll.getY() + filterList.getHeight();
                    filterScrollYTo = buttonScroll.getY() + filterList.getHeight();
                }
                else
                {
                    buttonScrollYTo = buttonScroll.getY() - filterList.getHeight();
                    filterScrollYTo = buttonScroll.getY() - filterList.getHeight();
                }

                if(!_isImagesSelected)
                {
                    ObjectAnimator buttonScrollAnimator = ObjectAnimator.ofFloat(buttonScroll, "translationY",
                            buttonScroll.getY(), buttonScrollYTo);
                    buttonScrollAnimator.setDuration(500);
                    buttonScrollAnimator.start();
                }

                ObjectAnimator filterListAnimator = ObjectAnimator.ofFloat(filterList, "translationY",
                        buttonScroll.getY(), filterScrollYTo);
                filterListAnimator.setDuration(500);
                filterListAnimator.start();

                _isFiltersSelected = !_isFiltersSelected;
                _isImagesSelected = false;

            }
            break;
            case R.id.imagesButton:
            {
                Float buttonScrollYTo, filterScrollYTo, imageScrollYTo;

                if(_isFiltersSelected)
                {
                    filterScrollYTo = buttonScroll.getY() + filterList.getHeight();
                    ObjectAnimator filterListAnimator= ObjectAnimator.ofFloat(filterList, "translationY",
                            buttonScroll.getY(), filterScrollYTo);
                    filterListAnimator.setDuration(500);
                    filterListAnimator.start();
                }

                if(_isImagesSelected)
                {
                    buttonScrollYTo = buttonScroll.getY() + filterList.getHeight();
                    imageScrollYTo = buttonScroll.getY() + filterList.getHeight() * 2;
                }
                else
                {
                    buttonScrollYTo = buttonScroll.getY() - filterList.getHeight();
                    imageScrollYTo = buttonScroll.getY() - filterList.getHeight() * 2;
                }


                if(!_isFiltersSelected)
                {
                    ObjectAnimator buttonScrollAnimator = ObjectAnimator.ofFloat(buttonScroll, "translationY",
                            buttonScroll.getY(), buttonScrollYTo);
                    buttonScrollAnimator.setDuration(500);
                    buttonScrollAnimator.start();
                }

                ObjectAnimator imagesListAnimator = ObjectAnimator.ofFloat(imageList, "translationY",
                        imageList.getY(), imageScrollYTo);
                imagesListAnimator.setDuration(500);
                imagesListAnimator.start();

                _isImagesSelected = !_isImagesSelected;
                _isFiltersSelected = false;

            }
            break;

            ////////////////////////////
            // Filters
            ///////////////////////////
            case  R.id.noFilterButton:
            {
                Bitmap bmp = originalBitmap.copy(originalBitmap.getConfig(), true);
                image.setImageBitmap(bmp);
            }
            break;

            case  R.id.grayScaleButton:
            {
                SimpleGrayscaleFilter grayscaleFilter = new SimpleGrayscaleFilter();
                Bitmap bmp;

                bmp  = originalBitmap.copy(originalBitmap.getConfig(), true);
                grayscaleFilter.process(originalBitmap, bmp);
                image.setImageBitmap(bmp);
            }
            break;

            ////////////////////////////
            // Images
            ///////////////////////////
            case  R.id.image1Button:
            {
               ImageView newImage = new ImageView(this);
               newImage.setImageResource(R.drawable.vizor_small);
               newImage.setLayoutParams(new FrameLayout.LayoutParams(
                       FrameLayout.LayoutParams.WRAP_CONTENT,
                       FrameLayout.LayoutParams.WRAP_CONTENT));
               FrameLayout layout = (FrameLayout)findViewById(R.id.GalleryLayout);
                newImage.setOnTouchListener(this);
               images.add(newImage);
               layout.addView(newImage);
            }
            break;
        }
    }


    /////////////////////////////////
    // Utility  Stuff
    /////////////////////////////////

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

   /////////////////////////////////
    // Touch Stuff
    /////////////////////////////////

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        boolean eventConsumed = true;
        int x = (int)event.getX();
        int y = (int)event.getY();
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) v.getLayoutParams();


        if(images.contains(v)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    int x_cord = (int) event.getRawX();
                    int y_cord = (int) event.getRawY();

                    if (x_cord > windowwidth - v.getWidth())
                    {
                        x_cord = windowwidth - v.getWidth();
                    }

                    if (y_cord > windowheight - v.getHeight())
                    {
                        y_cord = windowheight - v.getHeight();
                    }

                    layoutParams.leftMargin = x_cord;
                    layoutParams.topMargin = y_cord;

                    v.setLayoutParams(layoutParams);
                    if(y_cord > windowheight - v.getHeight()/2)
                    {
                        FrameLayout layout = (FrameLayout)findViewById(R.id.GalleryLayout);

                        layout.removeView(v);
                        v = null;
                    }
                    break;
                default:
                    break;
            }
        }
        //return true;

        return eventConsumed;

        //return false;
    }



    /////////////////////////////////
    // Text Stuff
    /////////////////////////////////

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (!event.isShiftPressed())
            {
                // the user is done typing.
                   text.setSelected(false);
                return true;
            }
        }
        return false; // pass on to other listeners.
    }


}
