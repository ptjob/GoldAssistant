package com.quark.guangchang;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.easemob.chatuidemo.activity.ChatActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qingmu.jianzhidaren.R;
import com.quark.common.JsonUtil;
import com.quark.common.ToastUtil;
import com.quark.common.Url;
import com.quark.jianzhidaren.BaseActivity;
import com.parttime.main.MainTabActivity;
import com.quark.model.AllJianzhiDetail;
import com.quark.quanzi.UserInfo;
import com.quark.setting.TousuActivity;
import com.quark.share.ShareModel;
import com.quark.share.SharePopupWindow;
import com.quark.ui.widget.CustomDialog;
import com.quark.us.HowRaiseAuthorityActivity;
import com.quark.us.LocalCarouselActivity;
import com.quark.us.MyResumeActivity;
import com.quark.utils.NetWorkCheck;
import com.quark.utils.Util;
import com.umeng.analytics.MobclickAgent;

/**
 * 广场-》活动详细
 * 
 * @author cluo
 * 
 */
@SuppressLint("NewApi")
public class ActivityDetialActivity extends BaseActivity implements
		PlatformActionListener, Callback {

	// 分享的内容
	private String imageurl = Url.GETPIC + "pop_share_btn_jz.png";
	private StringBuffer shareTitle = new StringBuffer();
	private StringBuffer shareText = new StringBuffer();
	private String url = "";
	private RelativeLayout mini_title;
	private String collectUrl, cancelCollectUrl;// 收藏、取消收藏
	private SharePopupWindow share;
	AllJianzhiDetail jianzhi = new AllJianzhiDetail();
	@ViewInject(R.id.jz_type)
	TextView jz_type;
	@ViewInject(R.id.jz_company)
	TextView jz_company;
	@ViewInject(R.id.jz_title)
	TextView jz_title;
	@ViewInject(R.id.jz_pay)
	TextView jz_pay;
	@ViewInject(R.id.jz_pay_type)
	TextView jz_pay_type;
	@ViewInject(R.id.jz_publish_time)
	TextView jz_publish_time;
	@ViewInject(R.id.jz_shijian)
	TextView jz_shijian;
	@ViewInject(R.id.work_zone)
	TextView work_zone;
	@ViewInject(R.id.jz_jieshuan_type)
	TextView jz_jieshuan_type;
	@ViewInject(R.id.jz_worker_number)
	TextView jz_worker_number;
	@ViewInject(R.id.jz_layout_height)
	LinearLayout jz_layout_height;
	@ViewInject(R.id.jz_heigh)
	TextView jz_heigh;
	@ViewInject(R.id.jz_layout_shoe)
	LinearLayout jz_layout_shoe;
	@ViewInject(R.id.jz_shoe)
	TextView jz_shoe;
	@ViewInject(R.id.jz_layout_close)
	LinearLayout jz_layout_close;
	@ViewInject(R.id.jz_close)
	TextView jz_close;
	@ViewInject(R.id.jz_layout_sanwei)
	LinearLayout jz_layout_sanwei;
	@ViewInject(R.id.jz_sanwei)
	TextView jz_sanwei;
	@ViewInject(R.id.jz_health_layout)
	LinearLayout jz_health_layout;
	@ViewInject(R.id.jz_health_card)
	TextView jz_health_card;
	@ViewInject(R.id.jz_layout_language)
	LinearLayout jz_layout_language;
	@ViewInject(R.id.jz_language)
	TextView jz_language;
	@ViewInject(R.id.jz_addressdetail)
	TextView jz_addressdetail;
	@ViewInject(R.id.jz_work_info)
	TextView jz_work_info;
	@ViewInject(R.id.baomingshu)
	TextView baomingshu;
	@ViewInject(R.id.manage)
	TextView manage;
	@ViewInject(R.id.baoming)
	TextView baoming;
	@ViewInject(R.id.baomingnum)
	Button baomingnum;
	@ViewInject(R.id.bootm_layout)
	LinearLayout bootm_layout;// 底部报名框,若是商家通过群消息进来时隐藏
	@ViewInject(R.id.totalNumber)
	TextView totalNumber;
	@ViewInject(R.id.yescount)
	TextView yescount;
	@ViewInject(R.id.nosee)
	TextView nosee;
	@ViewInject(R.id.number)
	TextView number;
	@ViewInject(R.id.number1)
	TextView number1;
	@ViewInject(R.id.number2)
	TextView number2;
	@ViewInject(R.id.contactshangjia)
	TextView contactshangjia;
	@ViewInject(R.id.jobstatuts)
	ImageView jobstatuts;
	@ViewInject(R.id.act_split_iv)
	ImageView actSplitIv;
	// 保证金、超级
	@ViewInject(R.id.act_baozhengjin_tv)
	TextView baozhengjinTv;
	@ViewInject(R.id.act_chaoji_tv)
	TextView chaojiTv;
	@ViewInject(R.id.activity_tousu_imv)
	ImageView tousu_imv;
	@ViewInject(R.id.activity_collect_imv)
	ImageView collectedImv;// 收藏活动

	private String activity_id;
	private String refreshUrl;
	private String crUrl;
	private String modify;
	private String modifyCommit;
	private String user_id;
	private int pay = 0;
	private String dataurl;
	private String applyUrl;
	private String cancelUrl;
	private String sendUrl;
	private String sendMSMUrl;
	private String sendEmailUrl;
	private String tips;
	private boolean cancelFlag = false;
	private String msmStr = "";
	private SharedPreferences sp;
	private boolean isComeFromGuangChang;// 是从广场跳过来的还是从我的兼职跳过来的

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
		setContentView(R.layout.activity_detail);
		ViewUtils.inject(this);
		mini_title = (RelativeLayout) findViewById(R.id.mini_title);
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		// 判断是从广场进来的还是我的兼职进来的
		isComeFromGuangChang = getIntent().getBooleanExtra(
				"isComeFromGuangChang", false);
		user_id = sp.getString("userId", "");
		// 底部报名框,若是商家通过群消息进来时隐藏
		RelativeLayout topLayout = (RelativeLayout) findViewById(R.id.top_title_layout);
		topLayout.setBackgroundColor(getResources().getColor(
				R.color.guanli_common_color));
		bootm_layout.setVisibility(View.GONE);
		tousu_imv.setVisibility(View.GONE);
		collectedImv.setVisibility(View.GONE);
		// 收藏、取消收藏
		collectUrl = Url.USER_COLLECT + "?token=" + MainTabActivity.token;
		cancelCollectUrl = Url.USER_CANCEL_COLLECT + "?token="
				+ MainTabActivity.token;

		activity_id = getIntent().getStringExtra("activity_id");
		Editor spEdt = sp.edit();
		spEdt.putBoolean(ConstantForSaveList.userId + activity_id, true);
		spEdt.commit();
		ShareSDK.initSDK(this);
		dataurl = Url.COMPANY_applyActivityDetail + "?token="
				+ MainTabActivity.token;
		applyUrl = Url.COMPANY_apply + "?token=" + MainTabActivity.token;
		cancelUrl = Url.COMPANY_userCancelActivityApply + "?token="
				+ MainTabActivity.token;
		sendUrl = applyUrl;
		sendMSMUrl = Url.COMPANY_sendMSM + "?token=" + MainTabActivity.token;
		sendEmailUrl = Url.USER_send_mail + "?token=" + MainTabActivity.token;

		setTopTitle("活动详细");
		setBackButton();
		setRightImage(R.id.right, shareBtn);
		ViewUtils.inject(this);
		getData();
		tousu_imv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showSheetTousu();
			}
		});
		// 收藏活动或者取消收藏活动
		collectedImv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (jianzhi != null) {
					if (jianzhi.getCollected() == 0) {
						// 未收藏
						collectedJianzhi();
					} else if (jianzhi.getCollected() == 1) {
						// 已收藏
						cancelCollectJianzhi();
					}
				}

			}
		});
	}

	/**
	 * 收藏兼职
	 * 
	 */
	private void collectedJianzhi() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST,
				collectUrl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss = js.getJSONObject("ResponseStatus");
							String status = jss.getString("status");
							if ("1".equals(status)) {
								collectedImv
										.setImageResource(R.drawable.activity_collected);
								ToastUtil.showShortToast("收藏成功");
								jianzhi.setCollected(1);
							} else {
								ToastUtil.showShortToast("网络不好,收藏失败");
							}

						} catch (JSONException e) {
							e.printStackTrace();
							ToastUtil.showShortToast("网络不好,收藏失败");

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
				map.put("activity_id", activity_id);
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
	}

	/**
	 * 取消收藏
	 */
	private void cancelCollectJianzhi() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST,
				cancelCollectUrl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss = js.getJSONObject("ResponseStatus");
							String status = jss.getString("status");
							if ("1".equals(status)) {
								collectedImv
										.setImageResource(R.drawable.activity_un_collected);
								ToastUtil.showShortToast("取消收藏成功");
								jianzhi.setCollected(0);
							} else {
								ToastUtil.showShortToast("网络不好,取消收藏失败");
							}

						} catch (JSONException e) {
							e.printStackTrace();
							ToastUtil.showShortToast("网络不好,取消收藏失败");

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
				map.put("activity_id", activity_id);
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
	}

	public void getData() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST, dataurl,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss = js
									.getJSONObject("ActivityDetails");
							jianzhi = (AllJianzhiDetail) JsonUtil.jsonToBean(
									jss, AllJianzhiDetail.class);
							initView();
						} catch (JSONException e) {
							e.printStackTrace();

						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showWait(false);
						mini_title.setVisibility(View.GONE);
						jz_type.setVisibility(View.GONE);
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", user_id);
				map.put("activity_id", activity_id);
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
	}

	/**
	 * 未被录取的时候点击取消报名
	 */
	public void getSubmint2() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST, sendUrl,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							int left_count = js.getInt("left_count");
							if (left_count == -1) {
								showAlertDialog("您是诚意金用户还可以报名多个活动", "取消成功",
										"我知道了");
							} else {
								if (isComeFromGuangChang) {
									showAlertDialog("已成功取消报名该活动，您今天还可报名"
											+ left_count + "个活动哦！", "取消成功",
											"去广场");
								} else {
									showAlertDialog("已成功取消报名该活动，您今天还可报名"
											+ left_count + "个活动哦！", "取消成功",
											"我知道了");
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
							showToast("申请失败");
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
				map.put("activity_id", activity_id);
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
	}

	/**
	 * 点击报名或者取消报名
	 */
	public void getSubmint() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST, sendUrl,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							if (cancelFlag) {
								int left_count = js.getInt("left_count");
								if (left_count == -1) {
									showAlertDialog("您是诚意金用户还可以报名多个活动", "取消成功",
											"我知道了");
								} else {
									if (isComeFromGuangChang) {
										showAlertDialog("已成功取消报名该活动，您今天还可报名"
												+ left_count + "个活动哦！", "取消成功",
												"去广场");
									} else {
										showAlertDialog("已成功取消报名该活动，您今天还可报名"
												+ left_count + "个活动哦！", "取消成功",
												"我知道了");
									}
								}

							} else {
								JSONObject jss = js
										.getJSONObject("ApplyResponse");
								Log.e("jss", jss.toString() + "?carson");
								int status = jss.getInt("status");

								if (status == 1) {
									showAlertDialog(
											"您已被别的商家录用，不能报名同一时间段的活动，报名失败",
											"温馨提示", "我知道了");
								} else if (status == 2 || status == 3) {
									String msg = jss.getString("msg");
									if (isComeFromGuangChang) {
										showAlertDialog(msg, "温馨提示", "去广场");
									} else {
										showAlertDialog(msg, "温馨提示", "我知道了");
									}
								} else if (status == 4) {
									showAlertDialog(
											"你的个人资料不够完善，完善您的个人简历，会增加商家录用您的概率哦！",
											"温馨提示", "完善简历");
								} else if (status == 5) {
									showAlertDialog("本活动已招募满，不能报名。", "温馨提示",
											"我知道了");
								} else if (status == 6) {
									showAlertDialog2(
											"由于您的信誉值过低，今天的报名次数已使用完，报名失败。",
											"报名失败", "取消", "提高信誉值");
								} else if (status == 7) {
									if (jss.getInt("left_count") == -1) {
										showAlertDialog("报名成功，您是诚意金用户可报名多个活动",
												"报名成功", "我知道了");
									} else {
										if (jss.getInt("left_count") > 0) {
											showAlertDialog2(
													"已将您的简历提交给商家，您今天还可以报名"
															+ jss.getInt("left_count")
															+ "个活动。（诚意金用户还可报名多个）",
													"报名成功", "了解诚意金", "我知道了");
										} else {
											showAlertDialog2(
													"已将您的简历提交给商家，今天的报名次数已用完。提高信誉值可增加报名次数",
													"报名成功", "提高信誉值", "我知道了");
										}
									}
								}
							}
							if (popupWindow != null && popupWindow.isShowing()) {
								popupWindow.dismiss();
							}
						} catch (JSONException e) {
							e.printStackTrace();
							showToast("申请失败");
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
				map.put("activity_id", activity_id);
				map.put("note", contentStr);

				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
	}

	public void showAlertDialog(final String str, final String str2,
			final String str3) {
		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(str);
		builder.setTitle(str2);
		builder.setPositiveButton(str3, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (str3.equals("完善简历")) {// 前往我的简历
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(),
							MyResumeActivity.class);
					startActivity(intent);
				}
				if (str.equals("告诉商家，你是来自兼职达人app的用户，会增大你的通过率哦！")) {// 打电话
					Intent intent = new Intent(Intent.ACTION_CALL, Uri
							.parse("tel:" + jianzhi.getTelephone()));
					startActivity(intent);
				}
				if (str.equals("兼职达人自动为您生成简历，并已成功发送给商家，请静候佳音。")) {

				}
				if (str3.equals("去广场")) {
					ActivityDetialActivity.this.finish();
					return;
				}
				if (str3.equals("我知道了")) {
					ActivityDetialActivity.this.finish();
					return;
				}
				if (str2.equals("报名成功")) {// 报名成功刷新
					getData();
				}
				if (str2.equals("取消成功")) {// 取消刷新
					getData();
				}
			}
		});
		builder.create().show();
	}

	/**
	 * 有取消的弹出框
	 */
	public void showAlertDialog2(final String str, final String str2,
			final String str3, final String str4) {
		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(str);
		builder.setTitle(str2);
		builder.setPositiveButton(str3, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (str3.equals("了解诚意金")) {
					Intent intent = new Intent(ActivityDetialActivity.this,
							LocalCarouselActivity.class);
					intent.putExtra("type", 3 + "");
					startActivity(intent);
				}
				if (str3.equals("提高信誉值")) {
					Intent intent = new Intent(ActivityDetialActivity.this,
							HowRaiseAuthorityActivity.class);
					startActivity(intent);
				}
				if (str2.equals("报名成功")) {// 报名成功刷新
					getData();
				}
				if (str2.equals("取消成功")) {// 取消刷新
					getData();
				}
				if (str2.equals("取消报名")) {// 未录取的取消报名
					getSubmint2();
				}
			}

		});
		builder.setNegativeButton(str4, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (str4.equals("提高信誉值")) {
					Intent intent = new Intent(ActivityDetialActivity.this,
							HowRaiseAuthorityActivity.class);
					startActivity(intent);
				}
				if (str2.equals("报名成功")) {// 报名成功刷新
					getData();
				}
				if (str2.equals("取消成功")) {// 取消刷新
					getData();
				}

			}
		});
		builder.create().show();
	}

	/**
	 * 展示数据
	 */
	public void initView() {
		// app兼职
		if (jianzhi.getSource() == 1) {
			int head_count = jianzhi.getHead_count();
			int confirmed_count = jianzhi.getConfirmed_count();
			int apply_count = jianzhi.getApply_count();
			number.setText("已报名" + apply_count + "人，差");
			number1.setText((head_count - confirmed_count) + "");
			contactshangjia.setOnClickListener(contactshangjiaApp);
			tips = "你好，我会努力做好这份工作的!";
			// getApply() 申请状态：0-待确认，1-已确认，2-拒绝(未通过),3-表示待确定 4 未申请
			if (jianzhi.getApply() == 3) { // 待确认
				cancelFlag = true;
				sendUrl = cancelUrl;
				baoming.setText("取消兼职");
				// baoming.setBackground(getResources().getDrawable(R.drawable.bord_quxiaocanjia));
				baoming.setBackgroundResource(R.drawable.bord_quxiaocanjia);
				baoming.setTextColor(getResources()
						.getColor(R.color.ziti_huise));
				tips = "你好，我因个人私事，无法参加兼职，抱歉！";
				jobstatuts.setVisibility(View.VISIBLE);
				// 如果人已招满则显示备胎,反之显示待确认
				if ((head_count - confirmed_count) <= 0) {
					jobstatuts.setImageDrawable(getResources().getDrawable(
							R.drawable.beitai));
				} else {
					jobstatuts.setImageDrawable(getResources().getDrawable(
							R.drawable.myjob_icon_wait));
				}
			} else if (jianzhi.getApply() == 1) {// 通过
				cancelFlag = true;
				sendUrl = cancelUrl;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					baoming.setBackground(getResources().getDrawable(
							R.drawable.bord_quxiaocanjia));
				} else {
					baoming.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.bord_quxiaocanjia));
				}
				baoming.setTextColor(getResources()
						.getColor(R.color.ziti_huise));
				baoming.setText("取消兼职");
				tips = "你好，我因个人私事，无法参加兼职，抱歉！";
				jobstatuts.setVisibility(View.VISIBLE);
				jobstatuts.setImageDrawable(getResources().getDrawable(
						R.drawable.myjob_icon_pass));
			} else if (jianzhi.getApply() == 2) {// 不通过
				baoming.setVisibility(view.GONE);
				jobstatuts.setVisibility(View.VISIBLE);
				jobstatuts.setImageDrawable(getResources().getDrawable(
						R.drawable.myjob_icon_refuse));
			} else if (jianzhi.getApply() == 4) {
				cancelFlag = false;
				sendUrl = applyUrl;
				jobstatuts.setVisibility(View.INVISIBLE);
				baoming.setVisibility(view.VISIBLE);
				baoming.setText("报名参加");
				baoming.setTextColor(getResources().getColor(R.color.orange));
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					baoming.setBackground(getResources().getDrawable(
							R.drawable.bord_baomingcanjia));
				} else {
					baoming.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.bord_baomingcanjia));
				}
			}
			// 分享url
			url = "http://weixin.jobdiy.cn/info1.php?user_id=1&activity_id="
					+ activity_id + "&type=" + jianzhi.getType();
		} else {// 抓来的
			baoming.setVisibility(view.GONE);
			number.setText("代招");
			number.setTextSize(17);
			number.setTextColor(getResources().getColor(R.color.ziti_orange));
			number1.setVisibility(View.GONE);
			number2.setVisibility(View.GONE);
			number.setPadding(0, 0, 30, 0);
			contactshangjia.setOnClickListener(contactshangjiaZD);
			// 分享url
			url = "http://weixin.jobdiy.cn/info0.php?user_id=1&activity_id="
					+ activity_id + "&type=" + jianzhi.getType();
		}

		jz_type.setText(jianzhi.getType());
		if (jianzhi.getType().equals("派发")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_1);
		}
		if (jianzhi.getType().equals("促销")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_2);
		}
		if (jianzhi.getType().equals("其他")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_3);
		}
		if (jianzhi.getType().equals("家教")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_4);
		}
		if (jianzhi.getType().equals("服务员")) {
			// jz_type.setText("服务");
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_5);
		}
		if (jianzhi.getType().equals("礼仪")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_6);
		}
		if (jianzhi.getType().equals("安保人员")) {
			// jz_type.setText("安保");
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_7);
		}
		if (jianzhi.getType().equals("模特")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_8);
		}
		if (jianzhi.getType().equals("主持")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_9);
		}
		if (jianzhi.getType().equals("翻译")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_10);
		}
		if (jianzhi.getType().equals("工作人员")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_11);
		}
		if (jianzhi.getType().equals("话务")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_12);
		}
		if (jianzhi.getType().equals("充场")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_13);
		}
		if (jianzhi.getType().equals("演艺")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_14);
		}
		if (jianzhi.getType().equals("访谈")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_15);
		}
		jz_title.setText(jianzhi.getTitle());

		if (jianzhi.getPublish_time() != null
				&& jianzhi.getPublish_time().length() > 10) {
			jz_publish_time.setText("发布时间"
					+ jianzhi.getPublish_time().substring(0, 10));
		}
		jz_pay.setText(jianzhi.getPay() + "");
		if (jianzhi.getPay_type() == 0) {
			jz_pay_type.setText("元/日");
			shareTitle = shareTitle.append(jianzhi.getTitle() + ";"
					+ jianzhi.getPay() + "元/日");
		} else {
			jz_pay_type.setText("元/小时");
			shareTitle = shareTitle.append(jianzhi.getTitle() + ";"
					+ jianzhi.getPay() + "元/小时");
		}

		jz_company.setText(jianzhi.getName() + "");

		String timeStr = jianzhi.getStart_time() + "至" + jianzhi.getEnd_time();
		work_zone.setText(jianzhi.getCounty());
		shareText = shareText.append("工作地点:" + jianzhi.getCounty());
		jz_shijian.setText(timeStr);
		shareText = shareText.append(";" + "工作时间:" + jianzhi.getStart_time());
		jz_jieshuan_type.setText(jianzhi.getPay_form());
		if (jianzhi.getApart_sex() == 0) {// 0-不区分男女
			jz_worker_number.setText(jianzhi.getHead_count() + "人");
		} else {
			if (jianzhi.getMale_count() == 0) {
				jz_worker_number
						.setText("女 " + jianzhi.getFemale_count() + "人");
			} else if (jianzhi.getFemale_count() == 0) {
				jz_worker_number.setText("男 " + jianzhi.getMale_count() + "人");
			} else {
				jz_worker_number.setText("男 " + jianzhi.getMale_count() + ","
						+ "女 " + jianzhi.getFemale_count());
			}
		}

		if (jianzhi.getRequire_height() > 0) {
			jz_heigh.setText(jianzhi.getRequire_height() + "cm以上");
		} else {
			jz_layout_height.setVisibility(View.GONE);
		}

		if ((jianzhi.getRequire_shoe_weigth() > 0)
				&& (jianzhi.getRequire_shoe_weigth() != 0)) {
			jz_shoe.setText(jianzhi.getRequire_shoe_weigth() + "");
		} else {
			jz_layout_shoe.setVisibility(View.GONE);
		}

		if (!(null == jianzhi.getRequire_cloth_weight())
				&& !jianzhi.getRequire_cloth_weight().equals("-1")
				&& (!jianzhi.getRequire_cloth_weight().equals("0"))
				&& (!"".equals(jianzhi.getRequire_cloth_weight()))) {
			jz_close.setText(jianzhi.getRequire_cloth_weight() + "");
		} else {
			jz_layout_close.setVisibility(View.GONE);
		}

		String sanweiStr = "";
		if (jianzhi.getRequire_bust() > 0 && jianzhi.getRequire_beltline() > 0
				&& jianzhi.getRequire_hipline() > 0) {
			sanweiStr = "胸 " + jianzhi.getRequire_bust() + "cm";
			sanweiStr += "  腰 " + jianzhi.getRequire_beltline() + "cm";
			sanweiStr += "  臀 " + jianzhi.getRequire_hipline() + "cm";
			jz_sanwei.setText(sanweiStr);
		} else {
			jz_layout_sanwei.setVisibility(View.GONE);
		}

		if (jianzhi.getRequire_health_record() == 0) {
			jz_health_card.setText("不需要");
		} else if (jianzhi.getRequire_health_record() == 1) {
			jz_health_card.setText("需要");
		} else {
			jz_health_layout.setVisibility(View.GONE);
		}
		if (jianzhi.getRequire_language() != null
				&& (!jianzhi.getRequire_language().equals(""))) {
			jz_language.setText(jianzhi.getRequire_language() + "");
		} else {
			jz_layout_language.setVisibility(View.GONE);
		}

		jz_addressdetail.setText(jianzhi.getAddress());
		jz_work_info.setText(jianzhi.getRequire_info());
		// 显示保证金商家、超级商家
		if (jianzhi.getGuarantee_title() == null
				|| "".equals(jianzhi.getGuarantee_title())) {
			baozhengjinTv.setVisibility(View.GONE);
		} else {
			actSplitIv.setVisibility(View.GONE);
			baozhengjinTv.setText(jianzhi.getGuarantee_title());
			baozhengjinTv.setVisibility(View.VISIBLE);
		}
		if (jianzhi.getSuperJob_title() == null
				|| "".equals(jianzhi.getSuperJob_title())) {
			chaojiTv.setVisibility(View.GONE);
		} else {
			actSplitIv.setVisibility(View.GONE);
			chaojiTv.setText(jianzhi.getSuperJob_title());
			chaojiTv.setVisibility(View.VISIBLE);
		}
	}

	// 操作
	private View view;
	private PopupWindow popupWindow;
	private EditText editText;
	private TextView zi_number;
	private ImageView send;
	String contentStr;

	@OnClick(R.id.baoming)
	public void manageOcnlick(View v) {
		// 数据埋点
		MobclickAgent.onEvent(ActivityDetialActivity.this, "onclick1", "报名");
		if (NetWorkCheck.isOpenNetwork(ActivityDetialActivity.this)) {
			if (jianzhi != null) {
				// 网络正常时进入活动详情
				// 未被录用的取消兼职无需发布留言
				if (jianzhi.getApply() == 3) {
					showAlertDialog2("真的要取消报名吗", "取消报名", "马上取消", "稍后再说");
				} else {
					if (popupWindow != null && popupWindow.isShowing()) {
						popupWindow.dismiss();
					} else {
						initPopupWindow(v);
					}
				}
			} else {
				// 网络太差或者无网络时进入活动详情
				ToastUtil.showShortToast("网络不好,请检查网络设置。");
			}
		} else {
			// 网络太差或者无网络时进入活动详情
			ToastUtil.showShortToast("网络不好,请检查网络设置。");
		}
	}

	/**
	 * 报名pop
	 * 
	 * @param v
	 */
	private void initPopupWindow(View v) {
		view = this.getLayoutInflater().inflate(R.layout.popup_window_baoming,
				null);
		popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);

		editText = (EditText) view.findViewById(R.id.content);
		editText.setText(tips);
		String textstr = editText.getText().toString();
		editText.setSelection(textstr.length());// 光标放在尾部
		zi_number = (TextView) view.findViewById(R.id.zi_number);
		send = (ImageView) view.findViewById(R.id.send);
		send.setOnClickListener(sendListener);
		editText.addTextChangedListener(textWatcher);
		TextView hp = (TextView) view.findViewById(R.id.hp);

		// 虚拟键盘在popup下方
		popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		// 再设置模式，和Activity的一样，覆盖，调整大小。
		popupWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		int[] location = new int[2];
		v.getLocationOnScreen(location);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.getBackground().setAlpha(100);

		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], 0);

		hp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
	}

	TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			zi_number.setText(editText.getText().toString().length() + "/50");
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	OnClickListener sendListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (check()) {
				getSubmint();// 取消兼职或者申请兼职
			}
		}
	};

	// ================操作end===============
	public boolean check() {
		contentStr = editText.getText().toString();

		if ((!Util.isEmpty(contentStr)) || (contentStr.length() < 5)) {
			showToast("请输入至少个5字的商家留言");
			return false;
		}

		return true;
	}

	/**
	 * 活动分享
	 */
	OnClickListener shareBtn = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (NetWorkCheck.isOpenNetwork(ActivityDetialActivity.this)) {
				if (jianzhi != null) {
					share = new SharePopupWindow(ActivityDetialActivity.this,
							true);
					share.setPlatformActionListener(ActivityDetialActivity.this);
					ShareModel model = new ShareModel();
					model.setText(shareText.toString());
					model.setTitle(shareTitle.toString());
					model.setUrl(url);
					model.setImageUrl(imageurl);
					share.initShareParams(model, 0);
					share.shareDataFromActivity(activity_id,
							jianzhi.getTitle(), jianzhi.getPay(),
							jianzhi.getPay_type(), jianzhi.getCounty(),
							jianzhi.getStart_time(), jianzhi.getHead_count()
									- jianzhi.getConfirmed_count());// 传递activity
																	// 详细信息
					share.showShareWindow();
					// 显示窗口 (设置layout在PopupWindow中显示的位置)
					share.showAtLocation(ActivityDetialActivity.this
							.findViewById(R.id.right), Gravity.TOP
							| Gravity.CENTER_HORIZONTAL, 0, 0);
				} else {
					ToastUtil.showShortToast("网络不好,请检查网络设置。");
				}
			} else {
				ToastUtil.showShortToast("网络不好,请检查网络设置。");
			}
		}
	};

	@OnClick(R.id.baoming)
	public void baomingonClick(View v) {

	}

	/**
	 * 重写回调方法
	 */
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.arg1) {
		case 1: {
			// 成功
			// showToast("分享成功");
			// Toast.makeText(ActivityDetialActivity.this, "分享成功",
			// Toast.LENGTH_SHORT).show();
			System.out.println("分享回调成功------------");
		}
			break;
		case 2: {
			// 失败
			// //showToast("分享失败");
			// Toast.makeText(ActivityDetialActivity.this, "分享失败",
			// Toast.LENGTH_SHORT).show();
		}
			break;
		case 3: {
			// 取消
			// showToast("分享取消");
			// Toast.makeText(ActivityDetialActivity.this, "分享取消",
			// Toast.LENGTH_SHORT).show();
		}
			break;
		}
		if (share != null) {
			share.dismiss();
		}
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (share != null) {
			share.dismiss();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ShareSDK.stopSDK(this);
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		Message msg = new Message();
		msg.what = 0;
		UIHandler.sendMessage(msg, this);

	}

	@Override
	public void onComplete(Platform plat, int action,
			HashMap<String, Object> arg2) {
		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = plat;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		Message msg = new Message();
		msg.what = 1;
		UIHandler.sendMessage(msg, this);
	}

	OnClickListener contactshangjiaApp = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 先判断是否是好友关系
			if (jianzhi.getCompany_im_id() != null
					&& !"".equals(jianzhi.getCompany_im_id())) {
				if (jianzhi.getDisturb() == 0) {
					// 默认未开启消息免打扰
					// 直接发送消息
					Intent intent = new Intent();
					intent.setClass(ActivityDetialActivity.this,
							ChatActivity.class);
					intent.putExtra("userId", jianzhi.getCompany_im_id());
					startActivity(intent);

				} else if (jianzhi.getDisturb() == 1) {
					// 开启了消息免打扰
					Intent intent = new Intent(ActivityDetialActivity.this,
							UserInfo.class);
					intent.putExtra("hxId", jianzhi.getCompany_im_id());
					startActivity(intent);
				}

			}

		}
	};

	OnClickListener contactshangjiaZD = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (popupWindowcontact != null && popupWindowcontact.isShowing()) {
				popupWindowcontact.dismiss();
			} else {
				contactPopupWindow(v);
			}
		}
	};

	private View viewContact;
	PopupWindow popupWindowcontact;
	private TextView refresh;
	private TextView edit;
	private TextView cancel;

	private void contactPopupWindow(View v) {
		viewContact = this.getLayoutInflater().inflate(
				R.layout.popup_window_jzm, null);
		popupWindowcontact = new PopupWindow(viewContact,
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		// 这里设置显示PopuWindow之后在外面点击是否有效。如果为false的话，那么点击PopuWindow外面并不会关闭PopuWindow。

		popupWindowcontact.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionsheet_bottom_normal));

		refresh = (TextView) viewContact.findViewById(R.id.refresh);
		refresh.setText("短信报名");
		edit = (TextView) viewContact.findViewById(R.id.edit);
		edit.setText("电话报名");
		cancel = (TextView) viewContact.findViewById(R.id.cancel);
		cancel.setText("邮件报名");
		if (jianzhi.getTelephone() != null
				&& !jianzhi.getTelephone().equals("")) {
			refresh.setTextColor(getResources().getColor(R.color.body_color));
			edit.setTextColor(getResources().getColor(R.color.body_color));
			refresh.setOnClickListener(refreshListener);
			edit.setOnClickListener(editListener);
		} else {
			refresh.setTextColor(getResources().getColor(R.color.item_huise));
			edit.setTextColor(getResources().getColor(R.color.item_huise));
			refresh.setClickable(false);
			edit.setClickable(false);
		}

		if (jianzhi.getClaw_email() != null
				&& !jianzhi.getClaw_email().equals("")) {
			cancel.setOnClickListener(calcelListener);
			cancel.setTextColor(getResources().getColor(R.color.body_color));
		} else {
			cancel.setTextColor(getResources().getColor(R.color.item_huise));
			cancel.setClickable(false);
		}

		refresh.getBackground().setAlpha(80);
		edit.getBackground().setAlpha(80);
		cancel.getBackground().setAlpha(80);
		bootm_layout.getBackground().setAlpha(120);

		popupWindowcontact.setFocusable(false);
		popupWindowcontact.setOutsideTouchable(false);
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		int a = location[0];
		int b = location[1];
		int c = viewContact.getHeight();

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);

		int width = metric.widthPixels; // 宽度（PX）
		int height = metric.heightPixels; // 高度（PX）

		float density = metric.density; // 密度（0.75 / 1.0 / 1.5）
		int densityDpi = metric.densityDpi; // 密度DPI（120 / 160 / 240）
		int d = (int) (74 * density);
		float sd = (float) densityDpi / 160;
		popupWindowcontact.showAtLocation(v, Gravity.NO_GRAVITY, location[0],
				location[1] - d);
		// popupWindowcontact.showAsDropDown(v,0, -c);

	}

	/**
	 * 显示投诉选择对话框
	 */
	public Dialog showSheetTousu() {
		final Dialog dlg = new Dialog(ActivityDetialActivity.this,
				R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.action_tousu_sheet, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		final TextView tousu1 = (TextView) layout.findViewById(R.id.tousu1);//
		final TextView tousu2 = (TextView) layout.findViewById(R.id.tousu2);
		final TextView tousu3 = (TextView) layout.findViewById(R.id.tousu3);//
		TextView cancel = (TextView) layout.findViewById(R.id.cancel);//
		tousu1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ActivityDetialActivity.this,
						TousuActivity.class);
				intent.putExtra("content", tousu1.getText().toString());
				intent.putExtra("activity_id", activity_id);
				startActivity(intent);
				dlg.dismiss();

			}
		});

		tousu2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ActivityDetialActivity.this,
						TousuActivity.class);
				intent.putExtra("content", tousu2.getText().toString());
				intent.putExtra("activity_id", activity_id);
				startActivity(intent);
				dlg.dismiss();
			}
		});

		tousu3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ActivityDetialActivity.this,
						TousuActivity.class);
				intent.putExtra("content", "");
				intent.putExtra("activity_id", activity_id);
				startActivity(intent);
				dlg.dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dlg.dismiss();
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(false);
		dlg.setContentView(layout);
		dlg.show();
		return dlg;
	}

	/**
	 * 发短信
	 */
	OnClickListener refreshListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (popupWindowcontact != null && popupWindowcontact.isShowing()) {
				popupWindowcontact.dismiss();
			}
			getSendMSMData();
		}
	};

	OnClickListener editListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (popupWindowcontact != null && popupWindowcontact.isShowing()) {
				popupWindowcontact.dismiss();
			}
			showAlertDialog("告诉商家，你是来自兼职达人app的用户，会增大你的通过率哦！", "温馨提示", "好的");
		}
	};

	/**
	 * 代招发邮件
	 */
	OnClickListener calcelListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (popupWindowcontact != null && popupWindowcontact.isShowing()) {
				popupWindowcontact.dismiss();
			}
			// showToast("发email");
			showWait(true);
			StringRequest request = new StringRequest(Request.Method.POST,
					sendEmailUrl, new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							Log.i("erros", "短信回来的数据" + response);
							showWait(false);
							showAlertDialog("兼职达人自动为您生成简历，并已成功发送给商家，请静候佳音。",
									"发送成功", "么么哒");
							// showToast("发送成功");
							// try {
							// JSONObject js = new JSONObject(response);
							// msmStr = js.getString("msm");

							// } catch (JSONException e) {
							// e.printStackTrace();
							// }
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError volleyError) {
							showWait(false);
							showToast("发送邮件失败！501");
						}
					}) {
				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					Map<String, String> map = new HashMap<String, String>();
					map.put("user_id", user_id);
					map.put("activity_id", activity_id);

					return map;
				}
			};
			queue.add(request);
			request.setRetryPolicy(new DefaultRetryPolicy(
					ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

		}
	};

	/**
	 * 获取发送短信的内容
	 */
	public void getSendMSMData() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST,
				sendMSMUrl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							msmStr = js.getString("msm");
							Uri uri = Uri.parse("smsto:"
									+ jianzhi.getTelephone());
							Intent intent = new Intent(Intent.ACTION_SENDTO,
									uri);
							intent.putExtra("sms_body", msmStr);
							startActivity(intent);
						} catch (JSONException e) {
							e.printStackTrace();
							// showToast("申请失败");
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showWait(false);
						showToast("获取发送短信内容失败！501");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", user_id);
				map.put("activity_id", activity_id);

				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

}
