package com.quark.company.function;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.qingmu.jianzhidaren.R;
import com.quark.adapter.FullStarffedAdapter;
import com.quark.common.JsonUtil;
import com.quark.common.Url;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.jianzhidaren.MainCompanyActivity;
import com.quark.model.RosterUser;
import com.quark.model.SignPersonList;

/**
 * 管理员：签到人员列表
 * 
 * @author Administrator
 * 
 */
public class SignPersonActivity extends BaseActivity {

	private ImageView im;
	private Button btn_code, btn_pen;
	LinearLayout codeLayout, penLayout;
	// 参数
	FullStarffedAdapter fullStarffedAdapter;
	private ListView listView;
	private String company_id, activity_id;
	private String sign_url, signUpList_url;
	private String activity_name, total;
	ArrayList<RosterUser> list = new ArrayList<RosterUser>();
	ArrayList<SignPersonList> signPersonList = new ArrayList<SignPersonList>();
	// //
	private ImageView qrImgImageView;
	Bitmap mBitmap;
	public int status = 0;
	// 图片宽度的一般
	private static final int IMAGE_HALFWIDTH = 50;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_person_sign_yesorno);
		setBackButton();
		setRightImage(R.id.right, listener);
		// 处理数据
		activity_name = getIntent().getStringExtra("activity_name");
		activity_id = getIntent().getStringExtra("activity_id");
		SharedPreferences sp = getSharedPreferences("jrdr.setting",
				Activity.MODE_PRIVATE);
		company_id = sp.getString("userId", "");
		//
		im = (ImageView) findViewById(R.id.right);
		Resources res = this.getResources();
		Drawable btnDrawable = res.getDrawable(R.drawable.nav_btn_refresh);
		im.setBackgroundDrawable(btnDrawable);
		// 评价
		btn_pen = (Button) findViewById(R.id.roster_pen);
		penLayout = (LinearLayout) findViewById(R.id.penLayout);
		penLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(SignPersonActivity.this,
						PersonAssessActivity.class);
				intent.putExtra("activity_id", activity_id + "");
				intent.putExtra("total_num", total);
				startActivity(intent);
			}
		});

		// 二维码签到
		btn_code = (Button) findViewById(R.id.roster_code);
		codeLayout = (LinearLayout) findViewById(R.id.codeLayout);
		codeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					initPopWindow();
				} catch (WriterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		setTopTitle(activity_name);
		sign_url = Url.COMPANY_sign + "?token=" + MainCompanyActivity.token
				+ "&company_id=" + company_id + "&activity_id=" + activity_id;
		signUpList_url = Url.COMPANY_signUpList + "?token="
				+ MainCompanyActivity.token;

	}

	/*
	 * 点击头顶刷新
	 */
	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Refresh();
		}
	};

	// 二维码签到刷新
	protected void Refresh() {
		// TODO Auto-generated method stub
		showWait(true);
		StringRequest stringRequest = new StringRequest(Method.POST, sign_url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						showWait(false);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						showWait(false);
						Log.e("TAG", arg0.getMessage(), arg0);
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> map = new HashMap<String, String>();
				map.put("activity_id", activity_id);
				map.put("company_id", company_id);
				return map;
			}
		};
		queue.add(stringRequest);
		stringRequest
				.setRetryPolicy(new DefaultRetryPolicy(ConstantForSaveList.DEFAULTRETRYTIME*1000, 1, 1.0f));

	}

	private void initPopWindow() throws WriterException {
		// 加载popupWindow的布局文件
		final View contentView = LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.alert_dialog_code, null);
		qrImgImageView = (ImageView) contentView
				.findViewById(R.id.qiandao_code);
		Resources r = getResources();
		// 生成二维码
		// res= getResources();activity的方法上下文获取
		mBitmap = BitmapFactory.decodeResource(r, R.drawable.code_logo);
		// 缩放图片
		Matrix m = new Matrix();
		float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
		float sy = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getHeight();
		m.setScale(sx, sy);
		// 重新构造一个40*40的图片
		mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
				mBitmap.getHeight(), m, false);

		// mBitmap = EncodingHandler.createQRCode(url, 420);//生成二维码
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);

		try {
			int width = wm.getDefaultDisplay().getWidth();// 屏幕
			int height = wm.getDefaultDisplay().getHeight();
			mBitmap = cretaeBitmap(
					new String(sign_url.getBytes(), "ISO-8859-1"),
					width * 3 / 4);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		qrImgImageView.setImageBitmap(mBitmap);

		// 声明一个弹出框
		final PopupWindow popupWindow = new PopupWindow(
				findViewById(R.id.pop_layout), LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		// 为弹出框设定自定义的布局
		popupWindow.setContentView(contentView);

		Button button_sure = (Button) contentView.findViewById(R.id.btn_cancel);
		button_sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				status = 1;
				signStaff();
			}
		});

		contentView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				int height = contentView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {// 把小于号 改为大于号，轻触其他地方都可以退出二维码
						popupWindow.dismiss();
					}
				}
				return true;
			}
		});

		// 设置SelectPicPopupWindow弹出窗体可点击
		popupWindow.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		popupWindow.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		popupWindow.setBackgroundDrawable(dw);
		popupWindow.showAtLocation(
				SignPersonActivity.this.findViewById(R.id.yes_no_full_main),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

	}

	/**
	 * 生成二维码
	 * 
	 * @param 字符串
	 * @return Bitmap
	 * @throws WriterException
	 */
	public Bitmap cretaeBitmap(String str, int widthAndHeight)
			throws WriterException {

		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		// BitMatrix matrix= new MultiFormatWriter().encode(str,
		// BarcodeFormat.QR_CODE, 300, 300);
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight, hints);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// 二维矩阵转为一维像素数组,也就是一直横着排了,logo位于中间
		int halfW = width / 2;
		int halfH = height / 2;
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
						&& y > halfH - IMAGE_HALFWIDTH
						&& y < halfH + IMAGE_HALFWIDTH) {
					pixels[y * width + x] = mBitmap.getPixel(x - halfW
							+ IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
				} else {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = 0xff000000;
					} else { // 无信息设置像素点为白色
						pixels[y * width + x] = 0xffffffff;
					}
				}

			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

		return bitmap;
	}

	// 签到人员列表
	private void signStaff() {
		// TODO Auto-generated method stub
		showWait(true);
		StringRequest stringRequest = new StringRequest(Method.POST,
				signUpList_url, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						Log.e("test1", "返回dd的数据" + response);
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss = js
									.getJSONObject("ActivitySignList");
							JSONArray jsss = jss.getJSONArray("list");
							if (jsss.length() > 0) {
								for (int i = 0; i < jsss.length(); i++) {
									SignPersonList rt = new SignPersonList();
									rt = (SignPersonList) JsonUtil.jsonToBean(
											jsss.getJSONObject(i),
											SignPersonList.class);
									signPersonList.add(rt);
								}
							}
							initSignPerson();
							Message msg = handleSign.obtainMessage();
							msg.what = 1;
							handleSign.sendMessage(msg);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						showWait(false);
						Log.e("TAG", arg0.getMessage(), arg0);
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> map = new HashMap<String, String>();
				map.put("activity_id", activity_id);
				map.put("company_id", company_id);
				return map;
			}
		};
		queue.add(stringRequest);
		stringRequest
				.setRetryPolicy(new DefaultRetryPolicy(ConstantForSaveList.DEFAULTRETRYTIME*1000, 1, 1.0f));

	}

	protected void initSignPerson() {
		// TODO Auto-generated method stub
		if (signPersonList.size() > 0) {
			Log.e("test2", "返回dd的数据");
		} else {
			Log.e("test3", "返回dd的数据");
		}
	}

	private Handler handleSign = new Handler() {
		public void handleMessage(Message msg) {
			// fullStarffedAdapter.notifyDataSetChanged();
		};
	};

}
