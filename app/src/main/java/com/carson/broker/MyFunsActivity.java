package com.carson.broker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.qingmu.jianzhidaren.R;
import com.quark.adapter.FunsAdapter;
import com.quark.common.JsonUtil;
import com.quark.common.Url;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.jianzhidaren.MainCompanyActivity;
import com.quark.model.FunsBean;
import com.quark.volley.VolleySington;

public class MyFunsActivity extends BaseActivity implements IXListViewListener {
	ArrayList<FunsBean> lists = new ArrayList<FunsBean>();
	XListView list;
	private FunsAdapter adpter;
	private String dataUrl;
	int pageNumber = 1;
	int currentCount = 1;
	int page_size = 20;
	private String user_id;
	private SharedPreferences sp;
	private RequestQueue queue;
	private ImageView nodata_img;
	private LinearLayout my_funs_layout;// 显示粉丝数
	private String city;
	private TextView my_funs_num_tv;// 当前粉丝数
	private int funs;// 传值过来的粉丝数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_funs);
		funs = getIntent().getIntExtra("funs", 0);
		my_funs_num_tv = (TextView) findViewById(R.id.my_funs_num_tv);
		my_funs_num_tv.setText(String.valueOf(funs) + "人");
		nodata_img = (ImageView) findViewById(R.id.nodata_img);
		my_funs_layout = (LinearLayout) findViewById(R.id.my_funs_layout);
		queue = VolleySington.getInstance().getRequestQueue();
		setTopTitle("我的粉丝");
		setBackButton();
		dataUrl = Url.MY_FOLLOWERS_LIST + "?token=" + MainCompanyActivity.token;
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		user_id = sp.getString("userId", "");
		city = sp.getString("city", "");

		// 头部显示灰色
		RelativeLayout reLayout = (RelativeLayout) findViewById(R.id.top_title_layout);
		reLayout.setBackgroundColor(getResources().getColor(
				R.color.guanli_common_color));
	}

	/**
	 * 清空数据链表
	 */
	private void init_lists() {
		lists = new ArrayList<FunsBean>();
	}

	@Override
	protected void onResume() {
		super.onResume();
		init_lists();// 置空数据链表
		pageNumber = 1;
		getData();
		list = (XListView) findViewById(R.id.my_funs_list);
		list.setPullLoadEnable(true);// 上啦刷新
		adpter = new FunsAdapter(MyFunsActivity.this, lists);
		list.setAdapter(adpter);
		list.setPullRefreshEnable(true);
		list.setXListViewListener(this);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

			}
		});

	}

	public void resetData() {
		init_lists();// 置空数据链表
		pageNumber = 1;
		getData();
	}

	// ========================xlist=================================================
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (lists != null && lists.size() > 0) {
				nodata_img.setVisibility(View.GONE);
				my_funs_layout.setVisibility(View.VISIBLE);
				adpter.notifyDataSetChanged();
				list.setLoadOver(currentCount, page_size);// 用于是否加载完了
				onLoad();
			} else {
				my_funs_layout.setVisibility(View.GONE);
				nodata_img.setVisibility(View.VISIBLE);
				list.setVisibility(View.GONE);
			}
		};
	};

	private void onLoad() {
		list.stopRefresh();
		list.stopLoadMore();
		list.setRefreshTime("刚刚");
	}

	/**
	 * 分享兼职活动
	 */
	public void setTopTitle(String titlestr) {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(titlestr);
	}

	/**
	 * 设置返回按钮
	 */
	public void setBackButton() {

		LinearLayout back_lay = (LinearLayout) findViewById(R.id.left);
		back_lay.setVisibility(View.VISIBLE);
		back_lay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onRefresh() {
		pageNumber = 1;
		lists.clear();
		getData();
		// mAdapter.notifyDataSetChanged();
		// adpter = new FunsAdapter(MyFunsActivity.this, lists);
		// list.setAdapter(adpter);
	}

	@Override
	public void onLoadMore() {
		pageNumber++;
		getData();
	}

	// ======xlist end============

	public void getData() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST, dataUrl,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss2 = js
									.getJSONObject("responseStatus");
							int status = jss2.getInt("status");
							// status 1返回成功，其它失败
							if (status == 1) {
								JSONObject jss3 = jss2
										.getJSONObject("followerPage");
								JSONArray ls = jss3.getJSONArray("list");
								if (ls.length() > 0) {
									for (int i = 0; i < ls.length(); i++) {
										FunsBean funsbean = (FunsBean) JsonUtil
												.jsonToBean(
														ls.getJSONObject(i),
														FunsBean.class);
										lists.add(funsbean);
									}
								}
								currentCount = ls.length();
								Message msg = handler.obtainMessage();
								msg.what = 20;
								handler.sendMessage(msg);
							} else {
								Message msg = handler.obtainMessage();
								msg.what = 20;
								handler.sendMessage(msg);
							}
						} catch (JSONException e) {
							e.printStackTrace();
							Message msg = handler.obtainMessage();
							msg.what = 20;
							handler.sendMessage(msg);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showWait(false);
						Message msg = handler.obtainMessage();
						msg.what = 20;
						handler.sendMessage(msg);
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("company_id", user_id);
				map.put("city", city);
				map.put("page_size", page_size + "");
				map.put("pn", pageNumber + "");
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

}
