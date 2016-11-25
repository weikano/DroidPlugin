package com.wkswind.aidl;

import android.os.RemoteException;
import android.util.Log;

/**
 * Created by Administrator on 2016-11-24.
 */

public class PersonPrint extends IPersonPrint.Stub {
    private static final String TAG  = PersonPrint.class.getSimpleName();
    @Override
    public void printPerson(Person person) throws RemoteException {
        Log.i(TAG, "printPerson: " + person);
    }
}
