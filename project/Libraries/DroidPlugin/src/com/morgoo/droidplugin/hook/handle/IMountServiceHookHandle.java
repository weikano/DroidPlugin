/*
**        DroidPlugin Project
**
** Copyright(c) 2015 Andy Zhang <zhangyong232@gmail.com>
**
** This file is part of DroidPlugin.
**
** DroidPlugin is free software: you can redistribute it and/or
** modify it under the terms of the GNU Lesser General Public
** License as published by the Free Software Foundation, either
** version 3 of the License, or (at your option) any later version.
**
** DroidPlugin is distributed in the hope that it will be useful,
** but WITHOUT ANY WARRANTY; without even the implied warranty of
** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
** Lesser General Public License for more details.
**
** You should have received a copy of the GNU Lesser General Public
** License along with DroidPlugin.  If not, see <http://www.gnu.org/licenses/lgpl.txt>
**
**/

package com.morgoo.droidplugin.hook.handle;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import com.morgoo.droidplugin.hook.BaseHookHandle;
import com.morgoo.droidplugin.hook.HookedMethodHandler;
import com.morgoo.droidplugin.pm.PluginManager;
import com.morgoo.droidplugin.reflect.FieldUtils;
import com.morgoo.droidplugin.reflect.MethodUtils;
import com.morgoo.helper.Log;
import com.morgoo.helper.Utils;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy Zhang(zhangyong232@gmail.com) on 2015/3/6.
 */
public class IMountServiceHookHandle extends BaseHookHandle {

//    private static final String ANDROID_DATA = "Android/data/";
//    private static final String ANDROID_OBB = "Android/obb/";
    public IMountServiceHookHandle(Context context) {
        super(context);

    }

    @Override
    protected void init() {
        sHookedMethodHandlers.put("mkdirs", new mkdirs(mHostContext));
    }


    private class mkdirs extends HookedMethodHandler {
        private ArrayList<String> disabledVolumes = new ArrayList<>();
        private String primaryVolumn ;
        private mkdirs(Context context) {
            super(context);
            mountVolumePaths();
        }

        private void mountVolumePaths(){
            StorageManager sm = (StorageManager) mHostContext.getSystemService(Context.STORAGE_SERVICE);
            try {
                Class<StorageManager> clazz = StorageManager.class;
                Method getVolumeListMethod = clazz.getMethod("getVolumeList");
                if(getVolumeListMethod != null) {
                    Object volumeList = getVolumeListMethod.invoke(sm);
                    if(volumeList != null){
                        int length = Array.getLength(volumeList);
                        for(int i=0;i<length;i++){
                            Object volume = Array.get(volumeList, i);
                            Log.i("StorageManager", String.valueOf(volume));
                            Object mState = FieldUtils.readField(volume,"mState", true);
                            if(!mState.equals("mounted")){
                                disabledVolumes.add(String.valueOf(FieldUtils.readField(volume,"mPath", true)));
                            }else{
                                primaryVolumn = String.valueOf(FieldUtils.readField(volume,"mPath", true));
                            }
                        }
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


        //  /sdcard/Android/data/com.example.plugin/fdfdfdfd.fdfd
        // /sdcard/Android/data/hostpackagename/Plugin/com.example.plugin/fdfdfdfd.fdfd
        @Override
        protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
            if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
                final int index = 0;
                if (args != null && args.length > index && args[index] instanceof String) {
                    String callingPkg = (String) args[index];
                    if (!TextUtils.equals(callingPkg, mHostContext.getPackageName())) {
                        args[index] = mHostContext.getPackageName();
                    }
                }

                //FIXME 这里这种暴力修改方式可能会产生问题。比如插件直接写死的情况。
                final int index1 = 1;
                if (args != null && args.length > index1 && args[index1] instanceof String) {
                    String path = (String) args[index1];
//                    String path1 = new File(Environment.getExternalStorageDirectory(), "Android/data/").getPath();
                    if (path != null && path.indexOf(mHostContext.getPackageName()) < 0) {
                        String[] dirs = path.split("/");
                        if (dirs != null && dirs.length > 0) {
                            String pluginPackageName = null;
                            for (int i = 0; i < dirs.length; i++) {
                                String str = dirs[i];
                                if (TextUtils.isEmpty(str)) {
                                    continue;
                                }
                                if (!Utils.validateJavaIdentifier(str)) {
                                    continue;
                                }
                                if (PluginManager.getInstance().isPluginPackage(str)) {
                                    pluginPackageName = str;
                                    break;
                                }
                            }
                            if (pluginPackageName != null) {
                                path = path.replaceFirst(pluginPackageName, mHostContext.getPackageName() + "/Plugin/" + pluginPackageName);
                                args[index1] = path;
                            }
                        }
                    }
                }
            } else {
                //FIXME 这里这种暴力修改方式可能会产生问题。比如插件直接写死的情况。
                final int index1 = 0;
                if (args != null && args.length > index1 && args[index1] instanceof String) {
                    String path = (String) args[index1];
//                    String path1 = new File(Environment.getExternalStorageDirectory(), "Android/data/").getPath();

                    if (path != null && path.indexOf(mHostContext.getPackageName()) < 0) {
                        String[] dirs = path.split("/");
                        if (dirs != null && dirs.length > 0) {
                            String pluginPackageName = null;
                            for (int i = 0; i < dirs.length; i++) {
                                String str = dirs[i];
                                if (TextUtils.isEmpty(str)) {
                                    continue;
                                }
                                if (!Utils.validateJavaIdentifier(str)) {
                                    continue;
                                }
                                if (PluginManager.getInstance().isPluginPackage(str)) {
                                    pluginPackageName = str;
                                    break;
                                }
                            }
                            if (pluginPackageName != null) {
                                path = path.replaceFirst(pluginPackageName, mHostContext.getPackageName() + "/Plugin/" + pluginPackageName);
                                args[index1] = path;
                            }
                        }
                    }
                }
            }
            if(args != null && args.length > 1){
                if(args[1] instanceof String){
                    String path = (String) args[1];
                    Log.i("MkDirsPathBefore", path);
                    path = convertPath(path);
                    Log.i("MkDirsPathAfter", path);
                    args[1] = path;
                }
            }
            return super.beforeInvoke(receiver, method, args);
        }
//
        private String convertPath(String path){
            for(String volume : disabledVolumes){
                if(path.startsWith(volume)){
                    return path.replaceFirst(volume, primaryVolumn);
                }
            }
            return path;
        }
    }


}
