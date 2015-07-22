package com.parttime.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.easemob.EMCallBack;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.parttime.login.FindPJLoginActivity;
import com.parttime.mine.EditMyIntroActivity;
import com.parttime.mine.FreshManGuideActivity;
import com.parttime.mine.MyFansActivity;
import com.parttime.mine.MyWalletActivity;
import com.parttime.mine.PraiseRecvedActivity;
import com.parttime.mine.RealNameCertActivity;
import com.parttime.mine.SuggestionActivity;
import com.parttime.mine.setting.SettingActivity;
import com.parttime.net.BaseRequest;
import com.parttime.net.Callback;
import com.parttime.type.AccountType;
import com.parttime.widget.FormItem;
import com.parttime.widget.RankView;
import com.qingmu.jianzhidaren.R;
import com.quark.common.JsonUtil;
import com.quark.common.Url;
import com.quark.fragment.company.BaseFragment;
import com.quark.http.image.LoadImage;
import com.quark.image.UploadImg;
import com.quark.jianzhidaren.ApplicationControl;
import com.quark.model.Function;
import com.quark.model.HuanxinUser;
import com.quark.ui.widget.CommonWidget;
import com.quark.ui.widget.CustomDialog;
import com.quark.utils.Util;
import com.quark.utils.WaitDialog;
import com.quark.volley.VolleySington;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 设置 商家 用户公用
 * 
 * @author howe
 * 
 */
public class MyFragment extends BaseFragment implements OnClickListener {
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";// 图片名称

	private static final int CERT_PASSED = 2;

	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	private String user_id;
	private String miandarao_url;// 设置商家免打扰
	private String get_miandarao_status_url;// 获取商家免打扰状态
	private int moneyPool, tip;// 活动提醒
	private int miandarao;// 商家免打扰
	protected WaitDialog dialog;
	protected RequestQueue queue = VolleySington.getInstance()
			.getRequestQueue();
	private View view;
	SharedPreferences sp;

	private Bitmap userPhotoBmp = null;
	private String uploadAvatarUrl;
	private String company_id, url;

	Function function;

	public static MyFragment newInstance(String param1, String param2) {
		MyFragment fragment = new MyFragment();

		return fragment;
	}

	public MyFragment() {

	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getActivity().getSharedPreferences("jrdr.setting",
				Context.MODE_PRIVATE);
		company_id = sp.getString("userId", "");
		url = Url.COMPANY_function + "?token=" + MainTabActivity.token;
		uploadAvatarUrl = Url.COMPANY_upload_avatar + "?token="
				+ MainTabActivity.token;
	}

	@Override
	public void onStart() {
		super.onStart();
		getMyInfo();
	}

	private void updateView(){
		if(function != null){
			tvName.setText(function.getName());
//			tvScore.setText(getString(R.string.x_scores, function.getPoint()));
			rvRank.rank((int) /*function.getPoint()*/4);
			String certString = getCertString();
			tvCertState.setText(certString);
			fiMyFans.setValue(getString(R.string.x_ge_in_chinese, function.getFollowers()));
			fiMyBalance.setValue(getString(R.string.x_rmb, function.getMoney()));
			fiRealNameCert.setValue(certString);
		}
	}

	private String getCertString(){
		if(function != null){
			int company_status = function.getCompany_status();
			if(company_status == CERT_PASSED){
				int type = function.getType();
				if(type == AccountType.PERSONAL){
					return getString(R.string.personal_certed);
				}else if(type == AccountType.ENTERPRISE){
					return getString(R.string.enterprise_certed);
				}
			}
		}
		return getString(R.string.not_certed);

	}

	private void getMyInfo(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("company_id", company_id);
		new BaseRequest().request(url, params, VolleySington.getInstance()
				.getRequestQueue(), new Callback() {
			@Override
			public void success(Object obj) {
				JSONObject json = (JSONObject) obj;
					function = (Function) JsonUtil.jsonToBean(json, Function.class);
					updateView();
					saveInfor();
			}

			@Override
			public void failed(Object obj) {

			}
		});
//		StringRequest request = new StringRequest(Method.POST, url, new Response.Listener<String>() {
//			@Override
//			public void onResponse(String s) {
//				try {
//					JSONObject json = new JSONObject(s);
//					function = (Function) JsonUtil.jsonToBean(json, Function.class);
//					updateView();
//					saveInfor();
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//		}, new Response.ErrorListener() {
//			@Override
//			public void onErrorResponse(VolleyError volleyError) {
//
//			}
//		}){
//			@Override
//			protected Map<String, String> getParams() throws AuthFailureError {
//				Map<String, String> params = new HashMap<String, String>();
//				params.put("company_id", company_id);
//				return params;
//			}
//		};
//
//		queue.add(request);
//		request.setRetryPolicy(new DefaultRetryPolicy(
//				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
	}

	@ViewInject(R.id.fi_my_intro)
	private FormItem fiMyIntro;

	@ViewInject(R.id.fi_my_fans)
	private FormItem fiMyFans;

	@ViewInject(R.id.fi_my_balance)
	private FormItem fiMyBalance;

	@ViewInject(R.id.fi_praise_recved)
	private FormItem fiPraiseRecved;

	@ViewInject(R.id.fi_real_name_cert)
	private FormItem fiRealNameCert;

	@ViewInject(R.id.fi_fresh_man_guide)
	private FormItem fiFreshManGuide;

	@ViewInject(R.id.fi_suggestion)
	private FormItem fiSuggestion;

	@ViewInject(R.id.fi_setting)
	private FormItem fiSetting;

	@ViewInject(R.id.iv_head)
	private ImageView ivHead;

	@ViewInject(R.id.tv_businessman)
	private TextView tvName;

//	@ViewInject(R.id.tv_score)
//	private TextView tvScore;
	@ViewInject(R.id.rv_rank)
	private RankView rvRank;

	@ViewInject(R.id.tv_cert_state)
	private TextView tvCertState;

	@ViewInject(R.id.tv_title)
	private TextView tvTitle;


	private void bindListeners(){
		fiMyIntro.setOnClickListener(this);
		fiMyFans.setOnClickListener(this);
		fiFreshManGuide.setOnClickListener(this);
		fiMyBalance.setOnClickListener(this);
		fiPraiseRecved.setOnClickListener(this);
		fiRealNameCert.setOnClickListener(this);
		fiSetting.setOnClickListener(this);
		fiSuggestion.setOnClickListener(this);
		ivHead.setOnClickListener(this);
	}



	@Override
	public void onResume() {
		super.onResume();
		// 先加载缓存
		loadHuncunFirst();
	}

	/**
	 * 先加载缓存
	 *
	 */
	private void loadHuncunFirst() {
//		TextView textView = (TextView) view.findViewById(R.id.name);
//		textView.setText(sp.getString(company_id + "name", "未知"));
		tvName.setText(sp.getString(company_id + "name", "未知"));
//		ImageView yan_img = (ImageView) view.findViewById(R.id.yan_img);
//		if (sp.getInt(company_id + "status", 0) == 2) {
//			yan_img.setImageResource(R.drawable.my_certified);
//		} else {
//			yan_img.setImageResource(R.drawable.my_unauthorized);
//			yan_img.setOnClickListener(yanzhengOnclick);
//		}

//		TextView money = (TextView) view.findViewById(R.id.money);
//		money.setText(sp.getInt(company_id + "money", 0) + "元");
//		cover_user_photo = (CircularImage) view
//				.findViewById(R.id.cover_user_photo);

		if (sp.getString("c" + company_id + "_photo", "") == null
				|| sp.getString("c" + company_id + "_photo", "").equals("")) {
			ivHead.setImageDrawable(getResources().getDrawable(
					R.drawable.photo_male));
		} else {
			loadNativePhotoFirst();
		}

//		TextView renzheng = (TextView) view.findViewById(R.id.renzheng);
//		if (sp.getInt(company_id + "status", 0) == 2) {
//			renzheng.setText("已认证");
//		} else {
//			renzheng.setText("未认证");
//		}
	}

	/**
	 * 初次进来先加载本地缓存头像
	 *
	 */
	private void loadNativePhotoFirst() {
		File mePhotoFold = new File(Environment.getExternalStorageDirectory()
				+ "/" + "jzdr/" + "image");
		if (!mePhotoFold.exists()) {
			mePhotoFold.mkdirs();
		}
		File picture_1 = new File(Environment.getExternalStorageDirectory()
				+ "/" + "jzdr/" + "image/"
				+ sp.getString("c" + company_id + "_photo", "a"));

		if (picture_1.exists()) {
			// 加载本地图片
			// Bitmap bb_bmp = MyResumeActivity.zoomImg(picture_1, 300, 300);
			Bitmap bb_bmp = BitmapFactory.decodeFile(Environment
					.getExternalStorageDirectory()
					+ "/"
					+ "jzdr/"
					+ "image/"
					+ sp.getString("c" + company_id + "_photo", "a"));
			if (bb_bmp != null) {
				ivHead
						.setImageBitmap(bb_bmp);
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Editor edt = sp.edit();
		edt.putInt("tip", tip);
		edt.putInt("miandarao", miandarao);
		edt.commit();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_my, container, false);
		ViewUtils.inject(this, view);
		tvTitle.setText(R.string.mine);
		bindListeners();
//		// carson初始化控件
//		check_lineLayout = (LinearLayout) view.findViewById(R.id.checkUpdate);
//		editTelephone_linelayout = (LinearLayout) view
//				.findViewById(R.id.editTelephone);
//		editPassword_linelayout = (LinearLayout) view
//				.findViewById(R.id.editPassword);
//		jianyi_linelayout = (LinearLayout) view.findViewById(R.id.jianyi);
//		loginOut_linelayout = (LinearLayout) view.findViewById(R.id.loginOut);
//		check_lineLayout.setOnClickListener(this);
//		editTelephone_linelayout.setOnClickListener(this);
//		editPassword_linelayout.setOnClickListener(this);
//		jianyi_linelayout.setOnClickListener(this);
//		loginOut_linelayout.setOnClickListener(this);

		// 免打扰url
		miandarao_url = Url.COMPANY_MIANDARAO + "?token="
				+ MainTabActivity.token;
		// 获取免打扰url
		get_miandarao_status_url = Url.COMPANY_MIANDARAO_STATUS + "?token="
				+ MainTabActivity.token;

		sp = getActivity().getSharedPreferences("jrdr.setting",
				android.content.Context.MODE_PRIVATE);
		user_id = sp.getString("userId", "");
		// *********预加载************
		// 设置奖金池
		moneyPool = sp.getInt("pool_money", 0);
		miandarao = sp.getInt("miandarao", 0);
		tip = sp.getInt("tip", 0);
//		TextView text = (TextView) view.findViewById(R.id.moneyPool);
//		text.setText(moneyPool + "");
		// 商家端时隐藏
		getMiandarao();
//		company_hit1.setVisibility(View.GONE);
//		company_hit2.setVisibility(View.GONE);
//		company_hit3.setVisibility(View.GONE);
//		company_hit4.setVisibility(View.GONE);
//		init_miandarao();// 获取免打扰状态
//		miandarao_kaiguan_on.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// 开启免打扰时,点击监听
//				miandarao_kaiguan_off.setVisibility(View.VISIBLE);
//				miandarao_kaiguan_on.setVisibility(View.GONE);
//				miandarao = 0;// 表示关闭了商家免打扰,任何人都能与商家聊天
//				miandarao_switch_btn();// 设置商家免打扰
//			}
//		});
//		miandarao_kaiguan_off.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// 免打扰关闭时,点击监听
//				miandarao_kaiguan_off.setVisibility(View.GONE);
//				miandarao_kaiguan_on.setVisibility(View.VISIBLE);
//				miandarao = 1;// 表示开启了商家免打扰,只允许好友与商家聊天
//				miandarao_switch_btn();
//			}
//		});

		// 飞机轮播池
//		LinearLayout feijichi = (LinearLayout) view.findViewById(R.id.feijichi);
//		feijichi.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(getActivity(), FeiJichiActivity.class);
//				startActivity(intent);
//			}
//		});

//		ImageView infoOperatingIV = (ImageView) view
//				.findViewById(R.id.infoOperating);
//		Animation operatingAnim = AnimationUtils.loadAnimation(getActivity(),
//				R.anim.roraterepeat);
//		LinearInterpolator lin = new LinearInterpolator();
//		operatingAnim.setInterpolator(lin);
//		if (operatingAnim != null) {
//			infoOperatingIV.startAnimation(operatingAnim);
//		}
		// 免打扰url
//		miandarao_url = Url.COMPANY_MIANDARAO + "?token="
//				+ MainTabActivity.token;
		return view;
	}

	/**
	 * sava info
	 */
	private void saveInfor() {
		Editor edt = sp.edit();
		edt.putString(company_id + "name", function.getName());
//		edt.putString("c" + company_id + "realname", function.getName());
		edt.putInt(company_id + "status", function.getStatus());
		edt.putFloat(company_id + "money",
				function.getMoney() > 0 ? function.getMoney() : 0);
		edt.putString("c" + company_id + "_photo", function.getAvatar());
		edt.putString(company_id + "_photo", function.getAvatar());
		edt.commit();
	}

	// 默认消息免打扰
	/*private void init_miandarao() {
		// showWait(true);
		StringRequest stringRequest = new StringRequest(Method.POST,
				get_miandarao_status_url, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							miandarao = js.getInt("disturb");
							// 处理money返回null的情况
							try {
								moneyPool = js.getInt("money");
							} catch (JSONException e) {
								moneyPool = 0;
							}
//							getMoneyPool();
//							getMiandarao();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// showWait(false);
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> map = new HashMap<String, String>();
				map.put("company_id", user_id);
				return map;
			}
		};
		queue.add(stringRequest);
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
	}*/

	/**
	 * 预加载免打扰开关
	 * 
	 */
	private void getMiandarao() {
//		miandarao_kaiguan_on = (ImageView) view
//				.findViewById(R.id.miandarao_kaiguan_on);
//		miandarao_kaiguan_off = (ImageView) view
//				.findViewById(R.id.miandarao_kaiguan_off);
//
//		if (miandarao == 1) {
//			miandarao_kaiguan_on.setVisibility(View.VISIBLE);
//			miandarao_kaiguan_off.setVisibility(View.GONE);
//		} else {
//			miandarao_kaiguan_on.setVisibility(View.GONE);
//			miandarao_kaiguan_off.setVisibility(View.VISIBLE);
//		}
	}

	protected void getTixing() {
//		kaiguan_on = (ImageView) view.findViewById(R.id.kaiguan_on);
//		kaiguan_off = (ImageView) view.findViewById(R.id.kaiguan_off);
//
//		if (tip == 1) {
//			kaiguan_on.setVisibility(View.VISIBLE);
//			kaiguan_off.setVisibility(View.GONE);
//		} else {
//			kaiguan_on.setVisibility(View.GONE);
//			kaiguan_off.setVisibility(View.VISIBLE);
//		}
	}

	/**
	 * 是否开启商家免打扰
	 */
	private void miandarao_switch_btn() {
		StringRequest stringRequest = new StringRequest(Method.POST,
				miandarao_url, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {

					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> map = new HashMap<String, String>();
				map.put("company_id", user_id);
				map.put("disturb", miandarao + "");
				return map;
			}
		};
		queue.add(stringRequest);
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	/**
	 * 读取图片的旋转的角度
	 *
	 * @param path
	 *            图片绝对路径
	 * @return 图片的旋转角度
	 */
	private int getBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 将图片按照某个角度进行旋转
	 *
	 * @param bm
	 *            需要旋转的图片
	 * @param degree
	 *            旋转角度
	 * @return 旋转后的图片
	 */
	private Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		Bitmap returnBm = null;

		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
					bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;
	}

	/**
	 * 裁剪图片方法实现
	 *
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri, int x, int y) {
		ConstantForSaveList.uploadUri = uri;// 暂时存储uri 如htc不能保存uri
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", x);
		intent.putExtra("aspectY", y);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", x);
		intent.putExtra("outputY", y);
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", false);
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	private void afterTakePhoto(){
		if (Util.hasSdcard()) {
			File tempFile = new File(
					Environment.getExternalStorageDirectory() + "/"
							+ IMAGE_FILE_NAME);
			//
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inJustDecodeBounds = true;
			userPhotoBmp = BitmapFactory.decodeFile(
					Environment.getExternalStorageDirectory() + "/"
							+ IMAGE_FILE_NAME, opt);
			// 获取到这个图片的原始宽度和高度
			int picWidth = opt.outWidth;
			int picHeight = opt.outHeight;

			// 获取屏的宽度和高度
			WindowManager windowManager = mActivity.getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			int screenWidth = display.getWidth();
			int screenHeight = display.getHeight();
			opt.inSampleSize = 2;
			if (picWidth > picHeight) {
				if (picWidth > screenWidth)
					opt.inSampleSize = picWidth / screenWidth;
			} else {
				if (picHeight > screenHeight)

					opt.inSampleSize = picHeight / screenHeight;
			}
			int degree = getBitmapDegree(Environment
					.getExternalStorageDirectory()
					+ "/"
					+ IMAGE_FILE_NAME);
			opt.inJustDecodeBounds = false;
			userPhotoBmp = BitmapFactory.decodeFile(
					Environment.getExternalStorageDirectory() + "/"
							+ IMAGE_FILE_NAME, opt);
			userPhotoBmp = rotateBitmapByDegree(userPhotoBmp, degree);

			Uri tt_uri = null;
			try {
				// tt_uri =
				// Uri.parse(MediaStore.Images.Media.insertImage(
				// getActivity().getContentResolver(),
				// Environment.getExternalStorageDirectory() + "/"
				// + IMAGE_FILE_NAME, null, null));
				tt_uri = Uri.parse(MediaStore.Images.Media.insertImage(
						getActivity().getContentResolver(),
						userPhotoBmp, null, null));
			} catch (Exception e) {
				e.printStackTrace();
				tt_uri = Uri.fromFile(tempFile);
			}
			if (tt_uri != null) {
				startPhotoZoom(tt_uri, 300, 300);
			} else {
				Toast mToast = Toast.makeText(getActivity(),
						"未找到存储卡，无法存储照片！", Toast.LENGTH_LONG);
				mToast.setGravity(Gravity.CENTER, 0, 0);
				mToast.show();
			}
		} else {

			Toast mToast = Toast.makeText(getActivity(),
					"未找到存储卡，无法存储照片！", Toast.LENGTH_LONG);
			mToast.setGravity(Gravity.CENTER, 0, 0);
			mToast.show();
		}
	}

	private void afterGetPhoto(Intent data){
		if (data.getData() != null) {
			startPhotoZoom(data.getData(), 300, 300);
		} else {
			Toast mToast = Toast.makeText(getActivity(), "获取图片失败。。。",
					Toast.LENGTH_LONG);
			mToast.setGravity(Gravity.CENTER, 0, 0);
			mToast.show();
		}
	}

	private void afterTakePhoto(Intent data){
		if (Util.hasSdcard()) {
			File tempFile = new File(
					Environment.getExternalStorageDirectory() + "/"
							+ IMAGE_FILE_NAME);
			//
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inJustDecodeBounds = true;
			userPhotoBmp = BitmapFactory.decodeFile(
					Environment.getExternalStorageDirectory() + "/"
							+ IMAGE_FILE_NAME, opt);
			// 获取到这个图片的原始宽度和高度
			int picWidth = opt.outWidth;
			int picHeight = opt.outHeight;

			// 获取屏的宽度和高度
			WindowManager windowManager = mActivity.getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			int screenWidth = display.getWidth();
			int screenHeight = display.getHeight();
			opt.inSampleSize = 2;
			if (picWidth > picHeight) {
				if (picWidth > screenWidth)
					opt.inSampleSize = picWidth / screenWidth;
			} else {
				if (picHeight > screenHeight)

					opt.inSampleSize = picHeight / screenHeight;
			}
			int degree = getBitmapDegree(Environment
					.getExternalStorageDirectory()
					+ "/"
					+ IMAGE_FILE_NAME);
			opt.inJustDecodeBounds = false;
			userPhotoBmp = BitmapFactory.decodeFile(
					Environment.getExternalStorageDirectory() + "/"
							+ IMAGE_FILE_NAME, opt);
			userPhotoBmp = rotateBitmapByDegree(userPhotoBmp, degree);

			Uri tt_uri = null;
			try {
				// tt_uri =
				// Uri.parse(MediaStore.Images.Media.insertImage(
				// getActivity().getContentResolver(),
				// Environment.getExternalStorageDirectory() + "/"
				// + IMAGE_FILE_NAME, null, null));
				tt_uri = Uri.parse(MediaStore.Images.Media.insertImage(
						getActivity().getContentResolver(),
						userPhotoBmp, null, null));
			} catch (Exception e) {
				e.printStackTrace();
				tt_uri = Uri.fromFile(tempFile);
			}
			if (tt_uri != null) {
				startPhotoZoom(tt_uri, 300, 300);
			} else {
				Toast mToast = Toast.makeText(getActivity(),
						"未找到存储卡，无法存储照片！", Toast.LENGTH_LONG);
				mToast.setGravity(Gravity.CENTER, 0, 0);
				mToast.show();
			}
		} else {

			Toast mToast = Toast.makeText(getActivity(),
					"未找到存储卡，无法存储照片！", Toast.LENGTH_LONG);
			mToast.setGravity(Gravity.CENTER, 0, 0);
			mToast.show();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Activity activity = getActivity();
		if(activity == null || !isAdded()){
			return ;
		}
		if(activity != null){
			if(resultCode != Activity.RESULT_OK){
				return;
			}
			switch (requestCode){
				case IMAGE_REQUEST_CODE:
					afterGetPhoto(data);
					break;
				case CAMERA_REQUEST_CODE:
					afterTakePhoto(data);
					break;
				case RESULT_REQUEST_CODE:
					if (data != null) {
						Toast mToast = Toast.makeText(getActivity(), R.string.wait_while_uploading_avatar,
								Toast.LENGTH_LONG);
						mToast.setGravity(Gravity.CENTER, 0, 0);
						mToast.show();
						UploadImg.getImageToView(getActivity(), data,
								ivHead, uploadAvatarUrl, null, null,
								null, "avatar", null, "company_id", company_id,
								null);
					}
					break;
			}
		}
	}



//	protected void getMoneyPool() {
//		TextView text = (TextView) view.findViewById(R.id.moneyPool);
//		text.setText(moneyPool + "");
//		saveInfor(moneyPool);
//	}

	protected void showWait(boolean isShow) {
		if (isShow) {
			if (null == dialog) {
				dialog = new WaitDialog(getActivity());
			}
			dialog.show();
		} else {
			if (null != dialog) {
				dialog.dismiss();
			}
		}
	}

	/**
	 * 检查更新
	 */
	// carson 屏蔽点击更新

	/*
	 * @OnClick(R.id.checkUpdate) public void checkUpdateOnclick(View v) {
	 * String ui = update_info.getText().toString(); if (ui.equals("发现新版本")) {
	 * UmengUpdateAgent.setUpdateAutoPopup(true);
	 * UmengUpdateAgent.setUpdateOnlyWifi(false);
	 * UmengUpdateAgent.forceUpdate(getActivity()); } else {
	 * showAlertDialog("您当前的版本已是最新版本，无需升级", "已是最新版本"); } }
	 */

	public void showAlertDialog(String str, final String str2) {

		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
		builder.setMessage(str);
		builder.setTitle(str2);

		builder.setPositiveButton("加油吧", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

    private void goToActivity(Class activityClz){
        Activity activity = getActivity();
        if(activity != null && !activity.isFinishing() && isAdded()) {
            Intent intent = new Intent(activity, activityClz);
            activity.startActivity(intent);
        }
    }

	private void myIntro(){
		goToActivity(EditMyIntroActivity.class);
	}

	private void myFans(){
		goToActivity(MyFansActivity.class);
	}

	private void myBalance(){
        goToActivity(MyWalletActivity.class);
	}

	private void praiseRecved(){
        goToActivity(PraiseRecvedActivity.class);
	}

	private void realNameCert(){
        goToActivity(RealNameCertActivity.class);
	}

	private void freshManGuide(){
        goToActivity(FreshManGuideActivity.class);
	}

	private void suggestion(){
        goToActivity(SuggestionActivity.class);
	}

	private void setting(){
		goToActivity(SettingActivity.class);
	}

	/**
	 * carson设置点击监听
	 */
	@Override
	public void onClick(View v) {
		if (user_id.equals("")) {
			CommonWidget.showAlertDialog(getActivity(), getActivity(),
					"您还没有登录，注册登录后才可以查看哦！", "温馨提示", "随便看看");
		} else {
			switch (v.getId()){
				case R.id.fi_my_intro:
					myIntro();
					break;
				case R.id.fi_my_fans:
					myFans();
					break;
				case R.id.fi_my_balance:
					myBalance();
					break;
				case R.id.fi_praise_recved:
					praiseRecved();
					break;
				case R.id.fi_real_name_cert:
					realNameCert();
					break;
				case R.id.fi_fresh_man_guide:
					freshManGuide();
					break;
				case R.id.fi_suggestion:
					suggestion();
					break;
				case R.id.fi_setting:
					setting();
					break;
				case R.id.iv_head:
					showSheetPic();
					break;
			}
//			Intent intent = new Intent();
//			switch (arg0.getId()) {
//			case R.id.checkUpdate:
//				showAlertDialog("当前版本已是最新版本，敬请期待下一版本", "温馨提示");
//				break;
//			case R.id.editTelephone:
//				intent.setClass(getActivity(), EditPhoneActivity.class);
//				startActivity(intent);
//				break;
//			case R.id.editPassword:
//				intent.setClass(getActivity(), ModifyPwdActivity.class);
//				startActivity(intent);
//				break;
//			case R.id.jianyi:
//				intent.setClass(getActivity(), SuggestActivity.class);
//				startActivity(intent);
//				break;
//			case R.id.loginOut:
//				loginOut();
//				break;
//			default:
//				break;
//			}

		}

	}

	public Dialog showSheetPic() {
		final Dialog dlg = new Dialog(getActivity(), R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.actionsheet, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		TextView mContent = (TextView) layout.findViewById(R.id.content);// 拍照上传
		TextView mCancel = (TextView) layout.findViewById(R.id.cancel);
		TextView mTitle = (TextView) layout.findViewById(R.id.title);// 相册中选择

		// 拍照上传
		mContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentFromCapture = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				if (Util.hasSdcard()) {
					File s = Environment.getExternalStorageDirectory();
					intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
							.fromFile(new File(Environment
									.getExternalStorageDirectory(),
									IMAGE_FILE_NAME)));
				} else {

					Toast mToast = Toast.makeText(getActivity(),
							"未找到存储卡，无法存储照片！", Toast.LENGTH_LONG);
					mToast.setGravity(Gravity.CENTER, 0, 0);
					mToast.show();
				}
				startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
				dlg.dismiss();
			}
		});

		mTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentFromGallery = new Intent();
				intentFromGallery.setType("image/*"); // 设置文件类型
				intentFromGallery.setAction(Intent.ACTION_PICK);
				startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
				dlg.dismiss();
			}
		});

		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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
		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}

}
