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

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.text.Spannable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
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
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.FileMessageBody;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.LocationMessageBody;
import com.easemob.chat.NormalFileMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VideoMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.activity.AlertDialog;
import com.easemob.chatuidemo.activity.BaiduMapActivity;
import com.easemob.chatuidemo.activity.ChatActivity;
import com.easemob.chatuidemo.activity.ContextMenu;
import com.easemob.chatuidemo.activity.GroupDetailsActivity;
import com.easemob.chatuidemo.activity.ShowBigImage;
import com.easemob.chatuidemo.activity.ShowNormalFileActivity;
import com.easemob.chatuidemo.activity.ShowVideoActivity;
import com.easemob.chatuidemo.task.LoadImageTask;
import com.easemob.chatuidemo.task.LoadVideoImageTask;
import com.easemob.chatuidemo.utils.ImageCache;
import com.easemob.chatuidemo.utils.ImageUtils;
import com.easemob.chatuidemo.utils.SmileUtils;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.DateUtils;
import com.easemob.util.EMLog;
import com.easemob.util.FileUtils;
import com.easemob.util.LatLng;
import com.easemob.util.TextFormater;
import com.qingmu.jianzhidaren.R;
import com.quark.common.JsonUtil;
import com.quark.common.ToastUtil;
import com.quark.common.Url;
import com.quark.guangchang.ActivityDetialActivity;
import com.quark.http.image.LoadImage;
import com.quark.image.UploadImg;
import com.quark.model.HuanxinUser;
import com.quark.volley.VolleySington;
import com.umeng.analytics.MobclickAgent;

public class MessageAdapter extends BaseAdapter {

	private final static String TAG = "msg";

	private static final int MESSAGE_TYPE_RECV_TXT = 0;
	private static final int MESSAGE_TYPE_SENT_TXT = 1;
	private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
	private static final int MESSAGE_TYPE_SENT_LOCATION = 3;
	private static final int MESSAGE_TYPE_RECV_LOCATION = 4;
	private static final int MESSAGE_TYPE_RECV_IMAGE = 5;
	private static final int MESSAGE_TYPE_SENT_VOICE = 6;
	private static final int MESSAGE_TYPE_RECV_VOICE = 7;
	private static final int MESSAGE_TYPE_SENT_VIDEO = 8;
	private static final int MESSAGE_TYPE_RECV_VIDEO = 9;
	private static final int MESSAGE_TYPE_SENT_FILE = 10;
	private static final int MESSAGE_TYPE_RECV_FILE = 11;
	private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 12;
	private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 13;
	private static final int MESSAGE_TYPE_SENT_TXT_ZIDINGYI = 14;
	private static final int MESSAGE_TYPE_RECV_TXT_ZIDINGYI = 15;

	public static final String IMAGE_DIR = "chat/image/";
	public static final String VOICE_DIR = "chat/audio/";
	public static final String VIDEO_DIR = "chat/video";

	private String username;
	private LayoutInflater inflater;
	private Activity activity;
	private SharedPreferences sp;
	// reference to conversation object in chatsdk
	private EMConversation conversation;

	private Context context;
	RequestQueue queue = VolleySington.getInstance().getRequestQueue();

	private Map<String, Timer> timers = new Hashtable<String, Timer>();

	public MessageAdapter(Context context, String username, int chatType) {
		this.username = username;
		this.context = context;
		inflater = LayoutInflater.from(context);
		activity = (Activity) context;
		this.conversation = EMChatManager.getInstance().getConversation(
				username);
		sp = context.getSharedPreferences("jrdr.setting", context.MODE_PRIVATE);
	}

	/**
	 * 获取item数
	 */
	public int getCount() {
		return conversation.getMsgCount();
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		notifyDataSetChanged();
	}

	public EMMessage getItem(int position) {
		return conversation.getMessage(position);
	}

	public EMConversation getEMConversationItem(int position) {
		return conversation;
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * 获取item类型
	 */
	public int getItemViewType(int position) {
		EMMessage message = conversation.getMessage(position);
		if (message.getType() == EMMessage.Type.TXT) {
			if (!message.getBooleanAttribute(
					Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
				if ("1".equals(message.getStringAttribute(
						Constant.MESSAGE_ATTR_IS_EXTRA, "0"))) {
					return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TXT_ZIDINGYI
							: MESSAGE_TYPE_SENT_TXT_ZIDINGYI;
				} else {
					return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TXT
							: MESSAGE_TYPE_SENT_TXT;
				}
			} else {
				return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL
						: MESSAGE_TYPE_SENT_VOICE_CALL;
			}
		}
		if (message.getType() == EMMessage.Type.IMAGE) {
			return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_IMAGE
					: MESSAGE_TYPE_SENT_IMAGE;

		}
		if (message.getType() == EMMessage.Type.LOCATION) {
			return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_LOCATION
					: MESSAGE_TYPE_SENT_LOCATION;
		}
		if (message.getType() == EMMessage.Type.VOICE) {
			return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE
					: MESSAGE_TYPE_SENT_VOICE;
		}
		if (message.getType() == EMMessage.Type.VIDEO) {
			return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO
					: MESSAGE_TYPE_SENT_VIDEO;
		}
		if (message.getType() == EMMessage.Type.FILE) {
			return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_FILE
					: MESSAGE_TYPE_SENT_FILE;
		}

		return -1;// invalid
	}

	public int getViewTypeCount() {
		return 16;
	}

	private View createViewByMessage(EMMessage message, int position) {
		switch (message.getType()) {
		case LOCATION:
			return message.direct == EMMessage.Direct.RECEIVE ? inflater
					.inflate(R.layout.row_received_location, null) : inflater
					.inflate(R.layout.row_sent_location, null);
		case IMAGE:
			return message.direct == EMMessage.Direct.RECEIVE ? inflater
					.inflate(R.layout.row_received_picture, null) : inflater
					.inflate(R.layout.row_sent_picture, null);
		case VOICE:
			return message.direct == EMMessage.Direct.RECEIVE ? inflater
					.inflate(R.layout.row_received_voice, null) : inflater
					.inflate(R.layout.row_sent_voice, null);
		case VIDEO:
			return message.direct == EMMessage.Direct.RECEIVE ? inflater
					.inflate(R.layout.row_received_video, null) : inflater
					.inflate(R.layout.row_sent_video, null);
		case FILE:
			return message.direct == EMMessage.Direct.RECEIVE ? inflater
					.inflate(R.layout.row_received_file, null) : inflater
					.inflate(R.layout.row_sent_file, null);
		default:
			// 语音电话
			if (message.getBooleanAttribute(
					Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
				return message.direct == EMMessage.Direct.RECEIVE ? inflater
						.inflate(R.layout.row_received_voice_call, null)
						: inflater.inflate(R.layout.row_sent_voice_call, null);
				// 先判断是否是有拓展属性
			} else {
				if ("1".equals(message.getStringAttribute(
						Constant.MESSAGE_ATTR_IS_EXTRA, "0"))) {
					return message.direct == EMMessage.Direct.RECEIVE ? inflater
							.inflate(R.layout.row_received_message_zidingyi,
									null) : inflater.inflate(
							R.layout.row_sent_message_zidingyi, null);
				} else {
					return message.direct == EMMessage.Direct.RECEIVE ? inflater
							.inflate(R.layout.row_received_message, null)
							: inflater.inflate(R.layout.row_sent_message, null);
				}

			}
		}
	}

	@SuppressLint("NewApi")
	public View getView(final int position, View convertView, ViewGroup parent) {
		final EMMessage message = getItem(position);
		ChatType chatType = message.getChatType();
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = createViewByMessage(message, position);
			if (message.getType() == EMMessage.Type.IMAGE) {
				try {
					holder.iv = ((ImageView) convertView
							.findViewById(R.id.iv_sendPicture));
					holder.head_iv = (ImageView) convertView
							.findViewById(R.id.iv_userhead);
					holder.tv = (TextView) convertView
							.findViewById(R.id.percentage);
					holder.pb = (ProgressBar) convertView
							.findViewById(R.id.progressBar);
					holder.staus_iv = (ImageView) convertView
							.findViewById(R.id.msg_status);
					holder.tv_userId = (TextView) convertView
							.findViewById(R.id.tv_userid);
				} catch (Exception e) {
				}

			} else if (message.getType() == EMMessage.Type.TXT) {

				try {
					holder.pb = (ProgressBar) convertView
							.findViewById(R.id.pb_sending);
					holder.staus_iv = (ImageView) convertView
							.findViewById(R.id.msg_status);
					holder.head_iv = (ImageView) convertView
							.findViewById(R.id.iv_userhead);
					// 这里是文字内容
					holder.tv = (TextView) convertView
							.findViewById(R.id.tv_chatcontent);
					holder.tv_userId = (TextView) convertView
							.findViewById(R.id.tv_userid);
				} catch (Exception e) {
				}

				// 语音通话
				if (message.getBooleanAttribute(
						Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
					holder.iv = (ImageView) convertView
							.findViewById(R.id.iv_call_icon);
					holder.tv = (TextView) convertView
							.findViewById(R.id.tv_chatcontent);
				}
				// 拓展的消息体
				if ("1".equals(message.getStringAttribute(
						Constant.MESSAGE_ATTR_IS_EXTRA, "0"))) {
					holder.zidingyi_chat_layout = (RelativeLayout) convertView
							.findViewById(R.id.zidingyi_chat_layout);// 拓展消息体
					holder.tv = (TextView) convertView
							.findViewById(R.id.tv_chatcontent);// 活动名称+工资
					holder.tv_job_place = (TextView) convertView// 工作区域
							.findViewById(R.id.tv_job_place);
					holder.tv_job_time = (TextView) convertView// 工作时间
							.findViewById(R.id.tv_job_date);
					holder.tv_job_zhaomurenshu = (TextView) convertView// 工作招募人数
							.findViewById(R.id.tv_job_zhaomu_renshu);
				}

			} else if (message.getType() == EMMessage.Type.VOICE) {
				try {
					holder.iv = ((ImageView) convertView
							.findViewById(R.id.iv_voice));
					holder.head_iv = (ImageView) convertView
							.findViewById(R.id.iv_userhead);
					holder.tv = (TextView) convertView
							.findViewById(R.id.tv_length);
					holder.pb = (ProgressBar) convertView
							.findViewById(R.id.pb_sending);
					holder.staus_iv = (ImageView) convertView
							.findViewById(R.id.msg_status);
					holder.tv_userId = (TextView) convertView
							.findViewById(R.id.tv_userid);
					holder.iv_read_status = (ImageView) convertView
							.findViewById(R.id.iv_unread_voice);
				} catch (Exception e) {
				}
			} else if (message.getType() == EMMessage.Type.LOCATION) {
				try {
					holder.head_iv = (ImageView) convertView
							.findViewById(R.id.iv_userhead);
					holder.tv = (TextView) convertView
							.findViewById(R.id.tv_location);
					holder.pb = (ProgressBar) convertView
							.findViewById(R.id.pb_sending);
					holder.staus_iv = (ImageView) convertView
							.findViewById(R.id.msg_status);
					holder.tv_userId = (TextView) convertView
							.findViewById(R.id.tv_userid);
				} catch (Exception e) {
				}
			} else if (message.getType() == EMMessage.Type.VIDEO) {
				try {
					holder.iv = ((ImageView) convertView
							.findViewById(R.id.chatting_content_iv));
					holder.head_iv = (ImageView) convertView
							.findViewById(R.id.iv_userhead);
					holder.tv = (TextView) convertView
							.findViewById(R.id.percentage);
					holder.pb = (ProgressBar) convertView
							.findViewById(R.id.progressBar);
					holder.staus_iv = (ImageView) convertView
							.findViewById(R.id.msg_status);
					holder.size = (TextView) convertView
							.findViewById(R.id.chatting_size_iv);
					holder.timeLength = (TextView) convertView
							.findViewById(R.id.chatting_length_iv);
					holder.playBtn = (ImageView) convertView
							.findViewById(R.id.chatting_status_btn);
					holder.container_status_btn = (LinearLayout) convertView
							.findViewById(R.id.container_status_btn);
					holder.tv_userId = (TextView) convertView
							.findViewById(R.id.tv_userid);

				} catch (Exception e) {
				}
			} else if (message.getType() == EMMessage.Type.FILE) {
				try {
					holder.head_iv = (ImageView) convertView
							.findViewById(R.id.iv_userhead);
					holder.tv_file_name = (TextView) convertView
							.findViewById(R.id.tv_file_name);
					holder.tv_file_size = (TextView) convertView
							.findViewById(R.id.tv_file_size);
					holder.pb = (ProgressBar) convertView
							.findViewById(R.id.pb_sending);
					holder.staus_iv = (ImageView) convertView
							.findViewById(R.id.msg_status);
					holder.tv_file_download_state = (TextView) convertView
							.findViewById(R.id.tv_file_state);
					holder.ll_container = (LinearLayout) convertView
							.findViewById(R.id.ll_file_container);
					// 这里是进度值
					holder.tv = (TextView) convertView
							.findViewById(R.id.percentage);
				} catch (Exception e) {
				}
				try {
					holder.tv_userId = (TextView) convertView
							.findViewById(R.id.tv_userid);
				} catch (Exception e) {
				}
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 群聊时，显示接收的消息的发送人的名称,接收方
		if (chatType == ChatType.GroupChat
				&& message.direct == EMMessage.Direct.RECEIVE)
		// holder.tv_userId.setText(message.getFrom());
		{ // 用username代替nick
			// 先判断图片是否在本地已经保存,若存在则不加载
			if (message.getFrom().equals("jianzhidaren")) {
				// message.getFrom()带了u或者c
				Drawable draw1 = context.getResources().getDrawable(
						R.drawable.job_photo);
				BitmapDrawable bd = (BitmapDrawable) draw1;
				Bitmap bitmap = bd.getBitmap();
				Bitmap bit = UploadImg.toRoundCorner(bitmap, 2);
				holder.head_iv.setImageBitmap(bit);
			} else if ("caiwu".equals(message.getFrom())) {
				Drawable draw1 = context.getResources().getDrawable(
						R.drawable.custom_caiwu);
				BitmapDrawable bd = (BitmapDrawable) draw1;
				Bitmap bitmap = bd.getBitmap();
				Bitmap bit = UploadImg.toRoundCorner(bitmap, 2);
				holder.head_iv.setImageBitmap(bit);
			} else if ("dingyue".equals(message.getFrom())) {
				Drawable draw1 = context.getResources().getDrawable(
						R.drawable.custom_xiaozhushou);
				BitmapDrawable bd = (BitmapDrawable) draw1;
				Bitmap bitmap = bd.getBitmap();
				Bitmap bit = UploadImg.toRoundCorner(bitmap, 2);
				holder.head_iv.setImageBitmap(bit);
			} else if ("kefu".equals(message.getFrom())) {
				Drawable draw1 = context.getResources().getDrawable(
						R.drawable.custom_kefu);
				BitmapDrawable bd = (BitmapDrawable) draw1;
				Bitmap bitmap = bd.getBitmap();
				Bitmap bit = UploadImg.toRoundCorner(bitmap, 2);
				holder.head_iv.setImageBitmap(bit);
			} else if ("tongzhi".equals(message.getFrom())) {
				Drawable draw1 = context.getResources().getDrawable(
						R.drawable.custom_tongzhi);
				BitmapDrawable bd = (BitmapDrawable) draw1;
				Bitmap bitmap = bd.getBitmap();
				Bitmap bit = UploadImg.toRoundCorner(bitmap, 2);
				holder.head_iv.setImageBitmap(bit);
			} else {
				loadNativePhoto(message.getFrom(), holder.head_iv,
						holder.tv_userId);
			}
		}
		// 发送方 单聊 如果是发送的消息并且不是群聊消息，显示已读textview
		if (message.direct == EMMessage.Direct.SEND
				&& chatType != ChatType.GroupChat) {
			holder.tv_ack = (TextView) convertView.findViewById(R.id.tv_ack);
			holder.tv_delivered = (TextView) convertView
					.findViewById(R.id.tv_delivered);
			if (holder.tv_ack != null) {
				if (message.isAcked) {
					if (holder.tv_delivered != null) {
						holder.tv_delivered.setVisibility(View.INVISIBLE);
					}
					holder.tv_ack.setVisibility(View.VISIBLE);
				} else {
					holder.tv_ack.setVisibility(View.INVISIBLE);

					// check and display msg delivered ack status
					if (holder.tv_delivered != null) {
						if (message.isDelivered) {
							holder.tv_delivered.setVisibility(View.VISIBLE);
						} else {
							holder.tv_delivered.setVisibility(View.INVISIBLE);
						}
					}
				}
			}
			if (message.getFrom().equals("jianzhidaren")) {
				Drawable draw1 = context.getResources().getDrawable(
						R.drawable.job_photo);
				BitmapDrawable bd = (BitmapDrawable) draw1;
				Bitmap bitmap = bd.getBitmap();
				Bitmap bit = UploadImg.toRoundCorner(bitmap, 2);
				holder.head_iv.setImageBitmap(bit);
				// getNick(message.getFrom(), holder.head_iv, null);
			} else if ("caiwu".equals(message.getFrom())) {
				Drawable draw1 = context.getResources().getDrawable(
						R.drawable.custom_caiwu);
				BitmapDrawable bd = (BitmapDrawable) draw1;
				Bitmap bitmap = bd.getBitmap();
				Bitmap bit = UploadImg.toRoundCorner(bitmap, 2);
				holder.head_iv.setImageBitmap(bit);
			} else if ("dingyue".equals(message.getFrom())) {
				Drawable draw1 = context.getResources().getDrawable(
						R.drawable.custom_xiaozhushou);
				BitmapDrawable bd = (BitmapDrawable) draw1;
				Bitmap bitmap = bd.getBitmap();
				Bitmap bit = UploadImg.toRoundCorner(bitmap, 2);
				holder.head_iv.setImageBitmap(bit);
			} else if ("kefu".equals(message.getFrom())) {
				Drawable draw1 = context.getResources().getDrawable(
						R.drawable.custom_kefu);
				BitmapDrawable bd = (BitmapDrawable) draw1;
				Bitmap bitmap = bd.getBitmap();
				Bitmap bit = UploadImg.toRoundCorner(bitmap, 2);
				holder.head_iv.setImageBitmap(bit);
			} else if ("tongzhi".equals(message.getFrom())) {
				Drawable draw1 = context.getResources().getDrawable(
						R.drawable.custom_tongzhi);
				BitmapDrawable bd = (BitmapDrawable) draw1;
				Bitmap bitmap = bd.getBitmap();
				Bitmap bit = UploadImg.toRoundCorner(bitmap, 2);
				holder.head_iv.setImageBitmap(bit);
			} else {
				loadNativePhoto(message.getFrom(), holder.head_iv, null);
			}
		} else {
			// 如果是文本或者地图消息并且不是group messgae，显示的时候给对方发送已读回执
			if ((message.getType() == Type.TXT || message.getType() == Type.LOCATION)
					&& !message.isAcked && chatType != ChatType.GroupChat) {
				// 不是语音通话记录
				if (!message.getBooleanAttribute(
						Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
					try {
						EMChatManager.getInstance().ackMessageRead(
								message.getFrom(), message.getMsgId());
						// 发送已读回执
						message.isAcked = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if (message.getFrom().equals("jianzhidaren")) {
				Drawable draw1 = context.getResources().getDrawable(
						R.drawable.job_photo);
				BitmapDrawable bd = (BitmapDrawable) draw1;
				Bitmap bitmap = bd.getBitmap();
				Bitmap bit = UploadImg.toRoundCorner(bitmap, 2);
				holder.head_iv.setImageBitmap(bit);
				// getNick(message.getFrom(), holder.head_iv, null);
			} else if ("caiwu".equals(message.getFrom())) {
				Drawable draw1 = context.getResources().getDrawable(
						R.drawable.custom_caiwu);
				BitmapDrawable bd = (BitmapDrawable) draw1;
				Bitmap bitmap = bd.getBitmap();
				Bitmap bit = UploadImg.toRoundCorner(bitmap, 2);
				holder.head_iv.setImageBitmap(bit);
			} else if ("dingyue".equals(message.getFrom())) {
				Drawable draw1 = context.getResources().getDrawable(
						R.drawable.custom_xiaozhushou);
				BitmapDrawable bd = (BitmapDrawable) draw1;
				Bitmap bitmap = bd.getBitmap();
				Bitmap bit = UploadImg.toRoundCorner(bitmap, 2);
				holder.head_iv.setImageBitmap(bit);
			} else if ("kefu".equals(message.getFrom())) {
				Drawable draw1 = context.getResources().getDrawable(
						R.drawable.custom_kefu);
				BitmapDrawable bd = (BitmapDrawable) draw1;
				Bitmap bitmap = bd.getBitmap();
				Bitmap bit = UploadImg.toRoundCorner(bitmap, 2);
				holder.head_iv.setImageBitmap(bit);
			} else if ("tongzhi".equals(message.getFrom())) {
				Drawable draw1 = context.getResources().getDrawable(
						R.drawable.custom_tongzhi);
				BitmapDrawable bd = (BitmapDrawable) draw1;
				Bitmap bitmap = bd.getBitmap();
				Bitmap bit = UploadImg.toRoundCorner(bitmap, 2);
				holder.head_iv.setImageBitmap(bit);
			} else {
				loadNativePhoto(message.getFrom(), holder.head_iv, null);
			}
		}

		switch (message.getType()) {
		// 根据消息type显示item
		case IMAGE: // 图片
			handleImageMessage(message, holder, position, convertView);
			break;
		case TXT: // 文本
			if (!message.getBooleanAttribute(
					Constant.MESSAGE_ATTR_IS_VOICE_CALL, false))
				handleTextMessage(message, holder, position);
			else
				// 语音电话
				handleVoiceCallMessage(message, holder, position);
			break;
		case LOCATION: // 位置
			handleLocationMessage(message, holder, position, convertView);
			break;
		case VOICE: // 语音
			handleVoiceMessage(message, holder, position, convertView);
			break;
		case VIDEO: // 视频
			handleVideoMessage(message, holder, position, convertView);
			break;
		case FILE: // 一般文件
			handleFileMessage(message, holder, position, convertView);
			break;
		default:
			// not supported
		}

		if (message.direct == EMMessage.Direct.SEND) {
			View statusView = convertView.findViewById(R.id.msg_status);
			// 重发按钮点击事件
			statusView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					// 显示重发消息的自定义alertdialog
					Intent intent = new Intent(activity, AlertDialog.class);
					intent.putExtra("msg",
							activity.getString(R.string.confirm_resend));
					intent.putExtra("title",
							activity.getString(R.string.resend));
					intent.putExtra("cancel", true);
					intent.putExtra("position", position);
					if (message.getType() == EMMessage.Type.TXT)
						activity.startActivityForResult(intent,
								ChatActivity.REQUEST_CODE_TEXT);
					else if (message.getType() == EMMessage.Type.VOICE)
						activity.startActivityForResult(intent,
								ChatActivity.REQUEST_CODE_VOICE);
					else if (message.getType() == EMMessage.Type.IMAGE)
						activity.startActivityForResult(intent,
								ChatActivity.REQUEST_CODE_PICTURE);
					else if (message.getType() == EMMessage.Type.LOCATION)
						activity.startActivityForResult(intent,
								ChatActivity.REQUEST_CODE_LOCATION);
					else if (message.getType() == EMMessage.Type.FILE)
						activity.startActivityForResult(intent,
								ChatActivity.REQUEST_CODE_FILE);
					else if (message.getType() == EMMessage.Type.VIDEO)
						activity.startActivityForResult(intent,
								ChatActivity.REQUEST_CODE_VIDEO);
				}
			});

		} else {
			// 长按头像，移入黑名单
			holder.head_iv.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					Intent intent = new Intent(activity, AlertDialog.class);
					intent.putExtra("msg", "移入到黑名单？");
					intent.putExtra("cancel", true);
					intent.putExtra("position", position);
					activity.startActivityForResult(intent,
							ChatActivity.REQUEST_CODE_ADD_TO_BLACKLIST);
					return true;
				}
			});

			// 按头像，查看个人资料 howe
			holder.head_iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if ("jianzhidaren".equals(message.getFrom())) {
						ToastUtil.showShortToast("兼职达人团队");
					} else if ("caiwu".equals(message.getFrom())) {
						ToastUtil.showShortToast("财务小管家");
					} else if ("dingyue".equals(message.getFrom())) {
						ToastUtil.showShortToast("订阅小助手");
					} else if ("kefu".equals(message.getFrom())) {
						ToastUtil.showShortToast("兼职达人客服");
					} else if ("tongzhi".equals(message.getFrom())) {
						ToastUtil.showShortToast("通知中心");
					} else {
						// Intent intent = new Intent(activity, UserInfo.class);
						// intent.putExtra("hxId", message.getFrom());
						// activity.startActivity(intent);
						activity.startActivity(new Intent(activity,
								ChatActivity.class).putExtra("userId",
								message.getFrom()));
					}
				}
			});
		}

		TextView timestamp = (TextView) convertView
				.findViewById(R.id.timestamp);

		if (position == 0) {
			timestamp.setText(DateUtils.getTimestampString(new Date(message
					.getMsgTime())));
			timestamp.setVisibility(View.VISIBLE);
		} else {
			// 两条消息时间离得如果稍长，显示时间
			if (DateUtils.isCloseEnough(message.getMsgTime(), conversation
					.getMessage(position - 1).getMsgTime())) {
				timestamp.setVisibility(View.GONE);
			} else {
				timestamp.setText(DateUtils.getTimestampString(new Date(message
						.getMsgTime())));
				timestamp.setVisibility(View.VISIBLE);
			}
		}
		return convertView;
	}

	/**
	 * 文本消息
	 * 
	 * @param message
	 * @param holder
	 * @param position
	 */
	private void handleTextMessage(EMMessage message, ViewHolder holder,
			final int position) {

		TextMessageBody txtBody = (TextMessageBody) message.getBody();

		Spannable span = SmileUtils
				.getSmiledText(context, txtBody.getMessage());
		// 设置内容
		if (span.toString().endsWith("邀请你加入了群聊")) {
			holder.tv.setText("欢迎加入群聊", BufferType.SPANNABLE);
		} else {
			holder.tv.setText(span, BufferType.SPANNABLE);
		}
		// 如果是拓展消息的文本
		if ("1".equals(message.getStringAttribute(
				Constant.MESSAGE_ATTR_IS_EXTRA, "0"))) {

			final String activityId = message.getStringAttribute("activityId",
					"");
			String activityTitle = message.getStringAttribute("activityTitle",
					"");
			String activityXinZi = message.getStringAttribute("activityXinZi",
					"");
			String activityJobPlace = message.getStringAttribute(
					"activityJobPlace", "");
			String activityStartTime = message.getStringAttribute(
					"activityStartTime", "");
			String leftcount = message.getStringAttribute("leftCount", "0");
			holder.tv.setText(activityTitle + activityXinZi);
			holder.tv_job_place.setText("工作区域:" + activityJobPlace);
			holder.tv_job_time.setText("工作时间:" + activityStartTime);
			holder.tv_job_zhaomurenshu.setText("还差人数:" + leftcount + "人");

			holder.zidingyi_chat_layout
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							// 跳转到活动详情界面
							Intent intent = new Intent();
							intent.setClass(context,
									ActivityDetialActivity.class);
							intent.putExtra("activity_id", activityId);
							intent.putExtra("isComeFromGuangChang", false);
							context.startActivity(intent);

						}
					});
		} else {
			// 设置长按事件监听
			holder.tv.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					activity.startActivityForResult((new Intent(activity,
							ContextMenu.class)).putExtra("position", position)
							.putExtra("type", EMMessage.Type.TXT.ordinal()),
							ChatActivity.REQUEST_CODE_CONTEXT_MENU);
					return true;
				}
			});
		}

		if (message.direct == EMMessage.Direct.SEND) {
			switch (message.status) {
			case SUCCESS: // 发送成功
				holder.pb.setVisibility(View.GONE);
				holder.staus_iv.setVisibility(View.GONE);
				break;
			case FAIL: // 发送失败
				holder.pb.setVisibility(View.GONE);
				holder.staus_iv.setVisibility(View.VISIBLE);
				break;
			case INPROGRESS: // 发送中
				holder.pb.setVisibility(View.VISIBLE);
				holder.staus_iv.setVisibility(View.GONE);
				break;
			default:
				// 发送消息
				sendMsgInBackground(message, holder);
			}
		}
	}

	/**
	 * 语音通话记录
	 * 
	 * @param message
	 * @param holder
	 * @param position
	 */
	private void handleVoiceCallMessage(EMMessage message, ViewHolder holder,
			final int position) {
		TextMessageBody txtBody = (TextMessageBody) message.getBody();
		holder.tv.setText(txtBody.getMessage());

	}

	/**
	 * 图片消息
	 * 
	 * @param message
	 * @param holder
	 * @param position
	 * @param convertView
	 */
	private void handleImageMessage(final EMMessage message,
			final ViewHolder holder, final int position, View convertView) {
		holder.pb.setTag(position);
		holder.iv.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				activity.startActivityForResult((new Intent(activity,
						ContextMenu.class)).putExtra("position", position)
						.putExtra("type", EMMessage.Type.IMAGE.ordinal()),
						ChatActivity.REQUEST_CODE_CONTEXT_MENU);
				return true;
			}
		});

		// 接收方向的消息
		if (message.direct == EMMessage.Direct.RECEIVE) {
			// "it is receive msg";
			if (message.status == EMMessage.Status.INPROGRESS) {
				// "!!!! back receive";
				holder.iv.setImageResource(R.drawable.default_image);
				showDownloadImageProgress(message, holder);
				// downloadImage(message, holder);
			} else {
				// "!!!! not back receive, show image directly");
				holder.pb.setVisibility(View.GONE);
				holder.tv.setVisibility(View.GONE);
				holder.iv.setImageResource(R.drawable.default_image);
				ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
				if (imgBody.getLocalUrl() != null) {
					// String filePath = imgBody.getLocalUrl();
					String remotePath = imgBody.getRemoteUrl();
					String filePath = ImageUtils.getImagePath(remotePath);
					String thumbRemoteUrl = imgBody.getThumbnailUrl();
					String thumbnailPath = ImageUtils
							.getThumbnailImagePath(thumbRemoteUrl);
					showImageView(thumbnailPath, holder.iv, filePath,
							imgBody.getRemoteUrl(), message);
				}
			}
			return;
		}

		// 发送的消息
		// process send message
		// send pic, show the pic directly
		ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
		String filePath = imgBody.getLocalUrl();
		if (filePath != null && new File(filePath).exists()) {
			showImageView(ImageUtils.getThumbnailImagePath(filePath),
					holder.iv, filePath, null, message);
		} else {
			showImageView(ImageUtils.getThumbnailImagePath(filePath),
					holder.iv, filePath, IMAGE_DIR, message);
		}

		switch (message.status) {
		case SUCCESS:
			holder.pb.setVisibility(View.GONE);
			holder.tv.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.GONE);
			break;
		case FAIL:
			holder.pb.setVisibility(View.GONE);
			holder.tv.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.VISIBLE);
			break;
		case INPROGRESS:
			holder.staus_iv.setVisibility(View.GONE);
			holder.pb.setVisibility(View.VISIBLE);
			holder.tv.setVisibility(View.VISIBLE);
			if (timers.containsKey(message.getMsgId()))
				return;
			// set a timer
			final Timer timer = new Timer();
			timers.put(message.getMsgId(), timer);
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					activity.runOnUiThread(new Runnable() {
						public void run() {
							holder.pb.setVisibility(View.VISIBLE);
							holder.tv.setVisibility(View.VISIBLE);
							holder.tv.setText(message.progress + "%");
							if (message.status == EMMessage.Status.SUCCESS) {
								holder.pb.setVisibility(View.GONE);
								holder.tv.setVisibility(View.GONE);
								// message.setSendingStatus(Message.SENDING_STATUS_SUCCESS);
								timer.cancel();
							} else if (message.status == EMMessage.Status.FAIL) {
								holder.pb.setVisibility(View.GONE);
								holder.tv.setVisibility(View.GONE);
								// message.setSendingStatus(Message.SENDING_STATUS_FAIL);
								// message.setProgress(0);
								holder.staus_iv.setVisibility(View.VISIBLE);
								Toast.makeText(
										activity,
										activity.getString(R.string.send_fail)
												+ activity
														.getString(R.string.connect_failuer_toast),
										0).show();
								timer.cancel();
							}

						}
					});

				}
			}, 0, 500);
			break;
		default:
			sendPictureMessage(message, holder);
		}
	}

	/**
	 * 视频消息
	 * 
	 * @param message
	 * @param holder
	 * @param position
	 * @param convertView
	 */
	private void handleVideoMessage(final EMMessage message,
			final ViewHolder holder, final int position, View convertView) {

		VideoMessageBody videoBody = (VideoMessageBody) message.getBody();
		// final File image=new File(PathUtil.getInstance().getVideoPath(),
		// videoBody.getFileName());
		String localThumb = videoBody.getLocalThumb();

		holder.iv.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				activity.startActivityForResult(new Intent(activity,
						ContextMenu.class).putExtra("position", position)
						.putExtra("type", EMMessage.Type.VIDEO.ordinal()),
						ChatActivity.REQUEST_CODE_CONTEXT_MENU);
				return true;
			}
		});

		if (localThumb != null) {

			showVideoThumbView(localThumb, holder.iv,
					videoBody.getThumbnailUrl(), message);
		}
		if (videoBody.getLength() > 0) {
			String time = DateUtils.toTimeBySecond(videoBody.getLength());
			holder.timeLength.setText(time);
		}
		holder.playBtn.setImageResource(R.drawable.video_download_btn_nor);

		if (message.direct == EMMessage.Direct.RECEIVE) {
			if (videoBody.getVideoFileLength() > 0) {
				String size = TextFormater.getDataSize(videoBody
						.getVideoFileLength());
				holder.size.setText(size);
			}
		} else {
			if (videoBody.getLocalUrl() != null
					&& new File(videoBody.getLocalUrl()).exists()) {
				String size = TextFormater.getDataSize(new File(videoBody
						.getLocalUrl()).length());
				holder.size.setText(size);
			}
		}

		if (message.direct == EMMessage.Direct.RECEIVE) {

			// System.err.println("it is receive msg");
			if (message.status == EMMessage.Status.INPROGRESS) {
				// System.err.println("!!!! back receive");
				holder.iv.setImageResource(R.drawable.default_image);
				showDownloadImageProgress(message, holder);

			} else {
				// System.err.println("!!!! not back receive, show image directly");
				holder.iv.setImageResource(R.drawable.default_image);
				if (localThumb != null) {
					showVideoThumbView(localThumb, holder.iv,
							videoBody.getThumbnailUrl(), message);
				}

			}

			return;
		}
		holder.pb.setTag(position);

		// until here ,deal with send video msg
		switch (message.status) {
		case SUCCESS:
			holder.pb.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.GONE);
			holder.tv.setVisibility(View.GONE);
			break;
		case FAIL:
			holder.pb.setVisibility(View.GONE);
			holder.tv.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.VISIBLE);
			break;
		case INPROGRESS:
			if (timers.containsKey(message.getMsgId()))
				return;
			// set a timer
			final Timer timer = new Timer();
			timers.put(message.getMsgId(), timer);
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							holder.pb.setVisibility(View.VISIBLE);
							holder.tv.setVisibility(View.VISIBLE);
							holder.tv.setText(message.progress + "%");
							if (message.status == EMMessage.Status.SUCCESS) {
								holder.pb.setVisibility(View.GONE);
								holder.tv.setVisibility(View.GONE);
								// message.setSendingStatus(Message.SENDING_STATUS_SUCCESS);
								timer.cancel();
							} else if (message.status == EMMessage.Status.FAIL) {
								holder.pb.setVisibility(View.GONE);
								holder.tv.setVisibility(View.GONE);
								// message.setSendingStatus(Message.SENDING_STATUS_FAIL);
								// message.setProgress(0);
								holder.staus_iv.setVisibility(View.VISIBLE);
								Toast.makeText(
										activity,
										activity.getString(R.string.send_fail)
												+ activity
														.getString(R.string.connect_failuer_toast),
										0).show();
								timer.cancel();
							}

						}
					});

				}
			}, 0, 500);
			break;
		default:
			// sendMsgInBackground(message, holder);
			sendPictureMessage(message, holder);

		}

	}

	/**
	 * 语音消息
	 * 
	 * @param message
	 * @param holder
	 * @param position
	 * @param convertView
	 */
	private void handleVoiceMessage(final EMMessage message,
			final ViewHolder holder, final int position, View convertView) {
		VoiceMessageBody voiceBody = (VoiceMessageBody) message.getBody();
		holder.tv.setText(voiceBody.getLength() + "\"");
		holder.iv.setOnClickListener(new VoicePlayClickListener(message,
				holder.iv, holder.iv_read_status, this, activity, username));
		holder.iv.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				activity.startActivityForResult((new Intent(activity,
						ContextMenu.class)).putExtra("position", position)
						.putExtra("type", EMMessage.Type.VOICE.ordinal()),
						ChatActivity.REQUEST_CODE_CONTEXT_MENU);
				return true;
			}
		});
		if (((ChatActivity) activity).playMsgId != null
				&& ((ChatActivity) activity).playMsgId.equals(message
						.getMsgId()) && VoicePlayClickListener.isPlaying) {
			AnimationDrawable voiceAnimation;
			if (message.direct == EMMessage.Direct.RECEIVE) {
				holder.iv.setImageResource(R.anim.voice_from_icon);
			} else {
				holder.iv.setImageResource(R.anim.voice_to_icon);
			}
			voiceAnimation = (AnimationDrawable) holder.iv.getDrawable();
			voiceAnimation.start();
		} else {
			if (message.direct == EMMessage.Direct.RECEIVE) {
				holder.iv.setImageResource(R.drawable.chatfrom_voice_playing);
			} else {
				holder.iv.setImageResource(R.drawable.chatto_voice_playing);
			}
		}

		if (message.direct == EMMessage.Direct.RECEIVE) {
			if (message.isListened()) {
				// 隐藏语音未听标志
				holder.iv_read_status.setVisibility(View.INVISIBLE);
			} else {
				holder.iv_read_status.setVisibility(View.VISIBLE);
			}
			System.err.println("it is receive msg");
			if (message.status == EMMessage.Status.INPROGRESS) {
				holder.pb.setVisibility(View.VISIBLE);
				System.err.println("!!!! back receive");
				((FileMessageBody) message.getBody())
						.setDownloadCallback(new EMCallBack() {

							@Override
							public void onSuccess() {
								activity.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										holder.pb.setVisibility(View.INVISIBLE);
										notifyDataSetChanged();
									}
								});

							}

							@Override
							public void onProgress(int progress, String status) {
							}

							@Override
							public void onError(int code, String message) {
								activity.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										holder.pb.setVisibility(View.INVISIBLE);
									}
								});

							}
						});

			} else {
				holder.pb.setVisibility(View.INVISIBLE);

			}
			return;
		}

		// until here, deal with send voice msg
		switch (message.status) {
		case SUCCESS:
			holder.pb.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.GONE);
			break;
		case FAIL:
			holder.pb.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.VISIBLE);
			break;
		case INPROGRESS:
			holder.pb.setVisibility(View.VISIBLE);
			holder.staus_iv.setVisibility(View.GONE);
			break;
		default:
			sendMsgInBackground(message, holder);
		}
	}

	/**
	 * 文件消息
	 * 
	 * @param message
	 * @param holder
	 * @param position
	 * @param convertView
	 */
	private void handleFileMessage(final EMMessage message,
			final ViewHolder holder, int position, View convertView) {
		final NormalFileMessageBody fileMessageBody = (NormalFileMessageBody) message
				.getBody();
		final String filePath = fileMessageBody.getLocalUrl();
		holder.tv_file_name.setText(fileMessageBody.getFileName());
		holder.tv_file_size.setText(TextFormater.getDataSize(fileMessageBody
				.getFileSize()));
		holder.ll_container.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				File file = new File(filePath);
				if (file != null && file.exists()) {
					// 文件存在，直接打开
					FileUtils.openFile(file, (Activity) context);
				} else {
					// 下载
					context.startActivity(new Intent(context,
							ShowNormalFileActivity.class).putExtra("msgbody",
							fileMessageBody));
				}
				if (message.direct == EMMessage.Direct.RECEIVE
						&& !message.isAcked) {
					try {
						EMChatManager.getInstance().ackMessageRead(
								message.getFrom(), message.getMsgId());
						message.isAcked = true;
					} catch (EaseMobException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		if (message.direct == EMMessage.Direct.RECEIVE) { // 接收的消息
			System.err.println("it is receive msg");
			File file = new File(filePath);
			if (file != null && file.exists()) {
				holder.tv_file_download_state.setText("已下载");
			} else {
				holder.tv_file_download_state.setText("未下载");
			}
			return;
		}

		// until here, deal with send voice msg
		switch (message.status) {
		case SUCCESS:
			holder.pb.setVisibility(View.INVISIBLE);
			holder.tv.setVisibility(View.INVISIBLE);
			holder.staus_iv.setVisibility(View.INVISIBLE);
			break;
		case FAIL:
			holder.pb.setVisibility(View.INVISIBLE);
			holder.tv.setVisibility(View.INVISIBLE);
			holder.staus_iv.setVisibility(View.VISIBLE);
			break;
		case INPROGRESS:
			if (timers.containsKey(message.getMsgId()))
				return;
			// set a timer
			final Timer timer = new Timer();
			timers.put(message.getMsgId(), timer);
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							holder.pb.setVisibility(View.VISIBLE);
							holder.tv.setVisibility(View.VISIBLE);
							holder.tv.setText(message.progress + "%");
							if (message.status == EMMessage.Status.SUCCESS) {
								holder.pb.setVisibility(View.INVISIBLE);
								holder.tv.setVisibility(View.INVISIBLE);
								timer.cancel();
							} else if (message.status == EMMessage.Status.FAIL) {
								holder.pb.setVisibility(View.INVISIBLE);
								holder.tv.setVisibility(View.INVISIBLE);
								holder.staus_iv.setVisibility(View.VISIBLE);
								Toast.makeText(
										activity,
										activity.getString(R.string.send_fail)
												+ activity
														.getString(R.string.connect_failuer_toast),
										0).show();
								timer.cancel();
							}

						}
					});

				}
			}, 0, 500);
			break;
		default:
			// 发送消息
			sendMsgInBackground(message, holder);
		}

	}

	/**
	 * 处理位置消息
	 * 
	 * @param message
	 * @param holder
	 * @param position
	 * @param convertView
	 */
	private void handleLocationMessage(final EMMessage message,
			final ViewHolder holder, final int position, View convertView) {
		TextView locationView = ((TextView) convertView
				.findViewById(R.id.tv_location));
		LocationMessageBody locBody = (LocationMessageBody) message.getBody();
		locationView.setText(locBody.getAddress());
		LatLng loc = new LatLng(locBody.getLatitude(), locBody.getLongitude());
		locationView.setOnClickListener(new MapClickListener(loc, locBody
				.getAddress()));
		locationView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				activity.startActivityForResult((new Intent(activity,
						ContextMenu.class)).putExtra("position", position)
						.putExtra("type", EMMessage.Type.LOCATION.ordinal()),
						ChatActivity.REQUEST_CODE_CONTEXT_MENU);
				return false;
			}
		});

		if (message.direct == EMMessage.Direct.RECEIVE) {
			return;
		}
		// deal with send message
		switch (message.status) {
		case SUCCESS:
			holder.pb.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.GONE);
			break;
		case FAIL:
			holder.pb.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.VISIBLE);
			break;
		case INPROGRESS:
			holder.pb.setVisibility(View.VISIBLE);
			break;
		default:
			sendMsgInBackground(message, holder);
		}
	}

	/**
	 * 发送消息
	 * 
	 * @param message
	 * @param holder
	 * @param position
	 */
	public void sendMsgInBackground(final EMMessage message,
			final ViewHolder holder) {
		holder.staus_iv.setVisibility(View.GONE);
		holder.pb.setVisibility(View.VISIBLE);

		final long start = System.currentTimeMillis();
		// 调用sdk发送异步发送方法
		EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

			@Override
			public void onSuccess() {
				// umeng自定义事件，
				sendEvent2Umeng(message, start);

				updateSendedView(message, holder);
			}

			@Override
			public void onError(int code, String error) {
				sendEvent2Umeng(message, start);

				updateSendedView(message, holder);
			}

			@Override
			public void onProgress(int progress, String status) {
			}

		});

	}

	/*
	 * chat sdk will automatic download thumbnail image for the image message we
	 * need to register callback show the download progress
	 */
	private void showDownloadImageProgress(final EMMessage message,
			final ViewHolder holder) {
		System.err.println("!!! show download image progress");
		// final ImageMessageBody msgbody = (ImageMessageBody)
		// message.getBody();
		final FileMessageBody msgbody = (FileMessageBody) message.getBody();
		if (holder.pb != null)
			holder.pb.setVisibility(View.VISIBLE);
		if (holder.tv != null)
			holder.tv.setVisibility(View.VISIBLE);

		msgbody.setDownloadCallback(new EMCallBack() {

			@Override
			public void onSuccess() {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// message.setBackReceive(false);
						if (message.getType() == EMMessage.Type.IMAGE) {
							holder.pb.setVisibility(View.GONE);
							holder.tv.setVisibility(View.GONE);
						}
						notifyDataSetChanged();
					}
				});
			}

			@Override
			public void onError(int code, String message) {

			}

			@Override
			public void onProgress(final int progress, String status) {
				if (message.getType() == EMMessage.Type.IMAGE) {
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							holder.tv.setText(progress + "%");

						}
					});
				}

			}

		});
	}

	/*
	 * send message with new sdk
	 */
	private void sendPictureMessage(final EMMessage message,
			final ViewHolder holder) {
		try {
			String to = message.getTo();

			// before send, update ui
			holder.staus_iv.setVisibility(View.GONE);
			holder.pb.setVisibility(View.VISIBLE);
			holder.tv.setVisibility(View.VISIBLE);
			holder.tv.setText("0%");

			final long start = System.currentTimeMillis();
			// if (chatType == ChatActivity.CHATTYPE_SINGLE) {
			EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

				@Override
				public void onSuccess() {
					Log.d(TAG, "send image message successfully");
					sendEvent2Umeng(message, start);
					activity.runOnUiThread(new Runnable() {
						public void run() {
							// send success
							holder.pb.setVisibility(View.GONE);
							holder.tv.setVisibility(View.GONE);
						}
					});
				}

				@Override
				public void onError(int code, String error) {
					sendEvent2Umeng(message, start);

					activity.runOnUiThread(new Runnable() {
						public void run() {
							holder.pb.setVisibility(View.GONE);
							holder.tv.setVisibility(View.GONE);
							// message.setSendingStatus(Message.SENDING_STATUS_FAIL);
							holder.staus_iv.setVisibility(View.VISIBLE);
							Toast.makeText(
									activity,
									activity.getString(R.string.send_fail)
											+ activity
													.getString(R.string.connect_failuer_toast),
									0).show();
						}
					});
				}

				@Override
				public void onProgress(final int progress, String status) {
					activity.runOnUiThread(new Runnable() {
						public void run() {
							holder.tv.setText(progress + "%");
						}
					});
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新ui上消息发送状态
	 * 
	 * @param message
	 * @param holder
	 */
	private void updateSendedView(final EMMessage message,
			final ViewHolder holder) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// send success
				if (message.getType() == EMMessage.Type.VIDEO) {
					holder.tv.setVisibility(View.GONE);
				}
				if (message.status == EMMessage.Status.SUCCESS) {
					// if (message.getType() == EMMessage.Type.FILE) {
					// holder.pb.setVisibility(View.INVISIBLE);
					// holder.staus_iv.setVisibility(View.INVISIBLE);
					// } else {
					// holder.pb.setVisibility(View.GONE);
					// holder.staus_iv.setVisibility(View.GONE);
					// }

				} else if (message.status == EMMessage.Status.FAIL) {
					// if (message.getType() == EMMessage.Type.FILE) {
					// holder.pb.setVisibility(View.INVISIBLE);
					// } else {
					// holder.pb.setVisibility(View.GONE);
					// }
					// holder.staus_iv.setVisibility(View.VISIBLE);
					Toast.makeText(
							activity,
							activity.getString(R.string.send_fail)
									+ activity
											.getString(R.string.connect_failuer_toast),
							0).show();
				}

				notifyDataSetChanged();
			}
		});
	}

	/**
	 * load image into image view
	 * 
	 * @param thumbernailPath
	 * @param iv
	 * @param position
	 * @return the image exists or not
	 */
	private boolean showImageView(final String thumbernailPath,
			final ImageView iv, final String localFullSizePath,
			String remoteDir, final EMMessage message) {
		// String imagename =
		// localFullSizePath.substring(localFullSizePath.lastIndexOf("/") + 1,
		// localFullSizePath.length());
		// final String remote = remoteDir != null ? remoteDir+imagename :
		// imagename;
		final String remote = remoteDir;
		EMLog.d("###", "local = " + localFullSizePath + " remote: " + remote);
		// first check if the thumbnail image already loaded into cache
		Bitmap bitmap = ImageCache.getInstance().get(thumbernailPath);
		if (bitmap != null) {
			// thumbnail image is already loaded, reuse the drawable
			iv.setImageBitmap(bitmap);
			iv.setClickable(true);
			iv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					System.err.println("image view on click");
					Intent intent = new Intent(activity, ShowBigImage.class);
					File file = new File(localFullSizePath);
					if (file.exists()) {
						Uri uri = Uri.fromFile(file);
						intent.putExtra("uri", uri);
						System.err
								.println("here need to check why download everytime");
					} else {
						// The local full size pic does not exist yet.
						// ShowBigImage needs to download it from the server
						// first
						// intent.putExtra("", message.get);
						ImageMessageBody body = (ImageMessageBody) message
								.getBody();
						intent.putExtra("secret", body.getSecret());
						intent.putExtra("remotepath", remote);
					}
					if (message != null
							&& message.direct == EMMessage.Direct.RECEIVE
							&& !message.isAcked
							&& message.getChatType() != ChatType.GroupChat) {
						try {
							EMChatManager.getInstance().ackMessageRead(
									message.getFrom(), message.getMsgId());
							message.isAcked = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					activity.startActivity(intent);
				}
			});
			return true;
		} else {

			new LoadImageTask().execute(thumbernailPath, localFullSizePath,
					remote, message.getChatType(), iv, activity, message);
			return true;
		}

	}

	/**
	 * 展示视频缩略图
	 * 
	 * @param localThumb
	 *            本地缩略图路径
	 * @param iv
	 * @param thumbnailUrl
	 *            远程缩略图路径
	 * @param message
	 */
	private void showVideoThumbView(String localThumb, ImageView iv,
			String thumbnailUrl, final EMMessage message) {
		// first check if the thumbnail image already loaded into cache
		Bitmap bitmap = ImageCache.getInstance().get(localThumb);
		if (bitmap != null) {
			// thumbnail image is already loaded, reuse the drawable
			iv.setImageBitmap(bitmap);
			iv.setClickable(true);
			iv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					VideoMessageBody videoBody = (VideoMessageBody) message
							.getBody();
					System.err.println("video view is on click");
					Intent intent = new Intent(activity,
							ShowVideoActivity.class);
					intent.putExtra("localpath", videoBody.getLocalUrl());
					intent.putExtra("secret", videoBody.getSecret());
					intent.putExtra("remotepath", videoBody.getRemoteUrl());
					if (message != null
							&& message.direct == EMMessage.Direct.RECEIVE
							&& !message.isAcked
							&& message.getChatType() != ChatType.GroupChat) {
						message.isAcked = true;
						try {
							EMChatManager.getInstance().ackMessageRead(
									message.getFrom(), message.getMsgId());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					activity.startActivity(intent);

				}
			});

		} else {
			new LoadVideoImageTask().execute(localThumb, thumbnailUrl, iv,
					activity, message, this);
		}

	}

	public static class ViewHolder {
		ImageView iv;
		TextView tv;
		ProgressBar pb;
		ImageView staus_iv;
		ImageView head_iv;
		TextView tv_userId;
		ImageView playBtn;
		TextView timeLength;
		TextView size;
		LinearLayout container_status_btn;
		LinearLayout ll_container;
		ImageView iv_read_status;
		// 显示已读回执状态
		TextView tv_ack;
		// 显示送达回执状态
		TextView tv_delivered;

		TextView tv_file_name;
		TextView tv_file_size;
		TextView tv_file_download_state;
		// 拓展消息体
		RelativeLayout zidingyi_chat_layout;
		TextView tv_job_place;// 工作地点
		TextView tv_job_time;// 工作时间
		TextView tv_job_zhaomurenshu;// 招募人数
	}

	/*
	 * 点击地图消息listener
	 */
	class MapClickListener implements View.OnClickListener {

		LatLng location;
		String address;

		public MapClickListener(LatLng loc, String address) {
			location = loc;
			this.address = address;

		}

		@Override
		public void onClick(View v) {
			Intent intent;
			intent = new Intent(context, BaiduMapActivity.class);
			intent.putExtra("latitude", location.latitude);
			intent.putExtra("longitude", location.longitude);
			intent.putExtra("address", address);
			activity.startActivity(intent);
		}
	}

	/**
	 * umeng自定义事件统计
	 * 
	 * @param message
	 */
	private void sendEvent2Umeng(final EMMessage message, final long start) {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				long costTime = System.currentTimeMillis() - start;
				Map<String, String> params = new HashMap<String, String>();
				if (message.status == EMMessage.Status.SUCCESS)
					params.put("status", "success");
				else
					params.put("status", "failure");

				switch (message.getType()) {
				case TXT:
				case LOCATION:
					MobclickAgent.onEventValue(activity, "text_msg", params,
							(int) costTime);
					MobclickAgent.onEventDuration(activity, "text_msg",
							(int) costTime);
					break;
				case IMAGE:
					MobclickAgent.onEventValue(activity, "img_msg", params,
							(int) costTime);
					MobclickAgent.onEventDuration(activity, "img_msg",
							(int) costTime);
					break;
				case VOICE:
					MobclickAgent.onEventValue(activity, "voice_msg", params,
							(int) costTime);
					MobclickAgent.onEventDuration(activity, "voice_msg",
							(int) costTime);
					break;
				case VIDEO:
					MobclickAgent.onEventValue(activity, "video_msg", params,
							(int) costTime);
					MobclickAgent.onEventDuration(activity, "video_msg",
							(int) costTime);
					break;
				default:
					break;
				}

			}
		});
	}

	// /*************************carson add on
	// 4-9-19:47****************************************
	/**
	 * 加载本地头像和名字
	 */
	private void loadNativePhoto(final String id, final ImageView avatar,
			final TextView name) {
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
				// getNick2(id);// 更新本地数据
			} else {
				avatar.setImageResource(R.drawable.default_avatar);
				getNick(id, avatar, name);
			}
		} else {
			avatar.setImageResource(R.drawable.default_avatar);
			getNick(id, avatar, name);
		}

	}

	// ====================获取网络信息存储到本地======================
	public void getNick2(final String id) {
		StringRequest request = new StringRequest(Request.Method.POST,
				Url.HUANXIN_avatars_pic, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject js = new JSONObject(response);
							JSONArray jss = js.getJSONArray("avatars");
							HuanxinUser us = (HuanxinUser) JsonUtil.jsonToBean(
									jss.getJSONObject(0), HuanxinUser.class);
							if ((us.getAvatar() != null)
									&& (!us.getAvatar().equals(""))) {
								Editor edt = sp.edit();
								edt.putString(id + "_photo", us.getAvatar());
								edt.putString(id + "realname", us.getName());
								edt.commit();
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
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
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	// ================================================
	public void getNick(final String id, final ImageView avatar,
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
							if (name != null) {
								name.setText(us.getName());
								if (us.getName() != null
										&& !"".equals(us.getName())) {
									Editor edt = sp.edit();
									edt.putString(id + "realname", us.getName());
									edt.commit();
								}
							}
							if ((us.getAvatar() != null)
									&& (!us.getAvatar().equals(""))) {
								loadpersonPic(id, us.getAvatar(), avatar, 1);

							} else {
								if (us.getUid().startsWith("u")) {
									avatar.setImageResource(R.drawable.default_avatar);
								} else {
									avatar.setImageResource(R.drawable.default_avatar_business);
								}
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						// Toast.makeText(context, "你的网络不够给力，获取数据失败！",0).show();
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
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	/**
	 * @Description: 加载图片
	 * @author howe
	 * @date 2014-7-30 下午5:57:52
	 * 
	 */
	public void loadpersonPic(final String id, final String url,
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
	// ================howe end==================
}