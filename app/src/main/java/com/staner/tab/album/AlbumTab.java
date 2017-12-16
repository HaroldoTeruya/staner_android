package com.staner.tab.album;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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
import com.staner.model.AlbumModel;
import com.staner.model.MediaFileInfo;
import com.staner.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Teruya on 25/09/15.
 */
public class AlbumTab implements TabHost.TabContentFactory
{
    //=================================================================================================
    //=========================================== ATTRIBUTES ==========================================
    //=================================================================================================

    private MainActivity mainActivity;
    public final static String TAG = "AlbumTab";

    //=================================================================================================
    //============================================ CONSTRUCTOR ========================================
    //=================================================================================================

    public AlbumTab(MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
    }

    //=================================================================================================
    //============================================= METHODS ===========================================
    //=================================================================================================

    public static LinearLayout tabHeader(final FragmentActivity homeActivity)
    {
        LinearLayout tabLinearLayout = (LinearLayout) Util.inflate(homeActivity, R.layout.home_tab_header_layout);
        ((TextView)tabLinearLayout.findViewById( R.id.tab_header_textview )).setText(R.string.album);
        return tabLinearLayout;
    }

    @Override
    public View createTabContent( String tag )
    {
        View view = Util.inflate(mainActivity, R.layout.album_tab_content_layout);

        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        gridview.setAdapter(new AlbumAdapter());
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Log.d(TAG, "onItemClick: " + view.getTag());
                mainActivity.getSupportFragmentManager().beginTransaction().add(R.id.album_fragment_container, AlbumMusicFragment.instantiate(String.valueOf(view.getTag()))).addToBackStack(null).commit();
            }
        });
        return view;
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

    public class AlbumAdapter extends BaseAdapter
    {
        private List<List<MediaFileInfo>> mediaFileCollectionList = null;

        public AlbumAdapter()
        {
            mediaFileCollectionList = new ArrayList<>();
            for( MediaFileInfo mediaFileInfo : mainActivity.getMusicList() )
            {
                String name = mediaFileInfo.getFileAlbumName();
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
                        if (mediaFileInfoList.get(0).getFileAlbumName().equalsIgnoreCase(name))
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
        }

        public int getCount()
        {
            return mediaFileCollectionList.size();
        }

        public List<MediaFileInfo> getItem(int position)
        {
            return mediaFileCollectionList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            String name = mediaFileCollectionList.get(position).get(0).getFileAlbumName();

            byte raw[] = mediaFileCollectionList.get(position).get(0).getFileAlbumArt();
            Bitmap image = null;
            if( raw == null )
            {
                image = BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.album);
            }
            else image = BitmapFactory.decodeByteArray(raw, 0, raw.length);

            convertView = Util.inflate(mainActivity, R.layout.cover_layout);
            convertView.setTag(name);
            ((ImageView)convertView.findViewById(R.id.imageview)).setImageBitmap(image);
            ((TextView)convertView.findViewById(R.id.textview)).setText(name);

            return convertView;
        }
    }
}