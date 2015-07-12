package com.quark.company.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qingmu.jianzhidaren.R;
import com.quark.adapter.SignAdapter;
import com.quark.common.JsonUtil;
import com.quark.common.ToastUtil;
import com.quark.common.Url;
import com.quark.guanli.QianDaoListActivity;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.jianzhidaren.MainCompanyActivity;
import com.quark.model.RosterModel;
import com.quark.model.SignPersonList;
import com.quark.ui.widget.CustomDialog;
import com.quark.ui.widget.ListViewForScrollView;
import com.quark.utils.NetWorkCheck;

/**
 * 管理员：活动人员列表
 * 
 * @author Administrator
 * 
 */
public class FullStarffedActivity extends BaseActivity {

	private ImageView im;
	// 参数
	SignAdapter allAdapter;
	SignAdapter nosignAdapter;
	SignAdapter signAdapter;
	private ListViewForScrollView list_allView;
	private ListViewForScrollView list_nosignView;
	private ListViewForScrollView list_signView;
	private String activity_id;
	private String company_id;
	private String url, sign_url, signUpList_url, company_cancel_required_url;
	private String activity_name, total;
	ArrayList<RosterModel> list_all = new ArrayList<RosterModel>();
	ArrayList<RosterModel> list_sign = new ArrayList<RosterModel>();
	ArrayList<RosterModel> list_nosign = new ArrayList<RosterModel>();
	private boolean havedPSign = false; // 是否有人签到 签到显示不同的页面
	private int signPerson = 0;// 签到的人生
	private int nosignPerson = 0;// 未签到人生
	private int manNumber = 0;
	private int womanNumber = 0;
	ArrayList<SignPersonList> signPersonList = new ArrayList<SignPersonList>();
	public int status = 0;
	@ViewInject(R.id.sign)
	ScrollView signView;
	@ViewInject(R.id.nosign)
	ScrollView nosignView;
	@ViewInject(R.id.men_num)
	TextView men_num;
	@ViewInject(R.id.woman_num)
	TextView woman_num;
	@ViewInject(R.id.total_num)
	TextView total_num;
	@ViewInject(R.id.total_nosign)
	TextView total_nosign;
	@ViewInject(R.id.total_sign)
	TextView total_sign;
	LinearLayout codeLayout;
	LinearLayout penLayout;
	SharedPreferences sp;
	private Button qiandaoRecordBtn;
	private String sign_msg;// 发起签到时弹出的提示...当前是第n次刷新。。。

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_person);
		ViewUtils.inject(this);
		activity_id = getIntent().getStringExtra("activity_id");
		activity_name = getIntent().getStringExtra("activity_name");
		sp = getSharedPreferences("jrdr.setting", Activity.MODE_PRIVATE);
		company_id = sp.getString("userId", "");
		RelativeLayout topLayout = (RelativeLayout) findViewById(R.id.top_title_layout);
		topLayout.setBackgroundColor(getResources().getColor(
				R.color.guanli_common_color));
		setBackButton();
		url = Url.COMPANY_activityFaceBook + "?token="
				+ MainCompanyActivity.token;
		sign_url = Url.COMPANY_sign + "?token=" + MainCompanyActivity.token;
		signUpList_url = Url.COMPANY_signUpList + "?token="
				+ MainCompanyActivity.token;
		company_cancel_required_url = Url.COMPANY_CANCEL_REQUIRED + "?token="
				+ MainCompanyActivity.token;
		setRightImage(R.id.right, listener);
		// 处理数据
		//
		im = (ImageView) findViewById(R.id.right);
		Resources res = this.getResources();
		Drawable btnDrawable = res.getDrawable(R.drawable.nav_btn_refresh);
		im.setBackgroundDrawable(btnDrawable);
		setTopTitle(activity_name);
		// carson 取消onresume 中刷新数据
		getData();
		qiandaoRecordBtn = (Button) findViewById(R.id.preview_qiandao_record_btn);
		qiandaoRecordBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(FullStarffedActivity.this,
						QianDaoListActivity.class);
				intent.putExtra("activity_id", activity_id);
				startActivity(intent);
			}
		});
	}

	private void getData() {
		list_all.clear();
		list_sign.clear();
		list_nosign.clear();
		havedPSign = false;
		signPerson = 0;
		nosignPerson = 0;
		manNumber = 0;
		womanNumber = 0;
		showWait(true);
		StringRequest stringRequest = new StringRequest(Method.POST,
				signUpList_url, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss = js
									.getJSONObject("ActivitySignList");
							sign_msg = jss.getString("msg");// 获取签到提示
							JSONArray jsss = jss.getJSONArray("list");
							if (jsss.length() > 0) {
								for (int i = 0; i < jsss.length(); i++) {
									RosterModel rt = new RosterModel();
									rt = (RosterModel) JsonUtil.jsonToBean(
											jsss.getJSONObject(i),
											RosterModel.class);
									list_all.add(rt);
									if (rt.getSex() == 1) {
										manNumber++;
									} else if (rt.getSex() == 0) {
										womanNumber++;
									}
									if (rt.getSign() == 1) { // 签到
										havedPSign = true;
										list_sign.add(rt);
										signPerson++;
									} else {
										list_nosign.add(rt);
										nosignPerson++;
									}

									if (havedPSign) {// 显示有人签到页面
										initSignView();
									} else {
										initNoSignView();
									}
								}
							}
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
	}

	protected void initNoSignView() {
		// 显示nosign scrollView
		signView.setVisibility(View.GONE);
		nosignView.setVisibility(View.VISIBLE);

		men_num.setText(manNumber + "人");
		woman_num.setText(womanNumber + "人");
		total_num.setText(list_all.size() + "人");

		list_allView = (ListViewForScrollView) findViewById(R.id.list_view1);
		allAdapter = new SignAdapter(this, list_all);
		list_allView.setAdapter(allAdapter);
		registerForContextMenu(list_allView);// 注册listview长按监听

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v == list_allView) {
			// 取消录取人员
			getMenuInflater().inflate(R.menu.cancel_luqu, menu);
		}
	}

	@Override
	public boolean onContextItemSelected(final MenuItem item) {
		if (item.getItemId() == R.id.cancel_luqu) {
			// 取消录取人员
			if (list_all != null && list_all.size() > 0) {
				showWait(true);
				StringRequest stringRequest = new StringRequest(Method.POST,
						company_cancel_required_url,
						new Response.Listener<String>() {
							@Override
							public void onResponse(String response) {
								showWait(false);
								try {
									JSONObject js = new JSONObject(response);
									JSONObject jss = js
											.getJSONObject("ResponseStatus");
									int status = jss.getInt("status");
									final String msg = jss.getString("msg");
									if (status == 1) {
										// 取消成功
										runOnUiThread(new Runnable() {
											@Override
											public void run() {
												showToast(msg);
												list_all.remove(((AdapterContextMenuInfo) item
														.getMenuInfo()).position);
												allAdapter = new SignAdapter(
														FullStarffedActivity.this,
														list_all);
												list_allView
														.setAdapter(allAdapter);

											}
										});

									} else if (status == 2) {
										// 取消失败
										showToast(msg);
									}

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
						int c_user_id = list_all
								.get(((AdapterContextMenuInfo) item
										.getMenuInfo()).position).getUser_id();
						map.put("user_id", String.valueOf(c_user_id));
						map.put("activity_id", activity_id);
						return map;
					}
				};
				queue.add(stringRequest);
				stringRequest.setRetryPolicy(new DefaultRetryPolicy(
						ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
			}

		}
		return super.onContextItemSelected(item);
	}

	protected void initSignView() {
		// 显示sign scrollView
		signView.setVisibility(View.VISIBLE);
		nosignView.setVisibility(View.GONE);

		total_nosign.setText(nosignPerson + "人");
		total_sign.setText(signPerson + "人");

		list_nosignView = (ListViewForScrollView) findViewById(R.id.list_view2);
		nosignAdapter = new SignAdapter(this, list_nosign);
		list_nosignView.setAdapter(nosignAdapter);
		list_signView = (ListViewForScrollView) findViewById(R.id.list_view3);
		signAdapter = new SignAdapter(this, list_sign);
		list_signView.setAdapter(signAdapter);

	}

	/*
	 * 点击头顶刷新
	 */
	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (havedPSign) {
				if (NetWorkCheck.isOpenNetwork(FullStarffedActivity.this)) {
					if (sign_msg != null && !"".equals(sign_msg.trim())) {
						showAlertDialog(sign_msg, "温馨提示");
					} else {
						showAlertDialog("当前是第3次签到，保存后将执行下次签到。确认进行下次签到？", "温馨提示");
					}
				} else {
					ToastUtil.showShortToast("当前网络不好,请稍后尝试。");
				}
			} else {
				ToastUtil.showShortToast("当前无人签到,无需刷新");
				// getData();
			}

		}
	};

	// 二维码签到刷新
	protected void Refresh() {
		showWait(true);
		StringRequest stringRequest = new StringRequest(Method.POST, sign_url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						getData();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						showWait(false);
						showToast("刷新失败,请重试");
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> map = new HashMap<String, String>();
				map.put("activity_id", activity_id);
				map.put("company_id", company_id);
				return map;
			}
		};
		queue.add(stringRequest);
	}

	public void showAlertDialog(String str, final String str2) {

		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(str);
		builder.setTitle(str2);

		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Refresh();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// carson
		// getData();//获取数据后判断显示哪个界面
	}
}
