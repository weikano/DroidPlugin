package com.wkswind.demo;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.morgoo.droidplugin.PluginApplication;
import com.morgoo.helper.Log;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


public class PluginGameApplication extends PluginApplication {
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
    }
}
