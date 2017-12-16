package com.staner.tab.playlist.dialog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.staner.MainActivity;
import com.staner.R;
import com.staner.database.DataBase;
import com.staner.model.PlaylistModel;

public class EditPlaylistDialogFragment extends BasePlaylistDialogFragment
{
    //=================================================================================================
    //=========================================== ATTRIBUTES ==========================================
    //=================================================================================================

    public static final String TAG = "EditPlaylist";
    private int id;

    //=================================================================================================
    //============================================ CONSTRUCTOR ========================================
    //=================================================================================================

    //=================================================================================================
    //============================================= METHODS ===========================================
    //=================================================================================================

    public static EditPlaylistDialogFragment instantiate(int id, String name, byte[] art)
    {
        EditPlaylistDialogFragment editPlaylistDialogFragment = new EditPlaylistDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DataBase.Playlist.ID, id);
        bundle.putString(DataBase.Playlist.COLUMN_NAME, name);
        bundle.putByteArray(DataBase.Playlist.COLUMN_ART, art);
        editPlaylistDialogFragment.setArguments(bundle);
        return editPlaylistDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.playlist_edit_dialogfragment_layout,container,false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        id = getArguments().getInt(DataBase.Playlist.ID);
        final String currentName = getArguments().getString(DataBase.Playlist.COLUMN_NAME);
        byte art[] = getArguments().getByteArray(DataBase.Playlist.COLUMN_ART);

        final EditText nameEditText = ((EditText)view.findViewById(R.id.name_edittext));
        final ImageView imageView = (ImageView)view.findViewById(R.id.imageview);

        Bitmap image = null;
        if( art == null )
        {
            image = BitmapFactory.decodeResource(getResources(), R.drawable.playlist);
        }
        else image = BitmapFactory.decodeByteArray(art, 0, art.length);
        final Bitmap currentArt = image;

        nameEditText.setText(currentName);
        imageView.setImageBitmap(currentArt);

        /**
         * When confitm button is clicked.
         */
        view.findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String name = nameEditText.getText().toString();
                Bitmap art = ((BitmapDrawable)(imageView).getDrawable()).getBitmap();

                if( currentArt.equals(art) && currentName.equalsIgnoreCase(name.toLowerCase()) )
                {
                    EditPlaylistDialogFragment.this.dismiss();
                }
                else
                {
                    if( ((MainActivity)getActivity()).getPlaylistByName(name) != null && !currentName.equalsIgnoreCase(name.toLowerCase()) )
                    {
                        Toast.makeText(getActivity(), R.string.playlist_already_exist, Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        PlaylistModel newPlaylistModel = new PlaylistModel();
                        newPlaylistModel.setId(id);
                        newPlaylistModel.setArt(art);
                        newPlaylistModel.setName(name);
                        editPLaylist(newPlaylistModel);
                        Toast.makeText(getActivity(), R.string.playlist_edited, Toast.LENGTH_LONG).show();

                        baseListener.onPlaylistEdited(id, art, name);

                        EditPlaylistDialogFragment.this.dismiss();
                    }
                }
            }
        });

        view.findViewById(R.id.cancel_button).setOnClickListener(onCancelButtonClickLitener);
        view.findViewById(R.id.imageview).setOnClickListener(onImageViewClickListener);
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
}