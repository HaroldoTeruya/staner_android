package com.staner.tab.base;

import android.graphics.Bitmap;

/**
 * Created by Teruya on 24/05/17.
 */

public interface BaseListener
{
    void onMusicClickListener(int id);

    void onPlaylistCreated();

    void onPlaylistEdited(int id, Bitmap art, String name);

    void onPlaylistRemoved(int id);

    void onAddToPlayListRequest(int id);

    void onRemoveMusicFromPlaylistListener(int id);

    void onEditListRequest(int id);
}
