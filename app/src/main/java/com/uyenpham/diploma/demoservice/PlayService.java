package com.uyenpham.diploma.demoservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by steadfast-macmini-05 on 11/13/17.
 */

public class PlayService extends Service {
    private ArrayList<Song> mArrayList = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private int index =0;
    private MediaPlayer.OnCompletionListener  listener;
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
    }

}
