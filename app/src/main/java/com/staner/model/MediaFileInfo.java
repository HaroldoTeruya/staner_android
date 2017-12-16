package com.staner.model;

/**
 * Created by joao on 02/05/17.
 */

public class MediaFileInfo
{
    private String filePath = "";
    private String fileName = "";
    private String fileAlbumName = "";
    private int duration = 0;
    private byte[] fileAlbumArt = null;
    private String fileGenre;
    private String fileArtist;
    private String fileType = "";
    private String filePlaylist = "";
    private int id = -1;

    public static final String TAG = "MediaFileInfo";

    public MediaFileInfo()
    {

    }

    public MediaFileInfo(String filePath, String fileName, String fileAlbumName, int duration, byte[] fileAlbumArt, String fileGenre, String fileArtist, int id)
    {
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileAlbumName = fileAlbumName;
        this.duration = duration;
        this.fileAlbumArt = fileAlbumArt;
        this.fileGenre = fileGenre;
        this.fileArtist = fileArtist;
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileAlbumName()
    {
        return fileAlbumName;
    }

    public void setFileAlbumName(String fileAlbumName)
    {
        this.fileAlbumName = fileAlbumName;
    }

    public int getDuration()
    {
        return duration;
    }

    public void setDuration(int duration)
    {
        this.duration = duration;
    }

    public byte[] getFileAlbumArt()
    {
        return fileAlbumArt;
    }

    public void setFileAlbumArt(byte[] fileAlbumArt)
    {
        this.fileAlbumArt = fileAlbumArt;
    }

    public String getFileGenre()
    {
        return fileGenre;
    }

    public void setFileGenre(String fileGenre)
    {
        this.fileGenre = fileGenre;
    }

    public String getFileArtist()
    {
        return fileArtist;
    }

    public void setFileArtist(String fileArtist)
    {
        this.fileArtist = fileArtist;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getFilePlaylist()
    {
        return filePlaylist;
    }

    public void setFilePlaylist(String filePlaylist)
    {
        this.filePlaylist = filePlaylist;
    }
}
