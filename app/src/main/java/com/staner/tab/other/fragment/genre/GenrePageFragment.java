package com.staner.tab.other.fragment.genre;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.staner.MainActivity;
import com.staner.R;
import com.staner.model.MediaFileInfo;
import com.staner.tab.other.fragment.artist.ArtistMusicFragment;
import com.staner.tab.other.fragment.artist.ArtistPageFragment;
import com.staner.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Teruya on 13/06/17.
 */

public class GenrePageFragment extends Fragment
{
    //=================================================================================================
    //=========================================== ATTRIBUTES ==========================================
    //=================================================================================================

    private MainActivity mainActivity;
    public static final String TAG = "GenrePageFragment";
    private GenreCoverAdapter genreCoverAdapter = null;

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
        return inflater.inflate(R.layout.other_genre_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) getActivity();

        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        genreCoverAdapter = new GenreCoverAdapter(mainActivity);
        gridview.setAdapter(genreCoverAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Log.d(TAG, "onItemClick: " + view.getTag());
                mainActivity.getSupportFragmentManager().beginTransaction().add(R.id.other_fragment_container, GenreMusicFragment.instantiate(String.valueOf(view.getTag())), GenreMusicFragment.TAG).addToBackStack(null).commit();
            }
        });
    }

    public void filter(String text)
    {
        Fragment fragment = mainActivity.getSupportFragmentManager().findFragmentByTag(GenreMusicFragment.TAG);
        if( fragment != null ) ((GenreMusicFragment)fragment).filter(text);
        else genreCoverAdapter.filter(text);
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

    public class GenreCoverAdapter extends BaseAdapter
    {
        private List<List<MediaFileInfo>> mediaFileCollectionList = null;
        private List<List<MediaFileInfo>> filteredMediaFileCollectionList = null;

        public GenreCoverAdapter(MainActivity mainActivity)
        {
            mediaFileCollectionList = new ArrayList<>();
            for( MediaFileInfo mediaFileInfo : mainActivity.getMusicList() )
            {
                String name = mediaFileInfo.getFileGenre();
                if( mediaFileCollectionList.isEmpty() )
                {
                    List<MediaFileInfo> aux = new ArrayList<>();
                    aux.add(mediaFileInfo);
                    mediaFileCollectionList.add(aux);
                }
                else
                {
                    boolean alreadyExist = false;
                    for (List<MediaFileInfo> mediaFileInfoList : mediaFileCollectionList)
                    {
                        if (mediaFileInfoList.get(0).getFileGenre().equalsIgnoreCase(name))
                        {
                            alreadyExist = true;
                            mediaFileInfoList.add(mediaFileInfo);
                        }
                    }
                    if( !alreadyExist )
                    {
                        List<MediaFileInfo> aux = new ArrayList<>();
                        aux.add(mediaFileInfo);
                        mediaFileCollectionList.add(aux);
                    }
                }
            }

            filteredMediaFileCollectionList = new ArrayList<>();
            filteredMediaFileCollectionList.addAll(mediaFileCollectionList);
        }

        public int getCount()
        {
            return mediaFileCollectionList.size();
        }

        public List<MediaFileInfo> getItem(int position)
        {
            return mediaFileCollectionList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            String name = mediaFileCollectionList.get(position).get(0).getFileGenre();

            byte raw[] = mediaFileCollectionList.get(position).get(0).getFileAlbumArt();
            Bitmap image = null;
            if( raw == null )
            {
                image = BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.genre);
            }
            else image = BitmapFactory.decodeByteArray(raw, 0, raw.length);
            image = Util.getThumbnailFromImage(image);

            if( convertView == null )
            {
                convertView = Util.inflate(mainActivity, R.layout.cover_layout);
            }
            convertView.setTag(name);
            ((ImageView)convertView.findViewById(R.id.imageview)).setImageBitmap(image);
            ((TextView)convertView.findViewById(R.id.textview)).setText(name);
            return convertView;
        }

        public void filter(String text)
        {
            text = text.toLowerCase(Locale.getDefault());
            mediaFileCollectionList.clear();
            if( text.isEmpty() )
            {
                mediaFileCollectionList.addAll(filteredMediaFileCollectionList);
            }
            else
            {
                for (List<MediaFileInfo> mediaFileInfoList : filteredMediaFileCollectionList)
                {
                    if (mediaFileInfoList.get(0).getFileGenre().toLowerCase(Locale.getDefault()).contains(text))
                    {
                        mediaFileCollectionList.add(mediaFileInfoList);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}
