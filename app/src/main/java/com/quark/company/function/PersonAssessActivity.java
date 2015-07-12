package com.quark.company.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.qingmu.jianzhidaren.R;
import com.quark.adapter.PersonAssessAdapter;
import com.quark.common.JsonUtil;
import com.quark.common.Url;
import com.quark.jianzhidaren.BaseActivity;
import com.parttime.main.MainTabActivity;
import com.quark.model.RosterActivityList;
import com.quark.model.RosterUser;

/**
 * 评价人员列表
 * 
 * @author Administrator
 * 
 */
public class PersonAssessActivity extends BaseActivity {

	// 参数
	PersonAssessAdapter personAssessAdapter;

	private ListView listView;
	private RosterActivityList rosterList;
	private String activity_id;
	private String company_id;
	private String url;
	private String activity_name, total;
	private TextView men_num, woman_num, total_num;
	ArrayList<RosterUser> list = new ArrayList<RosterUser>();

	@Override
	public void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		setContentView(R.layout.company_person_evaluate_list);
		setBackButton();
		setTopTitle("人员评价");
		// 处理数据
		activity_id = getIntent().getStringExtra("activity_id");
		total = getIntent().getStringExtra("total_num");
		SharedPreferences sp = getSharedPreferences("jrdr.setting",
				Activity.MODE_PRIVATE);
		company_id = sp.getString("userId", "");
		//
		url = Url.COMPANY_activityFaceBook + "?token="
				+ MainTabActivity.token;
		RelativeLayout topLayout = (RelativeLayout) findViewById(R.id.top_title_layout);
		topLayout.setBackgroundColor(getResources().getColor(
				R.color.guanli_common_color));

	}

	private void getData() {
		list.clear();
		showWait(true);
		StringRequest stringRequest = new StringRequest(Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.e("res", response);
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss = js
									.getJSONObject("ActivityFaceBook");
							rosterList = (RosterActivityList) JsonUtil
									.jsonToBean(jss, RosterActivityList.class);
							JSONArray jsss = jss.getJSONArray("list");
							if (jsss.length() > 0) {
								for (int i = 0; i < jsss.length(); i++) {
									RosterUser rt = new RosterUser();
									rt = (RosterUser) JsonUtil.jsonToBean(
											jsss.getJSONObject(i),
											RosterUser.class);
									list.add(rt);
								}
							}
							initView();
							Message msg = handler.obtainMessage();
							msg.what = 1;
							handler.sendMessage(msg);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						showWait(false);
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> map = new HashMap<String, String>();
				map.put("activity_id", activity_id);
				return map;
			}
		};
		queue.add(stringRequest);
		stringRequest
				.setRetryPolicy(new DefaultRetryPolicy(ConstantForSaveList.DEFAULTRETRYTIME*1000, 1, 1.0f));

	}

	protected void initView() {
		// TODO Auto-generated method stub
		men_num = (TextView) findViewById(R.id.men_num);
		woman_num = (TextView) findViewById(R.id.woman_num);
		total_num = (TextView) findViewById(R.id.total_num);
		men_num.setText(rosterList.getMale() + "人");
		woman_num.setText(rosterList.getFemale() + "人");
		total_num
				.setText((rosterList.getMale() + rosterList.getFemale()) + "人");
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			personAssessAdapter.notifyDataSetChanged();
		};
	};

	@Override
	protected void onResume() {
		super.onResume();
		getData();
		listView = (ListView) findViewById(R.id.ListView01);
		personAssessAdapter = new PersonAssessAdapter(this, list);
		listView.setAdapter(personAssessAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (list.get(position).getIs_commented() == 0) {
					Intent intent = new Intent();
					// ablecomment 活动开始前不能评论 0表示不能评论
					if (list.get(position).getAbleComment() == 0) {
						showToast("您的活动还未开始,请活动开始后再评价");
					} else {
						// 如果是取消兼职的评价
						if (list.get(position).getStatus() == 0) {

							intent.setClass(PersonAssessActivity.this,
									CancelBaomingActivity.class);
							intent.putExtra("activity_id", activity_id);
							intent.putExtra("user_id", list.get(position)
									.getUser_id() + "");
						} else {
							intent.putExtra("activity_id", activity_id);
							intent.putExtra("user_id", list.get(position)
									.getUser_id() + "");
							intent.setClass(PersonAssessActivity.this,
									PersonAssessDetailActivity.class);
						}
						startActivity(intent);
					}
				} else {
					showToast("已评价");
				}
			}
		});
	};
}
