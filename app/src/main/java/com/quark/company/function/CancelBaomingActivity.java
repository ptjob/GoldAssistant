package com.quark.company.function;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.qingmu.jianzhidaren.R;
import com.quark.common.JsonUtil;
import com.quark.common.Url;
import com.quark.http.image.CircularImage;
import com.quark.http.image.LoadImage;
import com.quark.jianzhidaren.BaseActivity;
import com.parttime.main.MainTabActivity;
import com.quark.model.CancelApply;

/**
 * 取消报名
 * 
 * @author C罗
 * 
 */
public class CancelBaomingActivity extends BaseActivity {
	private String activity_id;
	private String user_id;
	private String url_ziliao, url_gree, content, radioButtonText;
	private CancelApply cancelApply;
	private TextView cancel_name, age, note, zi_number;
	private ImageView iconc, yan_img, cyj_img, sex_img;
	private Button gree_cancel;
	private EditText cancel_neirong;
	private RadioGroup orderBy1;
	private RadioButton zhongping, chaping, xuanzhe;
	int[] heartImg = { R.id.xinyi_bt1, R.id.xinyi_bt2, R.id.xinyi_bt3,
			R.id.xinyi_bt4, R.id.xinyi_bt5, R.id.xinyi_bt6, R.id.xinyi_bt7,
			R.id.xinyi_bt8, R.id.xinyi_bt9, R.id.xinyi_bt10 };
	private SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_cancel_baoming);
		sp = getSharedPreferences("jrdr.setting", Activity.MODE_PRIVATE);
		setBackButton();
		setTopTitle("取消报名");
		RelativeLayout topLayout = (RelativeLayout) findViewById(R.id.top_title_layout);
		topLayout.setBackgroundColor(getResources().getColor(
				R.color.guanli_common_color));
		activity_id = getIntent().getStringExtra("activity_id");
		user_id = getIntent().getStringExtra("user_id");
		url_ziliao = Url.COMPANY_cancelApply + "?token="
				+ MainTabActivity.token;
		url_gree = Url.COMPANY_commentRequirer + "?token="
				+ MainTabActivity.token;
		getData();
		// 提交同意取消报名
		gree_cancel = (Button) findViewById(R.id.gree_cancel);
		orderBy1 = (RadioGroup) findViewById(R.id.orderBy1);
		zhongping = (RadioButton) findViewById(R.id.zhongping);
		chaping = (RadioButton) findViewById(R.id.chaping);
		zhongping.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				cancel_neirong.setText("期待下次与你合作;");
			}
		});
		chaping.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				cancel_neirong.setText("希望下次能够早一些告诉我，好让我有个准备;");
			}
		});

		zi_number = (TextView) findViewById(R.id.zi_number);
		cancel_neirong = (EditText) findViewById(R.id.cancel_neirong);
		cancel_neirong.addTextChangedListener(watcher);
		gree_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				xuanzhe = (RadioButton) findViewById(orderBy1
						.getCheckedRadioButtonId());
				if (xuanzhe != null) {
					radioButtonText = xuanzhe.getText().toString()
							.replaceAll(" ", ""); // 去掉字符串空格
				} else {
					showToast("请选择评价类别");
				}
				content = cancel_neirong.getText().toString();
				if (check()) {
					showWait(true);
					StringRequest stringRequest = new StringRequest(
							Method.POST, url_gree,
							new Response.Listener<String>() {
								@Override
								public void onResponse(String response) {
									showWait(false);
									try {
										JSONObject js = new JSONObject(response);
										JSONObject jss = js
												.getJSONObject("AgreeCancelApplyResponse");
										int status = jss.getInt("status");
										getResultStatus(status);

									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}, new Response.ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError arg0) {
									showWait(false);
									showToast("审批失败");
								}
							}) {
						@Override
						protected Map<String, String> getParams() {
							Map<String, String> map = new HashMap<String, String>();
							map.put("activity_id", activity_id);
							map.put("user_id", user_id);
							map.put("comment", radioButtonText);
							map.put("remark", content);
							return map;
						}
					};
					queue.add(stringRequest);
					stringRequest.setRetryPolicy(new DefaultRetryPolicy(
							ConstantForSaveList.DEFAULTRETRYTIME*1000, 1, 1.0f));

				}
			}
		});
	}

	private void getResultStatus(int status) {
		if (status == 1) {
			showToast("审批成功");
			finish();
		} else {
			showToast("审批失败");
		}
	}

	// 异步加载数据
	private void getData() {
		// TODO Auto-generated method stub
		showWait(true);
		StringRequest stringRequest = new StringRequest(Method.POST,
				url_ziliao, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss = js
									.getJSONObject("CancelApplyResponse");
							cancelApply = (CancelApply) JsonUtil.jsonToBean(
									jss, CancelApply.class);
							initView();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						showWait(false);
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", user_id);
				map.put("activity_id", activity_id);
				return map;
			}
		};
		queue.add(stringRequest);
		stringRequest
				.setRetryPolicy(new DefaultRetryPolicy(ConstantForSaveList.DEFAULTRETRYTIME*1000, 1, 1.0f));

	}

	protected void initView() {
		// TODO Auto-generated method stub
		cancel_name = (TextView) findViewById(R.id.cancel_name);
		cancel_name.setText(cancelApply.getName());
		sex_img = (ImageView) findViewById(R.id.item_sex_imv);
		if (cancelApply.getSex() == -1) {
			sex_img.setVisibility(View.INVISIBLE);
		} else if (cancelApply.getSex() == 0) {
			sex_img.setImageResource(R.drawable.my_women);
			sex_img.setVisibility(View.VISIBLE);
		} else {
			sex_img.setImageResource(R.drawable.my_men);
			sex_img.setVisibility(View.VISIBLE);
		}
		age = (TextView) findViewById(R.id.age);
		age.setText(cancelApply.getAge() + "岁");
		note = (TextView) findViewById(R.id.note);
		if (cancelApply.getNote() == null || cancelApply.getNote().equals("")) {
			note.setText("没有填写理由。");
		} else {
			note.setText(cancelApply.getNote());
		}
		yan_img = (ImageView) findViewById(R.id.yan_img);
		if (cancelApply.getCertification() == 2) {
			yan_img.setImageResource(R.drawable.my_certified);
		} else {
			yan_img.setImageResource(R.drawable.my_unauthorized);
		}
		cyj_img = (ImageView) findViewById(R.id.cyj_img);
		if (cancelApply.getEarnest_money() == 1) {
			cyj_img.setImageResource(R.drawable.my_margin);
		} else {
			cyj_img.setImageResource(R.drawable.my_ordinary);
		}
		CircularImage cover_user_photo = (CircularImage) findViewById(R.id.cover_user_photo);
		if (cancelApply.getPicture_1() == null
				|| cancelApply.getPicture_1().equals("")) {
		} else {
			LoadImage.loadImage(Url.GETPIC + cancelApply.getPicture_1(),
					cover_user_photo);
		}

		addXinToView(cancelApply.getCreditworthiness());

	}

	// end
	TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			zi_number.setText(cancel_neirong.getText().toString().length()
					+ "/200");
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	};

	private boolean check() {
		if (content == null || content.trim().equals("")) {
			showToast("请输入评论内容");
			// Toast.makeText(getApplicationContext(), "请输入评论内容", 0).show();
			return false;
		} else if (zhongping.isChecked() == false
				&& chaping.isChecked() == false) {
			return false;
		}
		return true;
	}

	/**
	 * 信誉值
	 * 
	 * @param xin
	 * @param convertView
	 */
	private void addXinToView(int xin) {
		if (xin > 0) {
			int heartCount = xin / 10;
			int heartHeart = xin % 10;
			int j = 0;
			if (heartCount > 9) {
				ImageView imageView = (ImageView) findViewById(heartImg[0]);
				imageView.setVisibility(View.VISIBLE);
				imageView.setImageResource(R.drawable.icon_heart_ten);
			} else {
				for (int i = 0; i < heartCount; i++) {
					ImageView imageView = (ImageView) findViewById(heartImg[i]);
					imageView.setVisibility(View.VISIBLE);
					imageView.setImageResource(R.drawable.icon_heart);
					j = i;
				}
				if (heartHeart == 5) {
					ImageView imageView = (ImageView) findViewById(heartImg[j + 1]);
					imageView.setImageResource(R.drawable.icon_heart_half);
					imageView.setVisibility(View.VISIBLE);
				}
				// 用于刷新UI
				if (heartCount < 9) {
					for (int ii = j + 2; ii < 10; ii++) {
						ImageView imageView = (ImageView) findViewById(heartImg[ii]);
						imageView.setVisibility(View.GONE);
					}
				}
			}
		} else {
			for (int a = 0; a < 10; a++) {
				ImageView imageView = (ImageView) findViewById(heartImg[a]);
				imageView.setVisibility(View.GONE);
			}
		}
	}
}
