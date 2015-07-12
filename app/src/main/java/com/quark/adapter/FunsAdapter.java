package com.quark.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.ImageRequest;
import com.carson.constant.ConstantForSaveList;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.http.image.CircularImage;
import com.quark.http.image.LoadImage;
import com.quark.model.FunsBean;
import com.quark.volley.VolleySington;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FunsAdapter extends BaseAdapter {

	private ArrayList<FunsBean> list;
	private Context context;
	protected RequestQueue queue;
	int[] heartImg = { R.id.xinyi_bt1, R.id.xinyi_bt2, R.id.xinyi_bt3,
			R.id.xinyi_bt4, R.id.xinyi_bt5, R.id.xinyi_bt6, R.id.xinyi_bt7,
			R.id.xinyi_bt8, R.id.xinyi_bt9, R.id.xinyi_bt10 };
	ViewHolder holder;

	public FunsAdapter(Context context, ArrayList<FunsBean> list) {
		this.context = context;
		this.list = list;
		queue = VolleySington.getInstance().getRequestQueue();
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list == null ? null : list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int i, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_funs_activity, null);
			holder.cover_user_photo = (CircularImage) convertView
					.findViewById(R.id.bm_headpic);
			holder.name = (TextView) convertView.findViewById(R.id.bm_title);
			holder.sexImv = (ImageView) convertView
					.findViewById(R.id.item_sex_imv);
			holder.age = (TextView) convertView.findViewById(R.id.bm_age);
			holder.renzhengImv = (ImageView) convertView
					.findViewById(R.id.bm_yan);
			holder.cyjImv = (ImageView) convertView.findViewById(R.id.bm_cyj);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(i).getUser_name());
		if (list.get(i).getSex() == 1) {
			holder.sexImv.setImageResource(R.drawable.my_men);
		} else {
			holder.sexImv.setImageResource(R.drawable.my_women);
		}
		holder.age.setText(list.get(i).getAge() + "岁");
		addXinToView(list.get(i).getCreditworthiness(), convertView);// 信誉值:步长为5,半个心
		// cyj
		if (list.get(i).getEarnest_money() == 1) {
			holder.cyjImv.setImageResource(R.drawable.my_margin);
		} else {// 前往充值
			holder.cyjImv.setImageResource(R.drawable.my_ordinary);
		}
		// 认证
		if (list.get(i).getCertification() == 2) {
			holder.renzhengImv.setImageResource(R.drawable.my_certified);
		} else {
			// 前往验证
			holder.renzhengImv.setImageResource(R.drawable.my_unauthorized);
		}
		// 给图片设置tag,防止异步加载图像错乱
		holder.cover_user_photo.setTag(list.get(i).getPicture_1());

		if (list.get(i).getPicture_1() == null
				|| list.get(i).getPicture_1().equals("")) {
			holder.cover_user_photo
					.setImageResource(R.drawable.pic_default_user);
		} else {
			checkPhotoExits(list.get(i).getPicture_1(), holder.cover_user_photo);
		}
		return convertView;
	}

	/**
	 * 信誉值
	 * 
	 * @param xin
	 * @param convertView
	 */
	private void addXinToView(int xin, View convertView) {
		if (xin > 0) {
			int heartCount = xin / 10;
			int heartHeart = xin % 10;
			int j = 0;
			if (heartCount > 9) {
				ImageView imageView = (ImageView) convertView
						.findViewById(heartImg[0]);
				imageView.setVisibility(View.VISIBLE);
				imageView.setImageResource(R.drawable.icon_heart_ten);
				for (int a = 1; a < 9; a++) {
					ImageView imageView2 = (ImageView) convertView
							.findViewById(heartImg[a]);
					imageView2.setVisibility(View.GONE);
				}
			} else {
				for (int i = 0; i < heartCount; i++) {
					ImageView imageView = (ImageView) convertView
							.findViewById(heartImg[i]);
					imageView.setVisibility(View.VISIBLE);
					imageView.setImageResource(R.drawable.icon_heart);
					j = i;
				}
				if (heartHeart == 5) {
					ImageView imageView = (ImageView) convertView
							.findViewById(heartImg[j + 1]);
					imageView.setImageResource(R.drawable.icon_heart_half);
					imageView.setVisibility(View.VISIBLE);
				}
				// 用于刷新UI
				if (heartCount < 9) {
					for (int ii = j + 2; ii < 10; ii++) {
						ImageView imageView = (ImageView) convertView
								.findViewById(heartImg[ii]);
						imageView.setVisibility(View.GONE);
					}
				}
			}
		} else {
			for (int a = 0; a < 10; a++) {
				ImageView imageView = (ImageView) convertView
						.findViewById(heartImg[a]);
				imageView.setVisibility(View.GONE);
			}
		}
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
							if (imageView.getTag() != null
									&& imageView.getTag().equals(picName)) {
								imageView.setImageBitmap(arg0);
							} else {
								imageView
										.setImageResource(R.drawable.pic_default_user);
							}
							OutputStream output = null;
							try {
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
										+ "jzdr/" + "image/" + picName);
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

	class ViewHolder {
		TextView name, age;
		CircularImage cover_user_photo;
		ImageView renzhengImv, cyjImv, sexImv;

	}

}
