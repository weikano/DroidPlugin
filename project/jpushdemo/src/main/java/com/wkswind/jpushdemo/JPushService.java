package com.wkswind.jpushdemo;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JPushService extends Service {
    private static final String TAG = JPushService.class.getSimpleName();
    private ArrayList<String> test;

    public JPushService() {
        test = new ArrayList<>();
        Log.i(TAG, "JPushService: onCreate ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i(TAG, "onStart: ");
        test.iterator();
//        PackageManager pm = getPackageManager();
//        Intent test = new Intent("test");
//        Iterator<ResolveInfo> iter = pm.queryBroadcastReceivers(test, 0).iterator();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
