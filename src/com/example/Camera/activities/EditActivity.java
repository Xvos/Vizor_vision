package com.example.Camera.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 28.06.2015.
 */
public class EditActivity extends Activity implements View.OnClickListener, View.OnTouchListener, TextView.OnEditorActionListener
{

    static final int NONE = 0;
    static final int ZOOM = 1;

    private int windowwidth;
    private int windowheight;

    private Bitmap bitmap, originalBitmap;
    private byte[] pictureByteArray;
    private ImageView image;
    private Button saveButton, faceDetectButton, textButton, filtersButton, cropButton,
            grayScaleButton, clearFilerButton, imagesButton;
    private Button image1Button, image2Button, image3Button, image4Button, image5Button, image6Button,
            image7Button, image8Button, image9Button, image10Button, image11Button, image12Button,
            image13Button, image14Button, image15Button, image16Button, image17Button, image18Button,
            image19Button, image20Button, image21Button, image22Button, image23Button, image24Button,
            image25Button, image26Button, image27Button, image28Button, image29Button, image30Button,
            image31Button, image32Button, image33Button, image34Button, image35Button, image36Button, image37Button;
    private HorizontalScrollView buttonScroll, filterList, imageList;
    private EditText text;
    private Boolean _isFiltersSelected = false;
    private Boolean _isImagesSelected = false;
    private ArrayList<Button> buttons = new ArrayList<Button>();
    private ArrayList<ImageView> images = new ArrayList<ImageView>();
    private ScaleGestureDetector mScaleDetector;
    private int dragX, dragY;

    int mode = NONE;
    float oldDist = 1f;

    private Map<Integer, Integer> imageMap;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit);

        windowwidth = getWindowManager().getDefaultDisplay().getWidth();
        windowheight = getWindowManager().getDefaultDisplay().getHeight();

        Intent intent = getIntent();
        pictureByteArray =  SaveController.originalPicture;
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
        //TODO : Такая инициализация переменных напоминает говно. ПЕРЕДЕЛАТЬ!!

        image1Button = (Button) findViewById(R.id.image1Button);
        image2Button = (Button) findViewById(R.id.image2Button);
        image3Button = (Button) findViewById(R.id.image3Button);
        image4Button = (Button) findViewById(R.id.image4Button);
        image5Button = (Button) findViewById(R.id.image5Button);
        image6Button = (Button) findViewById(R.id.image6Button);
        image7Button = (Button) findViewById(R.id.image7Button);
        image8Button = (Button) findViewById(R.id.image8Button);
        image9Button = (Button) findViewById(R.id.image9Button);
        image10Button = (Button) findViewById(R.id.image10Button);
        image11Button = (Button) findViewById(R.id.image11Button);
        image12Button = (Button) findViewById(R.id.image12Button);
        image13Button = (Button) findViewById(R.id.image13Button);
        image14Button = (Button) findViewById(R.id.image14Button);
        image15Button = (Button) findViewById(R.id.image15Button);
        image16Button = (Button) findViewById(R.id.image16Button);
        image17Button = (Button) findViewById(R.id.image17Button);
        image18Button = (Button) findViewById(R.id.image18Button);
        image19Button = (Button) findViewById(R.id.image19Button);
        image20Button = (Button) findViewById(R.id.image20Button);
        image21Button = (Button) findViewById(R.id.image21Button);
        image22Button = (Button) findViewById(R.id.image22Button);
        image23Button = (Button) findViewById(R.id.image23Button);
        image24Button = (Button) findViewById(R.id.image24Button);
        image25Button = (Button) findViewById(R.id.image25Button);
        image26Button = (Button) findViewById(R.id.image26Button);
        image27Button = (Button) findViewById(R.id.image27Button);
        image28Button = (Button) findViewById(R.id.image28Button);
        image29Button = (Button) findViewById(R.id.image29Button);
        image30Button = (Button) findViewById(R.id.image30Button);
        image31Button = (Button) findViewById(R.id.image31Button);
        image32Button = (Button) findViewById(R.id.image32Button);
        image33Button = (Button) findViewById(R.id.image33Button);
        image34Button = (Button) findViewById(R.id.image34Button);
        image35Button = (Button) findViewById(R.id.image35Button);
        image36Button = (Button) findViewById(R.id.image36Button);
        image37Button = (Button) findViewById(R.id.image37Button);

        //Setting button listeners
        saveButton.setOnClickListener(this);
        faceDetectButton.setOnClickListener(this);
        textButton.setOnClickListener(this);
        cropButton.setOnClickListener(this);
        filtersButton.setOnClickListener(this);
        grayScaleButton.setOnClickListener(this);
        imagesButton.setOnClickListener(this);
        clearFilerButton.setOnClickListener(this);

        //Images
        //TODO : Такая инициализация лисенеров тоже напоминает говно. ПЕРЕДЕЛАТЬ!!
        image1Button.setOnClickListener(this);
        image2Button.setOnClickListener(this);
        image3Button.setOnClickListener(this);
        image4Button.setOnClickListener(this);
        image5Button.setOnClickListener(this);
        image6Button.setOnClickListener(this);
        image7Button.setOnClickListener(this);
        image8Button.setOnClickListener(this);
        image9Button.setOnClickListener(this);
        image10Button.setOnClickListener(this);
        image11Button.setOnClickListener(this);
        image12Button.setOnClickListener(this);
        image13Button.setOnClickListener(this);
        image14Button.setOnClickListener(this);
        image15Button.setOnClickListener(this);
        image16Button.setOnClickListener(this);
        image17Button.setOnClickListener(this);
        image18Button.setOnClickListener(this);
        image19Button.setOnClickListener(this);
        image20Button.setOnClickListener(this);
        image21Button.setOnClickListener(this);
        image22Button.setOnClickListener(this);
        image23Button.setOnClickListener(this);
        image24Button.setOnClickListener(this);
        image25Button.setOnClickListener(this);
        image26Button.setOnClickListener(this);
        image27Button.setOnClickListener(this);
        image28Button.setOnClickListener(this);
        image29Button.setOnClickListener(this);
        image30Button.setOnClickListener(this);
        image31Button.setOnClickListener(this);
        image32Button.setOnClickListener(this);
        image33Button.setOnClickListener(this);
        image34Button.setOnClickListener(this);
        image35Button.setOnClickListener(this);
        image36Button.setOnClickListener(this);
        image37Button.setOnClickListener(this);

        //Map
        imageMap = new HashMap<Integer, Integer>();
        imageMap.put(R.id.image1Button, R.drawable.bandit);
        imageMap.put(R.id.image2Button, R.drawable.bear);
        imageMap.put(R.id.image3Button, R.drawable.beard);
        imageMap.put(R.id.image4Button, R.drawable.black_princess);
        imageMap.put(R.id.image5Button, R.drawable.blond_princess);
        imageMap.put(R.id.image6Button, R.drawable.chiken);
        imageMap.put(R.id.image7Button, R.drawable.cowboy);
        imageMap.put(R.id.image8Button, R.drawable.foot_1);
        imageMap.put(R.id.image9Button, R.drawable.foot_2);
        imageMap.put(R.id.image10Button, R.drawable.foot_3);
        imageMap.put(R.id.image11Button, R.drawable.foot_4);
        imageMap.put(R.id.image12Button, R.drawable.glasses);
        imageMap.put(R.id.image13Button, R.drawable.gnom);
        imageMap.put(R.id.image14Button, R.drawable.gold_like);
        imageMap.put(R.id.image15Button, R.drawable.logo_black);
        imageMap.put(R.id.image16Button, R.drawable.logo_white);
        imageMap.put(R.id.image17Button, R.drawable.logo_zf);
        imageMap.put(R.id.image18Button, R.drawable.mask_b);
        imageMap.put(R.id.image19Button, R.drawable.mask_w);
        imageMap.put(R.id.image20Button, R.drawable.logo_klone);
        imageMap.put(R.id.image21Button, R.drawable.logo_loyalty);
        imageMap.put(R.id.image22Button, R.drawable.mustache);
        imageMap.put(R.id.image23Button, R.drawable.mustache_1);
        imageMap.put(R.id.image24Button, R.drawable.mustache_2);
        imageMap.put(R.id.image25Button, R.drawable.paty_bear);
        imageMap.put(R.id.image26Button, R.drawable.pavlin);
        imageMap.put(R.id.image27Button, R.drawable.pers);
        imageMap.put(R.id.image28Button, R.drawable.red_princess);
        imageMap.put(R.id.image29Button, R.drawable.sheep);
        imageMap.put(R.id.image30Button, R.drawable.sheriff);
        imageMap.put(R.id.image31Button, R.drawable.vova_1);
        imageMap.put(R.id.image32Button, R.drawable.vova_2);
        imageMap.put(R.id.image33Button, R.drawable.vova_3);
        imageMap.put(R.id.image34Button, R.drawable.zombie);
        imageMap.put(R.id.image35Button, R.drawable.zombie_1);
        imageMap.put(R.id.image36Button, R.drawable.zombie_2);
        imageMap.put(R.id.image37Button, R.drawable.zombie_girl);

        mScaleDetector = new ScaleGestureDetector(getBaseContext(), new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
            }
            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                Log.d("TAG", "zoom ongoing, scale: " + detector.getScaleFactor());
                return false;
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.SaveButton:
            {
                parseBitmapAndSave();
                String message = String.valueOf(text.getText());
                Intent intent = new Intent(this, SocialActivity.class);
                intent.putExtra("TEXT", message);
                startActivity(intent);
                finish();
            }
            break;
            case R.id.FaceDetectButton:
            {
               /* int i = 0;
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
                }*/

            }
            break;
            case R.id.CropButton:
            {
                //CropFilter

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
                bitmap = bmp;
            }
            break;

            case  R.id.grayScaleButton:
            {
                SimpleGrayscaleFilter grayscaleFilter = new SimpleGrayscaleFilter();
                Bitmap bmp;

                bmp  = originalBitmap.copy(originalBitmap.getConfig(), true);
                grayscaleFilter.process(originalBitmap, bmp);
                image.setImageBitmap(bmp);
                bitmap = bmp;
            }
            break;

            ////////////////////////////
            // Images
            ///////////////////////////
            case  R.id.image1Button:
            case  R.id.image2Button:
            case  R.id.image3Button:
            case  R.id.image4Button:
            case  R.id.image5Button:
            case  R.id.image6Button:
            case  R.id.image7Button:
            case  R.id.image8Button:
            case  R.id.image9Button:
            case  R.id.image10Button:
            case  R.id.image11Button:
            case  R.id.image12Button:
            case  R.id.image13Button:
            case  R.id.image14Button:
            case  R.id.image15Button:
            case  R.id.image16Button:
            case  R.id.image17Button:
            case  R.id.image18Button:
            case  R.id.image19Button:
            case  R.id.image20Button:
            case  R.id.image21Button:
            case  R.id.image22Button:
            case  R.id.image23Button:
            case  R.id.image24Button:
            case  R.id.image25Button:
            case  R.id.image26Button:
            case  R.id.image27Button:
            case  R.id.image28Button:
            case  R.id.image29Button:
            case  R.id.image30Button:
            case  R.id.image31Button:
            case  R.id.image32Button:
            case  R.id.image33Button:
            case  R.id.image34Button:
            case  R.id.image35Button:
            case  R.id.image36Button:
            case  R.id.image37Button:
            {
               ImageView newImage = new ImageView(this);
               newImage.setImageResource(imageMap.get(v.getId()));
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
        Bitmap drawableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(drawableBitmap);

        int bitmapW = bitmap.getWidth();
        int bitmapH = bitmap.getHeight();
        int imageW = image.getWidth();
        int imageH = image.getHeight();
        float scaleFactorX = Float.valueOf(bitmapW) / imageW;
        float scaleFactorY = Float.valueOf(bitmapH) / imageH;
        FrameLayout.LayoutParams layoutParams;
        ImageView curImage;

        for(int i = 0; i < images.size(); i++)
        {
            curImage = images.get(i);
            layoutParams = (FrameLayout.LayoutParams) curImage.getLayoutParams();
            curImage.buildDrawingCache();
            Bitmap curBitmap = curImage.getDrawingCache();

             // "RECREATE" THE NEW BITMAP
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                    curBitmap, (int)(curBitmap.getWidth() * scaleFactorX * curImage.getScaleX()),
                    (int)(curBitmap.getHeight() * scaleFactorY *  curImage.getScaleY()), false);

            canvas.drawBitmap(resizedBitmap, layoutParams.leftMargin * scaleFactorX + (int)(curBitmap.getWidth() * (1 - curImage.getScaleX())),
                    layoutParams.topMargin * scaleFactorY + (int)(curBitmap.getWidth() * (1 - curImage.getScaleX())), null);
        }


        //Adding Text
        if(text.getVisibility() == View.VISIBLE)
        {
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(text.getTextSize() + 25);
            canvas.drawText(text.getText(), 0, text.getText().length(),
                    Float.valueOf((drawableBitmap.getWidth()) / 2), drawableBitmap.getHeight() * 4 / 5, paint);
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        drawableBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        SaveController.savePicture(byteArray);
    }

   /////////////////////////////////
    // Touch Stuff
    /////////////////////////////////

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        boolean eventConsumed = true;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) v.getLayoutParams();


        if(images.contains(v))
        {
            switch (event.getAction() & MotionEvent.ACTION_MASK)
            {
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    Log.d("TAG", "oldDist=" + oldDist);
                    if (oldDist > 10f) {
                        mode = ZOOM;
                        Log.d("TAG", "mode=ZOOM" );
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    break;
                case MotionEvent.ACTION_DOWN:
                {
                    dragX = (int)event.getRawX() - layoutParams.leftMargin;
                    dragY = (int)event.getRawY() - layoutParams.topMargin;
                }
                    break;
                case MotionEvent.ACTION_UP:
                {

                }
                break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == ZOOM)
                    {
                        float newDist = spacing(event);
                        // If you want to tweak font scaling, this is the place to go.
                        if (newDist > 10f)
                        {
                            float scale = newDist / oldDist;

                            if (scale > 1)
                            {
                                scale = 1.05f;
                            }
                            else if (scale < 1)
                            {
                                scale = 0.95f;
                            }

                            float currentScale = v.getScaleX();
                            if ((currentScale < 2 && currentScale > 0.25)
                                    ||(currentScale >= 2 && scale < 1)
                                    || (currentScale <= 0.25 && scale > 1))
                            {
                                v.setScaleX(scale * v.getScaleX());
                                v.setScaleY(scale * v.getScaleY());//.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentSize);
                            }
                        }
                    }
                    else
                    {
                        int x = (int)event.getX();
                        int y = (int)event.getY();
                        int x_cord = (int) event.getRawX() - dragX;
                        int y_cord = (int) event.getRawY() - dragY;
                        float imageScaleX = v.getScaleX();
                        //float imageScaleY = v.getScaleY();


                        if (x_cord > windowwidth - v.getWidth() * imageScaleX)
                        {
                            x_cord = (int)(windowwidth - v.getWidth() * imageScaleX);
                        }

                        /*if (x_cord < 0) {
                            x_cord = 0;
                        }*/

                        if (y_cord > windowheight - v.getHeight()) {
                            y_cord = windowheight - v.getHeight();
                        }

                       /* if (y_cord < 0) {
                            y_cord = 0;
                        }*/

                        layoutParams.leftMargin = x_cord;
                        if (event.getRawY() > windowheight * 0.8)
                        {
                            layoutParams.topMargin = y_cord + 400;
                        }
                        else
                        {
                            layoutParams.topMargin = y_cord;
                        }

                        v.setLayoutParams(layoutParams);
                        if (y_cord > windowheight * 0.85) {
                            FrameLayout layout = (FrameLayout) findViewById(R.id.GalleryLayout);

                            layout.removeView(v);
                            v = null;
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        return eventConsumed;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
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
