package com.quark.guanli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
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
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qingmu.jianzhidaren.R;
import com.quark.adapter.BaomingAdapter;
import com.quark.common.JsonUtil;
import com.quark.common.ToastUtil;
import com.quark.common.Url;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.jianzhidaren.MainCompanyActivity;
import com.quark.model.BaomingList;

/**
 * 
 * @ClassName: BaomingListActivity
 * @Description: 报名列表
 * @author howe
 * @date 2015-1-29 下午12:04:35
 * 
 */
@SuppressLint("NewApi")
public class BaomingListActivity extends BaseActivity {
	private String url;
	private BaomingAdapter bmAdapter;
	private BaomingAdapter noseeAdapter;
	private BaomingAdapter passAdapter;
	ArrayList<BaomingList> allPerson = new ArrayList<BaomingList>();
	ArrayList<BaomingList> noseePerson = new ArrayList<BaomingList>();
	ArrayList<BaomingList> passPerson = new ArrayList<BaomingList>();
	int man_num = 0;// 已录取男人数量
	int pageNumber = 1;
	int currentCount = 1;
	int page_size = 3;
	ListView allList;
	ListView noseeList;
	ListView passList;
	private String activity_id;
	@ViewInject(R.id.all)
	TextView allView;
	@ViewInject(R.id.nosee)
	TextView noseeView;
	@ViewInject(R.id.pass)
	TextView passView;
	@ViewInject(R.id.tip)
	TextView tip;
	private String title;
	String female_count, male_count;// 招人需要的男女人数限制
	boolean fromNotification = false;
	private SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.company_baoming_list);
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		RelativeLayout topLayout = (RelativeLayout) findViewById(R.id.top_title_layout);
		LinearLayout topBaomingLayout = (LinearLayout) findViewById(R.id.top_baoming_layout);
		topLayout.setBackgroundColor(getResources().getColor(
				R.color.guanli_common_color));
		topBaomingLayout.setBackgroundColor(getResources().getColor(
				R.color.guanli_common_color));
		female_count = (String) getValue4Intent("female_count");
		male_count = (String) getValue4Intent("male_count");
		fromNotification = (Boolean) getValue4Intent("fromNotification");
		ViewUtils.inject(this);
		setBackButton();
		title = (String) getValue4Intent("title");
		url = Url.COMPANY_checkApply + "?token=" + MainCompanyActivity.token;
		activity_id = (String) getValue4Intent("activity_id");
		allList = (ListView) findViewById(R.id.list1);
		noseeList = (ListView) findViewById(R.id.list2);
		passList = (ListView) findViewById(R.id.list3);
		LinearLayout right_layout = (LinearLayout) findViewById(R.id.right_layout);
		right_layout.setOnClickListener(refleshListener);
	}

	OnClickListener refleshListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			getData();
		}
	};

	public void getData() {
		allPerson.removeAll(allPerson);
		noseePerson.removeAll(noseePerson);
		passPerson.removeAll(passPerson);
		man_num = 0;
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONArray jall = js.getJSONArray("all");
							JSONArray jnosee = js.getJSONArray("uncheck");
							JSONArray jchecked = js.getJSONArray("checked");

							if (jall.length() > 0) {
								for (int i = 0; i < jall.length(); i++) {
									BaomingList jianzhi = new BaomingList();
									jianzhi = (BaomingList) JsonUtil
											.jsonToBean(jall.getJSONObject(i),
													BaomingList.class);
									allPerson.add(jianzhi);
								}
							}

							if (jnosee.length() > 0) {
								for (int i = 0; i < jnosee.length(); i++) {
									BaomingList jianzhi = new BaomingList();
									jianzhi = (BaomingList) JsonUtil
											.jsonToBean(
													jnosee.getJSONObject(i),
													BaomingList.class);
									noseePerson.add(jianzhi);
								}
							}
							if (jchecked.length() > 0) {
								for (int i = 0; i < jchecked.length(); i++) {
									BaomingList jianzhi = new BaomingList();
									jianzhi = (BaomingList) JsonUtil
											.jsonToBean(
													jchecked.getJSONObject(i),
													BaomingList.class);
									passPerson.add(jianzhi);
								}
							}

							if (allPerson.size() > 0) {
								allView.setText("全部  " + allPerson.size());
								allList.setVisibility(View.VISIBLE);
								tip.setVisibility(View.GONE);
							} else {
								allView.setText("全部  " + 0);
								tip.setVisibility(View.VISIBLE);
							}
							if (noseePerson != null) {
								noseeView.setText("未处理  " + noseePerson.size());
							} else {
								noseeView.setText("未处理  " + 0);
							}
							if (passPerson != null) {
								passView.setText("已录取  " + passPerson.size());
							} else {
								passView.setText("已录取  " + 0);
							}

							if (passPerson != null) {
								if (passPerson.size() > 0) {
									for (int i = 0; i < passPerson.size(); i++) {
										if (passPerson.get(i).getSex() == 1) {
											man_num++;// 如果已录取一个男的,加1
										}
									}
								}
							}

							bmAdapter = new BaomingAdapter(
									BaomingListActivity.this, allPerson);
							allList.setAdapter(bmAdapter);
							// 构造传过去的id数组

							// all点击效果
							allList.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(
										AdapterView<?> adapterView, View view,
										int position, long id) {
									Intent intent = new Intent();
									intent.setClass(BaomingListActivity.this,
											ResumeScanActivity.class);
									intent.putExtra("userId",
											allPerson.get(position)
													.getUser_id() + "");
									intent.putExtra("activity_id", activity_id
											+ "");
									intent.putExtra("title", title);
									intent.putExtra("currentPosition", position
											+ "");
									Bundle bundle = new Bundle();
									bundle.putSerializable("userIds", allPerson);
									bundle.putBoolean("showtitile", true);
									bundle.putInt("man_num", man_num);
									bundle.putInt("passpersonsize",
											passPerson.size());
									bundle.putString("female_count",
											female_count);
									bundle.putString("male_count", male_count);
									intent.putExtras(bundle);
									startActivity(intent);
								}
							});

							noseeAdapter = new BaomingAdapter(
									BaomingListActivity.this, noseePerson);
							noseeList.setAdapter(noseeAdapter);
							// nosee点击效果
							noseeList
									.setOnItemClickListener(new OnItemClickListener() {
										@Override
										public void onItemClick(
												AdapterView<?> adapterView,
												View view, int position, long id) {
											Intent intent = new Intent();
											intent.setClass(
													BaomingListActivity.this,
													ResumeScanActivity.class);
											intent.putExtra("userId",
													noseePerson.get(position)
															.getUser_id() + "");
											intent.putExtra("activity_id",
													activity_id + "");
											intent.putExtra("title", title);
											intent.putExtra("currentPosition",
													position + "");
											Bundle bundle = new Bundle();
											bundle.putSerializable("userIds",
													noseePerson);
											bundle.putBoolean("showtitile",
													true);
											bundle.putInt("man_num", man_num);
											bundle.putInt("passpersonsize",
													passPerson.size());
											bundle.putString("female_count",
													female_count);
											bundle.putString("male_count",
													male_count);
											intent.putExtras(bundle);
											startActivity(intent);
										}
									});
							passAdapter = new BaomingAdapter(
									BaomingListActivity.this, passPerson);
							passList.setAdapter(passAdapter);
							// pass点击效果
							passList.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(
										AdapterView<?> adapterView, View view,
										int position, long id) {
									Intent intent = new Intent();
									intent.setClass(BaomingListActivity.this,
											ResumeScanActivity.class);
									intent.putExtra("userId",
											passPerson.get(position)
													.getUser_id() + "");
									intent.putExtra("activity_id", activity_id
											+ "");
									intent.putExtra("title", title);
									intent.putExtra("currentPosition", position
											+ "");
									Bundle bundle = new Bundle();
									bundle.putSerializable("userIds",
											passPerson);
									bundle.putBoolean("showtitile", true);
									bundle.putInt("passpersonsize",
											passPerson.size());
									bundle.putInt("man_num", man_num);
									bundle.putString("female_count",
											female_count);
									bundle.putString("male_count", male_count);
									intent.putExtras(bundle);
									startActivity(intent);
								}
							});
							if (fromNotification) {
								showNosee(noseeView);
							} else {
								showAll(allView);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showWait(false);
						ToastUtil.showShortToast("当前网络状态不好,请检查网络");
						if (fromNotification) {
							showNosee(noseeView);
						} else {
							showAll(allView);
						}
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("activity_id", activity_id + "");
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	@OnClick(R.id.all)
	public void showAll(View v) {
		setstatus(1);
		allList.setVisibility(View.VISIBLE);
		noseeList.setVisibility(View.GONE);
		passList.setVisibility(View.GONE);
		if (allPerson.size() > 0) {
			tip.setVisibility(View.GONE);
		} else {
			tip.setVisibility(View.VISIBLE);
		}
	}

	@OnClick(R.id.pass)
	public void showPass(View v) {
		setstatus(2);
		allList.setVisibility(View.GONE);
		noseeList.setVisibility(View.GONE);
		passList.setVisibility(View.VISIBLE);
		if (passPerson.size() > 0) {
			tip.setVisibility(View.GONE);
		} else {
			tip.setVisibility(View.VISIBLE);
		}
	}

	@OnClick(R.id.nosee)
	public void showNosee(View v) {
		setstatus(3);
		allList.setVisibility(View.GONE);
		noseeList.setVisibility(View.VISIBLE);
		passList.setVisibility(View.GONE);
		if (noseePerson.size() > 0) {
			tip.setVisibility(View.GONE);
		} else {
			tip.setVisibility(View.VISIBLE);
		}
	}

	public void setstatus(int i) {
		Resources res = getResources();
		if (i == 1) {// 全部
			allView.setBackground(res.getDrawable(R.drawable.bord_baomingright));
			allView.setTextColor(res.getColor(R.color.guanli_common_color));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				noseeView.setBackground(null);
				passView.setBackground(null);
			} else {
				passView.setBackgroundDrawable(null);
				noseeView.setBackgroundDrawable(null);
			}
			noseeView.setTextColor(res.getColor(R.color.body_color));
			passView.setTextColor(res.getColor(R.color.body_color));
		} else if (i == 2) {// 确定
			allView.setTextColor(res.getColor(R.color.body_color));
			noseeView.setTextColor(res.getColor(R.color.body_color));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				allView.setBackground(null);
				noseeView.setBackground(null);
				passView.setBackground(res
						.getDrawable(R.drawable.bord_baomingmind));
			} else {
				noseeView.setBackgroundDrawable(null);
				allView.setBackgroundDrawable(null);
				passView.setBackgroundDrawable(res
						.getDrawable(R.drawable.bord_baomingmind));
			}
			passView.setTextColor(res.getColor(R.color.guanli_common_color));
		} else if (i == 3) {// 未查看
			allView.setTextColor(res.getColor(R.color.body_color));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				allView.setBackground(null);
				noseeView.setBackground(res
						.getDrawable(R.drawable.bord_baomingleft));
				passView.setBackground(null);
			} else {
				allView.setBackgroundDrawable(null);
				passView.setBackgroundDrawable(null);
				noseeView.setBackgroundDrawable(res
						.getDrawable(R.drawable.bord_baomingleft));
			}
			noseeView.setTextColor(res.getColor(R.color.guanli_common_color));
			passView.setTextColor(res.getColor(R.color.body_color));
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		fromNotification = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		getData();
	}
}
