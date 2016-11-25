package com.wkswind.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2016-11-23.
 */

public class ComputeService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Compute();
    }
}
