package com.example.testapp.ReceiverManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BaoReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Dậy đi","Xin chào");
        Intent intent1=new Intent(context,BaoNhac.class);
        context.startService(intent1);
    }
}
