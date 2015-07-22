package com.parttime.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.quark.jianzhidaren.ApplicationControl;

/**
 * 判断手机的网络状态
 * Created by luhua on 2015/7/20.
 */
public class NetUtils {

    protected static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = null;
            try {
                info = connectivity.getAllNetworkInfo();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isNetworkAvailable() {
        try {
            return isNetworkAvailable(ApplicationControl.getInstance());
        }catch (RuntimeException e){

        }
        return false ;
    }
}
