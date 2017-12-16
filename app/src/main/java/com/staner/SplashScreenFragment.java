package com.staner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.staner.database.DataBaseController;
import com.staner.model.MediaFileInfo;

import java.util.ArrayList;
import java.util.List;

public class SplashScreenFragment extends Fragment
{
    public static final String TAG = SplashScreenFragment.class.getName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.splash_screen_fragmentactivity_layout, container, false);
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (!Settings.System.canWrite(getActivity()))
                {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case 2909:
            {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Log.e(TAG, "permission granted");
                    new DataLoaderAsyncTask().execute();
                }
                else
                {
                    Log.e(TAG, "permission denied");
                }
                return;
            }
        }
    }

    private class DataLoaderAsyncTask extends AsyncTask<String, Void, DataLoaderAsyncTask.DataModel>
    {
        @Override
        protected DataLoaderAsyncTask.DataModel doInBackground(String... params)
        {
            List<MediaFileInfo> mediaFileInfoList = new ArrayList<>();

            final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            final String[] cursor_cols =
                    {
                            MediaStore.Audio.Media._ID,
                            MediaStore.Audio.Media.ARTIST,
                            MediaStore.Audio.Media.ALBUM,
                            MediaStore.Audio.Media.TITLE,
                            MediaStore.Audio.Media.DATA,
                            MediaStore.Audio.Media.ALBUM_ID,
                            MediaStore.Audio.Media.DURATION
                    };

            final String where = MediaStore.Audio.Media.IS_MUSIC + "=1";
            final Cursor cursor = getActivity().getContentResolver().query(uri, cursor_cols, where, null, null);
            if( cursor.moveToFirst() )
            {
                while (cursor.moveToNext())
                {
                    String fileAlbumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                    String fileName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                    String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                    int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(filePath);
                    byte[] fileAlbumArt = mediaMetadataRetriever.getEmbeddedPicture();
                    String fileGenre = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
                    if( fileGenre == null )
                    {
                        fileGenre = getString(R.string.unknow);
                    }
                    String fileArtist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                    if( fileArtist == null )
                    {
                        fileArtist = getString(R.string.unknow);
                    }

                    mediaFileInfoList.add(new MediaFileInfo(filePath, fileName, fileAlbumName, duration, fileAlbumArt, fileGenre, fileArtist, id));
                }
            }
            cursor.close();

            DataBaseController dataBaseController = new DataBaseController(getActivity(), mediaFileInfoList);
            List<List<MediaFileInfo>> playlistList = dataBaseController.getPlaylistList();

            return new DataModel(mediaFileInfoList,playlistList);
        }

        @Override
        protected void onPostExecute(DataLoaderAsyncTask.DataModel dataModel)
        {
            MainActivity mainActivity = ((MainActivity) getActivity());
            mainActivity.createTab(dataModel.getMusicList(), dataModel.getPlaylistList());
            mainActivity.getSupportFragmentManager().popBackStackImmediate();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}

        public class DataModel
        {
            private List<List<MediaFileInfo>> playlistList = null;
            private List<MediaFileInfo> musicList = null;

            public DataModel(List<MediaFileInfo> musicList, List<List<MediaFileInfo>> playlistList)
            {
                this.musicList = musicList;
                this.playlistList = playlistList;
            }

            public List<List<MediaFileInfo>> getPlaylistList()
            {
                return playlistList;
            }

            public List<MediaFileInfo> getMusicList()
            {
                return musicList;
            }
        }
    }
}
