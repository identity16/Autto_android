package dev.handeul.autto;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class AuttoApp extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
