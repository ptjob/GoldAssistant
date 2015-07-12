package com.quark.guangchang;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qingmu.jianzhidaren.R;
import com.quark.common.ToastUtil;
import com.quark.db.CityDB;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.ui.widget.ActionSheet;
import com.quark.ui.widget.ActionSheet.OnActionSheetSelected;
import com.quark.ui.widget.SaixuanUi;

/**
 * 广场-》筛选
 * 
 * @author Administrator
 * 
 */
@SuppressLint({ "ResourceAsColor", "NewApi" })
public class SaiXuanActivity extends BaseActivity implements
		OnActionSheetSelected, OnCancelListener, AMapLocationListener {

	@ViewInject(R.id.ttime_choose_xingqi)
	private TextView ttime_choose_xingqi;

	@ViewInject(R.id.ttime_choose_zhiding)
	private TextView ttime_choose_zhiding;

	//
	@ViewInject(R.id.time_choose_zhiding_layout)
	private LinearLayout time_choose_zhiding_layout;
	//
	@ViewInject(R.id.time_choose_xingqi_layout)
	private LinearLayout time_choose_xingqi_layout;

	@ViewInject(R.id.time_table)
	private TableLayout time_table;
	// 地点
	@ViewInject(R.id.didian_table)
	private TableLayout didian_layout;
	// 地点
	@ViewInject(R.id.type_table)
	private TableLayout type_table;

	@ViewInject(R.id.paytype_table)
	private TableLayout paytype_table;

	ArrayList<String> countrys = new ArrayList<String>();
	ArrayList<String> types = new ArrayList<String>();
	ArrayList<String> payTypes = new ArrayList<String>();
	ArrayList<String> times = new ArrayList<String>();

	private String timeType = "xingqi";
	// 用于记录时
	ArrayList<String> chooseTimes = new ArrayList<String>();
	ArrayList<String> chooseCitys = new ArrayList<String>();
	ArrayList<String> chooseTypes = new ArrayList<String>();
	ArrayList<String> choosepayTypes = new ArrayList<String>();

	String choosetimeStr;
	String chooseCityStr;
	String chooseTypeStr;
	String choosePayTypeStr;
	String tempCity;
	private String city;

	int[] cityIds;
	int[] timeIds;
	int[] typeIds;
	int[] paytypeIds;
	// 添加最新、最近兼职筛选
	private boolean newJianZhiFlag = true;// 默认是最新兼职
	private TextView newJianZhiTv, nearlyJianZhiTv;// 最新最近兼职
	private SharedPreferences sp;
	// ===========高德地图=================
	private LocationManagerProxy mLocationManagerProxy;
	private double lat, lng;// 经纬度

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
		setContentView(R.layout.guangchang_saixuanl);
		setTopTitle("筛  选");
		setBackButton();
		ViewUtils.inject(this);
		// 最新、最近兼职
		initGaoDe();// 初始化高德
		newJianZhiTv = (TextView) findViewById(R.id.shaixuan_new_tv);
		nearlyJianZhiTv = (TextView) findViewById(R.id.shaixuan_nearly_tv);
		newJianZhiTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				newJianZhiFlag = true;
				Drawable btnDrawable = getResources().getDrawable(
						R.color.head_color);
				Drawable btnDrawable2 = getResources().getDrawable(
						R.drawable.bord_saixun);
				newJianZhiTv.setBackgroundDrawable(btnDrawable);
				newJianZhiTv.setTextColor(getResources().getColor(
						R.color.body_color));
				nearlyJianZhiTv.setBackgroundDrawable(btnDrawable2);
				nearlyJianZhiTv.setTextColor(getResources().getColor(
						R.color.ziti_huise));

			}
		});
		nearlyJianZhiTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				newJianZhiFlag = false;
				Drawable btnDrawable = getResources().getDrawable(
						R.color.head_color);
				Drawable btnDrawable2 = getResources().getDrawable(
						R.drawable.bord_saixun);
				newJianZhiTv.setBackgroundDrawable(btnDrawable2);
				newJianZhiTv.setTextColor(getResources().getColor(
						R.color.ziti_huise));
				nearlyJianZhiTv.setBackgroundDrawable(btnDrawable);
				nearlyJianZhiTv.setTextColor(getResources().getColor(
						R.color.body_color));

			}
		});

		sp = getSharedPreferences("jrdr.setting",
				android.content.Context.MODE_PRIVATE);
		city = sp.getString("city", "深圳");
		// 接口要求 去掉市字
		tempCity = city;
		if (city.endsWith("市")) {
			tempCity = city.substring(0, city.length() - 1);
		}
		// 城市view的id是用1开头 规则是i+1001
		countrys = CityDB.getCitys(SaiXuanActivity.this, tempCity);

		SaixuanUi.initDidian(this, countrys, didian_layout, cityOnclick, 1);
		// chooseCitys.add("全"+tempCity);

		cityIds = new int[countrys.size()];
		// 构建城市的 view 数组
		for (int i = 0; i < countrys.size(); i++) {
			cityIds[i] = i + 1001;
		}

		// 时间2开头
		gettimes();
		SaixuanUi.initDidian(this, times, time_table, timeOnclick, 2);
		timeIds = new int[times.size()];
		for (int i = 0; i < times.size(); i++) {
			timeIds[i] = i + 2001;
		}
		// 类型3开头
		getType();
		SaixuanUi.initDidian(this, types, type_table, typeOnclick, 3);
		typeIds = new int[types.size()];
		for (int i = 0; i < types.size(); i++) {
			typeIds[i] = i + 3001;
		}
		// 支付类型4开头
		getPayTypes();
		SaixuanUi.initDidian(this, payTypes, paytype_table, payTypeOnclick, 4);
		paytypeIds = new int[payTypes.size()];
		for (int i = 0; i < payTypes.size(); i++) {
			paytypeIds[i] = i + 4001;
		}

		Date da = new Date();
		SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");

		date.setText(simp.format(da));
	}

	public void getType() {
		types.remove(types);
		types.add(" 全  部 ");
		types.add(" 派  发 ");
		types.add(" 促  销 ");
		types.add(" 家  教 ");
		types.add("服务员");
		types.add(" 礼  仪 ");
		types.add("安保人员");
		types.add("模特");
		types.add("主持");
		types.add("翻译");
		types.add("工作人员");
		types.add("话务");
		types.add("充场");
		types.add("演艺");
		types.add("访谈");
		types.add("其他");
	}

	public void getPayTypes() {
		payTypes.remove(payTypes);
		payTypes.add(" 不  限 ");
		payTypes.add(" 周  结 ");
		payTypes.add(" 日  结 ");
		payTypes.add(" 月  结 ");
		payTypes.add("完工结");
	}

	public void gettimes() {
		times.remove(times);
		times.add(" 不  限 ");
		times.add(" 周 末 ");
		times.add(" 节假日 ");
	}

	// 星期
	@OnClick(R.id.ttime_choose_xingqi)
	public void ttime_choose_xingqionClick(View v) {
		timeType = "xingqi";
		ttime_choose_xingqi.setBackgroundResource(R.drawable.btn_tab_left_on);
		ttime_choose_xingqi.setTextColor(getResources().getColor(
				R.color.body_color));
		ttime_choose_zhiding
				.setBackgroundResource(R.drawable.btn_tab_right_off);
		ttime_choose_zhiding.setTextColor(getResources().getColor(
				R.color.head_color));
		time_choose_xingqi_layout.setVisibility(View.VISIBLE);
		time_choose_zhiding_layout.setVisibility(View.GONE);
	}

	@ViewInject(R.id.date)
	TextView date;

	// 指定
	@OnClick(R.id.time_choose_zhiding_layout)
	public void chooseDate(View v) {
		ActionSheet.showSheetTime2(SaiXuanActivity.this, SaiXuanActivity.this,
				SaiXuanActivity.this, date);
	}

	// 指定
	@OnClick(R.id.ttime_choose_zhiding)
	public void ttime_choose_zhidingonClick(View v) {
		timeType = "zhiding";
		ttime_choose_xingqi.setBackgroundResource(R.drawable.btn_tab_left_off);
		ttime_choose_xingqi.setTextColor(getResources().getColor(
				R.color.head_color));
		ttime_choose_zhiding.setBackgroundResource(R.drawable.btn_tab_right_on);
		ttime_choose_zhiding.setTextColor(getResources().getColor(
				R.color.body_color));
		time_choose_xingqi_layout.setVisibility(View.GONE);
		time_choose_zhiding_layout.setVisibility(View.VISIBLE);
	}

	OnClickListener cityOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			TextView vv = (TextView) v;
			// 点击全部 清除其他所有状态 什么都没选为全xx
			if (vv.getText().toString().equals("全" + tempCity)) {
				for (int i = 1; i < cityIds.length; i++) {
					TextView tView = (TextView) findViewById(cityIds[i]);
					chooseCitys.clear();
					Resources resources = tView.getContext().getResources();
					Drawable btnDrawable = resources
							.getDrawable(R.drawable.bord_saixun);
					tView.setBackgroundDrawable(btnDrawable);
					((TextView) tView).setTextColor(resources
							.getColor(R.color.ziti_huise));
				}
			} else {// 点击其他 清除全部的状态
				chooseCitys.remove("全" + tempCity);
				TextView atView = (TextView) findViewById(cityIds[0]);
				Resources resources = atView.getContext().getResources();
				Drawable btnDrawable = resources
						.getDrawable(R.drawable.bord_saixun);
				atView.setBackgroundDrawable(btnDrawable);
				((TextView) atView).setTextColor(resources
						.getColor(R.color.ziti_huise));
			}

			if (chooseCitys.contains(vv.getText().toString())) {
				chooseCitys.remove(vv.getText().toString());
				Resources resources = v.getContext().getResources();
				Drawable btnDrawable = resources
						.getDrawable(R.drawable.bord_saixun);
				v.setBackgroundDrawable(btnDrawable);
				((TextView) v).setTextColor(resources
						.getColor(R.color.ziti_huise));
				if (chooseCitys.size() == 0) {// 全部为空的时候 全部高亮
					TextView atView = (TextView) findViewById(cityIds[0]);
					Drawable btnDrawablett = resources
							.getDrawable(R.color.choose_city);
					atView.setBackgroundDrawable(btnDrawablett);
					((TextView) atView).setTextColor(resources
							.getColor(R.color.body_color));
				}
			} else {
				if (!(vv.getText().toString().equals("全" + tempCity))) {
					chooseCitys.add(vv.getText().toString());
				}
				Resources resources = v.getContext().getResources();
				Drawable btnDrawable = resources
						.getDrawable(R.color.choose_city);
				v.setBackgroundDrawable(btnDrawable);
				((TextView) v).setTextColor(resources
						.getColor(R.color.body_color));
			}
		}
	};

	OnClickListener timeOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			TextView vv = (TextView) v;

			// 点击全部 清除其他所有状态 什么都没选为全xx
			if (vv.getText().toString().replace(" ", "").equals("不限")) {
				for (int i = 1; i < timeIds.length; i++) {
					TextView tView = (TextView) findViewById(timeIds[i]);
					chooseTimes.clear();
					Resources resources = tView.getContext().getResources();
					Drawable btnDrawable = resources
							.getDrawable(R.drawable.bord_saixun);
					tView.setBackgroundDrawable(btnDrawable);
					((TextView) tView).setTextColor(resources
							.getColor(R.color.ziti_huise));
				}
			} else { // 点击其他 清除全部的状态
				chooseTimes.remove("不限");
				TextView atView = (TextView) findViewById(timeIds[0]);
				Resources resources = atView.getContext().getResources();
				Drawable btnDrawable = resources
						.getDrawable(R.drawable.bord_saixun);
				atView.setBackgroundDrawable(btnDrawable);
				((TextView) atView).setTextColor(resources
						.getColor(R.color.ziti_huise));
			}

			if (chooseTimes.contains(vv.getText().toString())) {
				chooseTimes.remove(vv.getText().toString());
				TextView dt = (TextView) findViewById(1001);
				Resources resources = v.getContext().getResources();
				Drawable btnDrawable = resources
						.getDrawable(R.drawable.bord_saixun);
				v.setBackgroundDrawable(btnDrawable);
				((TextView) v).setTextColor(resources
						.getColor(R.color.ziti_huise));
				if (chooseTimes.size() == 0) {// 全部为空的时候 全部高亮
					TextView atView = (TextView) findViewById(timeIds[0]);
					Drawable btnDrawablett = resources
							.getDrawable(R.color.saixuan_sjian);
					atView.setBackgroundDrawable(btnDrawablett);
					((TextView) atView).setTextColor(resources
							.getColor(R.color.body_color));
				}
			} else {
				if (!(vv.getText().toString().replace(" ", "").equals("不限"))) {
					chooseTimes.add(vv.getText().toString());
				}
				Resources resources = v.getContext().getResources();
				Drawable btnDrawable = resources
						.getDrawable(R.color.saixuan_sjian);
				v.setBackgroundDrawable(btnDrawable);
				((TextView) v).setTextColor(resources
						.getColor(R.color.body_color));
			}
		}
	};

	OnClickListener typeOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			TextView vv = (TextView) v;

			// 点击全部 清除其他所有状态 什么都没选为全xx
			if (vv.getText().toString().replace(" ", "").equals("全部")) {
				for (int i = 1; i < typeIds.length; i++) {
					TextView tView = (TextView) findViewById(typeIds[i]);
					chooseTypes.clear();
					Resources resources = tView.getContext().getResources();
					Drawable btnDrawable = resources
							.getDrawable(R.drawable.bord_saixun);
					tView.setBackgroundDrawable(btnDrawable);
					((TextView) tView).setTextColor(resources
							.getColor(R.color.ziti_huise));
				}
			} else {// 点击其他 清除全部的状态
				chooseTypes.remove("全部");
				TextView atView = (TextView) findViewById(typeIds[0]);
				Resources resources = atView.getContext().getResources();
				Drawable btnDrawable = resources
						.getDrawable(R.drawable.bord_saixun);
				atView.setBackgroundDrawable(btnDrawable);
				((TextView) atView).setTextColor(resources
						.getColor(R.color.ziti_huise));

			}

			if (chooseTypes.contains(vv.getText().toString())) {
				chooseTypes.remove(vv.getText().toString());
				Resources resources = v.getContext().getResources();
				Drawable btnDrawable = resources
						.getDrawable(R.drawable.bord_saixun);
				v.setBackgroundDrawable(btnDrawable);
				((TextView) v).setTextColor(resources
						.getColor(R.color.ziti_huise));
				if (chooseTypes.size() == 0) {// 全部为空的时候 全部高亮
					TextView atView = (TextView) findViewById(typeIds[0]);
					Drawable btnDrawablett = resources
							.getDrawable(R.color.choose_type);
					atView.setBackgroundDrawable(btnDrawablett);
					((TextView) atView).setTextColor(resources
							.getColor(R.color.body_color));
				}
			} else {
				if (!(vv.getText().toString().replace(" ", "").equals("全部"))) {
					chooseTypes.add(vv.getText().toString());
				}
				Resources resources = v.getContext().getResources();
				Drawable btnDrawable = resources
						.getDrawable(R.color.choose_type);
				v.setBackgroundDrawable(btnDrawable);
				((TextView) v).setTextColor(resources
						.getColor(R.color.body_color));
			}
		}
	};

	OnClickListener payTypeOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			TextView vv = (TextView) v;

			// 点击全部 清除其他所有状态 什么都没选为全xx
			if (vv.getText().toString().replace(" ", "").equals("不限")) {
				for (int i = 1; i < paytypeIds.length; i++) {
					TextView tView = (TextView) findViewById(paytypeIds[i]);
					choosepayTypes.clear();
					Resources resources = tView.getContext().getResources();
					Drawable btnDrawable = resources
							.getDrawable(R.drawable.bord_saixun);
					tView.setBackgroundDrawable(btnDrawable);
					((TextView) tView).setTextColor(resources
							.getColor(R.color.ziti_huise));
				}
			} else { // 点击其他 清除全部的状态
				choosepayTypes.remove("不限");
				TextView atView = (TextView) findViewById(paytypeIds[0]);
				Resources resources = atView.getContext().getResources();
				Drawable btnDrawable = resources
						.getDrawable(R.drawable.bord_saixun);
				atView.setBackgroundDrawable(btnDrawable);
				((TextView) atView).setTextColor(resources
						.getColor(R.color.ziti_huise));
			}

			if (choosepayTypes.contains(vv.getText().toString())) {
				choosepayTypes.remove(vv.getText().toString());
				Resources resources = v.getContext().getResources();
				Drawable btnDrawable = resources
						.getDrawable(R.drawable.bord_saixun);
				v.setBackgroundDrawable(btnDrawable);
				((TextView) v).setTextColor(resources
						.getColor(R.color.ziti_huise));
				if (choosepayTypes.size() == 0) { // 全部为空的时候 全部高亮
					TextView atView = (TextView) findViewById(paytypeIds[0]);
					Drawable btnDrawablett = resources
							.getDrawable(R.color.choose_paytype);
					atView.setBackgroundDrawable(btnDrawablett);
					((TextView) atView).setTextColor(resources
							.getColor(R.color.body_color));
				}
			} else {
				if (!(vv.getText().toString().replace(" ", "").equals("不限"))) {
					choosepayTypes.add(vv.getText().toString());
				}
				Resources resources = v.getContext().getResources();
				Drawable btnDrawable = resources
						.getDrawable(R.color.choose_paytype);
				v.setBackgroundDrawable(btnDrawable);
				((TextView) v).setTextColor(resources
						.getColor(R.color.body_color));
			}
		}
	};

	@OnClick(R.id.submint)
	public void submintOnclick(View v) {
		Intent intent = new Intent();
		intent.setClass(SaiXuanActivity.this, SearchResult.class);
		choosetimeStr = "{";
		if (timeType.equals("xingqi")) {
			if (chooseTimes.size() > 0) {
				for (int i = 0; i < chooseTimes.size(); i++) {
					if (!chooseTimes.get(i).equals("")) {
						choosetimeStr += chooseTimes.get(i).replace(" ", "")
								+ "、";
					}
				}
				choosetimeStr = choosetimeStr.substring(0,
						choosetimeStr.length() - 1)
						+ "}";
			} else {
				choosetimeStr = "{不限}";
			}
		} else {
			String datestr = date.getText().toString();
			choosetimeStr += datestr + "}";
		}

		// 城市
		chooseCityStr = "{";
		if (chooseCitys.size() > 0) {
			for (int i = 0; i < chooseCitys.size(); i++) {
				if (!chooseCitys.get(i).equals("")) {
					chooseCityStr += tempCity + "-"
							+ chooseCitys.get(i).replace(" ", "") + "、";
				}
			}
			chooseCityStr = chooseCityStr.substring(0,
					chooseCityStr.length() - 1) + "}";
		} else {
			chooseCityStr = "{全" + tempCity + "}";
		}

		// 类型
		chooseTypeStr = "{";
		if (chooseTypes.size() > 0) {
			for (int i = 0; i < chooseTypes.size(); i++) {
				if (!chooseTypes.get(i).equals("")) {
					chooseTypeStr += chooseTypes.get(i).replace(" ", "") + "、";
				}
			}
			chooseTypeStr = chooseTypeStr.substring(0,
					chooseTypeStr.length() - 1) + "}";
		} else {
			chooseTypeStr = "{全部}";
		}

		// 结算
		choosePayTypeStr = "{";
		if (choosepayTypes.size() > 0) {
			for (int i = 0; i < choosepayTypes.size(); i++) {
				if (!choosepayTypes.get(i).equals("")) {
					choosePayTypeStr += choosepayTypes.get(i).replace(" ", "")
							+ "、";
				}
			}
			choosePayTypeStr = choosePayTypeStr.substring(0,
					choosePayTypeStr.length() - 1)
					+ "}";
		} else {
			choosePayTypeStr = "{不限}";
		}

		intent.putExtra("choosetimeStr", choosetimeStr);
		intent.putExtra("chooseCityStr", chooseCityStr);
		intent.putExtra("chooseTypeStr", chooseTypeStr);
		intent.putExtra("choosePayTypeStr", choosePayTypeStr);
		intent.putExtra("choose_filter_type", newJianZhiFlag);// true?false
		if (newJianZhiFlag) {
			// 最新
			startActivity(intent);
			SaiXuanActivity.this.finish();// 筛选界面销毁
		} else {
			// 最近
			if (lng > 0 && lat > 0) {
				intent.putExtra("choose_lat", lat);
				intent.putExtra("choose_lng", lng);
				startActivity(intent);
				SaiXuanActivity.this.finish();// 筛选界面销毁
			} else {
				ToastUtil.showShortToast("还未定位到您的当前位置,请稍后^_^");
			}
		}

	}

	@Override
	public void onCancel(DialogInterface dialog) {

	}

	@Override
	public void onClick(int whichButton) {

	}

	/**
	 * 初始化定位
	 */
	private void initGaoDe() {
		// 初始化定位，只采用网络定位
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		mLocationManagerProxy.setGpsEnable(false);
		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用destroy()方法
		// 其中如果间隔时间为-1，则定位只定一次,
		// 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, 60 * 1000, 3, this);

	}

	@Override
	public void onLocationChanged(Location arg0) {

	}

	@Override
	public void onProviderDisabled(String arg0) {

	}

	@Override
	public void onProviderEnabled(String arg0) {

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (amapLocation != null
				&& amapLocation.getAMapException().getErrorCode() == 0) {
			// 定位成功回调信息，设置相关消息
			if (amapLocation.getCity() != null) {
				mLocationManagerProxy.removeUpdates(this);
				lat = amapLocation.getLatitude();
				lng = amapLocation.getLongitude();
			} else {
				Log.e("AmapErr", "Location ERR:"
						+ amapLocation.getAMapException().getErrorCode());
			}
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 销毁定位
		if (mLocationManagerProxy != null) {
			mLocationManagerProxy.destroy();
		}
	}

}
