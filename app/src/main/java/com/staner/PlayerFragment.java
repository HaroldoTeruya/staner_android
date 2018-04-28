package com.staner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.staner.model.MediaFileInfo;
import com.staner.util.Util;

import java.util.concurrent.TimeUnit;

/**
 * Created by Teruya on 16/12/17.
 */

public class PlayerFragment extends Fragment
{
    //=================================================================================================
    //=========================================== ATTRIBUTES ==========================================
    //=================================================================================================

    public static final String TAG = PlayerFragment.class.getName();
    private PlayerController playerController = null;
    private MediaPlayer mediaPlayer = null;
    private TextView progressTextView = null;
    private TextView durationTextView = null;
    private SeekBar musicProgressBar = null;
    private TextView musicNameTextView = null;
    private TextView albumnNameTextView = null;
    private MediaFileInfo currentMedia = null;
    private ImageView playPauseButton = null;
    private ImageView albumArtImageview = null;


    //=================================================================================================
    //============================================ CONSTRUCTOR ========================================
    //=================================================================================================

    //=================================================================================================
    //============================================= METHODS ===========================================
    //=================================================================================================

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.getSupportActionBar().hide();
        mainActivity.findViewById(R.id.minimized_player_content).setVisibility(View.GONE);
        return inflater.inflate(R.layout.music_player_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        final MainActivity mainActivity = (MainActivity) getActivity();
        view.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                PlayerFragment.this.getFragmentManager().popBackStackImmediate();
                mainActivity.findViewById(R.id.minimized_player_content).setVisibility(View.GONE);
                mainActivity.stop();
            }
        });
        Util.togglePlayer(view.findViewById(R.id.play_button));
        this.playPauseButton = view.findViewById(R.id.play_button);
        playPauseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                boolean play = Util.togglePlayer(view);
                if( play )
                {
                    mainActivity.resume();
                }
                else
                {
                    mainActivity.pause();
                }
            }
        });
        view.findViewById(R.id.prev_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!mediaPlayer.isPlaying()) {
                    Util.togglePlayer(playPauseButton);
                }
                mainActivity.prev();
            }
        });
        view.findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!mediaPlayer.isPlaying()) {
                    Util.togglePlayer(playPauseButton);
                }
                mainActivity.next();
            }
        });

        this.musicNameTextView = view.findViewById(R.id.name_textview);
        this.albumnNameTextView = view.findViewById(R.id.album_name_textview);
        this.albumArtImageview = view.findViewById(R.id.album_art_imageview);
        musicNameTextView.setText(currentMedia.getFileName());
        albumnNameTextView.setText(currentMedia.getFileAlbumName());
        Bitmap image = null;
        if(currentMedia.getFileAlbumArt() == null )
        {
            image = BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.album);
        }
        else image = BitmapFactory.decodeByteArray(currentMedia.getFileAlbumArt(), 0, currentMedia.getFileAlbumArt().length);
        albumArtImageview.setImageBitmap(image);
        this.progressTextView = view.findViewById(R.id.progress_textview);
        this.durationTextView = view.findViewById(R.id.duration_textview);
        this.musicProgressBar = view.findViewById(R.id.music_progressbar);
        this.musicProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    Log.d(TAG, "onProgressChanged: " + progress);
                    mainActivity.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        view.findViewById(R.id.minimized_equalizer_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null) {
                    Intent i = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                    i.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, mediaPlayer.getAudioSessionId());
                    startActivityForResult(i, 11113);
                }
            }
        });
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        ((MainActivity)getActivity()).getSupportActionBar().show();
    }



    //=================================================================================================
    //=================================== SETTERS, GETTERS & OTHERS ===================================
    //=================================================================================================

    public void setPlayerController(PlayerController playerController)
    {
        this.playerController = playerController;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public void setMediaInfo(MediaFileInfo currentMedia) {
        final MainActivity mainActivity = (MainActivity) getActivity();
        musicNameTextView.setText(currentMedia.getFileName());
        albumnNameTextView.setText(currentMedia.getFileAlbumName());
        Bitmap image = null;
        if( currentMedia.getFileAlbumArt() == null )
        {
            image = BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.album);
        }
        else image = BitmapFactory.decodeByteArray(currentMedia.getFileAlbumArt(), 0, currentMedia.getFileAlbumArt().length);
        albumArtImageview.setImageBitmap(image);
    }


    public void setCurrentMedia(MediaFileInfo currentMedia) {
        this.currentMedia = currentMedia;
    }

    public TextView getProgressTextView() {
        return progressTextView;
    }

    public TextView getDurationTextView() {
        return durationTextView;
    }

    public ProgressBar getMusicProgressBar() {
        return musicProgressBar;
    }

}
