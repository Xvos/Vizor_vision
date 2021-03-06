package com.vizor.vision.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;

import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.view.ViewGroup.LayoutParams;


import com.vizor.vision.R;
import com.vizor.vision.control.SaveController;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
            Camera.Parameters.FLASH_MODE_AUTO,
            Camera.Parameters.FLASH_MODE_OFF,
            Camera.Parameters.FLASH_MODE_ON
    };

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
        SaveController.tempBitmap = null;
        SaveController.originalPicture = null;
    }

 /*   @Override
    protected void onResume()
    {
        super.onResume();
        _camera = vision.open();
        vision.Parameters params = _camera.getParameters();
        params.setFlashMode(_flashTypes[_curFlashType]);
        params.setFocusMode(vision.Parameters.FOCUS_MODE_AUTO);
        List<vision.Size> sizes = params.getSupportedPictureSizes();
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
    public void surfaceCreated(SurfaceHolder holder)
    {
        if(_camera != null)
        {
            stopCamera();
        }

        _camera = Camera.open();
        Camera.Parameters params = _camera.getParameters();


        // Set flash mode
        final List<String> supportedFlashModes = params.getSupportedFlashModes();
        if(supportedFlashModes != null && supportedFlashModes.contains(_flashTypes[_curFlashType])) {
            params.setFlashMode(_flashTypes[_curFlashType]);
        }

        // Set autofocus mode
        final List<String> focusModes = params.getSupportedFocusModes();
        if(focusModes != null && focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

        // Set size
        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        params.setPictureSize(sizes.get(0).width, sizes.get(0).height);

        // Exposure compensation
        if(params.isAutoExposureLockSupported()) {
            params.setAutoExposureLock(false);
        }

        // White balance
        if(params.isAutoWhiteBalanceLockSupported()) {
            params.setAutoWhiteBalanceLock(false);
        }

        // Set ISO
        String supportedIsoValues = params.get("iso-values");
        if (supportedIsoValues != null && supportedIsoValues != null) {
            params.set("iso", "auto"); //Tried with 400, 800, 600 (values obtained from flatten())
        }

        // Set color effect
        final List<String> supportedColorEffects = params.getSupportedColorEffects();
        if(supportedColorEffects != null && supportedColorEffects.contains("none")) {
            params.setColorEffect("none");
        }

        // Scene modes
        final List<String> supportedSceneModes = params.getSupportedSceneModes();
        if(supportedSceneModes != null && supportedSceneModes.contains("auto")) {
            params.set("scene-mode", "auto");
        }

        // exposure compensation
        final int exposureCompensation = 4;
        if(exposureCompensation >= params.getMinExposureCompensation() &&
            exposureCompensation < params.getMaxExposureCompensation())
        {
            params.setExposureCompensation(exposureCompensation);
        }

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

        SaveController.Rotation = rotate;

        if(info.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            Log.d("VISION", "LANDSCAPE");
        }
        else
        {
            Log.d("VISION", "PORTRAIT");
        }

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

            // Set flash mode
            final List<String> supportedFlashModes = params.getSupportedFlashModes();
            if(supportedFlashModes != null && supportedFlashModes.contains(_flashTypes[_curFlashType])) {
                params.setFlashMode(_flashTypes[_curFlashType]);
            }

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

                // Set autofocus mode
                List<String> focusModes = params.getSupportedFocusModes();
                if(focusModes != null && focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
                {
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                }

                // Set size
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
            if (requestCode == SELECT_PICTURE)
            {
                Uri imageUri = data.getData();

                try
                {
                    AssetFileDescriptor fileDescriptor = getContentResolver().openAssetFileDescriptor(imageUri, "r");

                    FileInputStream stream = new FileInputStream(fileDescriptor.getFileDescriptor());
                    SaveController.originalPicture = readDesc(fileDescriptor.getFileDescriptor());

                    stream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                // Если выбрали из галереи, не поворачиваем картинку. TODO: убрать это говно!
                SaveController.Rotation = 0;

                Intent intent = new Intent(this, PreviewActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    public byte[] readDesc(FileDescriptor file) throws IOException
    {
        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ( (read = ios.read(buffer)) != -1 ) {
                ous.write(buffer, 0, read);
            }
        } finally {
            try {
                if ( ous != null )
                    ous.close();
            } catch ( IOException e) {
            }

            try {
                if ( ios != null )
                    ios.close();
            } catch ( IOException e) {
            }
        }
        return ous.toByteArray();
    }

    @Override
    public void onPictureTaken(byte[] paramArrayOfByte, Camera paramCamera)
    {
        try {
            //vision.Parameters params = _camera.getParameters();
            //String str = params.flatten();
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
    // vision utility stuff
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