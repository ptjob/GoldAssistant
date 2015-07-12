package com.quark.fragment.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.qingmu.jianzhidaren.R;
import com.quark.adapter.MyJianzhiAdapter;
import com.quark.common.JsonUtil;
import com.quark.common.ToastUtil;
import com.quark.common.Url;
import com.quark.company.function.PersonAssessActivity;
import com.quark.company.function.RosterActivity;
import com.quark.guanli.PublishActivity;
import com.quark.jianzhidaren.BaseActivity;
import com.parttime.main.MainTabActivity;
import com.quark.model.MyJianzhi;
import com.quark.model.PublishAvailability;
import com.quark.ui.widget.CustomDialog;
import com.quark.ui.widget.CustomDialogThree;
import com.quark.us.AuthenticationActivity;
import com.thirdparty.alipay.RechargeActivity;

/**
 * 商家端 管理
 * 
 * @ClassName: HomeFragment
 * @Description: TODO
 * @author howe
 * @date 2015-1-16 下午9:28:49
 * 
 */
public class HomeFragmentCompany extends BaseActivity implements
		IXListViewListener {

	private MyJianzhiAdapter mjzAdapter;
	static XListView listView;
	static View findLayout;
	ArrayList<MyJianzhi> jianzhis = new ArrayList<MyJianzhi>();
	int pageNumber = 1;
	int currentCount = 1;
	int page_size = 3;
	private String url;// 获取管理兼职列表
	private String avalible_url;// 是否能发布兼职
	private PublishAvailability availability;// 是否能发布兼职
	private String user_id, city;
	private SharedPreferences sp;

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_home_company);
		sp = getSharedPreferences("jrdr.setting", Context.MODE_PRIVATE);
		user_id = sp.getString("userId", "");
		city = sp.getString("city", "深圳");
		// 当前城市
		url = Url.COMPANY_MyJianzhi_List + "?token="
				+ MainTabActivity.token;
		// 获取是否能发布兼职
		avalible_url = Url.COMPANY_availability + "?token="
				+ MainTabActivity.token;
		listView = (XListView) findViewById(R.id.list1);
		listView.setPullLoadEnable(true);
		mjzAdapter = new MyJianzhiAdapter(HomeFragmentCompany.this, jianzhis);
		listView.setAdapter(mjzAdapter);
		listView.setXListViewListener(this);
		RelativeLayout reLayout = (RelativeLayout) findViewById(R.id.top_title_layout);
		reLayout.setBackgroundColor(getResources().getColor(
				R.color.guanli_common_color));
		setBackButton();

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

	/**
	 * 获取各种状态
	 */
	public void getAvailability() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST,
				avalible_url, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject sd = js
									.getJSONObject("AvailabilityResponse");
							availability = (PublishAvailability) JsonUtil
									.jsonToBean(sd, PublishAvailability.class);
							if (checkStatus()) {
								Intent intent = new Intent();
								intent.setClass(HomeFragmentCompany.this,
										PublishActivity.class);
								startActivity(intent);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showWait(false);
						Toast.makeText(HomeFragmentCompany.this,
								"你的网络不够给力，获取数据失败", Toast.LENGTH_SHORT).show();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("company_id", user_id);
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
	}

	/**
	 * @param str
	 * @param str2
	 */
	private void showAlertDialogPingJia(String str, final String str2,
			final String submintStr, final String activity_id,
			final String totalCount) {

		CustomDialog.Builder builder = new CustomDialog.Builder(
				HomeFragmentCompany.this);
		builder.setMessage(str);
		builder.setTitle(str2);

		builder.setPositiveButton(submintStr,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 到指定评价人员列表
						Intent intent = new Intent();
						intent.setClass(HomeFragmentCompany.this,
								PersonAssessActivity.class);
						intent.putExtra("activity_id", activity_id);
						intent.putExtra("total_num", totalCount);
						startActivity(intent);

					}
				});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();

			}
		});
		builder.create().show();
	}

	/**
	 * @param str
	 * @param str2
	 */
	private void showAlertDialog1(String str, final String str2,
			final String submintStr) {

		CustomDialog.Builder builder = new CustomDialog.Builder(
				HomeFragmentCompany.this);
		builder.setMessage(str);
		builder.setTitle(str2);

		builder.setPositiveButton(submintStr,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (submintStr.equals("去认证")) {
							Intent intent = new Intent();
							intent.setClass(HomeFragmentCompany.this,
									AuthenticationActivity.class);
							startActivity(intent);
						}
						if (submintStr.equals("去评价")) {
							// 到指定评价人员列表

							Intent intent = new Intent();
							intent.setClass(HomeFragmentCompany.this,
									RosterActivity.class);
							startActivity(intent);
						}

					}
				});
		if (!"快一点吧".equals(submintStr)) {
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							dialog.dismiss();

						}
					});
		}
		builder.create().show();
	}

	/**
	 * @param str
	 * @param str2
	 */
	private void showAlertDialog2(String str, final String str2) {

		CustomDialog.Builder builder = new CustomDialog.Builder(
				HomeFragmentCompany.this);
		builder.setMessage(str);
		builder.setTitle(str2);

		builder.setPositiveButton("立即充值",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent intent = new Intent();
						intent.setClass(HomeFragmentCompany.this,
								RechargeActivity.class);
						startActivity(intent);
					}
				});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	/**
	 * @param str
	 * @param str2
	 */
	private void showAlertDialog3(String str, final String str2, String str3) {

		CustomDialogThree.Builder builder = new CustomDialogThree.Builder(
				HomeFragmentCompany.this);
		builder.setMessage(str);
		builder.setTitle(str2);
		builder.setMoney(str3);

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent = new Intent();
				intent.setClass(HomeFragmentCompany.this, PublishActivity.class);
				startActivity(intent);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	/**
	 * 点击发布时判断是否能发布
	 */
	public boolean checkStatus() {
		// 以发布过一条信息 未实名验证 商家注册可以在未实名认证条件下发布一条信息
		if (availability == null) {
			return false;
		} else {
			// 0未提交审核,1审核中,3驳回
			if ((availability.getTotal_count() > 0)
					&& (availability.getCertification() == 0)) {
				showAlertDialog1("您需要完成实名认证，才可以继续发布兼职信息", "温馨提示", "去认证");
				return false;
			}
			// 实名认证正式审核中
			if ((availability.getTotal_count() > 0)
					&& (availability.getCertification() == 1)) {
				showAlertDialog1("您的实名认证正在审核中，审核通过我们将会第一时间通知您", "温馨提示", "快一点吧");
				return false;
			}
			// 实名认证被驳回
			if ((availability.getTotal_count() > 0)
					&& (availability.getCertification() == 3)) {
				showAlertDialog1("您需要完成实名认证，才可以继续发布兼职信息", "温馨提示", "去认证");
				return false;
			}
			// 未评论
			if (availability.getTobe_comment_activity_id() == -1) {
			} else {
				showAlertDialogPingJia(
						"完成" + availability.getTobe_comment_activity_title()
								+ "人员评价后,才可以发布新的招聘信息", "温馨提示", "去评论",
						String.valueOf(availability
								.getTobe_comment_activity_id()),
						String.valueOf(availability.getTotal_count()));
				return false;
			}
			int s = availability.getCharge_count()
					+ availability.getFree_count();
			if (s < 1) {
				showAlertDialog2("您今日已发布过一条免费招聘信息，如需再发布需要付费5元（账户余额"
						+ availability.getMoney() + "元）", "余额不足");
				return false;
			}
			// 已经发布过免费的,本次需要付费发布
			// if (availability.getFree_count() < 1) {
			// if (availability.getCharge_count() >= 1) {
			// showAlertDialog3("您今日已发布过一条免费招聘信息，如需再发布需要付费5元,", "发布兼职",
			// "(账号余额：" + availability.getMoney() + "元)");
			// return false;
			// }
			// }

		}

		return true;
	}

	public void getData() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							// todo表示所有活动未处理数目String
							String todo = js.getString("todo");
							int temp = 0;
							try {
								temp = Integer.parseInt(todo);
							} catch (Exception e) {
								temp = 0;
							}
							Editor edt = sp.edit();
							edt.putInt(ConstantForSaveList.userId + "todo",
									temp);
							edt.commit();

							JSONObject jss = js.getJSONObject("MyActivity");
							JSONArray jsss = jss.getJSONArray("list");
							if (jsss.length() > 0) {
								for (int i = 0; i < jsss.length(); i++) {
									MyJianzhi jianzhi = new MyJianzhi();
									jianzhi = (MyJianzhi) JsonUtil.jsonToBean(
											jsss.getJSONObject(i),
											MyJianzhi.class);
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
						ToastUtil.showShortToast("当前网络状态不好,请检查网络");
						currentCount = 0;
						Message msg = handler.obtainMessage();
						msg.what = 1;
						handler.sendMessage(msg);
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
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	// ========================xlist=================================================
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			listView.setLoadOver(currentCount, page_size);// 用于是否加载完了
			mjzAdapter.notifyDataSetChanged();
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
		mjzAdapter = new MyJianzhiAdapter(HomeFragmentCompany.this, jianzhis);
		listView.setAdapter(mjzAdapter);
	}

	@Override
	public void onLoadMore() {
		pageNumber++;
		getData();
	}

	// ======xlist end============
	@Override
	public void onResume() {
		super.onResume();
		jianzhis.clear();
		pageNumber = 1;
		getData();
	}
}
