package com.quark.us;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carson.https.HttpsUtils;
import com.qingmu.jianzhidaren.R;
import com.quark.common.ToastUtil;
import com.quark.common.Url;
import com.quark.model.BankCardBean;
import com.quark.ui.widget.CustomDialogTixian;

public class MyWalletHisTixianActivity extends Activity {

	private TextView nameTv, accountNumTv, bankTv, availibTv;
	private EditText moneyEdt;
	private BankCardBean bankCardBean = new BankCardBean();
	private Button submitBtn;// 提现
	private String thisToken;
	private String tokenUrl;// 获取token
	private String tiXianUrl;// 提现url
	private String user_id;
	private SharedPreferences sp;
	private String name;// 姓名
	private String userMoney;// 余额
	private boolean idcardFlag = false;// 默认身份证后6位填写错误
	private String myIdcardValue;// 身份证后6位

	@Override
	protected void onResume() {
		super.onResume();
		getToken();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_wallet_his_tixian);
		bankCardBean = (BankCardBean) getIntent().getSerializableExtra(
				"bankCardBean");
		name = getIntent().getStringExtra("name");
		userMoney = getIntent().getStringExtra("userMoney");
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		user_id = sp.getString("userId", "");
		tokenUrl = Url.USER_GET_TOKEN;
		tiXianUrl = Url.USER_DRAW_MONEY;
		setBackButton();
		initView();
	}

	private void initView() {
		nameTv = (TextView) findViewById(R.id.my_wallet_tixian_his_name_tv);
		nameTv.setText(name);
		accountNumTv = (TextView) findViewById(R.id.my_wallet_tixian_his_bankcard_num_tv);
		accountNumTv.setText(bankCardBean.getAccount_num());
		bankTv = (TextView) findViewById(R.id.my_wallet_tixian_his_bank_name_tv);
		if (1 == bankCardBean.getType()) {
			bankTv.setText("支付宝");
		} else if (2 == bankCardBean.getType()) {
			bankTv.setText(bankCardBean.getBank());
		}
		availibTv = (TextView) findViewById(R.id.my_wallet_tixian_his_current_money_tv);
		availibTv.setText(userMoney);
		moneyEdt = (EditText) findViewById(R.id.my_wallet_tixian_his_money_edt);
		submitBtn = (Button) findViewById(R.id.my_wallet_tixian_his_submit_btn);
		submitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (check()) {
					CustomDialogTixian.Builder builder = new CustomDialogTixian.Builder(
							MyWalletHisTixianActivity.this,
							new CustomDialogTixian.Builder.CustomDialogListener() {
								@Override
								public void getEdtValue(String idcardValue) {
									if (idcardValue != null) {
										myIdcardValue = idcardValue;
										if (idcardValue.trim().length() != 6) {
											ToastUtil
													.showShortToast("请输入正确的身份证后6位");
											idcardFlag = false;
										} else {
											idcardFlag = true;
										}
									} else {
										idcardFlag = false;
										ToastUtil
												.showShortToast("请输入正确的身份证后6位");
									}
								}
							});

					builder.setTitle("提现申请密码");
					BigDecimal bg = new BigDecimal(moneyEdt.getText()
							.toString().trim());
					String f2 = bg.setScale(2, BigDecimal.ROUND_HALF_UP)
							.toString();
					builder.setMoney(f2 + "元");
					builder.setAccountNum(bankCardBean.getAccount_num());
					builder.setPositiveButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int arg1) {
									dialog.dismiss();
								}
							});
					builder.setNegativeButton("提现",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int arg1) {
									if (idcardFlag) {
										dialog.dismiss();
										submitMoney();
									}

								}
							});
					builder.create().show();
				}

			}
		});

	}

	/**
	 * 验证输入金额是否合法
	 */
	private boolean check() {
		// 提现余额
		String temp_tiqu_money = moneyEdt.getText().toString();
		if (temp_tiqu_money != null) {
			// 判断提取金额是否大于余额
			double availibleMoney = 0;
			try {
				availibleMoney = Double.parseDouble(userMoney);
			} catch (Exception e) {
				availibleMoney = 0;
			}

			if (availibleMoney <= 0) {
				// 帐号余额为0
				ToastUtil.showShortToast(getResources().getString(
						R.string.wallet_money_not_availible));
				return false;
			}

			// 提取金额
			double tempMoney = 0;
			try {
				tempMoney = Double.parseDouble(temp_tiqu_money);
			} catch (Exception e) {
				tempMoney = 0;
			}
			if (tempMoney <= 0) {
				ToastUtil.showShortToast(getResources().getString(
						R.string.wallet_money_no_input));
				return false;
			}
			if (availibleMoney < tempMoney) {
				ToastUtil.showShortToast(getResources().getString(
						R.string.wallet_money_not_availible));
				return false;
			}
		} else {
			// 金额没有输入
			ToastUtil.showShortToast(getResources().getString(
					R.string.wallet_money_no_input));
			return false;
		}
		return true;
	}

	/**
	 * 设置返回按钮
	 */
	public void setBackButton() {
		TextView titiTv = (TextView) findViewById(R.id.title);
		if (bankCardBean.getType() == 1)
			titiTv.setText("提现到支付宝");
		else if (bankCardBean.getType() == 2)
			titiTv.setText("提现到银行卡");
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
	 * getToken
	 */
	private void getToken() {
		new Thread() {
			public void run() {
				if (!"".equals(user_id)) {
					try {
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("user_id", user_id));
						String result = HttpsUtils
								.doHttpsPost(tokenUrl, params);// 获取返回结果
						JSONObject js = new JSONObject(result);
						thisToken = js.getString("push_token");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();

	}

	/**
	 * 提交提现申请
	 * 
	 */
	private void submitMoney() {
		new Thread() {
			public void run() {
				if (!"".equals(user_id)) {
					try {
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						Log.e("type-num", bankCardBean.getType() + ":"
								+ bankCardBean.getAccount_num() + ":"
								+ bankCardBean.getAccount_name() + ":"
								+ bankCardBean.getBank());
						params.add(new BasicNameValuePair("user_id", user_id));
						// type 1
						if (1 == bankCardBean.getType()) {
							params.add(new BasicNameValuePair("type", "1"));
						} else if (2 == bankCardBean.getType()) {
							params.add(new BasicNameValuePair("type", "2"));
							params.add(new BasicNameValuePair("bank",
									bankCardBean.getBank()));
							// params.add(new BasicNameValuePair("bank_branch",
							// bankCardBean.getBank_branch()));
						}
						params.add(new BasicNameValuePair("account_num",
								bankCardBean.getAccount_num()));
						BigDecimal bg = new BigDecimal(moneyEdt.getText()
								.toString().trim());
						String f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP)
								.toString();
						params.add(new BasicNameValuePair("money", f1));
						// params.add(new BasicNameValuePair("remark", ""));
						params.add(new BasicNameValuePair("account_name",
								bankCardBean.getAccount_name()));
						params.add(new BasicNameValuePair("push_token",
								thisToken));
						params.add(new BasicNameValuePair("password",
								myIdcardValue));
						String result = HttpsUtils.doHttpsPost(tiXianUrl,
								params);// 获取返回结果
						// 解析json
						JSONObject js = new JSONObject(result);
						JSONObject jss = js.getJSONObject("ResponseStatus");
						int status = jss.getInt("status");
						String msg = jss.getString("msg");// 后端提示
						if (status == 3) {
							// 密码错误
							ToastUtil.showShortToast(msg);
							getToken();
						} else if (status == 1) {
							// 提交成功
							ToastUtil.showShortToast(msg);
							MyWalletHisTixianActivity.this.finish();
						} else if (status == 9) {
							// 重复提交
							ToastUtil.showShortToast(msg);
							getToken();
						} else {
							ToastUtil.showShortToast(msg);
							getToken();
						}

					} catch (Exception e) {
						e.printStackTrace();
						ToastUtil.showShortToast(getResources().getString(
								R.string.wallet_net_error));
						getToken();
					}
				}
			}
		}.start();

	}
}
