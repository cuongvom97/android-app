package com.example.testapp.ReceiverManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.example.testapp.R;

public class BaoNhac extends Service {
    public static MediaPlayer mediaPlayer;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Báo cho","Báo đi");
        mediaPlayer=MediaPlayer.create(this,R.raw.baothuc);
        mediaPlayer.start();
        Intent intent1=new Intent(this,NotificationNN.class);
        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pendingIntent=PendingIntent.getService(this,1,intent1,0);
        Notification.Builder builder=new Notification.Builder(this);
        builder.setContentTitle("Quản lý thời giam và công việc");
        builder.setContentText("Bạn sắp có công việc cần làm.");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        notificationManager.notify(1,builder.build());
        return START_NOT_STICKY;
    }
}
