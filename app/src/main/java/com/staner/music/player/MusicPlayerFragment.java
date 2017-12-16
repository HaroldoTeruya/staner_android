package com.staner.music.player;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.staner.MainActivity;
import com.staner.R;
import com.staner.database.DataBase;
import com.staner.model.AlbumModel;
import com.staner.model.MusicModel;
import com.staner.music.equalizer.MusicEqualizerFragment;
import com.staner.music.preset.PresetMenuDialogFragment;
import com.staner.util.Util;

/**
 * Created by Teruya on 01/05/17.
 */
public class MusicPlayerFragment extends Fragment
{
    //=================================================================================================
    //=========================================== ATTRIBUTES ==========================================
    //=================================================================================================

    public static final String TAG = "MusicFragment";

    //=================================================================================================
    //============================================ CONSTRUCTOR ========================================
    //=================================================================================================

    //=================================================================================================
    //============================================= METHODS ===========================================
    //=================================================================================================

    public static MusicPlayerFragment instantiate(int id)
    {
        MusicPlayerFragment musicPlayerFragment = new MusicPlayerFragment();
        Bundle bundle = new Bundle();
//        bundle.putInt(DataBase.Music.ID,id);
        musicPlayerFragment.setArguments(bundle);
        return musicPlayerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.getSupportActionBar().hide();
        mainActivity.findViewById(R.id.minimized_player_content).setVisibility(View.GONE);
        return inflater.inflate(R.layout.music_player_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

//        MusicModel musicModel = ((MainActivity)getActivity()).getMusicModelById(getArguments().getInt(DataBase.Music.ID));
//        AlbumModel albumModel = ((MainActivity)getActivity()).getAlbumModelByMusicId(musicModel.getId());
//
//        ((TextView)view.findViewById(R.id.progress_textview)).setText("0");
//        ((TextView)view.findViewById(R.id.duration_textview)).setText(Util.convertSecondToHHMMString(musicModel.getDuration()));
//        ((ImageView)view.findViewById(R.id.art_imageview)).setImageBitmap(Util.getImageFromMp3Path(musicModel.getPath(), getActivity()));
//        ((TextView)view.findViewById(R.id.textview)).setText(albumModel.getName());
//        ((ImageView)view.findViewById(R.id.album_art_imageview)).setImageBitmap(albumModel.getArt());
//        ((TextView)view.findViewById(R.id.name_textview)).setText(musicModel.getName());
//
//        createPresetMenu();
//
//        final MainActivity mainActivity = (MainActivity) getActivity();
//        view.findViewById(R.id.minimized_equalizer_button).setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                mainActivity.getSupportFragmentManager().beginTransaction().add(R.id.music_fragment_content, new MusicEqualizerFragment()).addToBackStack(MusicEqualizerFragment.TAG).commit();
//            }
//        });
//        view.findViewById(R.id.minimized_player_button).setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                MusicPlayerFragment.this.getFragmentManager().popBackStackImmediate();
//                mainActivity.findViewById(R.id.minimized_player_content).setVisibility(View.VISIBLE);
//            }
//        });
//        view.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                MusicPlayerFragment.this.getFragmentManager().popBackStackImmediate();
//                mainActivity.findViewById(R.id.minimized_player_content).setVisibility(View.GONE);
//                mainActivity.stop();
//            }
//        });
//        view.findViewById(R.id.play_button).setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                boolean play = Util.togglePlayer(view);
//                if( play )
//                {
//                    mainActivity.play();
//                }
//                else
//                {
//                    mainActivity.pause();
//                }
//            }
//        });
//        view.findViewById(R.id.prev_button).setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                mainActivity.prev();
//            }
//        });
//        view.findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                mainActivity.next();
//            }
//        });
    }

    private void createPresetMenu()
    {
        View view = getView().findViewById(R.id.preset_button);
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new PresetMenuDialogFragment().show(getFragmentManager(), PresetMenuDialogFragment.TAG);
            }
        });
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        ((MainActivity)getActivity()).getSupportActionBar().show();
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
