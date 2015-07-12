package com.quark.guangchang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.qingmu.jianzhidaren.R;
import com.quark.adapter.GuangchangListAdapter;
import com.quark.common.JsonUtil;
import com.quark.common.Url;
import com.quark.jianzhidaren.BaseActivity;
import com.parttime.main.MainTabActivity;
import com.quark.model.GuangchangModle;
import com.quark.ui.widget.CommonWidget;

/**
 * 
 * @ClassName: SearchResult
 * @Description: 搜索结果
 * @author howe
 * @date 2015-1-27 上午11:49:07
 * 
 */
public class SearchResult extends BaseActivity implements IXListViewListener {

	private GuangchangListAdapter adapter;
	static XListView listView;
	static View findLayout;
	ArrayList<GuangchangModle> jianzhis = new ArrayList<GuangchangModle>();
	int pageNumber = 1;
	int currentCount = 1;
	int page_size = 5;
	private String url;
	private String user_id;

	String choosetimeStr;
	String chooseCityStr;
	String chooseTypeStr;
	String choosePayTypeStr;
	boolean newJianZhiFlag = true;// true最新 false 最近
	double lat, lng;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guangchang_search_result);
		LinearLayout left = (LinearLayout) findViewById(R.id.left);
		left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		SharedPreferences sp = getSharedPreferences("jrdr.setting",
				MODE_PRIVATE);
		user_id = sp.getString("userId", "");
		url = Url.COMPANY_filter + "?token=" + MainTabActivity.token;

		choosetimeStr = getIntent().getStringExtra("choosetimeStr");
		chooseCityStr = getIntent().getStringExtra("chooseCityStr");
		chooseTypeStr = getIntent().getStringExtra("chooseTypeStr");
		choosePayTypeStr = getIntent().getStringExtra("choosePayTypeStr");
		newJianZhiFlag = getIntent()
				.getBooleanExtra("choose_filter_type", true);
		// 获取经纬度
		if (!newJianZhiFlag) {
			lat = getIntent().getDoubleExtra("choose_lat", 0);
			lng = getIntent().getDoubleExtra("choose_lng", 0);
		}
		getData();
		listView = (XListView) findViewById(R.id.list1);
		listView.setPullLoadEnable(true);
		adapter = new GuangchangListAdapter(this, jianzhis);
		listView.setAdapter(adapter);
		listView.setXListViewListener(this);

		// 点击效果
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				if (user_id.equals("")) {
					CommonWidget.showAlertDialog(SearchResult.this,
							SearchResult.this, "您还没有登陆，注册登陆后才可以查看哦！", "温馨提示",
							"随便看看");
				} else {
					if (jianzhis != null && jianzhis.size() > 0 && position > 0) {
						Intent intent = new Intent();
						intent.setClass(SearchResult.this,
								ActivityDetialActivity.class);
						intent.putExtra("activity_id",
								jianzhis.get(position - 1).getActivity_id()
										+ "");
						startActivity(intent);
					}
				}
			}
		});

		TextView right = (TextView) findViewById(R.id.right);
		right.setOnClickListener(saixuanListener);
	}

	View.OnClickListener saixuanListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(SearchResult.this, SaiXuanActivity.class);
			startActivity(intent);
		}
	};

	public void getData() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss = js
									.getJSONObject("ActivityResponse");
							JSONArray jsss = jss.getJSONArray("list");
							if (jsss.length() > 0) {
								for (int i = 0; i < jsss.length(); i++) {
									GuangchangModle jianzhi = new GuangchangModle();
									jianzhi = (GuangchangModle) JsonUtil
											.jsonToBean(jsss.getJSONObject(i),
													GuangchangModle.class);
									jianzhis.add(jianzhi);
								}
							}
							currentCount = jsss.length();

							Message msg = handler.obtainMessage();
							msg.what = 1;
							handler.sendMessage(msg);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showWait(false);
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("time", choosetimeStr);
				map.put("county", chooseCityStr);
				map.put("type", chooseTypeStr);
				map.put("pay_form", choosePayTypeStr);
				if (newJianZhiFlag) {
					map.put("filter_type", "0");
				} else {
					map.put("filter_type", "1");
					map.put("lat", String.valueOf(lat));
					map.put("lng", String.valueOf(lng));
				}

				map.put("page_size", page_size + "");
				map.put("pn", pageNumber + "");

				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	// ========================xlist=================================================
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			listView.setLoadOver(currentCount, page_size);// 用于是否加载完了
			adapter.notifyDataSetChanged();
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
		jianzhis.clear();
		getData();
		// mAdapter.notifyDataSetChanged();
		adapter = new GuangchangListAdapter(this, jianzhis);
		listView.setAdapter(adapter);
	}

	@Override
	public void onLoadMore() {
		pageNumber++;
		getData();
	}
	// ======xlist end============

}
