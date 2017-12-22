package com.staner;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Teruya on 16/12/17.
 */

public class PlayerController
{
    //=================================================================================================
    //=========================================== ATTRIBUTES ==========================================
    //=================================================================================================

    private MainActivity mainActivity = null;
    private MediaPlayer mediaPlayer;
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

    public void play(String playlistName, int musicId) {
        Log.d(TAG, playlistName + " " + musicId + " play");
        this.playlistName = playlistName;
        this.stop();
        this.currentId = musicId;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        try {
            mediaPlayer.setDataSource(mainActivity.getMusicById(musicId).getFilePath());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resume()
    {
        Log.d(TAG, " resume");
        if(mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void pause()
    {
        Log.d(TAG, "pause");
//        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
//            mediaPlayer.pause();
//        }

    }

    public MediaPlayer getMediaPlayer() {
        return this.mediaPlayer;
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
        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        Log.d(TAG, "stop");
    }
}
