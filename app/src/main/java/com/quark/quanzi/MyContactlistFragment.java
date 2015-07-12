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
package com.quark.quanzi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.easemob.chat.EMContactManager;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.activity.AddContactActivity;
import com.parttime.IM.ChatActivity;
import com.easemob.chatuidemo.activity.GroupsActivity;
import com.easemob.chatuidemo.activity.NewFriendsMsgActivity;
import com.easemob.chatuidemo.adapter.ContactAdapter;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.widget.Sidebar;
import com.easemob.exceptions.EaseMobException;
import com.qingmu.jianzhidaren.R;
import com.quark.citylistview.CharacterParser;
import com.quark.common.JsonUtil;
import com.quark.common.Url;
import com.quark.jianzhidaren.ApplicationControl;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.model.HuanxinUser;

/**
 * 联系人列表页
 * 
 */
/**
 * 
 * @ClassName: MyContactlistFragment
 * @Description: TODO 获取昵称XXXX
 * @author howe
 * @date 2015-2-5 下午8:48:58
 * 
 */
public class MyContactlistFragment extends BaseActivity {
	private ContactAdapter adapter;
	private List<User> contactList;
	private List<String> contactIds;
	private String contactIdsStr = "";
	private ListView listView;
	private boolean hidden;
	private Sidebar sidebar;
	private InputMethodManager inputMethodManager;
	private List<String> blackList;
	ArrayList<HuanxinUser> usersNick = new ArrayList<HuanxinUser>();
	// =========转拼音========
	private CharacterParser characterParser;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator_quanzhi pinyinComparator;
	private PinyinComparator_quanzhitwo pinyinComparatorTwo;

	// =======转拼音========
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_contact_list);
		// 实例化汉字转拼音类 加
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator_quanzhi();
		pinyinComparatorTwo = new PinyinComparator_quanzhitwo();
		// 增加返回键 加
		ImageView left = (ImageView) findViewById(R.id.left);
		left.setOnClickListener(backOnclick);

		// 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
		if (savedInstanceState != null
				&& savedInstanceState.getBoolean("isConflict", false))
			return;
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		listView = (ListView) findViewById(R.id.list);
		sidebar = (Sidebar) findViewById(R.id.sidebar);
		sidebar.setListView(listView);
		// 黑名单列表
		blackList = EMContactManager.getInstance().getBlackListUsernames();
		contactList = new ArrayList<User>();
		contactIds = new ArrayList<String>();

		// dealdd();
		// setlist方法

		ImageView addContactView = (ImageView) findViewById(R.id.iv_new_contact);
		// 进入添加好友页
		addContactView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MyContactlistFragment.this,
						AddContactActivity.class));
			}
		});
		registerForContextMenu(listView);
	}

	public void dealdd() {
		usersNick.clear();
		// 获取设置contactlist
		// 获取头像及昵称
		getContactList();
		if (contactIds.size() > 0) {
			contactIdsStr = "{";
			for (int i = 0; i < contactIds.size(); i++) {
				contactIdsStr += contactIds.get(i) + "、";
			}
			contactIdsStr = contactIdsStr.substring(0,
					contactIdsStr.length() - 1) + "}";

			if (contactIdsStr.equals("{}")) {
				filledData();
			} else {
				getNick();
			}
		} else {
			filledData();
		}
	}

	OnClickListener backOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// 长按前两个不弹menu
		if (((AdapterContextMenuInfo) menuInfo).position >= 2) {
			MyContactlistFragment.this.getMenuInflater().inflate(
					R.menu.context_contact_list, menu);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete_contact) {
			User tobeDeleteUser = adapter
					.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			// 删除此联系人
			deleteContact(tobeDeleteUser);
			// 删除相关的邀请消息
			InviteMessgeDao dao = new InviteMessgeDao(
					MyContactlistFragment.this);
			dao.deleteMessage(tobeDeleteUser.getUsername());
			return true;
		} else if (item.getItemId() == R.id.add_to_blacklist) {
			User user = adapter.getItem(((AdapterContextMenuInfo) item
					.getMenuInfo()).position);
			moveToBlacklist(user.getUsername());
			return true;
		}
		return super.onContextItemSelected(item);
	}

	// @Override
	// public void onHiddenChanged(boolean hidden) {
	// super.onHiddenChanged(hidden);
	// this.hidden = hidden;
	// if (!hidden) {
	// refresh();
	// }
	// }

	@Override
	public void onResume() {
		super.onResume();
		if (!hidden) {
			dealdd();
		}
	}

	/**
	 * 删除联系人
	 * 
	 * @param toDeleteUser
	 */
	public void deleteContact(final User tobeDeleteUser) {
		final ProgressDialog pd = new ProgressDialog(MyContactlistFragment.this);
		pd.setMessage("正在删除...");
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					EMContactManager.getInstance().deleteContact(
							tobeDeleteUser.getUsername());
					// 删除db和内存中此用户的数据
					UserDao dao = new UserDao(MyContactlistFragment.this);
					dao.deleteContact(tobeDeleteUser.getUsername());
					ApplicationControl.getInstance().getContactList()
							.remove(tobeDeleteUser.getUsername());
					MyContactlistFragment.this.runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							adapter.remove(tobeDeleteUser);
							adapter.notifyDataSetChanged();
							dealdd();// 长按删除联系人要刷新当前页面

						}
					});
				} catch (final Exception e) {
					// getActivity().runOnUiThread(new Runnable() {
					// public void run() {
					// pd.dismiss();
					// Toast.makeText(MyContactlistFragment.this, "删除失败: " +
					// e.getMessage(), 1).show();
					// }
					// });
				}
			}
		}).start();
	}

	/**
	 * 把user移入到黑名单
	 */
	private void moveToBlacklist(final String username) {
		final ProgressDialog pd = new ProgressDialog(MyContactlistFragment.this);
		pd.setMessage("正在移入黑名单...");
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					// 加入到黑名单
					EMContactManager.getInstance().addUserToBlackList(username,
							false);
					MyContactlistFragment.this.runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							showToast("移入黑名单成功");
							refresh();
						}
					});
				} catch (EaseMobException e) {
					e.printStackTrace();
					MyContactlistFragment.this.runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							showToast("移入黑名单失败");
						}
					});
				}
			}
		}).start();
	}

	// 刷新ui
	public void refresh() {
		try {
			// 可能会在子线程中调到这方法
			MyContactlistFragment.this.runOnUiThread(new Runnable() {
				public void run() {
					getContactList();
					adapter.notifyDataSetChanged();

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取联系人列表，并过滤掉黑名单和排序
	 */

	private void getContactList() {
		contactList.clear();
		contactIds.clear();
		// 获取本地好友列表
		Map<String, User> users = ApplicationControl.getInstance()
				.getContactList();
		Iterator<Entry<String, User>> iterator = users.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, User> entry = iterator.next();
			if (!entry.getKey().equals(Constant.NEW_FRIENDS_USERNAME)
					&& !entry.getKey().equals(Constant.GROUP_USERNAME)
					&& !blackList.contains(entry.getKey())) {
				// 这里有bug，会有好友列表有uid,没有名字的情况
				if (!entry.getKey().equals("jianzhidaren")) {
					// userName ==nick 都是u661或者c221之类的
					// head 是u或者c
					contactList.add(entry.getValue());
				}
			}
		}
		// 排序
		// Collections.sort(contactList, new Comparator<User>() {
		//
		// @Override
		// public int compare(User lhs, User rhs) {
		// return lhs.getRealName().compareTo(rhs.getRealName());
		// }
		// });

		for (int i = 0; i < contactList.size(); i++) {
			contactIds.add(contactList.get(i).getUsername());
		}

	}

	// @Override
	// public void onSaveInstanceState(Bundle outState) {
	// super.onSaveInstanceState(outState);
	// if(MainActivity.isConflict){
	// outState.putBoolean("isConflict", true);
	// }else if(((MainActivity)getActivity()).getCurrentAccountRemoved()){
	// outState.putBoolean(Constant.ACCOUNT_REMOVED, true);
	// }

	// }
	// ====================================howe=========================

	public void getNick() {
		StringRequest request = new StringRequest(Request.Method.POST,
				Url.HUANXIN_avatars_pic, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject js = new JSONObject(response);
							JSONArray jss = js.getJSONArray("avatars");
							for (int i = 0; i < jss.length(); i++) {
								HuanxinUser us = (HuanxinUser) JsonUtil
										.jsonToBean(jss.getJSONObject(i),
												HuanxinUser.class);
								if (us.getName() == null
										|| us.getName().equals("")) {
									us.setName("未知好友");
								}
								usersNick.add(us);

							}
							filledData(); // 转化拼音

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						// showWait(false);
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {

				Map<String, String> map = new HashMap<String, String>();
				map.put("user_ids", contactIdsStr);
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(ConstantForSaveList.DEFAULTRETRYTIME*1000, 1, 1.0f));

	}

	public void setlist() {
		// 设置adapter
		adapter = new ContactAdapter(this, R.layout.row_contact, contactList,
				sidebar, usersNick);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String username = adapter.getItem(position).getUsername();
				if (Constant.NEW_FRIENDS_USERNAME.equals(username)) {
					// 进入申请与通知页面
					User user = ApplicationControl.getInstance()
							.getContactList()
							.get(Constant.NEW_FRIENDS_USERNAME);
					user.setUnreadMsgCount(0);
					startActivity(new Intent(MyContactlistFragment.this,
							NewFriendsMsgActivity.class));
				} else if (Constant.GROUP_USERNAME.equals(username)) {
					// 进入群聊列表页面
					startActivity(new Intent(MyContactlistFragment.this,
							GroupsActivity.class));
				} else {
					// 中直接进入聊天页面，实际一般是进入用户详情页
					startActivity(new Intent(MyContactlistFragment.this,
							ChatActivity.class).putExtra("userId", adapter
							.getItem(position).getUsername()));
				}
			}
		});

		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 隐藏软键盘
				if (MyContactlistFragment.this.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (MyContactlistFragment.this.getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(
								MyContactlistFragment.this.getCurrentFocus()
										.getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});
	}

	/**
	 * 转化拼音
	 * 
	 * @param date
	 * @return
	 */
	private void filledData() {

		for (int i = 0; i < usersNick.size(); i++) {

			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(usersNick.get(i)
					.getName());
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				usersNick.get(i).setNamePinyin(sortString.toUpperCase());
				contactList.get(i).setHeader(sortString.toUpperCase());
			} else {
				usersNick.get(i).setNamePinyin("#");
				contactList.get(i).setHeader("#");
			}
		}
		Collections.sort(usersNick, pinyinComparator);
		Collections.sort(contactList, pinyinComparatorTwo);

		// 列表中头两条是群和通知 所以从联系人进来的 usersNick真实数据应该从第三条开始（开始两条为空）
		HuanxinUser nullhx = new HuanxinUser();
		nullhx.setNamePinyin("aaa");
		nullhx.setName("aaa");
		usersNick.add(0, nullhx);
		usersNick.add(0, nullhx);

		Map<String, User> users = ApplicationControl.getInstance()
				.getContactList();
		// 加入"申请与通知"和"群聊"
		contactList.add(0, users.get(Constant.GROUP_USERNAME));
		// 把"申请与通知"添加到首位
		contactList.add(0, users.get(Constant.NEW_FRIENDS_USERNAME));
		setlist();
		// refresh();
	}
}
