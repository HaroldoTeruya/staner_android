package com.staner.tab.playlist.dialog;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.staner.MainActivity;
import com.staner.R;
import com.staner.model.MediaFileInfo;
import com.staner.tab.playlist.PlaylistTab;

import java.util.List;

public class CreatePlaylistDialogFragment extends DialogFragment
{
    //=================================================================================================
    //=========================================== ATTRIBUTES ==========================================
    //=================================================================================================

    public static final String TAG = "CreatePlaylist";
    private PlaylistTab.PLaylistInterface playlistInterface;

    //=================================================================================================
    //============================================ CONSTRUCTOR ========================================
    //=================================================================================================

    //=================================================================================================
    //============================================= METHODS ===========================================
    //=================================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.playlist_create_dialogfragment_layout,container,false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // on confirm button
        view.findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String name = ((EditText)view.findViewById(R.id.name_edittext)).getText().toString();
                Bitmap art = ((BitmapDrawable)((ImageView)view.findViewById(R.id.imageview)).getDrawable()).getBitmap();

                List<MediaFileInfo> mediaFileInfoList = getPlayListByName(name);
                if( mediaFileInfoList != null )
                {
                    Toast.makeText(getActivity(), R.string.playlist_already_exist, Toast.LENGTH_LONG).show();
                }
                else
                {
                    playlistInterface.onPlaylistCreated(name, art);

                    Toast.makeText(getActivity(), R.string.playlist_created, Toast.LENGTH_LONG).show();
                    CreatePlaylistDialogFragment.this.dismiss();
                }
            }
        });

        // on cancel button clicked
        view.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CreatePlaylistDialogFragment.this.dismiss();
            }
        });

        // on image view clicked
        view.findViewById(R.id.imageview).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((MainActivity)getActivity()).loadImageFromGallery(CreatePlaylistDialogFragment.this);
            }
        });
    }

    private List<MediaFileInfo> getPlayListByName(String name)
    {
        for(List<MediaFileInfo> mediaFileInfoList : ((MainActivity) getActivity()).getPlaylistList() )
        {
            if( mediaFileInfoList.get(0).getFilePlaylist().equalsIgnoreCase(name) )
            {
                return mediaFileInfoList;
            }
        }
        return null;
    }

    public void setPlaylistInterface(PlaylistTab.PLaylistInterface playlistInterface)
    {
        this.playlistInterface = playlistInterface;
    }

    //=================================================================================================
    //============================================== EVENTS ===========================================
    //=================================================================================================

    //=================================================================================================
    //======================================== SETTERS  & GETTERS =====================================
    //=================================================================================================

    //=================================================================================================
    //============================================== CLASS ============================================
    //=================================================================================================
}