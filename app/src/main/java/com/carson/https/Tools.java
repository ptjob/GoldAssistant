package com.carson.https;

import android.os.Environment;

public class Tools {

	public static String getSdPath() {
		String state = getInsideStorage();
		String directoryPath = null;
		if ("removed".equals(state)) {
			directoryPath = "/mnt/sdcard2";
		} else if ("mounted".equals(state)) {
			directoryPath = Environment.getExternalStorageDirectory() + "";
		}
		return directoryPath;
	}

	public static String getInsideStorage() {
		return Environment.getExternalStorageState();
	}

}
