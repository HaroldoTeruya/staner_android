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

//    public static class Music implements BaseColumns
//    {
//        // attributes
//        public static final String TABLE_NAME = "music";
//
//        public static final String COLUMN_NAME = "name";
//        public static final String COLUMN_PATH = "path";
//        public static final String COLUMN_DURATION = "duration";
//        public static final String COLUMN_ALBUM = "album";
//        public static final String COLUMN_ARTIST = "artist";
//        public static final String COLUMN_GENRE = "genre";
//
//        public static final String ID = "id";
//        public static final String ALBUM_ID = "album_id";
//
//        // query
//        public static final String SELECT = " SELECT  * FROM " + TABLE_NAME + " WHERE " + ID + " = ";
//        public static final String SELECT_ALL = " SELECT  * FROM " + TABLE_NAME + " ";
//        public static final String SELECT_BY_ALBUM = " SELECT  * FROM " + TABLE_NAME + " WHERE " + ALBUM_ID + " = ";
//
//        public static String insertOrIgnore(MediaFileInfo mediaFileInfo, int albumId)
//        {
//            String name = mediaFileInfo.getFileName();
//            String path = mediaFileInfo.getFilePath();
//            String albumName = mediaFileInfo.getFileAlbumName();
//            String duration = String.valueOf(mediaFileInfo.getDuration());
//            String artist = mediaFileInfo.getFileArtist();
//            String genre = mediaFileInfo.getFileGenre();
//
//            if( artist == null )
//            {
//                artist = "Desconhecido";
//            }
//            if( genre == null )
//            {
//                genre = "Não identificado";
//            }
//
//            return "INSERT OR IGNORE INTO " + TABLE_NAME + " (" +
//                    COLUMN_NAME + ", " +
//                    COLUMN_PATH + ", " +
//                    COLUMN_DURATION + ", " +
//                    COLUMN_ALBUM + ", " +
//                    COLUMN_ARTIST + ", " +
//                    COLUMN_GENRE + ", " +
//                    ALBUM_ID +
//                    ") VALUES (" +
//                    "'" + name  + "', " +
//                    "'" + path + "', " +
//                    "" + duration + ", " +
//                    "'" + albumName + "', " +
//                    "'" + artist + "', " +
//                    "'" + genre + "', " +
//                    "" + albumId + ")";
//
//        }
//    }

//    public static class Album implements BaseColumns
//    {
//        public static final String TABLE_NAME = "album";
//
//        public static final String COLUMN_NAME = "album_name";
//        public static final String COLUMN_ART = "album_art";
//
//        public static final String ID = "id";
//
//        // query
//        public static final String SELECT_ALL = " SELECT  * FROM " + TABLE_NAME + " ";
//    }

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
    public static final int DATABASE_VERSION = 15;

//    public static String CREATE_TABLE_MUSIC = "CREATE TABLE " + Music.TABLE_NAME +
//            "(" + Music.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//            Music.COLUMN_NAME + " TEXT," +
//            Music.COLUMN_PATH + " TEXT," +
//            Music.COLUMN_DURATION + " INTEGER," +
//            Music.COLUMN_ALBUM + " TEXT, " +
//            Music.ALBUM_ID + " INTEGER," +
//            Music.COLUMN_ARTIST + " TEXT," +
//            Music.COLUMN_GENRE + " TEXT," +
//            "UNIQUE (" + Music.COLUMN_NAME + ") )";

//    public static String CREATE_TABLE_ALBUM = "CREATE TABLE " + Album.TABLE_NAME +
//            "(" + Album.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//            Album.COLUMN_NAME + " TEXT," +
//            Album.COLUMN_ART + " BLOB," +
//            "UNIQUE (" + Album.COLUMN_NAME + ") )";

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
