package com.staner.tab.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.staner.R;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;

/**
 * Created by Teruya on 28/04/17.
 */

public class BaseFragment extends Fragment
{
    //=================================================================================================
    //=========================================== ATTRIBUTES ==========================================
    //=================================================================================================

    protected int id;
    protected ArrayList<Pair<Long, String>> itemArray;
    protected DragListView dragListView;
    public static final String TAG = "BaseFragment";

    //=================================================================================================
    //============================================ CONSTRUCTOR ========================================
    //=================================================================================================

    //=================================================================================================
    //============================================= METHODS ===========================================
    //=================================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.base_fragment_layout, container, false);
        dragListView = (DragListView) view.findViewById(R.id.drag_list_view);
        dragListView.getRecyclerView().setVerticalScrollBarEnabled(true);
        dragListView.setDragListListener(new DragListView.DragListListenerAdapter()
        {
            @Override
            public void onItemDragStarted(int position)
            {
                Toast.makeText(dragListView.getContext(), "Start - position: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition)
            {
                if (fromPosition != toPosition)
                {
                    Toast.makeText(dragListView.getContext(), "from: " + fromPosition + " to: " + toPosition, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

//    protected void setupAlbum(BaseListener baseListener)
//    {
//        getView().findViewById(R.id.menu_button).setVisibility(View.GONE);
//
//        MainActivity mainActivity = (MainActivity) getActivity();
//        AlbumModel albumModel = mainActivity.getAlbumModelById(id);
//        itemArray = new ArrayList<>();
//        if( albumModel != null )
//        {
//            for( MusicModel musicModel : albumModel.getMusicList() )
//            {
//                itemArray.add(new Pair<>((long) musicModel.getId(), musicModel.getName()));
//            }
//        }
//
//        getView().findViewById(R.id.play_button).setOnClickListener(onHeaderPlayClickListener);
//        ((TextView)getView().findViewById(R.id.textview)).setText(albumModel.getName());
//
//        Bitmap art = albumModel.getArt();
//        if( art != null )
//        {
//            ((ImageView) getView().findViewById(R.id.imageview)).setImageBitmap(art);
//        }
//
//        setupView(baseListener, R.menu.album_music_options_menu, false);
//    }
//
//    protected void setupPlaylist(BaseListener baseListener)
//    {
//        MainActivity mainActivity = (MainActivity) getActivity();
//        PlaylistModel playlistModel = mainActivity.getPlaylistById(id);
//        itemArray = new ArrayList<>();
//        if( playlistModel != null )
//        {
//            for( MusicModel musicModel : playlistModel.getMusicList() )
//            {
//                itemArray.add(new Pair<>((long) musicModel.getId(), musicModel.getName()));
//            }
//        }
//
//        getView().findViewById(R.id.play_button).setOnClickListener(onHeaderPlayClickListener);
//        ((ImageView)getView().findViewById(R.id.imageview)).setImageBitmap(playlistModel.getArt());
//        ((TextView)getView().findViewById(R.id.textview)).setText(playlistModel.getName());
//
//        setupView(baseListener, R.menu.playlist_music_options_menu, true);
//    }
//
//    protected void setupOther(BaseListener baseListener)
//    {
//        MainActivity mainActivity = (MainActivity) getActivity();
//        List<MusicModel> musicModelList = mainActivity.getMusicModelList();
//        itemArray = new ArrayList<>();
//        if( musicModelList != null )
//        {
//            for( MusicModel musicModel : musicModelList )
//            {
//                itemArray.add(new Pair<>((long) musicModel.getId(), musicModel.getName()));
//            }
//        }
//
//        getView().findViewById(R.id.base_header_layout).setVisibility(View.GONE);
//        setupView(baseListener, R.menu.album_music_options_menu, true);
//    }
//
//    protected void setupGenre(BaseListener baseListener)
//    {
//        getView().findViewById(R.id.menu_button).setVisibility(View.GONE);
//
//        MainActivity mainActivity = (MainActivity) getActivity();
//        AlbumModel genreModel = mainActivity.getGenreModelById(id);
//        itemArray = new ArrayList<>();
//        if( genreModel != null )
//        {
//            for( MusicModel musicModel : genreModel.getMusicList() )
//            {
//                itemArray.add(new Pair<>((long) musicModel.getId(), musicModel.getName()));
//            }
//        }
//
//        Bitmap image = genreModel.getArt();
//        if( image == null )
//        {
//            image =  BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.genre);
//        }
//        getView().findViewById(R.id.play_button).setOnClickListener(onHeaderPlayClickListener);
//        ((ImageView)getView().findViewById(R.id.imageview)).setImageBitmap(image);
//        ((TextView)getView().findViewById(R.id.textview)).setText(genreModel.getName());
//
//        setupView(baseListener, R.menu.album_music_options_menu, false);
//    }
//
//    protected void setupArtist(BaseListener baseListener)
//    {
//        getView().findViewById(R.id.menu_button).setVisibility(View.GONE);
//
//        MainActivity mainActivity = (MainActivity) getActivity();
//        AlbumModel artistModel = mainActivity.getArtistModelById(id);
//        itemArray = new ArrayList<>();
//        if( artistModel != null )
//        {
//            for( MusicModel musicModel : artistModel.getMusicList() )
//            {
//                itemArray.add(new Pair<>((long) musicModel.getId(), musicModel.getName()));
//            }
//        }
//
//        getView().findViewById(R.id.play_button).setOnClickListener(onHeaderPlayClickListener);
//        Bitmap image = artistModel.getArt();
//        if( image == null )
//        {
//            image =  BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.artist);
//        }
//        ((ImageView)getView().findViewById(R.id.imageview)).setImageBitmap(image);
//        ((TextView)getView().findViewById(R.id.textview)).setText(artistModel.getName());
//
//        setupView(baseListener, R.menu.album_music_options_menu, false);
//    }
//
//    protected void setupView(final BaseListener baseListener, final int menuLayoutId, boolean isAlbumEnable)
//    {
//        MainActivity mainActivity = (MainActivity) getActivity();
//        dragListView.setLayoutManager(new LinearLayoutManager(getContext()));
//        MusicListAdapter listAdapter = new MusicListAdapter(
//                itemArray,
//                R.layout.drag_listitem_layout,
//                R.id.music_menu,
//                menuLayoutId,
//                baseListener,
//                mainActivity,
//                isAlbumEnable);
//
//        dragListView.setAdapter(listAdapter, true);
//        dragListView.setCanDragHorizontally(false);
//        dragListView.setCustomDragItem(new BaseFragment.DragItemView(getContext(), R.layout.drag_listitem_layout));
//    }
//
//    /*
//     * when a music in the music list is clicked, the Music Player Fragment is called
//     */
//    @Override
//    public void onMusicClickListener(int id)
//    {
//        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.music_fragment_content, MusicPlayerFragment.instantiate(id)).addToBackStack(MusicPlayerFragment.TAG).commit();
//    }
//
//    @Override
//    public void onPlaylistCreated()
//    {
//
//    }
//
//    @Override
//    public void onPlaylistEdited()
//    {
//
//    }
//
//    @Override
//    public void onPlaylistRemoved(int id)
//    {
//
//    }
//
//    @Override
//    public void onAddToPlayListRequest(int id)
//    {
//        Log.d(TAG,"onAddToPlayListRequest: " + id);
//        AddToPlaylistDialogFragment.instantiate(id, DataBase.Music.TABLE_NAME).show(getFragmentManager(), getResources().getString(R.string.add_to_playlist).toUpperCase());
//    }
//
//    @Override
//    public void onRemoveMusicFromPlaylistListener(int musicId)
//    {
//
//    }
//
//    @Override
//    public void onEditListRequest(int id)
//    {
//    }

}
