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

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupInfo;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.ui.widget.EditDialog;
import com.quark.volley.VolleySington;

/**
 * 
 * @ClassName: GroupSimpleDetailActivity
 * @Description: 群资料 需要把id为创建人的名字
 * @author howe
 * @date 2015-2-28 下午4:59:36
 * 
 */
public class GroupSimpleDetailActivity extends BaseActivity {
	private Button btn_add_group;
	private TextView tv_admin;
	private TextView tv_name;
	private TextView tv_introduction;
	private EMGroup group;
	private String groupid;
	private ProgressBar progressBar;
	RequestQueue queue = VolleySington.getInstance().getRequestQueue();
	private RelativeLayout topLayout;
	private SharedPreferences sp;
	ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_simle_details);
		pd = new ProgressDialog(GroupSimpleDetailActivity.this);
		pd.setMessage("正在发送请求...");
		pd.setCanceledOnTouchOutside(false);
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		topLayout = (RelativeLayout) findViewById(R.id.top_title_layout);
		topLayout.setBackgroundColor(getResources().getColor(
				R.color.guanli_common_color));
		tv_name = (TextView) findViewById(R.id.name);
		tv_admin = (TextView) findViewById(R.id.tv_admin);
		btn_add_group = (Button) findViewById(R.id.btn_add_to_group);
		tv_introduction = (TextView) findViewById(R.id.tv_introduction);
		progressBar = (ProgressBar) findViewById(R.id.loading);

		EMGroupInfo groupInfo = (EMGroupInfo) getIntent().getSerializableExtra(
				"groupinfo");
		String groupname = groupInfo.getGroupName();
		groupid = groupInfo.getGroupId();

		tv_name.setText(groupname);

		new Thread(new Runnable() {

			public void run() {
				// 从服务器获取详情
				try {
					group = EMGroupManager.getInstance().getGroupFromServer(
							groupid);
					runOnUiThread(new Runnable() {
						public void run() {
							progressBar.setVisibility(View.INVISIBLE);
							// 获取详情成功，并且自己不在群中，才让加入群聊按钮可点击
							if (!group.getMembers().contains(
									EMChatManager.getInstance()
											.getCurrentUser()))
								btn_add_group.setEnabled(true);
							tv_name.setText(group.getGroupName());
							// 将id改为创建者的名字

							// tv_admin.setText(group.getOwner());
							getNick(group.getOwner(), tv_admin);
							tv_introduction.setText(group.getDescription());
						}
					});
				} catch (final EaseMobException e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						public void run() {
							progressBar.setVisibility(View.INVISIBLE);
							Toast.makeText(GroupSimpleDetailActivity.this,
									"获取群聊信息失败: " + e.getMessage(), 1).show();
						}
					});
				}

			}
		}).start();

	}

	// 加入群聊
	public void addToGroup(View view) {
		new Thread(new Runnable() {
			public void run() {
				try {
					// 如果是membersOnly的群，需要申请加入，不能直接join
					if (group.isMembersOnly()) {
						runOnUiThread(new Runnable() {
							public void run() {
								showAlertDialog("留言", "留言", groupid);
							}
						});

					} else {
						EMGroupManager.getInstance().joinGroup(groupid);
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(GroupSimpleDetailActivity.this,
										"加入群聊成功", 0).show();
								btn_add_group.setEnabled(false);
							}
						});

					}
				} catch (final EaseMobException e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(GroupSimpleDetailActivity.this,
									"加入群聊失败：" + e.getMessage(), 0).show();
						}
					});
				}
			}
		}).start();
	}

	public void back(View view) {
		finish();
	}

	public void getNick(final String id, final TextView name) {
		StringRequest request = new StringRequest(Request.Method.POST,
				Url.HUANXIN_user_info, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss = js.getJSONObject("info");
							// if(jss!=null){
							// HuanxinUser us =
							// (HuanxinUser)JsonUtil.jsonToBean(jss.getJSONObject(0),
							// HuanxinUser.class);
							// if(name!=null){
							name.setText(jss.getString("name"));
							// }
							// }
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						Toast.makeText(GroupSimpleDetailActivity.this,
								"你的网络不够给力，获取数据失败！", 0).show();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", id);

				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	/**
	 * 申请入群发送原因
	 * 
	 */
	public void showAlertDialog(String str, final String str2,
			final String groupId) {
		final EditDialog.Builder builder = new EditDialog.Builder(
				GroupSimpleDetailActivity.this);
		builder.setMessage(str);
		builder.setTitle(str2);
		builder.setPositiveButton("确 定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				final String content = builder.getContent();
				dialog.dismiss();
				new Thread(new Runnable() {
					public void run() {
						try {
							String sendstr = "";
							if (content.isEmpty()) {
								sendstr = "求加入";
							} else {
								sendstr = content;
							}
							EMGroupManager.getInstance().applyJoinToGroup(
									groupId, sendstr);
							runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(
											GroupSimpleDetailActivity.this,
											"发送请求成功,等待群主验证", 1).show();
									btn_add_group.setEnabled(false);
								}
							});
						} catch (final Exception e) {
							runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(
											GroupSimpleDetailActivity.this,
											"请求入群失败:" + e.getMessage(), 1)
											.show();
								}
							});
						}
					}
				}).start();

			}
		});
		builder.create().show();
	}
}
