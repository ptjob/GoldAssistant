package com.quark.us;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.carson.constant.IDCard;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qingmu.jianzhidaren.R;
import com.quark.common.JsonUtil;
import com.quark.common.Url;
import com.quark.image.UploadImg;
import com.quark.jianzhidaren.BaseActivity;
import com.parttime.main.MainTabActivity;
import com.quark.model.AuthenticationResponse;
import com.quark.ui.widget.ActionSheet.OnActionSheetSelected;
import com.quark.ui.widget.CustomDialog;
import com.quark.ui.widget.CustomDialogTwo;
import com.quark.utils.Util;

/**
 * 我---认证 商家端
 * 
 * @author Administrator
 * 
 */
@SuppressLint("NewApi")
public class AuthenticationActivity extends BaseActivity implements
		OnActionSheetSelected, OnCancelListener {

	private String url;
	private String parameter;
	private String userId;
	private String uploadidUrl;
	private String uploadidUrl_zheng;
	private String uploadidUrl_fan;
	private String urlSubmit;
	private String realNameStr;
	private String idCardStr;
	private int option = 1;// 1上传正面照 2上传反面照 3上传执照
	private AuthenticationResponse autrsp;
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";// 图片名称
	private int userStatus; // 审核通过 未通过
	public boolean isUploadsfz_zheng = false;
	public boolean isUploadsfz_fan = false;
	public boolean isUploads_qiyepic = false;

	@ViewInject(R.id.real_name)
	EditText real_name;

	@ViewInject(R.id.idCode)
	EditText idCode;
	// 真实姓名错误提示
	@ViewInject(R.id.icon_warn_name)
	Button icon_warn_name;
	// 身份证错误提示
	@ViewInject(R.id.icon_warn_code)
	Button icon_warn_code;

	// 身份证图片
	@ViewInject(R.id.pic_idCard)
	ImageView pic_idCard;
	// 身份证图片错误提示
	@ViewInject(R.id.icon_warn_idCard)
	Button icon_warn_idCard;
	// 身份证图片xiugai按钮
	@ViewInject(R.id.updateIdCard)
	Button updateIdCard;

	// 身份证图片
	@ViewInject(R.id.pic_idCard_back)
	ImageView pic_idCard_back;
	// 身份证图片错误提示
	@ViewInject(R.id.icon_warn_idCard_back)
	Button icon_warn_idCard_back;
	// 身份证图片提交按钮
	@ViewInject(R.id.updateIdCard_back)
	Button updateIdCard_back;
	// 提交按钮
	@ViewInject(R.id.verify)
	Button verify;
	@ViewInject(R.id.isPass)
	ImageView isPass;
	@ViewInject(R.id.name_text)
	TextView name_text;
	@ViewInject(R.id.idcode_text)
	TextView idcode_text;
	private int width;
	private Bitmap userPhotoBmp = null;// 上传图像
	private ProgressBar proBar1, proBar2;// 身份证正、反、营业执照

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.me_authentication);
		ViewUtils.inject(this);
		setTopTitle("经纪人认证");
		setBackButton();
		// 上传图片加载框
		proBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		proBar2 = (ProgressBar) findViewById(R.id.progressBar2);
		// 获取屏幕宽度
		WindowManager wm = this.getWindowManager();
		width = wm.getDefaultDisplay().getWidth();
		SharedPreferences sp = getSharedPreferences("jrdr.setting",
				MODE_PRIVATE);
		userId = sp.getString("userId", "");
		RelativeLayout topLayout = (RelativeLayout) findViewById(R.id.top_title_layout);
		topLayout.setBackgroundColor(getResources().getColor(
				R.color.guanli_common_color));

		url = Url.COMPANY_yanzheng + "?token=" + MainTabActivity.token;
		parameter = "company_id";
		uploadidUrl_zheng = Url.COMPANY_uploadIdcard_zheng + "?token="
				+ MainTabActivity.token;
		uploadidUrl_fan = Url.COMPANY_uploadIdcard_fan + "?token="
				+ MainTabActivity.token;
		urlSubmit = Url.COMPANY_shenheSubmit + "?token="
				+ MainTabActivity.token;
		getStatus();

	}

	/**
	 * 获取数据
	 */
	public void getStatus() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						Log.e("tag", response);
						try {
							JSONObject js = new JSONObject(response);
							int type = js.getInt("ResponseType");
							if (type == 1) {
								// 表示经纪人端
								JSONObject contentjs = js
										.getJSONObject("InfoResponse");
								autrsp = (AuthenticationResponse) JsonUtil
										.jsonToBean(contentjs,
												AuthenticationResponse.class);
								real_name.setText(autrsp.getName());
								if (autrsp.getName() != null
										&& !"".equals(autrsp.getName())) {
									real_name.setSelection(autrsp.getName()
											.length());// 设置光标在尾部
								}
								idCode.setText(autrsp.getIdentity());
								if (autrsp.getIdentity() != null
										&& !"".equals(autrsp.getIdentity())) {
									idCode.setSelection(autrsp.getIdentity()
											.length());
								}
								// volley加载图片
								if (autrsp.getIdentity_front() != null
										&& !"".equals(autrsp
												.getIdentity_front())) {
									isUploadsfz_zheng = true;
									loadpersonPic(
											Url.GETPIC
													+ autrsp.getIdentity_front(),
											pic_idCard, 0);
								}
								if (autrsp.getIdentity_verso() != null
										&& !"".equals(autrsp
												.getIdentity_verso())) {
									isUploadsfz_fan = true;
									loadpersonPic(
											Url.GETPIC
													+ autrsp.getIdentity_verso(),
											pic_idCard_back, 0);
								}
								userStatus = contentjs.getInt("status");
								if (userStatus == 0) {
									verify.setText("提交兼职达人审核");
								} else if (userStatus == 1) {
									verify.setText("审核中···");
									verify.setBackgroundResource(R.drawable.btn_changingcolor_gray);
									real_name.setClickable(false);
									idCode.setClickable(false);
									verify.setClickable(false);
									updateIdCard.setVisibility(View.GONE);
									updateIdCard_back.setVisibility(View.GONE);
								} else if (userStatus == 2) {// 审核通过
									real_name.setClickable(false);
									idCode.setClickable(false);
									icon_warn_idCard
											.setVisibility(View.INVISIBLE);
									icon_warn_idCard_back
											.setVisibility(View.INVISIBLE);

									verify.setVisibility(View.GONE);
									verify.setClickable(false);
									updateIdCard.setVisibility(View.GONE);
									updateIdCard_back.setVisibility(View.GONE);
									isPass.setVisibility(View.VISIBLE);
								} else if (userStatus == 3) {
									verify.setText("审核失败，重新提交");
									verify.setBackgroundResource(R.drawable.btn_changingcolor_gray);
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showToast("提交失败");
						showWait(false);
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put(parameter, userId);
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	// 提交数据
	@OnClick(R.id.verify)
	public void submitOnclick(View v) {
		realNameStr = real_name.getText().toString();
		idCardStr = idCode.getText().toString();
		if (check()) {
			showWait(true);
			StringRequest request = new StringRequest(Request.Method.POST,
					urlSubmit, new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							showWait(false);
							try {
								JSONObject js = new JSONObject(response);
								JSONObject statusjs = js
										.getJSONObject("ResponseStatus");
								int status = statusjs.getInt("status");
								if (status == 3) {
									String title = statusjs.getString("title");
									String msg = statusjs.getString("msg");
									String confirm = statusjs
											.getString("confirm");
									showAlertDialog(msg, title, confirm);
								} else if (status == 2) {
									succeedDialog();
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
							showToast("提交失败");
						}
					}) {
				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					Map<String, String> map = new HashMap<String, String>();
					map.put(parameter, userId);
					map.put("name", realNameStr);
					map.put("identity", idCardStr);
					return map;
				}
			};
			queue.add(request);
			request.setRetryPolicy(new DefaultRetryPolicy(
					ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

		}
	}

	public boolean check() {

		int isUploadidCard = icon_warn_idCard.getVisibility(); // gone= 8
																// invisiable =
																// 4 visiable =0

		if (isUploadidCard == 0) {
			if (!isUploadsfz_zheng) {
				icon_warn_idCard.setVisibility(View.VISIBLE);
				showToast("请上传身份证正面图片");
				return false;
			}
		}

		icon_warn_idCard.setVisibility(View.INVISIBLE);

		int isUploadidCardBack = icon_warn_idCard_back.getVisibility();
		// Log.e("error","身份证正面图片="+isUploadidCardBack);
		if (isUploadidCardBack == 0) {
			if (!isUploadsfz_fan) {
				icon_warn_idCard_back.setVisibility(View.VISIBLE);
				showToast("请上传身份证背面图片");
				return false;
			}
		}
		icon_warn_idCard_back.setVisibility(View.INVISIBLE);

		if (!Util.isEmpty(realNameStr)) {
			icon_warn_name.setVisibility(View.VISIBLE);
			showToast("姓名不能为空");
			return false;
		}
		icon_warn_name.setVisibility(View.INVISIBLE);

		if (!Util.isIdCard(idCardStr)) {
			icon_warn_code.setVisibility(View.VISIBLE);
			showToast("请输入正确的身份证号码");
			return false;
		} else {
			String str = IDCard.IDCardValidate(idCardStr);
			if (!"".equals(str)) {
				icon_warn_code.setVisibility(View.VISIBLE);
				showToast(str);
				return false;
			}
		}
		icon_warn_code.setVisibility(View.INVISIBLE);

		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
			if (userStatus == 0 || userStatus == 3) {
				exitApp();
			} else {
				finish();
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			// 监控/拦截菜单键
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {
			// 由于Home键为系统键，此处不能捕获，需要重写onAttachedToWindow()
		}
		return super.onKeyDown(keyCode, event);
	}

	// 点击退出
	public void exitApp() {
		CustomDialogTwo.Builder builder = new CustomDialogTwo.Builder(this);
		builder.setMessage("完成实名认证，才能发布兼职活动哦");
		builder.setTitle("温馨提示");

		builder.setPositiveButton("确认退出",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});
		builder.setNegativeButton("继续认证",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				});
		builder.create().show();
	}

	// 成功提示
	public void succeedDialog() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage("您的实名认证申请已经成功提交，系统将在1~2个工作日内完成审核");
		builder.setTitle("提交成功");

		builder.setPositiveButton("我知道了",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});
		builder.create().show();
	}

	// 提交失败提示
	private void showAlertDialog(String str, String str2, String str3) {
		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(str);
		builder.setTitle(str2);

		builder.setPositiveButton(str3, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// finish();
			}
		});
		builder.create().show();
	}

	// ==============图片上传===========
	// 正面身份证
	@OnClick(R.id.updateIdCard)
	public void updateIdCardOnclick(View v) {
		option = 1;
		uploadidUrl = uploadidUrl_zheng;
		UploadImg.showSheetPic(AuthenticationActivity.this,
				AuthenticationActivity.this, AuthenticationActivity.this,
				AuthenticationActivity.this);
	}

	// 反面身份证
	@OnClick(R.id.updateIdCard_back)
	public void updateIdCardBackOnclick(View v) {
		option = 2;
		uploadidUrl = uploadidUrl_fan;
		UploadImg.showSheetPic(AuthenticationActivity.this,
				AuthenticationActivity.this, AuthenticationActivity.this,
				AuthenticationActivity.this);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	private void startPhotoZoom(Uri uri, int x, int y) {
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// ****************************之前的拍照先取消**end********************************
		// 结果码不等于取消时候
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				// 位置固定 只能固定大小
				if (data.getData() != null) {
					startPhotoZoom(data.getData(), 360, 224);
				} else {
					showToast("获取图片失败...");
				}
				break;
			case CAMERA_REQUEST_CODE:
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
					userPhotoBmp = BitmapFactory.decodeFile(
							Environment.getExternalStorageDirectory() + "/"
									+ IMAGE_FILE_NAME, opt);
					userPhotoBmp = rotateBitmapByDegree(userPhotoBmp, degree);

					Uri tt_uri = null;
					try {
						tt_uri = Uri
								.parse(MediaStore.Images.Media.insertImage(
										getContentResolver(), userPhotoBmp,
										null, null));
					} catch (Exception e) {
						e.printStackTrace();
						tt_uri = Uri.fromFile(tempFile);
					}
					if (tt_uri != null) {
						startPhotoZoom(tt_uri, 360, 224);
					} else {
						showToast("未找到存储卡，无法存储照片");
					}
				} else {
					showToast("未找到存储卡，无法存储照片");
				}
				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					// 企业
					if (option == 1) {
						proBar1.setVisibility(View.VISIBLE);
						UploadImg.getImageToView(AuthenticationActivity.this,
								data, pic_idCard, uploadidUrl,
								icon_warn_idCard, "company_id", userId,
								"identity_front", null, null, null, proBar1);
					} else if (option == 2) {
						proBar2.setVisibility(View.VISIBLE);
						UploadImg.getImageToView(AuthenticationActivity.this,
								data, pic_idCard_back, uploadidUrl,
								icon_warn_idCard_back, "company_id", userId,
								"identity_verso", null, null, null, proBar2);
					}

				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// ==============图片上传end===========

	/**
	 * @Description: 加载图片
	 * @author howe
	 * @date 2014-7-30 下午5:57:52
	 * 
	 */
	public void loadpersonPic(String url, final ImageView imageView,
			final int isRound) {
		ImageRequest imgRequest = new ImageRequest(url,
				new Response.Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap arg0) {
						if (isRound == 1) {
							// Bitmap output=Util.toRoundCorner(arg0, 2);
							// imageView.setImageBitmap(output);
						} else {
							LayoutParams params = new LayoutParams(width - 30,
									(width - 30) * 380 / 640);
							// params.setMargins(15, 0, 0, 0);
							//
							// imageView.setLayoutParams(params );
							imageView.setImageBitmap(arg0);
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

	@Override
	public void onCancel(DialogInterface dialog) {

	}

	@Override
	public void onClick(int whichButton) {

	}

}
