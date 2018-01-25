package com.staner;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private MediaPlayer mediaPlayer;
    public static final String TAG = PlayerController.class.getName();

    private String playlistName = "";
    private int currentId = -1;
    private int prevId = -1;
    private int nextId = -1;
    private ArrayList<MediaFileInfo> playlist;
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

    public void play(String playlistName, int musicId) {
        Log.d(TAG, playlistName + " " + musicId + " play");
        this.stop();
        if(playlistName.compareTo(this.playlistName) != -1) { //new playlist! So update the current playlist!
//            this.playlist = new ArrayList<>(mainActivity.getPlaylistByName(playlistName));
            Log.d("uË", ""+ mainActivity.getPlaylistByName(playlistName).get(0).getId());
            this.playlistName = playlistName;
        }
        if(musicId == -1) { //if is -1 then get the first one in the currentplaylist
            this.currentId = this.playlist.get(0).getId();
        }
        else { //if not just get the song passed
            this.currentId = musicId;
        }
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        try {
            mediaPlayer.setDataSource(mainActivity.getMusicById(this.currentId).getFilePath());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        playerFragment = new PlayerFragment();

        // use with responsibility
        // playerFragment.loadMediaFileInfo(); // load in the fragment the media file info
        // playerFragment.setTimeTrack();      // set time track if has a music playing

        mainActivity.getSupportFragmentManager().beginTransaction().add(R.id.music_fragment_content, playerFragment).addToBackStack(PlayerFragment.TAG).commit();
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

    //=================================================================================================
    //============================================== CLASS ============================================
    //=================================================================================================
}
