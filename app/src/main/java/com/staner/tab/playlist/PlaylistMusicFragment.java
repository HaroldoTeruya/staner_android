package com.staner.tab.playlist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.staner.MainActivity;
import com.staner.R;
import com.staner.model.MediaFileInfo;
import com.staner.tab.base.BaseListener;
import com.staner.tab.playlist.dialog.EditPlaylistDialogFragment;
import com.staner.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Teruya on 26/04/17.
 */

public class PlaylistMusicFragment extends Fragment implements BaseListener
{
    //=================================================================================================
    //=========================================== ATTRIBUTES ==========================================
    //=================================================================================================

    private MainActivity mainActivity = null;
    public static final String TAG = "PlaylistMusicFragment";

    private int playlistId;
    private byte art[] = null;
    private String name = "";
    private PlaylistTab.PLaylistInterface playlistInterface = null;
    private MusicListAdapter musicListAdapter;

    //=================================================================================================
    //============================================ CONSTRUCTOR ========================================
    //=================================================================================================

    //=================================================================================================
    //============================================= METHODS ===========================================
    //=================================================================================================

    public static PlaylistMusicFragment instantiate(int id)
    {
        PlaylistMusicFragment playlistMusicFragment = new PlaylistMusicFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG,id);
        playlistMusicFragment.setArguments(bundle);
        return playlistMusicFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mainActivity = (MainActivity) getActivity();
        return inflater.inflate(R.layout.playlist_music_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // current playlist id
        playlistId = getArguments().getInt(TAG);

        List<MediaFileInfo> mediaFileInfoList = populateMusicList(playlistId);
        art = mediaFileInfoList.get(0).getFileAlbumArt();
        name = mediaFileInfoList.get(0).getFilePlaylist();

        setHeaderView();

        ListView listView = (ListView) view.findViewById(R.id.listview);
        musicListAdapter = new MusicListAdapter(mediaFileInfoList);
        listView.setAdapter(musicListAdapter);
    }

    /**
     * Return value List<MediaFileInfo>: never null. Can be empty.
     *
     * @param playlistId int
     * @return List<MediaFileInfo>: never null. Can be empty.
     */
    public List<MediaFileInfo> populateMusicList(int playlistId)
    {
        for( List<MediaFileInfo> mediaFileInfoList : mainActivity.getPlaylistList() )
        {
            if( mediaFileInfoList.get(0).getId() == playlistId )
            {
                return mediaFileInfoList;
            }
        }
        return null;
    }

    private void setHeaderView()
    {
        Bitmap image = null;
        if( art == null )
        {
            image = BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.playlist);
        }
        else image = BitmapFactory.decodeByteArray(art, 0, art.length);

        ImageView imageView = ((ImageView) getView().findViewById(R.id.imageview));
        image = Util.getThumbnailFromImage(image);
        imageView.setImageBitmap(image);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setAdjustViewBounds(true);

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

        // on header menu button clicked
        getView().findViewById(R.id.menu_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Util.createPopupMenu(playlistId, (MainActivity) getActivity(), view, R.menu.playlist_header_options_menu, PlaylistMusicFragment.this);
            }
        });
    }

    @Override
    public void onMusicClickListener(int id)
    {
        Log.d(TAG, String.valueOf(id));
    }

    @Override
    public void onPlaylistCreated()
    {
        Log.d(TAG, "empty");
    }

    @Override
    public void onPlaylistEdited(int id, Bitmap art, String name)
    {
        List<MediaFileInfo> mediaFileInfoList = populateMusicList(playlistId);
        this.art = mediaFileInfoList.get(0).getFileAlbumArt();
        this.name = name;

        setHeaderView();

        playlistInterface.onPlaylistEdited();
    }

    @Override
    public void onPlaylistRemoved(int id)
    {
        playlistInterface.onPlaylistRemoved(id);
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void onAddToPlayListRequest(int id)
    {
    }

    @Override
    public void onRemoveMusicFromPlaylistListener(int id)
    {
    }

    /**
     * Called wheh the "Edit" in the header is clicked.
     *
     * @param id
     */
    @Override
    public void onEditListRequest(int id)
    {
        EditPlaylistDialogFragment editPlaylistDialogFragment = EditPlaylistDialogFragment.instantiate(id, name, art);
        editPlaylistDialogFragment.setBaseListener(this);
        editPlaylistDialogFragment.show((getActivity()).getSupportFragmentManager(), EditPlaylistDialogFragment.TAG);
    }

    public void setPLaylistInterface(PlaylistTab.PLaylistInterface playlistInterface)
    {
        this.playlistInterface = playlistInterface;
    }

    public void filter(String text)
    {
        musicListAdapter.filter(text);
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
        private List<MediaFileInfo> filteredMusicList;

        public MusicListAdapter(List<MediaFileInfo> musicList)
        {
            this.musicList = new ArrayList<>();
            int size = musicList.size();
            for( int i = 1; i < size; i++ )
            {
                this.musicList.add(musicList.get(i));
            }

            filteredMusicList = new ArrayList<>();
            filteredMusicList.addAll(this.musicList);
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
            final String playlistName = musicList.get(position).getFilePlaylist();
            final int id = musicList.get(position).getId();
            byte raw[] = mainActivity.getRawImageById(id);
            Bitmap image = null;
            if( raw == null )
            {
                image = BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.music);
            }
            else image = BitmapFactory.decodeByteArray(raw, 0, raw.length);
            image = Util.getThumbnailFromImage(image);

            if( convertView == null )
            {
                convertView = Util.inflate(mainActivity, R.layout.item_list_layout);
            }
            convertView.setTag(name);
            ((ImageView)convertView.findViewById(R.id.imageview)).setImageBitmap(Util.getThumbnailFromImage(image));
            ((TextView)convertView.findViewById(R.id.name_textview)).setText(name);
            ((ImageView)convertView.findViewById(R.id.menu_button)).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    createMusicPlaylistPopupMenu(name, view);
                }
            });

            convertView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    mainActivity.play(playlistName, id);
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
                        Log.d(TAG,"onMenuItemClick: " + name);

                        AddToPlaylistDialogFragment addToPlaylistDialogFragment = new AddToPlaylistDialogFragment();
                        addToPlaylistDialogFragment.setPLaylistInterface(new PlaylistInterface()
                        {
                            @Override
                            public void onMusicAddeInPlaylist(String name)
                            {
                                MediaFileInfo mediaFileInfo = mainActivity.getMusicByName(name);
                                if( mediaFileInfo != null )
                                {
                                    Log.d(TAG, "update");
                                    musicList.add(mediaFileInfo);
                                    MusicListAdapter.this.notifyDataSetChanged();
                                }
                            }
                        });
                        Bundle bundle = new Bundle();
                        bundle.putString(AddToPlaylistDialogFragment.TAG, name);
                        // TODO WE HAVE TO REMOVE
                        bundle.putInt(AddToPlaylistDialogFragment.ID, PlaylistMusicFragment.this.getArguments().getInt(TAG));
                        addToPlaylistDialogFragment.setArguments(bundle);
                        addToPlaylistDialogFragment.show(getFragmentManager(), getResources().getString(R.string.add_to_playlist).toUpperCase());
                    }
                    return false;
                }
            });
            popup.show();
        }

        public void filter(String text)
        {
            musicList = Util.filter(musicList, filteredMusicList, text);
            notifyDataSetChanged();
        }
    }

    //=================================================================================================

    public static class AddToPlaylistDialogFragment extends DialogFragment
    {
        public static final String TAG = "AddToPlaylist";
        public static final String ID = "id";
        private PlaylistInterface playlistInterface;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            return inflater.inflate(R.layout.add_to_playlist_dialogfragment_layout,container,false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
        {
            super.onViewCreated(view, savedInstanceState);

            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            Log.d(TAG, String.valueOf(getArguments().getInt(ID)));

            LinearLayout playlistLinearLayout = (LinearLayout) view.findViewById(R.id.playlist_linearlayout);
            MainActivity mainActivity = ((MainActivity)getActivity());
            List<List<MediaFileInfo>> playlistList = mainActivity.getPlaylistList();

            for( List<MediaFileInfo> playlist : playlistList )
            {
                playlistLinearLayout.addView(createPlaylistItemView(playlist));
            }

            ((TextView)view.findViewById(R.id.title_textview)).setText(R.string.add_to_playlist);
            view.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    PlaylistMusicFragment.AddToPlaylistDialogFragment.this.dismiss();
                }
            });
        }

        /**
         * Create the item that represents a playlist.
         *
         * @param playlist
         * @return View
         */
        private View createPlaylistItemView(final List<MediaFileInfo> playlist)
        {
            View view = getActivity().getLayoutInflater().inflate(R.layout.playlist_item_layout, null);

            final int playlistId = playlist.get(0).getId();
            view.setId(playlistId);

            Log.d(TAG, String.valueOf(playlistId) + " " + AddToPlaylistDialogFragment.this.getArguments().getInt(ID));

            byte raw[] = playlist.get(0).getFileAlbumArt();
            Bitmap image = null;
            if( raw == null )
            {
                image = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.playlist);
            }
            else image = BitmapFactory.decodeByteArray(raw, 0, raw.length);

            ((ImageView)view.findViewById(R.id.imageview)).setImageBitmap(image);
            ((TextView)view.findViewById(R.id.textview)).setText(playlist.get(0).getFilePlaylist());
            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    String name = getArguments().getString(TAG);

                    Log.d(TAG, name + " -> " + String.valueOf(playlistId));

                    // update the playlist list in the main activity and the database
                    ((MainActivity)getActivity()).addMusicInPlaylist(playlistId, name);

                    // update the PlaylistMusicFragment
//                    Log.d(TAG, playlistId + " " + getArguments().getInt(ID));
                    if( playlistId == getArguments().getInt(ID) )
                    {
                        playlistInterface.onMusicAddeInPlaylist(name);
                    }

                    AddToPlaylistDialogFragment.this.dismiss();
                }

            });
            return view;
        }

        public void setPLaylistInterface(PlaylistInterface playlistInterface)
        {
            this.playlistInterface = playlistInterface;
        }
    }

    private interface PlaylistInterface
    {
        void onMusicAddeInPlaylist(String name);
    }
}
