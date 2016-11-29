package com.wkswind.demo;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.liulishuo.filedownloader.FileDownloader;
import com.morgoo.droidplugin.pm.PluginManager;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
    private static final int OVERLAY_PERMISSION_REQ_CODE = 0x1;
    private OnlineItem item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_launch);
        Action1<Permission> permissionAction1 = new Action1<Permission>() {
            @Override
            public void call(Permission permission) {
                if(!permission.granted){
                    if(permission.name.equalsIgnoreCase(Manifest.permission.SYSTEM_ALERT_WINDOW)){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!Settings.canDrawOverlays(MainActivity.this)){
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + MainActivity.this.getPackageName()));
                                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                            }
                        }
                    }else{
                        Toast.makeText(MainActivity.this, permission.name + "没有获得权限", Toast.LENGTH_SHORT).show();
                    }
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
//        permissions.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
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
        rxPermissions.requestEach(arr).subscribe(permissionAction1);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "请在悬浮窗管理中允许本应用", Toast.LENGTH_SHORT).show();
                }
            }
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
