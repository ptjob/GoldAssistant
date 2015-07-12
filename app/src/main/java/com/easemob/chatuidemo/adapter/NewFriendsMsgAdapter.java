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
package com.easemob.chatuidemo.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.domain.InviteMessage;
import com.easemob.chatuidemo.domain.InviteMessage.InviteMesageStatus;
import com.qingmu.jianzhidaren.R;
import com.quark.common.JsonUtil;
import com.quark.common.Url;
import com.quark.http.image.CircularImage;
import com.quark.model.HuanxinUser;
import com.quark.volley.VolleySington;

/**
 * 
 * @ClassName: NewFriendsMsgAdapter
 * @Description: 申请与通知
 * @author
 * @date 2015-2-10 下午6:38:48
 * 
 */
public class NewFriendsMsgAdapter extends ArrayAdapter<InviteMessage> {

	private Context context;
	private InviteMessgeDao messgeDao;
	RequestQueue queue = VolleySington.getInstance().getRequestQueue();

	public NewFriendsMsgAdapter(Context context, int textViewResourceId,
			List<InviteMessage> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		messgeDao = new InviteMessgeDao(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.row_invite_msg, null);
			holder.avator = (CircularImage) convertView
					.findViewById(R.id.avatar);
			holder.reason = (TextView) convertView.findViewById(R.id.message);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.status = (Button) convertView.findViewById(R.id.user_state);
			holder.refuse = (Button) convertView.findViewById(R.id.user_refuse);
			holder.groupContainer = (LinearLayout) convertView
					.findViewById(R.id.ll_group);
			holder.groupname = (TextView) convertView
					.findViewById(R.id.tv_groupName);
			// holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final InviteMessage msg = getItem(position);
		if (msg != null) {
			if (msg.getGroupId() != null) { // 显示群聊提示
				holder.groupContainer.setVisibility(View.VISIBLE);
				holder.groupname.setText(msg.getGroupName());
			} else {
				holder.groupContainer.setVisibility(View.GONE);
			}

			holder.reason.setText(msg.getReason());
			// holder.name.setText(msg.getFrom());
			getNick(msg.getFrom(), holder.avator, holder.name);
			// holder.time.setText(DateUtils.getTimestampString(new
			// Date(msg.getTime())));
			if (msg.getStatus() == InviteMesageStatus.BEAGREED) {
				holder.status.setVisibility(View.INVISIBLE);
				holder.reason.setText("已同意你的好友请求");
				holder.refuse.setVisibility(View.GONE);
			} else if (msg.getStatus() == InviteMesageStatus.BEINVITEED
					|| msg.getStatus() == InviteMesageStatus.BEAPPLYED) {
				holder.status.setVisibility(View.VISIBLE);
				holder.status.setEnabled(true);
				holder.status.setBackgroundResource(R.drawable.button_bg);
				holder.status.setText("同意");
				// 群申请拒绝
				holder.refuse.setVisibility(View.VISIBLE);
				holder.refuse.setBackgroundResource(R.drawable.button_bg);
				holder.refuse.setEnabled(true);
				holder.refuse.setText("拒绝");
				if (msg.getStatus() == InviteMesageStatus.BEINVITEED) {
					if (msg.getReason() == null) {
						// 如果没写理由
						holder.reason.setText("请求加你为好友");
					}
				} else { // 入群申请
					if (TextUtils.isEmpty(msg.getReason())) {
						holder.reason.setText("申请加入群：" + msg.getGroupName());
					}
				}
				// 设置点击事件 同意
				holder.status.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 同意别人发的好友请求
						acceptInvitation(holder.status, msg);
						holder.status.setText("已同意");
						holder.status.setBackgroundDrawable(null);
						holder.status.setVisibility(View.VISIBLE);
						holder.status.setEnabled(false);
						holder.refuse.setVisibility(View.GONE);
					}
				});
				// 设置点击事件 拒绝
				holder.refuse.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						refuseInvitation(holder.refuse, msg);
						holder.refuse.setText("已拒绝");
						holder.refuse.setBackgroundDrawable(null);
						holder.refuse.setVisibility(View.VISIBLE);
						holder.refuse.setEnabled(false);
						holder.status.setVisibility(View.GONE);

					}
				});
			} else if (msg.getStatus() == InviteMesageStatus.AGREED) {
				holder.status.setText("已同意");
				holder.status.setBackgroundDrawable(null);
				holder.status.setEnabled(false);
				holder.status.setVisibility(View.VISIBLE);
				holder.refuse.setVisibility(View.GONE);
			} else if (msg.getStatus() == InviteMesageStatus.REFUSED) {
				holder.refuse.setText("已拒绝");
				holder.refuse.setBackgroundDrawable(null);
				holder.refuse.setVisibility(View.VISIBLE);
				holder.refuse.setEnabled(false);
				holder.status.setVisibility(View.GONE);
			}

			// 设置用户头像
		}

		return convertView;
	}

	/**
	 * 同意好友请求或者群申请
	 * 
	 * @param button
	 * @param username
	 */
	private void acceptInvitation(final Button button, final InviteMessage msg) {
		final ProgressDialog pd = new ProgressDialog(context);
		pd.setMessage("正在同意...");
		pd.setCanceledOnTouchOutside(false);
		pd.show();

		new Thread(new Runnable() {
			public void run() {
				// 调用sdk的同意方法
				try {
					if (msg.getGroupId() == null) { // 同意好友请求
						EMChatManager.getInstance().acceptInvitation(
								msg.getFrom());
					} else {
						// 同意加群申请
						EMGroupManager.getInstance().acceptApplication(
								msg.getFrom(), msg.getGroupId());
					}
					((Activity) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							pd.dismiss();
							button.setText("已同意");
							msg.setStatus(InviteMesageStatus.AGREED);
							// 更新db
							ContentValues values = new ContentValues();
							values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg
									.getStatus().ordinal());
							messgeDao.updateMessage(msg.getId(), values);
							button.setBackgroundDrawable(null);
							button.setEnabled(false);
						}
					});
				} catch (final Exception e) {
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							Toast.makeText(context, "同意失败: " + e.getMessage(),
									1).show();
						}
					});

				}
			}
		}).start();
	}

	/**
	 * 拒绝好友请求或者群申请
	 * 
	 * @param button
	 * @param username
	 */
	private void refuseInvitation(final Button button, final InviteMessage msg) {
		final ProgressDialog pd = new ProgressDialog(context);
		pd.setMessage("正在拒绝...");
		pd.setCanceledOnTouchOutside(false);
		pd.show();

		new Thread(new Runnable() {
			public void run() {
				// 调用sdk的同意方法
				try {
					if (msg.getGroupId() == null) {// 拒绝好友请求
						EMChatManager.getInstance().refuseInvitation(
								msg.getFrom());
					} else {
						// 拒绝加群申请
						EMGroupManager.getInstance().declineApplication(
								msg.getFrom(), msg.getGroupId(), "已拒绝");
					}
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							button.setText("已拒绝");
							msg.setStatus(InviteMesageStatus.REFUSED);
							// 更新db
							ContentValues values = new ContentValues();
							values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg
									.getStatus().ordinal());
							messgeDao.updateMessage(msg.getId(), values);
							button.setBackgroundDrawable(null);
							button.setEnabled(false);
						}
					});
				} catch (final Exception e) {
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							Toast.makeText(context, "拒绝失败: " + e.getMessage(),
									1).show();
						}
					});

				}
			}
		}).start();
	}

	private static class ViewHolder {
		CircularImage avator;
		TextView name;
		TextView reason;
		Button status;// 同意
		Button refuse;// 拒绝
		LinearLayout groupContainer;
		TextView groupname;
		// TextView time;
	}

	// ====================================howe=========================
	public void getNick(final String id, final CircularImage avatar,
			final TextView name) {
		StringRequest request = new StringRequest(Request.Method.POST,
				Url.HUANXIN_avatars_pic, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject js = new JSONObject(response);
							JSONArray jss = js.getJSONArray("avatars");
							HuanxinUser us = (HuanxinUser) JsonUtil.jsonToBean(
									jss.getJSONObject(0), HuanxinUser.class);
							name.setText(us.getName());
							Log.e("erros", "返bean=" + us.toString());
							if ((us.getAvatar() != null)
									&& (!us.getAvatar().equals(""))) {
								loadpersonPic(Url.GETPIC + us.getAvatar(),
										avatar, 1);
							} else {
								avatar.setImageResource(R.drawable.default_avatar);
							}

						} catch (JSONException e) {
							e.printStackTrace();
							System.out
									.println("==================reg json 异常===========");
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						Toast.makeText(context, "你的网络不够给力，获取数据失败！", 0).show();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_ids", "{" + id + "}");

				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(ConstantForSaveList.DEFAULTRETRYTIME*1000, 1, 1.0f));

	}

	/**
	 * @Description: 加载图片
	 * @author howe
	 * @date 2014-7-30 下午5:57:52
	 * 
	 */
	public void loadpersonPic(String url, final ImageView imageView,
			final int isRound) {
		ImageRequest imgRequest = new ImageRequest(url,
				new Response.Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap arg0) {
						if (isRound == 1) {
							// Bitmap bit = UploadImg.toRoundCorner(arg0, 2);
							imageView.setImageBitmap(arg0);
						} else {

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
	// ================howe end==================
}
