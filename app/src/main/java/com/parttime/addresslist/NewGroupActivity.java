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

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.activity.AlertDialog;
import com.easemob.chatuidemo.activity.BaseActivity;
import com.easemob.exceptions.EaseMobException;
import com.google.gson.Gson;
import com.parttime.base.WithTitleActivity;
import com.parttime.constants.ActivityExtraAndKeys;
import com.parttime.pojo.GroupDescription;
import com.parttime.widget.CountingEditText;
import com.parttime.widget.EditItem;
import com.qingmu.jianzhidaren.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class NewGroupActivity extends WithTitleActivity {
//	private EditText groupNameEditText;
	private ProgressDialog progressDialog;
//	private EditText introductionEditText;
	private CheckBox checkBox;
	private CheckBox memberCheckbox;
	private LinearLayout openInviteContainer;

	private EditItem eiName;
	private CountingEditText eiNotification;

	@Override
	protected ViewGroup getLeftWrapper() {
		return null;
	}

	@Override
	protected ViewGroup getRightWrapper() {
		return null;
	}

	@Override
	protected TextView getCenter() {
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_group);
		initView();
		eiName = (EditItem) findViewById(R.id.ei_name);
		eiNotification = (CountingEditText) findViewById(R.id.cet_notification);
		checkBox = (CheckBox) findViewById(R.id.cb_public);
		memberCheckbox = (CheckBox) findViewById(R.id.cb_member_inviter);
		openInviteContainer = (LinearLayout) findViewById(R.id.ll_open_invite);

		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					openInviteContainer.setVisibility(View.INVISIBLE);
				} else {
					openInviteContainer.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	private void initView(){
		center(R.string.new_group);
		left(TextView.class, R.string.back);
	}

	/**
	 * @param v
	 */
	public void chooseMember(View v) {
		String name = eiName.getValue().trim();
		if (TextUtils.isEmpty(name)) {
			Intent intent = new Intent(this, AlertDialog.class);
			intent.putExtra("msg", "群组名称不能为空");
			startActivity(intent);
		} else {

			// 进通讯录选人
			startActivityForResult(
					new Intent(this, GroupPickContactsActivity.class).putExtra(
							"groupName", name), 0);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			// 新建群组
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在创建群聊...");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();

			new Thread(new Runnable() {
				@Override
				public void run() {
					// 调用sdk创建群组方法
					String groupName = eiName.getValue()
							.trim();
					String desc = eiNotification.getText().toString().trim();
                    GroupDescription gd = new GroupDescription();
                    gd.type = GroupDescription.ACTIVITY_ADDRESSBOOK_GROUP;
                    gd.info = desc;
                    desc = new Gson().toJson(gd);
                    try {
                        desc = URLEncoder.encode(desc,"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String[] members = data.getStringArrayExtra(ActivityExtraAndKeys.Addressbook.MEMBER);
					try {
						if (checkBox.isChecked()) {
							// 创建公开群，此种方式创建的群，可以自由加入
							// EMGroupManager.getInstance().createPublicGroup(groupName,
							// desc, members, false);
							// 创建公开群，此种方式创建的群，用户需要申请，等群主同意后才能加入此群
							EMGroupManager.getInstance().createPublicGroup(
									groupName, desc, members, true);
						} else {
							// 创建不公开群
							EMGroupManager.getInstance().createPrivateGroup(
									groupName, desc, members,
									memberCheckbox.isChecked());
						}
						runOnUiThread(new Runnable() {
							public void run() {
								progressDialog.dismiss();
								setResult(RESULT_OK);
								finish();
							}
						});
					} catch (final EaseMobException e) {
						runOnUiThread(new Runnable() {
							public void run() {
								progressDialog.dismiss();
								Toast.makeText(NewGroupActivity.this, "创建群组失败",
										Toast.LENGTH_LONG).show();
							}
						});
					}

				}
			}).start();
		}
	}

	public void back(View view) {
		finish();
	}
}
