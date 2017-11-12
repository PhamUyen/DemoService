package com.uyenpham.diploma.demoservice;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Integer> mArrayList = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private int index =0;
    private MediaPlayer.OnCompletionListener  listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = new MediaPlayer();
        initCompleteCallback();
        listRaw();
        nextSong(index);
    }

    public void listRaw() {
        Field[] fields = R.raw.class.getFields();
        for (int count = 0; count < fields.length; count++) {
            Log.i("Raw Asset: ", fields[count].getName());
            int resourceID = 0;
            try {
                resourceID = fields[count].getInt(fields[count]);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            mArrayList.add(resourceID);
        }
    }

    private void initCompleteCallback() {
        listener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                index++;
                nextSong(index);
            }
        };

    }

    private void nextSong(int i) {
        mediaPlayer = MediaPlayer.create(this, mArrayList.get(i));
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(listener);
    }
}
