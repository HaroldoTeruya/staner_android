package com.staner.model;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Teruya on 01/06/17.
 */

public class PlaylistModel
{
    private String name;
    private int id;
    private Bitmap art;
    private List<MusicModel> musicList;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Bitmap getArt()
    {
        return art;
    }

    public void setArt(Bitmap art)
    {
        this.art = art;
    }

    public List<MusicModel> getMusicList()
    {
        return musicList;
    }

    public void setMusicList(List<MusicModel> musicList)
    {
        this.musicList = musicList;
    }
}
