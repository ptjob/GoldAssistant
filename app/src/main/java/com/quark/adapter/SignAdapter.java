package com.quark.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.quark.model.RosterModel;
import com.quark.volley.VolleySington;

/**
 * 
 * @ClassName: SignAdapter
 * @Description: 签到
 * @author howe
 * @date 2015-2-7 上午9:47:18
 * 
 */
public class SignAdapter extends BaseAdapter {

	private ViewHolder holder;
	private List<RosterModel> list;
	private Context context;
	protected RequestQueue queue;

	public SignAdapter(Context context, List<RosterModel> list) {
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
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int i, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.company_person_list, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.cover_user_photo = (CircularImage) convertView
					.findViewById(R.id.cover_user_photo);
			holder.telephone = (TextView) convertView
					.findViewById(R.id.telephone);
			holder.sex = (TextView) convertView.findViewById(R.id.sex);
			holder.age = (TextView) convertView.findViewById(R.id.age);
			holder.phone = (ImageView) convertView.findViewById(R.id.phone);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(list.get(i).getName());
		holder.telephone.setText(list.get(i).getTelephone());
		if (list.get(i).getSex() == -1) {
			holder.sex.setText("未知");
		} else if (list.get(i).getSex() == 0) {
			holder.sex.setText("女");
		} else {
			holder.sex.setText("男");
		}

		holder.age.setText(list.get(i).getAge() + "");

		if (list.get(i).getPicture_1() == null
				|| list.get(i).getPicture_1().equals("")) {
			holder.cover_user_photo
					.setImageResource(R.drawable.pic_default_user);

		} else {
			checkPhotoExits(list.get(i).getPicture_1(), holder.cover_user_photo);
		}

		holder.phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String number = list.get(i).getTelephone().toString();
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ number));
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	public static class ViewHolder {
		TextView name;
		public CircularImage cover_user_photo;
		TextView telephone;
		TextView sex;
		TextView age;
		ImageView phone;
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
