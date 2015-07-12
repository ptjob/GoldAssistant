package com.carson.loadpic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.carson.constant.ConstantForSaveList;
import com.quark.common.Url;
import com.quark.http.image.LoadImage;
import com.quark.volley.VolleySington;

public class CarsonLoadPic {

	/**
	 * 判断本地是否存储了之前的照片
	 * 
	 */

	public static void checkPhotoExits(String picName, ImageView iv) {
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

	@SuppressWarnings("deprecation")
	public static void loadpersonPic(final String picName,
			final ImageView imageView, final int isRound) {
		RequestQueue queue = VolleySington.getInstance().getRequestQueue();
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
	 * 将给定图片维持宽高比缩放后，截取正中间的正方形部分。
	 * 
	 * @param bitmap
	 *            原图
	 * @param edgeLength
	 *            希望得到的正方形部分的边长
	 * @return 缩放截取正中部分后的位图。
	 */
	public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
		if (null == bitmap || edgeLength <= 0) {
			return null;
		}

		Bitmap result = bitmap;
		int widthOrg = bitmap.getWidth();
		int heightOrg = bitmap.getHeight();

		if (widthOrg > edgeLength && heightOrg > edgeLength) {
			// 压缩到一个最小长度是edgeLength的bitmap
			int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math
					.min(widthOrg, heightOrg));
			int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
			int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
			Bitmap scaledBitmap;

			try {
				scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
						scaledHeight, true);
			} catch (Exception e) {
				return null;
			}

			// 从图中截取正中间的正方形部分。
			int xTopLeft = (scaledWidth - edgeLength) / 2;
			int yTopLeft = (scaledHeight - edgeLength) / 2;

			try {
				result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft,
						edgeLength, edgeLength);
				if (scaledBitmap != null && (!scaledBitmap.isRecycled())
						&& scaledBitmap != result) {
					scaledBitmap.recycle();
				}
			} catch (Exception e) {
				return null;
			}
		} else {
			// 异常图片，长宽小于130

			int scaledWidth = widthOrg;
			int scaledHeight = heightOrg;

			Bitmap scaledBitmap;
			try {
				scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
						scaledHeight, true);
			} catch (Exception e) {
				return null;
			}
			int xTopLeft, yTopLeft;
			// 从图中截取正中间的正方形部分。
			if (scaledWidth >= scaledHeight) {
				xTopLeft = (scaledWidth - scaledHeight) / 2;
				yTopLeft = 0;
			} else {
				xTopLeft = 0;
				yTopLeft = (scaledHeight - scaledWidth) / 2;

			}

			int min_temp_xy = Math.min(scaledWidth, scaledHeight);
			try {
				result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft,
						min_temp_xy, min_temp_xy);
				if (scaledBitmap != null && (!scaledBitmap.isRecycled())
						&& scaledBitmap != result) {
					scaledBitmap.recycle();
				}
			} catch (Exception e) {
				return null;
			}
		}

		return result;
	}
}
