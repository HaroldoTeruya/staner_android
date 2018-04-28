package com.staner.tab.playlist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.staner.MainActivity;
import com.staner.R;
import com.staner.database.DataBaseController;
import com.staner.model.MediaFileInfo;
import com.staner.model.PlaylistModel;
import com.staner.tab.base.BaseListener;
import com.staner.tab.playlist.dialog.CreatePlaylistDialogFragment;
import com.staner.tab.playlist.dialog.EditPlaylistDialogFragment;
import com.staner.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Teruya on 25/09/15.
 */
public class PlaylistTab implements TabHost.TabContentFactory
{
    //=================================================================================================
    //=========================================== ATTRIBUTES ==========================================
    //=================================================================================================

    private MainActivity mainActivity = null;
    public static final String TAG = "PlaylistTab";
    private PlaylistAdapter playlistAdapter = null;
    private GridView gridview;

    //=================================================================================================
    //============================================ CONSTRUCTOR ========================================
    //=================================================================================================

    public PlaylistTab(final MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
    }

    //=================================================================================================
    //============================================= METHODS ===========================================
    //=================================================================================================

    public static LinearLayout tabHeader(final FragmentActivity homeActivity)
    {
        LinearLayout tabLinearLayout = (LinearLayout) Util.inflate(homeActivity, R.layout.home_tab_header_layout);
        ((TextView)tabLinearLayout.findViewById( R.id.tab_header_textview )).setText(R.string.playlist);
        return tabLinearLayout;
    }

    @Override
    public View createTabContent( String tag )
    {
        final View view = Util.inflate(mainActivity, R.layout.playlist_tab_content_layout);
        playlistAdapter = new PlaylistAdapter();
        gridview = (GridView) view.findViewById(R.id.gridview);

        final PLaylistInterface playlistInterface = new PLaylistInterface()
        {
            @Override
            public void onPlaylistCreated(String name, Bitmap art)
            {
                createPlaylist(name, art);
            }

            @Override
            public void onPlaylistEdited()
            {
                gridview.setAdapter(new PlaylistAdapter());
            }

            @Override
            public void onPlaylistRemoved(int id)
            {
                mainActivity.removePlaylist(id);

                playlistAdapter = new PlaylistAdapter();
                gridview.setAdapter(playlistAdapter);
            }
        };

        gridview.setAdapter(playlistAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                PlaylistMusicFragment playlistMusicFragment = PlaylistMusicFragment.instantiate(view.getId());
                playlistMusicFragment.setPLaylistInterface(playlistInterface);
                mainActivity.getSupportFragmentManager().beginTransaction().add(R.id.playlist_fragment_container, playlistMusicFragment, PlaylistMusicFragment.TAG).addToBackStack(null).commit();
            }
        });

        // on item long clicked
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, int i, long l)
            {
                Util.createPopupMenu(view.getId(), mainActivity, view, R.menu.playlist_header_options_menu, new BaseListener()
                {
                    @Override
                    public void onMusicClickListener(int id)
                    {

                    }

                    @Override
                    public void onPlaylistCreated()
                    {

                    }

                    @Override
                    public void onPlaylistEdited(int id, Bitmap art, String name)
                    {
                        PlaylistModel playlistModel = new PlaylistModel();
                        playlistModel.setId(id);
                        playlistModel.setArt(art);
                        playlistModel.setName(name);

                        // update the playlist in main activity
                        mainActivity.updatePlaylist(playlistModel);

                        // update the current gridview
                        gridview.setAdapter(new PlaylistAdapter());
                    }

                    @Override
                    public void onPlaylistRemoved(int id)
                    {
                        mainActivity.removePlaylist(id);
                        gridview.setAdapter(new PlaylistAdapter());
                    }

                    @Override
                    public void onAddToPlayListRequest(int id)
                    {

                    }

                    @Override
                    public void onRemoveMusicFromPlaylistListener(int id)
                    {

                    }

                    // called when the edit button in the menu
                    @Override
                    public void onEditListRequest(int id)
                    {
                        // get the name of the playlist selected
                        String name = ((TextView)view.findViewById(R.id.album_name_textview)).getText().toString();

                        // get the playlist by name
                        List<MediaFileInfo> mediaFileInfoList = mainActivity.getPlaylistByName(name);
                        byte art[] = mediaFileInfoList.get(0).getFileAlbumArt();

                        // open the edit dialog fragment, if edited, the onPlaylistEdited(id, art, name) is called
                        EditPlaylistDialogFragment editPlaylistDialogFragment = EditPlaylistDialogFragment.instantiate(id, name, art);
                        editPlaylistDialogFragment.setBaseListener(this);
                        editPlaylistDialogFragment.show(mainActivity.getSupportFragmentManager(), EditPlaylistDialogFragment.TAG);
                    }
                });
                return true;
            }
        });

        // on fab clicked
        view.findViewById(R.id.add_fab).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                CreatePlaylistDialogFragment createPlaylistDialogFragment = new CreatePlaylistDialogFragment();
                createPlaylistDialogFragment.setPlaylistInterface(playlistInterface);
                createPlaylistDialogFragment.show(mainActivity.getSupportFragmentManager(), CreatePlaylistDialogFragment.TAG);
            }
        });

        return view;
    }

    public void createPlaylist(String name, Bitmap art)
    {
        // create in the data base the playlist
        DataBaseController dataBaseController = new DataBaseController(mainActivity);
        int id = dataBaseController.insertPlaylist(name, art);

        // create the playlist object
        List<MediaFileInfo> mediaFileInfoList = new ArrayList<>();
        MediaFileInfo mediaFileInfo = new MediaFileInfo();
        mediaFileInfo.setFilePlaylist(name);
        mediaFileInfo.setId(id);
        mediaFileInfo.setFileAlbumArt(Util.convertBitmapToByte(art));
        mediaFileInfoList.add(mediaFileInfo);

        // update the playlist in the mainActivity
        mainActivity.addPlaylist(mediaFileInfoList);

        playlistAdapter = new PlaylistAdapter();
        gridview.setAdapter(playlistAdapter);
    }

    public void filter(String text)
    {
        Fragment fragment = mainActivity.getSupportFragmentManager().findFragmentByTag(PlaylistMusicFragment.TAG);
        if( fragment != null )
        {
            ((PlaylistMusicFragment)fragment).filter(text);
        }
        else if( playlistAdapter != null )
        {
            playlistAdapter.filter(text);
        }
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

    public class PlaylistAdapter extends BaseAdapter
    {
        private List<List<MediaFileInfo>> playlistList = null;
        private List<List<MediaFileInfo>> filteredPlaylistList = null;

        public PlaylistAdapter()
        {
            playlistList = mainActivity.getPlaylistList();

            filteredPlaylistList = new ArrayList<>();
            filteredPlaylistList.addAll(playlistList);
        }

        public int getCount() {
            return playlistList.size();
        }

        public List<MediaFileInfo> getItem(int position) {
            return playlistList.get(position);
        }

        public long getItemId(int position)
        {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            String name = playlistList.get(position).get(0).getFilePlaylist();
            int playlistId = playlistList.get(position).get(0).getId();
            byte raw[] = playlistList.get(position).get(0).getFileAlbumArt();
            Bitmap image = null;
            if( raw == null )
            {
                image = BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.playlist);
            }
            else image = BitmapFactory.decodeByteArray(raw, 0, raw.length);
            image = Util.getThumbnailFromImage(image);

            if( convertView == null )
            {
                convertView = Util.inflate(mainActivity, R.layout.cover_layout);
            }
            convertView.setId(playlistId);
            ImageView imageView = ((ImageView)convertView.findViewById(R.id.imageview));
            imageView.setImageBitmap(image);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setAdjustViewBounds(true);

            ((TextView)convertView.findViewById(R.id.album_name_textview)).setText(name);

            return convertView;
        }

        public void filter(String text)
        {
            text = text.toLowerCase(Locale.getDefault());
            playlistList.clear();
            if( text.isEmpty() )
            {
                playlistList.addAll(filteredPlaylistList);
            }
            else
            {
                for (List<MediaFileInfo> mediaFileInfoList : filteredPlaylistList)
                {
                    if (mediaFileInfoList.get(0).getFilePlaylist().toLowerCase(Locale.getDefault()).contains(text))
                    {
                        playlistList.add(mediaFileInfoList);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    public interface PLaylistInterface
    {
        void onPlaylistCreated(String name, Bitmap art);

        void onPlaylistEdited();

        void onPlaylistRemoved(int id);
    }
}
