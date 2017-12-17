package com.staner;

import android.util.Log;

import com.staner.model.MediaFileInfo;

/**
 * Created by Teruya on 16/12/17.
 */

public class PlayerController
{
    //=================================================================================================
    //=========================================== ATTRIBUTES ==========================================
    //=================================================================================================

    private MainActivity mainActivity = null;
    public static final String TAG = PlayerController.class.getName();

    private String playlistName = "";
    private int currentId = -1;
    private int prevId = -1;
    private int nextId = -1;

    private PlayerFragment playerFragment = null;

    //=================================================================================================
    //============================================ CONSTRUCTOR ========================================
    //=================================================================================================

    public PlayerController(MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
    }

    //=================================================================================================
    //============================================= METHODS ===========================================
    //=================================================================================================

    public void play(String playlistName, int musicId)
    {
        Log.d(TAG, playlistName + " " + musicId + " play");

        playerFragment = new PlayerFragment();

        // use with responsibility
        // playerFragment.loadMediaFileInfo(); // load in the fragment the media file info
        // playerFragment.setTimeTrack();      // set time track if has a music playing

        mainActivity.getSupportFragmentManager().beginTransaction().add(R.id.music_fragment_content, playerFragment).addToBackStack(PlayerFragment.TAG).commit();
    }

    public void resume()
    {
        Log.d(TAG, " resume");
    }

    public void pause()
    {
        Log.d(TAG, "pause");
    }

    public void prev()
    {
        Log.d(TAG, "prev");
    }

    public void next()
    {
        Log.d(TAG, "next");
    }

    public void stop()
    {
        Log.d(TAG, "stop");
    }

    //=================================================================================================
    //============================================== CLASS ============================================
    //=================================================================================================
}
