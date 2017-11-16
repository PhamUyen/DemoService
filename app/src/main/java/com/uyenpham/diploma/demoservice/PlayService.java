package com.uyenpham.diploma.demoservice;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by steadfast-macmini-05 on 11/13/17.
 */

public class PlayService extends Service {
    private ArrayList<Song> mArrayList = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private int index =0;
    private MediaPlayer.OnCompletionListener  listener;
    Notification status;
    private final String LOG_TAG = "NotificationService";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mediaPlayer = new MediaPlayer();
     initCompleteCallback();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mArrayList = (ArrayList<Song>)intent.getBundleExtra("bundle").getSerializable("list");
        nextSong(index);
//        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
//
//            Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
//
//        } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
//            Toast.makeText(this, "Clicked Previous", Toast.LENGTH_SHORT).show();
//            Log.i(LOG_TAG, "Clicked Previous");
//        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
//            Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show();
//            Log.i(LOG_TAG, "Clicked Play");
//        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
//            Toast.makeText(this, "Clicked Next", Toast.LENGTH_SHORT).show();
//            Log.i(LOG_TAG, "Clicked Next");
//        } else if (intent.getAction().equals(
//                Constants.ACTION.STOPFOREGROUND_ACTION)) {
//            Log.i(LOG_TAG, "Received Stop Foreground Intent");
//            Toast.makeText(this, "Service Stoped", Toast.LENGTH_SHORT).show();
//            stopForeground(true);
//            stopSelf();
//        }
        return Service.START_NOT_STICKY;
    }

    public void onDestroy() {
        Log.i("Current song ", "stop");
        mediaPlayer.stop();
        mediaPlayer.release();
        stopSelf();
        super.onDestroy();
    }
    private void initCompleteCallback() {
        listener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                index++;
                if(index < mArrayList.size()){
                    nextSong(index);
                }
            }
        };

    }
    private void nextSong(int i) {
        mediaPlayer = MediaPlayer.create(this, mArrayList.get(i).getId());
        Log.i("Current song ", mArrayList.get(i).getName());
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(listener);
        showNotification(mArrayList.get(i));
    }

    private void showNotification(Song song) {
// Using RemoteViews to bind custom layouts into Notification
        RemoteViews views = new RemoteViews(getPackageName(),
                R.layout.status_bar);
        RemoteViews bigViews = new RemoteViews(getPackageName(),
                R.layout.status_bar_expanded);

// showing default album image
        views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        views.setViewVisibility(R.id.status_bar_album_art, View.GONE);
        bigViews.setImageViewBitmap(R.id.status_bar_album_art,
                Constants.getDefaultAlbumArt(this));

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, PlayService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, PlayService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, PlayService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, PlayService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);

        views.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);

        views.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);

        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

//        views.setImageViewResource(R.id.status_bar_play,
//                R.drawable.apollo_holo_dark_pause);
//        bigViews.setImageViewResource(R.id.status_bar_play,
//                R.drawable.apollo_holo_dark_pause);

        views.setTextViewText(R.id.status_bar_track_name, song.getName());
        bigViews.setTextViewText(R.id.status_bar_track_name, song.getName());

        views.setTextViewText(R.id.status_bar_artist_name, song.getName());
        bigViews.setTextViewText(R.id.status_bar_artist_name, song.getName());

        bigViews.setTextViewText(R.id.status_bar_album_name, song.getName());

        status = new Notification.Builder(this).build();
        status.contentView = views;
        status.bigContentView = bigViews;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.mipmap.ic_launcher;
        status.contentIntent = pendingIntent;
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
    }

}
