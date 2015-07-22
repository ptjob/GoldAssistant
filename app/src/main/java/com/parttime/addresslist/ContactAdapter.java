/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.parttime.addresslist;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.carson.constant.ConstantForSaveList;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.widget.Sidebar;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.http.image.CircularImage;
import com.quark.http.image.LoadImage;
import com.quark.model.HuanxinUser;
import com.quark.volley.VolleySington;

/**
 * 简单的好友Adapter实现
 *
 */
/**
 * 
 * @ClassName: ContactAdapter
 * @Description: 显示头像和昵称
 * @author howe
 * @date 2015-2-5 下午8:17:49
 * 
 */
public class ContactAdapter extends ArrayAdapter<User> implements
		SectionIndexer {

	private LayoutInflater layoutInflater;
	private EditText query;
	private ImageButton clearSearch;
	private SparseIntArray positionOfSection;
	private SparseIntArray sectionOfPosition;
	private Sidebar sidebar;
	private int res;
	ArrayList<HuanxinUser> usersNick;
	RequestQueue queue = VolleySington.getInstance().getRequestQueue();
	List<User> temp;
	private SharedPreferences sp;

	public ContactAdapter(Context context, int resource, List<User> objects,
			Sidebar sidebar, ArrayList<HuanxinUser> usersNick) {
		super(context, resource, objects);
		this.res = resource;
		this.sidebar = sidebar;
		temp = objects;
		this.usersNick = (ArrayList<HuanxinUser>) usersNick.clone();
		layoutInflater = LayoutInflater.from(context);
		sp = context.getSharedPreferences("jrdr.setting", Context.MODE_PRIVATE);
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return position == 0 ? 0 : 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(res, null);
		}

		CircularImage avatar = (CircularImage) convertView
				.findViewById(R.id.avatar);
		TextView unreadMsgView = (TextView) convertView
				.findViewById(R.id.unread_msg_number);
		TextView nameTextview = (TextView) convertView.findViewById(R.id.name);
		TextView tvHeader = (TextView) convertView.findViewById(R.id.header);
		User user = getItem(position);
		if (user == null) {
			Log.d("ContactAdapter", position + "");
		} else {
			// 设置nick，demo里不涉及到完整user，用username代替nick显示
			String username = user.getUsername();
			String header = user.getHeader();
			if (position == 0 || header != null
					&& !header.equals(getItem(position - 1).getHeader())) {
				if ("".equals(header)) {
					tvHeader.setVisibility(View.GONE);
				} else {
					tvHeader.setVisibility(View.VISIBLE);
					tvHeader.setText(header);
				}
			} else {
				tvHeader.setVisibility(View.GONE);
			}

			// 显示申请与通知item
			if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
				nameTextview.setText(user.getNick());
				avatar.setImageResource(R.drawable.new_friends_icon);
				if (user.getUnreadMsgCount() > 0) {
					unreadMsgView.setVisibility(View.VISIBLE);
					unreadMsgView.setText(user.getUnreadMsgCount() + "");
				} else {
					unreadMsgView.setVisibility(View.INVISIBLE);
				}
			} else if (username.equals(Constant.PUBLIC_COUNT)) {
				// 官方账号
				nameTextview.setText(user.getNick());
				avatar.setImageResource(R.drawable.peoplephoto);
            } else if (username.equals(Constant.GROUP_USERNAME)) {
				// 群聊item
				nameTextview.setText(user.getNick());
				avatar.setImageResource(R.drawable.peoplephoto);
			} else {
				// 设置昵称 头像 头两条是群和通知 所以从联系人进来的 usersNick真实数据应该从第三条开始（开始两条为空）
				// 2.从群设置进来 无通知和群聊，真实数据从第二个开始（第一个未空值）
				if (usersNick.size() > position) {
					nameTextview.setText(usersNick.get(position).getName());
					if ((usersNick.get(position).getAvatar() != null)
							&& (!usersNick.get(position).getAvatar().equals(""))) {
						// 默认加载本地图片
						loadNativePhoto(usersNick.get(position).getUid(),
								usersNick.get(position).getAvatar(), avatar,
								nameTextview);
						Editor edt = sp.edit();
						edt.putString(usersNick.get(position).getUid()
								+ "realname", usersNick.get(position).getName());
						edt.commit();
					} else {
						avatar.setImageResource(R.drawable.default_avatar);
					}
				}
				if (unreadMsgView != null)
					unreadMsgView.setVisibility(View.INVISIBLE);
			}
			// }
		}
		return convertView;
	}

	@Override
	public User getItem(int position) {
		User user = new User();
		user.setHeader(getContext().getString(R.string.search_header));
		// return position == 0 ? user : super.getItem(position - 1);
		return super.getItem(position);
	}

	public HuanxinUser getItemNick(int position) {
		HuanxinUser hxus = new HuanxinUser();
		hxus = usersNick.get(position);
		return hxus;
	}

	@Override
	public int getCount() {
		// 有搜索框，count+1
		// return super.getCount() + 1;
		return super.getCount();
	}

	public int getPositionForSection(int section) {
		return positionOfSection.get(section);
	}

	public int getSectionForPosition(int position) {
		return sectionOfPosition.get(position);
	}

	@Override
	public Object[] getSections() {
		positionOfSection = new SparseIntArray();
		sectionOfPosition = new SparseIntArray();
		int count = getCount();
		List<String> list = new ArrayList<String>();
		list.add(getContext().getString(R.string.search_header));
		positionOfSection.put(0, 0);
		sectionOfPosition.put(0, 0);
		for (int i = 1; i < count; i++) {
			String letter = getItem(i).getHeader();
			System.err.println("contactadapter getsection getHeader:" + letter
					+ " name:" + getItem(i).getUsername());
			int section = list.size() - 1;
			if (list.get(section) != null && !list.get(section).equals(letter)) {
				list.add(letter);
				section++;
				positionOfSection.put(section, i);
			}
			sectionOfPosition.put(i, section);
		}
		return list.toArray(new String[list.size()]);
	}

	// /*************************carson add on
	// 4-9-19:47****************************************
	/**
	 * 加载本地头像和名字
	 */
	private void loadNativePhoto(final String id, final String avatarUrl,
			final ImageView avatar, final TextView name) {
		// 先获取本地名字和头像
		String nativeName = sp.getString(id + "realname", "");
		if (name == null) {

		} else {
			if (!"".equals(nativeName)) {
				name.setText(nativeName);
			}
		}
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
				avatar.setImageResource(R.drawable.default_avatar);
				loadpersonPic(id, avatarUrl, avatar, 1);
			}
		} else {
			avatar.setImageResource(R.drawable.default_avatar);
			loadpersonPic(id, avatarUrl, avatar, 1);
		}

	}

	/**
	 * @Description: 加载图片
	 * @author howe
	 * @date 2014-7-30 下午5:57:52
	 * 
	 */
	private void loadpersonPic(final String id, final String url,
			final ImageView imageView, final int isRound) {
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
							Editor edt = sp.edit();
							edt.putString(id + "_photo", url);
							edt.commit();
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

}
