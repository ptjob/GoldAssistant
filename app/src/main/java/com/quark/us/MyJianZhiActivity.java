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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qingmu.jianzhidaren.R;
import com.quark.adapter.UserJianzhitAdapter;
import com.quark.common.JsonUtil;
import com.quark.common.Url;
import com.quark.guangchang.ActivityDetialActivity;
import com.quark.jianzhidaren.BaseActivity;
import com.parttime.main.MainTabActivity;
import com.quark.model.MyJianZhiModle;

/**
 * 我的兼职
 * 
 * @author
 * 
 */
public class MyJianZhiActivity extends BaseActivity implements
		IXListViewListener {

	static View findLayout;
	ArrayList<MyJianZhiModle> lists1 = new ArrayList<MyJianZhiModle>();
	ArrayList<MyJianZhiModle> lists2 = new ArrayList<MyJianZhiModle>();
	ArrayList<MyJianZhiModle> lists3 = new ArrayList<MyJianZhiModle>();
	ArrayList<MyJianZhiModle> lists4 = new ArrayList<MyJianZhiModle>();
	XListView list1;
	XListView list2;
	XListView list3;
	XListView list4;
	private UserJianzhitAdapter adpter1;
	private UserJianzhitAdapter adpter2;
	private UserJianzhitAdapter adpter3;
	private UserJianzhitAdapter adpter4;

	private String dataUrl;
	private int type = 1; // type:1-全部、2-已录取、3-待录取、4-被拒绝

	int pageNumber = 1;
	int pageNumber1 = 1;
	int pageNumber2 = 1;
	int pageNumber3 = 1;
	int pageNumber4 = 1;

	int currentCount1 = 1;
	int currentCount2 = 1;
	int currentCount3 = 1;
	int currentCount4 = 1;

	int page_size = 5;
	private String user_id;

	@ViewInject(R.id.all_text)
	TextView all_text;
	@ViewInject(R.id.all_img)
	ImageView all_img;

	@ViewInject(R.id.agree_text)
	TextView agree_text;
	@ViewInject(R.id.agree_img)
	ImageView agree_img;

	@ViewInject(R.id.wait_text)
	TextView wait_text;
	@ViewInject(R.id.wait_img)
	ImageView wait_img;

	@ViewInject(R.id.reject_text)
	TextView reject_text;
	@ViewInject(R.id.reject_img)
	ImageView reject_img;

	@ViewInject(R.id.nodata_img)
	ImageView nodata_img;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_jianzhi);
		setTopTitle("我的兼职");
		ViewUtils.inject(this);
		setBackButton();
		dataUrl = Url.COMPANY_requireActivity + "?token=" + MainTabActivity.token;
		SharedPreferences sp = getSharedPreferences("jrdr.setting",
				MODE_PRIVATE);
		user_id = sp.getString("userId", "");
		list1 = (XListView) findViewById(R.id.list1);
		list2 = (XListView) findViewById(R.id.list2);
		list3 = (XListView) findViewById(R.id.list3);
		list4 = (XListView) findViewById(R.id.list4);

		list1.setPullLoadEnable(true);
		list1.setPullRefreshEnable(true);
		list2.setPullLoadEnable(true);
		list2.setPullRefreshEnable(true);
		list3.setPullLoadEnable(true);
		list3.setPullRefreshEnable(true);
		list4.setPullLoadEnable(true);
		list4.setPullRefreshEnable(true);

		list1.setXListViewListener(this);
		list2.setXListViewListener(this);
		list3.setXListViewListener(this);
		list4.setXListViewListener(this);

		list1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				// 网络异常加载造成outofbound异常
				if (lists1 != null && lists1.size() > 0 && position >= 1) {
					Intent intent = new Intent();
					intent.setClass(MyJianZhiActivity.this,
							ActivityDetialActivity.class);
					intent.putExtra("activity_id", lists1.get(position - 1)
							.getActivity_id() + "");
					intent.putExtra("isComeFromGuangChang", false);
					startActivity(intent);
				}
			}
		});
		list2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				// 网络异常加载造成outofbound异常
				if (lists2 != null && lists2.size() > 0 && position >= 1) {
					Intent intent = new Intent();
					intent.setClass(MyJianZhiActivity.this,
							ActivityDetialActivity.class);
					intent.putExtra("activity_id", lists2.get(position - 1)
							.getActivity_id() + "");
					intent.putExtra("isComeFromGuangChang", false);
					startActivity(intent);
				}

			}
		});
		list3.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				// 网络异常加载造成outofbound异常
				if (lists3 != null && lists3.size() > 0 && position >= 1) {
					Intent intent = new Intent();
					intent.setClass(MyJianZhiActivity.this,
							ActivityDetialActivity.class);
					intent.putExtra("activity_id", lists3.get(position - 1)
							.getActivity_id() + "");
					intent.putExtra("isComeFromGuangChang", false);
					startActivity(intent);
				}

			}
		});
		list4.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				// 网络异常加载造成outofbound异常
				if (lists4 != null && lists4.size() > 0 && position >= 1) {
					Intent intent = new Intent();
					intent.setClass(MyJianZhiActivity.this,
							ActivityDetialActivity.class);
					intent.putExtra("activity_id", lists4.get(position - 1)
							.getActivity_id() + "");
					intent.putExtra("isComeFromGuangChang", false);
					startActivity(intent);
				}
			}
		});
	}

	/**
	 * 清空数据链表
	 */
	private void init_lists() {
		lists1 = new ArrayList<MyJianZhiModle>();
		lists2 = new ArrayList<MyJianZhiModle>();
		lists3 = new ArrayList<MyJianZhiModle>();
		lists4 = new ArrayList<MyJianZhiModle>();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// carson修改，点击兼职详情取消了兼职后能实时刷新数据
		init_lists();// 置空数据链表
		pageNumber = 1;
		getData();
		adpter1 = new UserJianzhitAdapter(MyJianZhiActivity.this, lists1);
		list1.setAdapter(adpter1);
		adpter2 = new UserJianzhitAdapter(MyJianZhiActivity.this, lists2);
		list2.setAdapter(adpter2);
		adpter3 = new UserJianzhitAdapter(MyJianZhiActivity.this, lists3);
		list3.setAdapter(adpter3);
		adpter4 = new UserJianzhitAdapter(MyJianZhiActivity.this, lists4);
		list4.setAdapter(adpter4);
	}

	@OnClick({ R.id.all_text, R.id.wait_text, R.id.agree_text, R.id.reject_text })
	public void setStatusOnclick(View view) {
		switch (view.getId()) {
		case R.id.all_text:
			lists1.removeAll(lists1);
			// pageNumber = pageNumber1;
			pageNumber = 1;
			type = 1;
			adpter1.notifyDataSetChanged();
			getData();
			setStatus(all_text, all_img);
			list1.setVisibility(View.VISIBLE);
			list2.setVisibility(View.GONE);
			list3.setVisibility(View.GONE);
			list4.setVisibility(View.GONE);
			break;

		case R.id.agree_text:
			lists2.removeAll(lists2);
			// pageNumber = pageNumber2;
			pageNumber = 1;
			type = 2;
			adpter2.notifyDataSetChanged();
			getData();
			nodata_img.setVisibility(View.GONE);
			setStatus(agree_text, agree_img);
			list1.setVisibility(View.GONE);
			list2.setVisibility(View.VISIBLE);
			list3.setVisibility(View.GONE);
			list4.setVisibility(View.GONE);
			break;
		case R.id.wait_text:
			lists3.removeAll(lists3);
			// pageNumber = pageNumber3;
			pageNumber = 1;
			type = 3;
			adpter3.notifyDataSetChanged();
			getData();
			nodata_img.setVisibility(View.GONE);
			setStatus(wait_text, wait_img);
			list1.setVisibility(View.GONE);
			list2.setVisibility(View.GONE);
			list3.setVisibility(View.VISIBLE);
			list4.setVisibility(View.GONE);
			break;
		case R.id.reject_text:
			lists4.removeAll(lists4);
			// pageNumber = pageNumber4;
			pageNumber = 1;
			type = 4;
			adpter4.notifyDataSetChanged();
			getData();
			nodata_img.setVisibility(View.GONE);
			setStatus(reject_text, reject_img);
			list1.setVisibility(View.GONE);
			list2.setVisibility(View.GONE);
			list3.setVisibility(View.GONE);
			list4.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	// ==========修改head状态=============
	public void setStatus(TextView textView, ImageView img) {
		clearStatus();
		textView.setTextColor(getResources().getColor(R.color.ziti_orange));
		img.setBackgroundColor(getResources().getColor(R.color.head_color));
	}

	public void clearStatus() {
		all_text.setTextColor(getResources().getColor(R.color.ziti_huise));
		wait_text.setTextColor(getResources().getColor(R.color.ziti_huise));
		agree_text.setTextColor(getResources().getColor(R.color.ziti_huise));
		reject_text.setTextColor(getResources().getColor(R.color.ziti_huise));
		all_img.setBackgroundColor(getResources().getColor(R.color.nav_huise));
		wait_img.setBackgroundColor(getResources().getColor(R.color.nav_huise));
		agree_img
				.setBackgroundColor(getResources().getColor(R.color.nav_huise));
		reject_img.setBackgroundColor(getResources()
				.getColor(R.color.nav_huise));
	}

	// ====修改head状态end===========

	// ========================xlist=================================================
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (type == 1) {
				list1.setLoadOver(currentCount1, page_size);// 用于是否加载完了
				adpter1.notifyDataSetChanged();
			} else if (type == 2) {
				list2.setLoadOver(currentCount2, page_size);// 用于是否加载完了
				adpter2.notifyDataSetChanged();
			} else if (type == 3) {
				list3.setLoadOver(currentCount3, page_size); // 用于是否加载完了
				adpter3.notifyDataSetChanged();
			} else if (type == 4) {
				list4.setLoadOver(currentCount4, page_size);// 用于是否加载完了
				adpter4.notifyDataSetChanged();
			}
			onLoad();
		};
	};

	private void onLoad() {
		if (type == 1) {
			list1.stopRefresh();
			list1.stopLoadMore();
			list1.setRefreshTime("刚刚");
		} else if (type == 2) {
			list2.stopRefresh();
			list2.stopLoadMore();
			list2.setRefreshTime("刚刚");
		} else if (type == 3) {
			list3.stopRefresh();
			list3.stopLoadMore();
			list3.setRefreshTime("刚刚");
		} else if (type == 4) {
			list4.stopRefresh();
			list4.stopLoadMore();
			list4.setRefreshTime("刚刚");
		}
	}

	@Override
	public void onRefresh() {
		if (type == 1) {
			pageNumber1 = 1;
			pageNumber = pageNumber1;
			lists1.clear();
			getData();
			// mAdapter.notifyDataSetChanged();
			adpter1 = new UserJianzhitAdapter(MyJianZhiActivity.this, lists1);
			list1.setAdapter(adpter1);
		} else if (type == 2) {
			pageNumber2 = 1;
			pageNumber = pageNumber2;
			lists2.clear();
			getData();
			// mAdapter.notifyDataSetChanged();
			adpter2 = new UserJianzhitAdapter(MyJianZhiActivity.this, lists2);
			list2.setAdapter(adpter2);
		} else if (type == 3) {
			pageNumber3 = 1;
			pageNumber = pageNumber3;
			lists3.clear();
			getData();
			// mAdapter.notifyDataSetChanged();
			adpter3 = new UserJianzhitAdapter(MyJianZhiActivity.this, lists3);
			list3.setAdapter(adpter3);
		} else if (type == 4) {
			pageNumber4 = 1;
			pageNumber = pageNumber4;
			lists4.clear();
			getData();
			// mAdapter.notifyDataSetChanged();
			adpter4 = new UserJianzhitAdapter(MyJianZhiActivity.this, lists4);
			list4.setAdapter(adpter4);
		}
	}

	@Override
	public void onLoadMore() {
		if (type == 1) {
			pageNumber1++;
			pageNumber = pageNumber1;
		} else if (type == 2) {
			pageNumber2++;
			pageNumber = pageNumber2;
		} else if (type == 3) {
			pageNumber3++;
			pageNumber = pageNumber3;
		} else if (type == 4) {
			pageNumber4++;
			pageNumber = pageNumber4;
		}
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
							JSONObject jss = js
									.getJSONObject("MyRequireActivityResponse");
							JSONArray ls = jss.getJSONArray("list");

							if (type == 1) {
								if (ls.length() > 0) {
									nodata_img.setVisibility(View.GONE);
									list1.setVisibility(View.VISIBLE);
									for (int i = 0; i < ls.length(); i++) {
										MyJianZhiModle jianzhi = new MyJianZhiModle();
										jianzhi = (MyJianZhiModle) JsonUtil
												.jsonToBean(
														ls.getJSONObject(i),
														MyJianZhiModle.class);
										lists1.add(jianzhi);
									}
								}
								currentCount1 = ls.length();
							}

							if (type == 2) {
								if (ls.length() > 0) {
									for (int i = 0; i < ls.length(); i++) {
										MyJianZhiModle jianzhi = new MyJianZhiModle();
										jianzhi = (MyJianZhiModle) JsonUtil
												.jsonToBean(
														ls.getJSONObject(i),
														MyJianZhiModle.class);
										lists2.add(jianzhi);
									}
								}
								currentCount2 = ls.length();
							}
							if (type == 3) {
								if (ls.length() > 0) {
									for (int i = 0; i < ls.length(); i++) {
										MyJianZhiModle jianzhi = new MyJianZhiModle();
										jianzhi = (MyJianZhiModle) JsonUtil
												.jsonToBean(
														ls.getJSONObject(i),
														MyJianZhiModle.class);
										lists3.add(jianzhi);
									}
								}
								currentCount3 = ls.length();
							}
							if (type == 4) {
								if (ls.length() > 0) {
									for (int i = 0; i < ls.length(); i++) {
										MyJianZhiModle jianzhi = new MyJianZhiModle();
										jianzhi = (MyJianZhiModle) JsonUtil
												.jsonToBean(
														ls.getJSONObject(i),
														MyJianZhiModle.class);
										lists4.add(jianzhi);
									}
								}
								currentCount4 = ls.length();
							}

							Message msg = handler.obtainMessage();
							msg.what = 20;
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
				map.put("user_id", user_id);
				map.put("page_size", page_size + "");
				map.put("pn", pageNumber + "");
				map.put("type", type + "");
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

}
