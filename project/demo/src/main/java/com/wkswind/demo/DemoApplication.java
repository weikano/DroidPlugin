package com.wkswind.demo;

import android.app.Application;
import android.telephony.TelephonyManager;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.morgoo.droidplugin.PluginApplication;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


public class DemoApplication extends PluginApplication {
    @Override
    public void onCreate() {
        super.onCreate();
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
