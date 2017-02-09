package com.iused.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Antto on 19-08-2016.
 */
public class GPS_Service extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.e("eeeeeee","ggggggggggggg");
        return super.onStartCommand(intent, flags, startId);
    }
}
