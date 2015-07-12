package com.quark.image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.carson.constant.ConstantForSaveList;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.qingmu.jianzhidaren.R;
import com.quark.ui.widget.ActionSheet.OnActionSheetSelected;
import com.quark.utils.Util;
import com.quark.utils.WaitDialog;

public class UploadImg {

	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";// 图片名称

	/**
	 * @Description: 将图片设置为圆形头像 pixels为角度比例
	 * @author howe
	 * @date 2014-7-30 下午10:36:02
	 * 
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, float pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);

		paint.setColor(color);

		canvas.drawRoundRect(rectF, bitmap.getWidth() / pixels,
				bitmap.getHeight() / pixels, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * @Description: 使用xutil上传图片 以圆形显示 要设置的ImageView 老师id
	 * @author howe
	 * @date 2014-7-30 下午5:00:32
	 * 
	 */
	public static void uploadpicRound(String path, final ImageView imageView,
			String id, String url) {
		final String pathstr = path;
		// ------使用xutils 修改头像 start-----------
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("teacherId", id);
		params.addBodyParameter("file", new File(path));
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						Log.i("mytag", "conn...");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
							Log.i("mytag", "upload: " + current + "/" + total);
						} else {
							Log.i("mytag", "reply: " + current + "/" + total);
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						Log.i("mytag", "reply: " + responseInfo.result);
						Bitmap bitm = BitmapFactory.decodeFile(pathstr);
						Bitmap output = toRoundCorner(bitm, 2);
						imageView.setImageBitmap(output);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("mytag", "错误：" + error.getExceptionCode() + ":"
								+ msg);
					}
				});
		// ----------修改头像end---------------------
	}

	/**
	 * @Description: 使用xutil上传图片 图片原样输出 要设置的ImageView 老师id
	 * @author howe
	 * @date 2014-7-30 下午5:00:32
	 * 
	 */
	public static void uploadpic(final Activity context, String path,
			final ImageView imageView, String url, final Button warning,
			String paramsOneName, String paramsOneValue, String fileParams,
			String paramsTwoValeu, String paramsThreeName,
			String paramsThreeValeu, final ProgressBar loadProBar) {
		if (context != null) {
			// showWait(true,context);
		}
		final String pathstr = path;
		// ------使用xutils 修改头像 start-----------
		RequestParams params = new RequestParams();
		if (paramsThreeName != null) {
			params.addQueryStringParameter(paramsThreeName, paramsThreeValeu);
		}
		if (paramsOneName != null) {
			params.addQueryStringParameter(paramsOneName, paramsOneValue);
		}
		params.addBodyParameter(fileParams, new File(path));
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						Log.i("mytag", "conn...");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
							Log.i("mytag", "upload: " + current + "/" + total);
						} else {
							Log.i("mytag", "reply: " + current + "/" + total);
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// responseInfor:pic:142890833507zddfd.jpg,status:1
						if (loadProBar != null) {
							loadProBar.setVisibility(View.INVISIBLE);
						}
						// context.sendBroadcast(new
						// Intent("com.carson.uploadpic"));
						Toast mToast = Toast.makeText(context, "上传成功", 0);
						mToast.setGravity(Gravity.CENTER, 0, 0);
						mToast.show();
						Bitmap bitm = BitmapFactory.decodeFile(pathstr);
						// Bitmap bitm = MyResumeActivity.zoomImg(
						// new File(pathstr), 300, 300);
						imageView.setImageBitmap(bitm);
						// 保存上传后的图片url,下载到本地
						OutputStream output = null;
						try {
							JSONObject json = new JSONObject(
									responseInfo.result);
							String pic = (String) json.get("pic");
							File mePhotoFold = new File(Environment
									.getExternalStorageDirectory()
									+ "/"
									+ "jzdr/" + "image");
							if (!mePhotoFold.exists()) {
								mePhotoFold.mkdirs();
							}
							output = new FileOutputStream(Environment
									.getExternalStorageDirectory()
									+ "/"
									+ "jzdr/" + "image/" + pic);
							bitm.compress(Bitmap.CompressFormat.JPEG, 100,
									output);
							output.flush();
							output.close();
						} catch (Exception e) {
							e.printStackTrace();
						}

						if (warning != null) {
							warning.setVisibility(View.INVISIBLE);
						}
						// showWait(false,context);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// context.sendBroadcast(new
						// Intent("com.carson.uploadpic"));
						Toast mToast = Toast.makeText(context, "上传失败，请重试", 0);
						mToast.setGravity(Gravity.CENTER, 0, 0);
						mToast.show();
						if (loadProBar != null) {
							loadProBar.setVisibility(View.INVISIBLE);
						}
					}
				});
		// ----------修改头像end---------------------
	}

	protected static WaitDialog dialog;

	// protected static void showWait(boolean isShow,Context context) {
	// if (isShow) {
	// if (null == dialog) {
	// dialog = new WaitDialog(context);
	// }
	// dialog.show();
	// } else {
	// if (null != dialog) {
	// dialog.dismiss();
	// }
	// }
	// }

	/**
	 * 显示选择对话框
	 */
	public static Dialog showSheetPic(Context context,
			final OnActionSheetSelected actionSheetSelected,
			OnCancelListener cancelListener, final Activity activity) {
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
					// carson tianjia orientation
					intentFromCapture.putExtra(
							MediaStore.Images.Media.ORIENTATION, 0);
				} else {
					Toast mToast = Toast.makeText(activity, "未找到存储卡，无法存储照片", 0);
					mToast.setGravity(Gravity.CENTER, 0, 0);
					mToast.show();
					// Toast.makeText(activity,
					// "未找到存储卡，无法存储照片！",Toast.LENGTH_LONG).show();
				}
				activity.startActivityForResult(intentFromCapture,
						CAMERA_REQUEST_CODE);
				dlg.dismiss();
			}
		});

		mTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentFromGallery = new Intent();
				intentFromGallery.setType("image/*"); // 设置文件类型
				intentFromGallery.setAction(Intent.ACTION_PICK);
				activity.startActivityForResult(intentFromGallery,
						IMAGE_REQUEST_CODE);
				dlg.dismiss();
			}
		});

		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public static void startPhotoZoom(Uri uri, final Activity activity, int x,
			int y) {

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
		activity.startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	/**
	 * @param activity
	 *            上下文
	 * @param data
	 *            数据
	 * @param img
	 *            会显ImageView
	 * @param url
	 *            上传图片url
	 * @param warning
	 *            提示button
	 * @param paramsOneName
	 * @param paramsOneValue
	 * @param paramsTwoName
	 *            文件名称
	 * @param paramsTwoValeu
	 * @param paramsThreeName
	 * @param paramsThreeValeu
	 */
	public static void getImageToView(Activity activity, Intent data,
			ImageView img, String url, Button warning, String paramsOneName,
			String paramsOneValue, String paramsTwoName, String paramsTwoValeu,
			String paramsThreeName, String paramsThreeValeu,
			ProgressBar loadProBar) {

		// 有的手机如Htc intent.getextras()为空
		Bundle extras = data.getExtras();
		if (extras != null) {
			// ----问题:有的手机(如s4)拍照上传得到的uri为空
			Uri uri = data.getData(); // 可以得到图片在Content：//。。。中的地址，把它转化成绝对地址如下
			Log.e("carson_uri", uri + ":");
			String[] proj = { MediaStore.Images.Media.DATA };
			String imagePath = "";
			// carson 抛出拍照上传异常
			Cursor cursor = null;
			try {
				cursor = activity.managedQuery(uri, proj, null, null, null);
			} catch (Exception e) {
				return;
			}
			if (cursor != null) {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				if (cursor.getCount() > 0 && cursor.moveToFirst()) {
					imagePath = cursor.getString(column_index);
					uploadpic(activity, imagePath, img, url, warning,
							paramsOneName, paramsOneValue, paramsTwoName,
							paramsTwoValeu, paramsThreeName, paramsThreeValeu,
							loadProBar);
				}
			} else {
			}
			//
			// ----
		} else {
			// 有的手机不能通过intent.getdata获取照片.用之前保存的uri记录代替
			Uri uri = ConstantForSaveList.uploadUri; // 可以得到图片在Content：//。。。中的地址，把它转化成绝对地址如下
			Log.e("carson_uri", uri + ":");
			String[] proj = { MediaStore.Images.Media.DATA };
			String imagePath = "";
			// carson 抛出拍照上传异常
			Cursor cursor = null;
			try {
				cursor = activity.managedQuery(uri, proj, null, null, null);
			} catch (Exception e) {
				return;
			}

			if (cursor != null) {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				if (cursor.getCount() > 0 && cursor.moveToFirst()) {
					imagePath = cursor.getString(column_index);
					uploadpic(activity, imagePath, img, url, warning,
							paramsOneName, paramsOneValue, paramsTwoName,
							paramsTwoValeu, paramsThreeName, paramsThreeValeu,
							loadProBar);
				}
			} else {
			}

		}
	}

}
