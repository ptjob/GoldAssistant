package com.quark.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.carson.constant.ConstantForSaveList;
import com.easemob.chat.EMContactManager;
import com.easemob.chatuidemo.activity.AlertDialog;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.http.image.CircularImage;
import com.quark.http.image.LoadImage;
import com.quark.jianzhidaren.ApplicationControl;
import com.quark.model.HuanxinUser;
import com.quark.ui.widget.EditDialog;
import com.quark.volley.VolleySington;

/**
 * 
 * @ClassName: HuanxingSearchUserAdapter
 * @Description: 环信查找人员列表
 * @author howe
 * @date 2015-2-5 上午11:16:35
 * 
 */
public class HuanxingSearchUserAdapter extends BaseAdapter {

	private ViewHolder holder;
	private List<HuanxinUser> list;
	private Context context;
	Activity activity;
	private ProgressDialog progressDialog;
	protected RequestQueue queue = VolleySington.getInstance()
			.getRequestQueue();
	private SharedPreferences sp;

	public HuanxingSearchUserAdapter(Context context, List<HuanxinUser> list,
			Activity activity) {
		this.list = list;
		this.context = context;
		this.activity = activity;
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
	public View getView(final int i, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_huanxinsearchuser, null);
			holder.avatar = (CircularImage) convertView
					.findViewById(R.id.avatar);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.indicator = (Button) convertView
					.findViewById(R.id.indicator);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if ((list.get(i).getAvatar() != null)
				&& (!list.get(i).getAvatar().equals(""))) {
			// 现获取本地头像
			loadNativePhoto(list.get(i).getUid(), list.get(i).getAvatar(),
					holder.avatar);

		} else {
			if (list.get(i).getUid().contains("u")) {
				holder.avatar.setImageResource(R.drawable.default_avatar);
			} else if (list.get(i).getUid().contains("c")) {
				holder.avatar
						.setImageResource(R.drawable.default_avatar_business);
			}
		}

		holder.name.setText(list.get(i).getName());

		holder.indicator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// carson 添加好友是点击添加判断是否已添加或者是否是自己？
				// ApplicationControl.getInstance().getUserName().equals(list.get(i).getName())
				// carson 更改判定条件是uid
				// ApplicationControl.getInstance().getUserName()得到的是uid，应该跟list.getUid对比
				if (ApplicationControl.getInstance().getUserName()
                        .equals(list.get(i).getUid())) {
                    context.startActivity(new Intent(context, AlertDialog.class)
                            .putExtra("msg", "不能添加自己"));
                    return;
                }

                if (ApplicationControl.getInstance().getContactList()
                        .containsKey(list.get(i).getUid())) {
                    context.startActivity(new Intent(context, AlertDialog.class)
                            .putExtra("msg", "此用户已是你的好友"));
                    return;
                }
                showAlertDialog("留言", "留言", i);

			}
		});

		return convertView;
	}

	private static class ViewHolder {
		CircularImage avatar;
		TextView name;
		Button indicator;
	}

	// /*************************carson add on
	// 4-9-19:47****************************************
	/**
	 * 加载本地头像
	 */
	private void loadNativePhoto(final String id, final String avatarPic,
			final ImageView avatar) {

		File mePhotoFold = new File(Environment.getExternalStorageDirectory()
				+ "/" + "jzdr/" + "image");
		if (!mePhotoFold.exists()) {
			mePhotoFold.mkdirs();
		}
		// 当前聊天对象的头像更改,要先联网验证头像路径是否更改
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
				// 更新本地数据
				if (avatarPic != null && !"".equals(avatarPic)) {
					Editor edt = sp.edit();
					edt.putString(id + "_photo", avatarPic);
					edt.commit();
				}
			} else {
				loadpersonPic(avatarPic, avatar, 1);
				if (avatarPic != null && !"".equals(avatarPic)) {
					Editor edt = sp.edit();
					edt.putString(id + "_photo", avatarPic);
					edt.commit();
				}
			}
		} else {
			loadpersonPic(avatarPic, avatar, 1);
			if (avatarPic != null && !"".equals(avatarPic)) {
				Editor edt = sp.edit();
				edt.putString(id + "_photo", avatarPic);
				edt.commit();
			}
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
		imgRequest.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	public void showAlertDialog(String str, final String str2, final int i) {

		final EditDialog.Builder builder = new EditDialog.Builder(context);
		builder.setMessage(str);
		builder.setTitle(str2);

		builder.setPositiveButton("确 定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				final String content = builder.getContent();

				dialog.dismiss();
				progressDialog = new ProgressDialog(activity);
				progressDialog.setMessage("正在发送请求...");
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.show();

				new Thread(new Runnable() {
					public void run() {
						try {
							String sendstr = "";
							// demo写死了个reason，实际应该让用户手动填入
							if (content.isEmpty()) {
								sendstr = "邀请你为好友";
							} else {
								sendstr = content;
							}
							EMContactManager.getInstance().addContact(
									list.get(i).getUid(), sendstr);
							activity.runOnUiThread(new Runnable() {
								public void run() {
									progressDialog.dismiss();
									Toast.makeText(context, "发送请求成功,等待对方验证", 1)
											.show();
								}
							});
						} catch (final Exception e) {
							activity.runOnUiThread(new Runnable() {
								public void run() {
									progressDialog.dismiss();
									Toast.makeText(context,
											"请求添加好友失败:" + e.getMessage(), 1)
											.show();
								}
							});
						}
					}
				}).start();

				// }else{
				// Toast.makeText(context, "请输入内容", 1).show();
				// }
			}
		});
		builder.create().show();
	}
}
