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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.carson.constant.ConstantForSaveList;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.activity.BaseActivity;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.widget.Sidebar;
import com.parttime.main.PinyinComparator;
import com.parttime.main.PinyinComparatorByHeader;
import com.parttime.net.DefaultCallback;
import com.parttime.net.HuanXinRequest;
import com.qingmu.jianzhidaren.R;
import com.quark.citylistview.CharacterParser;
import com.quark.jianzhidaren.ApplicationControl;
import com.quark.model.HuanxinUser;
import com.quark.utils.NetWorkCheck;
import com.quark.utils.WaitDialog;
import com.quark.volley.VolleySington;

/**
 * 
 * @ClassName: GroupPickContactsActivity
 * @Description: PickContactAdapter继承进行修改 创建群 选择联系人
 * @author howe
 * @date 2015-2-5 下午8:19:20
 * 
 */
public class GroupPickContactsActivity extends BaseActivity {
	private ListView listView;
	/** 是否为一个新建的群组 */
	protected boolean isCreatingNewGroup;
	/** 是否为单选 */
	private boolean isSignleChecked;
	private PickContactAdapter contactAdapter;
	/** group中一开始就有的成员 */
	private List<String> exitingMembers;
	protected WaitDialog dialog;
	RequestQueue queue = VolleySington.getInstance().getRequestQueue();
	ArrayList<HuanxinUser> usersNick = new ArrayList<>();
	ArrayList<String> contactIds;
	private StringBuilder contactIdsStr = new StringBuilder();
	List<User> alluserList;
	// =========转拼音========
	private CharacterParser characterParser;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	private PinyinComparatorByHeader pinyinComparatorTwo;

	// =======转拼音========
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_pick_contacts);
		// 实例化汉字转拼音类 加
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		pinyinComparatorTwo = new PinyinComparatorByHeader();

		// String groupName = getIntent().getStringExtra("groupName");
		String groupId = getIntent().getStringExtra("groupId");
		if (groupId == null) {// 创建群组
			isCreatingNewGroup = true;
		} else {
			// 获取此群组的成员列表
			EMGroup group = EMGroupManager.getInstance().getGroup(groupId);
			exitingMembers = group.getMembers();
		}
		if (exitingMembers == null)
			exitingMembers = new ArrayList<>();
		// 获取好友列表
		alluserList = new ArrayList<>();
		for (User user : ApplicationControl.getInstance().getContactList()
				.values()) {
			if (!user.getUsername().equals(Constant.NEW_FRIENDS_USERNAME)
					&& !user.getUsername().equals(Constant.GROUP_USERNAME)
                    && !user.getUsername().equals(Constant.PUBLIC_COUNT)) {
				if (!user.getUsername().equals("jianzhidaren")) {
					alluserList.add(user);
				}
			}
		}
		// 对list进行排序
		// Collections.sort(alluserList, new Comparator<User>() {
		// @Override
		// public int compare(User lhs, User rhs) {
		// return (lhs.getUsername().compareTo(rhs.getUsername()));
		//
		// }
		// });

		contactIds = new ArrayList<>();
		for (int i = 0; i < alluserList.size(); i++) {
			contactIds.add(alluserList.get(i).getUsername());
		}

		// 获取设置contactlist howe
		// 获取头像及昵称
		if (contactIds.size() > 0) {
            int size = contactIds.size();
            for (int i = 0; i < size; i++) {
                if(i < size -1 ) {
                    contactIdsStr.append(contactIds.get(i)).append(",");
                }else{
                    contactIdsStr.append(contactIds.get(i));
                }
            }

            if ("".equals(contactIdsStr)) {
				filledData();
			} else {
				getNick();
			}
		} else {
			filledData(); // 转化拼音
		}
	}

	/**
	 * 确认选择的members
	 * 
	 * @param v
	 */
	public void save(View v) {
		if (NetWorkCheck.isOpenNetwork(GroupPickContactsActivity.this)) {
			setResult(RESULT_OK, new Intent().putExtra("newmembers",
					getToBeAddMembers().toArray(new String[0])));
			finish();
		} else {
			Toast.makeText(getApplicationContext(), "请检查网络连接设置", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	/**
	 * 获取要被添加的成员
	 * 
	 * @return
	 */
	private List<String> getToBeAddMembers() {
		List<String> members = new ArrayList<String>();
		if (contactAdapter == null) {
			return members;
		}
		if (contactAdapter.getCount() == 1)
			return members;
		int length = contactAdapter.isCheckedArray.length;
		for (int i = 0; i < length; i++) {
			String username = contactAdapter.getItem(i).getUsername();
			if (contactAdapter.isCheckedArray[i]
					&& !exitingMembers.contains(username)) {
				members.add(username);
			}
		}

		return members;
	}

	/**
	 * adapter
	 */
	private class PickContactAdapter extends ContactAdapter {

		private boolean[] isCheckedArray;

		/**
		 * 修改
		 * 
		 * @param context
		 * @param resource
		 * @param users
		 */
		public PickContactAdapter(Context context, int resource,
				List<User> users, ArrayList<HuanxinUser> usersNick) {
			super(context, resource, users, null, usersNick);
			isCheckedArray = new boolean[users.size()];
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			// if (position > 0) {
			final String username = getItem(position).getUsername();
			// 选择框checkbox
			final CheckBox checkBox = (CheckBox) view
					.findViewById(R.id.checkbox);
			if (exitingMembers != null && exitingMembers.contains(username)) {
				checkBox.setButtonDrawable(R.drawable.checkbox_bg_gray_selector);
			} else {
				checkBox.setButtonDrawable(R.drawable.checkbox_bg_selector);
			}
			if (checkBox != null) {
				// checkBox.setOnCheckedChangeListener(null);

				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// 群组中原来的成员一直设为选中状态
						if (exitingMembers.contains(username)) {
							isChecked = true;
							checkBox.setChecked(true);
						}
						isCheckedArray[position] = isChecked;
						// 如果是单选模式
						if (isSignleChecked && isChecked) {
							for (int i = 0; i < isCheckedArray.length; i++) {
								if (i != position) {
									isCheckedArray[i] = false;
								}
							}
							contactAdapter.notifyDataSetChanged();
						}
					}
				});
				// 群组中原来的成员一直设为选中状态
				if (exitingMembers.contains(username)) {
					checkBox.setChecked(true);
					isCheckedArray[position] = true;
				} else {
					checkBox.setChecked(isCheckedArray[position]);
				}
			}
			// }
			return view;
		}
	}

	public void back(View view) {
		finish();
	}

	// 获取查找结果
	// ====================================howe=========================
	// adapter 抽取
	public void setlist() {
		listView = (ListView) findViewById(R.id.list);
		contactAdapter = new PickContactAdapter(this,
				R.layout.row_contact_with_checkbox, alluserList, usersNick);
		listView.setAdapter(contactAdapter);

		((Sidebar) findViewById(R.id.sidebar)).setListView(listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
				checkBox.toggle();

			}
		});
	}

	public void getNick() {
		showWait(true);
        new HuanXinRequest().getHuanxinUserList(contactIdsStr.toString(), queue, new DefaultCallback(){
            @Override
            public void success(Object obj) {
                super.success(obj);
                showWait(false);
                if(obj instanceof ArrayList){
                    @SuppressLint("Unchecked")
                    ArrayList<HuanxinUser> list = (ArrayList<HuanxinUser>)obj;
                    usersNick.clear();
                    usersNick.addAll(list);
                    if (usersNick.size() > 0) {
                        ConstantForSaveList.usersNick = usersNick;// 保存缓存

                    }
                    filledData(); // 转化拼音
                }
            }

            @Override
            public void failed(Object obj) {
                super.failed(obj);
                showWait(false);
                Toast.makeText(GroupPickContactsActivity.this,
                        "你的网络不够给力，获取数据失败！", Toast.LENGTH_SHORT).show();
            }
        });

	}

	protected void showWait(boolean isShow) {
		if (isShow) {
			if (null == dialog) {
				dialog = new WaitDialog(this);
			}
			dialog.show();
		} else {
			if (null != dialog) {
				dialog.dismiss();
			}
		}
	}

	/**
	 * 转化拼音
	 * 
	 * @return
	 */
	private void filledData() {

		for (int i = 0; i < usersNick.size(); i++) {
			String temp;
			if (usersNick.get(i).getName() == null
					|| "".equals(usersNick.get(i).getName())) {
				temp = "未知好友";
			} else {
				temp = usersNick.get(i).getName();
			}
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(temp);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				usersNick.get(i).setNamePinyin(sortString.toUpperCase());
				alluserList.get(i).setHeader(sortString.toUpperCase());
			} else {
				usersNick.get(i).setNamePinyin("#");
				alluserList.get(i).setHeader("#");
			}
		}
		Collections.sort(usersNick, pinyinComparator);
		Collections.sort(alluserList, pinyinComparatorTwo);

		// 列表中头两条是群和通知 所以从联系人进来的 usersNick真实数据应该从第三条开始（开始两条为空）
		// HuanxinUser nullhx = new HuanxinUser();
		// nullhx.setName("aaa");
		// usersNick.add(0,nullhx);

		Map<String, User> users = ApplicationControl.getInstance()
				.getContactList();
		setlist();
		// refresh();
	}
}
