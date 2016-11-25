package com.wkswind.aidl;

import android.os.RemoteException;

/**
 * Created by Administrator on 2016-11-23.
 */

public class Compute extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
