package com.quark.setting;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.ui.widget.CommonWidget;
import com.quark.ui.widget.CustomDialog;

public class TousuActivity extends BaseActivity {
	private ImageButton imageBtn;
	private TextView zi_number;
	EditText editText;
	String url;
	String content;
	String user_id;
	String activity_id;
	SharedPreferences sp;
	private TextView tousu_tijiao_tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
		setContentView(R.layout.tousu_jianyi);
		ViewUtils.inject(this);
		tousu_tijiao_tv = (TextView) findViewById(R.id.tousu_tijiao_tv);
		url = Url.USER_TOUSU;// 投诉
		content = getIntent().getStringExtra("content");
		activity_id = getIntent().getStringExtra("activity_id");
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		user_id = sp.getString("userId", "");
		imageBtn = (ImageButton) findViewById(R.id.back);
		CommonWidget.back(imageBtn, this);
		zi_number = (TextView) findViewById(R.id.zi_number);
		editText = (EditText) findViewById(R.id.content);
		editText.addTextChangedListener(textWatcher);
		if (content != null && !"".equals(content)) {
			editText.setText(content);
			editText.setSelection(content.length());
		}
	}

	public void showAlertDialog(String str, final String str2) {
		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(str);
		builder.setTitle(str2);
		builder.setPositiveButton("确 定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (str2.equals("发送成功")) {
					TousuActivity.this.finish();
				}
			}
		});

		builder.create().show();
	}

	@OnClick(R.id.tousu_tijiao_tv)
	public void sbOnclick(View v) {
		content = editText.getText().toString();
		if (check()) {
			if (user_id != null && !"".equals(user_id.trim())
					&& activity_id != null && !"".equals(activity_id.trim())) {
				StringRequest requestss = new StringRequest(
						Request.Method.POST, url,
						new Response.Listener<String>() {
							@Override
							public void onResponse(String response) {
								try {
									JSONObject sjonds = new JSONObject(response);
									JSONObject sjond = sjonds
											.getJSONObject("ResponseStatus");
									int status = sjond.getInt("status");
									if (status == 1) {
										tousu_tijiao_tv.setClickable(false);
										showAlertDialog(
												"非常感谢您对兼职达人团队提供的投诉，我们会认真审核的！",
												"发送成功");
									} else if (status == 2) {
										showAlertDialog("您已经投诉过该活动,请勿重复提交,谢谢!",
												"发送成功");
									} else {
										showToast("连接失败,请稍候再试^_^");
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								showAlertDialog("您的网络不够给力，再试一次吧！", "提交失败");
							}
						}) {
					@Override
					protected Map<String, String> getParams()
							throws AuthFailureError {
						Map<String, String> map = new HashMap<String, String>();
						map.put("user_id", user_id);
						map.put("activity_id", activity_id);
						map.put("comment", content);
						return map;
					}
				};
				queue.add(requestss);
				requestss.setRetryPolicy(new DefaultRetryPolicy(ConstantForSaveList.DEFAULTRETRYTIME*1000, 1,
						1.0f));

			}
		}

	}

	//
	TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			zi_number.setText(editText.getText().toString().length() + "/200");
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	public boolean check() {
		if (content == null || content.trim().equals("")) {
			showToast("请输入投诉内容");
			return false;
		}
		return true;
	}
}
