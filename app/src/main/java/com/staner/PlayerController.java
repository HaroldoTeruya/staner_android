package com.staner;

import android.util.Log;

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

        PlayerFragment playerFragment = new PlayerFragment();
        // playerFragment set
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
}
