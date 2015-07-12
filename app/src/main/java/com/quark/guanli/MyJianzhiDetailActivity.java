package com.quark.guanli;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qingmu.jianzhidaren.R;
import com.quark.common.JsonUtil;
import com.quark.common.ToastUtil;
import com.quark.common.Url;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.jianzhidaren.MainCompanyActivity;
import com.quark.model.PublishJianzhi;
import com.quark.share.ShareModel;
import com.quark.share.SharePopupWindow;
import com.quark.ui.widget.CustomDialog;
import com.quark.ui.widget.CustomDialogThree;
import com.quark.utils.NetWorkCheck;
import com.thirdparty.alipay.RechargeActivity;

/**
 * 
 * @ClassName: MyJianzhiDetailActivity
 * @Description: 我的兼职详细
 * @author howe
 * @date 2015-1-23 下午3:23:57
 * 
 */
public class MyJianzhiDetailActivity extends BaseActivity implements
		PlatformActionListener, Callback {
	PublishJianzhi jianzhi;
	@ViewInject(R.id.jz_type)
	TextView jz_type;
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
	@ViewInject(R.id.jz_company)
	TextView jz_company;
	@ViewInject(R.id.jz_addressdetail)
	TextView jz_addressdetail;
	@ViewInject(R.id.jz_work_info)
	TextView jz_work_info;
	@ViewInject(R.id.baomingshu)
	TextView baomingshu;
	@ViewInject(R.id.manage)
	TextView edtJobTv;
	@ViewInject(R.id.re_shangjia)
	TextView reShangjiaTv;
	@ViewInject(R.id.baomingnum)
	Button baomingnum;
	@ViewInject(R.id.bootm_layout)
	LinearLayout bootm_layout;
	@ViewInject(R.id.totalNumber)
	TextView totalNumber;
	@ViewInject(R.id.yescount)
	TextView yescount;
	@ViewInject(R.id.nosee)
	TextView nosee;
	@ViewInject(R.id.nosee_layout)
	LinearLayout nosee_layout;
	private String activity_id;
	private String url;
	private String refreshUrl;// 刷新url
	private String previewRefreshUrl;// 刷新前判断
	private String crUrl;
	private String modify;
	private String modifyCommit;
	private String user_id;
	private int pay = 0;
	private String imageurl = Url.GETPIC + "pop_share_btn_jz.png";
	private StringBuffer shareTitle;
	private StringBuffer shareText;
	private String share_url = "";
	private SharePopupWindow share;
	private TextView view_count_tv;// 浏览次数
	private SharedPreferences sp;

	// private String yiqueren, weichuli;// 上一个界面传送过来的已确认、未处理人数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
		setContentView(R.layout.activity_my_jianzhi_detail);
		ViewUtils.inject(this);
		url = Url.COMPANY_MyJianzhi_detail + "?token="
				+ MainCompanyActivity.token;
		refreshUrl = Url.COMPANY_MyJianzhi_reflesh + "?token="
				+ MainCompanyActivity.token;
		previewRefreshUrl = Url.COMPANY_MyJianzhi_previewReflesh + "?token="
				+ MainCompanyActivity.token;
		activity_id = getIntent().getStringExtra("activity_id");
		// 已确认、未处理
		// yiqueren = getIntent().getStringExtra("yiqueren" + "");
		// weichuli = getIntent().getStringExtra("weichuli" + "");
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		user_id = sp.getString("userId", "");
		RelativeLayout topLayout = (RelativeLayout) findViewById(R.id.top_title_layout);
			topLayout.setBackgroundColor(getResources().getColor(
					R.color.guanli_common_color));
		baomingshu.setVisibility(View.GONE);
		setTopTitle("活动详情");
		setBackButton();
		ShareSDK.initSDK(this);
		setRightImage(R.id.right, shareBtn);
	}

	public void initView() {
		view_count_tv = (TextView) findViewById(R.id.jz_view_count);
		view_count_tv.setText(jianzhi.getView_count() + "");
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
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_5);
		}
		if (jianzhi.getType().equals("礼仪")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_6);
		}
		if (jianzhi.getType().equals("安保人员")) {
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
		if (jianzhi.getPublish_time().length() > 10) {
			jz_publish_time.setText("发布时间   "
					+ jianzhi.getPublish_time().substring(0, 10));
		}

		jz_pay.setText(jianzhi.getPay() + "");
		if (jianzhi.getPay_type() == 0) {
			shareTitle = new StringBuffer();
			jz_pay_type.setText("元/日");
			shareTitle = shareTitle.append(jianzhi.getTitle() + ";"
					+ jianzhi.getPay() + "元/日");
		} else {
			shareTitle = new StringBuffer();
			jz_pay_type.setText("元/时");
			shareTitle = shareTitle.append(jianzhi.getTitle() + ";"
					+ jianzhi.getPay() + "元/时");
		}

		String timeStr = "";
		timeStr = jianzhi.getStart_time();
		timeStr += "至" + jianzhi.getEnd_time();

		shareText = new StringBuffer();
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

		totalNumber.setText(jianzhi.getHead_count() + "");

		// 修改成从上一个界面获取来的
		// yescount.setText(yiqueren);
		// nosee.setText(weichuli);
		yescount.setText(jianzhi.getConfirmed_count() + "");
		nosee.setText(jianzhi.getUncheck_count() + "");
		if ("0".equals(jianzhi.getUncheck_count() + "")) {
			nosee_layout.setVisibility(View.GONE);
		}
		baomingnum.setText(jianzhi.getUncheck_count() + "");
		// 需求说隐藏
		baomingnum.setVisibility(View.GONE);

		if (jianzhi.getStatus() == 4) {
			reShangjiaTv.setText("重新上架");
		} else {
			reShangjiaTv.setText("下架兼职");
		}
		edtJobTv.setOnClickListener(editListener);
		reShangjiaTv.setOnClickListener(calcelListener);

		if (jianzhi.getRequire_height() != -1) {
			jz_heigh.setText(jianzhi.getRequire_height() + "cm以上");
		} else {
			jz_layout_height.setVisibility(View.GONE);
		}

		if ((jianzhi.getRequire_shoe_weigth() != -1)
				&& (jianzhi.getRequire_shoe_weigth() != 0)) {
			jz_shoe.setText(jianzhi.getRequire_shoe_weigth() + "");
		} else {
			jz_layout_shoe.setVisibility(View.GONE);
		}

		if (!jianzhi.getRequire_cloth_weight().equals("-1")
				&& !"".equals(jianzhi.getRequire_cloth_weight())) {
			jz_close.setText(jianzhi.getRequire_cloth_weight() + "");
		} else {
			jz_layout_close.setVisibility(View.GONE);
		}
		jz_company.setText(jianzhi.getName() + "");
		String sanweiStr = "";
		if (jianzhi.getRequire_bust() != -1
				&& jianzhi.getRequire_beltline() != -1
				&& jianzhi.getRequire_hipline() != -1) {
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

		if (jianzhi.getRequire_language() != null) {
			jz_language.setText(jianzhi.getRequire_language() + "");
		} else {
			jz_layout_language.setVisibility(View.GONE);
		}

		jz_addressdetail.setText(jianzhi.getAddress());
		jz_work_info.setText(jianzhi.getRequire_info());
		// baomingnum.setText(jianzhi.get)
		// 分享URL
		share_url = "http://weixin.jobdiy.cn/info1.php?user_id=1&activity_id="
				+ activity_id + "&type=" + jianzhi.getType();
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
							JSONObject jss = js.getJSONObject("MyActivity");
							jianzhi = (PublishJianzhi) JsonUtil.jsonToBean(jss,
									PublishJianzhi.class);
							initView();
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
				map.put("company_id", user_id);
				map.put("activity_id", activity_id);
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	OnClickListener refreshListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (jianzhi.getStatus() == 3) {
				// 审核未通过不能刷新
				showToast("审核未通过不能进行刷新");
			} else if (jianzhi.getStatus() == 4) {
				// 已下架的活动不能刷新
				showToast("该活动已下架不能进行刷新");

			} else {
				if (jianzhi.getStatus() != 1) {
					// 弹框提示是否要刷新
					previewRefreshJianZhi();
					// 点击刷选窗口关闭
				} else {
					showToast("正在审核的兼职不能刷新");
				}
			}
		}
	};

	OnClickListener editListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (NetWorkCheck.isOpenNetwork(MyJianzhiDetailActivity.this)) {
				if (jianzhi != null) {
					// 正在审核的兼职不能修改
					if (jianzhi.getStatus() != 1) {
						Intent intent = new Intent();
						intent.setClass(MyJianzhiDetailActivity.this,
								WritePartjobActivity.class);
						intent.putExtra("activity_id", activity_id);
						intent.putExtra("type", jianzhi.getType());
						startActivity(intent);
					} else {
						showToast("正在审核的兼职不能修改");
					}
				} else {
					ToastUtil.showShortToast("网络不好,请检查网络设置。");
				}
			} else {
				ToastUtil.showShortToast("网络不好,请检查网络设置。");
			}
		}
	};

	/**
	 * 比较日期大小
	 * 
	 */
	private int compare_date(String DATE1, String DATE2) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				System.out.println("dt1 在dt2前");
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				System.out.println("dt1在dt2后");
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	OnClickListener calcelListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (NetWorkCheck.isOpenNetwork(MyJianzhiDetailActivity.this)) {
				if (jianzhi != null) {
					// 审核未通过不能进行取消
					if (jianzhi.getStatus() != 3) {
						// 正在审核的兼职不能取消
						if (jianzhi.getStatus() != 1) {
							if (reShangjiaTv.getText().toString()
									.equals("重新上架")) {
								new Thread() {
									public void run() {
										URL url;
										try {
											url = new URL(
													"http://www.bjtime.cn");
											// 取得资源对象
											URLConnection uc = url
													.openConnection();// 生成连接对象
											uc.connect(); // 发出连接
											long ld = uc.getDate(); // 取得网站日期时间
											Date date = new Date(ld); // 转换为标准时间对象
											SimpleDateFormat sdformat = new SimpleDateFormat(
													"yyyy-MM-dd");
											String internet_time = sdformat
													.format(date);
											int res_date = compare_date("2015-"
													+ jianzhi.getEnd_time(),
													internet_time);

											if (res_date == -1) {
												// 结束时间小于当前时间,需要修改活动才上架
												runOnUiThread(new Runnable() {
													@Override
													public void run() {
														showAlertUpdateDialog();
														// 弹框修改兼职
													}
												});

											} else {
												// 结束时间大于当前时间，随便上架
												runOnUiThread(new Runnable() {
													public void run() {
														// 上架
														crUrl = Url.COMPANY_MyJianzhi_republish
																+ "?token="
																+ MainCompanyActivity.token;
														crJianzhi(1);
													}
												});
											}

										} catch (MalformedURLException e) {
											e.printStackTrace();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}.start();

							} else {
								// 取消
								pay = 0;
								crUrl = Url.COMPANY_MyJianzhi_cancelActivity
										+ "?token=" + MainCompanyActivity.token;
								crJianzhi(2);
							}
						} else {
							showToast("正在审核的兼职不能取消");
						}
					} else {
						showToast("审核未通过不能进行取消");
					}
				} else {
					ToastUtil.showShortToast("网络不好,请检查网络设置。");
				}
			} else {
				ToastUtil.showShortToast("网络不好,请检查网络设置。");
			}
		}
	};

	/**
	 * 上架或者取消
	 * 
	 * @param flage
	 */
	public void crJianzhi(final int flage) {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST, crUrl,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							if (flage == 1) {
								JSONObject jss = js
										.getJSONObject("RepublishResponse");
								int status = jss.getInt("status");
								if (status == 1) {
									showAlertDialog("每日可免费重新上架一次，再次重新上架需付费",
											"上架成功", "我知道了");
								} else if (status == 2) {
									showAlertDialog("您的余额不足，需要充值才可以上架", "上架失败",
											"去充值");
								} else if (status == 3) {
									showAlertDialog(
											"活动已重新上架，把兼职分享出去，可以帮您尽快补齐人员",
											"上架成功", "好吧");
								}
							} else if (flage == 2) {
								JSONObject jss = js
										.getJSONObject("CancelActivityResponse");
								int status = jss.getInt("status");
								if (status == 1) {
									showToast("下架活动成功");
									getData();// 刷新界面
								} else if (status == 2) {
									showToast("下架失败");
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
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("company_id", user_id);
				map.put("activity_id", activity_id);
				return map;
			}
		};

		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	/**
	 * 刷新兼职信息
	 * 
	 */
	private void refreshJianZhi(final String flag) {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST,
				refreshUrl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss = js
									.getJSONObject("RefleshResponse");
							int status = jss.getInt("status");
							if (status == 1) {
								// 状态1是当前可以免费刷新一次
								// ToastUtil.showShortToast("刷新成功");
								showAlertDialog(
										"您已成功刷新兼职，要记得把活动分享出去哦，让更多的人来报名吧",
										"刷新成功", "我知道了");
							} else if (status == 2) {
								// 状态2是当前免费刷新次数用完,余额不足

							} else if (status == 3) {
								// 状态3表示当前免费刷新次数用完,有可用余额
								ToastUtil.showShortToast("刷新成功");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showWait(false);
						ToastUtil.showShortToast("刷新失败");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("company_id", user_id);
				map.put("activity_id", activity_id);
				map.put("isPay", flag);
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
	}

	/**
	 * 刷新前先判断钱是否够用
	 * 
	 */
	public void previewRefreshJianZhi() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST,
				previewRefreshUrl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss = js
									.getJSONObject("PreRefleshResponse");
							int status = jss.getInt("status");
							String money = jss.getString("money");
							String msg = jss.getString("alert_msg");
							String titile = jss.getString("alert_title");
							String other = jss.getString("alert_other");
							String cancle = jss.getString("alert_cancle");
							if (status == 1) {
								// 状态1是当前可以免费刷新一次

								// showAlertDialog(
								// "您已成功刷新兼职，要记得把活动分享出去哦，让更多的人来报名吧",
								// "刷新成功", "我知道了");
								// true 付费 false 免费
								// 1付费 0 免费
								showRefreshAlertDialog(msg, titile, other,
										cancle, String.valueOf(status));// msg,titile,button
							} else if (status == 2) {
								// 状态2是当前免费刷新次数用完,余额不足
								// showAlertDialog(
								// "您今日的免费刷新次数已使用完，如需刷新此条兼职需要付费3元",
								// "刷新失败", "立即充值");
								showFeeRefreshAlertDialog(msg, titile, other,
										cancle, money, String.valueOf(status));
							} else if (status == 3) {
								// 状态3表示当前免费刷新次数用完,有可用余额
								// showAlertDialog(
								// "活动刷新成功，将活动分享给他人，可以帮你更快的完成人员招聘哦",
								// "刷新成功", "我知道了");
								showFeeRefreshAlertDialog(msg, titile, other,
										cancle, money, String.valueOf(status));
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
				map.put("company_id", user_id);
				map.put("activity_id", activity_id);
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	/**
	 * 刷新付费活动时弹框
	 * 
	 */

	public void showFeeRefreshAlertDialog(String str, final String str2,
			final String str3, final String str4, String money,
			final String flag) {

		CustomDialogThree.Builder builder = new CustomDialogThree.Builder(this);
		builder.setTitle(str2);
		builder.setMessage(str);
		builder.setMoney("(帐号余额:" + money + "元)");
		builder.setPositiveButton(str3, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 2是立即充值,其它是刷新
				if ("2".equals(flag)) {
					Intent intent = new Intent();
					intent.setClass(MyJianzhiDetailActivity.this,
							RechargeActivity.class);
					startActivity(intent);
				} else {
					refreshJianZhi(flag);

				}
			}
		});

		builder.setNegativeButton(str4, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	/**
	 * 刷新活动时弹框
	 * 
	 */

	public void showRefreshAlertDialog(String str, final String str2,
			final String str3, final String str4, final String flag) {

		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setTitle(str2);
		builder.setMessage(str);
		builder.setPositiveButton(str3, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 2是立即充值,其它是刷新
				if ("2".equals(flag)) {
					Intent intent = new Intent();
					intent.setClass(MyJianzhiDetailActivity.this,
							RechargeActivity.class);
					startActivity(intent);
				} else {
					refreshJianZhi(flag);

				}
			}
		});

		builder.setNegativeButton(str4, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	public void showAlertUpdateDialog() {
		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setTitle("温馨提示");
		builder.setMessage("您的活动已到结束时间，如需重新上架，请修改活动时间。");
		builder.setPositiveButton("点错了", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("修改兼职",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
						if (NetWorkCheck
								.isOpenNetwork(MyJianzhiDetailActivity.this)) {
							if (jianzhi != null) {
								// 正在审核的兼职不能修改
								if (jianzhi.getStatus() != 1) {
									Intent intent = new Intent();
									intent.setClass(
											MyJianzhiDetailActivity.this,
											WritePartjobActivity.class);
									intent.putExtra("activity_id", activity_id);
									intent.putExtra("type", jianzhi.getType());
									startActivity(intent);
								} else {
									showToast("正在审核的兼职不能修改");
								}
							} else {
								ToastUtil.showShortToast("网络不好,请检查网络设置。");
							}
						} else {
							ToastUtil.showShortToast("网络不好,请检查网络设置。");
						}

					}
				});
		builder.create().show();

	}

	/**
	 * 上架兼职
	 */
	public void showAlertDialog(String str, final String str2, String str3) {

		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setTitle(str2);
		builder.setMessage(str);
		builder.setPositiveButton(str3, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (str2.equals("刷新失败")) {
					Intent intent = new Intent();
					intent.setClass(MyJianzhiDetailActivity.this,
							RechargeActivity.class);
					startActivity(intent);
				} else if (str2.equals("上架失败")) {
					Intent intent = new Intent();
					intent.setClass(MyJianzhiDetailActivity.this,
							RechargeActivity.class);
					startActivity(intent);

				} else if (str2.equals("上架成功")) {
					getData();// 刷新界面
				} else if (str2.equals("刷新成功")) {
					getData();// 刷新界面
				}
			}
		});
		if ("立即充值".equals(str3)) {
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

	@Override
	protected void onResume() {
		super.onResume();
		if (share != null) {
			share.dismiss();
		}
		getData();
	}

	@OnClick({ R.id.nosee_layout })
	public void baoming2(View v) {
		if (NetWorkCheck.isOpenNetwork(MyJianzhiDetailActivity.this)) {
			if (jianzhi != null) {
				Bundle bundle = new Bundle();
				bundle.putString("activity_id", activity_id);
				bundle.putString("title", jianzhi.getTitle());
				bundle.putString("female_count",
						String.valueOf(jianzhi.getFemale_count()));
				bundle.putString("male_count",
						String.valueOf(jianzhi.getMale_count()));
				bundle.putBoolean("fromNotification", true);
				startActivityByClass(BaomingListActivity.class, bundle);
			} else {
				ToastUtil.showShortToast("网络不好,请检查网络设置。");
			}
		} else {
			ToastUtil.showShortToast("网络不好,请检查网络设置。");
		}
	}

	@OnClick({ R.id.baoming })
	public void baoming(View v) {
		if (NetWorkCheck.isOpenNetwork(MyJianzhiDetailActivity.this)) {
			if (jianzhi != null) {
				Bundle bundle = new Bundle();
				bundle.putString("activity_id", activity_id);
				bundle.putString("title", jianzhi.getTitle());
				bundle.putString("female_count",
						String.valueOf(jianzhi.getFemale_count()));
				bundle.putString("male_count",
						String.valueOf(jianzhi.getMale_count()));
				bundle.putBoolean("fromNotification", false);
				startActivityByClass(BaomingListActivity.class, bundle);
			} else {
				ToastUtil.showShortToast("网络不好,请检查网络设置。");
			}
		} else {
			ToastUtil.showShortToast("网络不好,请检查网络设置。");
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * 活动分享
	 */
	OnClickListener shareBtn = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (NetWorkCheck.isOpenNetwork(MyJianzhiDetailActivity.this)) {
				if (jianzhi != null) {
					// status =2 ，4时可以分享 其它不能分享
					if (jianzhi.getStatus() == 2 || jianzhi.getStatus() == 4) {
						share = new SharePopupWindow(
								MyJianzhiDetailActivity.this, true);
					} else {
						share = new SharePopupWindow(
								MyJianzhiDetailActivity.this, false);
					}
					share.setPlatformActionListener(MyJianzhiDetailActivity.this);
					ShareModel model = new ShareModel();
					model.setText(shareText.toString());
					model.setTitle(shareTitle.toString());
					model.setUrl(share_url);
					model.setImageUrl(imageurl);
					share.initShareParams(model, 0);
					// 添加分享的额外属性
					share.shareDataFromActivity(activity_id,
							jianzhi.getTitle(), jianzhi.getPay(),
							jianzhi.getPay_type(), jianzhi.getCounty(),
							jianzhi.getStart_time(), jianzhi.getHead_count()
									- jianzhi.getConfirmed_count());// 传递activity
																	// 详细信息
					share.showShareWindow();
					// 显示窗口 (设置layout在PopupWindow中显示的位置)
					share.showAtLocation(MyJianzhiDetailActivity.this
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

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.arg1) {
		case 1: {
			// 成功
			System.out.println("分享回调成功------------");
		}
			break;
		case 2: {
			// 失败
		}
			break;
		case 3: {
			// 取消
		}
			break;
		}
		if (share != null) {
			share.dismiss();
		}
		return false;
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		Message msg = new Message();
		msg.what = 0;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ShareSDK.stopSDK(this);
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = arg1;
		msg.obj = arg0;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		Message msg = new Message();
		msg.what = 1;
		UIHandler.sendMessage(msg, this);
	}
}
