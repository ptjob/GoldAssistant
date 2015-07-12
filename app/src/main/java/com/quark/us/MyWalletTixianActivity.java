package com.quark.us;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carson.constant.ConstantForSaveList;
import com.carson.https.HttpsUtils;
import com.droid.carson.BankActivity;
import com.qingmu.jianzhidaren.R;
import com.quark.common.ToastUtil;
import com.quark.common.Url;
import com.quark.ui.widget.CustomDialogTixian;
import com.quark.utils.Util;
import com.quark.utils.WaitDialog;

public class MyWalletTixianActivity extends Activity implements OnClickListener {

	private TextView availibleMoneyTv, nameTv, bankNameTv;// 可用余额
	private EditText zfbAccountEdt, bankCardEdt, bankZhihangEdt;
	private EditText tiquMoneyEdt;// 提取金额
	private LinearLayout zfbLayout, bankLayout;// 支付宝模块、银行卡模块
	private ImageView zfbAccountDelImv, bankCardDelImv, bankZhihangDelImv;
	boolean isZhiFuBao;
	private Button subBtn;// 提交
	private String thisToken;
	private String tokenUrl;// 获取token
	private String tiXianUrl;// 提现url
	private String user_id;
	private SharedPreferences sp;
	private String name;// 姓名
	private String userMoney;// 余额
	private boolean idcardFlag = false;// 默认身份证后6位填写错误
	private String myIdcardValue;// 身份证后6位
	private WaitDialog dialog;// 提交缓冲框
	private RelativeLayout bankNameRelayout;// 银行名称
	private ImageView bankIconImv;// 银行图标
	private TextView tipsChooseBankTv;// 请选择银行的提示
	private TextView bankNameTipsTv;// 银行名称
	private Drawable[] iconDrawables;// 银行icon

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_wallet_tixian);
		name = getIntent().getStringExtra("name");
		userMoney = getIntent().getStringExtra("userMoney");
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		user_id = sp.getString("userId", "");
		isZhiFuBao = getIntent().getExtras().getBoolean("isZhiFuBao");
		tokenUrl = Url.USER_GET_TOKEN;
		tiXianUrl = Url.USER_DRAW_MONEY;
		setBackButton();
		initView();
		initData();
		iconDrawables = new Drawable[] {
				getResources().getDrawable(R.drawable.bank_01),
				getResources().getDrawable(R.drawable.bank_02),
				getResources().getDrawable(R.drawable.bank_03),
				getResources().getDrawable(R.drawable.bank_04),
				getResources().getDrawable(R.drawable.bank_05),
				getResources().getDrawable(R.drawable.bank_06),
				getResources().getDrawable(R.drawable.bank_07),
				getResources().getDrawable(R.drawable.bank_08),
				getResources().getDrawable(R.drawable.bank_09),
				getResources().getDrawable(R.drawable.bank_10),
				getResources().getDrawable(R.drawable.bank_11),
				getResources().getDrawable(R.drawable.bank_12),
				getResources().getDrawable(R.drawable.bank_13),
				getResources().getDrawable(R.drawable.bank_14),
				getResources().getDrawable(R.drawable.bank_15),
				getResources().getDrawable(R.drawable.bank_16),
				getResources().getDrawable(R.drawable.bank_17),
				getResources().getDrawable(R.drawable.bank_18),
				getResources().getDrawable(R.drawable.bank_19),
				getResources().getDrawable(R.drawable.bank_20) };
	}

	@Override
	protected void onResume() {
		super.onResume();
		getToken();

	}

	/**
	 * 检测本地输入框填写内容 type 1:支付宝 2:银行卡
	 * 
	 */
	private boolean check(int type) {
		if (type == 1) {
			// 提现帐号
			String temp_zfb_account_num = zfbAccountEdt.getText().toString();
			if (temp_zfb_account_num != null) {
				// 先判断是否全是数字
				if (Util.isNumeric(temp_zfb_account_num)) {
					if (!Util.isMobileNO(temp_zfb_account_num)) {
						ToastUtil.showShortToast(getResources().getString(
								R.string.wallet_account_no_vertify));
						return false;
					}

				} else {
					// 判断邮箱
					if (!Util.isEmail(temp_zfb_account_num)) {
						ToastUtil.showShortToast(getResources().getString(
								R.string.wallet_account_no_vertify));
						return false;
					}
				}

			} else {
				ToastUtil.showShortToast(getResources().getString(
						R.string.wallet_account_no_vertify));
				return false;
			}

		} else if (type == 2) {
			// 银行名称
			String tempBank = bankNameTv.getText().toString();
			if (tempBank != null) {
				if (tempBank.trim().length() < 4) {
					ToastUtil.showShortToast(getResources().getString(
							R.string.wallet_bank_not_availible));
					return false;
				}
			} else {
				ToastUtil.showShortToast(getResources().getString(
						R.string.wallet_bank_not_availible));
				return false;
			}
			// 银行卡号
			String tempBankNum = bankCardEdt.getText().toString();
			if (tempBankNum != null) {
				if (tempBankNum.length() < 10) {
					ToastUtil.showShortToast(getResources().getString(
							R.string.wallet_bank_card_not_availible));
					return false;
				}
				if (!Util.checkBankCard(tempBankNum)) {
					ToastUtil.showShortToast(getResources().getString(
							R.string.wallet_bank_card_not_availible));
					return false;
				}
			} else {
				ToastUtil.showShortToast(getResources().getString(
						R.string.wallet_bank_card_not_availible));
				return false;
			}
			// 开户支行
			String tempBankZhihang = bankZhihangEdt.getText().toString();

			if (tempBankZhihang != null) {
				if (tempBankZhihang.trim().length() < 4) {
					ToastUtil.showShortToast(getResources().getString(
							R.string.wallet_bank_zhihang_not_availible));
					return false;
				}
			} else {
				ToastUtil.showShortToast(getResources().getString(
						R.string.wallet_bank_zhihang_not_availible));
				return false;
			}
		} else {
			// 非支付宝、银行卡提现
			ToastUtil.showShortToast("异常状态");
			return false;
		}
		// 提现余额
		String temp_tiqu_money = tiquMoneyEdt.getText().toString();
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

	private void initView() {
		subBtn = (Button) findViewById(R.id.my_wallet_tixian_submit_btn);
		subBtn.setOnClickListener(this);
		zfbLayout = (LinearLayout) findViewById(R.id.my_wallet_tixian_zhifubao_layout);
		bankLayout = (LinearLayout) findViewById(R.id.my_wallet_tixian_bank_layout);
		TextView tt_tipTv = (TextView) findViewById(R.id.tixiantips_tv);
		if (isZhiFuBao) {
			tt_tipTv.setText(getResources().getString(R.string.wallet_zfb_tt_tips));
			zfbLayout.setVisibility(View.VISIBLE);
			bankLayout.setVisibility(View.GONE);
		} else {
			tt_tipTv.setText(getResources().getString(R.string.wallet_bank_tt_tips));
			zfbLayout.setVisibility(View.GONE);
			bankLayout.setVisibility(View.VISIBLE);
		}

		availibleMoneyTv = (TextView) findViewById(R.id.my_wallet_tixian_jine_tv);
		availibleMoneyTv.setText(userMoney);
		tiquMoneyEdt = (EditText) findViewById(R.id.my_wallet_tixian_money_edt);
		// 银行名称
		bankNameRelayout = (RelativeLayout) findViewById(R.id.my_wallet_tixian_bank_name_relayout);
		bankNameRelayout.setOnClickListener(this);
		bankNameTipsTv = (TextView) findViewById(R.id.bank_tv);// 银行名称
		bankIconImv = (ImageView) findViewById(R.id.my_wallet_tixian_bank_icon_imv);// 银行icon
		tipsChooseBankTv = (TextView) findViewById(R.id.my_wallet_tixian_input_bank_tv);// 请选择银行
		bankNameTv = (TextView) findViewById(R.id.my_wallet_tixian_bank_name_tv);// 选择好了的银行名称
		// 编辑框
		zfbAccountEdt = (EditText) findViewById(R.id.my_wallet_tixian_zhifubao_edt);
		bankCardEdt = (EditText) findViewById(R.id.my_wallet_tixian_bank_card_edt);
		bankZhihangEdt = (EditText) findViewById(R.id.my_wallet_tixian_bank_zhihang_edt);
		nameTv = (TextView) findViewById(R.id.my_wallet_tixian_name_tv);
		nameTv.setText(name);
		// 支付宝帐号编辑框设置内容改变监听
		zfbAccountEdt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if ("".equals(arg0.toString())) {
					zfbAccountDelImv.setVisibility(View.GONE);
				} else {
					zfbAccountDelImv.setVisibility(View.VISIBLE);
				}

			}
		});
		// 银行名称编辑框设置内容改变监听

		// 银行卡编辑框设置内容改变监听
		bankCardEdt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if ("".equals(arg0.toString())) {
					bankCardDelImv.setVisibility(View.GONE);
				} else {
					bankCardDelImv.setVisibility(View.VISIBLE);
				}

			}
		});

		// 开户支行编辑框设置内容改变监听
		bankZhihangEdt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if ("".equals(arg0.toString())) {
					bankZhihangDelImv.setVisibility(View.GONE);
				} else {
					bankZhihangDelImv.setVisibility(View.VISIBLE);
				}

			}
		});
		// 编辑框有内容出现删除icon
		zfbAccountDelImv = (ImageView) findViewById(R.id.my_wallet_tixian_zhifubao_del_imv);
		bankCardDelImv = (ImageView) findViewById(R.id.my_wallet_tixian_bank_card_del_imv);
		bankZhihangDelImv = (ImageView) findViewById(R.id.my_wallet_tixian_bank_zhihang_del_imv);
		zfbAccountDelImv.setOnClickListener(this);
		bankCardDelImv.setOnClickListener(this);
		bankZhihangDelImv.setOnClickListener(this);

	}

	private void initData() {

	}

	/**
	 * 设置返回按钮
	 */
	public void setBackButton() {
		TextView titiTv = (TextView) findViewById(R.id.title);
		if (isZhiFuBao)
			titiTv.setText("提现到支付宝");
		else
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

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.my_wallet_tixian_zhifubao_del_imv:
			zfbAccountEdt.setText("");
			break;
		case R.id.my_wallet_tixian_bank_card_del_imv:
			bankCardEdt.setText("");
			break;
		case R.id.my_wallet_tixian_bank_zhihang_del_imv:
			bankZhihangEdt.setText("");
			break;
		case R.id.my_wallet_tixian_submit_btn:
			// 提交时先判断
			int type = 0;
			type = isZhiFuBao ? 1 : 2;
			if (check(type)) {
				CustomDialogTixian.Builder builder = new CustomDialogTixian.Builder(
						this,
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
									ToastUtil.showShortToast("请输入正确的身份证后6位");
								}
							}
						});

				builder.setTitle("提现申请密码");
				BigDecimal bg = new BigDecimal(tiquMoneyEdt.getText()
						.toString().trim());
				String f2 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
				builder.setMoney(f2 + "元");
				if (isZhiFuBao) {
					builder.setAccountNum(zfbAccountEdt.getText().toString());
				} else {
					builder.setAccountNum(bankCardEdt.getText().toString());
				}
				builder.setPositiveButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("提现",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								if (idcardFlag) {
									dialog.dismiss();
									submitMoney();
								}

							}
						});
				builder.create().show();
			}
			break;
		case R.id.my_wallet_tixian_bank_name_relayout:
			Intent intent = new Intent(MyWalletTixianActivity.this,
					BankActivity.class);
			startActivityForResult(intent, 110);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 110) {
			if (resultCode == RESULT_OK) {
				int bankCode = data.getExtras().getInt("bank", 0);
				if (bankCode >= 0) {
					bankNameTv
							.setText(ConstantForSaveList.nativeBankList[bankCode]);
					tipsChooseBankTv.setVisibility(View.GONE);
					bankNameTipsTv.setVisibility(View.INVISIBLE);
					bankIconImv.setImageDrawable(iconDrawables[bankCode]);
					bankIconImv.setVisibility(View.VISIBLE);
				}
			}
		}
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
						params.add(new BasicNameValuePair("user_id", user_id));
						// type 1
						if (isZhiFuBao) {
							params.add(new BasicNameValuePair("type", "1"));
							params.add(new BasicNameValuePair("account_num",
									zfbAccountEdt.getText().toString().trim()));
						} else {
							params.add(new BasicNameValuePair("type", "2"));
							params.add(new BasicNameValuePair("bank",
									bankNameTv.getText().toString().trim()));
							params.add(new BasicNameValuePair("bank_branch",
									bankZhihangEdt.getText().toString().trim()));
							params.add(new BasicNameValuePair("account_num",
									bankCardEdt.getText().toString().trim()));
						}
						BigDecimal bg = new BigDecimal(tiquMoneyEdt.getText()
								.toString().trim());
						String f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP)
								.toString();
						params.add(new BasicNameValuePair("money", f1));
						// params.add(new BasicNameValuePair("remark", ""));
						params.add(new BasicNameValuePair("account_name",
								nameTv.getText().toString().trim()));
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
							MyWalletTixianActivity.this.finish();
						} else if (status == 9) {
							ToastUtil.showShortToast(msg);
							getToken();
						} else {
							ToastUtil.showShortToast(getResources().getString(
									R.string.wallet_account_error));
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

	protected void showWait(boolean isShow) {
		if (isShow) {
			if (null == dialog) {
				dialog = new WaitDialog(MyWalletTixianActivity.this);
			}
			dialog.show();
		} else {
			if (null != dialog) {
				dialog.dismiss();
			}
		}
	}

}
