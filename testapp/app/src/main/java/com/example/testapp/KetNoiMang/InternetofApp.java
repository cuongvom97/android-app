package com.example.testapp.KetNoiMang;
import android.app.Application;
public class InternetofApp extends Application {
    static InternetofApp internetofApp;

    @Override
    public void onCreate() {
        super.onCreate();
        internetofApp=this;
    }
    public static synchronized InternetofApp getInstance() {
        return internetofApp;
    }
}
