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
    private List<MediaFileInfo> playlist;
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

    public void populatePlaylist(String playlistName)
    {
        playlist = new ArrayList<>();
        for( List<MediaFileInfo> mediaFileInfoList : mainActivity.getPlaylistList() )
        {
//            Log.d(TAG, mediaFileInfoList.get(0).getFilePlaylist());
            if( mediaFileInfoList.get(0).getFilePlaylist() == playlistName )
            {
                for ( MediaFileInfo mediaFileInfo : mediaFileInfoList )
                {
                    if ( !mediaFileInfo.getFilePath().isEmpty() )
                    {
                        playlist.add(mediaFileInfo);
                    }
                }
            }
        }

        if ( playlist.isEmpty() )
        {
            for( MediaFileInfo mediaFileInfo : mainActivity.getMusicList() )
            {
                if( !mediaFileInfo.getFileAlbumName().isEmpty() && mediaFileInfo.getFileAlbumName().equalsIgnoreCase(playlistName) )
                {
                    playlist.add(mediaFileInfo);
                }
            }
        }

        if ( playlist.isEmpty() )
        {
            for( MediaFileInfo mediaFileInfo : mainActivity.getMusicList() )
            {
                if( !mediaFileInfo.getFileArtist().isEmpty() && mediaFileInfo.getFileArtist().equalsIgnoreCase(playlistName) )
                {
                    playlist.add(mediaFileInfo);
                }
            }
        }

        if ( playlist.isEmpty() )
        {
            for( MediaFileInfo mediaFileInfo : mainActivity.getMusicList() )
            {
                if( !mediaFileInfo.getFileGenre().isEmpty() && mediaFileInfo.getFileGenre().equalsIgnoreCase(playlistName) )
                {
                    playlist.add(mediaFileInfo);
                }
            }
        }

        if ( playlist.isEmpty() )
        {
            for( MediaFileInfo mediaFileInfo : mainActivity.getMusicList() )
            {
                if( !mediaFileInfo.getFileType().isEmpty() && mediaFileInfo.getFileType().equalsIgnoreCase(playlistName) )
                {
                    playlist.add(mediaFileInfo);
                }
            }
        }

        if ( playlist.isEmpty() )
        {
            playlist = mainActivity.getMusicList();
        }
    }

    public MediaFileInfo getMusic(int id)
    {
        for( MediaFileInfo mediaFileInfo : playlist )
        {
//            Log.d(TAG, mediaFileInfo.getId() + " " + id);
            if( mediaFileInfo.getId() == id )
            {
                return mediaFileInfo;
            }
        }
        return null;
    }

    public void play(String playlistName, int musicId)
    {
        Log.d(TAG, playlistName + " " + musicId + " play");

        populatePlaylist(playlistName);

        MediaFileInfo currentMediaFileInfo = null;
        if( musicId != -1 )
        {
            currentMediaFileInfo = getMusic(musicId);
        }
        else
        {
            currentMediaFileInfo = playlist.get(0);
        }

        if ( currentMediaFileInfo == null )
        {
            Log.d(TAG, "error on search music");
            return;
        }

        this.stop();

        // for test
//        for( MediaFileInfo mediaFileInfo : playlist )
//        {
//            Log.d(TAG, mediaFileInfo.getFileName());
//        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer)
            {
                mediaPlayer.start();
            }
        });
        try
        {
            mediaPlayer.setDataSource(currentMediaFileInfo.getFilePath());
            mediaPlayer.prepareAsync();
        } catch (IOException e)
        {
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
