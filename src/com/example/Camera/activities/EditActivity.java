package com.example.Camera.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.media.FaceDetector;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
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
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.Camera.NativeUtils;
import com.example.Camera.R;
import com.example.Camera.control.SaveController;
import com.example.Camera.editor.FilterType;
import com.example.Camera.editor.filter.CropFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 28.06.2015.
 */
public class EditActivity extends Activity implements View.OnClickListener, View.OnTouchListener, TextView.OnEditorActionListener,
        SeekBar.OnSeekBarChangeListener {

    static final int NONE = 0;
    static final int ZOOM = 1;

    private int windowwidth;
    private int windowheight;

    private Bitmap originalBitmap;
    private ImageView image;
    private Button saveButton, faceDetectButton, textButton, filtersButton, cropButton,
            clearFilerButton, imagesButton;
       private HorizontalScrollView buttonScroll, filterList, imageList;
    private EditText text;
    private Boolean _isFiltersSelected = false;
    private Boolean _isImagesSelected = false;
    private ArrayList<ImageView> images = new ArrayList<ImageView>();
    private ScaleGestureDetector mScaleDetector;
    private int dragX, dragY;

    private ArrayList<Drawable> imageOrigContents = new ArrayList<Drawable>();

    int mode = NONE;
    float oldDist = 1f;

    private Map<Integer, Integer> imageMap;
    private Map<Integer, FilterType> filterMap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit);

        windowwidth = getWindowManager().getDefaultDisplay().getWidth();
        windowheight = getWindowManager().getDefaultDisplay().getHeight();

        ////////////////////////////////////////////////////////////////////////////////


        float scaleFactor = (float)windowwidth / (float)SaveController.bitmapToSave.getWidth();

        originalBitmap = Bitmap.createScaledBitmap(SaveController.bitmapToSave, windowwidth, (int)(SaveController.bitmapToSave.getHeight() *
                scaleFactor), false);


        image = (ImageView) findViewById(R.id.editImage);
        image.setImageBitmap(originalBitmap);

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
        clearFilerButton = (Button) findViewById(R.id.noFilterButton);
        imagesButton = (Button) findViewById(R.id.imagesButton);

       /* faceDetectButton.setBackgroundResource(R.drawable.action_button);
        textButton.setBackgroundResource(R.drawable.action_button);
        cropButton.setBackgroundResource(R.drawable.action_button);
        filtersButton.setBackgroundResource(R.drawable.action_button);
        imagesButton.setBackgroundResource(R.drawable.action_button);
        saveButton.setBackgroundResource(R.drawable.save_button);*/

        //Edit Text
        text = (EditText) findViewById(R.id.editText);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) text.getLayoutParams();
        layoutParams.topMargin = windowheight * 4 / 5;
        text.setLayoutParams(layoutParams);
        text.setVisibility(View.INVISIBLE);
        text.setTextColor(Color.WHITE);
        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


        //Image Buttons
        //TODO : Такая инициализация переменных напоминает говно. ПЕРЕДЕЛАТЬ!!

        findViewById(R.id.image1Button).setOnClickListener(this);
        findViewById(R.id.image2Button).setOnClickListener(this);
        findViewById(R.id.image3Button).setOnClickListener(this);
        findViewById(R.id.image4Button).setOnClickListener(this);
        findViewById(R.id.image5Button).setOnClickListener(this);
        findViewById(R.id.image6Button).setOnClickListener(this);
        findViewById(R.id.image7Button).setOnClickListener(this);
        findViewById(R.id.image8Button).setOnClickListener(this);
        findViewById(R.id.image9Button).setOnClickListener(this);
        findViewById(R.id.image10Button).setOnClickListener(this);
        findViewById(R.id.image11Button).setOnClickListener(this);
        findViewById(R.id.image12Button).setOnClickListener(this);
        findViewById(R.id.image13Button).setOnClickListener(this);
        findViewById(R.id.image14Button).setOnClickListener(this);
        findViewById(R.id.image15Button).setOnClickListener(this);
        findViewById(R.id.image16Button).setOnClickListener(this);
        findViewById(R.id.image17Button).setOnClickListener(this);
        findViewById(R.id.image18Button).setOnClickListener(this);
        findViewById(R.id.image19Button).setOnClickListener(this);
        findViewById(R.id.image20Button).setOnClickListener(this);
        findViewById(R.id.image21Button).setOnClickListener(this);
        findViewById(R.id.image22Button).setOnClickListener(this);
        findViewById(R.id.image23Button).setOnClickListener(this);
        findViewById(R.id.image24Button).setOnClickListener(this);
        findViewById(R.id.image25Button).setOnClickListener(this);
        findViewById(R.id.image26Button).setOnClickListener(this);
        findViewById(R.id.image27Button).setOnClickListener(this);
        findViewById(R.id.image28Button).setOnClickListener(this);
        findViewById(R.id.image29Button).setOnClickListener(this);
        findViewById(R.id.image30Button).setOnClickListener(this);
        findViewById(R.id.image31Button).setOnClickListener(this);
        findViewById(R.id.image32Button).setOnClickListener(this);
        findViewById(R.id.image33Button).setOnClickListener(this);
        findViewById(R.id.image34Button).setOnClickListener(this);
        findViewById(R.id.image35Button).setOnClickListener(this);
        findViewById(R.id.image36Button).setOnClickListener(this);
        findViewById(R.id.image37Button).setOnClickListener(this);

        //Setting button listeners
        saveButton.setOnClickListener(this);
        faceDetectButton.setOnClickListener(this);
        textButton.setOnClickListener(this);
        cropButton.setOnClickListener(this);
        filtersButton.setOnClickListener(this);
        imagesButton.setOnClickListener(this);
        clearFilerButton.setOnClickListener(this);

        //Filter Buttons
        findViewById(R.id.filterButton1).setOnClickListener(this);
        findViewById(R.id.filterButton2).setOnClickListener(this);
        findViewById(R.id.filterButton3).setOnClickListener(this);
        findViewById(R.id.filterButton4).setOnClickListener(this);
        findViewById(R.id.filterButton5).setOnClickListener(this);
        findViewById(R.id.filterButton6).setOnClickListener(this);
        findViewById(R.id.filterButton7).setOnClickListener(this);

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

        filterMap = new HashMap<Integer, FilterType>();
        filterMap.put(R.id.filterButton1, FilterType.FILTER_0);
        filterMap.put(R.id.filterButton2, FilterType.FILTER_1);
        filterMap.put(R.id.filterButton3, FilterType.FILTER_2);
        filterMap.put(R.id.filterButton4, FilterType.FILTER_3);
        filterMap.put(R.id.filterButton5, FilterType.FILTER_4);
        filterMap.put(R.id.filterButton6, FilterType.FILTER_5);
        filterMap.put(R.id.filterButton7, FilterType.FILTER_6);

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.SaveButton: {
                parseBitmapAndSave();
                String message = String.valueOf(text.getText());
                Intent intent = new Intent(this, SocialActivity.class);
                intent.putExtra("TEXT", message);
                startActivity(intent);
                finish();
            }
            break;
            case R.id.FaceDetectButton: {
                int i = 0;
                //Bitmap bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.length);
                FrameLayout layout = (FrameLayout)findViewById(R.id.GalleryLayout);

               /* while(!buttons.isEmpty())
                {
                    layout.removeView(buttons.get(0));
                    buttons.remove(0);
                }*/

                FaceDetector fd = new FaceDetector(originalBitmap.getWidth(), originalBitmap.getHeight(), 5);
                FaceDetector.Face[] faces = new FaceDetector.Face[5];
                int c = fd.findFaces(originalBitmap, faces);
                for (i = 0; i < c; i++)
                {
                    Log.d("TAG", Float.toString(faces[i].eyesDistance()));
                    Log.d("TAG", Float.toString(image.getScaleX()));
                    Log.d("TAG", Float.toString(image.getScaleY()));

                    Float scaleX = (float)(image.getWidth()) / originalBitmap.getWidth();
                    Float scaleY = (float)(image.getHeight()) / originalBitmap.getHeight();

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
                CropFilter cropFilter = new CropFilter(0, (originalBitmap.getHeight() - originalBitmap.getWidth()) / 2, originalBitmap.getWidth(), originalBitmap.getWidth());
                Bitmap bmp;

                bmp = originalBitmap.copy(originalBitmap.getConfig(), true);
                cropFilter.process(originalBitmap, bmp);
                image.setImageBitmap(null);
                image.setImageDrawable(null);
                image.destroyDrawingCache();

                image.setImageBitmap(bmp);
                originalBitmap = bmp;
            }
            break;
            case R.id.AddText: {
                text.setVisibility(View.VISIBLE);
                text.setText("#Vizor8");
                text.setOnEditorActionListener(this);
            }
            break;
            case R.id.FiltersButton:
            {
                Float buttonScrollYTo, filterScrollYTo, imageScrollYTo;
                if (_isImagesSelected)
                {
                    imageScrollYTo = imageList.getY() + filterList.getHeight() * 2;
                    ObjectAnimator imageScrollAnimator = ObjectAnimator.ofFloat(imageList, "translationY",
                            imageList.getY(), imageScrollYTo);
                    imageScrollAnimator.setDuration(500);
                    imageScrollAnimator.start();
                }

                if (_isFiltersSelected)
                {
                    buttonScrollYTo = buttonScroll.getY() + filterList.getHeight();
                    filterScrollYTo = buttonScroll.getY() + filterList.getHeight();
                } else
                {
                    buttonScrollYTo = buttonScroll.getY() - filterList.getHeight();
                    filterScrollYTo = buttonScroll.getY() - filterList.getHeight();
                }

                if (!_isImagesSelected) {
                    ObjectAnimator buttonScrollAnimator = ObjectAnimator.ofFloat(buttonScroll, "translationY",
                            buttonScroll.getY(), buttonScrollYTo);
                    buttonScrollAnimator.setDuration(500);
                    buttonScrollAnimator.start();
                }

                ObjectAnimator filterListAnimator = ObjectAnimator.ofFloat(filterList, "translationY",
                        filterList.getY(), filterScrollYTo);
                filterListAnimator.setDuration(500);
                filterListAnimator.start();

                _isFiltersSelected = !_isFiltersSelected;
                _isImagesSelected = false;

            }
            break;
            case R.id.imagesButton: {
                Float buttonScrollYTo, filterScrollYTo, imageScrollYTo;

                if (_isFiltersSelected) {
                    filterScrollYTo = buttonScroll.getY() + filterList.getHeight();
                    ObjectAnimator filterListAnimator = ObjectAnimator.ofFloat(filterList, "translationY",
                            buttonScroll.getY(), filterScrollYTo);
                    filterListAnimator.setDuration(500);
                    filterListAnimator.start();
                }

                if (_isImagesSelected) {
                    buttonScrollYTo = buttonScroll.getY() + filterList.getHeight();
                    imageScrollYTo = buttonScroll.getY() + filterList.getHeight() * 2;
                } else {
                    buttonScrollYTo = buttonScroll.getY() - filterList.getHeight();
                    imageScrollYTo = buttonScroll.getY() - filterList.getHeight() * 2;
                }


                if (!_isFiltersSelected) {
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
            case R.id.noFilterButton: {
                Bitmap bmp = SaveController.bitmapToSave.copy(SaveController.bitmapToSave.getConfig(), true);
                image.setImageBitmap(bmp);
                originalBitmap = bmp;
                for(int i = 0; i < images.size(); i++)
                {
                    images.get(i).setImageBitmap(null);
                    images.get(i).setImageDrawable(imageOrigContents.get(i));
                }
            }
            break;

            case R.id.filterButton1:
            case R.id.filterButton2:
            case R.id.filterButton3:
            case R.id.filterButton4:
            case R.id.filterButton5:
            {
                Bitmap bmp = SaveController.bitmapToSave.copy(SaveController.bitmapToSave.getConfig(), true);

                NativeUtils nativeUtils = new NativeUtils();
                long stamp = System.nanoTime();
                nativeUtils.blend(bmp, filterMap.get(v.getId()));
                Log.d("VISION", "Time: " + (System.nanoTime() - stamp));

                //grayscaleFilter.process(originalBitmap, bmp);
                image.setImageBitmap(bmp);
                originalBitmap = bmp;

//                for(int i = 0; i < images.size(); i++)
//                {
//                    images.get(i).buildDrawingCache();
//                    Bitmap curBitmap = images.get(i).getDrawingCache();
//                    Bitmap grayBitmap = curBitmap.copy(curBitmap.getConfig(), true);
//                    grayscaleFilter.process(curBitmap, grayBitmap);
//                    images.get(i).setImageResource(android.R.color.transparent);
//                    images.get(i).destroyDrawingCache();
//                    images.get(i).setImageBitmap(grayBitmap);
//
//                }
            }
            break;


            ////////////////////////////
            // Images
            ///////////////////////////
            case R.id.image1Button:
            case R.id.image2Button:
            case R.id.image3Button:
            case R.id.image4Button:
            case R.id.image5Button:
            case R.id.image6Button:
            case R.id.image7Button:
            case R.id.image8Button:
            case R.id.image9Button:
            case R.id.image10Button:
            case R.id.image11Button:
            case R.id.image12Button:
            case R.id.image13Button:
            case R.id.image14Button:
            case R.id.image15Button:
            case R.id.image16Button:
            case R.id.image17Button:
            case R.id.image18Button:
            case R.id.image19Button:
            case R.id.image20Button:
            case R.id.image21Button:
            case R.id.image22Button:
            case R.id.image23Button:
            case R.id.image24Button:
            case R.id.image25Button:
            case R.id.image26Button:
            case R.id.image27Button:
            case R.id.image28Button:
            case R.id.image29Button:
            case R.id.image30Button:
            case R.id.image31Button:
            case R.id.image32Button:
            case R.id.image33Button:
            case R.id.image34Button:
            case R.id.image35Button:
            case R.id.image36Button:
            case R.id.image37Button: {
                ImageView newImage = new ImageView(this);
                newImage.setImageResource(imageMap.get(v.getId()));
                newImage.setLayoutParams(new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT));
                FrameLayout layout = (FrameLayout) findViewById(R.id.GalleryLayout);
                newImage.setOnTouchListener(this);
                newImage.setAdjustViewBounds(false);

                images.add(newImage);

                imageOrigContents.add(newImage.getDrawable());
                layout.addView(newImage);
            }
            break;
        }
    }


    /////////////////////////////////
    // Utility  Stuff
    /////////////////////////////////

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    private void parseBitmapAndSave() {
        Bitmap drawableBitmap = SaveController.bitmapToSave.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(drawableBitmap);

        int bitmapW = drawableBitmap.getWidth();
        int bitmapH = drawableBitmap.getHeight();
        int imageW = image.getWidth();
        int imageH = image.getHeight();
        float scaleFactorX = (float)bitmapW / imageW;
        float scaleFactorY = (float)bitmapH /imageH;
        FrameLayout.LayoutParams layoutParams;
        ImageView curImage;

        for (int i = 0; i < images.size(); i++) {
            curImage = images.get(i);
            layoutParams = (FrameLayout.LayoutParams) curImage.getLayoutParams();
            curImage.buildDrawingCache();
            Bitmap curBitmap = curImage.getDrawingCache();

            // "RECREATE" THE NEW BITMAP
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                    curBitmap, (int) (curBitmap.getWidth() * scaleFactorX * curImage.getScaleX()),
                    (int) (curBitmap.getHeight() * scaleFactorY * curImage.getScaleY()), false);

            canvas.drawBitmap(resizedBitmap, layoutParams.leftMargin * scaleFactorX + (curBitmap.getWidth() -  resizedBitmap.getWidth()),
                    layoutParams.topMargin * scaleFactorY +  (curBitmap.getHeight() - resizedBitmap.getHeight()), null);
        }


        //Adding Text
        if (text.getVisibility() == View.VISIBLE)
        {
            int textHeight = (int)(text.getTextSize() * (float)drawableBitmap.getHeight() / originalBitmap.getHeight());
            Paint recPaint = new Paint();
            recPaint.setColor(0x8c313131);
            canvas.drawRect(0, drawableBitmap.getHeight() * 5/6 - textHeight, drawableBitmap.getWidth(),
                    drawableBitmap.getHeight() * 5/6 + textHeight/8, recPaint);

            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(text.getTextSize() * (float)drawableBitmap.getHeight() / originalBitmap.getHeight());
            canvas.drawText(text.getText(), 0, text.getText().length(),
                    Float.valueOf((drawableBitmap.getWidth()) / 2), drawableBitmap.getHeight() * 5/6, paint);
        }


        SaveController.savePicture(drawableBitmap);
    }

    /////////////////////////////////
    // Touch Stuff
    /////////////////////////////////

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean eventConsumed = true;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) v.getLayoutParams();


        if (images.contains(v)) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    Log.d("TAG", "oldDist=" + oldDist);
                    if (oldDist > 10f) {
                        mode = ZOOM;
                        Log.d("TAG", "mode=ZOOM");
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    break;
                case MotionEvent.ACTION_DOWN: {
                    dragX = (int) event.getRawX() - layoutParams.leftMargin;
                    dragY = (int) event.getRawY() - layoutParams.topMargin;
                }
                break;
                case MotionEvent.ACTION_UP: {

                }
                break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == ZOOM)
                    {
                        float newDist = spacing(event);
                        // If you want to tweak font scaling, this is the place to go.
                        if (newDist > 10f) {
                            float scale = newDist / oldDist;

                            if (scale > 1) {
                                scale = 1.05f;
                            } else if (scale < 1) {
                                scale = 0.95f;
                            }

                            float currentScale = v.getScaleX();
                            if ((currentScale < 2 && currentScale > 0.25)
                                    || (currentScale >= 2 && scale < 1)
                                    || (currentScale <= 0.25 && scale > 1)) {
                                v.setScaleX(scale * v.getScaleX());
                                v.setScaleY(scale * v.getScaleY());//.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentSize);
                            }
                        }
                    } else {
                        int x_cord = (int) event.getRawX() - dragX;
                        int y_cord = (int) event.getRawY() - dragY;
                        float imageScaleX = v.getScaleX();
                        int vWidth = v.getWidth();
                        int vHeight = v.getHeight();

                        layoutParams.leftMargin = x_cord;
                        if (y_cord > windowheight * 0.8)
                        {
                            layoutParams.topMargin = y_cord + 400;
                        }
                        else
                        {
                            layoutParams.topMargin = y_cord;
                            layoutParams.width = vWidth;
                            layoutParams.height = vHeight;
                        }

                        v.setLayoutParams(layoutParams);

                        if (event.getRawY()  > windowheight * 0.9) {
                            FrameLayout layout = (FrameLayout) findViewById(R.id.GalleryLayout);

                            imageOrigContents.remove(images.indexOf(v));
                            images.remove(v);
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
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (!event.isShiftPressed()) {
                // the user is done typing.
                text.setSelected(false);
                return true;
            }
        }
        return false; // pass on to other listeners.
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
