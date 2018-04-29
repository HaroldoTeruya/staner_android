package com.staner.music.player;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.staner.MainActivity;
import com.staner.R;
import com.staner.model.MediaFileInfo;
import com.staner.music.equalizer.MusicEqualizerFragment;
import com.staner.util.Util;

/**
 * Created by Teruya on 01/05/17.
 */

public class MinimizedPlayerFragment extends Fragment
{
    public static final String TAG = MinimizedPlayerFragment.class.getName();
    private ImageView playStopButton = null;
    private ImageView thumbnail = null;
    private TextView nameTextView = null;
    private MainActivity mainActivity = null;
    private MediaFileInfo mediaFileInfo = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.minimized_music_player_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        this.mainActivity = (MainActivity) MinimizedPlayerFragment.this.getActivity();
        this.thumbnail = (ImageView) view.findViewById(R.id.minimized_thumbnail_imageview);
        this.nameTextView = (TextView) view.findViewById(R.id.name_textview);
        this.playStopButton = (ImageView) view.findViewById(R.id.minimized_play_button);
        this.playStopButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if ( Util.togglePlayer(MinimizedPlayerFragment.this.playStopButton) ) {
                    Log.d(getTag(), "Minimized player says: Play!");
//                    mainActivity.play();
                } else {
                    Log.d(getTag(), "Minimized player says: Stop!");
                    mainActivity.stop();
                }
            }
        });
    }

    public void setMediaFileInfoAndDisplay(MediaFileInfo mediaFileInfo) {

        byte[] raw = mediaFileInfo.getFileAlbumArt();
        Bitmap image = null;
        if ( raw == null || raw.length == 0 ) {
            image = BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.album);
        } else {
            image = BitmapFactory.decodeByteArray(mediaFileInfo.getFileAlbumArt(), 0, mediaFileInfo.getFileAlbumArt().length);
        }

        this.mediaFileInfo = mediaFileInfo;
        this.thumbnail.setImageBitmap(image);
        this.nameTextView.setText(mediaFileInfo.getFileName());
    }
}
