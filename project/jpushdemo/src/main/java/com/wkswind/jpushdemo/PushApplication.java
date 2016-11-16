package com.wkswind.jpushdemo;

import android.app.Application;

/**
 * Created by Administrator on 2016-11-10.
 */

public class PushApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        PushManager.getInstance().initialize(this);
    }
}
