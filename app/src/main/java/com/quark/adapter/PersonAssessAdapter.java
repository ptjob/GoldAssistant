package com.quark.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.carson.constant.ConstantForSaveList;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.http.image.CircularImage;
import com.quark.http.image.LoadImage;
import com.quark.model.RosterUser;
import com.quark.volley.VolleySington;

/**
 * 
 * @ClassName: PersonAssessAdapter
 * @Description: TODO
 * @author howe
 * @date 2015-3-4 上午9:53:55
 * 
 */
public class PersonAssessAdapter extends BaseAdapter {

	private ViewHolder holder;
	private List<RosterUser> list;
	private Context context;
	int[] heartImg = { R.id.xinyi_bt1, R.id.xinyi_bt2, R.id.xinyi_bt3,
			R.id.xinyi_bt4, R.id.xinyi_bt5, R.id.xinyi_bt6, R.id.xinyi_bt7,
			R.id.xinyi_bt8, R.id.xinyi_bt9, R.id.xinyi_bt10 };
	protected RequestQueue queue;

	public PersonAssessAdapter(Context context, List<RosterUser> list) {
		this.list = list;
		this.context = context;
		queue = VolleySington.getInstance().getRequestQueue();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int i, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.company_person_evaluate, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.quxiaojianzhi_tv = (TextView) convertView
					.findViewById(R.id.quxiao_jianzhi_tv);
			holder.cover_user_photo = (CircularImage) convertView
					.findViewById(R.id.cover_user_photo);
			holder.sex = (TextView) convertView.findViewById(R.id.sex);
			holder.age = (TextView) convertView.findViewById(R.id.age);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.back_layout = (LinearLayout) convertView
					.findViewById(R.id.company_person_back_layout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(list.get(i).getName());
		if (list.get(i).getSex() == -1) {
			holder.sex.setText("未知");
		} else if (list.get(i).getSex() == 0) {
			holder.sex.setText("女");
		} else {
			holder.sex.setText("男");
		}

		addXinToView(list.get(i).getCreditworthiness(), convertView);// 信誉值:步长为5,半个心
		holder.age.setText(list.get(i).getAge() + "");
		if (list.get(i).getPicture_1() == null
				|| list.get(i).getPicture_1().equals("")) {
			holder.cover_user_photo
					.setImageResource(R.drawable.pic_default_user);
		} else {
			// PersonAcImageLoader.loadImage(Url.GETPIC
			// + list.get(i).getPicture_1(), this, holder);
			checkPhotoExits(list.get(i).getPicture_1(), holder.cover_user_photo);
		}
		// 是否是取消兼职用户(0取消1正常2放飞机)
		if (list.get(i).getStatus() == 0) {
			holder.back_layout.setBackgroundColor(context.getResources()
					.getColor(R.color.quxiao_jianzhi_back_color));
			holder.quxiaojianzhi_tv.setVisibility(View.VISIBLE);
		} else if (list.get(i).getStatus() == 1) {
			holder.back_layout.setBackgroundColor(context.getResources()
					.getColor(R.color.body_color));
			holder.quxiaojianzhi_tv.setVisibility(View.GONE);
		} else if (list.get(i).getStatus() == 2) {
			holder.back_layout.setBackgroundColor(context.getResources()
					.getColor(R.color.body_color));
			holder.quxiaojianzhi_tv.setVisibility(View.GONE);
		} else {
			holder.back_layout.setBackgroundColor(context.getResources()
					.getColor(R.color.body_color));
			holder.quxiaojianzhi_tv.setVisibility(View.GONE);
		}

		// 是否已评论
		if (list.get(i).getIs_commented() == 0) {
			holder.image.setVisibility(View.VISIBLE);
			holder.text.setVisibility(View.GONE);
		} else {
			holder.image.setVisibility(View.GONE);
			holder.text.setVisibility(View.VISIBLE);
			holder.text.setText(list.get(i).getComment());
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

	public static class ViewHolder {
		LinearLayout back_layout;
		TextView name, quxiaojianzhi_tv;// 取消兼职红色字体提示
		public CircularImage cover_user_photo;
		TextView sex;
		TextView age;
		ImageView image;
		TextView text;
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
		imgRequest.setRetryPolicy(new DefaultRetryPolicy(ConstantForSaveList.DEFAULTRETRYTIME*1000, 1, 1.0f));

	}

}
