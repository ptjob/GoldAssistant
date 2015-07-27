package com.parttime.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.qingmu.jianzhidaren.R;
import com.quark.common.JsonUtil;
import com.quark.common.Url;
import com.quark.fragment.company.BaseFragment;
import com.quark.http.image.CircularImage;
import com.quark.http.image.LoadImage;
import com.quark.image.UploadImg;
import com.quark.model.Function;
import com.quark.ui.widget.ActionSheet.OnActionSheetSelected;
import com.quark.us.AuthenticationActivity;
import com.quark.us.ReputationAuthorityActivity;
import com.quark.utils.Util;
import com.thirdparty.alipay.RechargeActivity;

/**
 * 商家 功能
 * 
 * @author C罗
 * 
 */
public class IntroduceFragment extends BaseFragment implements
		View.OnClickListener, OnActionSheetSelected, OnCancelListener {

	private static final String TAG = "IntroduceFragment";
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";// 图片名称
	private String uploadAvatarUrl;
	CircularImage cover_user_photo;

	private Bitmap userPhotoBmp = null;
	private SharedPreferences sp;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment IntroduceFragment.
	 */
	public static IntroduceFragment newInstance(String param1, String param2) {
		IntroduceFragment fragment = new IntroduceFragment();
		return fragment;
	}

	public IntroduceFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private View Roster, reputationvalue_com, authdata_layout;
	private String company_id, url;
	Function function = new Function();
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_introduce_company, container,
				false);

		sp = getActivity().getSharedPreferences("jrdr.setting",
				Context.MODE_PRIVATE);
		company_id = sp.getString("userId", "");
		url = Url.COMPANY_function + "?token=" + MainTabActivity.token;
		uploadAvatarUrl = Url.COMPANY_upload_avatar + "?token="
				+ MainTabActivity.token;

		// 初始化界面
		// 花名册
		// Roster = view.findViewById(R.id.Roster);
		// initComontView(Roster, R.string.Roster, false);
		// 资料认证
		authdata_layout = view.findViewById(R.id.authdata_layout);
		initComontView(authdata_layout, R.string.authentication_data, false, 1);

		// 关于信誉值与诚意金
		reputationvalue_com = view.findViewById(R.id.reputationvalue_com);
		initComontView(reputationvalue_com, R.string.reputationvalue_com,
				false, 2);

		Button recharge = (Button) view.findViewById(R.id.recharge);
		recharge.setOnClickListener(toRechargeListener);
		return view;
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
				cover_user_photo
						.setImageBitmap(LoadImage.toRoundBitmap(bb_bmp));
			}
		}
	}

	/**
	 * 先加载缓存
	 * 
	 */
	private void loadHuncunFirst() {
		TextView textView = (TextView) view.findViewById(R.id.name);
		textView.setText(sp.getString(company_id + "name", "未知"));
		ImageView yan_img = (ImageView) view.findViewById(R.id.yan_img);
		if (sp.getInt(company_id + "status", 0) == 2) {
			yan_img.setImageResource(R.drawable.my_certified);
		} else {
			yan_img.setImageResource(R.drawable.my_unauthorized);
			yan_img.setOnClickListener(yanzhengOnclick);
		}

		TextView money = (TextView) view.findViewById(R.id.money);
		money.setText(sp.getInt(company_id + "money", 0) + "元");
		cover_user_photo = (CircularImage) view
				.findViewById(R.id.cover_user_photo);
		if (sp.getString("c" + company_id + "_photo", "") == null
				|| sp.getString("c" + company_id + "_photo", "").equals("")) {
			cover_user_photo.setImageDrawable(getResources().getDrawable(
					R.drawable.photo_male));
		} else {
			loadNativePhotoFirst();
		}

		TextView renzheng = (TextView) view.findViewById(R.id.renzheng);
		if (sp.getInt(company_id + "status", 0) == 2) {
			renzheng.setText("已认证");
		} else {
			renzheng.setText("未认证");
		}
	}

	/**
	 * 保存商家信息
	 */
	private void saveInfor() {
		Editor edt = sp.edit();
		edt.putString(company_id + "name", function.getName());
		edt.putString("c" + company_id + "realname", function.getName());
		edt.putInt(company_id + "status", function.getStatus());
		edt.putFloat(company_id + "money",
				function.getMoney() > 0 ? function.getMoney() : 0);
		edt.putString("c" + company_id + "_photo", function.getAvatar());
		edt.putString(company_id + "_photo", function.getAvatar());
		edt.commit();
	}

	/**
	 * 判断本地是否存储了之前的照片
	 * 
	 */

	private void checkPhotoExits(String picName, ImageView iv) {
		File mePhotoFold = new File(Environment.getExternalStorageDirectory()
				+ "/" + "jzdr/" + "image");
		if (!mePhotoFold.exists()) {
			mePhotoFold.mkdirs();
		}
		File f = new File(Environment.getExternalStorageDirectory() + "/"
				+ "jzdr/" + "image/" + picName);
		if (f.exists()) {
			// Bitmap bb_bmp = MyResumeActivity.zoomImg(f, 300, 300);
			Bitmap bb_bmp = BitmapFactory.decodeFile(Environment
					.getExternalStorageDirectory()
					+ "/"
					+ "jzdr/"
					+ "image/"
					+ picName);
			if (bb_bmp != null) {
				iv.setImageBitmap(LoadImage.toRoundBitmap(bb_bmp));
			} else {
				loadpersonPic(picName, iv, 0);
			}

		} else {
			loadpersonPic(picName, iv, 0);
		}

	}

	/**
	 * @Description: 加载图片
	 * @author howe
	 * @date 2014-7-30 下午5:57:52
	 * 
	 */

	private void loadpersonPic(final String picName, final ImageView imageView,
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

	/**
	 * 获取商家信息
	 * 
	 */
	private void initMy() {
		StringRequest stringRequest = new StringRequest(Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject js = new JSONObject(response);
							function = (Function) JsonUtil.jsonToBean(js,
									Function.class);
							initView();
							saveInfor();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> map = new HashMap<String, String>();
				map.put("company_id", company_id);
				return map;
			}
		};
		queue.add(stringRequest);
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (null == userPhotoBmp) {

		} else {
			userPhotoBmp.recycle();
			userPhotoBmp = null;
		}
	}

	protected void initView() {
		TextView textView = (TextView) view.findViewById(R.id.name);
		textView.setText(function.getName());
		ImageView yan_img = (ImageView) view.findViewById(R.id.yan_img);
		if (function.getStatus() == 2) {
			yan_img.setImageResource(R.drawable.my_certified);
		} else {
			yan_img.setImageResource(R.drawable.my_unauthorized);
			yan_img.setOnClickListener(yanzhengOnclick);
		}

		TextView money = (TextView) view.findViewById(R.id.money);
		money.setText((function.getMoney() > 0 ? function.getMoney() : 0) + "元");
		cover_user_photo = (CircularImage) view
				.findViewById(R.id.cover_user_photo);
		if (function.getAvatar() == null || function.getAvatar().equals("")) {
			if (isAdded()) {
				cover_user_photo.setImageDrawable(getResources().getDrawable(
						R.drawable.photo_male));
			}
		} else {
			checkPhotoExits(function.getAvatar(), cover_user_photo);

		}
		cover_user_photo.setOnClickListener(chargeOnclick);

		TextView renzheng = (TextView) view.findViewById(R.id.renzheng);
		if (function.getStatus() == 2) {
			renzheng.setText("已认证");
		} else {
			renzheng.setText("未认证");
		}

	}

	/**
	 * 验证黑户
	 */
	OnClickListener yanzhengOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mActivity, AuthenticationActivity.class);
			startActivity(intent);
		}
	};

	/**
	 * 修改头像
	 */
	OnClickListener chargeOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			showSheetPic();
		}
	};

	OnClickListener toRechargeListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(getActivity(), RechargeActivity.class);
			startActivity(intent);
		}
	};

	private void initComontView(View view, int titleId, boolean showNum,
			int position) {
		view.setOnClickListener(layoutListener);
		((TextView) view.findViewById(R.id.me_title)).setText(titleId);
		((Button) view.findViewById(R.id.jobtitlenum))
				.setVisibility(showNum ? View.VISIBLE : View.INVISIBLE);
		((ImageView) view.findViewById(R.id.me_enter)).setTag(view);
		((ImageView) view.findViewById(R.id.me_enter)).setOnClickListener(this);
		ImageView iconImv = (ImageView) view.findViewById(R.id.left_icon_imv);
		if (position == 1) {
			iconImv.setImageResource(R.drawable.my_realname);
		} else if (position == 2) {
			iconImv.setImageResource(R.drawable.my_credibility);

		}
	}

	private View.OnClickListener layoutListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			handlerClickIntent(v.getId());
		}
	};

	// 响应点击事件
	private void handlerClickIntent(int viewId) {
		Intent intent = null;
		switch (viewId) {
		// case R.id.Roster:
		// intent = new Intent(mActivity, RosterActivity.class);
		// break;
		case R.id.authdata_layout:
			SharedPreferences sp = getActivity().getSharedPreferences(
					"jrdr.setting", getActivity().MODE_PRIVATE);

			intent = new Intent(mActivity, AuthenticationActivity.class);
			break;
		// 关于信誉值和诚意金
		case R.id.reputationvalue_com:
			intent = new Intent(mActivity, ReputationAuthorityActivity.class);
			break;
		}
		if (null != intent) {
			startActivity(intent);
		}
	}

	@Override
	public void onClick(View v) {
		View parnet = (View) v.getTag();
		handlerClickIntent(parnet.getId());
	}

	@Override
	public void onCancel(DialogInterface dialog) {

	}

	@Override
	public void onClick(int whichButton) {

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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 结果码不等于取消时候
		if (resultCode != getActivity().RESULT_CANCELED) {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				if (data.getData() != null) {
					startPhotoZoom(data.getData(), 300, 300);
				} else {
					Toast mToast = Toast.makeText(getActivity(), "获取图片失败。。。",
							Toast.LENGTH_LONG);
					mToast.setGravity(Gravity.CENTER, 0, 0);
					mToast.show();
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
				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					Toast mToast = Toast.makeText(getActivity(), "正在上传头像,请稍候。",
							Toast.LENGTH_LONG);
					mToast.setGravity(Gravity.CENTER, 0, 0);
					mToast.show();
					UploadImg.getImageToView(getActivity(), data,
							cover_user_photo, uploadAvatarUrl, null, null,
							null, "avatar", null, "company_id", company_id,
							null, null);
				}
				break;
			}
		}
	}

	// ========头像上传 不可抽取出==================
	/**
	 * 显示选择对话框
	 */
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

	// ========头像上传 不可抽取出end==================

	@Override
	public void onResume() {
		super.onResume();
		// 先加载缓存
		loadHuncunFirst();
		// 更新花名册数字
		// 个人信息
		initMy();
	}

}
