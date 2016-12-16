package com.morgoo.helper.compat;

import android.content.Context;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import com.morgoo.droidplugin.reflect.FieldUtils;
import com.morgoo.droidplugin.reflect.MethodUtils;
import com.morgoo.helper.Log;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016-12-15.
 */

public class StorageVolumeCompat {
    private static Class<?> sClass;

    private static final String TAG = StorageVolumeCompat.class.getSimpleName();

    public static Class getMyClass() throws ClassNotFoundException {
        if (sClass == null) {
            sClass = Class.forName("android.os.storage.StorageVolume");
        }
        return sClass;
    }

    public static class SystemVolume {
        String primary;
        ArrayList<String> removableVolumes = new ArrayList<>(2);
        public boolean needReplace(){
            return !removableVolumes.isEmpty();
        }

        public void replaceRemovableVolume(String path){
            if(TextUtils.isEmpty(primary)){
                return;
            }
            if(removableVolumes.isEmpty()){
                return;
            }
            if(!needReplace()){
                return;
            }
            for (int i = removableVolumes.size() - 1; i >= 0; i--) {
                String volume = removableVolumes.get(i);

                if(path.startsWith(volume)){
                    Log.i(TAG, "replaceRemovableVolume volume : " + volume +", path " + path);
                    path = path.replace(volume, primary);
                    break;
                }
            }
            Log.i(TAG, "replaceRemovableVolume after : " + path);
        }
    }

    public static SystemVolume scanVolumeLists(Context context) {
        SystemVolume result = new SystemVolume();
        StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            Object volumeList = MethodUtils.invokeMethod(sm, "getVolumeList", (Object[]) null);
//            Log.i(TAG, "volumeList: " + volumeList);
            if (volumeList != null) {
//                if (volumeList instanceof Array) {
                    int length = Array.getLength(volumeList);
                    for (int i = 0; i < length; i++) {
                        Object volume = Array.get(volumeList, i);
                        if (volume.getClass().equals(StorageVolumeCompat.getMyClass())) {
                            boolean primary = (boolean) FieldUtils.readField(volume, "mPrimary");
                            String path = String.valueOf(FieldUtils.readField(volume, "mPath"));
                            if (primary) {
                                result.primary = path;
                            } else {
                                result.removableVolumes.add(path);
                            }
                        }
                    }
//                }
            }
            return result;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
