package com.wkswind.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PrintPersonService extends Service {
    public PrintPersonService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new PersonPrint();
    }
}
