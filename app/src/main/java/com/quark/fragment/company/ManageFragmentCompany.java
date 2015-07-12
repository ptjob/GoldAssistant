package com.quark.fragment.company;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.broker.BrokerActivity;
import com.carson.broker.JiedanActivity;
import com.carson.broker.MyFunsActivity;
import com.carson.constant.ConstantForSaveList;
import com.droid.carson.Activity01;
import com.qingmu.jianzhidaren.R;
import com.quark.common.JsonUtil;
import com.quark.common.ToastUtil;
import com.quark.common.Url;
import com.quark.company.function.PersonAssessActivity;
import com.quark.company.function.RosterActivity;
import com.quark.guanli.PublishActivity;
import com.quark.jianzhidaren.MainCompanyActivity;
import com.quark.model.MyJianzhi;
import com.quark.model.PublishAvailability;
import com.quark.ui.widget.CustomDialog;
import com.quark.us.AuthenticationActivity;
import com.quark.utils.NetWorkCheck;
import com.thirdparty.alipay.RechargeActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ManageFragmentCompany extends BaseFragment implements
		OnClickListener {
	private String url, avalible_url;// 获取首页红点数、 是否能发布兼职
	private PublishAvailability availability;// 是否能发布兼职
	private String user_id, city;
	private static SharedPreferences sp;
	private TextView cityTv;// 城市框
	private RelativeLayout home_page_city_relayout;
	private ReceiveBroadCast receiveBroadCast;// 注册广播更换城市数据
	private RelativeLayout reLayout1, reLayout2, reLayout3, reLayout4,
			reLayout5;// 排行榜,我的粉丝,接受活动,发布兼职,活动管理
	private TextView paihangbang_mingci_tv, my_funs_num_tv;
	private static ImageView accept_act_number_imv;// 接单新活动红点提示
	private static TextView manage_act_number_tv;

	public static ManageFragmentCompany newInstance(String param1, String param2) {
		ManageFragmentCompany fragment = new ManageFragmentCompany();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public ManageFragmentCompany() {

	}

	@Override
	public void onAttach(Activity activity) {
		/** 注册广播 */
		receiveBroadCast = new ReceiveBroadCast();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.carson.company.changgecity");
		activity.registerReceiver(receiveBroadCast, filter);
		super.onAttach(activity);
	}

	@Override
	public void onDestroy() {
		if (receiveBroadCast != null) {
			getActivity().unregisterReceiver(receiveBroadCast);
		}
		super.onDestroy();
	}

	/**
	 * 注册广播用于处理切换城市位置更新兼职信息
	 */
	class ReceiveBroadCast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String changecity = intent.getExtras().getString("changgecity");
			city = changecity;
			cityTv.setText(city);

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 100) {
			switch (resultCode) {
			case android.app.Activity.RESULT_OK:
				// String province = data.getExtras().getString("province");
				city = data.getExtras().getString("city");
				if ((city != null) && (!city.equals(""))) {
					cityTv.setText(city);
					// 跟原来保存的城市对比
					String old_city = sp.getString("city", "深圳");
					if (old_city.equals(city)) {
						ConstantForSaveList.change_city = false;
					} else {
						ConstantForSaveList.change_city = true;
					}
					Editor edit = sp.edit();
					edit.putString("city", city);
					edit.commit();

					// 切换到指定城市,访问后台传输城市
					String cityUrl;
					cityUrl = Url.CHANGE_CITY_CUSTOM + "?token="
							+ MainCompanyActivity.token;

					StringRequest request = new StringRequest(
							Request.Method.POST, cityUrl,
							new Response.Listener<String>() {
								@Override
								public void onResponse(String response) {
								}
							}, new Response.ErrorListener() {
								@Override
								public void onErrorResponse(
										VolleyError volleyError) {
								}
							}) {
						@Override
						protected Map<String, String> getParams()
								throws AuthFailureError {
							Map<String, String> map = new HashMap<String, String>();
							map.put("company_id", user_id);
							map.put("city", city);
							return map;
						}
					};
					queue.add(request);
					request.setRetryPolicy(new DefaultRetryPolicy(
							ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1,
							1.0f));

					// 获取区

				}
			default:
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_manage_company,
				container, false);
		sp = getActivity().getSharedPreferences("jrdr.setting",
				Context.MODE_PRIVATE);
		user_id = sp.getString("userId", "");
		city = sp.getString("city", "深圳");
		// 4个显示数字的tv
		paihangbang_mingci_tv = (TextView) view
				.findViewById(R.id.paihangbang_mingci_tv);
		my_funs_num_tv = (TextView) view.findViewById(R.id.my_funs_num_tv);
		accept_act_number_imv = (ImageView) view
				.findViewById(R.id.accept_act_number_tv);
		manage_act_number_tv = (TextView) view
				.findViewById(R.id.manage_act_number_tv);
		// 当前城市
		cityTv = (TextView) view.findViewById(R.id.home_page_city);
		cityTv.setText(city);
		home_page_city_relayout = (RelativeLayout) view
				.findViewById(R.id.home_page_city_relayout);
		home_page_city_relayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				// 传值当前定位城市
				intent.putExtra("citylist_city",
						sp.getString("dingweicity", "定位失败"));
				intent.setClass(getActivity(), Activity01.class);
				startActivityForResult(intent, 100);

			}
		});
		// 获取首页状态
		url = Url.BROKER_MAIN_PAGE + "?token=" + MainCompanyActivity.token;
		// 获取是否能发布兼职
		avalible_url = Url.COMPANY_availability + "?token="
				+ MainCompanyActivity.token;
		// 隐藏右侧发布按钮
		LinearLayout right_layout = (LinearLayout) view
				.findViewById(R.id.right_layout);
		right_layout.setVisibility(View.GONE);
		// 头部设置成灰色
		RelativeLayout reLayout = (RelativeLayout) view
				.findViewById(R.id.home_common_guangchang_relayout);
		reLayout.setBackgroundColor(getResources().getColor(
				R.color.guanli_common_color));
		// 初始化5大layout
		reLayout1 = (RelativeLayout) view.findViewById(R.id.relayout_1);
		reLayout2 = (RelativeLayout) view.findViewById(R.id.relayout_2);
		reLayout3 = (RelativeLayout) view.findViewById(R.id.relayout_3);
		reLayout4 = (RelativeLayout) view.findViewById(R.id.relayout_4);
		reLayout5 = (RelativeLayout) view.findViewById(R.id.relayout_5);
		reLayout1.setOnClickListener(this);
		reLayout2.setOnClickListener(this);
		reLayout3.setOnClickListener(this);
		reLayout4.setOnClickListener(this);
		reLayout5.setOnClickListener(this);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		getData();
	}

	/**
	 * 获取首页的信息
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
							if (status == 1) {
								// todo表示所有活动未处理数目String
								int todo = jss.getInt("todo");
								int rank = jss.getInt("rank");
								int fans = jss.getInt("fans");
								Editor edt = sp.edit();
								edt.putInt(ConstantForSaveList.userId + "todo",
										todo);
								edt.putInt(user_id + "rank", rank);
								edt.putInt(user_id + "fans", fans);
								edt.commit();
								handler.sendEmptyMessage(1);
							}
						} catch (JSONException e) {
							e.printStackTrace();
							handler.sendEmptyMessage(1);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showWait(false);
						ToastUtil.showShortToast("当前网络状态不好,请检查网络");
						handler.sendEmptyMessage(1);

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
	 * 更新活动管理、开始接单红点
	 */
	public static void updateTodoAndjieDan() {
		int todo = sp.getInt(ConstantForSaveList.userId + "todo", 0);
		boolean typeFlag = sp.getBoolean(ConstantForSaveList.userId + "type",
				false);
		if (todo > 0) {
			manage_act_number_tv.setText(String.valueOf(todo));
			manage_act_number_tv.setVisibility(View.VISIBLE);
		} else {
			manage_act_number_tv.setVisibility(View.INVISIBLE);
		}
		// 是否显示接单红点
		if (typeFlag) {
			accept_act_number_imv.setVisibility(View.VISIBLE);
		} else {
			accept_act_number_imv.setVisibility(View.INVISIBLE);
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				int rank = sp.getInt(user_id + "rank", 1);
				paihangbang_mingci_tv.setText("第" + rank + "名");
				int fans = sp.getInt(user_id + "fans", 1);
				my_funs_num_tv.setText(String.valueOf(fans));
				int todo = sp.getInt(ConstantForSaveList.userId + "todo", 0);
				if (todo > 0) {
					manage_act_number_tv.setText(String.valueOf(todo));
					manage_act_number_tv.setVisibility(View.VISIBLE);
				} else {
					manage_act_number_tv.setVisibility(View.INVISIBLE);
				}
				break;

			default:
				break;
			}

		};
	};

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
								if (getActivity() != null) {
									Intent intent = new Intent();
									intent.setClass(getActivity(),
											PublishActivity.class);
									startActivity(intent);
								}
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showWait(false);
						Toast.makeText(getActivity(), "你的网络不够给力，获取数据失败",
								Toast.LENGTH_SHORT).show();
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

		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
		builder.setMessage(str);
		builder.setTitle(str2);

		builder.setPositiveButton(submintStr,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 到指定评价人员列表
						Intent intent = new Intent();
						intent.setClass(getActivity(),
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

		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
		builder.setMessage(str);
		builder.setTitle(str2);

		builder.setPositiveButton(submintStr,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (submintStr.equals("去认证")) {
							Intent intent = new Intent();
							intent.setClass(getActivity(),
									AuthenticationActivity.class);
							startActivity(intent);
						}
						if (submintStr.equals("去评价")) {
							// 到指定评价人员列表

							Intent intent = new Intent();
							intent.setClass(getActivity(), RosterActivity.class);
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

		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
		builder.setMessage(str);
		builder.setTitle(str2);

		builder.setPositiveButton("立即充值",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent intent = new Intent();
						intent.setClass(getActivity(), RechargeActivity.class);
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

		}

		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.relayout_1:
			if (getActivity() != null) {
				if (NetWorkCheck.isOpenNetwork(getActivity()))
					startActivity(new Intent(getActivity(),
							BrokerActivity.class));
				else
					ToastUtil.showShortToast("网络连接不给力，请稍后!");
			}
			break;
		case R.id.relayout_2:
			if (getActivity() != null) {
				if (NetWorkCheck.isOpenNetwork(getActivity()))
					startActivity(new Intent(getActivity(),
							MyFunsActivity.class).putExtra("funs",
							sp.getInt(user_id + "fans", 1)));
				else
					ToastUtil.showShortToast("网络连接不给力，请稍后!");
			}
			break;
		case R.id.relayout_3:
			if (getActivity() != null) {
				if (NetWorkCheck.isOpenNetwork(getActivity())) {
					Editor edt = sp.edit();
					edt.putBoolean(ConstantForSaveList.userId + "type", false);
					edt.commit();
					accept_act_number_imv.setVisibility(View.INVISIBLE);
					startActivity(new Intent(getActivity(),
							JiedanActivity.class));
				} else
					ToastUtil.showShortToast("网络连接不给力，请稍后!");
			}
			break;
		case R.id.relayout_4:
			getAvailability();
			break;
		case R.id.relayout_5:
			if (getActivity() != null) {
				if (NetWorkCheck.isOpenNetwork(getActivity()))
					startActivity(new Intent(getActivity(),
							HomeFragmentCompany.class));
				else
					ToastUtil.showShortToast("网络连接不给力，请稍后!");
			}
			break;

		default:
			break;
		}

	}
}
