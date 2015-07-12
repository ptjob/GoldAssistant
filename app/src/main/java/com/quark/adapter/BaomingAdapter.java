package com.quark.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.quark.model.BaomingList;
import com.quark.volley.VolleySington;

/**
 * 
 * @ClassName: BaomingAdapter
 * @Description: 管理 详细 报名人员列表
 * @author howe
 * @date 2015-1-29 上午10:23:23
 * 
 */
public class BaomingAdapter extends BaseAdapter {

	private ViewHolder holder;
	private List<BaomingList> list;
	private Context context;
	private SharedPreferences sp;
	int[] heartImg = { R.id.xinyi_bt1, R.id.xinyi_bt2, R.id.xinyi_bt3,
			R.id.xinyi_bt4, R.id.xinyi_bt5, R.id.xinyi_bt6, R.id.xinyi_bt7,
			R.id.xinyi_bt8, R.id.xinyi_bt9, R.id.xinyi_bt10 };
	protected RequestQueue queue = VolleySington.getInstance()
			.getRequestQueue();

	public BaomingAdapter(Context context, List<BaomingList> list) {
		this.list = list;
		this.context = context;
		sp = context.getSharedPreferences("jrdr.setting", context.MODE_PRIVATE);
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
					R.layout.item_baoming, null);
			holder.bm_headpic = (CircularImage) convertView
					.findViewById(R.id.bm_headpic);
			holder.bm_title = (TextView) convertView
					.findViewById(R.id.bm_title);
			holder.sex_imv = (ImageView) convertView
					.findViewById(R.id.item_sex_imv);
			holder.bm_yan = (ImageView) convertView.findViewById(R.id.bm_yan);
			holder.bm_cyj = (ImageView) convertView.findViewById(R.id.bm_cyj);
			holder.bm_age = (TextView) convertView.findViewById(R.id.bm_age);
			holder.bm_note = (TextView) convertView.findViewById(R.id.bm_note);
			holder.bm_status = (ImageView) convertView
					.findViewById(R.id.bm_status);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String sstr;
		if (list.get(i).getPicture_1() != null
				&& !list.get(i).getPicture_1().equals("")) {
			// function1 开始是设置应用内缓存,持续时间设定为100秒,超过100秒重新加载
			// BaomingImageLoader.loadImage(Url.GETPIC
			// + list.get(i).getPicture_1(), this, holder);
			// function2 下载图片到本地,之后从本地读取
			loadNativePhoto("u" + list.get(i).getUser_id(), list.get(i)
					.getPicture_1(), holder.bm_headpic);
			Editor edt = sp.edit();
			edt.putString("u" + list.get(i).getUser_id() + "_photo", list
					.get(i).getPicture_1());
			edt.commit();

			// loadpersonPic(Url.GETPIC + list.get(i).getPicture_1(),
			// holder.bm_headpic, 1);
		} else {
			if (list.get(i).getSex() == 1) {
				sstr = "男";
				holder.bm_headpic.setImageDrawable(context.getResources()
						.getDrawable(R.drawable.photo_male));
				holder.sex_imv.setImageDrawable(context.getResources()
						.getDrawable(R.drawable.my_men));
			} else {
				sstr = "女";
				holder.bm_headpic.setImageDrawable(context.getResources()
						.getDrawable(R.drawable.photo_female));
				holder.sex_imv.setImageDrawable(context.getResources()
						.getDrawable(R.drawable.my_women));
			}
		}

		holder.bm_title.setText(list.get(i).getName());
		if (list.get(i).getCertification() == 2) {
			holder.bm_yan.setImageDrawable(context.getResources().getDrawable(
					R.drawable.my_certified));
		} else {
			holder.bm_yan.setImageDrawable(context.getResources().getDrawable(
					R.drawable.my_unauthorized));
		}
		if (list.get(i).getEarnest_money() == 0) {
			holder.bm_cyj.setImageDrawable(context.getResources().getDrawable(
					R.drawable.my_ordinary));
		} else {
			holder.bm_cyj.setImageDrawable(context.getResources().getDrawable(
					R.drawable.my_margin));
		}

		if (list.get(i).getSex() == 1) {
			sstr = "男";
			holder.sex_imv.setImageDrawable(context.getResources()
					.getDrawable(R.drawable.my_men));

		} else {
			sstr = "女";
			holder.sex_imv.setImageDrawable(context.getResources()
					.getDrawable(R.drawable.my_women));
		}
		holder.bm_age.setText(list.get(i).getAge() + "岁");
		holder.bm_note.setText(list.get(i).getNote());
		if (list.get(i).getApply() == 1) {
			holder.bm_status.setImageDrawable(context.getResources()
					.getDrawable(R.drawable.myjob_icon_pass));
		} else if (list.get(i).getApply() == 2) {
			holder.bm_status.setImageDrawable(context.getResources()
					.getDrawable(R.drawable.myjob_icon_refuse));
		} else {
			holder.bm_status.setVisibility(View.INVISIBLE);
		}
		addXinToView(list.get(i).getCreditworthiness(), convertView);// 信誉值:步长为5,半个心
		return convertView;
	}

	public static class ViewHolder {

		public CircularImage bm_headpic;
		TextView bm_title;
		ImageView sex_imv;
		ImageView bm_yan;
		ImageView bm_cyj;
		TextView bm_age;
		TextView bm_note;
		ImageView bm_status;
	}

	// /*************************carson add on
	// 4-9-19:47****************************************
	/**
	 * 加载本地头像和名字
	 */
	private void loadNativePhoto(final String id, final String avatarUrl,
			final ImageView avatar) {
		// 先获取本地名字和头像
		File mePhotoFold = new File(Environment.getExternalStorageDirectory()
				+ "/" + "jzdr/" + "image");
		if (!mePhotoFold.exists()) {
			mePhotoFold.mkdirs();
		}
		File picture_1 = new File(Environment.getExternalStorageDirectory()
				+ "/" + "jzdr/" + "image/" + sp.getString(id + "_photo", "c"));

		if (picture_1.exists()) {
			// 加载本地图片
			// Bitmap bb_bmp = MyResumeActivity.zoomImg(picture_1, 300, 300);
			Bitmap bb_bmp = BitmapFactory.decodeFile(Environment
					.getExternalStorageDirectory()
					+ "/"
					+ "jzdr/"
					+ "image/"
					+ sp.getString(id + "_photo", "c"));
			if (bb_bmp != null) {
				avatar.setImageBitmap(LoadImage.toRoundBitmap(bb_bmp));
			} else {
				loadpersonPic(avatarUrl, avatar, 1);
			}
		} else {
			loadpersonPic(avatarUrl, avatar, 1);
		}

	}

	/**
	 * @Description: 加载图片
	 * @author howe
	 * @date 2014-7-30 下午5:57:52
	 * 
	 */
	public void loadpersonPic(final String url, final ImageView imageView,
			final int isRound) {
		ImageRequest imgRequest = new ImageRequest(Url.GETPIC + url,
				new Response.Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap arg0) {
						String picName = url;
						imageView.setImageBitmap(LoadImage.toRoundBitmap(arg0));
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
				}, 300, 200, Config.ARGB_8888, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {

					}
				});
		queue.add(imgRequest);
		imgRequest.setRetryPolicy(new DefaultRetryPolicy(ConstantForSaveList.DEFAULTRETRYTIME*1000, 1, 1.0f));

	}

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
}
