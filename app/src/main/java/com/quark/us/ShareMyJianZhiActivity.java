package com.quark.us;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.carson.loadpic.SwipeAdapter;
import com.qingmu.jianzhidaren.R;
import com.quark.adapter.MyJianZhiCollectedAdapter;
import com.quark.common.JsonUtil;
import com.quark.common.ToastUtil;
import com.quark.common.Url;
import com.quark.guangchang.ActivityDetialActivity;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.jianzhidaren.MainCompanyActivity;
import com.quark.model.GuangchangModle;
import com.quark.volley.VolleySington;

public class ShareMyJianZhiActivity extends BaseActivity implements
		IXListViewListener {

	ArrayList<GuangchangModle> lists = new ArrayList<GuangchangModle>();
	XListView list;
	private MyJianZhiCollectedAdapter adpter;
	private String dataUrl;
	int pageNumber = 1;
	int currentCount = 1;
	int page_size = 5;
	private String user_id;
	private SharedPreferences sp;
	private RequestQueue queue;
	private ImageView nodata_img;
	private boolean isFromShareFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_my_jianzhi);
		nodata_img = (ImageView) findViewById(R.id.nodata_img);
		queue = VolleySington.getInstance().getRequestQueue();
		isFromShareFlag = getIntent().getBooleanExtra("isFromShare", true);
		if (isFromShareFlag) {
			setTopTitle("分享我的收藏");
		} else {
			setTopTitle("我的收藏");
		}
		setBackButton();
		dataUrl = Url.USER_COLLECT_ACTIVITY + "?token=" + MainCompanyActivity.token;
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		user_id = sp.getString("userId", "");
		list = (XListView) findViewById(R.id.share_list);
		list.setPullLoadEnable(true);
		list.setPullRefreshEnable(true);
		list.setXListViewListener(this);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				if (isFromShareFlag) {
					// 回调接口到chatactivity
					Intent intent = new Intent(); // Itent就是我们要发送的内容
					intent.setAction("com.carson.share.jianzhi"); // 设置你这个广播的action
					intent.putExtra("activity_id", lists.get(position - 1)
							.getActivity_id() + "");
					intent.putExtra("title", lists.get(position - 1).getTitle());
					intent.putExtra("pay", lists.get(position - 1).getPay());
					intent.putExtra("pay_type", lists.get(position - 1)
							.getPay_type());
					intent.putExtra("job_place", lists.get(position - 1)
							.getCounty());
					intent.putExtra("start_time", lists.get(position - 1)
							.getStart_time());
					intent.putExtra("left_count", lists.get(position - 1)
							.getLeft_count());
					sendBroadcast(intent); // 发送广播
					ShareMyJianZhiActivity.this.finish();
					ToastUtil.showShortToast("活动分享成功^_^");
				} else {
					Intent intent = new Intent();
					intent.setClass(ShareMyJianZhiActivity.this,
							ActivityDetialActivity.class);
					if (lists != null && lists.size() > 0 && position > 0) {
						intent.putExtra("activity_id", lists.get(position - 1)
								.getActivity_id() + "");
						intent.putExtra("isComeFromGuangChang", false);
						startActivity(intent);
					}
				}

			}
		});
	}

	/**
	 * 清空数据链表
	 */
	private void init_lists() {
		lists = new ArrayList<GuangchangModle>();
	}

	@Override
	protected void onResume() {
		super.onResume();
		init_lists();// 置空数据链表
		pageNumber = 1;
		getData();

	}

	public void resetData() {
		init_lists();// 置空数据链表
		pageNumber = 1;
		getData();
	}

	// ========================xlist=================================================
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			adpter = new MyJianZhiCollectedAdapter(ShareMyJianZhiActivity.this,
					lists, isFromShareFlag);
			list.setAdapter(adpter);
			adpter.setMode(SwipeAdapter.Mode.Single);
			if (lists != null && lists.size() > 0) {
				nodata_img.setVisibility(View.GONE);
			} else {
				nodata_img.setVisibility(View.VISIBLE);
			}
			list.setLoadOver(currentCount, page_size);// 用于是否加载完了
			onLoad();
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
		adpter = new MyJianZhiCollectedAdapter(ShareMyJianZhiActivity.this,
				lists, isFromShareFlag);
		list.setAdapter(adpter);
		adpter.setMode(SwipeAdapter.Mode.Single);
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
									.getJSONObject("CollectedActivityResponse");
							int status = jss2.getInt("status");
							if (status == 1) {
								JSONObject jss = jss2
										.getJSONObject("collectedActivity");
								JSONArray ls = jss.getJSONArray("list");
								if (ls.length() > 0) {
									for (int i = 0; i < ls.length(); i++) {
										GuangchangModle jianzhi = new GuangchangModle();
										jianzhi = (GuangchangModle) JsonUtil
												.jsonToBean(
														ls.getJSONObject(i),
														GuangchangModle.class);
										lists.add(jianzhi);
									}
								}
								currentCount = ls.length();

								Message msg = handler.obtainMessage();
								msg.what = 20;
								handler.sendMessage(msg);
							} else {

							}
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
				map.put("user_id", user_id);
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
