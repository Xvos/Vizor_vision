package com.example.Camera.editor;

import android.graphics.Color;

import com.example.Camera.editor.filter.Filter;

/**
 * Created by netherwire on 8/16/15.
 */
public class FilterPrefab
{
    private BlendFunction blendFunction;
    private int color;
    private float opacity;

    private FilterPrefab(BlendFunction blendFunction, int color, float opacity)
    {
        this.blendFunction = blendFunction;
        this.color = color;
        this.opacity = opacity;
    }

    public BlendFunction getBlendFunction()
    {
        return blendFunction;
    }

    public int getColor()
    {
        return color;
    }

    public float getOpacity()
    {
        return opacity;
    }

    //SoftLight, 97 67 20, 70%
    public static final FilterPrefab Filter0 = new FilterPrefab(BlendFunction.Soft_Light, Color.rgb(97, 67, 20), .7f);

    //ColorDodge, 97 89 20, 50%
    public static final FilterPrefab Filter1 = new FilterPrefab(BlendFunction.Color_Dodge, Color.rgb(97, 89, 20), .5f);

    //Overlay, 195 153 128, 50%
    public static final FilterPrefab Filter2 = new FilterPrefab(BlendFunction.Overlay, Color.rgb(195, 153, 128), .5f);

    //HardLight, 148 124 172, 50%
    public static final FilterPrefab Filter3 = new FilterPrefab(BlendFunction.Hard_Light, Color.rgb(148, 124, 172), .5f);

    //Lightin 10%, 255 243 214
    public static final FilterPrefab Filter4 = new FilterPrefab(BlendFunction.Lighten, Color.rgb(255, 243, 214), .1f);

    //Screen 20%, 219 64 154
    public static final FilterPrefab Filter5 = new FilterPrefab(BlendFunction.Screen, Color.rgb(219, 64, 154), .2f);

    //Exclusion 30%, 219 64 154
    public static final FilterPrefab Filter6 = new FilterPrefab(BlendFunction.Exclusion, Color.rgb(219, 64, 154), .3f);

    //Color burn 30%, 249 201 196
    public static final FilterPrefab Filter7 = new FilterPrefab(BlendFunction.Color_Burn, Color.rgb(249, 201, 196), .3f);

    //Linear light 50%, 209 196 249
    public static final FilterPrefab Filter8 = new FilterPrefab(BlendFunction.Linear_Light, Color.rgb(209, 196, 249), .5f);

    //Vivid light 50%, 209 196 249
    public static final FilterPrefab Filter9 = new FilterPrefab(BlendFunction.Vivid_Light, Color.rgb(209, 196, 249), .5f);

    //Soft light 20%, 0 255 54 by Netherwire
    public static final FilterPrefab Filter10 = new FilterPrefab(BlendFunction.Soft_Light, Color.rgb(0, 255, 54), .2f);

    //Soft light 20%, 0 54 255 by Netherwire
    public static final FilterPrefab Filter11 = new FilterPrefab(BlendFunction.Soft_Light, Color.rgb(0, 54, 255), .2f);

    //Darken, 113 112 105 by Netherwire
    public static final FilterPrefab Filter12 = new FilterPrefab(BlendFunction.Darken, Color.rgb(113, 112, 105), .5f);

    private static final FilterPrefab filterPrefabs[] = new FilterPrefab[] {
            Filter0,
            Filter1,
            Filter2,
            Filter3,
            Filter4,
            Filter5,
            Filter6,
            Filter7,
            Filter8,
            Filter9,
            Filter10,
            Filter11,
            Filter12
    };

    public static final FilterPrefab getFilterPrefab(int index)
    {
        return filterPrefabs[index];
    }
}
