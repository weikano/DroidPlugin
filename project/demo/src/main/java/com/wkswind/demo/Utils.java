package com.wkswind.demo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.RemoteException;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;

import rx.Observable;
import rx.Subscriber;

import com.morgoo.droidplugin.pm.PluginManager;

import java.io.File;

/**
 * Created by Administrator on 2016-11-17.
 */

class Utils {

    static void launch(Context context, String path) {
        PackageManager pm = context.getPackageManager();
        final PackageInfo info = pm.getPackageArchiveInfo(path, 0);
        Intent intent = pm.getLaunchIntentForPackage(info.packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    static void createShortCut(Context context, String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, 0);
        Intent shortcutIntent = new Intent(context, LaunchActivity.class);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.putExtra(OnlineItem.class.getName(), path);


        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, R.drawable.ic_launcher));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "传奇枭雄");
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(addIntent);
    }


    static String getDownloadPath(Context context, String url) {
        String fileName = URLUtil.guessFileName(url, null, MimeTypeMap.getSingleton().getMimeTypeFromExtension("apk"));
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
        return file.getAbsolutePath();
    }


    static Observable<Integer> doInstallRx(final String path) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    int response = PluginManager.getInstance().installPackage(path, 0);
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(response);
                        subscriber.onCompleted();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(e);
                    }
                }
            }
        });
    }
}
