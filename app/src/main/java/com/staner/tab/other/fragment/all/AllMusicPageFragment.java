package com.staner.tab.other.fragment.all;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.staner.MainActivity;
import com.staner.R;
import com.staner.database.DataBase;
import com.staner.model.MediaFileInfo;
import com.staner.tab.base.BaseFragment;
import com.staner.tab.base.BaseListener;
import com.staner.tab.other.fragment.artist.ArtistMusicFragment;
import com.staner.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Teruya on 26/04/17.
 */

public class AllMusicPageFragment extends Fragment
{
    //=================================================================================================
    //=========================================== ATTRIBUTES ==========================================
    //=================================================================================================

    private MainActivity mainActivity;
    public static final String TAG = "AllMusicPageFragment";

    //=================================================================================================
    //============================================ CONSTRUCTOR ========================================
    //=================================================================================================

    //=================================================================================================
    //============================================= METHODS ===========================================
    //=================================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mainActivity = (MainActivity) getActivity();
        return inflater.inflate(R.layout.other_all_music_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(new MusicListAdapter(mainActivity.getMusicList()));
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
            String name = musicList.get(position).getFileName();

            byte raw[] = musicList.get(position).getFileAlbumArt();
            Bitmap image = null;
            if( raw == null )
            {
                image = BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.music);
            }
            else image = BitmapFactory.decodeByteArray(raw, 0, raw.length);

            convertView = Util.inflate(mainActivity, R.layout.item_list_layout);
            convertView.setTag(name);
            ((ImageView)convertView.findViewById(R.id.imageview)).setImageBitmap(Util.getThumbnailFromImage(image));
            ((TextView)convertView.findViewById(R.id.name_textview)).setText(name);

            return convertView;
        }
    }
}
