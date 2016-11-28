package com.wkswind.demo;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.liulishuo.filedownloader.FileDownloader;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
    private OnlineItem item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_launch);
        Action1<Boolean> subscriber = new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if(!aBoolean){
                    Toast.makeText(MainActivity.this, "有权限未打开，请在应用设置中打开该权限，否则应用可能无法运行", Toast.LENGTH_SHORT).show();
                }
            }
        };

        RxPermissions rxPermissions = new RxPermissions(this);
        ArrayList<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            permissions.add(Manifest.permission.INSTALL_SHORTCUT);
        }
        permissions.add(Manifest.permission.CHANGE_WIFI_STATE);
        permissions.add(Manifest.permission.WAKE_LOCK);
        permissions.add(Manifest.permission.READ_PHONE_STATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        permissions.add(Manifest.permission.VIBRATE);
        permissions.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
        permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
        permissions.add(Manifest.permission.GET_TASKS);
        permissions.add(Manifest.permission.SYSTEM_ALERT_WINDOW);
        permissions.add(Manifest.permission.SEND_SMS);
        permissions.add(Manifest.permission.GET_ACCOUNTS);
        permissions.add(Manifest.permission.CHANGE_NETWORK_STATE);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.READ_SMS);
        permissions.add(Manifest.permission.READ_CONTACTS);
        permissions.add(Manifest.permission.RECEIVE_SMS);
        permissions.add(Manifest.permission.RECEIVE_BOOT_COMPLETED);
        permissions.add(Manifest.permission.RESTART_PACKAGES);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.INTERNET);
        permissions.add(Manifest.permission.RECORD_AUDIO);
        String[] arr = new String[permissions.size()];
        permissions.toArray(arr);
        rxPermissions.request(arr).subscribe(subscriber);

//        ArrayList<Observable<Boolean>> arrays = new ArrayList<>();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            arrays.add(rxPermissions.request(Manifest.permission.INSTALL_SHORTCUT));
//        }
//        arrays.add(rxPermissions.request(Manifest.permission.CHANGE_WIFI_STATE));
//        arrays.add(rxPermissions.request(Manifest.permission.WAKE_LOCK));
//        arrays.add(rxPermissions.request(Manifest.permission.READ_PHONE_STATE));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            arrays.add(rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE));
//        }
//        arrays.add(rxPermissions.request(Manifest.permission.VIBRATE));
//        arrays.add(rxPermissions.request(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS));
//        arrays.add(rxPermissions.request(Manifest.permission.ACCESS_NETWORK_STATE));
//        arrays.add(rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE));
//        arrays.add(rxPermissions.request(Manifest.permission.ACCESS_WIFI_STATE));
//        arrays.add(rxPermissions.request(Manifest.permission.GET_TASKS));
//        arrays.add(rxPermissions.request(Manifest.permission.SYSTEM_ALERT_WINDOW));
//        arrays.add(rxPermissions.request(Manifest.permission.SEND_SMS));
//        arrays.add(rxPermissions.request(Manifest.permission.GET_ACCOUNTS));
//        arrays.add(rxPermissions.request(Manifest.permission.CHANGE_NETWORK_STATE));
//        arrays.add(rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION));
//        arrays.add(rxPermissions.request(Manifest.permission.READ_SMS));
//        arrays.add(rxPermissions.request(Manifest.permission.READ_CONTACTS));
//        arrays.add(rxPermissions.request(Manifest.permission.RECEIVE_SMS));
//        arrays.add(rxPermissions.request(Manifest.permission.RECEIVE_BOOT_COMPLETED));
//        arrays.add(rxPermissions.request(Manifest.permission.RESTART_PACKAGES));
//        arrays.add(rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION));
//        arrays.add(rxPermissions.request(Manifest.permission.INTERNET));
//        arrays.add(rxPermissions.request(Manifest.permission.RECORD_AUDIO));
//        Observable.concat(arrays).subscribe(subscriber);

//        rxPermissions.request(Manifest.permission.INSTALL_SHORTCUT).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.CHANGE_WIFI_STATE).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.RECEIVE_USER_PRESENT).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.WAKE_LOCK).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.READ_PHONE_STATE).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.WRITE_SETTINGS).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.VIBRATE).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.ACCESS_NETWORK_STATE).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.ACCESS_WIFI_STATE).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.GET_TASKS).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.SYSTEM_ALERT_WINDOW).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.SEND_SMS).subscribe(subscriber);
//
////        rxPermissions.request(Manifest.permission.AUTHENTICATE_ACCOUNTS).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.GET_ACCOUNTS).subscribe(subscriber);
////        rxPermissions.request(Manifest.permission.USE_CREDENTIALS).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.CHANGE_NETWORK_STATE).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.READ_SMS).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.READ_CONTACTS).subscribe(subscriber);
////        rxPermissions.request(Manifest.permission.READ_HISTORY_BOOKMARKS).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.RECEIVE_SMS).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.RECEIVE_BOOT_COMPLETED).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.RESTART_PACKAGES).subscribe(subscriber);
////        rxPermissions.request(Manifest.permission.RUN_INSTRUMENTATION).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.INTERNET).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.RECORD_AUDIO).subscribe(subscriber);


        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey(OnlineItem.class.getName())) {
            String path = extras.getString(OnlineItem.class.getName());
            Utils.launch(this, path);
            finish();
        }else {
            item = OnlineItem.fakeItem();
            findViewById(R.id.launch_game).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onLaunchGameClick(item);
                }
            });
            FileDownloader.getImpl().bindService();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onLaunchGameClick(OnlineItem item) {
        Intent intent = new Intent(this, InstallActivity.class);
        intent.putExtra(OnlineItem.class.getName(), item);
        startActivity(intent);
    }
}
