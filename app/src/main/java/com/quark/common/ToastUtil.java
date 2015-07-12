package com.quark.common;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.quark.jianzhidaren.ApplicationControl;

public class ToastUtil {
	private static android.widget.Toast mToast = null;

	/** 保证在UI线程中显示Toast */
	private static Handler mHandler = new Handler(Looper.getMainLooper()) {

		@Override
		public void handleMessage(Message msg) {
			if (mToast != null) {
				mToast.cancel();
			}
			String text = (String) msg.obj;
			int duration = msg.arg2;

			Toast.makeText(ApplicationControl.getInstance(), text,duration).show();
			
		}
	};

	public static void showShortToast(String text) {
		showToast(text, Toast.LENGTH_SHORT);
	}

	public static void showShortToast(int textResId) {
		showToast(textResId, Toast.LENGTH_SHORT);
	}

	public static void showLongToast(String text) {
		showToast(text, Toast.LENGTH_LONG);
	}

	public static void showLongToast(int textResId) {
		showToast(textResId, Toast.LENGTH_LONG);
	}

	public static void showToast(int textResId, int duration) {
		showToast(ApplicationControl.getInstance().getString(textResId), duration);
	}

	public static void showToast(String text, int duration) {
		mHandler.sendMessage(mHandler.obtainMessage(0, 0, duration, text));
	}
}
