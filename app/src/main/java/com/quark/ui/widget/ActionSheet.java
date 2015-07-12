package com.quark.ui.widget;

import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;
import com.quark.db.CityDB;

@SuppressLint("NewApi")
public class ActionSheet {
	static String sexstr;
	static String xueliStr;
	static LinearLayout layout;
	static Context context;

	public interface OnActionSheetSelected {
		void onClick(int whichButton);
	}

	private ActionSheet() {
	}

	public static Dialog showSheetPic(Context context,
			final OnActionSheetSelected actionSheetSelected,
			OnCancelListener cancelListener) {
		final Dialog dlg = new Dialog(context, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.actionsheet, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		TextView mContent = (TextView) layout.findViewById(R.id.content);
		TextView mCancel = (TextView) layout.findViewById(R.id.cancel);
		TextView mTitle = (TextView) layout.findViewById(R.id.title);

		mTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				actionSheetSelected.onClick(0);

				dlg.dismiss();
			}
		});
		mContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				actionSheetSelected.onClick(1);

				dlg.dismiss();
			}
		});

		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				actionSheetSelected.onClick(2);
				dlg.dismiss();
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(false);
		if (cancelListener != null)
			dlg.setOnCancelListener(cancelListener);

		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}

	public static Dialog showSheetSex(final Context context,
			final OnActionSheetSelected actionSheetSelected,
			OnCancelListener cancelListener, final TextView view) {
		final Dialog dlg = new Dialog(context, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.actionsheetsex, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		final TextView sex_man = (TextView) layout.findViewById(R.id.sex_man);
		final TextView sex_lady = (TextView) layout.findViewById(R.id.sex_lady);
		ImageView submit = (ImageView) layout.findViewById(R.id.submit);

		sex_man.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				settatus(context, sex_man, sex_lady);
				sexstr = "男性";
			}
		});
		sex_lady.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				settatus(context, sex_lady, sex_man);
				sexstr = "女性";
			}
		});
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!"".equals(sexstr)) {
					view.setText(sexstr);
					view.setTextColor(context.getResources().getColor(
							R.color.ziti_black));
					dlg.dismiss();
				}
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		if (cancelListener != null)
			dlg.setOnCancelListener(cancelListener);

		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}

	public static Dialog showSheetTime(final Context context,
			final OnActionSheetSelected actionSheetSelected,
			OnCancelListener cancelListener, final TextView view) {
		final Dialog dlg = new Dialog(context, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.actionsheettime, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		ImageView submit = (ImageView) layout.findViewById(R.id.submit);
		final DatePicker datePicker = (DatePicker) layout
				.findViewById(R.id.date);

		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StringBuffer sb = new StringBuffer();
				sb.append(String.format("%d-%02d-%02d", datePicker.getYear(),
						datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
				view.setText(sb.toString());
				view.setTextColor(context.getResources().getColor(
						R.color.ziti_black));
				dlg.dismiss();
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		if (cancelListener != null)
			dlg.setOnCancelListener(cancelListener);

		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}

	/**
	 * 发布兼职 必须是今天之后
	 * 
	 * @param context
	 * @param actionSheetSelected
	 * @param cancelListener
	 * @param view
	 * @return
	 */
	public static Dialog showSheetTime4(final Context context,
			final OnActionSheetSelected actionSheetSelected,
			OnCancelListener cancelListener, final TextView view) {
		final Dialog dlg = new Dialog(context, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.actionsheettime, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		ImageView submit = (ImageView) layout.findViewById(R.id.submit);
		final DatePicker datePicker = (DatePicker) layout
				.findViewById(R.id.date);

		Date sd = new Date();
		datePicker.setMinDate(sd.getTime() - 1000);

		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StringBuffer sb = new StringBuffer();
				sb.append(String.format("%d-%02d-%02d", datePicker.getYear(),
						datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
				view.setText(sb.toString());
				view.setTextColor(context.getResources().getColor(
						R.color.ziti_black));
				dlg.dismiss();
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		if (cancelListener != null)
			dlg.setOnCancelListener(cancelListener);

		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}

	public static Dialog showSheetBorthdayTime(final Context context,
			final OnActionSheetSelected actionSheetSelected,
			OnCancelListener cancelListener, final TextView view) {
		final Dialog dlg = new Dialog(context, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.actionsheettime, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		ImageView submit = (ImageView) layout.findViewById(R.id.submit);
		final DatePicker datePicker = (DatePicker) layout
				.findViewById(R.id.date);
		Date d = new Date();
		long longtime = d.getTime();
		datePicker.setMaxDate(longtime);

		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StringBuffer sb = new StringBuffer();
				sb.append(String.format("%d-%02d-%02d", datePicker.getYear(),
						datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
				view.setText(sb.toString());
				view.setTextColor(context.getResources().getColor(
						R.color.ziti_black));
				dlg.dismiss();
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		if (cancelListener != null)
			dlg.setOnCancelListener(cancelListener);

		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}

	/**
	 * 字体为橙色
	 * 
	 * @param context
	 * @param actionSheetSelected
	 * @param cancelListener
	 * @param view
	 * @return
	 */
	public static Dialog showSheetTime2(final Context context,
			final OnActionSheetSelected actionSheetSelected,
			OnCancelListener cancelListener, final TextView view) {
		final Dialog dlg = new Dialog(context, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.actionsheettime, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		ImageView submit = (ImageView) layout.findViewById(R.id.submit);
		final DatePicker datePicker = (DatePicker) layout
				.findViewById(R.id.date);

		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StringBuffer sb = new StringBuffer();
				sb.append(String.format("%d-%02d-%02d", datePicker.getYear(),
						datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
				view.setText(sb.toString());
				view.setTextColor(context.getResources().getColor(
						R.color.ziti_orange));
				dlg.dismiss();
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		if (cancelListener != null)
			dlg.setOnCancelListener(cancelListener);

		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}

	/**
	 * 广场筛选显示为当前日期
	 * 
	 * @param context
	 * @param actionSheetSelected
	 * @param cancelListener
	 * @param view
	 * @return
	 */
	// public static Dialog showSheetTime3(final Context context, final
	// OnActionSheetSelected actionSheetSelected,
	// OnCancelListener cancelListener,final TextView view) {
	//
	// final Dialog dlg = new Dialog(context, R.style.ActionSheet);
	// LayoutInflater inflater = (LayoutInflater)
	// context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	// LinearLayout layout = (LinearLayout)
	// inflater.inflate(R.layout.actionsheettime, null);
	// final int cFullFillWidth = 10000;
	// layout.setMinimumWidth(cFullFillWidth);
	// ImageView submit = (ImageView) layout.findViewById(R.id.submit);
	// final DatePicker datePicker = (DatePicker)
	// layout.findViewById(R.id.date);
	//
	//
	//
	// submit.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// StringBuffer sb = new StringBuffer();
	// sb.append(String.format("%d-%02d-%02d",
	// datePicker.getYear(),
	// datePicker.getMonth() + 1,
	// datePicker.getDayOfMonth()));
	// view.setText(sb.toString());
	// view.setTextColor(context.getResources().getColor(R.color.ziti_orange));
	// dlg.dismiss();
	// }
	// });
	//
	// Window w = dlg.getWindow();
	// WindowManager.LayoutParams lp = w.getAttributes();
	// lp.x = 0;
	// final int cMakeBottom = -1000;
	// lp.y = cMakeBottom;
	// lp.gravity = Gravity.BOTTOM;
	// dlg.onWindowAttributesChanged(lp);
	// dlg.setCanceledOnTouchOutside(true);
	// if (cancelListener != null)
	// dlg.setOnCancelListener(cancelListener);
	//
	// dlg.setContentView(layout);
	// dlg.show();
	//
	// return dlg;
	// }
	/**
	 * xueli
	 * 
	 * @param context
	 * @param actionSheetSelected
	 * @param cancelListener
	 * @param view
	 * @return
	 */
	public static Dialog showSheetXueli(final Context context,
			final OnActionSheetSelected actionSheetSelected,
			OnCancelListener cancelListener, final TextView view) {
		xueliStr = "";
		final Dialog dlg = new Dialog(context, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = (LinearLayout) inflater.inflate(R.layout.actionsheetxueli,
				null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		TextView xueli_benke = (TextView) layout.findViewById(R.id.xueli_benke);
		xueli_benke.setOnClickListener(xueliOnclick);
		TextView xueli_boshi = (TextView) layout.findViewById(R.id.xueli_boshi);
		xueli_boshi.setOnClickListener(xueliOnclick);
		TextView xueli_chuzhong = (TextView) layout
				.findViewById(R.id.xueli_chuzhong);
		xueli_chuzhong.setOnClickListener(xueliOnclick);
		TextView xueli_dazhuan = (TextView) layout
				.findViewById(R.id.xueli_dazhuan);
		xueli_dazhuan.setOnClickListener(xueliOnclick);
		TextView xueli_gaozhong = (TextView) layout
				.findViewById(R.id.xueli_gaozhong);
		xueli_gaozhong.setOnClickListener(xueliOnclick);
		TextView xueli_jixiao = (TextView) layout
				.findViewById(R.id.xueli_jixiao);
		xueli_jixiao.setOnClickListener(xueliOnclick);
		TextView xueli_shuoshi = (TextView) layout
				.findViewById(R.id.xueli_shuoshi);
		xueli_shuoshi.setOnClickListener(xueliOnclick);
		TextView xueli_xiaoxue = (TextView) layout
				.findViewById(R.id.xueli_xiaoxue);
		xueli_xiaoxue.setOnClickListener(xueliOnclick);
		TextView xueli_zhiyegaozhong = (TextView) layout
				.findViewById(R.id.xueli_zhiyegaozhong);
		xueli_zhiyegaozhong.setOnClickListener(xueliOnclick);
		TextView xueli_zhongzhuan = (TextView) layout
				.findViewById(R.id.xueli_zhongzhuan);
		xueli_zhongzhuan.setOnClickListener(xueliOnclick);

		ImageView submit = (ImageView) layout.findViewById(R.id.submit);
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!"".equals(xueliStr)) {
					view.setText(xueliStr);
					view.setTextColor(context.getResources().getColor(
							R.color.ziti_black));
					dlg.dismiss();
				}
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		if (cancelListener != null)
			dlg.setOnCancelListener(cancelListener);
		dlg.setContentView(layout);
		dlg.show();
		return dlg;
	}

	static OnClickListener xueliOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			xueliOnclickDone(v.getId());
		}
	};

	public static void xueliOnclickDone(int id) {
		ArrayList<TextView> views = new ArrayList<TextView>();
		views.add((TextView) layout.findViewById(R.id.xueli_benke));
		views.add((TextView) layout.findViewById(R.id.xueli_boshi));
		views.add((TextView) layout.findViewById(R.id.xueli_chuzhong));
		views.add((TextView) layout.findViewById(R.id.xueli_dazhuan));
		views.add((TextView) layout.findViewById(R.id.xueli_gaozhong));
		views.add((TextView) layout.findViewById(R.id.xueli_jixiao));
		views.add((TextView) layout.findViewById(R.id.xueli_shuoshi));
		views.add((TextView) layout.findViewById(R.id.xueli_xiaoxue));
		views.add((TextView) layout.findViewById(R.id.xueli_zhiyegaozhong));
		views.add((TextView) layout.findViewById(R.id.xueli_zhongzhuan));

		switch (id) {
		case R.id.xueli_benke:
			views.remove(0);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.xueli_benke), views);
			xueliStr = "本科";
			break;
		case R.id.xueli_boshi:
			views.remove(1);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.xueli_boshi), views);
			xueliStr = "博士";
			break;
		case R.id.xueli_chuzhong:
			views.remove(2);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.xueli_chuzhong), views);
			xueliStr = "初中";
			break;
		case R.id.xueli_dazhuan:
			views.remove(3);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.xueli_dazhuan), views);
			xueliStr = "大专";
			break;
		case R.id.xueli_gaozhong:
			views.remove(4);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.xueli_gaozhong), views);
			xueliStr = "高中";
			break;
		case R.id.xueli_jixiao:
			views.remove(5);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.xueli_jixiao), views);
			xueliStr = "技校";
			break;
		case R.id.xueli_shuoshi:
			views.remove(6);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.xueli_shuoshi), views);
			xueliStr = "硕士";
			break;
		case R.id.xueli_xiaoxue:
			views.remove(7);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.xueli_xiaoxue), views);
			xueliStr = "小学";
			break;
		case R.id.xueli_zhiyegaozhong:
			views.remove(8);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.xueli_zhiyegaozhong),
					views);
			xueliStr = "专业高中";
			break;
		case R.id.xueli_zhongzhuan:
			views.remove(9);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.xueli_zhongzhuan),
					views);
			xueliStr = "中专";
			break;
		default:
			break;
		}

	}

	/**
	 * 是否有健康证
	 * 
	 * @param context
	 * @param actionSheetSelected
	 * @param cancelListener
	 * @param view
	 * @return
	 */
	public static Dialog showSheetJianKZ(final Context context,
			final OnActionSheetSelected actionSheetSelected,
			OnCancelListener cancelListener, final TextView view) {
		sexstr = "";
		final Dialog dlg = new Dialog(context, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.actionsheetsex, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		final TextView title = (TextView) layout.findViewById(R.id.title);
		title.setText("是否有健康证");

		final TextView sex_man = (TextView) layout.findViewById(R.id.sex_man);
		sex_man.setText("有");
		final TextView sex_lady = (TextView) layout.findViewById(R.id.sex_lady);
		sex_lady.setText("无");
		ImageView submit = (ImageView) layout.findViewById(R.id.submit);

		sex_man.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				settatus(context, sex_man, sex_lady);
				sexstr = "有";
			}
		});
		sex_lady.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				settatus(context, sex_lady, sex_man);
				sexstr = "无";
			}
		});
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!"".equals(sexstr)) {
					view.setText(sexstr);
					view.setTextColor(context.getResources().getColor(
							R.color.ziti_black));
					dlg.dismiss();
				}
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		if (cancelListener != null)
			dlg.setOnCancelListener(cancelListener);

		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}

	// ============yi fu chi ma start=========

	/**
	 * 衣服尺码
	 * 
	 * @param context
	 * @param actionSheetSelected
	 * @param cancelListener
	 * @param view
	 * @return
	 */
	public static Dialog showSheetYiFZM(final Context context,
			final OnActionSheetSelected actionSheetSelected,
			OnCancelListener cancelListener, final TextView view) {
		xueliStr = "";
		final Dialog dlg = new Dialog(context, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = (LinearLayout) inflater.inflate(R.layout.actionsheetyifuchima,
				null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		TextView xueli_benke = (TextView) layout.findViewById(R.id.one);
		xueli_benke.setOnClickListener(yifuOnclick);
		TextView xueli_boshi = (TextView) layout.findViewById(R.id.two);
		xueli_boshi.setOnClickListener(yifuOnclick);
		TextView xueli_chuzhong = (TextView) layout.findViewById(R.id.three);
		xueli_chuzhong.setOnClickListener(yifuOnclick);
		TextView xueli_dazhuan = (TextView) layout.findViewById(R.id.four);
		xueli_dazhuan.setOnClickListener(yifuOnclick);
		TextView xueli_gaozhong = (TextView) layout.findViewById(R.id.five);
		xueli_gaozhong.setOnClickListener(yifuOnclick);
		TextView xueli_jixiao = (TextView) layout.findViewById(R.id.six);
		xueli_jixiao.setOnClickListener(yifuOnclick);

		ImageView submit = (ImageView) layout.findViewById(R.id.submit);
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!"".equals(xueliStr)) {
					view.setText(xueliStr);
					view.setTextColor(context.getResources().getColor(
							R.color.ziti_black));
					dlg.dismiss();
				}
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		if (cancelListener != null)
			dlg.setOnCancelListener(cancelListener);
		dlg.setContentView(layout);
		dlg.show();
		return dlg;
	}

	static OnClickListener yifuOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			yifuOnclickDone(v.getId());
		}
	};

	public static void yifuOnclickDone(int id) {
		ArrayList<TextView> views = new ArrayList<TextView>();
		views.add((TextView) layout.findViewById(R.id.one));
		views.add((TextView) layout.findViewById(R.id.two));
		views.add((TextView) layout.findViewById(R.id.three));
		views.add((TextView) layout.findViewById(R.id.four));
		views.add((TextView) layout.findViewById(R.id.five));
		views.add((TextView) layout.findViewById(R.id.six));

		switch (id) {
		case R.id.one:
			views.remove(0);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.one), views);
			xueliStr = "S";
			break;
		case R.id.two:
			views.remove(1);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.two), views);
			xueliStr = "M";
			break;
		case R.id.three:
			views.remove(2);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.three), views);
			xueliStr = "L";
			break;
		case R.id.four:
			views.remove(3);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.four), views);
			xueliStr = "XL";
			break;
		case R.id.five:
			views.remove(4);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.five), views);
			xueliStr = "XXL";
			break;
		case R.id.six:
			views.remove(5);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.six), views);
			xueliStr = "XXXL";
			break;
		default:
			break;
		}
	}

	// ===========yi fu chi ma======================
	// ============shoos start=========

	/**
	 * 鞋尺码
	 * 
	 * @param context
	 * @param actionSheetSelected
	 * @param cancelListener
	 * @param view
	 * @return
	 */
	public static Dialog showSheetShoos(final Context context,
			final OnActionSheetSelected actionSheetSelected,
			OnCancelListener cancelListener, final TextView view) {
		xueliStr = "";
		final Dialog dlg = new Dialog(context, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = (LinearLayout) inflater
				.inflate(R.layout.actionsheetshoo, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		TextView xueli_benke = (TextView) layout.findViewById(R.id.one);
		xueli_benke.setOnClickListener(shoseOnclick);
		TextView xueli_boshi = (TextView) layout.findViewById(R.id.two);
		xueli_boshi.setOnClickListener(shoseOnclick);
		TextView xueli_chuzhong = (TextView) layout.findViewById(R.id.three);
		xueli_chuzhong.setOnClickListener(shoseOnclick);
		TextView xueli_dazhuan = (TextView) layout.findViewById(R.id.four);
		xueli_dazhuan.setOnClickListener(shoseOnclick);
		TextView xueli_gaozhong = (TextView) layout.findViewById(R.id.five);
		xueli_gaozhong.setOnClickListener(shoseOnclick);
		TextView xueli_jixiao = (TextView) layout.findViewById(R.id.six);
		xueli_jixiao.setOnClickListener(shoseOnclick);
		TextView seven = (TextView) layout.findViewById(R.id.seven);
		seven.setOnClickListener(shoseOnclick);
		TextView eight = (TextView) layout.findViewById(R.id.eight);
		eight.setOnClickListener(shoseOnclick);
		TextView nine = (TextView) layout.findViewById(R.id.nine);
		nine.setOnClickListener(shoseOnclick);
		TextView ten = (TextView) layout.findViewById(R.id.ten);
		ten.setOnClickListener(shoseOnclick);
		TextView eleven = (TextView) layout.findViewById(R.id.eleven);
		eleven.setOnClickListener(shoseOnclick);
		TextView twelve = (TextView) layout.findViewById(R.id.twelve);
		twelve.setOnClickListener(shoseOnclick);
		TextView thirteen = (TextView) layout.findViewById(R.id.thirteen);
		thirteen.setOnClickListener(shoseOnclick);

		ImageView submit = (ImageView) layout.findViewById(R.id.submit);
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!"".equals(xueliStr)) {
					view.setText(xueliStr);
					view.setTextColor(context.getResources().getColor(
							R.color.ziti_black));
					dlg.dismiss();
				}
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		if (cancelListener != null)
			dlg.setOnCancelListener(cancelListener);
		dlg.setContentView(layout);
		dlg.show();
		return dlg;
	}

	static OnClickListener shoseOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			shoseOnclickDone(v.getId());
		}
	};

	public static void shoseOnclickDone(int id) {
		ArrayList<TextView> views = new ArrayList<TextView>();
		views.add((TextView) layout.findViewById(R.id.one));
		views.add((TextView) layout.findViewById(R.id.two));
		views.add((TextView) layout.findViewById(R.id.three));
		views.add((TextView) layout.findViewById(R.id.four));
		views.add((TextView) layout.findViewById(R.id.five));
		views.add((TextView) layout.findViewById(R.id.six));
		views.add((TextView) layout.findViewById(R.id.seven));
		views.add((TextView) layout.findViewById(R.id.eight));
		views.add((TextView) layout.findViewById(R.id.nine));
		views.add((TextView) layout.findViewById(R.id.ten));
		views.add((TextView) layout.findViewById(R.id.eleven));
		views.add((TextView) layout.findViewById(R.id.twelve));
		views.add((TextView) layout.findViewById(R.id.thirteen));
		switch (id) {
		case R.id.one:
			views.remove(0);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.one), views);
			xueliStr = "33";
			break;
		case R.id.two:
			views.remove(1);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.two), views);
			xueliStr = "34";
			break;
		case R.id.three:
			views.remove(2);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.three), views);
			xueliStr = "35";
			break;
		case R.id.four:
			views.remove(3);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.four), views);
			xueliStr = "36";
			break;
		case R.id.five:
			views.remove(4);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.five), views);
			xueliStr = "37";
			break;
		case R.id.six:
			views.remove(5);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.six), views);
			xueliStr = "38";
			break;
		case R.id.seven:
			views.remove(6);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.seven), views);
			xueliStr = "39";
			break;
		case R.id.eight:
			views.remove(7);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.eight), views);
			xueliStr = "40";
			break;
		case R.id.nine:
			views.remove(8);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.nine), views);
			xueliStr = "41";
			break;
		case R.id.ten:
			views.remove(9);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.ten), views);
			xueliStr = "42";
			break;
		case R.id.eleven:
			views.remove(10);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.eleven), views);
			xueliStr = "43";
			break;
		case R.id.twelve:
			views.remove(11);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.twelve), views);
			xueliStr = "44";
			break;
		case R.id.thirteen:
			views.remove(12);
			settatus(layout.getContext(),
					(TextView) layout.findViewById(R.id.thirteen), views);
			xueliStr = "45";
			break;
		default:
			break;
		}
	}

	// ===========yi fu chi ma======================
	// ============shoos start=========

	/**
	 * 语言码
	 * 
	 * @param context
	 * @param actionSheetSelected
	 * @param cancelListener
	 * @param view
	 * @return
	 */
	public static Dialog showSheetLanguage(final Context context,
			final OnActionSheetSelected actionSheetSelected,
			OnCancelListener cancelListener, final TextView view) {
		final Dialog dlg = new Dialog(context, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = (LinearLayout) inflater.inflate(R.layout.actionsheetlanguage,
				null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		for (int i = 0; i < 10; i++) {
			language[i] = "";
		}
		TextView xueli_benke = (TextView) layout.findViewById(R.id.xueli_benke);
		xueli_benke.setOnClickListener(languageOnclick);
		TextView xueli_boshi = (TextView) layout.findViewById(R.id.xueli_boshi);
		xueli_boshi.setOnClickListener(languageOnclick);
		TextView xueli_chuzhong = (TextView) layout
				.findViewById(R.id.xueli_chuzhong);
		xueli_chuzhong.setOnClickListener(languageOnclick);
		TextView xueli_dazhuan = (TextView) layout
				.findViewById(R.id.xueli_dazhuan);
		xueli_dazhuan.setOnClickListener(languageOnclick);
		TextView xueli_gaozhong = (TextView) layout
				.findViewById(R.id.xueli_gaozhong);
		xueli_gaozhong.setOnClickListener(languageOnclick);
		TextView xueli_jixiao = (TextView) layout
				.findViewById(R.id.xueli_jixiao);
		xueli_jixiao.setOnClickListener(languageOnclick);
		TextView xueli_shuoshi = (TextView) layout
				.findViewById(R.id.xueli_shuoshi);
		xueli_shuoshi.setOnClickListener(languageOnclick);
		TextView xueli_xiaoxue = (TextView) layout
				.findViewById(R.id.xueli_xiaoxue);
		xueli_xiaoxue.setOnClickListener(languageOnclick);
		TextView xueli_zhiyegaozhong = (TextView) layout
				.findViewById(R.id.xueli_zhiyegaozhong);
		xueli_zhiyegaozhong.setOnClickListener(languageOnclick);
		TextView xueli_zhongzhuan = (TextView) layout
				.findViewById(R.id.xueli_zhongzhuan);
		xueli_zhongzhuan.setOnClickListener(languageOnclick);

		ImageView submit = (ImageView) layout.findViewById(R.id.submit);
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String languageStr = "";
				for (int i = 0; i < 10; i++) {
					if (!language[i].equals("")) {
						languageStr += language[i] + "、";
					}
				}
				if (languageStr.endsWith("、")) {
					languageStr = languageStr.substring(0,
							languageStr.length() - 1);
				}
				if (!"".equals(languageStr)) {
					view.setText(languageStr);
					view.setTextColor(context.getResources().getColor(
							R.color.ziti_black));
					dlg.dismiss();
				}
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		if (cancelListener != null)
			dlg.setOnCancelListener(cancelListener);
		dlg.setContentView(layout);
		dlg.show();
		return dlg;
	}

	static OnClickListener languageOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			languageOnclickDone(v.getId());
		}
	};

	static String[] language = { "", "", "", "", "", "", "", "", "", "" };

	public static void languageOnclickDone(int id) {

		switch (id) {
		case R.id.xueli_benke:
			if (language[0].equals("")) {
				language[0] = "意大利语";
				setstatusLanguage(layout.getContext(),
						(TextView) layout.findViewById(R.id.xueli_benke), true);
			} else {
				language[0] = "";
				setstatusLanguage(layout.getContext(),
						(TextView) layout.findViewById(R.id.xueli_benke), false);
			}
			break;
		case R.id.xueli_boshi:
			if (language[1].equals("")) {
				language[1] = "阿拉伯语";
				setstatusLanguage(layout.getContext(),
						(TextView) layout.findViewById(R.id.xueli_boshi), true);
			} else {
				language[1] = "";
				setstatusLanguage(layout.getContext(),
						(TextView) layout.findViewById(R.id.xueli_boshi), false);
			}
			break;
		case R.id.xueli_chuzhong:
			if (language[2].equals("")) {
				language[2] = "粤语";
				setstatusLanguage(layout.getContext(),
						(TextView) layout.findViewById(R.id.xueli_chuzhong),
						true);
			} else {
				language[2] = "";
				setstatusLanguage(layout.getContext(),
						(TextView) layout.findViewById(R.id.xueli_chuzhong),
						false);
			}
			break;
		case R.id.xueli_dazhuan:
			if (language[3].equals("")) {
				language[3] = "日语";
				setstatusLanguage(layout.getContext(),
						(TextView) layout.findViewById(R.id.xueli_dazhuan),
						true);
			} else {
				language[3] = "";
				setstatusLanguage(layout.getContext(),
						(TextView) layout.findViewById(R.id.xueli_dazhuan),
						false);
			}
			break;
		case R.id.xueli_gaozhong:
			if (language[4].equals("")) {
				language[4] = "英语";
				setstatusLanguage(layout.getContext(),
						(TextView) layout.findViewById(R.id.xueli_gaozhong),
						true);
			} else {
				language[4] = "";
				setstatusLanguage(layout.getContext(),
						(TextView) layout.findViewById(R.id.xueli_gaozhong),
						false);
			}
			break;
		case R.id.xueli_jixiao:
			if (language[5].equals("")) {
				language[5] = "韩语";
				setstatusLanguage(layout.getContext(),
						(TextView) layout.findViewById(R.id.xueli_jixiao), true);
			} else {
				language[5] = "";
				setstatusLanguage(layout.getContext(),
						(TextView) layout.findViewById(R.id.xueli_jixiao),
						false);
			}
			break;
		case R.id.xueli_shuoshi:
			if (language[6].equals("")) {
				language[6] = "俄语";
				setstatusLanguage(layout.getContext(),
						(TextView) layout.findViewById(R.id.xueli_shuoshi),
						true);
			} else {
				language[6] = "";
				setstatusLanguage(layout.getContext(),
						(TextView) layout.findViewById(R.id.xueli_shuoshi),
						false);
			}
			break;
		case R.id.xueli_xiaoxue:
			if (language[7].equals("")) {
				language[7] = "普通话";
				setstatusLanguage(layout.getContext(),
						(TextView) layout.findViewById(R.id.xueli_xiaoxue),
						true);
			} else {
				language[7] = "";
				setstatusLanguage(layout.getContext(),
						(TextView) layout.findViewById(R.id.xueli_xiaoxue),
						false);
			}
			break;
		case R.id.xueli_zhiyegaozhong:
			if (language[8].equals("")) {
				language[8] = "德语";
				setstatusLanguage(layout.getContext(),
						(TextView) layout
								.findViewById(R.id.xueli_zhiyegaozhong), true);
			} else {
				language[8] = "";
				setstatusLanguage(layout.getContext(),
						(TextView) layout
								.findViewById(R.id.xueli_zhiyegaozhong), false);
			}
			break;
		case R.id.xueli_zhongzhuan:
			if (language[9].equals("")) {
				language[9] = "法语";
				setstatusLanguage(layout.getContext(),
						(TextView) layout.findViewById(R.id.xueli_zhongzhuan),
						true);
			} else {
				language[9] = "";
				setstatusLanguage(layout.getContext(),
						(TextView) layout.findViewById(R.id.xueli_zhongzhuan),
						false);
			}
			break;
		default:
			break;
		}
	}

	public static void settatus(Context context, TextView onview,
			TextView disonView) {
		Resources resources = context.getResources();
		Drawable btnDrawable = resources.getDrawable(R.color.saixuan_jiesuan);
		onview.setBackgroundDrawable(btnDrawable);
		onview.setTextColor(context.getResources().getColor(R.color.body_color));

		Drawable btnDrawable2 = resources.getDrawable(R.drawable.bord_saixun);
		disonView.setBackgroundDrawable(btnDrawable2);
		disonView.setTextColor(context.getResources().getColor(
				R.color.ziti_huise));
	}

	public static void settatus(Context context, TextView onview,
			ArrayList<TextView> d) {
		Resources resources = context.getResources();
		Drawable btnDrawable;
		btnDrawable = resources.getDrawable(R.color.saixuan_jiesuan);
		onview.setBackgroundDrawable(btnDrawable);
		onview.setTextColor(context.getResources().getColor(R.color.body_color));
		// ArrayList<TextView> d = new ArrayList<TextView>();
		for (int i = 0; i < d.size(); i++) {
			btnDrawable = resources.getDrawable(R.drawable.bord_saixun);
			d.get(i).setBackgroundDrawable(btnDrawable);
			d.get(i).setTextColor(
					context.getResources().getColor(R.color.ziti_huise));
		}
	}

	public static void setstatusLanguage(Context context, TextView onview,
			boolean isChoose) {
		Resources resources = context.getResources();
		Drawable btnDrawable;
		if (isChoose) {
			btnDrawable = resources.getDrawable(R.color.saixuan_jiesuan);
			onview.setBackgroundDrawable(btnDrawable);
			onview.setTextColor(context.getResources().getColor(
					R.color.body_color));
		} else {
			btnDrawable = resources.getDrawable(R.drawable.bord_saixun);
			onview.setBackgroundDrawable(btnDrawable);
			onview.setTextColor(context.getResources().getColor(
					R.color.ziti_huise));
		}
	}

	static String country = "";
	static ArrayList<TextView> d = new ArrayList<TextView>();;

	// 地区
	public static Dialog showSheetCountry(final Context context,
			final OnActionSheetSelected actionSheetSelected,
			OnCancelListener cancelListener, final TextView view) {
		final Dialog dlg = new Dialog(context, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.actionsheetcountry, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		SharedPreferences sp = context.getSharedPreferences("jrdr.setting",
				android.content.Context.MODE_PRIVATE);
		String city = sp.getString("city", "深圳");
		ArrayList<String> countrys = new ArrayList<String>();
		countrys = CityDB.getCitys(context, city);
		country = countrys.get(0);
		TableLayout didian_layout = (TableLayout) layout
				.findViewById(R.id.didian_table);
		SaixuanUi.initDidian(context, countrys, didian_layout, countryOnclick,
				5);
		for (int i = 0; i < countrys.size(); i++) {
			TextView textView = (TextView) layout.findViewById(i + 5001);
			d.add(textView);
		}

		ImageView submit = (ImageView) layout.findViewById(R.id.submit);

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				view.setText(country);
				view.setTextColor(context.getResources().getColor(
						R.color.ziti_black));
				dlg.dismiss();
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		if (cancelListener != null)
			dlg.setOnCancelListener(cancelListener);

		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}

	static OnClickListener countryOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Resources resources = v.getContext().getResources();
			Drawable btnDrawable;
			for (int i = 0; i < d.size(); i++) {
				btnDrawable = resources.getDrawable(R.drawable.bord_saixun);
				d.get(i).setBackgroundDrawable(btnDrawable);
				d.get(i).setTextColor(resources.getColor(R.color.ziti_huise));
			}
			country = ((TextView) v).getText().toString();
			btnDrawable = resources.getDrawable(R.color.saixuan_sjian);
			v.setBackgroundDrawable(btnDrawable);
			((TextView) v).setTextColor(resources.getColor(R.color.body_color));
		}
	};
}
