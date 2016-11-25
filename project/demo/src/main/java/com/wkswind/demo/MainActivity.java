package com.wkswind.demo;

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
import com.wkswind.aidl.ICompute;

public class MainActivity extends AppCompatActivity {
    private OnlineItem item;
    private ServiceConnection conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_launch);
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
