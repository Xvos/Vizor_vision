package com.example.Camera;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.view.ViewGroup.LayoutParams;
import com.example.Camera.control.Params;
import com.example.Camera.control.SaveController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;


public class CameraActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener, Camera.PictureCallback, Camera.PreviewCallback, Camera.AutoFocusCallback
{
    /**
     * ID параметра изображения
     */
    public final static String PICTURE = "PICTURE";

    private Camera _camera;
    private SurfaceHolder _surfaceHolder;
    private SurfaceView _preview;
    private Button _shotBtn, _switchButton, _uploadButton, _flashLightButton;
    private Boolean _frontCameraSelected = false;
    private String[] _flashTypes = {
    Camera.Parameters.FLASH_MODE_AUTO, Camera.Parameters.FLASH_MODE_OFF, Camera.Parameters.FLASH_MODE_ON};

    //В дальнейшем перейдем на иконки, ну а пока будут теста
    private String[] _flashButtonNames = {"Flash AUTO", "Flash OFF", "Flash ON"};

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
        _shotBtn = (Button) findViewById(R.id.Button01);
        _shotBtn.setText("Shot");
        _shotBtn.setOnClickListener(this);

        _switchButton = (Button) findViewById(R.id.ButtonCameraSwitch);
        _switchButton.setOnClickListener(this);
        _switchButton.setText("Switch");
        _uploadButton = (Button) findViewById(R.id.UploadButton);
        _uploadButton.setOnClickListener(this);
        _uploadButton.setText("Upload");

        _flashLightButton= (Button) findViewById(R.id.FlashlightButton);
        _flashLightButton.setOnClickListener(this);
        _flashLightButton.setText(_flashButtonNames[_curFlashType]);
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
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        _camera = Camera.open();
        Camera.Parameters params = _camera.getParameters();
        params.setFlashMode(_flashTypes[_curFlashType]);
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        params.setPictureSize(sizes.get(0).width,  sizes.get(0).height);

        _camera.setParameters(params);
        setCameraHolder(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
    }

    @Override
    public void onClick(View v)
    {
        if (v == _shotBtn)
        {
            // либо делаем снимок непосредственно здесь
            // 	либо включаем обработчик автофокуса

            _camera.autoFocus(this);
            _camera.takePicture(null, null, null, this);
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
            _flashLightButton.setText(_flashButtonNames[_curFlashType]);
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
                setCameraHolder(_surfaceHolder);
            }

        }
        else if(v == _uploadButton)
        {
            Intent intent = new Intent(this, UploadActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPictureTaken(byte[] paramArrayOfByte, Camera paramCamera)
    {
        try {
            Intent intent = new Intent(this, PreviewActivity.class);
            intent.putExtra(PICTURE, paramArrayOfByte);
            startActivity(intent);
            stopCamera();
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
        _camera.autoFocus(this);
        return super.onTouchEvent(event);
    }

    @Override
    public void onAutoFocus(boolean paramBoolean, Camera paramCamera)
    {
        if (paramBoolean)
        {
            // если удалось сфокусироваться, делаем снимок
            //paramCamera.takePicture(null, null, null, this);
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
        _camera.setPreviewCallback(null);
        _camera.stopPreview();
        _camera.release();
        _camera = null;
    }
}