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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.carson.constant.ConstantForSaveList;
import com.easemob.chatuidemo.Constant;
import com.parttime.addresslist.ContactAdapter;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.widget.Sidebar;
import com.parttime.constants.ApplicationConstants;
import com.parttime.main.PinyinComparator;
import com.parttime.main.PinyinComparatorByHeader;
import com.parttime.net.DefaultCallback;
import com.parttime.net.HuanXinRequest;
import com.qingmu.jianzhidaren.R;
import com.quark.citylistview.CharacterParser;
import com.quark.jianzhidaren.ApplicationControl;
import com.quark.model.HuanxinUser;
import com.quark.utils.WaitDialog;
import com.quark.volley.VolleySington;

/**
 * 
 * @ClassName: PickContactNoCheckboxActivity
 * @Description: ContactAdatper 增加了list
 * @author howe
 * @date 2015-2-5 下午8:21:02
 * 
 */
public class PickContactNoCheckboxActivity extends BaseActivity {

	private ListView listView;
	private Sidebar sidebar;
	protected ContactAdapter contactAdapter;
	private List<User> contactList;
	private List<String> contactIds;
	private StringBuilder contactIdsStr = new StringBuilder("");
	ArrayList<HuanxinUser> usersNick = new ArrayList<>();
	// =========转拼音========
	private CharacterParser characterParser;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	private PinyinComparatorByHeader pinyinComparatorTwo;
	// =======转拼音========
	protected WaitDialog dialog;
	protected RequestQueue queue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_contact_no_checkbox);
		listView = (ListView) findViewById(R.id.list);
		sidebar = (Sidebar) findViewById(R.id.sidebar);
		sidebar.setListView(listView);
		queue = VolleySington.getInstance().getRequestQueue();
		contactList = new ArrayList<>();
		contactIds = new ArrayList<>();
		// 实例化汉字转拼音类 加
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		pinyinComparatorTwo = new PinyinComparatorByHeader();

		// 获取设置contactlist
		getContactList();
		// 设置adapter
		// contactAdapter = new ContactAdapter(this, R.layout.row_contact,
		// contactList, sidebar);
		//

	}

	protected void onListItemClick(int position) {
		// if (position != 0) {
		setResult(
				RESULT_OK,
				new Intent().putExtra("username",
						contactAdapter.getItem(position).getUsername()));
		finish();
		// }
	}

	public void back(View view) {
		finish();
	}

	private void getContactList() {
		contactList.clear();
		Map<String, User> users = ApplicationControl.getInstance()
				.getContactList();
		Iterator<Entry<String, User>> iterator = users.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, User> entry = iterator.next();
			if (!entry.getKey().equals(Constant.NEW_FRIENDS_USERNAME)
					&& !entry.getKey().equals(Constant.GROUP_USERNAME))
				if (!entry.getKey().equals(ApplicationConstants.JZDR)) {
					contactList.add(entry.getValue());
				}
		}

		// 排序
		Collections.sort(contactList, new Comparator<User>() {

			@Override
			public int compare(User lhs, User rhs) {
				return lhs.getUsername().compareTo(rhs.getUsername());
			}
		});

		for (int i = 0; i < contactList.size(); i++) {
			contactIds.add(contactList.get(i).getUsername());
		}

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
				// setlist();
				// refresh();
			} else {
				getNick();
			}
		} else {
			filledData();
			// setlist();
			// refresh();
		}
	}

	/**
	 * 转化拼音
	 * 
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
		// nullhx.setNamePinyin("aaa");
		// nullhx.setName("aaa");
		// usersNick.add(0,nullhx);
		// usersNick.add(0,nullhx);

		Map<String, User> users = ApplicationControl.getInstance()
				.getContactList();
		// 加入"申请与通知"和"群聊"
		// contactList.add(0, users.get(Constant.GROUP_USERNAME));
		// 把"申请与通知"添加到首位
		// contactList.add(0, users.get(Constant.NEW_FRIENDS_USERNAME));

		contactAdapter = new ContactAdapter(this, R.layout.row_contact,
				contactList, sidebar, usersNick);
		listView.setAdapter(contactAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onListItemClick(position);
			}
		});
		// setlist();
		// refresh();
	}

	// ====================================howe=========================

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
}
