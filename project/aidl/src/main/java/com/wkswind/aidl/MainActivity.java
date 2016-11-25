package com.wkswind.aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ServiceConnection computeConn, personConn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        computeConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                ICompute binder = ICompute.Stub.asInterface(iBinder);
                try {
                    Toast.makeText(MainActivity.this, "" + binder.add(5,6) +"," + binder.getClass(), Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };

        personConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                IPersonPrint binder = IPersonPrint.Stub.asInterface(iBinder);
                Person person = new Person();
                person.age = 10;
                person.male = true;
                person.name = "test";
                try {
                    binder.printPerson(person);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
//        bindService(new Intent(this, ComputeService.class), computeConn, BIND_AUTO_CREATE);
        bindService(new Intent(this, PrintPersonService.class),personConn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(personConn);
//        unbindService(computeConn);
    }
}
