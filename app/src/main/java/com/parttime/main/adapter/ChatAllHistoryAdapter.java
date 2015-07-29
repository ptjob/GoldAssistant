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
package com.parttime.main.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

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
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.utils.SmileUtils;
import com.easemob.util.DateUtils;
import com.parttime.constants.ApplicationConstants;
import com.parttime.net.DefaultCallback;
import com.parttime.net.HuanXinRequest;
import com.parttime.pojo.MessageSet;
import com.qingmu.jianzhidaren.R;
import com.quark.common.JsonUtil;
import com.quark.common.Url;
import com.quark.http.image.CircularImage;
import com.quark.http.image.LoadImage;
import com.quark.image.UploadImg;
import com.quark.model.HuanxinUser;
import com.quark.volley.VolleySington;

/**
 * 显示所有聊天记录adpater
 * 
 */
public class ChatAllHistoryAdapter extends ArrayAdapter<EMConversation> {

	private LayoutInflater inflater;
	private List<EMConversation> conversationList = null;
	private List<EMConversation> copyConversationList;
	private List<String> realNames;
	private ConversationFilter conversationFilter;
	RequestQueue queue = VolleySington.getInstance().getRequestQueue();
	Context tcontext;
	SharedPreferences sp;
	//置顶设置，显示背景有用
    public Map<String, MessageSet> messageSetMap;

    public ChatAllHistoryAdapter(Context context, int textViewResourceId,
			List<EMConversation> objects) {
		super(context, textViewResourceId, objects);
		this.conversationList = objects;
		copyConversationList = new ArrayList<>();
		copyConversationList.addAll(objects);
		inflater = LayoutInflater.from(context);
		this.tcontext = context;
		sp = context.getSharedPreferences("jrdr.setting", context.MODE_PRIVATE);
		realNames = new ArrayList<>();// 真实名字
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_chat_history, parent,
					false);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (holder == null) {
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.unreadLabel = (TextView) convertView
					.findViewById(R.id.unread_msg_number);
			holder.message = (TextView) convertView.findViewById(R.id.message);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.avatar = (CircularImage) convertView
					.findViewById(R.id.avatar);
			holder.msgState = convertView.findViewById(R.id.msg_state);
            holder.quit = (ImageView)convertView.findViewById(R.id.quit);
			holder.list_item_layout = (RelativeLayout) convertView
					.findViewById(R.id.list_item_layout);
			convertView.setTag(holder);
		}
		/*if (position % 2 == 0) {
			holder.list_item_layout
					.setBackgroundResource(R.drawable.mm_listitem);
		} else {
			holder.list_item_layout
					.setBackgroundResource(R.drawable.mm_listitem_grey);
		}*/
        bindValue(position, holder);


        return convertView;
	}

    private void bindValue(int position, ViewHolder holder) {
        // 获取与此用户/群组的会话
        EMConversation conversation = getItem(position);
        if(conversation == null){
            return ;
        }
        // 获取用户username或者群组groupid
        String username = conversation.getUserName();
        List<EMGroup> groups = EMGroupManager.getInstance().getAllGroups();
        EMContact contact = null;
        boolean isGroup = false;
        boolean isMsgBlocked = false;
        boolean isMsgGag = false;
        for (EMGroup group : groups) {
            if (group.getGroupId().equals(username)) {
                isGroup = true;
                contact = group;
                isMsgBlocked = group.getMsgBlocked();
                break;
            }
        }
        List<String> pingbiListGroup = EMChatManager.getInstance()
                .getChatOptions().getReceiveNoNotifyGroup();
        if (pingbiListGroup != null) {
            if (pingbiListGroup.contains(username)) {
                isMsgGag = true;
            }
        }

        if(messageSetMap != null){
            MessageSet messageSet = messageSetMap.get(username);
            if(messageSet != null){
                holder.list_item_layout
                        .setBackgroundResource(R.drawable.mm_listitem_yellow);
            }
        }

        if (isGroup) {
            // 群聊消息，显示群聊头像
            holder.avatar.setImageResource(R.drawable.group_icon);
            holder.name.setText(contact.getNick() != null ? contact.getNick()
                    : username);
            if(isMsgBlocked){
                holder.quit.setVisibility(View.VISIBLE);
            }else{
                holder.quit.setVisibility(View.GONE);
            }
            if(isMsgGag){
                holder.quit.setVisibility(View.VISIBLE);
            }else{
                holder.quit.setVisibility(View.GONE);
            }
        } else {
            holder.quit.setVisibility(View.GONE);
            // 本地或者服务器获取用户详情，以用来显示头像和nick
            // 先加载本地头像和名字
            if (username.equals(ApplicationConstants.JZDR)) {
                holder.name.setText("兼职达人团队");
                Drawable draw1 = tcontext.getResources().getDrawable(
                        R.drawable.job_photo);
                BitmapDrawable bd = (BitmapDrawable) draw1;
                Bitmap bitmap = bd.getBitmap();
                Bitmap bit = UploadImg.toRoundCorner(bitmap, 2);
                holder.avatar.setImageBitmap(bit);
            } else if (username.equals(ApplicationConstants.CAIWU)) {
                holder.name.setText("财务小管家");
                Drawable draw1 = tcontext.getResources().getDrawable(
                        R.drawable.custom_caiwu);
                BitmapDrawable bd = (BitmapDrawable) draw1;
                Bitmap bitmap = bd.getBitmap();
                Bitmap bit = UploadImg.toRoundCorner(bitmap, 2);
                holder.avatar.setImageBitmap(bit);
            } else if (username.equals(ApplicationConstants.DINGYUE)) {
                holder.name.setText("订阅小助手");
                Drawable draw1 = tcontext.getResources().getDrawable(
                        R.drawable.custom_xiaozhushou);
                BitmapDrawable bd = (BitmapDrawable) draw1;
                Bitmap bitmap = bd.getBitmap();
                Bitmap bit = UploadImg.toRoundCorner(bitmap, 2);
                holder.avatar.setImageBitmap(bit);
            } else if (username.equals(ApplicationConstants.KEFU)) {
                holder.name.setText("兼职达人客服");
                Drawable draw1 = tcontext.getResources().getDrawable(
                        R.drawable.custom_kefu);
                BitmapDrawable bd = (BitmapDrawable) draw1;
                Bitmap bitmap = bd.getBitmap();
                Bitmap bit = UploadImg.toRoundCorner(bitmap, 2);
                holder.avatar.setImageBitmap(bit);
            } else if (username.equals(ApplicationConstants.TONGZHI)) {
                holder.name.setText("通知中心");
                Drawable draw1 = tcontext.getResources().getDrawable(
                        R.drawable.custom_tongzhi);
                BitmapDrawable bd = (BitmapDrawable) draw1;
                Bitmap bitmap = bd.getBitmap();
                Bitmap bit = UploadImg.toRoundCorner(bitmap, 2);
                holder.avatar.setImageBitmap(bit);
            } else {
                loadNativePhoto(username, holder.avatar, holder.name);
            }

            if (username.equals(Constant.GROUP_USERNAME)) {
                holder.name.setText(R.string.group_chat);

            } else if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
                holder.name.setText(R.string.apply_notify);
            }
            // holder.name.setText(username);
        }

        if (conversation.getUnreadMsgCount() > 0) {
            // 显示与此用户的消息未读数
            holder.unreadLabel.setText(String.valueOf(conversation
                    .getUnreadMsgCount()));
            holder.unreadLabel.setVisibility(View.VISIBLE);
        } else {
            holder.unreadLabel.setVisibility(View.INVISIBLE);
        }

        if (conversation.getMsgCount() != 0) {
            // 把最后一条消息的内容作为item的message内容
            EMMessage lastMessage = conversation.getLastMessage();
            holder.message
                    .setText(
                            SmileUtils.getSmiledText(
                                    getContext(),
                                    getMessageDigest(lastMessage,
                                            (this.getContext()))),
                            BufferType.SPANNABLE);

            holder.time.setText(DateUtils.getTimestampString(new Date(
                    lastMessage.getMsgTime())));
            if (lastMessage.direct == EMMessage.Direct.SEND
                    && lastMessage.status == EMMessage.Status.FAIL) {
                holder.msgState.setVisibility(View.VISIBLE);
            } else {
                holder.msgState.setVisibility(View.GONE);
            }
        }
    }

    /**
	 * 加载本地头像和名字
	 */
	private void loadNativePhoto(final String id, final CircularImage avatar,
			final TextView name) {
		// 先获取本地名字和头像
		String nativeName = sp.getString(id + "realname", "");
		if (!"".equals(nativeName)) {
			name.setText(nativeName);
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
				getNick(id, avatar, name);
			}
		} else {
			avatar.setImageResource(R.drawable.default_avatar);
			getNick(id, avatar, name);
		}

	}

	/**
	 * 根据消息内容和消息类型获取消息内容提示
	 * 
	 * @param message EMMessage
	 * @param context Context
	 * @return String
	 */
	private String getMessageDigest(EMMessage message, Context context) {
		String digest = "";
		switch (message.getType()) {
		case LOCATION: // 位置消息
			if (message.direct == EMMessage.Direct.RECEIVE) {
				// 从sdk中提到了ui中，使用更简单不犯错的获取string的方法
				// digest = EasyUtils.getAppResourceString(context,
				// "location_recv");
				digest = getStrng(context, R.string.location_recv);
				digest = String.format(digest, "发送了地理");

				return digest;
			} else {
				// digest = EasyUtils.getAppResourceString(context,
				// "location_prefix");
				digest = getStrng(context, R.string.location_prefix);
			}
			break;
		case IMAGE: // 图片消息
			ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
			// digest = getStrng(context, R.string.picture)
			// + imageBody.getFileName();
			digest = getStrng(context, R.string.picture);
			break;
		case VOICE:// 语音消息
			digest = getStrng(context, R.string.voice);
			break;
		case VIDEO: // 视频消息
			digest = getStrng(context, R.string.video);
			break;
		case TXT: // 文本消息
			if (!message.getBooleanAttribute(
					Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = txtBody.getMessage();
				if (digest.endsWith("邀请你加入了群聊")) {
					digest = "欢迎加入群聊";
				}
			} else {
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = getStrng(context, R.string.voice_call)
						+ txtBody.getMessage();
				if (digest.endsWith("邀请你加入了群聊")) {
					digest = "欢迎加入群聊";
				}
			}
			break;
		case FILE: // 普通文件消息
			digest = getStrng(context, R.string.file);
			break;
		default:
			System.err.println("error, unknow type");
			return "";
		}

		return digest;
	}

	private static class ViewHolder {
		/** 和谁的聊天记录 */
		TextView name;
		/** 消息未读数 */
		TextView unreadLabel;
		/** 最后一条消息的内容 */
		TextView message;
		/** 最后一条消息的时间 */
		TextView time;
		/** 用户头像 */
		CircularImage avatar;
		/** 最后一条消息的发送状态 */
		View msgState;
		/** 整个list中每一行总布局 */
		RelativeLayout list_item_layout;
        /** 静音设置 */
        ImageView quit;

	}

	String getStrng(Context context, int resId) {
		return context.getResources().getString(resId);
	}

	@Override
	public Filter getFilter() {
		if (conversationFilter == null) {
			conversationFilter = new ConversationFilter(conversationList);
		}
		return conversationFilter;
	}

	private class ConversationFilter extends Filter {
		List<EMConversation> mOriginalValues = null;

		public ConversationFilter(List<EMConversation> mList) {
			mOriginalValues = mList;
		}

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mOriginalValues == null) {
				mOriginalValues = new ArrayList<>();
			}
			if (prefix == null || prefix.length() == 0) {
				results.values = copyConversationList;
				results.count = copyConversationList.size();
			} else {
				String prefixString = prefix.toString();
				final int count = mOriginalValues.size();
				final ArrayList<EMConversation> newValues = new ArrayList<>();

				for (int i = 0; i < count; i++) {
					final EMConversation value = mOriginalValues.get(i);
					String username = value.getUserName();

					EMGroup group = EMGroupManager.getInstance().getGroup(
							username);
					if (group != null) {
						username = group.getGroupName();
					}
					String realName = realNames.get(i);

					// First match against the whole ,non-splitted value
					// if (username.startsWith(prefixString)) {
					if (realName.startsWith(prefixString)) {
						newValues.add(value);
					} else {
						final String[] words = realName.split(" ");
						final int wordCount = words.length;

						// Start at index 0, in case valueText starts with
						// space(s)
						for (int k = 0; k < wordCount; k++) {
							if (words[k].startsWith(prefixString)) {
								newValues.add(value);
								break;
							}
						}
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			conversationList.clear();
			conversationList.addAll((List<EMConversation>) results.values);
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}

	// ====================================howe=========================
	public void getNick(final String id, final CircularImage avatar,
			final TextView name) {
        new HuanXinRequest().getHuanxinUserList(String.valueOf(id), queue, new DefaultCallback(){
            @Override
            public void success(Object obj) {
                super.success(obj);
                if(obj instanceof ArrayList){
                    @SuppressLint("Unchecked")
                    ArrayList<HuanxinUser> list = (ArrayList<HuanxinUser>)obj;
                    if(list.size() == 1) {
                        if (name != null) {
                            HuanxinUser us = list.get(0);
                            name.setText(us.getName());
                            if ((us.getAvatar() != null)
                                    && (!us.getAvatar().equals(""))) {
                                loadpersonPic(id, us.getAvatar(), us.getName(),
                                        avatar, 1);
                            } else {
                                avatar.setImageResource(R.drawable.default_avatar);
                            }
                            realNames.add(us.getName());// 记录所有真实用户名 用于搜索
                        }
                    }
                }
            }
        });
	}

	/**
	 * @Description: 加载图片
	 * @author howe
	 * @date 2014-7-30 下午5:57:52 加载图片,若是第一次加载则下载图片到本地SD卡中
	 */
	public void loadpersonPic(final String id, final String url,
			final String realName, final ImageView imageView, final int isRound) {
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
							edt.putString(id + "realname", realName);
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
	// ================howe end==================
}
