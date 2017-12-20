package com.staner.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.staner.MainActivity;
import com.staner.R;
import com.staner.model.AlbumModel;
import com.staner.model.MediaFileInfo;
import com.staner.model.MusicModel;
import com.staner.tab.base.BaseListener;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Util
{
    // ===================================================================================
    // ==================================== ATTRIBUTES
    // ===================================================================================

    private final String TAG = Util.this.getClass().getSimpleName();

    // ===================================================================================
    // ==================================== CONSTRUCTORS
    // ===================================================================================

    // ===================================================================================
    // ==================================== METHODS
    // ===================================================================================

    static public View inflate(Activity activity, int id )
    {
        return ((LayoutInflater)activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE )).inflate( id, null );
    }

//    static public int getIntFromTextView(View view)
//    {
//        return Integer.valueOf(((TextView) view).getText().toString());
//    }
//
//    static public boolean hasTag(View view, int tag)
//    {
//        if ( view.findViewWithTag(tag) == null )
//        {
//            return false;
//        }
//        else
//        {
//            return true;
//        }
//    }

    static public void createPopupMenu(
            final int id,
            final MainActivity mainActivity,
            View view,
            int menuLayoutId,
            final BaseListener baseListener)
    {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        baseListener.onPlaylistRemoved(id);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        PopupMenu popup = new PopupMenu(mainActivity, view);
        popup.getMenuInflater().inflate(menuLayoutId, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.add:
                        baseListener.onAddToPlayListRequest(id);
                        break;

                    case R.id.remove:
                        new AlertDialog.Builder(mainActivity).setMessage(R.string.remove_playlist_request).setPositiveButton(R.string.confirm, dialogClickListener).setNegativeButton(R.string.cancel, dialogClickListener).show();
                        break;

                    case R.id.edit:
                        baseListener.onEditListRequest(id);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    static public void createMusicPlaylistPopupMenu(
            final int id,
            final MainActivity mainActivity,
            View view,
            int menuLayoutId,
            final BaseListener baseListener)
    {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        baseListener.onRemoveMusicFromPlaylistListener(id);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        PopupMenu popup = new PopupMenu(mainActivity, view);
        popup.getMenuInflater().inflate(menuLayoutId, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.add:
                        baseListener.onAddToPlayListRequest(id);
                        break;

                    case R.id.remove:
                        new AlertDialog.Builder(mainActivity).setMessage(R.string.remove_music_playlist_request).setPositiveButton(R.string.confirm, dialogClickListener).setNegativeButton(R.string.cancel, dialogClickListener).show();
                        break;

                    case R.id.edit:
                        baseListener.onEditListRequest(id);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }


    public static boolean togglePlayer(View view)
    {
        String tag = (String) view.getTag();
        ImageView imageView = (ImageView)view;
        if( tag.equals("true") )
        {
            imageView.setImageResource(R.drawable.pause);
            view.setTag("");
            return true;
        }
        else
        {
            imageView.setImageResource(R.drawable.play);
            view.setTag("true");
            return false;
        }
    }

//    public static String mlsecToFormatedString(int duration)
//    {
//        long longVal = duration / 1000;
//        int hours = (int) longVal / 3600;
//        int remainder = (int) longVal - hours * 3600;
//        int mins = remainder / 60;
//        remainder = remainder - mins * 60;
//        int secs = remainder;
//
////        int[] ints = {hours , mins , secs};
//
//        String durationFormated = "";
//        if( hours != 0 )
//        {
//            durationFormated += String.valueOf(hours);
//        }
//        else durationFormated += "00";
//        if( mins != 0 )
//        {
//            durationFormated += ":" + String.valueOf(mins);
//        }
//        else durationFormated += ":00";
//        if( secs != 0 )
//        {
//            durationFormated += ":" + String.valueOf(secs);
//        }
//        else durationFormated += ":00";
//
//        return durationFormated;
//    }

//    public static String convertSecondToHHMMString( long lnValue)
//    {
//        lnValue = lnValue / 1000;
//        String lcStr = "00:00:00";
//        String lcSign = (lnValue>=0 ? " " : "-");
//        lnValue = lnValue * (lnValue>=0 ? 1 : -1);
//
//        if (lnValue>0) {
//            long lnHor  = (lnValue/3600);
//            long lnHor1 = (lnValue % 3600);
//            long lnMin  = (lnHor1/60);
//            long lnSec  = (lnHor1 % 60);
//
//            lcStr = lcSign + ( lnHor < 10 ? "0": "") + String.valueOf(lnHor) +":"+
//                    ( lnMin < 10 ? "0": "") + String.valueOf(lnMin) +":"+
//                    ( lnSec < 10 ? "0": "") + String.valueOf(lnSec) ;
//        }
//        return lcStr;
//
//    }

    public static byte[] convertBitmapToByte(Bitmap bmp)
    {
        if( bmp == null ) return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

//    public static Bitmap getImageFromMp3Path(String filePath, Context context)
//    {
//        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
//        Log.d("TAG", filePath);
//        mediaMetadataRetriever.setDataSource(filePath);
//        byte[] art = mediaMetadataRetriever.getEmbeddedPicture();
//        Bitmap image = null;
//        if (art != null)
//        {
//            image = BitmapFactory.decodeByteArray(art, 0, art.length);
//        }
//        else image = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
//        return image;
//    }

//    public static Bitmap getThumbnailFromMp3Path(String filePath, Context context)
//    {
//        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
//        Log.d("TAG", filePath);
//        mediaMetadataRetriever.setDataSource(filePath);
//        byte[] art = mediaMetadataRetriever.getEmbeddedPicture();
//        Bitmap image = null;
//        if (art != null)
//        {
//            image = BitmapFactory.decodeByteArray(art, 0, art.length);
//        }
//        else image = BitmapFactory.decodeResource(context.getResources(), R.drawable.music);
//        image = Bitmap.createScaledBitmap(image, 64, 64, false);
//        return image;
//    }

//    public static List<AlbumModel> createGenreListByMusicModelList(List<MusicModel> musicModelList, Context context)
//    {
//        List<AlbumModel> genreList = new ArrayList<>();
//        int id = 1;
//        for(  MusicModel musicModel : musicModelList )
//        {
//            String genreText = musicModel.getGenre();
//            if( genreList.isEmpty() )
//            {
//                AlbumModel genre = new AlbumModel();
//                genre.setId(id++);
//                List<MusicModel> musicGenreList = new ArrayList<>();
//                musicGenreList.add(musicModel);
//
//                genre.setMusicList(musicGenreList);
//                genre.setName(genreText);
////                genre.setArt(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo));
//
//                genreList.add(genre);
//            }
//            else
//            {
//                AlbumModel genreModel = getGenreModelByGenre(genreList, genreText);
//
//                // exist
//                if( genreModel!=null )
//                {
//                    genreModel.getMusicList().add(musicModel);
//                }
//
//                /// not exist
//                else
//                {
//                    AlbumModel genre = new AlbumModel();
//                    genre.setId(id++);
//                    List<MusicModel> musicGenreList = new ArrayList<>();
//                    musicGenreList.add(musicModel);
//
//                    genre.setMusicList(musicGenreList);
//                    genre.setName(genreText);
////                    genre.setArt(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo));
//
//                    genreList.add(genre);
//                }
//            }
//        }
//        return genreList;
//    }

//    private static AlbumModel getGenreModelByGenre(List<AlbumModel> genreModelList, String genre)
//    {
//        for( AlbumModel genreModel : genreModelList )
//        {
//            if( genreModel.getName().toLowerCase().equals(genre.toLowerCase()) )
//            {
//                return genreModel;
//            }
//        }
//        return null;
//    }

//    public static List<AlbumModel> createArtistListByMusicModelList(List<MusicModel> musicModelList, Context context)
//    {
//        List<AlbumModel> artistList = new ArrayList<>();
//        int id = 1;
//        for(  MusicModel musicModel : musicModelList )
//        {
//            String artistText = musicModel.getArtist();
//            if( artistList.isEmpty() )
//            {
//                AlbumModel artist = new AlbumModel();
//                artist.setId(id++);
//                List<MusicModel> musicArtistList = new ArrayList<>();
//                musicArtistList.add(musicModel);
//
//                artist.setMusicList(musicArtistList);
//                artist.setName(artistText);
////                artist.setArt(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo));
//
//                artistList.add(artist);
//            }
//            else
//            {
//                AlbumModel artistModel = getArtistModelByGenre(artistList, artistText);
//
//                // exist
//                if( artistModel!=null )
//                {
//                    artistModel.getMusicList().add(musicModel);
//                }
//
//                /// not exist
//                else
//                {
//                    AlbumModel artist = new AlbumModel();
//                    artist.setId(id++);
//                    List<MusicModel> musicArtistList = new ArrayList<>();
//                    musicArtistList.add(musicModel);
//
//                    artist.setMusicList(musicArtistList);
//                    artist.setName(artistText);
////                    artist.setArt(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo));
//
//                    artistList.add(artist);
//                }
//            }
//        }
//        return artistList;
//    }

//    private static AlbumModel getArtistModelByGenre(List<AlbumModel> artistModelList, String genre)
//    {
//        for( AlbumModel artistModel : artistModelList )
//        {
//            if( artistModel.getName().toLowerCase().equals(genre.toLowerCase()) )
//            {
//                return artistModel;
//            }
//        }
//        return null;
//    }

    public static Bitmap getThumbnailFromImage(Bitmap image)
    {
        int width = image.getWidth();
        int height = image.getHeight();
        float ratioBitmap = (float) width / (float) height;
        float ratioMax = 1;

        int finalWidth = 96;
        int finalHeight = 96;
        if (ratioMax > ratioBitmap)
        {
            finalWidth = (int) ((float)96 * ratioBitmap);
        }
        else
        {
            finalHeight = (int) ((float)96 / ratioBitmap);
        }
        image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
        return image;
    }

    public static Bitmap scaleImage(Bitmap image, int size)
    {
        int width = image.getWidth();
        int height = image.getHeight();
        float ratioBitmap = (float) width / (float) height;
        float ratioMax = 1;

        int finalWidth = size;
        int finalHeight = size;
        if (ratioMax > ratioBitmap)
        {
            finalWidth = (int) ((float)size * ratioBitmap);
        }
        else
        {
            finalHeight = (int) ((float)size / ratioBitmap);
        }
        image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
        return image;
    }

    public static List<MediaFileInfo> filter(List<MediaFileInfo> musicList, List<MediaFileInfo> filteredMusicList, String text)
    {
        text = text.toLowerCase(Locale.getDefault());
        musicList.clear();
        if( text.isEmpty() )
        {
            musicList.addAll(filteredMusicList);
        }
        else
        {
            for (MediaFileInfo mediaFileInfoList : filteredMusicList)
            {
                if (mediaFileInfoList.getFileName().toLowerCase(Locale.getDefault()).contains(text))
                {
                    musicList.add(mediaFileInfoList);
                }
            }
        }
        return musicList;
    }

    public static List<List<MediaFileInfo>> filterPlaylist(List<List<MediaFileInfo>> mediaFileCollectionList, List<List<MediaFileInfo>> filteredMediaFileCollectionList, String text)
    {
        text = text.toLowerCase(Locale.getDefault());
        mediaFileCollectionList.clear();
        if( text.isEmpty() )
        {
            mediaFileCollectionList.addAll(filteredMediaFileCollectionList);
        }
        else
        {
            for (List<MediaFileInfo> mediaFileInfoList : filteredMediaFileCollectionList)
            {
                if (mediaFileInfoList.get(0).getFileAlbumName().toLowerCase(Locale.getDefault()).contains(text))
                {
                    mediaFileCollectionList.add(mediaFileInfoList);
                }
            }
        }
        return mediaFileCollectionList;
    }

    // ===================================================================================
    // ==================================== EVENTS
    // ===================================================================================

    // ===================================================================================
    // ==================================== SETTERS GETTERS
    // ===================================================================================

    // ===================================================================================
    // ==================================== CLASS
    // ===================================================================================
}
