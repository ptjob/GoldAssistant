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
import com.quark.adapter.FunsAdapter.ViewHolder;
import com.quark.common.Url;
import com.quark.http.image.CircularImage;
import com.quark.http.image.LoadImage;
import com.quark.model.BrokerBean;
import com.quark.model.FunsBean;
import com.quark.volley.VolleySington;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.os.Environment;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BrokerAdapter extends BaseAdapter {

	private ArrayList<BrokerBean> list;
	private Context context;
	protected RequestQueue queue;
	int[] heartImg = { R.id.xinyi_bt1, R.id.xinyi_bt2, R.id.xinyi_bt3,
			R.id.xinyi_bt4, R.id.xinyi_bt5, R.id.xinyi_bt6, R.id.xinyi_bt7,
			R.id.xinyi_bt8, R.id.xinyi_bt9, R.id.xinyi_bt10 };
	ViewHolder holder;

	public BrokerAdapter(Context context, ArrayList<BrokerBean> list) {
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
					R.layout.item_broker_listview, null);
			holder.item_broker_beiyong_tv = (TextView) convertView
					.findViewById(R.id.item_broker_beiyong_tv);
			holder.kingImv = (ImageView) convertView
					.findViewById(R.id.item_broker_king_imv);
			holder.cover_user_photo = (CircularImage) convertView
					.findViewById(R.id.cover_user_photo);
			holder.indexTv = (TextView) convertView
					.findViewById(R.id.item_broker_mingci_tv);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.fans = (TextView) convertView
					.findViewById(R.id.item_broker_funs_num_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		TextPaint tp = holder.indexTv.getPaint();
		tp.setFakeBoldText(true);
		TextPaint tp2 = holder.name.getPaint();
		tp2.setFakeBoldText(true);
		if (i < 9) {
			holder.indexTv.setText("0" + String.valueOf(i + 1));
		} else {
			holder.indexTv.setText(String.valueOf(i + 1));
		}
		holder.name.setText(list.get(i).getCompany_name());
		holder.fans.setText(list.get(i).getFans() + " 粉丝");
		if (i == 0) {
			holder.item_broker_beiyong_tv.setVisibility(View.GONE);
			holder.kingImv.setVisibility(View.VISIBLE);
			holder.kingImv.setImageResource(R.drawable.king_first);
			holder.indexTv.setTextColor(context.getResources().getColor(
					R.color.king_first));
		} else if (i == 1) {
			holder.item_broker_beiyong_tv.setVisibility(View.GONE);

			holder.indexTv.setTextColor(context.getResources().getColor(
					R.color.king_second));
			holder.kingImv.setVisibility(View.VISIBLE);
			holder.kingImv.setImageResource(R.drawable.king_second);

		} else if (i == 2) {
			holder.item_broker_beiyong_tv.setVisibility(View.GONE);
			holder.indexTv.setTextColor(context.getResources().getColor(
					R.color.king_third));
			holder.kingImv.setVisibility(View.VISIBLE);
			holder.kingImv.setImageResource(R.drawable.king_thrid);

		} else {
			holder.indexTv.setTextColor(context.getResources().getColor(
					R.color.ziti_huise));
			holder.item_broker_beiyong_tv.setVisibility(View.VISIBLE);
			holder.kingImv.setVisibility(View.GONE);

		}

		if (list.get(i).getAvatar() == null
				|| list.get(i).getAvatar().equals("")) {
			holder.cover_user_photo
					.setImageResource(R.drawable.default_avatar_business);
		} else {
			checkPhotoExits(list.get(i).getAvatar(), holder.cover_user_photo);
		}
		return convertView;
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

	class ViewHolder {
		TextView indexTv, name, fans, item_broker_beiyong_tv;// 序号、名字、粉丝、填充无皇冠的空间
		CircularImage cover_user_photo;
		ImageView kingImv;// 皇冠

	}

}
