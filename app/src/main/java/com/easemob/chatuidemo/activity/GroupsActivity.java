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
package com.easemob.chatuidemo.activity;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.adapter.GroupAdapter;
import com.easemob.exceptions.EaseMobException;
import com.qingmu.jianzhidaren.R;
import com.quark.common.ToastUtil;

/**
 * 
 * @ClassName: GroupsActivity
 * @Description: 群列表
 * @author howe
 * @date 2015-2-12 上午10:43:04
 * 
 */
public class GroupsActivity extends BaseActivity {
	private ListView groupListView;
	protected List<EMGroup> grouplist;
	private GroupAdapter groupAdapter;
	private InputMethodManager inputMethodManager;
	public static GroupsActivity instance;
	boolean isFromShare;// 是否是从分享跳转过来的
	private String activityId, activityTitle, job_place, startTime;
	private int pay, pay_type, leftcount;

	private EMConversation conversation;// share分享的时候添加会话记录
	private RelativeLayout topLayout;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_groups);
		topLayout = (RelativeLayout) findViewById(R.id.title);
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
			topLayout.setBackgroundColor(getResources().getColor(
					R.color.guanli_common_color));
		isFromShare = getIntent().getExtras().getBoolean("isFromShare", false);
		if (isFromShare) {
			activityId = getIntent().getExtras().getString("activityId");
			activityTitle = getIntent().getExtras().getString("title");
			job_place = getIntent().getExtras().getString("job_place");
			startTime = getIntent().getExtras().getString("start_time");
			pay = getIntent().getExtras().getInt("pay");
			pay_type = getIntent().getExtras().getInt("pay_type");
			leftcount = getIntent().getExtras().getInt("left_count");
		}
		instance = this;
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		grouplist = EMGroupManager.getInstance().getAllGroups();
		groupListView = (ListView) findViewById(R.id.list);
		groupAdapter = new GroupAdapter(this, 1, grouplist);
		groupListView.setAdapter(groupAdapter);
		groupListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (position == groupAdapter.getCount() - 1) {
					if (!isFromShare)
						// 新建群聊
						startActivityForResult(new Intent(GroupsActivity.this,
								NewGroupActivity.class), 0);
				} else {
					if (!isFromShare) {
						// 进入群聊
						Intent intent = new Intent(GroupsActivity.this,
								ChatActivity.class);
						// it is group chat
						intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
						intent.putExtra("groupId",
								groupAdapter.getItem(position).getGroupId());
						startActivityForResult(intent, 0);
					} else {
						conversation = EMChatManager.getInstance()
								.getConversation(
										groupAdapter.getItem(position)
												.getGroupId());
						EMMessage message = EMMessage
								.createSendMessage(EMMessage.Type.TXT);
						// 如果是群聊，设置chattype,默认是单聊
						message.setChatType(ChatType.GroupChat);
						TextMessageBody txtBody = new TextMessageBody(
								activityTitle);
						// 设置消息body
						message.addBody(txtBody);
						// 增加自定义的拓展消息属性
						message.setAttribute("activityExtra", "1");
						message.setAttribute("activityId", activityId);
						message.setAttribute("activityTitle", activityTitle);
						String temp = null;
						// 日薪(0),时薪(1)
						if (pay_type == 0) {
							temp = "元/天";
						} else if (pay_type == 1) {
							temp = "元/小时";
						}
						message.setAttribute("activityXinZi", pay + temp);

						message.setAttribute("activityJobPlace", job_place);

						message.setAttribute("activityStartTime", startTime);
						message.setAttribute("leftCount",
								String.valueOf(leftcount));
						// 设置要发给谁,用户username或者群聊groupid
						message.setReceipt(groupAdapter.getItem(position)
								.getGroupId());
						// 把messgage加到conversation中
						conversation.addMessage(message);
						try {
							if (EMChatManager.getInstance() != null) {
								EMChatManager.getInstance()
										.sendMessage(message);
								ToastUtil.showShortToast("活动分享成功^_^");
								GroupsActivity.this.finish();
							} else {
								ToastUtil.showShortToast("当前网络状态太差,请稍后再试^_^");
								GroupsActivity.this.finish();
							}
						} catch (EaseMobException e) {
							e.printStackTrace();
							ToastUtil.showShortToast("当前网络状态太差,请稍后再试^_^");
							GroupsActivity.this.finish();
						}
						//
						setResult(RESULT_OK);

						// 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
						// adapter.refresh();
						// listView.setSelection(listView.getCount() - 1);
						// mEditTextContent.setText("");
						// setResult(RESULT_OK);

					}
				}
			}

		});
		groupListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(
								getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});
	}

	/**
	 * 进入公开群聊列表
	 */
	public void onPublicGroups(View view) {
		startActivity(new Intent(this, PublicGroupsActivity.class));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResume() {
		super.onResume();
		grouplist = EMGroupManager.getInstance().getAllGroups();
		groupAdapter = new GroupAdapter(this, 1, grouplist);
		groupListView.setAdapter(groupAdapter);
		groupAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		instance = null;
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back(View view) {
		finish();
	}
}
