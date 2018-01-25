package com.staner.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import com.staner.model.MediaFileInfo;
import com.staner.model.PlaylistModel;
import com.staner.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Teruya on 01/06/17.
 */

public class DataBaseController extends SQLiteOpenHelper
{
    //=================================================================================================
    //=========================================== ATTRIBUTES ==========================================
    //=================================================================================================

    public static final String TAG = "DataBase";
    private Context context;
    private List<MediaFileInfo> mediaFileInfoList = null;

    //=================================================================================================
    //============================================ CONSTRUCTOR ========================================
    //=================================================================================================

    public DataBaseController(Context context, List<MediaFileInfo> mediaFileInfoList)
    {
        super(context, DataBase.DATABASE_NAME, null, DataBase.DATABASE_VERSION);
        this.context = context;
        this.mediaFileInfoList = mediaFileInfoList;
    }

    public DataBaseController(Context context)
    {
        super(context, DataBase.DATABASE_NAME, null, DataBase.DATABASE_VERSION);
        this.context = context;
    }

    //=================================================================================================
    //============================================= METHODS ===========================================
    //=================================================================================================

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DataBase.CREATE_TABLE_PLAYLIST);
        db.execSQL(DataBase.CREATE_TABLE_MUSIC_PLAYLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + DataBase.Playlist.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataBase.MusicPlaylist.TABLE_NAME);

        // create new tables
        onCreate(db);
    }

    // PLAYLIST =======================================================================================

    /**
     * Get from the databse all the playlists
     *
     * @return List<List<MediaFileInfo>>
     */
    public List<List<MediaFileInfo>> getPlaylistList()
    {
        Log.d(TAG, "getPlaylistList");
        SQLiteDatabase db = getReadableDatabase();
        List<List<MediaFileInfo>> playlistMediaFileInfoList = new ArrayList<>();
        Cursor cursor = db.rawQuery(DataBase.Playlist.SELECT_ALL,null);
        if( cursor == null ) return null;
        else if (cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                String name = cursor.getString(cursor.getColumnIndex(DataBase.Playlist.COLUMN_NAME));
                int playlistId = cursor.getInt(cursor.getColumnIndex(DataBase.Playlist.ID));
                byte[] art = cursor.getBlob(cursor.getColumnIndex(DataBase.Playlist.COLUMN_ART));

                MediaFileInfo mediaFileInfo = new MediaFileInfo();
                mediaFileInfo.setFilePlaylist(name);
                mediaFileInfo.setId(playlistId);
                mediaFileInfo.setFileAlbumArt(art);

                List<MediaFileInfo> musicModelList = getMusicListByPLaylistId(playlistId);
                musicModelList.add(0, mediaFileInfo);
                playlistMediaFileInfoList.add(musicModelList);

                Log.d(TAG, name + " " + playlistId + " " + cursor.isClosed());

                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return playlistMediaFileInfoList;
    }

    /**
     * Insert an playlist
     *
     * @param name String
     * @param art Bitmap
     *
     * @return int The new id of the playlist
     */
    public int insertPlaylist(String name, Bitmap art)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBase.Playlist.COLUMN_NAME, name);
        contentValues.put(DataBase.Playlist.COLUMN_ART, Util.convertBitmapToByte(art));

        long id = db.insert(DataBase.Playlist.TABLE_NAME, null, contentValues);
        db.close();

        return (int) id;
    }

    /**
     * Remove a playlist from the database by id
     *
     * @param id int
     */
    public void removePlaylistById(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DataBase.Playlist.TABLE_NAME, DataBase.Playlist.ID + " = " + String.valueOf(id), null);
        db.close();

        removeMusicPlaylist(id);
    }

    /**
     * Insert a music in a playlist
     *
     * @param musicName String
     * @param playlistId int
     *
     * @return int The new id of the music-playlist relation
     */
    public int insertMusicInPlaylist(String musicName, int playlistId)
    {
        SQLiteDatabase db = getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBase.MusicPlaylist.MUSIC_NAME, musicName);
        contentValues.put(DataBase.MusicPlaylist.PLAYLIST_ID, playlistId);

        long id = db.insert(DataBase.MusicPlaylist.TABLE_NAME, null, contentValues);
        db.close();

        return (int) id;
    }

    /**
     * Remove a music from a playlist
     *
     * @param musicName The music name
     * @param playlistId The playlist id
     */
    public void removeMusicFromPlaylist(String musicName, int playlistId)
    {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DataBase.MusicPlaylist.TABLE_NAME + " WHERE " + DataBase.MusicPlaylist.MUSIC_NAME + " = " + (musicName) + " AND " + DataBase.MusicPlaylist.PLAYLIST_ID + " = " + (playlistId),null);
        if( cursor == null ) return;
        else if (cursor.moveToFirst())
        {
            String id = cursor.getString(cursor.getColumnIndex(DataBase.MusicPlaylist.ID));
            db.delete(DataBase.MusicPlaylist.TABLE_NAME, DataBase.MusicPlaylist.ID + "=?",  new String[]{id});
        }
        cursor.close();
        db.close();
    }

    public void editPlaylist(PlaylistModel playlistModel)
    {
        SQLiteDatabase db = getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBase.Playlist.COLUMN_NAME, playlistModel.getName());
        contentValues.put(DataBase.Playlist.COLUMN_ART, Util.convertBitmapToByte(playlistModel.getArt()));

        long id = db.update(DataBase.Playlist.TABLE_NAME, contentValues, DataBase.Playlist.ID + " = " + String.valueOf(playlistModel.getId()), null);
        db.close();
    }

    // get all music from a playlist
    public List<MediaFileInfo> getMusicListByPLaylistId(int playlistId)
    {
        Log.d(TAG, "getMusicModelListByPlaylist " + playlistId);
        List<MediaFileInfo> musicMediaFileInfoList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(DataBase.MusicPlaylist.SELECT_MUSIC_BY_PLAYLIST + String.valueOf(playlistId), null);
        if( cursor == null ) return null;
        else if( cursor.moveToFirst() )
        {
            while (!cursor.isAfterLast())
            {
                // music name from the music_playlist table
                String musicName = cursor.getString(cursor.getColumnIndex(DataBase.MusicPlaylist.MUSIC_NAME));

                MediaFileInfo musicMediaFileInfo = getMusicMediaFileInfoByName(musicName);
                if( musicMediaFileInfo == null )
                {
                    removeMusicFromPlaylist(musicName, playlistId);
                }
                else
                {
                    musicMediaFileInfoList.add(musicMediaFileInfo);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return musicMediaFileInfoList;
    }

    public MediaFileInfo getMusicMediaFileInfoByName(String name)
    {
        for( MediaFileInfo mediaFileInfo : mediaFileInfoList)
        {
            if( mediaFileInfo.getFileName().equalsIgnoreCase(name) )
            {
                return mediaFileInfo;
            }
        }
        return null;
    }

    // remove music playlist relationship
    public void removeMusicPlaylist(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DataBase.MusicPlaylist.TABLE_NAME, DataBase.MusicPlaylist.PLAYLIST_ID + " = " + String.valueOf(id), null);
        db.close();
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
