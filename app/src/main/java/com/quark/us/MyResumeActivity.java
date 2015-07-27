package com.quark.us;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qingmu.jianzhidaren.R;
import com.quark.common.JsonHelper;
import com.quark.common.JsonUtil;
import com.quark.common.ToastUtil;
import com.quark.common.Url;
import com.quark.image.UploadImg;
import com.quark.jianzhidaren.BaseActivity;
import com.parttime.main.MainTabActivity;
import com.quark.model.MyResume;
import com.quark.senab.us.image.ImagePagerActivity;
import com.quark.ui.widget.ActionSheet;
import com.quark.ui.widget.ActionSheet.OnActionSheetSelected;
import com.quark.ui.widget.CustomDialog;
import com.quark.utils.Util;

/**
 * 我的简历
 * 
 * @author C罗
 * 
 */
public class MyResumeActivity extends BaseActivity implements
		OnActionSheetSelected, OnCancelListener {
	String url1 = "";
	String url2 = "";
	String url3 = "";
	String url4 = "";
	String url5 = "";
	String url6 = "";

	String[] imagesUrls = new String[] { url1, url2, url3, url4, url5, url6 };// 直接初始化
	String type;
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";// 图片名称
	private String totalPic;
	private MyResume re = new MyResume();
	private String userId;
	private String getResumeUrl;
	private String submintUrl;
	private String loadPicUrl;
	private String uploadPicUrl;
	private boolean isLoadData = false;
	private int myCountPic = 0;
	private int MIN_COUNT = 0;
	// 我的照片
	@ViewInject(R.id.button1)
	ImageView button1;
	@ViewInject(R.id.button2)
	ImageView button2;
	@ViewInject(R.id.button3)
	ImageView button3;
	@ViewInject(R.id.button4)
	ImageView button4;
	@ViewInject(R.id.button5)
	ImageView button5;
	@ViewInject(R.id.button6)
	ImageView button6;

	// 姓名
	@ViewInject(R.id.my_name)
	TextView my_name;

	@ViewInject(R.id.sex_layout)
	LinearLayout sex_layout;
	@ViewInject(R.id.my_sex)
	TextView my_sex;

	@ViewInject(R.id.borthday_layout)
	LinearLayout borthday_layout;
	@ViewInject(R.id.my_borthyday)
	TextView my_birthyday;
	// 身高
	@ViewInject(R.id.my_shengao)
	EditText my_shengao;
	// 毕业
	@ViewInject(R.id.my_biye)
	EditText my_biye;
	// 学历
	@ViewInject(R.id.my_xueli_layout)
	LinearLayout my_xueli_layout;
	@ViewInject(R.id.my_xueli)
	TextView my_xueli;
	// 三围
	/*
	 * @ViewInject(R.id.my_sanwei_xiong) EditText my_sanwei_xiong;
	 * 
	 * @ViewInject(R.id.my_sanwei_yao) EditText my_sanwei_yao;
	 * 
	 * @ViewInject(R.id.my_sanwei_tun) EditText my_sanwei_tun;
	 */
	@ViewInject(R.id.my_sanwei)
	EditText my_sanwei;
	// 健康证
	@ViewInject(R.id.my_jiankongzheng_layout)
	LinearLayout my_jiankongzheng_layout;
	@ViewInject(R.id.my_jiankongzheng)
	TextView my_jiankongzheng;
	//
	@ViewInject(R.id.my_yifuchima_layout)
	LinearLayout my_yifuchima_layout;
	@ViewInject(R.id.my_yifuchima)
	TextView my_yifuchima;
	//
	@ViewInject(R.id.my_shoos_layout)
	LinearLayout my_shoos_layout;
	@ViewInject(R.id.my_shoos)
	TextView my_shoos;
	// 语言
	@ViewInject(R.id.my_language_layout)
	LinearLayout my_language_layout;
	@ViewInject(R.id.my_language)
	TextView my_language;
	// 经验
	@ViewInject(R.id.my_jinyan)
	EditText my_jinyan;
	// 图片总数
	@ViewInject(R.id.my_pic)
	TextView my_pic;
	int width; // 屏幕宽度
	private Bitmap pngBM = null;
	Uri tt_uri = null;// 拍照的Uri,三星调用拍照会启动ondestory,需额外保存
	private ImageView me_top_left_img;// 左边返回图标
	private SharedPreferences sp;
	private ProgressBar loadProgressBar;// 上传图片时加载框

	// /
	// private ImagePopupWindow code;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.me_my_resume);
		ViewUtils.inject(this);
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		userId = sp.getString("userId", "");
		getResumeUrl = Url.USER_jianli_show + "?token=" + MainTabActivity.token;
		submintUrl = Url.USER_jianli_submit + "?token=" + MainTabActivity.token;
		uploadPicUrl = Url.USER_jianli_uploadmypic + "?token="
				+ MainTabActivity.token;
		loadPicUrl = Url.GETPIC;
		// 图片上传loading
		loadProgressBar = (ProgressBar) findViewById(R.id.progressBar);// 加载框
		getMyResume();
		WindowManager wm = this.getWindowManager();
		width = wm.getDefaultDisplay().getWidth();

		my_jinyan.addTextChangedListener(mTextWatcher);
		my_jinyan.setSelection(my_jinyan.length()); // 将光标移动最后一个字符后面
		me_top_left_img = (ImageView) findViewById(R.id.me_top_left_img);
		me_top_left_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (isJianliChange()) {
					showAlertDialog("您编辑的资料尚未保存。", "温馨提示");
				} else {
					MyResumeActivity.this.finish();
				}
			}
		});
	}

	/**
	 * 弹出是否保存的弹出框
	 */
	private void showAlertDialog(String str, final String str2) {

		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(str);
		builder.setTitle(str2);

		builder.setPositiveButton("替我保存",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						saveData();
						if (check()) {
							showWait(true);
							StringRequest request = new StringRequest(
									Request.Method.POST, submintUrl,
									new Response.Listener<String>() {
										@Override
										public void onResponse(String response) {
											showWait(false);
											try {
												JSONObject js = new JSONObject(
														response);
												JSONObject statusjs = js
														.getJSONObject("ResponseStatus");
												int status = statusjs
														.getInt("status");
												if (status == 2) {
													showToast("更新成功");
													finish();
												} else {
													showToast("用户不存在");
												}
											} catch (JSONException e) {
												e.printStackTrace();
											}
										}
									}, new Response.ErrorListener() {
										@Override
										public void onErrorResponse(
												VolleyError volleyError) {
											showWait(false);
										}
									}) {
								@Override
								protected Map<String, String> getParams()
										throws AuthFailureError {
									return JsonHelper.toMap(re);
								}
							};
							queue.add(request);
							request.setRetryPolicy(new DefaultRetryPolicy(
									ConstantForSaveList.DEFAULTRETRYTIME * 1000,
									1, 1.0f));

						}
					}
				});
		builder.setNegativeButton("暂时没空",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
						MyResumeActivity.this.finish();
					}
				});
		builder.create().show();
	}

	/**
	 * 判断简历信息是否更改
	 * 
	 */
	private boolean isJianliChange() {
		// 如果简历更改返回true,else 返回false

		// 性别
		if (re.getSex() == 0) {
			if (my_sex.getText() == null) {
				return true;
			} else {
				if (!my_sex.getText().equals("女性")) {
					return true;
				}
			}
		} else if (re.getSex() == 1) {
			if (my_sex.getText() == null) {
				return true;
			} else {
				if (!my_sex.getText().equals("男性")) {
					return true;
				}
			}

		} else {
			if (my_sex.getText() == null) {
				return true;
			} else {
				if (!my_sex.getText().equals("未填写")) {
					return true;
				}
			}
		}

		// 生日
		if (my_birthyday.getText() == null) {
			return true;
		} else {
			if (!my_birthyday.getText().toString().equals(re.getBirthdate())) {
				return true;
			}
		}
		// 身高
		if (my_shengao.getText() == null) {
			return true;
		} else {
			if (!my_shengao.getText().toString()
					.equals(String.valueOf(re.getHeight()))) {
				return true;
			}
		}
		// 学校
		if (my_biye.getText() == null) {
			return true;
		} else {
			if (!my_biye.getText().toString().equals(re.getGraduate())) {
				return true;
			}
		}
		// 学历
		if (my_xueli.getText() == null) {
			return true;
		} else {
			if (!my_xueli.getText().toString().equals(re.getEducation())) {
				return true;
			}
		}
		// 三围
		if (my_sanwei.getText() == null) {
			return true;
		} else {
			if (re.getBbh() == null) {
				if (!"".equals(my_sanwei.getText().toString())) {
					return true;
				}
			} else {
				if (!my_sanwei.getText().toString().equals(re.getBbh())) {
					return true;
				}
			}
		}
		// 健康证
		if (my_jiankongzheng.getText() == null) {
			return true;
		} else {
			String reString = "";
			if (re.getHealth_record() == 0) {
				reString = "无";
			} else if (re.getHealth_record() == 1) {
				reString = "有";
			} else {
				reString = "未填写";
			}
			if (!my_jiankongzheng.getText().toString().equals(reString)) {
				return true;
			}
		}
		// 衣服尺码
		if (my_yifuchima.getText() == null) {
			return true;
		} else {
			String clothStr = re.getCloth_weight();
			if (clothStr == null || "".equals(clothStr)) {
				clothStr = "未填写";
			}
			if (!my_yifuchima.getText().toString().equals(clothStr)) {
				return true;
			}
		}
		// 鞋码
		if (my_shoos.getText() == null) {
			return true;
		} else {
			if ("未填写".equals(my_shoos.getText().toString())) {
				if (re.getShoe_weight() == 0 || re.getShoe_weight() == -1) {

				} else {
					return true;
				}

			} else {
				if (!my_shoos.getText().toString()
						.equals(String.valueOf(re.getShoe_weight()))) {
					return true;
				}
			}
		}
		// 语言
		if (my_language.getText() == null) {
			return true;
		} else {
			String lag = re.getLanguage();
			if (lag == null || "".equals(lag)) {
				lag = "未填写";
			}
			if (!my_language.getText().toString().equals(lag)) {
				return true;
			}
		}
		// 经历
		if (my_jinyan.getText() == null) {
			return true;
		} else {
			if (!my_jinyan.getText().toString().equals(re.getSummary())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
			// moveTaskToBack(true);
			// 弹出是否保存的按钮
			if (isJianliChange()) {
				showAlertDialog("您编辑的资料尚未保存!", "温馨提示");
			} else {
				MyResumeActivity.this.finish();
			}

			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			// 监控/拦截菜单键
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {
			// 由于Home键为系统键，此处不能捕获，需要重写onAttachedToWindow()
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 缓存本地个人简历信息,网络不好的时候提供调用
	 * 
	 */
	private void savaPersonJianliInfor() {
		Editor personEdt = sp.edit();
		personEdt.putString(userId + "name", re.getName());// 名字
		personEdt.putInt(userId + "sex", re.getSex());// 性别
		personEdt.putString(userId + "birthdate", re.getBirthdate());// 生日
		personEdt.putInt(userId + "height", re.getHeight());// 身高
		personEdt.putString(userId + "graduate", re.getGraduate());// 学校
		personEdt.putString(userId + "education", re.getEducation());// 学历
		personEdt.putString(userId + "bbh", re.getBbh());// 三围
		personEdt.putInt(userId + "health_record", re.getHealth_record());// 是否有健康证
		personEdt.putString(userId + "cloth_weight", re.getCloth_weight());// 衣服尺码
		personEdt.putInt(userId + "shoe_weight", re.getShoe_weight());// 鞋子尺寸
		personEdt.putString(userId + "language", re.getLanguage());// 语言
		personEdt.putString(userId + "summary", re.getSummary());// 经历
		String pic_1 = re.getPicture_1();
		if (pic_1 == null || pic_1.equals("")) {

		} else {
			personEdt.putString(userId + "pic_1", pic_1);
		}
		String pic_2 = re.getPicture_2();
		if (pic_2 == null || pic_2.equals("")) {

		} else {
			personEdt.putString(userId + "pic_2", pic_2);
		}
		String pic_3 = re.getPicture_3();
		if (pic_3 == null || pic_3.equals("")) {

		} else {
			personEdt.putString(userId + "pic_3", pic_3);
		}
		String pic_4 = re.getPicture_4();
		if (pic_4 == null || pic_4.equals("")) {

		} else {
			personEdt.putString(userId + "pic_4", pic_4);
		}
		String pic_5 = re.getPicture_5();
		if (pic_5 == null || pic_5.equals("")) {

		} else {
			personEdt.putString(userId + "pic_5", pic_5);
		}
		String pic_6 = re.getPicture_6();
		if (pic_6 == null || pic_6.equals("")) {

		} else {
			personEdt.putString(userId + "pic_6", pic_6);
		}
		personEdt.commit();

	}

	/**
	 * 获取本地缓存的个人简历信息 网络请求失败的时候使用
	 */
	private void getPersonJianliInfor() {
		re.setName(sp.getString(userId + "name", re.getName()));// 名字
		re.setSex(sp.getInt(userId + "sex", re.getSex()));// 性别
		re.setBirthdate(sp.getString(userId + "birthdate", re.getBirthdate()));// 生日
		re.setHeight(sp.getInt(userId + "height", re.getHeight()));// 身高
		re.setGraduate(sp.getString(userId + "graduate", re.getGraduate()));// 学校
		re.setEducation(sp.getString(userId + "education", re.getEducation()));// 学历
		re.setBbh(sp.getString(userId + "bbh", re.getBbh()));// 三围
		re.setHealth_record(sp.getInt(userId + "health_record",
				re.getHealth_record()));// 是否有健康证
		re.setCloth_weight(sp.getString(userId + "cloth_weight",
				re.getCloth_weight()));// 衣服尺码
		re.setShoe_weight(sp.getInt(userId + "shoe_weight", re.getShoe_weight()));// 鞋子尺寸
		re.setLanguage(sp.getString(userId + "language", re.getLanguage()));// 语言
		re.setSummary(sp.getString(userId + "summary", ""));// 经历
		re.setPicture_1(sp.getString(userId + "pic_1", ""));// 图片1
		re.setPicture_2(sp.getString(userId + "pic_2", ""));// 图片2
		re.setPicture_3(sp.getString(userId + "pic_3", ""));// 图片3
		re.setPicture_4(sp.getString(userId + "pic_4", ""));// 图片4
		re.setPicture_5(sp.getString(userId + "pic_5", ""));// 图片5
		re.setPicture_6(sp.getString(userId + "pic_6", ""));// 图片6
		if (!re.getPicture_1().equals("")) {
			checkPhotoExits(re.getPicture_1(), button1);
			myCountPic++;
		}
		if (!re.getPicture_2().equals("")) {
			checkPhotoExits(re.getPicture_2(), button2);
			myCountPic++;
		}
		if (!re.getPicture_3().equals("")) {
			checkPhotoExits(re.getPicture_3(), button3);
			myCountPic++;
		}
		if (!re.getPicture_4().equals("")) {
			checkPhotoExits(re.getPicture_4(), button4);
			myCountPic++;
		}
		if (!re.getPicture_5().equals("")) {
			checkPhotoExits(re.getPicture_5(), button5);
			myCountPic++;
		}
		if (!re.getPicture_6().equals("")) {
			checkPhotoExits(re.getPicture_6(), button6);
			myCountPic++;
		}
		// initview 设置界面的默认值
		initDefaultView();// 加载没有网络请求到的数据

	}

	/**
	 * 加载默认的简历列表 之前本地存储的
	 */
	private void initDefaultView() {
		// 赋值
		my_pic.setText("我的照片（" + myCountPic + "/6）");
		my_name.setText(re.getName());
		int sexInt = re.getSex();
		if (sexInt == 0) {
			my_sex.setText("女性");
			my_sex.setTextColor(getResources().getColor(R.color.ziti_black));
		} else if (sexInt == -1) {
			my_sex.setText("未填写");
		} else {
			my_sex.setText("男性");
			my_sex.setTextColor(getResources().getColor(R.color.ziti_black));
		}
		if (re.getBirthdate() != null
				&& re.getBirthdate().equals("2015-01-01") == false) {
			my_birthyday.setText(re.getBirthdate());
			my_birthyday.setTextColor(getResources().getColor(
					R.color.ziti_black));
		}
		if (re.getHeight() != -1) {
			my_shengao.setText(re.getHeight() + "");
		}
		if (re.getGraduate() != null) {
			my_biye.setText(re.getGraduate());
		}

		if (re.getEducation() != null) {
			my_xueli.setText(re.getEducation());
			my_xueli.setTextColor(getResources().getColor(R.color.ziti_black));
		}
		// 三围
		if (re.getBbh() != null && !"".equals(re.getBbh())) {
			my_sanwei.setText(re.getBbh() + "");
		}

		// 三围end
		int jkz = re.getHealth_record();
		if (jkz != -1) {
			if (jkz == 0) {
				my_jiankongzheng.setText("无");
				my_jiankongzheng.setTextColor(getResources().getColor(
						R.color.ziti_black));
			} else {
				my_jiankongzheng.setText("有");
				my_jiankongzheng.setTextColor(getResources().getColor(
						R.color.ziti_black));
			}
		}
		if ((re.getCloth_weight() != null)
				&& (!re.getCloth_weight().equals("-1"))) {
			my_yifuchima.setText(re.getCloth_weight());
			my_yifuchima.setTextColor(getResources().getColor(
					R.color.ziti_black));
		}
		int shoes = re.getShoe_weight();
		if (shoes == -1 || shoes == 0) {
			my_shoos.setText("未填写");
		} else {
			my_shoos.setText(re.getShoe_weight() + "");
			my_shoos.setTextColor(getResources().getColor(R.color.ziti_black));
		}
		if ((re.getLanguage() != null) && (!re.getLanguage().equals("-1"))) {
			my_language.setText(re.getLanguage());
			my_language.setTextColor(getResources()
					.getColor(R.color.ziti_black));
		}

		if (re.getSummary() != null) {
			my_jinyan.setText(re.getSummary());
		}

	}

	/**
	 * 判断本地是否存储了之前的照片
	 * 
	 */

	private void checkPhotoExits(final String picName, final ImageView iv) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				File mePhotoFold = new File(
						Environment.getExternalStorageDirectory() + "/"
								+ "jzdr/" + "image");
				if (!mePhotoFold.exists()) {
					mePhotoFold.mkdirs();
				}
				File f = new File(Environment.getExternalStorageDirectory()
						+ "/" + "jzdr/" + "image/" + picName);
				if (f.exists()) {
					Bitmap bb_bmp = zoomImg(f, 300, 400);
					if (bb_bmp != null) {
						iv.setImageBitmap(bb_bmp);
					} else {
						loadpersonPic(picName, iv, 0);
					}

				} else {
					loadpersonPic(picName, iv, 0);
				}

			}
		});

	}

	/**
	 * 处理图片
	 * 
	 * @param bm
	 *            所要转换的bitmap
	 * @param newWidth新的宽
	 * @param newHeight新的高
	 * @return 指定宽高的bitmap
	 */
	public static Bitmap zoomImg(File picFile, int newWidth, int newHeight) {
		Bitmap bmp = null;

		try {
			FileInputStream fis = new FileInputStream(picFile);
			bmp = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return bmp;
		}
		if (bmp == null) {
			return null;
		}
		// 获得图片的宽高
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		bmp = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
		return bmp;
	}

	/**
	 * 获取 网络数据
	 */
	public void getMyResume() {
		myCountPic = 0;
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST,
				getResumeUrl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject statusjs = js
									.getJSONObject("ResponseStatus");
							int status = statusjs.getInt("status");
							if (status == 2) {
								JSONObject myResumeJson = js
										.getJSONObject("MyResumeResponse");
								re = (MyResume) JsonUtil.jsonToBean(
										myResumeJson, MyResume.class);
								isLoadData = true;
								if (re.getPicture_1() != null
										&& !"".equals(re.getPicture_1())) {
									url1 = Url.GETPIC + re.getPicture_1();
									imagesUrls[0] = url1;
									myCountPic++;
									new Thread() {
										public void run() {
											checkPhotoExits(re.getPicture_1(),
													button1);
										}
									}.start();

								}
								if (re.getPicture_2() != null
										&& !"".equals(re.getPicture_2())) {
									url2 = Url.GETPIC + re.getPicture_2();
									imagesUrls[1] = url2;
									myCountPic++;
									new Thread() {
										public void run() {
											checkPhotoExits(re.getPicture_2(),
													button2);
										}
									}.start();
								}
								if (re.getPicture_3() != null
										&& !"".equals(re.getPicture_3())) {
									url3 = Url.GETPIC + re.getPicture_3();
									imagesUrls[2] = url3;
									myCountPic++;
									new Thread() {
										public void run() {
											checkPhotoExits(re.getPicture_3(),
													button3);
										}
									}.start();
								}
								if (re.getPicture_4() != null
										&& !"".equals(re.getPicture_4())) {
									url4 = Url.GETPIC + re.getPicture_4();
									imagesUrls[3] = url4;
									myCountPic++;
									new Thread() {
										public void run() {
											checkPhotoExits(re.getPicture_4(),
													button4);
										}
									}.start();
								}
								if (re.getPicture_5() != null
										&& !"".equals(re.getPicture_5())) {
									url5 = Url.GETPIC + re.getPicture_5();
									imagesUrls[4] = url5;
									myCountPic++;
									new Thread() {
										public void run() {
											checkPhotoExits(re.getPicture_5(),
													button5);
										}
									}.start();
								}
								if (re.getPicture_6() != null
										&& !"".equals(re.getPicture_6())) {
									url6 = Url.GETPIC + re.getPicture_6();
									imagesUrls[5] = url6;
									myCountPic++;
									new Thread() {
										public void run() {
											checkPhotoExits(re.getPicture_6(),
													button6);
										}
									}.start();
								}
								my_pic.setText("我的照片（" + myCountPic + "/6）");
								// 赋值
								my_name.setText(re.getName());
								int sexInt = re.getSex();
								if (sexInt == 0) {
									my_sex.setText("女性");
									my_sex.setTextColor(getResources()
											.getColor(R.color.ziti_black));
								} else if (sexInt == -1) {
									my_sex.setText("未填写");
								} else {
									my_sex.setText("男性");
									my_sex.setTextColor(getResources()
											.getColor(R.color.ziti_black));
								}
								if (re.getBirthdate() != null
										&& re.getBirthdate().equals(
												"2015-01-01") == false) {
									my_birthyday.setText(re.getBirthdate());
									my_birthyday.setTextColor(getResources()
											.getColor(R.color.ziti_black));
								}
								if (re.getHeight() != -1) {
									my_shengao.setText(re.getHeight() + "");
								}
								if (re.getGraduate() != null) {
									my_biye.setText(re.getGraduate());
								}

								if (re.getEducation() != null) {
									my_xueli.setText(re.getEducation());
									my_xueli.setTextColor(getResources()
											.getColor(R.color.ziti_black));
								}
								// 三围
								if (re.getBbh() != null) {
									my_sanwei.setText(re.getBbh() + "");
								}

								// 三围end
								int jkz = re.getHealth_record();
								if (jkz != -1) {
									if (jkz == 0) {
										my_jiankongzheng.setText("无");
										my_jiankongzheng
												.setTextColor(getResources()
														.getColor(
																R.color.ziti_black));
									} else {
										my_jiankongzheng.setText("有");
										my_jiankongzheng
												.setTextColor(getResources()
														.getColor(
																R.color.ziti_black));
									}
								}
								if ((re.getCloth_weight() != null)
										&& (!re.getCloth_weight().equals("-1"))) {
									my_yifuchima.setText(re.getCloth_weight());
									my_yifuchima.setTextColor(getResources()
											.getColor(R.color.ziti_black));
								} else {
									my_yifuchima.setText("未填写");
								}
								int shoes = re.getShoe_weight();
								if (shoes != -1 && shoes != 0) {
									my_shoos.setText(re.getShoe_weight() + "");
									my_shoos.setTextColor(getResources()
											.getColor(R.color.ziti_black));
								} else {
									my_shoos.setText("未填写");
								}
								if ((re.getLanguage() != null)
										&& (!re.getLanguage().equals("-1"))) {
									my_language.setText(re.getLanguage());
									my_language.setTextColor(getResources()
											.getColor(R.color.ziti_black));
								}

								if (re.getSummary() != null) {
									my_jinyan.setText(re.getSummary());
								}
								savaPersonJianliInfor();// 获取网络信息成功,保存简历信息
							} else {
								showToast("用户不存在");
							}
						} catch (JSONException e) {
							e.printStackTrace();
							getPersonJianliInfor();// 网络请求失败调用本地简历信息
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						// showToast("加载数据失败，请重新加载该页面");
						showWait(false);
						getPersonJianliInfor();// 网络请求失败调用本地简历信息
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", userId);
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	// 图片点击加载
	@OnClick({ R.id.button1, R.id.button2, R.id.button3, R.id.button4,
			R.id.button5, R.id.button6 })
	public void photoOnClick(View view) {
		switch (view.getId()) {
		case R.id.button1:
			imageBrower(0, imagesUrls);
			break;
		case R.id.button2:
			imageBrower(1, imagesUrls);
			break;
		case R.id.button3:
			imageBrower(2, imagesUrls);
			break;
		case R.id.button4:
			imageBrower(3, imagesUrls);
			break;
		case R.id.button5:
			imageBrower(4, imagesUrls);
			break;
		case R.id.button6:
			imageBrower(5, imagesUrls);
			break;
		default:
			break;
		}
	}

	// 已做缓存处理
	private void imageBrower(int position, String[] urls) {

		Intent intent = new Intent();
		intent.setClass(MyResumeActivity.this, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		startActivityForResult(intent, 100);

		/*
		 * code = new
		 * ImagePopupWindow(MyResumeActivity.this,urls,position,userId);
		 * 
		 * // 显示窗口 (设置layout在PopupWindow中显示的位置)
		 * code.showAtLocation(MyResumeActivity
		 * .this.findViewById(R.id.me_my_main), Gravity.BOTTOM |
		 * Gravity.CENTER_HORIZONTAL, 0, 0);
		 */

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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != pngBM) {
			pngBM.recycle();
			pngBM = null;
		}
	}

	// 有的三星手机调用系统照相机就ondestroy
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	// 返回結果
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_CANCELED) {
			// 弹出拍照上传、相册中取、取消的pop弹出框
			if (requestCode == 100) {
				// 位置标识：1-正面近脸，2-正面半身，3-正面全身，4-任意个照第1张，5-任意个照第2张，6-任意个照第3张
				String position = data.getExtras().getString("position");
				totalPic = data.getExtras().getString("totalPic");
				my_pic.setText("我的照片（" + totalPic + "/6）");
				type = position;
				UploadImg.showSheetPic(MyResumeActivity.this,
						MyResumeActivity.this, MyResumeActivity.this,
						MyResumeActivity.this);
			}
		}
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			// 相册中选取
			case IMAGE_REQUEST_CODE:
				// 位置固定 只能固定大小
				// UploadImg.startPhotoZoom(data.getData(),MyResumeActivity.this,408,534);
				double sd = Double.valueOf(width);
				double d = sd * 1.4;
				if (data.getData() != null) {
					UploadImg.startPhotoZoom(data.getData(),
							MyResumeActivity.this, width, (int) d);
				} else {
					ToastUtil.showShortToast("获取图片失败。。。");
				}
				break;
			// 拍照上传
			case CAMERA_REQUEST_CODE:
				if (Util.hasSdcard()) {
					File tempFile = new File(
							Environment.getExternalStorageDirectory() + "/"
									+ IMAGE_FILE_NAME);
					double sds = Double.valueOf(width);
					double dd = sds * 1.4;
					BitmapFactory.Options opt = new BitmapFactory.Options();
					opt.inJustDecodeBounds = true;
					pngBM = BitmapFactory.decodeFile(
							Environment.getExternalStorageDirectory() + "/"
									+ IMAGE_FILE_NAME, opt);
					// 获取到这个图片的原始宽度和高度
					int picWidth = opt.outWidth;
					int picHeight = opt.outHeight;

					// 获取屏的宽度和高度
					WindowManager windowManager = getWindowManager();
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
					pngBM = BitmapFactory.decodeFile(
							Environment.getExternalStorageDirectory() + "/"
									+ IMAGE_FILE_NAME, opt);
					pngBM = rotateBitmapByDegree(pngBM, degree);
					try {
						// tt_uri =
						// Uri.parse(MediaStore.Images.Media.insertImage(
						// getContentResolver(),
						// Environment.getExternalStorageDirectory() + "/"
						// + IMAGE_FILE_NAME, null, null));
						tt_uri = Uri.parse(MediaStore.Images.Media.insertImage(
								getContentResolver(), pngBM, null, null));
					} catch (Exception e) {
						e.printStackTrace();
						// tt_uri = Uri.fromFile(tempFile);
						return;
					}
					if (tt_uri != null) {
						UploadImg.startPhotoZoom(tt_uri, MyResumeActivity.this,
								width, (int) dd);
					} else {
						showToast("未找到存储卡，无法存储照片");
					}
				} else {
					showToast("未找到存储卡，无法存储照片");
				}
				break;
			case RESULT_REQUEST_CODE:
				// 上传图片后回调上传成功或失败关闭loading加载框
				if (data != null) {
					if (null != type) {
						if (type.equals("1")) {
							loadProgressBar.setVisibility(View.VISIBLE);
							UploadImg.getImageToView(MyResumeActivity.this,
									data, button1, uploadPicUrl, null, "type",
									type, "file", null, "user_id", userId,
									loadProgressBar, null);
						}
						if (type.equals("2")) {
							loadProgressBar.setVisibility(View.VISIBLE);
							UploadImg.getImageToView(MyResumeActivity.this,
									data, button2, uploadPicUrl, null, "type",
									type, "file", null, "user_id", userId,
									loadProgressBar, null);
						}
						if (type.equals("3")) {
							loadProgressBar.setVisibility(View.VISIBLE);
							UploadImg.getImageToView(MyResumeActivity.this,
									data, button3, uploadPicUrl, null, "type",
									type, "file", null, "user_id", userId,
									loadProgressBar, null);
						}
						if (type.equals("4")) {
							loadProgressBar.setVisibility(View.VISIBLE);
							UploadImg.getImageToView(MyResumeActivity.this,
									data, button4, uploadPicUrl, null, "type",
									type, "file", null, "user_id", userId,
									loadProgressBar, null);
						}
						if (type.equals("5")) {
							loadProgressBar.setVisibility(View.VISIBLE);
							UploadImg.getImageToView(MyResumeActivity.this,
									data, button5, uploadPicUrl, null, "type",
									type, "file", null, "user_id", userId,
									loadProgressBar,  null);
						}
						if (type.equals("6")) {
							loadProgressBar.setVisibility(View.VISIBLE);
							UploadImg.getImageToView(MyResumeActivity.this,
									data, button6, uploadPicUrl, null, "type",
									type, "file", null, "user_id", userId,
									loadProgressBar, null);
						}
					} else {
					}
				} else {
				}
				break;
			default:

				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	//
	@OnClick({ R.id.sex_layout, R.id.borthday_layout, R.id.my_xueli_layout,
			R.id.my_jiankongzheng_layout, R.id.my_yifuchima_layout,
			R.id.my_shoos_layout, R.id.my_language_layout })
	public void sexOnclick(View view) {
		switch (view.getId()) {
		case R.id.sex_layout:
			ActionSheet.showSheetSex(MyResumeActivity.this,
					MyResumeActivity.this, MyResumeActivity.this, my_sex);
			break;
		case R.id.borthday_layout:
			ActionSheet.showSheetBorthdayTime(MyResumeActivity.this,
					MyResumeActivity.this, MyResumeActivity.this, my_birthyday);
			break;
		case R.id.my_xueli_layout:
			ActionSheet.showSheetXueli(MyResumeActivity.this,
					MyResumeActivity.this, MyResumeActivity.this, my_xueli);
			break;
		case R.id.my_jiankongzheng_layout:
			ActionSheet.showSheetJianKZ(MyResumeActivity.this,
					MyResumeActivity.this, MyResumeActivity.this,
					my_jiankongzheng);
			break;
		case R.id.my_yifuchima_layout:
			ActionSheet.showSheetYiFZM(MyResumeActivity.this,
					MyResumeActivity.this, MyResumeActivity.this, my_yifuchima);
			break;
		case R.id.my_shoos_layout:
			ActionSheet.showSheetShoos(MyResumeActivity.this,
					MyResumeActivity.this, MyResumeActivity.this, my_shoos);
			break;
		case R.id.my_language_layout:
			ActionSheet.showSheetLanguage(MyResumeActivity.this,
					MyResumeActivity.this, MyResumeActivity.this, my_language);
			break;
		default:
			break;
		}
	}

	@OnClick(R.id.submit)
	public void submitOnclick(View v) {
		saveData();
		if (check()) {
			showWait(true);
			StringRequest request = new StringRequest(Request.Method.POST,
					submintUrl, new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							showWait(false);
							try {
								JSONObject js = new JSONObject(response);
								JSONObject statusjs = js
										.getJSONObject("ResponseStatus");
								int status = statusjs.getInt("status");
								if (status == 2) {
									showToast("更新成功");
									finish();
								} else {
									showToast("用户不存在");
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError volleyError) {
							showWait(false);
						}
					}) {
				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					return JsonHelper.toMap(re);
				}
			};
			queue.add(request);
			request.setRetryPolicy(new DefaultRetryPolicy(
					ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

		}
	}

	@Override
	public void onClick(int whichButton) {

	}

	@OnClick(R.id.mePreview)
	public void toShow(View view) {
		if (isLoadData) {
			saveData();
			Intent intent = new Intent();
			intent.setClass(this, MyResumeScanActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("myResume", re);
			bundle.putBoolean("showtitile", false);
			intent.putExtras(bundle);
			startActivity(intent);
		} else {
			showToast("加载数据失败，请重新加载该页面");
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {

	}

	public void saveData() {
		if (userId != null && !"".equals(userId))
			re.setUser_id(Integer.valueOf(userId));
		/*
		 * if(Util.isEmpty(my_name.getText().toString())){
		 * re.setName(my_name.getText().toString()); }
		 */
		if (!my_sex.getText().toString().equals("未填写")) {
			if (my_sex.getText().toString().equals("女性")) {
				re.setSex(0);
			} else {
				re.setSex(1);
			}
		} else {
			re.setSex(-1);
		}

		if (my_birthyday.getText().toString().equals("未填写")) {
			// re.setBirthdate(my_birthyday.getText().toString());
		} else {
			re.setBirthdate(my_birthyday.getText().toString());
		}
		if (Util.isEmpty(my_shengao.getText().toString())) {
			re.setHeight(Integer.valueOf(my_shengao.getText().toString()));
		} else {
			re.setHeight(0);
		}
		if (Util.isEmpty(my_biye.getText().toString())) {
			re.setGraduate(my_biye.getText().toString());
		} else {
			re.setGraduate("");
		}

		if (!my_xueli.getText().toString().equals("未填写")) {
			re.setEducation(my_xueli.getText().toString());
		}
		// 三围
		if (Util.isEmpty(my_sanwei.getText().toString())) {
			re.setBbh(my_sanwei.getText().toString());
		}
		// 三围end
		if (!my_jiankongzheng.getText().toString().equals("未填写")) {
			if (my_jiankongzheng.getText().toString().equals("无")) {
				re.setHealth_record(0);
			}
			if (my_jiankongzheng.getText().toString().equals("有")) {
				re.setHealth_record(1);
			}
		} else {
			re.setHealth_record(-1);
		}
		if (!my_yifuchima.getText().toString().equals("未填写")) {
			re.setCloth_weight(my_yifuchima.getText().toString());
		}
		if (!my_shoos.getText().toString().equals("未填写")) {
			re.setShoe_weight(Integer.valueOf(my_shoos.getText().toString()));
		} else {
			re.setShoe_weight(0);
		}
		if (!my_language.getText().toString().equals("未填写")) {
			re.setLanguage(my_language.getText().toString());
		}
		if (!my_jinyan.getText().toString().equals("未填写")) {
			re.setSummary(my_jinyan.getText().toString());
		}
	}

	/**
	 * @Description: 加载图片
	 * @author howe
	 * @date 2014-7-30 下午5:57:52
	 * 
	 */

	public void loadpersonPic(final String picName, final ImageView imageView,
			final int isRound) {
		ImageRequest imgRequest = new ImageRequest(Url.GETPIC + picName,
				new Response.Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap arg0) {
						if (isRound == 1) {
						} else {
							imageView.setImageBitmap(arg0);
							OutputStream output = null;
							try {
								File mePhotoFold = new File(
										Environment
												.getExternalStorageDirectory()
												+ "/" + "jzdr/" + "image");
								if (!mePhotoFold.exists()) {
									mePhotoFold.mkdirs();
								}
								output = new FileOutputStream(
										Environment
												.getExternalStorageDirectory()
												+ "/"
												+ "jzdr/"
												+ "image/"
												+ picName);
								arg0.compress(Bitmap.CompressFormat.JPEG, 100,
										output);
								output.flush();
								output.close();
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}
				}, 300, 200, Config.ARGB_8888, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {

					}
				});
		queue.add(imgRequest);
		imgRequest.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	public boolean check() {
		if (!Util.isEmpty(my_sex.getText().toString())
				|| (my_sex.getText().toString().equals("未填写"))) {
			showToast("请输性别");
			return false;
		}

		if (!Util.isEmpty(my_birthyday.getText().toString())
				|| (my_birthyday.getText().toString().equals("未填写"))) {
			showToast("请输入生日");
			return false;
		}
		if (!Util.heightCheck(my_shengao.getText().toString())) {
			showToast("身高范围应该为140-200");
			return false;
		}
		if (!Util.isEmpty(my_biye.getText().toString())) {
			showToast("请输入就读/毕业学校");
			return false;
		}
		if (my_xueli.getText().toString().equals("未填写")) {
			showToast("请选择学历");
			return false;
		}
		if (!Util.isEmpty(my_jinyan.getText().toString())) {
			showToast("经历简述不能为空");
			return false;
		}
		if (MIN_COUNT < 20) {
			showToast("经历简述必须不少于20个字");
			return false;
		}

		return true;
	}

	private TextWatcher mTextWatcher = new TextWatcher() {

		private CharSequence temp;
		private int selectionEnd;

		public void afterTextChanged(Editable s) {
			MIN_COUNT = temp.length();
			selectionEnd = my_jinyan.getSelectionEnd();
			my_jinyan.setSelection(selectionEnd);// 设置光标在最后
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			temp = s;
		}

	};
}
