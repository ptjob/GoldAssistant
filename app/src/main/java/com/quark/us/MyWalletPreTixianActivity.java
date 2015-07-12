package com.quark.us;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.carson.https.HttpsUtils;
import com.qingmu.jianzhidaren.R;
import com.quark.adapter.WalletBankCardAdapter;
import com.quark.common.JsonUtil;
import com.quark.common.Url;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.model.BankCardBean;
import com.quark.utils.NetWorkCheck;

public class MyWalletPreTixianActivity extends BaseActivity implements
		OnClickListener {
	private ImageView zfbImv, yhkImv;// 支付宝支付、银行卡支付
	private ListView listView;// 曾经转账过的帐号信息列表
	private WalletBankCardAdapter adapter;
	private ArrayList<BankCardBean> bankCardList = new ArrayList<BankCardBean>();
	private SharedPreferences sp;
	private String url;
	private String user_id;
	private String name;// 姓名
	private String userMoney;// 余额
	private String deleteQianbaoUrl;// 删除绑定卡号

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_wallet_pre_tixian);
		setBackButton();
		name = getIntent().getStringExtra("name");
		userMoney = getIntent().getStringExtra("userMoney");
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		url = Url.USER_LIST_ACCOUNT;
		deleteQianbaoUrl = Url.USER_DELETE_ACCOUNT;
		user_id = sp.getString("userId", "");
		initView();

	}

	@Override
	protected void onResume() {
		super.onResume();
		bankCardList.clear();
		getHistoryAccount();
	}

	private void initView() {
		zfbImv = (ImageView) findViewById(R.id.my_wallet_pre_zfb_imv);
		yhkImv = (ImageView) findViewById(R.id.my_wallet_pre_yhk_imv);
		zfbImv.setOnClickListener(this);
		yhkImv.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.my_wallet_pre_listview);
		registerForContextMenu(listView);// 注册listview长按监听
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if (NetWorkCheck.isOpenNetwork(MyWalletPreTixianActivity.this)) {
					Intent intent = new Intent(MyWalletPreTixianActivity.this,
							MyWalletHisTixianActivity.class);
					intent.putExtra("bankCardBean", bankCardList.get(arg2));
					intent.putExtra("name", name);// 姓名
					intent.putExtra("userMoney", userMoney);// 余额
					// 跳转到之前已有记录的银行提款界面
					startActivity(intent);
					MyWalletPreTixianActivity.this.finish();
				} else {
					Toast mToast = Toast.makeText(
							MyWalletPreTixianActivity.this, "不能连接到网络",
							Toast.LENGTH_SHORT);
					mToast.setGravity(Gravity.CENTER, 0, 0);
					mToast.show();
				}

			}
		});

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v == listView) {
			// 取消录取人员
			getMenuInflater().inflate(R.menu.cancel_qianbao, menu);
		}
	}

	@Override
	public boolean onContextItemSelected(final MenuItem item) {
		// 取消绑定钱包卡
		if (item.getItemId() == R.id.cancel_qianbao) {
			if (bankCardList != null && bankCardList.size() > 0) {
				new Thread() {
					public void run() {
						if (!"".equals(user_id)) {
							try {
								mHandler.sendEmptyMessage(0);
								List<NameValuePair> params = new ArrayList<NameValuePair>();
								params.add(new BasicNameValuePair("user_id",
										user_id));
								params.add(new BasicNameValuePair(
										"account_id",
										bankCardList
												.get((((AdapterContextMenuInfo) item
														.getMenuInfo()).position))
												.getAccount_id()
												+ ""));
								String result = HttpsUtils.doHttpsPost(
										deleteQianbaoUrl, params);// 获取返回结果
								JSONObject js = new JSONObject(result);
								JSONObject jss = js
										.getJSONObject("ResponseStatus");
								String status = jss.getString("status");
								final String msg = jss.getString("msg");
								if ("1".equals(status)) {
									// 删除成功
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											showToast(msg);
											bankCardList
													.remove(((AdapterContextMenuInfo) item
															.getMenuInfo()).position);
											mHandler.sendEmptyMessage(1);
										}
									});

								} else {
									// 删除失败
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											showToast(msg);
											mHandler.sendEmptyMessage(2);
										}
									});

								}

							} catch (Exception e) {
								e.printStackTrace();
								mHandler.sendEmptyMessage(2);
							}
						}
					}
				}.start();

			}
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * 设置返回按钮
	 */
	public void setBackButton() {

		TextView titiTv = (TextView) findViewById(R.id.title);
		titiTv.setText("我要提现");
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
		case R.id.my_wallet_pre_zfb_imv:
			if (NetWorkCheck.isOpenNetwork(MyWalletPreTixianActivity.this)) {
				Intent intent = new Intent(MyWalletPreTixianActivity.this,
						MyWalletTixianActivity.class);
				intent.putExtra("isZhiFuBao", true);// 跳转到支付宝or银行卡
				intent.putExtra("name", name);// 姓名
				intent.putExtra("userMoney", userMoney);// 余额
				startActivity(intent);
				MyWalletPreTixianActivity.this.finish();
			} else {
				Toast mToast = Toast.makeText(MyWalletPreTixianActivity.this,
						"不能连接到网络", Toast.LENGTH_SHORT);
				mToast.setGravity(Gravity.CENTER, 0, 0);
				mToast.show();
			}

			break;
		case R.id.my_wallet_pre_yhk_imv:
			if (NetWorkCheck.isOpenNetwork(MyWalletPreTixianActivity.this)) {
				Intent intent2 = new Intent(MyWalletPreTixianActivity.this,
						MyWalletTixianActivity.class);
				intent2.putExtra("isZhiFuBao", false);// 跳转到支付宝or银行卡
				intent2.putExtra("name", name);// 姓名
				intent2.putExtra("userMoney", userMoney);// 余额
				startActivity(intent2);
				MyWalletPreTixianActivity.this.finish();
			} else {
				Toast mToast = Toast.makeText(MyWalletPreTixianActivity.this,
						"不能连接到网络", Toast.LENGTH_SHORT);
				mToast.setGravity(Gravity.CENTER, 0, 0);
				mToast.show();
			}
			break;

		default:
			break;
		}

	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				showWait(true);
				break;
			case 1:
				adapter = new WalletBankCardAdapter(
						MyWalletPreTixianActivity.this, bankCardList);
				listView.setAdapter(adapter);
				showWait(false);
				break;
			case 2:
				showWait(false);
				break;

			default:
				break;
			}
		}

	};

	/**
	 * 获取历史账户
	 * 
	 */
	private void getHistoryAccount() {
		new Thread() {
			public void run() {
				if (!"".equals(user_id)) {
					try {
						mHandler.sendEmptyMessage(0);
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("user_id", user_id));
						String result = HttpsUtils.doHttpsPost(url, params);// 获取返回结果
						JSONObject js = new JSONObject(result);
						JSONObject jss = js.getJSONObject("responseValues");
						JSONArray jsss = jss.getJSONArray("acountList");
						if (jsss.length() > 0) {
							for (int i = 0; i < jsss.length(); i++) {
								BankCardBean bean = new BankCardBean();
								bean = (BankCardBean) JsonUtil.jsonToBean(
										jsss.getJSONObject(i),
										BankCardBean.class);
								bankCardList.add(bean);
							}
						}
						mHandler.sendEmptyMessage(1);

					} catch (Exception e) {
						e.printStackTrace();
						mHandler.sendEmptyMessage(1);
					}
				}
			}
		}.start();

	}
}
