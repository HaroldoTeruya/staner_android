package com.staner.tab.playlist.dialog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.staner.MainActivity;
import com.staner.R;
import com.staner.model.MediaFileInfo;
import com.staner.util.Util;

import java.util.List;

public class AddToPlaylistDialogFragment extends DialogFragment
{
    //=================================================================================================
    //=========================================== ATTRIBUTES ==========================================
    //=================================================================================================

    public static final String TAG = "AddToPlaylist";
    private List<List<MediaFileInfo>> playlistList;

    public static final String ID = "id";
    public static final String TYPE = "type";

    //=================================================================================================
    //============================================ CONSTRUCTOR ========================================
    //=================================================================================================

    //=================================================================================================
    //============================================= METHODS ===========================================
    //=================================================================================================

    // The type parameter will identify if the request id from an Album, Playlist or Music
    public static AddToPlaylistDialogFragment instantiate(int id, String type)
    {
        AddToPlaylistDialogFragment addToPlaylistDialogFragment = new AddToPlaylistDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ID, id);
        bundle.putString(TYPE, type);
        addToPlaylistDialogFragment.setArguments(bundle);
        return addToPlaylistDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        playlistList = ((MainActivity)getActivity()).getPlaylistList();
        return inflater.inflate(R.layout.add_to_playlist_dialogfragment_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        LinearLayout playlistLinearLayout = (LinearLayout) view.findViewById(R.id.playlist_linearlayout);
        for( List<MediaFileInfo> playlist : playlistList)
        {
            playlistLinearLayout.addView(createPlaylistItemView(playlist));
        }

        ((TextView)view.findViewById(R.id.title_textview)).setText(R.string.add_to_playlist);
        view.findViewById(R.id.cancel_button).setOnClickListener(onCancelButtonClickListener);
    }

    private View createPlaylistItemView(List<MediaFileInfo> playlist)
    {
        View view = getActivity().getLayoutInflater().inflate(R.layout.playlist_item_layout, null);
        view.setId(playlist.get(0).getId());

        byte raw[] = playlist.get(0).getFileAlbumArt();
        Bitmap image = null;
        if( raw == null )
        {
            image = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.playlist);
        }
        else image = BitmapFactory.decodeByteArray(raw, 0, raw.length);

        ((ImageView)view.findViewById(R.id.imageview)).setImageBitmap(Util.getThumbnailFromImage(image));
        ((TextView)view.findViewById(R.id.album_name_textview)).setText(playlist.get(0).getFilePlaylist());
        view.setOnClickListener(onPlaylistItemClickListener);
        return view;
    }

    //=================================================================================================
    //============================================== EVENTS ===========================================
    //=================================================================================================

    private View.OnClickListener onPlaylistItemClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            MainActivity mainActivity = ((MainActivity)getActivity());

            int playlistId = view.getId();

            int targetId = getArguments().getInt(ID);
            String type = getArguments().getString(TYPE);

            Log.d(TAG, String.valueOf(type) + " " + String.valueOf(targetId) + " " + String.valueOf(playlistId));
//            if( DataBase.Music.TABLE_NAME.equals(type) )
//            {
//                mainActivity.addMusicInPlaylist(targetId, playlistId);
//            }
//            else if( DataBase.Album.TABLE_NAME.equals(type) )
//            {
//                AlbumModel albumModel = mainActivity.getAlbumModelById(targetId);
//                for( MusicModel musicModel : albumModel.getMusicList() )
//                {
//                    mainActivity.addMusicInPlaylist(musicModel.getId(), playlistId);
//                }
//            }
//            else if( DataBase.Playlist.TABLE_NAME.equals(type) )
//            {
//                PlaylistModel playlistModel = mainActivity.getPlaylistById(targetId);
//                for( MusicModel musicModel : playlistModel.getMusicList() )
//                {
//                    mainActivity.addMusicInPlaylist(musicModel.getId(), playlistId);
//                }
//            }
//            AddToPlaylistDialogFragment.this.dismiss();
        }
    };

    private View.OnClickListener onCancelButtonClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            AddToPlaylistDialogFragment.this.dismiss();
        }
    };

    //=================================================================================================
    //======================================== SETTERS  & GETTERS =====================================
    //=================================================================================================

    //=================================================================================================
    //============================================== CLASS ============================================
    //=================================================================================================
}