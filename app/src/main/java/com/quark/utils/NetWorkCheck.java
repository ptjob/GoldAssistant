/*
 * 文  件  名:  NetWorkCheck.java
 * 创建日期:  2013-6-23/上午9:50:33
 * 版       权:  Royal.k.peng@gmail.com, All rights reserved
 * 作       者:  Royal
 * 座 右  铭:  想要看到璀璨的星空，就要忘记平趟的大地
 */
package com.quark.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络检测
 * @author 
 *
 */
public class NetWorkCheck {

	/** 
	 * 判断网络是否可用
	 * 连接上了，并且能够连接到互联网
	 * @return  true,可用; false 不可用
	 */
	public static boolean isOpenNetwork(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
			return connManager.getActiveNetworkInfo().isAvailable();
		}

		return false;
	}


	/**
	 * 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap网络3：net网络 
	 * @author Administrator / Never give up, adhere to in the end.
	 * @addrs  pengqinping@gmail.com
	 * @param context
	 * @return 没有网络 ：WIFI网络：wap网络：net网络
	 */
	public static String getNetWorkType(Context context) {
		String netType = "";
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return "no type";
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			Logger.i("network type :" + networkInfo.getExtraInfo());
			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
				netType = "cmnet";
			} else {
				netType = "cmwap";
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = "wifi";
		}
		return netType;
	}

	public static boolean isMoblieNet(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return false;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

}
