package com.wkswind.demo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadLine;
import com.liulishuo.filedownloader.FileDownloadLineAsync;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.morgoo.droidplugin.pm.PluginManager;
import com.morgoo.helper.Log;
import com.morgoo.helper.compat.PackageManagerCompat;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.Locale;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LaunchActivity extends Activity {
    private static final int OVERLAY_PERMISSION_REQ_CODE = 0x1;
    private String path;
    private OnlineItem item = OnlineItem.fakeItem();

    private TextView label, progress;
    private ProgressBar progressBar;
    private View progressContainer;

    private PackageManager pm;
    private Action1<Integer> installAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_install);
        if(CommHelper.firstOpen(this)){
            ActionService.action(LaunchActivity.this, ActionService.ACTION_GAME_ACTIVATE);
        }

        progressBar = (ProgressBar) findViewById(R.id.progress);
        label = (TextView) findViewById(R.id.info);
        progressContainer = findViewById(R.id.progress_container);
        progress = (TextView) findViewById(R.id.progress_number);
        pm = getPackageManager();
        installAction = new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                switch (integer) {
                    case PackageManagerCompat.INSTALL_SUCCEEDED:
                        label.setText(R.string.install_success);
//                        Utils.createShortCut(LaunchActivity.this, path);
                        launch(path);
                        finish();
                        break;
                    case PackageManagerCompat.INSTALL_FAILED_INTERNAL_ERROR:
                        label.setText(R.string.install_failed);
                        break;
                    case -100001:
                        label.setText(R.string.install_failed_permission);
                        break;
                }
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(LaunchActivity.this)) {
                requestSystemAlertWindowPermission();
            } else {
                requestPermissions();
            }
        } else {
            requestPermissions();
        }
    }

    private void requestPermissions() {
        Subscriber<Permission> permissionSubscriber = new Subscriber<Permission>() {
            boolean allPermissionsGranted = true;

            @Override
            public void onCompleted() {
                if (allPermissionsGranted && item != null) {
                    progressContainer.setVisibility(View.VISIBLE);
                    path = Utils.getDownloadPath(LaunchActivity.this, item.url);
                    downloadOnlineItem();
                } else {
                    Toast.makeText(LaunchActivity.this, "有权限未获取到，请在应用设置中允许后重新打开", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(LaunchActivity.this, "异常退出: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onNext(Permission permission) {
                allPermissionsGranted = allPermissionsGranted && permission.granted;
                if (!permission.granted) {
                    if (permission.name.equalsIgnoreCase(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!Settings.canDrawOverlays(LaunchActivity.this)) {
                                requestSystemAlertWindowPermission();
                            }
                        }
                    } else {
                        Toast.makeText(LaunchActivity.this, permission.name + "没有获得权限", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
//        Action1<Permission> permissionAction1 = new Action1<Permission>() {
//            @Override
//            public void call(Permission permission) {
//                if(!permission.granted){
//                    if(permission.name.equalsIgnoreCase(Manifest.permission.SYSTEM_ALERT_WINDOW)){
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            if (!Settings.canDrawOverlays(LaunchActivity.this)){
//                                requestSystemAlertWindowPermission();
//                            }
//                        }
//                    }else{
//                        Toast.makeText(LaunchActivity.this, permission.name + "没有获得权限", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        };
        RxPermissions rxPermissions = new RxPermissions(this);
        ArrayList<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.INSTALL_SHORTCUT);
        permissions.add(Manifest.permission.CHANGE_WIFI_STATE);
        permissions.add(Manifest.permission.WAKE_LOCK);
        permissions.add(Manifest.permission.READ_PHONE_STATE);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.VIBRATE);
        permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
        //noinspection deprecation
        permissions.add(Manifest.permission.GET_TASKS);
        //特殊处理 requestSystemAlertWindow
//        permissions.add(Manifest.permission.SYSTEM_ALERT_WINDOW);
        permissions.add(Manifest.permission.SEND_SMS);
        permissions.add(Manifest.permission.GET_ACCOUNTS);
        permissions.add(Manifest.permission.CHANGE_NETWORK_STATE);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.READ_SMS);
        permissions.add(Manifest.permission.READ_CONTACTS);
        permissions.add(Manifest.permission.RECEIVE_SMS);
        permissions.add(Manifest.permission.RECEIVE_BOOT_COMPLETED);
        //noinspection deprecation
        permissions.add(Manifest.permission.RESTART_PACKAGES);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.INTERNET);
        permissions.add(Manifest.permission.RECORD_AUDIO);
        String[] arr = new String[permissions.size()];
        permissions.toArray(arr);
        rxPermissions.requestEach(arr).subscribe(permissionSubscriber);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestSystemAlertWindowPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + LaunchActivity.this.getPackageName()));
        startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "请在悬浮窗管理中允许本应用", Toast.LENGTH_SHORT).show();
                    requestSystemAlertWindowPermission();
                } else {
                    requestPermissions();
                }
            }
        }
    }

    private void downloadOnlineItem() {
        FileDownloader.getImpl().bindService(new Runnable() {
            @Override
            public void run() {
                int status = FileDownloader.getImpl().getStatus(item.url, path);
                if (status == FileDownloadStatus.completed) {
                    installPlugin(path);
                } else {
                    label.setText(R.string.download);
                    FileDownloader.getImpl().create(item.url).setPath(path).setListener(new FileDownloadListenerAdapter(LaunchActivity.this) {
                        @Override
                        protected void error(BaseDownloadTask task, Throwable e) {
                            super.error(task, e);
                            android.util.Log.e("ERROR", task.getTargetFilePath(), e);
                            label.setText(R.string.install_failed);
                        }

                        @Override
                        protected void completed(BaseDownloadTask task) {
                            super.completed(task);
                            ActionService.action(LaunchActivity.this, ActionService.ACTION_DOWNLOAD_FINISH);
                            installPlugin(task.getTargetFilePath());
                        }

                        @Override
                        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                            super.progress(task, soFarBytes, totalBytes);
                            int progressValue = (int) (1f * soFarBytes / totalBytes * 100);
                            progressBar.setProgress(progressValue);
                            progress.setText(String.format(Locale.getDefault(), "%d%%", progressValue));
                        }
                    }).start();
                }
            }
        });


    }

    private void installPlugin(String path) {
        final PackageInfo info = pm.getPackageArchiveInfo(path, 0);
        if (PluginManager.getInstance().isConnected()) {
            try {
                if (PluginManager.getInstance().isPluginPackage(info.packageName)) {
                    launch(path);
                    finish();
                } else {
                    label.setText(R.string.install);
                    Utils.doInstallRx(path).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(installAction);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void launch(String path) {
        Utils.launch(this, path);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileDownloader.getImpl().pauseAll();
    }
}
