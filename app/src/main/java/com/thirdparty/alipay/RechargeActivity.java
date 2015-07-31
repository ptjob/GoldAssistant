package com.thirdparty.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.lidroid.xutils.ViewUtils;
import com.qingmu.jianzhidaren.R;
import com.quark.adapter.RechargeLogAdapter;
import com.quark.common.Url;
import com.quark.jianzhidaren.BaseActivity;
import com.parttime.main.MainTabActivity;
import com.quark.ui.widget.ListViewForScrollView;

/**
 * 
 * @ClassName: RechargeActivity
 * @Description: 充值
 * @author howe
 * @date 2015-1-24 上午8:48:52
 * 
 */

public class RechargeActivity extends BaseActivity {
	RadioGroup radioGroup1, radioGroup2;
	private Boolean changeedGroup = false;
	ListViewForScrollView comment_list;
	ArrayList<String> rechargeList = new ArrayList<String>();
//	private String url;
	private String OrderIdUrl;
	private String aliPayResultUrl;
	private String userId;
	private int money;
	int out_trade_no;// 订单号
	// =====
	// 商户PID
	public static final String PARTNER = "2088811181647667";
	// 商户收款账号
	public static final String SELLER = "510445519@qq.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICXQIBAAKBgQDF7XxcSoSA/mB4fkiBuCxFk8XDuviW3le7RALN/fJfuvMQDwkNtfLa1xm4HUlIRL6W+kL6+JHDLvIjE+2+wKTS4//m96NwswZjYUO6mPwIfe5rjXdLIx6qDzmRjvZIbv6BlHsEGgAlq4NLnvEdFbnPVE32UvjB9ajhf7sM+Fs95QIDAQABAoGAb7s0rNTUIA15YAvJ2pChTVWyGl/93Qz+8ZPfEXH91NSwSaxzK+4+fhNXTXwa1lUYUhpMnWicwFZMEkk5uKj/YZ9Ly7uarJd4y4VEay3m1RG3BZJJapyunCNDVdUEv9OEafGpa6TbUNoFXpcVIzUVpTJYGBZX/zI2U60K+pvfvLECQQDpezSrF7ZSfzgqTvaTnAQfyxmnb4ym4lktD83wN7iFUnXQ+8UxL8ns3ZpWOi5T77CteDo3X+s7+s6jspNuD2jTAkEA2QRwtGg+HNgUnVcFmndUYgo/e7Igs85QtFS2vz+7j2kGmd9rihO3aD8qn8smYDBLKLjSSgqPTI9khBRhBl0LZwJBAJKLIB2a/nZ9HxV/BkjTjcsewPVUkGVWgD5GQy3Y61nSzdvjins60XR4CpzAW7+XG79lTLTg4VZ+LyCTvvE/fr0CQG3qEs880OC5DE/YaG0grStut1KGGIwZHcUH9vsMY4myDvbWMthfPhBdldATC1/Cdf6tBU0c5hFHuwgubinT7FcCQQCCA59CGj7rcgpqG/V5sIlLjAHyI4gZDU0cemR/e5vF90ZijNr5x2hOPy7a+A3LgfkaKsN95/+7U2nNVu30QzEU";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDF7XxcSoSA/mB4fkiBuCxFk8XDuviW3le7RALN/fJfuvMQDwkNtfLa1xm4HUlIRL6W+kL6+JHDLvIjE+2+wKTS4//m96NwswZjYUO6mPwIfe5rjXdLIx6qDzmRjvZIbv6BlHsEGgAlq4NLnvEdFbnPVE32UvjB9ajhf7sM+Fs95QIDAQAB";

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					// 调用服务器进行充值
					// getServerPayResult();
					Toast.makeText(RechargeActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(RechargeActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(RechargeActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(RechargeActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};

	// =====

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recharge);
		ViewUtils.inject(this);
		TextView tv = (TextView) findViewById(R.id.title);
		tv.setText("充值");
		SharedPreferences sp = getSharedPreferences("jrdr.setting",
				MODE_PRIVATE);
		userId = sp.getString("userId", "");

		setBackButton();
//		url = Url.COMPANY_recharge_log + "?token=" + MainTabActivity.token;
		OrderIdUrl = Url.COMPANY_recharge_lproduct + "?token="
				+ MainTabActivity.token;
		aliPayResultUrl = Url.COMPANY_recharge_AliPay + "?token="
				+ MainTabActivity.token;

		radioGroup1 = (RadioGroup) findViewById(R.id.orderBy1);
		radioGroup1
				.setOnCheckedChangeListener(new MyRadioGroupOnCheckedChangedListener());
		radioGroup2 = (RadioGroup) findViewById(R.id.orderBy2);
		radioGroup2
				.setOnCheckedChangeListener(new MyRadioGroupOnCheckedChangedListener());
//		getRechargeLog();
		Button submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(submintListener);
	}

	public void initadatpe() {
		RechargeLogAdapter adapter1 = new RechargeLogAdapter(this, rechargeList);
		comment_list = (ListViewForScrollView) findViewById(R.id.recharge_list);
		comment_list.setAdapter(adapter1);
	}

	class MyRadioGroupOnCheckedChangedListener implements
			OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (!changeedGroup) {
				changeedGroup = true;
				if (group == radioGroup1) {
					radioGroup2.clearCheck();
				} else if (group == radioGroup2) {
					radioGroup1.clearCheck();
				}
				changeedGroup = false;
			}
		}
	}

	OnClickListener submintListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (check()) {
				// 充值
				getOrderId();

			}
		}
	};

	public boolean check() {
		boolean pay_from = true;
		RadioButton radioButton = (RadioButton) findViewById(radioGroup1
				.getCheckedRadioButtonId());
		if (radioButton != null) {
			String text = radioButton.getText().toString();
			money = Integer.valueOf(text.substring(0, text.length() - 1));
			pay_from = false;
		}
		if (pay_from) {
			RadioButton radioButton2 = (RadioButton) findViewById(radioGroup2
					.getCheckedRadioButtonId());
			if (radioButton2 != null) {
				String text2 = radioButton2.getText().toString();
				money = Integer.valueOf(text2.substring(0, text2.length() - 1));
				pay_from = false;
			}
		}
		if (pay_from) {
			showToast("请选择充值金额");
			return false;
		}
		return true;
	}

//	public void getRechargeLog() {
//		showWait(true);
//		StringRequest request = new StringRequest(Request.Method.POST, url,
//				new Response.Listener<String>() {
//					@Override
//					public void onResponse(String response) {
//						showWait(false);
//						try {
//							JSONObject js = new JSONObject(response);
//							JSONArray jss = js.getJSONArray("chargeLog");
//							for (int i = 0; i < jss.length(); i++) {
//								rechargeList.add(jss.getJSONObject(i)
//										.getString("info"));
//							}
//							initadatpe();
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//					}
//				}, new Response.ErrorListener() {
//					@Override
//					public void onErrorResponse(VolleyError volleyError) {
//						showWait(false);
//						showToast("你的网络不够给力，获取数据失败！");
//					}
//				}) {
//			@Override
//			protected Map<String, String> getParams() throws AuthFailureError {
//				Map<String, String> map = new HashMap<String, String>();
//				map.put("company_id", userId + "");
//
//				return map;
//			}
//		};
//		queue.add(request);
//		request.setRetryPolicy(new DefaultRetryPolicy(
//				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
//
//	}

	public void getOrderId() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST,
				OrderIdUrl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss = js.getJSONObject("OrderResponse");
							out_trade_no = jss.getInt("out_trade_no");// 唯一订单号
							pay();
						} catch (JSONException e) {
							e.printStackTrace();
							showToast("获取订单号失败！");
							System.out
									.println("==================reg json 异常===========");
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showWait(false);
						showToast("你的网络不够给力，获取订单号失败！");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("company_id", userId + "");
				map.put("charge_money", money + "");
				map.put("charge_type", 0 + "");

				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	public void getServerPayResult() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST,
				aliPayResultUrl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss = js.getJSONObject("OrderResponse");
							int status = jss.getInt("status");
							if (status == 1) {
								showToast("支付成功！");
							} else {
								showToast("支付失败！");
							}
						} catch (JSONException e) {
							e.printStackTrace();
							showToast("支付结果确认中！");
							System.out
									.println("==================reg json 异常===========");
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showWait(false);
						showToast("支付失败！501");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("out_trade_no", out_trade_no + "");
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	// ===================支付宝=======================
	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay() {
		// 订单
		String orderInfo = getOrderInfo("充值", "兼职达人个人账户充值" + money + "元", money
				+ "");

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(RechargeActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(RechargeActivity.this);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + carsonGetOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\""
				+ Url.COMPANY_recharge_AliPayAsynNotify + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String carsonGetOutTradeNo() {
		return "c" + out_trade_no;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	// ==================支付宝end==================
}
