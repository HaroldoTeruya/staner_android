package com.staner.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.staner.MainActivity;
import com.staner.R;
import com.staner.tab.album.AlbumMusicFragment;
import com.staner.tab.base.BaseListener;
import com.staner.util.Util;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Teruya on 27/04/17.
 */

public class MusicListAdapter extends BaseAdapter
{
    public static final String TAG = MusicListAdapter.class.getName();

    private List<MediaFileInfo> musicList;
    private List<MediaFileInfo> filteredMusicList;
    private MainActivity mainActivity;

    public MusicListAdapter(MainActivity mainActivity, List<MediaFileInfo> musicList)
    {
        this.mainActivity = mainActivity;
        this.musicList = musicList;

        filteredMusicList = new ArrayList<>();
        this.filteredMusicList.addAll(musicList);
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
                mainActivity.play("", id);
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

                    String title = mainActivity.getResources().getString(R.string.add_to_playlist).toUpperCase();
                    AlbumMusicFragment.AddToPlaylistDialogFragment.instantiate(name).show(mainActivity.getSupportFragmentManager(), title);
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