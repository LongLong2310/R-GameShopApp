package com.example.r_gameshopapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
            int stream = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", 0);
            if (stream == AudioManager.STREAM_MUSIC) {
                AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                int currentvol = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (currentvol == 0) {
                    Toast.makeText(context, "Music is off automatically because the device is muted.", Toast.LENGTH_SHORT).show();
                    context.stopService(new Intent(context, BackgroundMusicService.class));
                }
            }
        }
        if (intent.getAction().equals("Start_service_event")) {
            AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int currentvol = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (currentvol == 0) {
                Toast.makeText(context, "Music is off automatically because the device is muted.", Toast.LENGTH_SHORT).show();
                context.stopService(new Intent(context, BackgroundMusicService.class));
            }
        }

    }
}
