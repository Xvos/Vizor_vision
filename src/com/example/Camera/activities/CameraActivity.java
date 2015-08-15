package com.example.Camera.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.*;
import android.widget.Button;
import android.view.ViewGroup.LayoutParams;


import com.example.Camera.R;
import com.example.Camera.control.SaveController;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.List;


public class CameraActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener,
        Camera.PictureCallback, Camera.PreviewCallback, Camera.AutoFocusCallback
{
    /**
     * ID параметра изображения
     */
    public final static String PICTURE = "PICTURE";

    private static final int SELECT_PICTURE = 1;


    private Camera _camera;
    private SurfaceHolder _surfaceHolder;
    private SurfaceView _preview;
    private Button _shotBtn, _switchButton, _uploadButton, _flashLightButton;
    private Boolean _frontCameraSelected = false;
    private String[] _flashTypes = {
    Camera.Parameters.FLASH_MODE_AUTO, Camera.Parameters.FLASH_MODE_OFF, Camera.Parameters.FLASH_MODE_ON};

    //В дальнейшем перейдем на иконки, ну а пока будут теста
    private int[] _flashButtonRes = {R.drawable.flash_auto_button, R.drawable.flash_dis_button, R.drawable.flash_button};

    private int _curFlashType = 0;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // если хотим, чтобы приложение постоянно имело портретную ориентацию
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // и без заголовка
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main);

        // наше SurfaceView имеет имя SurfaceView01
        _preview = (SurfaceView) findViewById(R.id.SurfaceView01);

        _surfaceHolder = _preview.getHolder();
        _surfaceHolder.addCallback(this);
        _surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // Кнопка съемки
        _shotBtn = (Button) findViewById(R.id.PhotoButton);
         _shotBtn.setOnClickListener(this);
        _shotBtn.setBackgroundResource(R.drawable.photo_button);


        _switchButton = (Button) findViewById(R.id.ButtonCameraSwitch);
        _switchButton.setOnClickListener(this);
        _switchButton.setBackgroundResource(R.drawable.camera_change_button);

        _uploadButton = (Button) findViewById(R.id.UploadButton);
        _uploadButton.setOnClickListener(this);
        _uploadButton.setBackgroundResource(R.drawable.open_button);

        _flashLightButton= (Button) findViewById(R.id.FlashlightButton);
        _flashLightButton.setOnClickListener(this);
        _flashLightButton.setBackgroundResource(_flashButtonRes[_curFlashType]);
        SaveController.bitmapToSave = null;
        SaveController.originalPicture = null;
    }

 /*   @Override
    protected void onResume()
    {
        super.onResume();
        _camera = Camera.open();
        Camera.Parameters params = _camera.getParameters();
        params.setFlashMode(_flashTypes[_curFlashType]);
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        params.setPictureSize(sizes.get(0).width,  sizes.get(0).height);

        _camera.setParameters(params);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (_camera != null)
        {
            _camera.setPreviewCallback(null);
            _camera.stopPreview();
            _camera.release();
            _camera = null;
        }
    }*/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        if(_camera != null)
        {
            stopCamera();
        }

        _camera = Camera.open();
        Camera.Parameters params = _camera.getParameters();
        params.setFlashMode(_flashTypes[_curFlashType]);
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        //params.setSceneMode(Camera.Parameters.SCENE_MODE_BEACH);
        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        params.setPictureSize(sizes.get(0).width,  sizes.get(0).height);
        params.setAutoExposureLock(false);
        params.setAutoWhiteBalanceLock(false);
        params.set("iso", "ISO800"); //Tried with 400, 800, 600 (values obtained from flatten())
        params.setColorEffect("none");
        params.set("scene-mode", "auto");
        params.setExposureCompensation(4);


        _camera.setParameters(params);
        setCameraRotation();
        setCameraHolder(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    private void setCameraRotation()
    {
        Camera.Parameters params = _camera.getParameters();
        Camera.CameraInfo info = new Camera.CameraInfo();
        if(_frontCameraSelected == true) {
            Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_FRONT, info);
        }
        else {
            Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        }
        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break; //Natural orientation
            case Surface.ROTATION_90: degrees = 90; break; //Landscape left
            case Surface.ROTATION_180: degrees = 180; break;//Upside down
            case Surface.ROTATION_270: degrees = 270; break;//Landscape right
        }
        int rotate = (info.orientation - degrees + 360) % 360;

        params.setRotation(rotate);

        _camera.setParameters(params);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        stopCamera();
    }

    @Override
    public void onClick(View v)
    {
        if (v == _shotBtn)
        {
            // либо делаем снимок непосредственно здесь
            // 	либо включаем обработчик автофокуса

            if(_frontCameraSelected)
            {
                _camera.takePicture(null, null, null, this);
            }
            else
            {
                _camera.autoFocus(this);
            }

        }
        else if(v == _flashLightButton)
        {
            Camera.Parameters params = _camera.getParameters();

            if(_curFlashType < _flashTypes.length - 1)
            {
                _curFlashType++;
            }
            else
            {
                _curFlashType = 0;
            }

            params.setFlashMode(_flashTypes[_curFlashType]);
            _camera.setParameters(params);
            _flashLightButton.setBackgroundResource(_flashButtonRes[_curFlashType]);
        }
        else if(v == _switchButton)
        {
            if (Camera.getNumberOfCameras() >= 2)
            {
                if(_frontCameraSelected)
                {
                    stopCamera();
                    _camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                }
                else
                {
                    stopCamera();
                    _camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                }

                Camera.Parameters params = _camera.getParameters();
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                List<Camera.Size> sizes = params.getSupportedPictureSizes();
                params.setPictureSize(sizes.get(0).width,  sizes.get(0).height);
                _camera.setParameters(params);
                _frontCameraSelected = !_frontCameraSelected;
                setCameraRotation();
                setCameraHolder(_surfaceHolder);
            }

        }
        else if(v == _uploadButton)
        {
             // Create intent to Open Image applications like Gallery, Google Photos
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            startActivityForResult(galleryIntent, SELECT_PICTURE);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri imageUri = data.getData();
                Bitmap bmp = null;
                try {
                    bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] paramArrayOfByte = convertBitmapToByteArray(bmp);
                Intent intent = new Intent(this, PreviewActivity.class);
                SaveController.originalPicture = paramArrayOfByte;
                startActivity(intent);
                finish();
            }
        }
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    @Override
    public void onPictureTaken(byte[] paramArrayOfByte, Camera paramCamera)
    {
        try {
            stopCamera();
            Intent intent = new Intent(this, PreviewActivity.class);
            SaveController.originalPicture = paramArrayOfByte;
            startActivity(intent);

            finish();
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void onBackPressed()
    {
        System.exit(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //_camera.autoFocus(this);
        return super.onTouchEvent(event);
    }

    @Override
    public void onAutoFocus(boolean paramBoolean, Camera paramCamera)
    {
        if (paramBoolean)
        {
            // если удалось сфокусироваться, делаем снимок
            paramCamera.takePicture(null, null, null, this);
        }
    }

    @Override
    public void onPreviewFrame(byte[] paramArrayOfByte, Camera paramCamera)
    {
        // здесь можно обрабатывать изображение, показываемое в preview
    }



    /////////////////////////////
    // Camera utility stuff
    /////////////////////////////

    private void setCameraHolder(SurfaceHolder holder)
    {
        try
        {
            if(holder != null)
            {
                _camera.setPreviewDisplay(holder);
                _camera.setPreviewCallback(this);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Camera.Size previewSize = _camera.getParameters().getPreviewSize();
        float aspect = (float) previewSize.width / previewSize.height;

        int previewSurfaceWidth = _preview.getWidth();
        int previewSurfaceHeight =_preview.getHeight();

        LayoutParams lp = _preview.getLayoutParams();

        // здесь корректируем размер отображаемого preview, чтобы не было искажений


        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
        {
            // портретный вид
            _camera.setDisplayOrientation(90);
            lp.height = previewSurfaceHeight;
            lp.width = (int) (previewSurfaceHeight / aspect);
        }
        else
        {
            // ландшафтный
            _camera.setDisplayOrientation(0);
            lp.width = previewSurfaceWidth;
            lp.height = (int) (previewSurfaceWidth / aspect);
        }

        _preview.setLayoutParams(lp);
        _camera.startPreview();
    }

    private void stopCamera()
    {
        if(_camera != null)
        {
            _camera.setPreviewCallback(null);
            _camera.stopPreview();
            _camera.release();
            _camera = null;
        }
    }
}