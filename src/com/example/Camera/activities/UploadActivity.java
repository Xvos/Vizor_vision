package com.example.Camera.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;

import com.example.Camera.R;
import com.example.Camera.control.GalleryAdapter;
import com.example.Camera.control.Params;
import com.example.Camera.control.SaveController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;

/**
 * Created by user on 22.06.2015.
 */
public class UploadActivity extends Activity implements AdapterView.OnItemClickListener
{
    private GridView gridView;
    //private List<File> images;
    private GalleryAdapter adapter;

    /**
     * Cursor used to access the results from querying for images on the SD card.
     */
    private Cursor cursor;
    /*
     * Column index for the Thumbnails Image IDs.
     */
    private int columnIndex;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.upload);

        gridView = (GridView) findViewById(R.id.gridView);

        // Set up an array of the Thumbnail Image ID column we want
        String[] projection = {MediaStore.Images.Thumbnails._ID};
        // Create the cursor pointing to the SDCard
        cursor = managedQuery( MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                projection, // Which columns to return
                null,       // Return all rows
                null,
                MediaStore.Images.Thumbnails._ID);
        // Get the column index of the Thumbnails Image ID
        columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);

        adapter = new GalleryAdapter(this);
        gridView.setAdapter(new ImageAdapter(this));

       /*String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();

        String targetPath = ExternalStorageDirectoryPath + "/test/";

        Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
        File targetDirector = new File(targetPath);

        File[] files = targetDirector.listFiles();

        for (File file : files){
            adapter.add(file.getAbsolutePath());
        }*/



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
        // Get the data location of the image
        String[] projection = {MediaStore.Images.Media.DATA};
        cursor = managedQuery( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, // Which columns to return
                null,       // Return all rows
                null,
                null);
        columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToPosition(position);
        // Get image filename
        String imagePath = cursor.getString(columnIndex);
        // Use this path to do further processing, i.e. full screen display

        //File targetDirector = new File(imagePath);
        Bitmap bmp  = BitmapFactory.decodeFile(imagePath);

        //Create intent
        Intent intent = new Intent(this, PreviewActivity.class);
        SaveController.originalPicture = convertBitmapToByteArray(bmp);
        startActivity(intent);
        finish();
    }



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

    /**
     * Adapter for our image files.
     */
    private class ImageAdapter extends BaseAdapter {

        private Context context;

        public ImageAdapter(Context localContext) {
            context = localContext;
        }

        public int getCount() {
            return cursor.getCount();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView picturesView;
            if (convertView == null) {
                picturesView = new ImageView(context);
                // Move cursor to current position
                cursor.moveToPosition(position);
                // Get the current value for the requested column
                int imageID = cursor.getInt(columnIndex);
                // Set the content of the image based on the provided URI
                picturesView.setImageURI(Uri.withAppendedPath(
                        MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, "" + imageID));
              //  picturesView.setImageURI(uri);
                picturesView.setScaleType(ImageView.ScaleType.FIT_XY);
                picturesView.setPadding(8, 8, 8, 8);
                picturesView.setLayoutParams(new GridView.LayoutParams(getWindowManager().getDefaultDisplay().getWidth() / 3,
                        getWindowManager().getDefaultDisplay().getWidth() / 3));
            }
            else {
                picturesView = (ImageView)convertView;
            }
            return picturesView;
        }


        public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

            Bitmap bm = null;
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(path, options);

            return bm;
        }

        public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
        {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                if (width > height) {
                    inSampleSize = Math.round((float)height / (float)reqHeight);
                } else {
                    inSampleSize = Math.round((float)width / (float)reqWidth);
                }
            }

            return inSampleSize;
        }

    }

}
