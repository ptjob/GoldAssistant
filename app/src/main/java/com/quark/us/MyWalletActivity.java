package com.quark.us;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carson.https.HttpsUtils;
import com.qingmu.jianzhidaren.R;
import com.quark.adapter.WalletLiushuiAdapter;
import com.quark.common.JsonUtil;
import com.quark.common.ToastUtil;
import com.quark.common.Url;
import com.quark.http.image.CircularImage;
import com.quark.http.image.LoadImage;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.model.BillRecordBean;
import com.quark.utils.NetWorkCheck;

public class MyWalletActivity extends BaseActivity implements
		IXListViewListener, OnClickListener {

	private CircularImage cover_user_photo;// 头像
	private TextView nameTv, teleTv, yueTv, jiaoNaTv, tiXianTv;
	private SharedPreferences sp;
	private String user_id;
	private static XListView listView;
	private WalletLiushuiAdapter adapter;
	int pageNumber = 1;
	int currentCount = 1;
	int page_size = 5;
	private String url;
	ArrayList<BillRecordBean> liushuiList = new ArrayList<BillRecordBean>();
	private int renzheng;// 是否已经实名认证
	private String name;// 用户姓名
	private String userMoney;// 当前余额
	private String telephone;// 手机号码

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_wallet);
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		url = Url.USER_LIST_BILL;
		user_id = sp.getString("userId", "");
		// 从上个界面获取认证信息和姓名
		renzheng = getIntent().getIntExtra("renzheng", 0);
		name = getIntent().getExtras().getString("name", "");
		telephone = getIntent().getExtras().getString("telephone", "");
		setBackButton();
		initView();
		init();
	}

	private void initView() {
		cover_user_photo = (CircularImage) findViewById(R.id.my_wallet_cover_user_photo);
		nameTv = (TextView) findViewById(R.id.my_wallet_name_tv);
		nameTv.setText(name);
		teleTv = (TextView) findViewById(R.id.my_wallet_tel_tv);
		if (telephone != null && telephone.trim().length() >= 11) {
			teleTv.setText(telephone.subSequence(0, 3) + "****"
					+ telephone.substring(7));
		}
		yueTv = (TextView) findViewById(R.id.my_wallet_yue_tv);
		jiaoNaTv = (TextView) findViewById(R.id.my_wallet_jiaona_tv);
		tiXianTv = (TextView) findViewById(R.id.my_wallet_tixian_tv);
		jiaoNaTv.setOnClickListener(this);
		tiXianTv.setOnClickListener(this);

	}

	private void init() {
		loadNativePhotoFirst();
		listView = (XListView) findViewById(R.id.liushui_list);
		listView.setPullLoadEnable(true);
		adapter = new WalletLiushuiAdapter(MyWalletActivity.this, liushuiList);
		listView.setAdapter(adapter);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 传递流水账单记录到账单详情界面
				Intent intent = new Intent(MyWalletActivity.this,
						MyWalletBillRecordDetailActivity.class);
				if (liushuiList != null && liushuiList.size() > 0
						&& position > 0) {
					intent.putExtra("BillRecordBean",
							liushuiList.get(position - 1));
					startActivity(intent);
				}

			}
		});

	}

	/**
	 * 初次进来先加载本地缓存头像
	 * 
	 */
	private void loadNativePhotoFirst() {
		File mePhotoFold = new File(Environment.getExternalStorageDirectory()
				+ "/" + "jzdr/" + "image");
		if (!mePhotoFold.exists()) {
			mePhotoFold.mkdirs();
		}
		File picture_1 = new File(Environment.getExternalStorageDirectory()
				+ "/" + "jzdr/" + "image/"
				+ sp.getString(user_id + "picture_1", "c"));

		if (picture_1.exists()) {
			// 加载本地图片
			final Bitmap bb_bmp = BitmapFactory.decodeFile(Environment
					.getExternalStorageDirectory()
					+ "/"
					+ "jzdr/"
					+ "image/"
					+ sp.getString(user_id + "picture_1", "c"));
			if (bb_bmp != null) {
				cover_user_photo
						.setImageBitmap(LoadImage.toRoundBitmap(bb_bmp));
			}
		}
	}

	/**
	 * 设置返回按钮
	 */
	public void setBackButton() {

		TextView titiTv = (TextView) findViewById(R.id.title);
		titiTv.setText("我的钱包");
		LinearLayout back_lay = (LinearLayout) findViewById(R.id.left);
		back_lay.setVisibility(View.VISIBLE);
		back_lay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	// ========================xlist=================================================
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// listView.setLoadOver(currentCount, page_size);// 用于是否加载完了
			// adapter.notifyDataSetChanged();
			// onLoad();
			switch (msg.what) {
			case 0:
				showWait(true);
				break;
			case 1:
				showWait(false);

			default:
				break;
			}
		};
	};

	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime("刚刚");
	}

	/**
	 * 这个是手指放最上面下拉刷新数据
	 */
	@Override
	public void onRefresh() {
		pageNumber = 1;
		liushuiList.clear();
		getData();

	}

	/**
	 * 底部上滑动加载更多
	 */
	@Override
	public void onLoadMore() {
		pageNumber++;
		getData();
	}

	// ======xlist end============
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.my_wallet_jiaona_tv:
			Intent intent = new Intent(MyWalletActivity.this,
					LocalCarouselActivity.class);
			intent.putExtra("type", 3 + "");
			startActivity(intent);

			break;
		case R.id.my_wallet_tixian_tv:
			if (renzheng == 2) {
				if (NetWorkCheck.isOpenNetwork(MyWalletActivity.this)) {
					Intent intent2 = new Intent(MyWalletActivity.this,
							MyWalletPreTixianActivity.class);
					intent2.putExtra("name", name);
					intent2.putExtra("userMoney", userMoney);
					startActivity(intent2);
				} else {
					Toast mToast = Toast.makeText(MyWalletActivity.this,
							"不能连接到网络", Toast.LENGTH_SHORT);
					mToast.setGravity(Gravity.CENTER, 0, 0);
					mToast.show();
				}

			} else {
				ToastUtil.showLongToast(getResources().getString(
						R.string.wallet_no_realname));
			}
			break;
		default:
			break;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		pageNumber = 1;
		liushuiList.clear();
		getData();
	}

	/**
	 * 获取流水记录
	 */
	private void getData() {
		new Thread() {
			public void run() {
				if (!"".equals(user_id)) {
					handler.sendEmptyMessage(0);
					try {
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("user_id", user_id));
						params.add(new BasicNameValuePair("pn", String
								.valueOf(pageNumber)));
						params.add(new BasicNameValuePair("page_size", String
								.valueOf(page_size)));
						String result = HttpsUtils.doHttpsPost(url, params);// 获取返回结果
						JSONObject js = new JSONObject(result);
						JSONObject jss = js.getJSONObject("responseValues");
						userMoney = jss.getString("user_money");
						JSONObject jsss = jss.getJSONObject("billPage");
						JSONArray jssss = jsss.getJSONArray("list");
						if (jssss.length() > 0) {
							for (int i = 0; i < jssss.length(); i++) {
								BillRecordBean billRecordBean = new BillRecordBean();
								billRecordBean = (BillRecordBean) JsonUtil
										.jsonToBean(jssss.getJSONObject(i),
												BillRecordBean.class);
								liushuiList.add(billRecordBean);
							}
						}
						currentCount = jssss.length();
						runOnUiThread(new Runnable() {
							public void run() {
								adapter = new WalletLiushuiAdapter(
										MyWalletActivity.this, liushuiList);
								listView.setAdapter(adapter);
								yueTv.setText(userMoney);
								listView.setLoadOver(currentCount, page_size);// 用于是否加载完了
								// adapter.notifyDataSetChanged();
								onLoad();
								showWait(false);
							}
						});
					} catch (Exception e) {
						runOnUiThread(new Runnable() {
							public void run() {
								adapter = new WalletLiushuiAdapter(
										MyWalletActivity.this, liushuiList);
								listView.setAdapter(adapter);
								listView.setLoadOver(currentCount, page_size);// 用于是否加载完了
								// adapter.notifyDataSetChanged();
								onLoad();
								showWait(false);
							}
						});
						e.printStackTrace();
					}
				}
			}
		}.start();

	}
}
