package com.morgoo.droidplugin.hook.handle;

import android.content.Context;
import android.os.Build;
import android.os.RemoteException;

import com.morgoo.droidplugin.hook.HookedMethodHandler;
import com.morgoo.droidplugin.pm.PluginManager;
import com.morgoo.helper.Log;

import java.lang.reflect.Method;

class ReplaceCallingPackageHookedMethodHandler extends HookedMethodHandler {

    private static final String TAG = ReplaceCallingPackageHookedMethodHandler.class.getSimpleName();

    public ReplaceCallingPackageHookedMethodHandler(Context hostContext) {
        super(hostContext);
    }

    @Override
    protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            if (args != null && args.length > 0) {
                for (int index = 0; index < args.length; index++) {
                    if (args[index] != null && (args[index] instanceof String)) {
                        String str = ((String) args[index]);
                        if (isPackagePlugin(str)) {
                            args[index] = mHostContext.getPackageName();
                            Log.i(TAG,String.valueOf(args[index]));
                        }
                    }
                }
            }
        }
        return super.beforeInvoke(receiver, method, args);
    }

    private static boolean isPackagePlugin(String packageName) throws RemoteException {
        return PluginManager.getInstance().isPluginPackage(packageName);
    }
}