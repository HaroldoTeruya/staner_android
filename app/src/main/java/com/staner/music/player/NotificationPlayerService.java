package com.staner.music.player;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.staner.MainActivity;
import com.staner.PlayerController;
import com.staner.R;

/**
 * Created by Teruya on 26/05/17.
 *
 * This class is responsible to create the notification player music layout as a service.
 *
 */
public class NotificationPlayerService extends Service
{
    //=================================================================================================
    //=========================================== ATTRIBUTES ==========================================
    //=================================================================================================

    // important to know the commands from the user
    private Notification notification;

    // class tag
    public static final String TAG = "NotificationService";

    // small layout
    private RemoteViews views;

    // big layout
    private RemoteViews bigViews;

    // binder to connect the MainActivity with this service
    private final IBinder binder = new LocalBinder();

    // the callback to call the MainActivity methods
    private NotificationPlayerServiceListener notificationPlayerServiceListener;

    // used to toggle the play/pause button
    private boolean play = true;

    //=================================================================================================
    //============================================= METHODS ===========================================
    //=================================================================================================

    @Override
    public IBinder onBind(Intent intent)
    {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // show notification
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION))
        {
            showNotification();
        }

        // on prev button clicked
        else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION))
        {
            notificationPlayerServiceListener.prev();
        }

        // on play button clicked
        else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION))
        {
            if (notificationPlayerServiceListener != null)
            {
                if( play )
                {
                    notificationPlayerServiceListener.pause();
                }
                else
                {
                    notificationPlayerServiceListener.resume();
                }
                toggleButton();
            }
        }

        // on next button clicked
        else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION))
        {
            notificationPlayerServiceListener.next();
        }

        // close notification
        else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION))
        {
            if( notificationPlayerServiceListener != null )
            {
                notificationPlayerServiceListener.stop();
            }

            notificationPlayerServiceListener.finish();

            stopForeground(true);
            stopSelf();
        }

        // return default value
        return START_STICKY;
    }

    private void toggleButton()
    {
        if( play )
        {
            views.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_play);
            bigViews.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_play);
        }
        else
        {
            views.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_pause);
            bigViews.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_pause);
        }

        notification.contentView = views;
        notification.bigContentView = bigViews;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);

        play = !play;
    }

    private void showNotification()
    {
        // Using RemoteViews to bind custom layouts into Notification
        views = new RemoteViews(getPackageName(), R.layout.status_bar);
        bigViews = new RemoteViews(getPackageName(), R.layout.status_bar_expanded);

        // showing default album image
        views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        views.setViewVisibility(R.id.status_bar_album_art, View.GONE);
        bigViews.setImageViewBitmap(R.id.status_bar_album_art, Constants.getDefaultAlbumArt(this));

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent previousIntent = new Intent(this, NotificationPlayerService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0, previousIntent, 0);

        Intent playIntent = new Intent(this, NotificationPlayerService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0, playIntent, 0);

        Intent nextIntent = new Intent(this, NotificationPlayerService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0, nextIntent, 0);

        Intent closeIntent = new Intent(this, NotificationPlayerService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0, closeIntent, 0);

        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);

        views.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);

        views.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);

        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        views.setTextViewText(R.id.status_bar_track_name, "Song Title");
        bigViews.setTextViewText(R.id.status_bar_track_name, "Song Title");

        bigViews.setTextViewText(R.id.status_bar_album_name, "Album Name");

        notification = new NotificationCompat.Builder(this).build();
        notification.contentView = views;
        notification.bigContentView = bigViews;
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.icon = R.drawable.logo_staner72;
        notification.contentIntent = pendingIntent;

        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);
    }

    //=================================================================================================
    //============================================== EVENTS ===========================================
    //=================================================================================================

    //=================================================================================================
    //======================================== SETTERS  & GETTERS =====================================
    //=================================================================================================

    public void setCallbacks(NotificationPlayerServiceListener notificationPlayerServiceListener)
    {
        this.notificationPlayerServiceListener = notificationPlayerServiceListener;
    }

    //=================================================================================================
    //============================================== CLASS ============================================
    //=================================================================================================

    static public class Constants
    {
        public interface ACTION
        {
            String MAIN_ACTION = "com.marothiatechs.customnotification.action.main";
            String INIT_ACTION = "com.marothiatechs.customnotification.action.init";
            String PREV_ACTION = "com.marothiatechs.customnotification.action.prev";
            String PLAY_ACTION = "com.marothiatechs.customnotification.action.play";
            String NEXT_ACTION = "com.marothiatechs.customnotification.action.next";
            String STARTFOREGROUND_ACTION = "com.marothiatechs.customnotification.action.startforeground";
            String STOPFOREGROUND_ACTION = "com.marothiatechs.customnotification.action.stopforeground";

        }

        public interface NOTIFICATION_ID
        {
            int FOREGROUND_SERVICE = 101;
        }

        public static Bitmap getDefaultAlbumArt(Context context)
        {
            Bitmap bm = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            try
            {
                bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_staner192, options);
            }
            catch (Error ee)
            {
            }
            catch (Exception e)
            {
            }
            return bm;
        }
    }

    public class LocalBinder extends Binder
    {
        public NotificationPlayerService getService()
        {
            return NotificationPlayerService.this;
        }
    }

    //=================================================================================================
    //============================================ INTERFACE ==========================================
    //=================================================================================================

    public interface NotificationPlayerServiceListener
    {
        void play(String playlistName, int musicId);

        void resume();

        void pause();

        void prev();

        void next();

        void stop();

        void finish();
    }
}