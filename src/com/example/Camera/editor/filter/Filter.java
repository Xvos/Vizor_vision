package com.example.Camera.editor.filter;

import android.graphics.Bitmap;

/**
 * Created by netherwire on 7/1/15.
 */
public interface Filter {

    void process(Bitmap source, Bitmap destination);

}
