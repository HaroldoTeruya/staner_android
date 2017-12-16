package com.staner.music.preset;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.staner.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class PresetMenuDialogFragment extends DialogFragment
{
    public static final String TAG = "PRESETMENU";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.presetmenu_dialogfragment_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ListView listView = (ListView) view.findViewById(R.id.preset_listview);
        ArrayList<String> presetList = new ArrayList<>();
//        presetList.add("1");
//        presetList.add("2");
//        presetList.add("3");
//        presetList.add("1");
//        presetList.add("2");
//        presetList.add("3");
//        presetList.add("1");
//        presetList.add("2");
//        presetList.add("3");
//        presetList.add("1");
//        presetList.add("2");
//        presetList.add("3");
        PresetAdapter adapter = new PresetAdapter(getActivity(), presetList, new RemoveListener()
        {
            @Override
            public void remove(String name)
            {
                Log.d(TAG, name);
            }
        });
        listView.setAdapter(adapter);
    }

    public static class PresetAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<String> items;
        private final RemoveListener removeListener;

        public PresetAdapter(Context context, ArrayList<String> items, RemoveListener removeListener)
        {
            this.context = context;
            this.items = items;
            this.removeListener = removeListener;
        }

        @Override
        public int getCount()
        {
            return items.size();
        }

        @Override
        public Object getItem(int i)
        {
            return items.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            if( view == null )
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.presetmenu_listitem_layout, viewGroup, false);
                view.findViewById(R.id.remove_button).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        String name = ((TextView) view.getRootView().findViewById(R.id.name_textview)).getText().toString();
                        removeListener.remove(name);
                    }
                });
            }
            return view;
        }
    }

    public interface RemoveListener
    {
        public void remove(String name);
    }
}