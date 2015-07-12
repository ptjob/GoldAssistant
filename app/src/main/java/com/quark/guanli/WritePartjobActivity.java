package com.quark.guanli;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.quark.common.JsonHelper;
import com.quark.common.JsonUtil;
import com.quark.common.Url;
import com.quark.company.function.RosterActivity;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.jianzhidaren.MainCompanyActivity;
import com.quark.model.PublishAvailability;
import com.quark.model.PublishJianzhi;
import com.quark.ui.widget.ActionSheet;
import com.quark.ui.widget.ActionSheet.OnActionSheetSelected;
import com.quark.ui.widget.CustomDialog;
import com.quark.ui.widget.CustomDialogThree;
import com.quark.us.AuthenticationActivity;
import com.quark.utils.Util;

/**
 * 发布兼职
 * 
 * @author cluo
 * 
 */
public class WritePartjobActivity extends BaseActivity implements
		OnActionSheetSelected, OnCancelListener {
	private ImageView button_on, button_off;
	public RadioButton radio1, radio2;
	private LinearLayout linearLayout, sex_layout, sex_total_layout,
			show_more_request;
	private RadioGroup radioGroup1;
	private RadioGroup radioGroup2, radioGroup3, radioGroup4;
	private RadioGroup sexRadioGroup;
	private Boolean changeedGroup = false;
	private Boolean changeedGroup2 = false;
	private TextView yuanshi;
	private String type;
	private String user_id;
	private String url;
	private String submitUrl;
	private String getJianzhiUrl;
	private PublishAvailability availability;

	private int company_id;// Int 商家ID
	private String title;// String 标题。默认Empty String
	private String Start_time;// String 开始时间。默认'2015-01-01'
	private String End_time;// String 结束时间。默认'2015-01-01'
	private String Time_tag;// String 时间标签。默认Empty String
	private String city;// String 城市：不带“市”。默认Empty String
	private String county;// String 区域：不带“区”。默认Empty String
	private String address;// String 具体位置。默认Empty String
	private int Pay;// Int 薪酬。默认0
	private int Pay_type = 0;// Int 工资薪酬：0-日薪，1-时新。默认-1
	private String Pay_form;// String 结算方式：不限、日结、周结、月结、完工结
	private int Head_count;// Int 总人数.默认0
	private int Male_count;// Int 男人数。默认-1
	private int Femal_count;// Int 女人数.默认0
	private int Apart_sex;// Int 是否区分男女.默认-1
	private String Require_info;// String 工作要求及内容：10-200字。默认Empty String
	private int Require_height;// Int 更多兼职要求：身高cm。默认-1
	private int Require_bust; // Int 更多兼职要求：胸围。默认-1
	private int Require_beltline;// Int 更多兼职要求：腰围。默认-1
	private int Require_hipline;// Int 更多兼职要求：臀围。默认-1
	private String Require_cloth_weight;// Int 衣服码：S．M．L．XL．XXL．XXXL.默认-1
	private int Require_shoe_weight;// Int 鞋码：33-45间.默认-1
	PublishJianzhi publishJianLi;

	// @ViewInject(R.id.type_num)
	// TextView type_num;
	@ViewInject(R.id.my_name)
	EditText my_name;
	// 开始时间
	@ViewInject(R.id.begin_time)
	TextView begin_time;
	@ViewInject(R.id.begin_time_img)
	ImageView begin_time_img;
	// 结束时间
	@ViewInject(R.id.end_time)
	TextView end_time;
	@ViewInject(R.id.end_time_img)
	ImageView end_time_img;
	// 地区
	@ViewInject(R.id.area)
	TextView area;

	@ViewInject(R.id.area_img)
	ImageView area_img;
	//
	@ViewInject(R.id.area_detail)
	EditText area_detail;
	// 薪资
	@ViewInject(R.id.salary)
	EditText salary;
	// 薪资类型
	@ViewInject(R.id.salary_choose_rixing)
	TextView salary_choose_rixing;
	@ViewInject(R.id.salary_choose_shixing)
	TextView salary_choose_shixing;
	// 结算方式
	@ViewInject(R.id.wind_type_buxian)
	RadioButton wind_type_buxian;
	@ViewInject(R.id.wind_type_rijie)
	RadioButton wind_type_rijie;
	@ViewInject(R.id.wind_type_zhoujie)
	RadioButton wind_type_zhoujie;
	@ViewInject(R.id.wind_type_yuejie)
	RadioButton wind_type_yuejie;
	@ViewInject(R.id.wind_type_wangongjie)
	RadioButton wind_type_wangongjie;

	// 时间标签
	@ViewInject(R.id.time_zhoumo)
	RadioButton time_zhoumo;
	@ViewInject(R.id.time_jiejiari)
	RadioButton time_jiejiari;
	@ViewInject(R.id.time_zhiding)
	RadioButton time_zhiding;
	@ViewInject(R.id.time_shuqi)
	RadioButton time_shuqi;
	@ViewInject(R.id.total_num)
	EditText total_num;

	@ViewInject(R.id.men_num)
	EditText men_num;
	@ViewInject(R.id.female_num)
	EditText female_num;

	@ViewInject(R.id.neirong)
	EditText neirong;

	private int sexRequest = 1;// 1不限制性别 2 限定性别
	// 更多要求
	@ViewInject(R.id.height)
	EditText height;
	@ViewInject(R.id.require_bust)
	EditText require_bust;
	@ViewInject(R.id.require_beltline)
	EditText require_beltline;
	@ViewInject(R.id.require_hipline)
	EditText require_hipline;
	@ViewInject(R.id.require_cloth_weight)
	TextView require_cloth_weight;
	@ViewInject(R.id.require_shoe_weigth)
	TextView require_shoe_weigth;
	@ViewInject(R.id.radiogroup_jkz)
	RadioGroup radiogroup_jkz;
	@ViewInject(R.id.require_language)
	TextView require_language;

	@ViewInject(R.id.require_health_record_btn1)
	RadioButton require_health_record_btn1;
	@ViewInject(R.id.require_health_record_btn2)
	RadioButton require_health_record_btn2;

	private String editActivity_id = "";
	private String editType = "publish";
	private SharedPreferences sp;
	private RelativeLayout topLayout;
	private String role;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.company_partjob_write);
		ViewUtils.inject(this);
		// 屏蔽城市显示
		RelativeLayout cityReLayout = (RelativeLayout) findViewById(R.id.home_page_city_relayout);
		cityReLayout.setVisibility(View.GONE);
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		topLayout = (RelativeLayout) findViewById(R.id.home_common_guangchang_relayout);
		role = sp.getString("role", "");
		if (role.equals("user")) {
		} else {
			TextView rightTv = (TextView) findViewById(R.id.right);
			rightTv.setBackgroundColor(getResources().getColor(
					R.color.guanli_common_color));
			topLayout.setBackgroundColor(getResources().getColor(
					R.color.guanli_common_color));
		}
		user_id = sp.getString("userId", "");
		type = getIntent().getStringExtra("type");
		yuanshi = (TextView) findViewById(R.id.yuanshi);

		publishJianLi = new PublishJianzhi();
		publishJianLi.setType(type.replace(" ", ""));
		publishJianLi.setCompany_id(Integer.valueOf(user_id));

		url = Url.COMPANY_availability + "?token=" + MainCompanyActivity.token;
		submitUrl = Url.COMPANY_publish + "?token=" + MainCompanyActivity.token;
		getJianzhiUrl = Url.COMPANY_MyJianzhi_modify + "?token="
				+ MainCompanyActivity.token;
		// 来自编辑
		Bundle bundle = getIntent().getExtras();
		if (bundle != null && bundle.containsKey("activity_id")) {
			editType = "edit";
			editActivity_id = bundle.getString("activity_id");
			getDataJianzhi();
			submitUrl = Url.COMPANY_MyJianzhi_modifyCommit + "?token="
					+ MainCompanyActivity.token;
		} else {
			String city_s = sp.getString("city", "深圳");
			publishJianLi.setCity(city_s);
		}

		linearLayout = (LinearLayout) findViewById(R.id.more_request);

		TextView tv = (TextView) findViewById(R.id.title);
		// 设置成上个页面提交过来的
		tv.setText(type + "");
		TextView tvl = (TextView) findViewById(R.id.right);
		tvl.setText("预览");
		tvl.setOnClickListener(showListener);
		setBackButton();

		// salary_choose_rixing.setBackgroundResource(R.drawable.btn_tab_left_off);
		// salary_choose_rixing.setTextColor(getResources().getColor(R.color.ziti_orange));
		// salary_choose_shixing.setBackgroundResource(R.drawable.btn_tab_right_on);
		// salary_choose_shixing.setTextColor(getResources().getColor(R.color.body_color));
		// yuanshi.setText("元/时");

		salary_choose_rixing.setBackgroundResource(R.drawable.btn_tab_left_on);
		salary_choose_rixing.setTextColor(getResources().getColor(
				R.color.body_color));
		salary_choose_shixing
				.setBackgroundResource(R.drawable.btn_tab_right_off);
		salary_choose_shixing.setTextColor(getResources().getColor(
				R.color.ziti_orange));
		yuanshi.setText("元/日");

		/**
		 * 隐藏显示更多兼职要求
		 */
		button_off = (ImageView) findViewById(R.id.more_req_off);
		button_on = (ImageView) findViewById(R.id.more_req_on);
		show_more_request = (LinearLayout) findViewById(R.id.show_more_request);
		show_more_request.setOnClickListener(moreRequestListener);

		sex_total_layout = (LinearLayout) findViewById(R.id.sex_total_layout);
		sex_layout = (LinearLayout) findViewById(R.id.sex_layout);
		radio1 = (RadioButton) findViewById(R.id.radio1);
		radio2 = (RadioButton) findViewById(R.id.radio2);
		sexRadioGroup = (RadioGroup) findViewById(R.id.sexRadioGroup);
		/**
		 * 性别选择隐藏
		 */
		sexRadioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == radio1.getId()) {
							// 不限制性别
							setSexStatus(1);
						} else {
							setSexStatus(2);
						}
					}
				});
		/**
		 * 实现RadioGroup单选互斥选择的逻辑，否则出现多选
		 */
		radioGroup1 = (RadioGroup) findViewById(R.id.orderBy1);
		radioGroup1
				.setOnCheckedChangeListener(new MyRadioGroupOnCheckedChangedListener());
		radioGroup2 = (RadioGroup) findViewById(R.id.orderBy2);
		radioGroup2
				.setOnCheckedChangeListener(new MyRadioGroupOnCheckedChangedListener());
		radioGroup3 = (RadioGroup) findViewById(R.id.radioGroup3);
		radioGroup3
				.setOnCheckedChangeListener(new MyRadioGroupOnCheckedChangedListener2());
		radioGroup4 = (RadioGroup) findViewById(R.id.radioGroup4);
		radioGroup4
				.setOnCheckedChangeListener(new MyRadioGroupOnCheckedChangedListener2());
	}

	public void setSexStatus(int flage) {
		if (flage == 1) {
			// 不限制性别
			radio1.setChecked(true);
			sexRequest = 1;
			sex_layout.setVisibility(View.GONE);
			sex_total_layout.setVisibility(View.VISIBLE);
		} else {
			radio2.setChecked(true);
			sexRequest = 2;
			sex_layout.setVisibility(View.VISIBLE);
			sex_total_layout.setVisibility(View.GONE);
		}
	}

	/**
	 * 预览
	 */
	OnClickListener showListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (check()) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						JianzhiShowActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("jianzhi", publishJianLi);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}
	};

	OnClickListener moreRequestListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (linearLayout.getVisibility() == 0) {
				linearLayout.setVisibility(View.GONE);
				button_off.setVisibility(View.GONE);
				button_on.setVisibility(View.VISIBLE);
			} else {
				linearLayout.setVisibility(View.VISIBLE);
				button_off.setVisibility(View.VISIBLE);
				button_on.setVisibility(View.GONE);
			}
		}
	};

	/**
	 * 结算方式
	 */
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

	/**
	 * 时间标签
	 */
	class MyRadioGroupOnCheckedChangedListener2 implements
			OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (!changeedGroup2) {
				changeedGroup2 = true;
				if (group == radioGroup3) {
					radioGroup4.clearCheck();
				} else if (group == radioGroup4) {
					radioGroup3.clearCheck();
				}
				changeedGroup2 = false;
			}
		}
	}

	/**
	 * 获取各种状态
	 */
	public void getAvailability() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject sd = js
									.getJSONObject("AvailabilityResponse");
							availability = (PublishAvailability) JsonUtil
									.jsonToBean(sd, PublishAvailability.class);
							initView();
							if (availability.getFree_count() > 0) {
								publishJianLi.setPay_money(0);
							} else {
								publishJianLi.setPay_money(5);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showWait(false);
						showToast("你的网络不够给力，获取数据失败！");
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

	// edit 获取兼职信息
	public void getDataJianzhi() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST,
				getJianzhiUrl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject sd = js
									.getJSONObject("ModifyActivityResponse");
							Log.e("tag__sd", sd.toString());
							JSONObject sjj = sd.getJSONObject("activity");
							publishJianLi = (PublishJianzhi) JsonUtil
									.jsonToBean(sjj, PublishJianzhi.class);
							publishJianLi.setActivity_id(Integer
									.valueOf(editActivity_id));
							initDataView();

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showWait(false);
						showToast("你的网络不够给力，获取数据失败！");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("activity_id", editActivity_id);
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	@OnClick(R.id.publish_job)
	public void publishOnclik(View view) {
		if (check()) {
			// 商家修改兼职信息，不需要扣钱的
			if (editType.equals("edit")) {
				submitData();
			} else {
				if (availability.getFree_count() > 0) {
					submitData();
				} else {
					showAlertDialog2("您今日的免费信息已使用完，如需发布此条兼职需要付费5元", "温馨提示",
							"(账号余额：" + availability.getMoney() + "元)");

				}
			}
		}
	}

	public void submitData() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST,
				submitUrl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss;
							if (editType.equals("edit")) {
								jss = js.getJSONObject("ModifyCommitResponse");
							} else {
								jss = js.getJSONObject("PublishResponse");
							}

							int sd = jss.getInt("status");
							if (sd == 0) {
								showToast("提交失败！501");
							} else {
								if (editType.equals("edit")) {
									showAlertDialog1("您已成功修改兼职，兼职达人团队正在为您审核",
											"修改成功", "快一点吧");
								} else {
									if (availability.getFree_count() > 0) {
										showAlertDialog1(
												"您已成功发布活动，兼职达人团队正在为您审核",
												"发布成功", "快一点吧");
									} else {
										showAlertDialog1(
												"您已成功发布兼职，兼职达人团队正在为您审核",
												"发布成功", "快一点吧");
									}
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
						showToast("你的网络不够给力，获取数据失败！");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Log.e("erros", "提交的数据="
						+ JsonHelper.toMap(publishJianLi).toString());
				// publishJianLi.setCity(sp.getString("city", "深圳"));
				return JsonHelper.toMap(publishJianLi);
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
	}

	@OnClick({ R.id.begin_time, R.id.end_time, R.id.area,
			R.id.require_language, R.id.require_shoe_weigth,
			R.id.require_cloth_weight, R.id.begin_time_img, R.id.end_time_img,
			R.id.area_img })
	public void sexOnclick(View view) {
		switch (view.getId()) {
		case R.id.begin_time:
			ActionSheet.showSheetTime4(WritePartjobActivity.this,
					WritePartjobActivity.this, WritePartjobActivity.this,
					begin_time);
			break;
		case R.id.begin_time_img:
			ActionSheet.showSheetTime4(WritePartjobActivity.this,
					WritePartjobActivity.this, WritePartjobActivity.this,
					begin_time);
			break;
		case R.id.end_time:
			ActionSheet.showSheetTime4(WritePartjobActivity.this,
					WritePartjobActivity.this, WritePartjobActivity.this,
					end_time);
			break;
		case R.id.end_time_img:
			ActionSheet.showSheetTime4(WritePartjobActivity.this,
					WritePartjobActivity.this, WritePartjobActivity.this,
					end_time);
			break;
		case R.id.area:
			ActionSheet.showSheetCountry(WritePartjobActivity.this,
					WritePartjobActivity.this, WritePartjobActivity.this, area);
			break;
		case R.id.area_img:
			ActionSheet.showSheetCountry(WritePartjobActivity.this,
					WritePartjobActivity.this, WritePartjobActivity.this, area);
			break;
		case R.id.require_language:
			ActionSheet.showSheetLanguage(WritePartjobActivity.this,
					WritePartjobActivity.this, WritePartjobActivity.this,
					require_language);
			break;
		case R.id.require_shoe_weigth:
			ActionSheet.showSheetShoos(WritePartjobActivity.this,
					WritePartjobActivity.this, WritePartjobActivity.this,
					require_shoe_weigth);
			break;
		case R.id.require_cloth_weight:
			ActionSheet.showSheetYiFZM(WritePartjobActivity.this,
					WritePartjobActivity.this, WritePartjobActivity.this,
					require_cloth_weight);
			break;
		default:
			break;
		}
	}

	/**
	 * 检查输入 保存输入数据
	 * 
	 * @return
	 * @throws ParseException
	 */
	public boolean check() {
		title = my_name.getText().toString();
		if (!Util.isEmpty(title)) {
			showToast("请输入标题");
			return false;
		}
		publishJianLi.setTitle(title);
		Start_time = begin_time.getText().toString();
		if (!Util.isEmpty(Start_time)) {
			showToast("请输入开始时间");
			return false;
		}
		publishJianLi.setStart_time(Start_time);
		End_time = end_time.getText().toString();
		if (!Util.isEmpty(End_time)) {
			showToast("请输入结束时间");
			return false;
		}
		publishJianLi.setEnd_time(End_time);

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dt1 = new Date();
		Date dt2 = new Date();
		try {
			dt1 = df.parse(Start_time);
			dt2 = df.parse(End_time);
			if (dt1.getTime() > dt2.getTime()) {
				showToast("开始时间大于结束时间有误");
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		county = area.getText().toString();
		if (!Util.isEmpty(county)) {
			showToast("请输入工作区域");
			return false;
		}
		publishJianLi.setCounty(county);
		address = area_detail.getText().toString();
		if (!Util.isAddressDetail(address)) {
			showToast("请输入4-20字的详细地址");
			return false;
		}
		publishJianLi.setAddress(address);
		String PayStr = salary.getText().toString();
		if (!Util.isNumeric(PayStr)) {

			showToast("请输入正确的薪酬");
			return false;
		} else {
			try {
				int ad = Integer.valueOf(PayStr);
				if (ad > 9999) {
					showToast("请输入正确的薪酬,不超过9999元");
					return false;
				}
			} catch (Exception e) {
				showToast("请输入正确的薪酬,不超过9999元");
				return false;
			}
		}

		publishJianLi.setPay(Integer.valueOf(PayStr));
		publishJianLi.setPay_type(Pay_type);// 设置薪资类型

		boolean pay_from = true;
		RadioButton radioButton = (RadioButton) findViewById(radioGroup1
				.getCheckedRadioButtonId());
		if (radioButton != null) {
			String text = radioButton.getText().toString();
			publishJianLi.setPay_form(text.replace(" ", ""));
			pay_from = false;
		}
		if (pay_from) {
			RadioButton radioButton2 = (RadioButton) findViewById(radioGroup2
					.getCheckedRadioButtonId());
			if (radioButton2 != null) {
				String text2 = radioButton2.getText().toString();
				publishJianLi.setPay_form(text2.replace(" ", ""));
				pay_from = false;
			}
		}
		if (pay_from) {
			showToast("请选择结算方式");
			return false;
		}

		// 时间标签
		boolean time_flag = true;
		RadioButton radioButton3 = (RadioButton) findViewById(radioGroup3
				.getCheckedRadioButtonId());
		if (radioButton3 != null) {
			String text = radioButton3.getText().toString();
			publishJianLi.setTime_tag(text.replace(" ", ""));
			time_flag = false;
		}
		if (time_flag) {
			RadioButton radioButton4 = (RadioButton) findViewById(radioGroup4
					.getCheckedRadioButtonId());
			if (radioButton4 != null) {
				String text2 = radioButton4.getText().toString();
				publishJianLi.setTime_tag(text2.replace(" ", ""));
				time_flag = false;
			}
		}
		if (time_flag) {
			showToast("请选择时间标签");
			return false;
		}

		if (sexRequest == 1) {
			String totalNum = total_num.getText().toString();
			if (!Util.isNumeric(totalNum)) {
				showToast("请输入正确的总人数");
				return false;
			} else {
				try {
					int ad = Integer.valueOf(totalNum);
					if (ad > 999) {
						showToast("请输入正确的总人数,不超过999人");
						return false;
					}
					if (ad <= 0) {
						showToast("请输入正确的总人数");
						return false;
					}
				} catch (Exception e) {
					showToast("请输入正确的总人数");
					return false;
				}
			}
			publishJianLi.setApart_sex(0);
			publishJianLi.setHead_count(Integer.valueOf(totalNum));
		}
		if (sexRequest == 2) {
			String manNum = men_num.getText().toString();
			String femaleNum = female_num.getText().toString();
			if ((!Util.isNumeric(femaleNum)) && (!Util.isNumeric(manNum))) {
				showToast("人数不能为空");
				return false;
			}
			int fn = 0;
			int mn = 0;
			if (Util.isNumeric(femaleNum)) {
				try {
					int ad = Integer.valueOf(femaleNum);
					if (ad > 999) {
						showToast("请输入正确的人数,不超过999人");
						return false;
					} else {
						fn = Integer.valueOf(femaleNum);
						publishJianLi.setFemale_count(Integer
								.valueOf(femaleNum));
					}
				} catch (Exception e) {
					showToast("请输入正确的人数,不超过999人");
					return false;
				}
			}
			if (Util.isNumeric(manNum)) {
				try {
					int ad = Integer.valueOf(manNum);
					if (ad > 999) {
						showToast("请输入正确的人数,不超过999人");
						return false;
					} else {
						mn = Integer.valueOf(manNum);
						publishJianLi.setMale_count(Integer.valueOf(manNum));
					}
				} catch (Exception e) {
					showToast("请输入正确的人数,不超过999人");
					return false;
				}
			}
			int hcount = fn + mn;
			if (hcount <= 0) {
				showToast("请输入正确的人数");
				return false;
			}
			publishJianLi.setApart_sex(1);
			publishJianLi.setHead_count(hcount);
		}

		String neirongStr = neirong.getText().toString();
		if (!Util.isInfook(neirongStr)) {
			showToast("请输入10-1000个字的工作要求及内容");
			return false;
		}
		publishJianLi.setRequire_info(neirongStr);

		// 更多兼职要求
		String heightStr = height.getText().toString();
		if (Util.isNumeric(heightStr)) {
			if (Integer.valueOf(heightStr) > 200) {
				showToast("身高要求请输入200以内...");
				return false;
			} else
				publishJianLi.setRequire_height(Integer.valueOf(heightStr));
		} else {
			publishJianLi.setRequire_height(-1);
		}

		String require_bustStr = require_bust.getText().toString();
		if (Util.isNumeric(require_bustStr)) {
			publishJianLi.setRequire_bust(Integer.valueOf(require_bustStr));
		} else {
			publishJianLi.setRequire_bust(-1);
		}

		String require_beltlineStr = require_beltline.getText().toString();
		if (Util.isNumeric(require_beltlineStr)) {
			publishJianLi.setRequire_beltline(Integer
					.valueOf(require_beltlineStr));
		} else {
			publishJianLi.setRequire_beltline(-1);
		}

		String require_hiplineStr = require_hipline.getText().toString();
		if (Util.isNumeric(require_beltlineStr)) {
			publishJianLi.setRequire_hipline(Integer
					.valueOf(require_hiplineStr));
		} else {
			publishJianLi.setRequire_hipline(-1);
		}

		String require_cloth_weightStr = require_cloth_weight.getText()
				.toString();
		if (Util.isEmpty(require_cloth_weightStr)) {
			publishJianLi.setRequire_cloth_weight(require_cloth_weightStr);
		} else {
			publishJianLi.setRequire_cloth_weight("-1");
		}

		String require_shoe_weigthStr = require_shoe_weigth.getText()
				.toString();
		if (Util.isNumeric(require_shoe_weigthStr)) {
			publishJianLi.setRequire_shoe_weigth(Integer
					.valueOf(require_shoe_weigthStr));
		} else {
			publishJianLi.setRequire_shoe_weigth(-1);
		}

		if (radiogroup_jkz.getCheckedRadioButtonId() == R.id.require_health_record_btn1) {
			publishJianLi.setRequire_health_record(1);
		} else if (radiogroup_jkz.getCheckedRadioButtonId() == R.id.require_health_record_btn2) {
			publishJianLi.setRequire_health_record(0);
		} else {
			publishJianLi.setRequire_health_record(-1);
		}

		String require_languageStr = require_language.getText().toString();
		if (Util.isEmpty(require_languageStr)) {
			publishJianLi.setRequire_language(require_languageStr);
		}

		return true;
	}

	public boolean checkStatus() {
		// 用户状态判断
		// Log.e("erros", "状态bean="+availability.toString());
		// no money
		// Log.e("erros",
		// availability.getCharge_count()+availability.getFree_count()+"==");

		// 以发布过一条信息 未实名验证 商家注册可以在未实名认证条件下发布一条信息
		if ((availability.getTotal_count() > 0)
				&& (availability.getCertification() == 0)) {
			showAlertDialog1("您已发布过一条招聘信息，需要实名认证才能继续发布。", "资料还未进行实名验证",
					"  现在就去验证  ");
			return false;
		}

		int s = availability.getCharge_count() + availability.getFree_count();
		if (s < 1) {
			showAlertDialog2("您今日已发布过一条招聘信息，如需再发布信息需要付费5元。", "余额不足", "(账号余额："
					+ availability.getMoney() + "元)");
			return false;
		}

		// 未评论
		if (availability.getTobe_comment_activity_id() == -1) {
		} else {
			showAlertDialog1(availability.getTobe_comment_activity_title()
					+ "人员评价尚未完成，不能发起新的活动", "到指定日期，未做人员评价", " 现在就去评价 ");
			return false;
		}

		return true;
	}

	/**
	 * @param str
	 * @param str2
	 */
	public void showAlertDialog1(String str, final String str2,
			String submintStr) {

		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(str);
		builder.setTitle(str2);

		builder.setPositiveButton(submintStr,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (str2.equals("资料还未进行实名验证")) {
							Intent intent = new Intent();
							intent.setClass(getApplicationContext(),
									AuthenticationActivity.class);
							startActivity(intent);
						}
						if (str2.equals("到指定日期，未做人员评价")) {
							Intent intent = new Intent();
							/*
							 * intent.putExtra("activity_id",
							 * availability.getTobe_comment_activity_id()+"");
							 * intent.putExtra("user_id", user_id+"");
							 * intent.setClass(getApplicationContext(),
							 * PersonAssessDetailActivity.class);
							 */
							intent.setClass(getApplicationContext(),
									RosterActivity.class);
							startActivity(intent);
						}
						if (str2.equals("每日1条免费信息")) {
							finish();
							if (PublishActivity.intanse != null) {
								PublishActivity.intanse.finish();
							}
						}
						if (str2.equals("发布成功")) {
							finish();
							if (PublishActivity.intanse != null) {
								PublishActivity.intanse.finish();
							}
						}
						if (str2.equals("修改成功")) {
							finish();
							if (PublishActivity.intanse != null) {
								PublishActivity.intanse.finish();
							}
						}
					}
				});
		builder.create().show();
	}

	public void showAlertDialog2(String str, final String str2, String str3) {

		CustomDialogThree.Builder builder = new CustomDialogThree.Builder(this);
		builder.setMessage(str);
		builder.setTitle(str2);
		builder.setMoney(str3);
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// WritePartjobActivity.this.finish();
			}
		});

		if (str2.equals("温馨提示")) {
			builder.setNegativeButton("确认",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							submitData();
						}
					});

		}
		builder.create().show();
	}

	@OnClick({ R.id.salary_choose_rixing, R.id.salary_choose_shixing })
	public void selectSalaryType(View view) {
		if (view.getId() == R.id.salary_choose_rixing) {
			setSalaryType(0);
		} else {
			setSalaryType(1);
		}
	}

	@SuppressLint("ResourceAsColor")
	public void setSalaryType(int type) {
		if (type == 0) {
			Pay_type = 0;
			salary_choose_rixing
					.setBackgroundResource(R.drawable.btn_tab_left_on);
			salary_choose_rixing.setTextColor(getResources().getColor(
					R.color.body_color));
			yuanshi.setText("元/日");
			salary_choose_shixing
					.setBackgroundResource(R.drawable.btn_tab_right_off);
			salary_choose_shixing.setTextColor(getResources().getColor(
					R.color.ziti_orange));
		} else {
			Pay_type = 1;
			salary_choose_rixing
					.setBackgroundResource(R.drawable.btn_tab_left_off);
			salary_choose_rixing.setTextColor(R.color.ziti_orange);
			salary_choose_shixing
					.setBackgroundResource(R.drawable.btn_tab_right_on);
			salary_choose_shixing.setTextColor(getResources().getColor(
					R.color.body_color));
			yuanshi.setText("元/时");
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {

	}

	@Override
	public void onClick(int whichButton) {

	}

	/**
	 * 初始化界面
	 */
	public void initView() {
		// type_num.setText((availability.getFree_count()+availability.getCharge_count())+"");
	}

	// 初始化数据
	public void initDataView() {
		my_name.setText(publishJianLi.getTitle());
		begin_time.setText(publishJianLi.getStart_time());
		end_time.setText(publishJianLi.getEnd_time());
		area.setText(publishJianLi.getCounty());
		area_detail.setText(publishJianLi.getAddress());
		salary.setText(publishJianLi.getPay() + "");
		setSalaryType(1);
		String pay_f = publishJianLi.getPay_form();

		if (pay_f.equals("不限")) {
			wind_type_buxian.setChecked(true);
		} else if (pay_f.equals("日结")) {
			wind_type_rijie.setChecked(true);
		} else if (pay_f.equals("周结")) {
			wind_type_zhoujie.setChecked(true);
		} else if (pay_f.equals("月结")) {
			wind_type_yuejie.setChecked(true);
		} else if (pay_f.equals("完工结")) {
			wind_type_wangongjie.setChecked(true);
		}

		String tiem_f = publishJianLi.getTime_tag();
		if (tiem_f.equals("周末")) {
			time_zhoumo.setChecked(true);
		} else if (tiem_f.equals("节假日")) {
			time_jiejiari.setChecked(true);
		} else if (tiem_f.equals("指定日期")) {
			time_zhiding.setChecked(true);
		}

		if (publishJianLi.getApart_sex() == 0) {// 不区分性别
			setSexStatus(1);
			total_num.setText(publishJianLi.getHead_count() + "");
		} else {
			setSexStatus(2);
			female_num.setText(publishJianLi.getFemale_count() + "");
			men_num.setText(publishJianLi.getMale_count() + "");
		}

		neirong.setText(publishJianLi.getRequire_info());

		if (publishJianLi.getRequire_height() != -1) {
			height.setText(publishJianLi.getRequire_height() + "");
		}

		if ((publishJianLi.getRequire_shoe_weigth() != -1)
				&& (publishJianLi.getRequire_shoe_weigth() != 0)) {
			require_shoe_weigth.setText(publishJianLi.getRequire_shoe_weigth()
					+ "");
		}

		if (!publishJianLi.getRequire_cloth_weight().equals("-1")) {
			require_cloth_weight.setText(publishJianLi
					.getRequire_cloth_weight() + "");
		}

		if (publishJianLi.getRequire_bust() != -1) {
			require_bust.setText(publishJianLi.getRequire_bust() + "");
		}
		if (publishJianLi.getRequire_beltline() != -1) {
			require_beltline.setText(publishJianLi.getRequire_beltline() + "");
		}
		if (publishJianLi.getRequire_hipline() != -1) {
			require_hipline.setText(publishJianLi.getRequire_hipline() + "");
		}

		if (publishJianLi.getRequire_health_record() == 0) {
			require_health_record_btn1.setSelected(true);
		} else if (publishJianLi.getRequire_health_record() == 1) {
			require_health_record_btn2.setSelected(true);
		}

		if (publishJianLi.getRequire_language() != null
				&& publishJianLi.getRequire_language().equals("-1")) {
			require_language.setText(publishJianLi.getRequire_language() + "");
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		getAvailability();
	}
}
