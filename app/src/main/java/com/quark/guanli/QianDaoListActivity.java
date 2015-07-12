package com.quark.guanli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.qingmu.jianzhidaren.R;
import com.quark.adapter.QianDaoListAdapter1;
import com.quark.adapter.QianDaoListAdapter2;
import com.quark.common.JsonUtil;
import com.quark.common.ToastUtil;
import com.quark.common.Url;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.jianzhidaren.MainCompanyActivity;
import com.quark.model.QianDaoListBean;

public class QianDaoListActivity extends BaseActivity {

	private ListView listview1, listview2;
	private String url;
	private String activity_id;
	private int all_count;// 发起签到的总次数
	private ArrayList<QianDaoListBean> list = new ArrayList<QianDaoListBean>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qiandaolist);
		list.add(new QianDaoListBean());// 添加list头部
		setBackButton();
		activity_id = getIntent().getStringExtra("activity_id");
		url = Url.COMPANY_ACTIVITY_SIGNLIST + "?token="
				+ MainCompanyActivity.token;
		listview1 = (ListView) findViewById(R.id.list1);
		listview2 = (ListView) findViewById(R.id.list2);
		getData();

	}

	/**
	 * 
	 */
	private void initView() {
		listview1.setAdapter(new QianDaoListAdapter1(QianDaoListActivity.this,
				list));
		setListViewHeightBasedOnChildren(listview1);
		listview2.setAdapter(new QianDaoListAdapter2(QianDaoListActivity.this,
				list, all_count));
		setListViewHeightBasedOnChildren(listview2);
	}

	/**
	 * 获取数据
	 */
	public void getData() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss = js.getJSONObject("responseStatus");
							int status = jss.getInt("status");
							// status 1 成功 sign_count 发起签到总次数
							all_count = jss.getInt("all_count");
							if (status == 1) {
								JSONArray jsss = jss.getJSONArray("list");
								if (jsss.length() > 0) {
									for (int i = 0; i < jsss.length(); i++) {
										QianDaoListBean bean = new QianDaoListBean();
										bean = (QianDaoListBean) JsonUtil
												.jsonToBean(
														jsss.getJSONObject(i),
														QianDaoListBean.class);
										list.add(bean);
										
									}
									// 更新适配器数据
									runOnUiThread(new Runnable() {
										public void run() {
											initView();
										}
									});
								} else {
									ToastUtil.showShortToast("当前尚未有人员签到");
								}
							} else {
								ToastUtil.showShortToast("获取数据失败，请稍后再试");
							}
						} catch (JSONException e) {
							e.printStackTrace();
							ToastUtil.showShortToast("获取数据失败，请稍后再试");
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showWait(false);
						ToastUtil.showShortToast("网络不好，请稍后再试");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("activity_id", activity_id);
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(ConstantForSaveList.DEFAULTRETRYTIME*1000, 1, 1.0f));

	}

	/*
	 * 动态设置ListView组建的高度
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();

		if (listAdapter == null) {

			return;

		}

		int totalHeight = 0;

		for (int i = 0; i < listAdapter.getCount(); i++) {

			View listItem = listAdapter.getView(i, null, listView);

			listItem.measure(0, 0);

			totalHeight += listItem.getMeasuredHeight();

		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();

		params.height = totalHeight

		+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		// params.height += 5;// if without this statement,the listview will be

		// a

		// little short

		// listView.getDividerHeight()获取子项间分隔符占用的高度

		// params.height最后得到整个ListView完整显示需要的高度

		listView.setLayoutParams(params);

	}

	/**
	 * 设置返回按钮
	 */
	public void setBackButton() {

		RelativeLayout topLayout = (RelativeLayout) findViewById(R.id.top_title_layout);
		topLayout.setBackgroundColor(getResources().getColor(
				R.color.guanli_common_color));

		LinearLayout back_lay = (LinearLayout) findViewById(R.id.left);
		back_lay.setVisibility(View.VISIBLE);
		back_lay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView tv = (TextView) findViewById(R.id.title);
		tv.setText("签到记录");
	}
}
