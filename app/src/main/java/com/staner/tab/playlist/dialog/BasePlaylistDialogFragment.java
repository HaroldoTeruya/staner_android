package com.staner.tab.playlist.dialog;

import android.support.v4.app.DialogFragment;
import android.view.View;

import com.staner.MainActivity;
import com.staner.database.DataBaseController;
import com.staner.model.MediaFileInfo;
import com.staner.model.PlaylistModel;
import com.staner.tab.base.BaseListener;

import java.util.List;

public class BasePlaylistDialogFragment extends DialogFragment
{
    //=================================================================================================
    //=========================================== ATTRIBUTES ==========================================
    //=================================================================================================

    public static final String TAG = "BasePlaylist";
    protected BaseListener baseListener;

    //=================================================================================================
    //============================================ CONSTRUCTOR ========================================
    //=================================================================================================

    //=================================================================================================
    //============================================= METHODS ===========================================
    //=================================================================================================

    public void insertPLaylist(PlaylistModel playlistModel)
    {
//        DataBaseController dataBaseController = new DataBaseController(getActivity());
//        dataBaseController.insertPlaylist(playlistModel);
//        List<PlaylistModel> playlistModelList = dataBaseController.getPlaylistList();
//        ((MainActivity) getActivity()).setPlaylistModelList(playlistModelList);
//        dataBaseController.close();
    }

    public void editPLaylist(PlaylistModel playlistModel)
    {
        // update database
        DataBaseController dataBaseController = new DataBaseController(getActivity());
        dataBaseController.editPlaylist(playlistModel);
        dataBaseController.close();

        // update mainactivity
        ((MainActivity) getActivity()).updatePlaylist(playlistModel);
    }

    //=================================================================================================
    //============================================== EVENTS ===========================================
    //=================================================================================================

    protected View.OnClickListener onCancelButtonClickLitener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            BasePlaylistDialogFragment.this.dismiss();
        }
    };

    protected View.OnClickListener onImageViewClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            ((MainActivity)getActivity()).loadImageFromGallery(BasePlaylistDialogFragment.this);
        }
    };

    //=================================================================================================
    //======================================== SETTERS  & GETTERS =====================================
    //=================================================================================================

    public void setBaseListener(BaseListener baseListener)
    {
        this.baseListener = baseListener;
    }

    //=================================================================================================
    //============================================== CLASS ============================================
    //=================================================================================================
}