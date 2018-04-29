package com.staner;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.staner.model.MediaFileInfo;
import com.staner.music.player.MinimizedPlayerFragment;

/**
 * Created by Teruya on 16/12/17.
 */

public class PlayerController
{
    //=================================================================================================
    //=========================================== ATTRIBUTES ==========================================
    //=================================================================================================

    private MainActivity mainActivity = null;
    private MediaPlayer mediaPlayer = null;
    private MediaFileInfo currentMediaFileInfo = null;
    private PlayerFragment playerFragment = null;
    private MinimizedPlayerFragment minimizedPlayerFragment = null;

    public static final String TAG = PlayerController.class.getName();

    private String playlistName = "";
    private int currentIndex = 0;
    private List<MediaFileInfo> playlist;
    private Thread thread = null;
    private String playerState = "stopped";

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
        for( List<MediaFileInfo> mediaFileInfoList : mainActivity.getPlaylistList() ) {
//            Log.d(TAG, mediaFileInfoList.get(0).getFilePlaylist());
            if( mediaFileInfoList.get(0).getFilePlaylist() == playlistName ) {
                for ( MediaFileInfo mediaFileInfo : mediaFileInfoList ) {
                    if ( !mediaFileInfo.getFilePath().isEmpty() ) {
                        playlist.add(mediaFileInfo);
                    }
                }
            }
        }

        if ( playlist.isEmpty() ) {
            for( MediaFileInfo mediaFileInfo : mainActivity.getMusicList() ) {
                if( !mediaFileInfo.getFileAlbumName().isEmpty() && mediaFileInfo.getFileAlbumName().equalsIgnoreCase(playlistName) ) {
                    playlist.add(mediaFileInfo);
                }
            }
        }

        if ( playlist.isEmpty() ) {
            for( MediaFileInfo mediaFileInfo : mainActivity.getMusicList() ) {
                if( !mediaFileInfo.getFileArtist().isEmpty() && mediaFileInfo.getFileArtist().equalsIgnoreCase(playlistName) ) {
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

        if ( playlist.isEmpty() ) {
            for( MediaFileInfo mediaFileInfo : mainActivity.getMusicList() ) {
                if( !mediaFileInfo.getFileType().isEmpty() && mediaFileInfo.getFileType().equalsIgnoreCase(playlistName) ) {
                    playlist.add(mediaFileInfo);
                }
            }
        }

        if ( playlist.isEmpty() ) {
            playlist = mainActivity.getMusicList();
        }
    }

    public MediaFileInfo getMusic(int id)
    {
        for( MediaFileInfo mediaFileInfo : playlist ) {
//            Log.d(TAG, mediaFileInfo.getId() + " " + id);
            if( mediaFileInfo.getId() == id ) {
                return mediaFileInfo;
            }
        }
        return null;
    }

    public void play(String playlistName, int musicId)
    {
        Log.d(TAG, playlistName + " " + musicId + " play");
        if(this.playlistName != playlistName) {
            populatePlaylist(playlistName);
        }

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

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next();
            }
        });
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer)
            {
                playerFragment.getMusicProgressBar().setMax(mediaPlayer.getDuration());
                String duration = String.format("%d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getDuration()),
                        TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getDuration()) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getDuration()))
                );
                playerFragment.getDurationTextView().setText(duration);
                playerState = "playing";
                mediaPlayer.start();
                observeMediaPlayerProgress();
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
        playerFragment.setPlayerFragmentListener(playerFragmentListener);
        playerFragment.setCurrentMedia(currentMediaFileInfo);
        mainActivity.getSupportFragmentManager().beginTransaction().add(R.id.music_fragment_content, playerFragment).addToBackStack(PlayerFragment.TAG).commit();
        playerFragment.setMediaPlayer(mediaPlayer);
    }


    public void resume()
    {
        Log.d(TAG, " resume");
        if(mediaPlayer != null && !mediaPlayer.isPlaying()) {
            playerState = "playing";
            mediaPlayer.start();
            this.observeMediaPlayerProgress();
        }
    }

    public void pause()
    {
        Log.d(TAG, "pause");
        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            playerState = "paused";
            mediaPlayer.pause();
        }

    }

    public MediaPlayer getMediaPlayer() {
        return this.mediaPlayer;
    }

    public void prev()
    {
       if(mediaPlayer.getCurrentPosition() >= 3000) {
           this.seekTo(0);
       } else {
           currentIndex--;
           if(currentIndex < 0) {
               currentIndex = 0;
           } else if(currentIndex >= 0) {
           playerState = "prev-song";
               stop();
               play(this.playlistName, playlist.get(currentIndex).getId());
           }
       }
    }

    public void next()
    {
        currentIndex++;
        if(currentIndex >= playlist.size()) {
            currentIndex = playlist.size() - 1;
        } else if(currentIndex < playlist.size()) {
            playerState = "next-song";
            stop();
            play(this.playlistName, playlist.get(currentIndex).getId());
        }
    }

    public void stop()
    {
        playerState = "stopped";

        if(this.thread != null && this.thread.isAlive()) {
            this.thread.interrupt();
            this.thread = null;
        }

        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        Log.d(TAG, "stop");
    }

    public void seekTo(int msecs) {
        if(mediaPlayer != null) {
            mediaPlayer.seekTo(msecs);
            setTimeTrack(msecs);
        }
    }

    public void observeMediaPlayerProgress() {
        if ( this.thread != null ) {
            this.thread.interrupt();
        }
        this.thread = null;
        this.thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: " + playerState);
                while (mediaPlayer != null && playerState.equalsIgnoreCase("playing")) {
                    if(mediaPlayer != null && mediaPlayer.isPlaying()) {
//                        Log.d(TAG, "run: ");
                        setTimeTrack(mediaPlayer.getCurrentPosition());
                    }
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        this.thread.start();
    }

    public void setTimeTrack(int time)
    {
        final int _time = time;
        final String _progressStr = String.format("%d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
        );
        try {
            playerFragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    playerFragment.getProgressTextView().setText(_progressStr);
                    playerFragment.getMusicProgressBar().setProgress(_time);
                }
            });
        } catch(NullPointerException e) {
            Log.d(TAG, "setTimeTrack: " + e);
        }
    }

    //=================================================================================================
    //============================================== EVENTS ===========================================
    //=================================================================================================

    private PlayerFragment.PlayerFragmentListener playerFragmentListener = new PlayerFragment.PlayerFragmentListener()
    {
        @Override
        public void onFragmentClosed(MediaFileInfo mediaFileInfo)
        {
            Log.d(TAG, "onFragmentClosed");
        }

        @Override
        public void onFragmentMinimized(MediaFileInfo mediaFileInfo)
        {
            Log.d(TAG, "onFragmentMinimized");

            mainActivity.findViewById(R.id.minimized_player_content).setVisibility(View.VISIBLE);
        }
    };

    //=================================================================================================
    //============================================== CLASS ============================================
    //=================================================================================================
}
