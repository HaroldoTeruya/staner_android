package com.staner.music.preset;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.staner.R;

public class EditPresetDialogFragment extends DialogFragment
{
    //=================================================================================================
    //=========================================== ATTRIBUTES ==========================================
    //=================================================================================================

    public static final String TAG = "EditPreset";
    private int id;

    //=================================================================================================
    //============================================ CONSTRUCTOR ========================================
    //=================================================================================================

    //=================================================================================================
    //============================================= METHODS ===========================================
    //=================================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.music_edit_preset_dialogfragment_layout,container,false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String name = ((EditText)view.findViewById(R.id.name_edittext)).getText().toString();
                EditPresetDialogFragment.this.dismiss();
            }
        });
        view.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditPresetDialogFragment.this.dismiss();
            }
        });
    }
}