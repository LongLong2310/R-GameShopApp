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
        player.setVolume(100, 100);
        registerReceiver();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        Intent intent1 = new Intent("Start_service_event");
        sendBroadcast(intent1);

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

    private void registerReceiver() {
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("android.media.VOLUME_CHANGED_ACTION");
        filter.addAction("Start_service_event");
        registerReceiver(myReceiver, filter);
    }
}
