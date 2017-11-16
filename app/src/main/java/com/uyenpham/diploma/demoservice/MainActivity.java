package com.uyenpham.diploma.demoservice;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Song> mArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listRaw();
        startService();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.i("Current song ", "onDestroy");
        stopService(new Intent(MainActivity.this, PlayService.class));
        super.onDestroy();
    }

    public void listRaw() {
        Field[] fields = R.raw.class.getFields();
        for (int count = 0; count < fields.length; count++) {
            int resourceID = 0;
            try {
                resourceID = fields[count].getInt(fields[count]);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            mArrayList.add(new Song(fields[count].getName(),resourceID));
        }
    }
    private void startService(){
        Intent intent = new Intent(MainActivity.this, PlayService.class);
        intent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", mArrayList);
        intent.putExtra("bundle", bundle);
        startService(intent);
    }
}
