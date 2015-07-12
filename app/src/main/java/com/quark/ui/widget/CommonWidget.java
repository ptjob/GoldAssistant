package com.quark.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import com.quark.jianzhidaren.EnterActivity;

public class CommonWidget {
	/**
	 * 返回键
	 * 
	 * @param imageButton
	 * @param activity
	 */
	public static void back(ImageButton imageButton, final Activity activity) {
		imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});
	}

	public static void showAlertDialog(Context context, String str,
			final String str2) {

		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setMessage(str);
		builder.setTitle(str2);
		builder.setPositiveButton("确 定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});
		builder.create().show();
	}

	/**
	 * 游客
	 * 
	 * @param context
	 * @param str
	 * @param str2
	 * @param str3
	 */
	public static void showAlertDialog(final Activity activity,
			final Context context, String str, final String str2, String str3) {

		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setMessage(str);
		builder.setTitle(str2);
		builder.setPositiveButton(str3, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});
		builder.setNegativeButton("注册登录",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent intent = new Intent();
						intent.setClass(context, EnterActivity.class);
						context.startActivity(intent);
					}
				});
		builder.create().show();
	}
}
