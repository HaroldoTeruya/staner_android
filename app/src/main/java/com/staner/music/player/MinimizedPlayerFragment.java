package com.staner.music.player;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.staner.R;
import com.staner.music.equalizer.MusicEqualizerFragment;
import com.staner.util.Util;

/**
 * Created by Teruya on 01/05/17.
 */

public class MinimizedPlayerFragment extends Fragment
{
    public static final String TAG = "MINIMIZEDPLAYERMUSIC";

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

        view.findViewById(R.id.minimized_equalizer_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getActivity().findViewById(R.id.minimized_player_content).setVisibility(View.GONE);
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.music_fragment_content, new MusicEqualizerFragment()).addToBackStack(MusicEqualizerFragment.TAG).commit();
            }
        });
        view.findViewById(R.id.minimized_play_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Util.togglePlayer(view);
            }
        });
        view.findViewById(R.id.minimized_player_linearlayout).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getActivity().findViewById(R.id.minimized_player_content).setVisibility(View.GONE);
//                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.music_fragment_content, new MusicPlayerFragment()).addToBackStack(MusicPlayerFragment.TAG).commit();
            }
        });
    }
}
