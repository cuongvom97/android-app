package com.example.testapp.ReceiverManager;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class NotificationNN extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public NotificationNN()
    {
        BaoNhac.mediaPlayer.stop();
    }
}
