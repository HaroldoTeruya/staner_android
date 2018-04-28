package com.staner.tab.other;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.staner.MainActivity;
import com.staner.R;
import com.staner.model.AlbumModel;
import com.staner.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Teruya on 16/06/17.
 */

public class OtherCoverAdapter extends BaseAdapter
{
    private MainActivity mainActivity;
    private List<AlbumModel> albumModelList = null;
    private int type;

    public OtherCoverAdapter(MainActivity mainActivity, List<AlbumModel> albumModelList, int type)
    {
        this.mainActivity = mainActivity;
        this.albumModelList = albumModelList;
        if( albumModelList == null )
        {
            albumModelList = new ArrayList<>();
        }

        this.type = type;
    }

    public int getCount()
    {
        return albumModelList.size();
    }

    public Object getItem(int position) {
        return albumModelList.get(position);
    }

    public long getItemId(int position) {
        return albumModelList.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View coverView;
        if (convertView == null)
        {
            coverView = Util.inflate(mainActivity, R.layout.cover_layout);
            coverView.setId(albumModelList.get(position).getId());
            ((ImageView)coverView.findViewById(R.id.imageview)).setImageBitmap(getCoverImage(albumModelList.get(position).getArt()));
            ((TextView)coverView.findViewById(R.id.album_name_textview)).setText(albumModelList.get(position).getName());
        }
        else
        {
            coverView = convertView;
        }
        return coverView;
    }

    private Bitmap getCoverImage(Bitmap coverImage)
    {
        if( coverImage == null )
        {
            switch ( type )
            {
                case OtherTab.ARTIST:
                    coverImage = BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.artist);
                    break;
                case OtherTab.GENRE:
                    coverImage = BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.genre);
                    break;
                case OtherTab.OTHER:
                    coverImage = BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.other);
                    break;
            }
        }
        return coverImage;
    }
}
