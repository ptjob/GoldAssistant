package com.quark.ui.widget;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;

@SuppressLint({ "ResourceAsColor", "NewApi" })
public class SaixuanUi {
	/**
	 * 动态生成地址
	 */
	public static void initDidian(Context context, ArrayList<String> citys,
			TableLayout table, OnClickListener onclick, int idhead) {
		// 获取屏幕宽度
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		if (citys.size() > 0) {
			for (int j = 0; j < (int) Math.ceil(citys.size() / 4.0); j++) {
				TableRow tableRow = new TableRow(context);
				tableRow.setPaddingRelative(30, 10, 20, 10);
				for (int i = 0; i < 4 && j * 4 + i < citys.size(); i++) {
					TextView textView = new TextView(context);
					String tempqu = citys.get(j * 4 + i);
					// 去除区字
					if (citys.get(j * 4 + i).endsWith("区")) {
						tempqu = tempqu.substring(0, tempqu.length() - 1);
					}
					textView.setText(tempqu);
					if (j + i == 0) {
						Resources resources = context.getResources();
						Drawable btnDrawable;
						if (idhead == 1) {
							btnDrawable = resources
									.getDrawable(R.color.choose_city);
							textView.setBackgroundDrawable(btnDrawable);
						} else if (idhead == 2) {
							btnDrawable = resources
									.getDrawable(R.color.saixuan_sjian);
							textView.setBackgroundDrawable(btnDrawable);
						} else if (idhead == 3) {
							btnDrawable = resources
									.getDrawable(R.color.choose_type);
							textView.setBackgroundDrawable(btnDrawable);
						} else if (idhead == 4) {
							btnDrawable = resources
									.getDrawable(R.color.choose_paytype);
							textView.setBackgroundDrawable(btnDrawable);
						} else if (idhead == 5) {
							btnDrawable = resources
									.getDrawable(R.color.saixuan_sjian);
							textView.setBackgroundDrawable(btnDrawable);
						}
						textView.setTextColor(resources
								.getColor(R.color.body_color));
					} else {
						Resources resources = context.getResources();
						Drawable btnDrawable = resources
								.getDrawable(R.drawable.bord_saixun);
						textView.setBackgroundDrawable(btnDrawable);
						textView.setTextColor(resources
								.getColor(R.color.ziti_huise));
					}
					textView.setPadding(5, 15, 5, 15);
					textView.setTextSize(14);
					textView.setMinEms(4);
					textView.setSingleLine(true);
					textView.setEllipsize(TextUtils.TruncateAt.END);
					textView.setWidth((width - 120) / 4);
					textView.setGravity(Gravity.CENTER);
					textView.setId(j * 4 + i + 1 + idhead * 1000);
					tableRow.addView(textView);
					textView.setOnClickListener(onclick);
					TextView textView2 = new TextView(context);
					textView2.setWidth(20);
					tableRow.addView(textView2);
				}
				table.addView(tableRow);
			}
		}
	}

	/**
	 * 设置显示状态
	 */
	public static void setStatus(Context context, TextView view, boolean stauts) {
		if (stauts) {
			Resources resources = context.getResources();
			Drawable btnDrawable = resources.getDrawable(R.color.saixuan_sjian);
			view.setBackgroundDrawable(btnDrawable);
			view.setTextColor(context.getResources().getColor(
					R.color.body_color));
		} else {
			Resources resources = context.getResources();
			Drawable btnDrawable = resources
					.getDrawable(R.drawable.bord_saixun);
			view.setBackgroundDrawable(btnDrawable);
			view.setTextColor(context.getResources().getColor(
					R.color.ziti_huise));
		}
	}

}
