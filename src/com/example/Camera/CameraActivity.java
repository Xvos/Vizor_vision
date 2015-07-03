package com.example.Camera;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.view.ViewGroup.LayoutParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class CameraActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener, Camera.PictureCallback, Camera.PreviewCallback, Camera.AutoFocusCallback
{
    /**
     * ID параметра изображения
     */
    public final static String PICTURE = "PICTURE";

    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private SurfaceView preview;
    private Button shotBtn, switchButton, uploadButton;
    private Boolean frontCameraSelected = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // если хотим, чтобы приложение постоянно имело портретную ориентацию
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // и без заголовка
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main);

        // наше SurfaceView имеет имя SurfaceView01
        preview = (SurfaceView) findViewById(R.id.SurfaceView01);

        surfaceHolder = preview.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // Кнопка съемки
        shotBtn = (Button) findViewById(R.id.Button01);
        shotBtn.setText("Shot");
        shotBtn.setOnClickListener(this);

        switchButton = (Button) findViewById(R.id.ButtonCameraSwitch);
        switchButton.setOnClickListener(this);
        switchButton.setText("Switch");
        switchButton.setX(200);

        uploadButton = (Button) findViewById(R.id.UploadButton);
        uploadButton.setOnClickListener(this);
        uploadButton.setText("Upload");
        uploadButton.setX(450);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        camera = Camera.open();
        Camera.Parameters params=camera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        params.setPictureSize(sizes.get(0).width,  sizes.get(0).height);
        camera.setParameters(params);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (camera != null)
        {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        setCameraHolder(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
    }

    @Override
    public void onClick(View v)
    {
        if (v == shotBtn)
        {
            // либо делаем снимок непосредственно здесь
            // 	либо включаем обработчик автофокуса

            camera.takePicture(null, null, null, this);
            //camera.autoFocus(this);
        }
        else if(v == switchButton)
        {
            if (Camera.getNumberOfCameras() >= 2)
            {
                if(frontCameraSelected)
                {
                    stopCamera();
                    camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                }
                else
                {
                    stopCamera();

                    camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                }

                Camera.Parameters params=camera.getParameters();
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                List<Camera.Size> sizes = params.getSupportedPictureSizes();
                //params.setJpegQuality(100);
                params.setPictureSize(sizes.get(0).width,  sizes.get(0).height);
                camera.setParameters(params);

                frontCameraSelected = !frontCameraSelected;
                setCameraHolder(surfaceHolder);
            }

        }
    }

    @Override
    public void onPictureTaken(byte[] paramArrayOfByte, Camera paramCamera)
    {
        // сохраняем полученные jpg в папке /sdcard/CameraExample/
        // имя файла - System.currentTimeMillis()

        try
        {
            File saveDir = new File("/sdcard/CameraExample/");

            if (!saveDir.exists())
            {
                saveDir.mkdirs();
            }

            FileOutputStream os = new FileOutputStream(String.format("/sdcard/CameraExample/%d.jpg", System.currentTimeMillis()));
            os.write(paramArrayOfByte);
            os.close();

            //Bitmap  bitmap = BitmapFactory.decodeByteArray(paramArrayOfByte, 0, paramArrayOfByte.length);
            Intent intent = new Intent(this, PreviewActivity.class);
            intent.putExtra(PICTURE, paramArrayOfByte);
            startActivity(intent);

            //TODO: или оптимизируй или переводи изображения в PreviewActivity
            //setCameraHolder(surfaceHolder);

            //Этот код распознает лица
            //Пока пусть побудет здесь, потом перекачует в другое место

           /* Bitmap  bitmap = BitmapFactory.decodeByteArray(paramArrayOfByte, 0, paramArrayOfByte.length);

            FaceDetector fd = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), 5);
            FaceDetector.Face[] faces = new FaceDetector.Face[5];
            int c = fd.findFaces(bitmap, faces);
            for (int i=0;i<c;i++) {
                Log.d("TAG", Float.toString(faces[i].eyesDistance()));
            }*/
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
        onBackPressed();
        camera.autoFocus(this);
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
            camera.setPreviewDisplay(holder);
            camera.setPreviewCallback(this);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        float aspect = (float) previewSize.width / previewSize.height;

        int previewSurfaceWidth = preview.getWidth();
        int previewSurfaceHeight = preview.getHeight();

        LayoutParams lp = preview.getLayoutParams();

        // здесь корректируем размер отображаемого preview, чтобы не было искажений
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
        {
            // портретный вид
            camera.setDisplayOrientation(90);
            lp.height = previewSurfaceHeight;
            lp.width = (int) (previewSurfaceHeight / aspect);
        }
        else
        {
            // ландшафтный
            camera.setDisplayOrientation(0);
            lp.width = previewSurfaceWidth;
            lp.height = (int) (previewSurfaceWidth / aspect);
        }

        preview.setLayoutParams(lp);
        camera.startPreview();
    }

    private void stopCamera()
    {
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }
}