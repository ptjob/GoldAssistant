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
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.easemob.chatuidemo.activity.AlertDialog;
import com.easemob.chatuidemo.activity.BaseActivity;
import com.qingmu.jianzhidaren.R;
import com.quark.adapter.HuanxingSearchUserAdapter;
import com.quark.common.JsonUtil;
import com.quark.common.ToastUtil;
import com.quark.common.Url;
import com.quark.model.HuanxinUser;
import com.quark.utils.WaitDialog;
import com.quark.volley.VolleySington;

public class AddContactActivity extends BaseActivity {
	private EditText editText;
	private LinearLayout searchedUserLayout;
	private TextView nameText;
	private Button searchBtn;
	private ImageView avatar;
	private InputMethodManager inputMethodManager;
	private String toAddUsername;
	private ProgressDialog progressDialog;
	private String searchUrl;
	protected RequestQueue queue = VolleySington.getInstance()
			.getRequestQueue();
	protected WaitDialog dialog;
	ArrayList<HuanxinUser> users = new ArrayList<HuanxinUser>();
	HuanxingSearchUserAdapter adapter;
	ListView listView;
	RelativeLayout topLayout;// 商家变灰色,用户不变
	SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		topLayout = (RelativeLayout) findViewById(R.id.title);
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		topLayout.setBackgroundColor(getResources().getColor(
				R.color.guanli_common_color));
		searchUrl = Url.HUANXIN_search;
		listView = (ListView) findViewById(R.id.list);
		editText = (EditText) findViewById(R.id.edit_note);
		searchedUserLayout = (LinearLayout) findViewById(R.id.ll_user);
		nameText = (TextView) findViewById(R.id.name);
		searchBtn = (Button) findViewById(R.id.search);
		avatar = (ImageView) findViewById(R.id.avatar);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	String name;

	/**
	 * 查找contact
	 * 
	 * @param v
	 */
	public void searchContact(View v) {
		name = editText.getText().toString();
		String saveText = searchBtn.getText().toString();

		if (getString(R.string.button_search).equals(saveText)) {
			toAddUsername = name;
			if (TextUtils.isEmpty(name)) {
				startActivity(new Intent(this, AlertDialog.class).putExtra(
						"msg", "请输入用户名"));
				return;
			}
			// TODO 从服务器获取此contact,如果不存在提示不存在此用户
			// 服务器存在此用户，显示此用户和添加按钮
			// 通过服务器获取用户列表 修改为list
			getData();
			// searchedUserLayout.setVisibility(View.VISIBLE);
			// nameText.setText(toAddUsername);
		}
	}



	public void back(View v) {
		finish();
	}

	// 获取查找结果
	private void getData() {
		users.clear();
		showWait(true);
		StringRequest stringRequest = new StringRequest(Method.POST, searchUrl,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONArray jss = js.getJSONArray("userList");
							for (int i = 0; i < jss.length(); i++) {
								HuanxinUser user = (HuanxinUser) JsonUtil
										.jsonToBean(jss.getJSONObject(i),
												HuanxinUser.class);
								users.add(user);
							}

							adapter = new HuanxingSearchUserAdapter(
									AddContactActivity.this, users,
									AddContactActivity.this);
							listView.setAdapter(adapter);
							if (users != null && users.size() > 0) {

							} else {
								ToastUtil.showShortToast("未查询到结果。。。");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						showWait(false);
						users = new ArrayList<>();
						adapter = new HuanxingSearchUserAdapter(
								AddContactActivity.this, users,
								AddContactActivity.this);
						listView.setAdapter(adapter);
						ToastUtil.showShortToast("网络异常");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", name);

				return map;

			}
		};
		queue.add(stringRequest);
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
	}

	public void showWait(boolean isShow) {
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
