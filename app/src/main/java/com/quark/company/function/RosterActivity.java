package com.quark.company.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.qingmu.jianzhidaren.R;
import com.quark.adapter.RosterAdapter;
import com.quark.common.JsonUtil;
import com.quark.common.Url;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.jianzhidaren.MainCompanyActivity;
import com.quark.model.Roster;

/**
 * 商家：花名册
 * 
 * @author C罗
 * 
 */
public class RosterActivity extends BaseActivity implements IXListViewListener {

	private RosterAdapter rosterAdapter;
	static XListView listView;// 已确认列表
	ArrayList<Roster> list = new ArrayList<Roster>();
	int pageNumber = 1;
	int currentCount = 1;
	int page_size = 5;
	private String url;
	private String user_id;
	private SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_roster);
		setTopTitle("花名册");
		setBackButton();
		sp = getSharedPreferences("jrdr.setting", Activity.MODE_PRIVATE);
		user_id = sp.getString("userId", "");
		url = Url.COMPANY_roster + "?token=" + MainCompanyActivity.token;
		

	}

	/**
	 * i=1默认加载取消兼职的数据，i!=1不加载取消兼职的数据
	 * 
	 * @param i
	 */
	private void getData(final int i) {
		showWait(true);
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				url, new Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.e("erros", "返回来ddd数据" + response);
						showWait(false);
						try {
							JSONObject js1 = new JSONObject(response);
							JSONObject jss2 = js1
									.getJSONObject("FaceBookResponse");
							if (i == 1) {
								// 取消
								JSONArray js12 = jss2.getJSONArray("tips");
								if (js12.length() > 0) {
									for (int i = 0; i < js12.length(); i++) {
										Roster rc = new Roster();
										rc = (Roster) JsonUtil.jsonToBean(
												js12.getJSONObject(i),
												Roster.class);
										list.add(rc);
									}
								}
							}
							// 已确认
							JSONObject jsss3 = jss2
									.getJSONObject("facebookList");
							JSONArray jssss4 = jsss3.getJSONArray("list");
							if (jssss4.length() > 0) {
								for (int i = 0; i < jssss4.length(); i++) {
									Roster rt = new Roster();
									rt = (Roster) JsonUtil.jsonToBean(
											jssss4.getJSONObject(i),
											Roster.class);
									list.add(rt);
								}
							}
							currentCount = jssss4.length();
							Message msg = handler.obtainMessage();
							msg.what = 1;
							handler.sendMessage(msg);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						showWait(false);
						Log.e("TAG", error.getMessage(), error);
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("company_id", user_id);
				map.put("page_size", page_size + "");
				map.put("pn", pageNumber + "");

				return map;
			}
		};
		queue.add(stringRequest);
	}

	// ========================xlist=================================================
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			listView.setLoadOver(currentCount, page_size);// 用于是否加载完了
			rosterAdapter.notifyDataSetChanged();
			onLoad();
		};
	};

	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		pageNumber = 1;
		list.clear();
		getData(1);
		// mAdapter.notifyDataSetChanged();
		rosterAdapter = new RosterAdapter(this, list);
		listView.setAdapter(rosterAdapter);
	}

	@Override
	public void onLoadMore() {
		pageNumber++;
		getData(2);
	}

	// ======xlist end============

	@Override
	protected void onResume() {
		super.onResume();
		list.clear();
		getData(1);
		// 确认兼职
		listView = (XListView) this.findViewById(R.id.roster_list);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
		rosterAdapter = new RosterAdapter(this, list);
		listView.setAdapter(rosterAdapter);
		// 点击
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				// 取消兼职
				if (list.get(position - 1).getId() > 0) {
					intent.setClass(RosterActivity.this,
							CancelBaomingActivity.class);
					intent.putExtra("activity_id", list.get(position - 1)
							.getActivity_id() + "");
					intent.putExtra("user_id", list.get(position - 1)
							.getUser_id() + "");
				} else {
					intent.setClass(RosterActivity.this,
							FullStarffedActivity.class);
					intent.putExtra("activity_id", list.get(position - 1)
							.getActivity_id() + "");
					intent.putExtra("activity_name", list.get(position - 1)
							.getTitle());
					intent.putExtra("total_num", list.get(position - 1)
							.getHead_count() + "");
				}
				startActivity(intent);
			}

		});
	}

}
