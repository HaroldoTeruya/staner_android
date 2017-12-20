package com.staner.database;

import android.provider.BaseColumns;

import com.staner.model.MediaFileInfo;

/**
 * Created by Teruya on 01/06/17.
 */

public final class DataBase
{
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DataBase() {}

    public class Playlist implements BaseColumns
    {
        public static final String TABLE_NAME = "playlist";

        public static final String COLUMN_NAME = "playlist_name";
        public static final String COLUMN_ART = "playlist_art";

        public static final String ID = "id";

        // query
        public static final String SELECT_ALL = " SELECT  * FROM " + TABLE_NAME + " ";
    }

    public static class MusicPlaylist implements BaseColumns
    {
        public static final String TABLE_NAME = "music_" + Playlist.TABLE_NAME;
        public static final String ID = "id";
        public static final String MUSIC_NAME = "music_name";
        public static final String PLAYLIST_ID = Playlist.TABLE_NAME + "_" + Playlist.ID;

        // query
        public static final String SELECT_MUSIC_BY_PLAYLIST = "SELECT " + MUSIC_NAME + " FROM " + TABLE_NAME + " WHERE " + PLAYLIST_ID + " = ";
    }

    public static final String DATABASE_NAME = "staner";

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 16;

    /*
     * id INT
     * name TEXT
     * ART BLOB
     */
    public static String CREATE_TABLE_PLAYLIST = "CREATE TABLE " + Playlist.TABLE_NAME +
            "(" + Playlist.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Playlist.COLUMN_NAME + " TEXT," +
            Playlist.COLUMN_ART + " BLOB," +
            "UNIQUE (" + Playlist.COLUMN_NAME + ") )";

    /*
     * id INT
     * playlist_id INT
     * music_name TEXT
     */
    public static String CREATE_TABLE_MUSIC_PLAYLIST = "CREATE TABLE " + MusicPlaylist.TABLE_NAME +
            "(" + MusicPlaylist.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MusicPlaylist.MUSIC_NAME + " TEXT," +
            MusicPlaylist.PLAYLIST_ID + " INTEGER )";
}
