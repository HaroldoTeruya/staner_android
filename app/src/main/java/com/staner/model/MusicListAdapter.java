package com.staner.model;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.staner.MainActivity;
import com.staner.R;
import com.staner.tab.base.BaseListener;
import com.staner.util.Util;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

/**
 * Created by Teruya on 27/04/17.
 */

public class MusicListAdapter extends DragItemAdapter<Pair<Long, String>, MusicListAdapter.ViewHolder>
{
    private int layoutId;
    private int grabHandleId;
    private int menuLayoutId;

    // base listener setted by PlaylistMusicFragment or AlbumMusicFragment
    private BaseListener baseListener;

    // indicates if the album name is VISIBLE or GONE
    private boolean isAlbumEnable;

    private MainActivity mainActivity;

    public MusicListAdapter(
            ArrayList<Pair<Long, String>> list,
            int layoutId,
            int grabHandleId,
            int menuLayoutId,
            BaseListener baseListener,
            MainActivity mainActivity,
            boolean isAlbumEnable)
    {
        this.layoutId = layoutId;
        this.grabHandleId = grabHandleId;
        this.menuLayoutId = menuLayoutId;
        this.baseListener = baseListener;
        this.mainActivity = mainActivity;
        this.isAlbumEnable = isAlbumEnable;
        setHasStableIds(true);
        setItemList(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        if( !isAlbumEnable )
        {
            view.findViewById(R.id.album_textview).setVisibility(View.GONE);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        super.onBindViewHolder(holder, position);
        String text = mItemList.get(position).second;

        holder.id = mItemList.get(position).first;
        holder.nameTextView.setText(text);
        if( !isAlbumEnable )
        {
            holder.albumTextView.setVisibility(View.GONE);
        }
//        else holder.albumTextView.setText(mainActivity.getAlbumModelByMusicId((int) holder.id).getName());

        holder.menuButton.setTag((int)getItemId(position));
        holder.menuButton.setOnClickListener(onMenuButtonClickListener);
//        holder.artImageView.setImageBitmap(Util.getThumbnailFromMp3Path(mainActivity.getMusicModelById(holder.id).getPath(), mainActivity));
        holder.itemView.setTag(mItemList.get(position));
    }

    @Override
    public long getItemId(int position)
    {
        return mItemList.get(position).first;
    }

    private View.OnClickListener onMenuButtonClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Util.createMusicPlaylistPopupMenu((int)view.getTag(), mainActivity, view, menuLayoutId, baseListener);
        }
    };

    public class ViewHolder extends DragItemAdapter.ViewHolder
    {
        private TextView nameTextView;
        private TextView albumTextView;
        private ImageView artImageView;
        private ImageView menuButton;
        private long id;

        ViewHolder(final View itemView)
        {
            super(itemView, grabHandleId, false);
            nameTextView = (TextView) itemView.findViewById(R.id.name_textview);
            albumTextView = (TextView) itemView.findViewById(R.id.album_textview);
            artImageView = (ImageView) itemView.findViewById(R.id.art_imageview);
            menuButton = (ImageView) itemView.findViewById(R.id.menu_button);
        }

        @Override
        public void onItemClicked(View view)
        {
            baseListener.onMusicClickListener((int) id);
        }

        @Override
        public boolean onItemLongClicked(View view)
        {
            return true;
        }
    }
}