package com.staner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        return inflater.inflate(R.layout.player_fragment_layout, container, false);
    }

    //=================================================================================================
    //=================================== SETTERS, GETTERS & OTHERS ===================================
    //=================================================================================================

    public void setPlayerController(PlayerController playerController)
    {
        this.playerController = playerController;
    }
}
