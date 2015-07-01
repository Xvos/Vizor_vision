package com.example.Camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.example.Camera.control.GalleryAdapter;
import com.example.Camera.control.Params;

import java.io.ByteArrayOutputStream;
import java.io.File;
/**
 * Created by user on 22.06.2015.
 */
public class UploadActivity extends Activity implements AdapterView.OnItemClickListener
{
    private GridView gridView;
    //private List<File> images;
    private GalleryAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.upload);

        gridView = (GridView) findViewById(R.id.gridView);

        //images = getImages();

        adapter = new GalleryAdapter(this);
        gridView.setAdapter(adapter);

        String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();

        Toast.makeText(getApplicationContext(), Params.FOLDER_PATH, Toast.LENGTH_LONG).show();
        File targetDirector = new File(Params.FOLDER_PATH);

        File[] files = targetDirector.listFiles();
        for (File file : files){
            adapter.add(file.getAbsolutePath());
        }

        gridView.setOnItemClickListener(this);


    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        ImageView item = (ImageView)view;//(ImageView) parent.getItemAtPosition(position);
        Drawable itemDrawable= item.getDrawable();
        Bitmap bmp = ((BitmapDrawable)itemDrawable).getBitmap();
        //Create intent
        Intent intent = new Intent(this, PreviewActivity.class);
        intent.putExtra("PICTURE", convertBitmapToByteArray(bmp));
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
        finish();
    }
}
