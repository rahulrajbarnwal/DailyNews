package com.rahulrajbarnwal.mynews;

import android.app.Application;
import android.content.Context;

public class DailyNews extends Application {
    private static DailyNews mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
    public static synchronized DailyNews getInstance(){
        return mInstance;
    }



}
