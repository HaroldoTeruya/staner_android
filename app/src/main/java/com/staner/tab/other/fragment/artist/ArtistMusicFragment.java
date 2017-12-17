package com.staner.tab.other.fragment.artist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.staner.MainActivity;
import com.staner.R;
import com.staner.model.MediaFileInfo;
import com.staner.tab.album.AlbumMusicFragment;
import com.staner.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Teruya on 26/04/17.
 */

public class ArtistMusicFragment extends Fragment
{
    //=================================================================================================
    //=========================================== ATTRIBUTES ==========================================
    //=================================================================================================

    private MainActivity mainActivity;
    public static final String TAG = "ArtistMusicFragment";

    //=================================================================================================
    //============================================ CONSTRUCTOR ========================================
    //=================================================================================================

    //=================================================================================================
    //============================================= METHODS ===========================================
    //=================================================================================================

    public static ArtistMusicFragment instantiate(String tag)
    {
        ArtistMusicFragment albumMusicFragment = new ArtistMusicFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TAG,tag);
        albumMusicFragment.setArguments(bundle);
        return albumMusicFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mainActivity = (MainActivity) getActivity();
        return inflater.inflate(R.layout.album_music_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        String name = getArguments().getString(TAG);

        List<MediaFileInfo> mediaFileInfoList = populateMusicList(name);

        setHeaderView(mediaFileInfoList.get(0).getFileAlbumArt(), name);

        ListView listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(new MusicListAdapter(mediaFileInfoList));
    }

    /*
     * Require parameter (1):
     * 1. String name.
     *
     * Return value List<MediaFileInfo>: never null. Can be empty.
     *
     * Getting the list of musics by artist name.
     */
    public List<MediaFileInfo> populateMusicList(String name)
    {
        List<MediaFileInfo> mediaFileInfoList = new ArrayList<>();
        for( MediaFileInfo mediaFileInfo : mainActivity.getMusicList() )
        {
            if( mediaFileInfo.getFileArtist().equalsIgnoreCase(name) )
            {
                mediaFileInfoList.add(mediaFileInfo);
            }
        }
        return mediaFileInfoList;
    }


    private void setHeaderView(byte raw[], final String name)
    {
        Bitmap image = null;
        if( raw == null )
        {
            image = BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.album);
        }
        else image = BitmapFactory.decodeByteArray(raw, 0, raw.length);

        ((ImageView) getView().findViewById(R.id.imageview)).setImageBitmap(image);
        ((TextView) getView().findViewById(R.id.textview)).setText(name);

        // when clicked in the play/pause button in the header
        getView().findViewById(R.id.play_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if( Util.togglePlayer(view) )
                {
                    mainActivity.play(name, -1);
                }
                else
                {
                    mainActivity.pause();
                }
            }
        });
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

    public class MusicListAdapter extends BaseAdapter
    {
        private List<MediaFileInfo> musicList;

        public MusicListAdapter(List<MediaFileInfo> musicList)
        {
            this.musicList = musicList;
        }

        public int getCount()
        {
            return musicList.size();
        }

        public MediaFileInfo getItem(int position)
        {
            return musicList.get(position);
        }

        public long getItemId(int position)
        {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            final String name = musicList.get(position).getFileName();
            final int musicId = musicList.get(position).getId();
            final String albumName = musicList.get(position).getFileAlbumName();
            byte raw[] = musicList.get(position).getFileAlbumArt();
            Bitmap image = null;
            if( raw == null )
            {
                image = BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.artist);
            }
            else image = BitmapFactory.decodeByteArray(raw, 0, raw.length);

            convertView = Util.inflate(mainActivity, R.layout.item_list_layout);
            convertView.setTag(name);
            ((ImageView)convertView.findViewById(R.id.imageview)).setImageBitmap(Util.getThumbnailFromImage(image));
            ((TextView)convertView.findViewById(R.id.name_textview)).setText(name);

            convertView.findViewById(R.id.menu_button).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    createMusicPlaylistPopupMenu(name, view);
                }
            });

            // when clicked in the music in the list
            convertView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    mainActivity.play(albumName, musicId);
                }
            });

            return convertView;
        }

        public void createMusicPlaylistPopupMenu(final String name, View view)
        {
            PopupMenu popup = new PopupMenu(mainActivity, view);
            popup.getMenuInflater().inflate(R.menu.album_music_options_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
            {
                @Override
                public boolean onMenuItemClick(MenuItem item)
                {
                    if( item.getItemId() == R.id.add )
                    {
                        Log.d(TAG,"onAddToPlayListRequest: " + name);
                        AlbumMusicFragment.AddToPlaylistDialogFragment.instantiate(name).show(getFragmentManager(), getResources().getString(R.string.add_to_playlist).toUpperCase());
                    }
                    return false;
                }
            });
            popup.show();
        }
    }
}
