package com.wkswind.demo;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.morgoo.droidplugin.PluginApplication;
import com.morgoo.helper.Log;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


public class PluginGameApplication extends PluginApplication implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = PluginGameApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
        Log.setDebug(BuildConfig.DEBUG);
        FileDownloader.init(getApplicationContext(), new FileDownloadHelper.OkHttpClientCustomMaker() {
            @Override
            public OkHttpClient customMake() {
                final OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.connectTimeout(5, TimeUnit.SECONDS);
                builder.proxy(Proxy.NO_PROXY);
                return builder.build();
            }
        });
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        android.util.Log.i(TAG, "onActivityCreated: " + activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
