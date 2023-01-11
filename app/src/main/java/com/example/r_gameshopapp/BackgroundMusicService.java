package com.example.r_gameshopapp;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class BackgroundMusicService extends Service {

    private static final String TAG = null;
    MediaPlayer player;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.music);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);
        registerReceiver();
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        return Service.START_NOT_STICKY;
    }

    public void onPause() {

    }

    @Override
    public void onDestroy() {
        player.stop();
        player.release();
        stopSelf();
        unregisterReceiver(myReceiver);
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {

    }
    protected MyReceiver myReceiver;
    protected IntentFilter intentFilter;

    private void registerReceiver(){
        myReceiver = new MyReceiver();
        IntentFilter filter=new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
//        filter.addAction("android.media.RINGER_MODE_CHANGED");
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(myReceiver,filter);
    }
}
