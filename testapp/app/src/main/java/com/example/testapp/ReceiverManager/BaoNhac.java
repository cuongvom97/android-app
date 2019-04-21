package com.example.testapp.ReceiverManager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.example.testapp.R;

public class BaoNhac extends Service {
    MediaPlayer mediaPlayer;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Báo cho","Báo đi");
        mediaPlayer=MediaPlayer.create(this,R.raw.baothuc);
        mediaPlayer.start();
        return START_NOT_STICKY;
    }
}
