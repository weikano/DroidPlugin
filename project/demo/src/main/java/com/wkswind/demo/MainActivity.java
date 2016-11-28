package com.wkswind.demo;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.liulishuo.filedownloader.FileDownloader;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.wkswind.aidl.ICompute;

import rx.Subscriber;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
    private OnlineItem item;
    private ServiceConnection conn;
    private Action1<Boolean> subscriber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_launch);
        subscriber = new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if(!aBoolean){
                    Toast.makeText(MainActivity.this, "请在应用设置中打开该权限，否则应用可能无法运行", Toast.LENGTH_SHORT).show();
                }
            }
        };
        RxPermissions rxPermissions = new RxPermissions(this);
//        rxPermissions.request(Manifest.permission.RECEIVE_USER_PRESENT).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.WAKE_LOCK).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.READ_PHONE_STATE).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.WRITE_SETTINGS).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.VIBRATE).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.ACCESS_NETWORK_STATE).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.ACCESS_WIFI_STATE).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.GET_TASKS).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.SYSTEM_ALERT_WINDOW).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.SEND_SMS).subscribe(subscriber);

//        rxPermissions.request(Manifest.permission.AUTHENTICATE_ACCOUNTS).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.GET_ACCOUNTS).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.USE_CREDENTIALS).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.CHANGE_NETWORK_STATE).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.READ_SMS).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.READ_CONTACTS).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.READ_HISTORY_BOOKMARKS).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.RECEIVE_SMS).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.RECEIVE_BOOT_COMPLETED).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.RESTART_PACKAGES).subscribe(subscriber);
//        rxPermissions.request(Manifest.permission.RUN_INSTRUMENTATION).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.INTERNET).subscribe(subscriber);
        rxPermissions.request(Manifest.permission.RECORD_AUDIO).subscribe(subscriber);


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
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                ICompute compute = ICompute.Stub.asInterface(iBinder);
                try {
                    Toast.makeText(MainActivity.this, "" + compute.add(3,4) +", " + compute, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        Intent intent = new Intent();
        ComponentName component = new ComponentName("com.wkswind.aidl","com.wkswind.aidl.ComputeService");
        intent.setComponent(component);
        bindService(intent,conn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    public void onLaunchGameClick(OnlineItem item) {
        Intent intent = new Intent(this, InstallActivity.class);
        intent.putExtra(OnlineItem.class.getName(), item);
        startActivity(intent);
//        InstallFragment fragment = InstallFragment.newInstance(item);
//        fm.beginTransaction().add(android.R.id.content, fragment, fragment.getClass().getSimpleName()).commit();
    }
}
