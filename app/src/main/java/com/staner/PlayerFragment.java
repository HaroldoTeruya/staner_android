package com.staner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.staner.util.Util;

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
        view.findViewById(R.id.play_button).setOnClickListener(new View.OnClickListener()
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
                mainActivity.prev();
            }
        });
        view.findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mainActivity.next();
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
}
