package com.wkswind.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Locale;

final class CommHelper {
    static boolean firstOpen(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        boolean value = sp.getBoolean(CommHelper.class.getSimpleName(), true);
        if(value){
            sp.edit().putBoolean(CommHelper.class.getSimpleName(), false).apply();
        }
        return value;
    }

    static String getEquipmentId() {
        String model = Build.MODEL; // 手机型号
        model = model.replaceAll(" ", "");
        return model.toLowerCase(Locale.getDefault());
    }

    /**
     * 系统版本号
     */
    static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取设备imei号 山寨机获取不到 返回 na
     *
     * @return
     */
    static String getPhoneImei(Context context) {
        @SuppressLint("HardwareIds") String imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        if (TextUtils.isEmpty(imei)) {
            imei = "na";
        }
        return imei;
    }

    /**
     * 获取设备mac地址 没有获取到的话返回 na
     *
     * @return
     */
    @SuppressLint("HardwareIds")
    static String getPhoneMacAddress(Context context) {
        String mac = "";

        InputStreamReader inputStreamReader = null;
        LineNumberReader lineNumberReader = null;
        try {
            @SuppressLint("WifiManagerPotentialLeak") WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            mac = info.getMacAddress();

            if (TextUtils.isEmpty(mac)) {
                Process process = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
                inputStreamReader = new InputStreamReader(process.getInputStream());
                lineNumberReader = new LineNumberReader(inputStreamReader);
                String line = lineNumberReader.readLine();
                if (line != null) {
                    mac = line.trim();
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (lineNumberReader != null) {
                try {
                    lineNumberReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        if (TextUtils.isEmpty(mac)) {
            mac = "na";
        }

        return mac;
    }

}
