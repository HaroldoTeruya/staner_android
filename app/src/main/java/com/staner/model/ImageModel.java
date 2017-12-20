package com.staner.model;

import android.util.Log;

/**
 * Created by Teruya on 20/12/2017.
 */

public class ImageModel
{
    private byte[] rawImage;
    private int id;

    public ImageModel(byte[] rawImage, int id)
    {
        this.rawImage = rawImage;
        this.id = id;
    }

    public byte[] getRawImage()
    {
        return rawImage;
    }

    public void setRawImage(byte[] rawImage)
    {
        this.rawImage = rawImage;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
}
