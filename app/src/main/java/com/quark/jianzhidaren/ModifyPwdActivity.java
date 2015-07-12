package com.quark.jianzhidaren;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.carson.constant.JiaoyanUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.ui.widget.CustomDialog;

/**
 * 修改密码密码 接口 参数不同 所以跟 忘记密码分开
 * 
 * @author Administrator
 * 
 */
public class ModifyPwdActivity extends BaseActivity {

	private LinearLayout imageBtn;
	@ViewInject(R.id.password)
	private EditText password;
	@ViewInject(R.id.againpassword)
	private EditText againpassword;
	@ViewInject(R.id.show_pwd_checkbox)
	private CheckBox show_pwd_checkbox;
	@ViewInject(R.id.regin)
	private Button regin;
	@ViewInject(R.id.title)
	private TextView title;
	String passwordStr;
	String againpasswordStr;
	String user_id;
	String url;
	private SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.modify_password);
		ViewUtils.inject(this);
		title.setText("修改密码");
		// 键盘隐藏
		LinearLayout activity_main = (LinearLayout) findViewById(R.id.forget_main);
		activity_main.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				return imm.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), 0);
			}
		});
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		user_id = sp.getString("userId", "");

		url = Url.COMPANY_EDITPASSWORD + "?token=" + MainCompanyActivity.token;
		RelativeLayout topLayout = (RelativeLayout) findViewById(R.id.top_title_layout);
		topLayout.setBackgroundColor(getResources().getColor(
				R.color.guanli_common_color));
		// 返回
		imageBtn = (LinearLayout) findViewById(R.id.back);
		imageBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ModifyPwdActivity.this.finish();
			}
		});

		show_pwd_checkbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						if (arg1) {
							password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
							againpassword
									.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
						} else {
							password.setInputType(InputType.TYPE_CLASS_TEXT
									| InputType.TYPE_TEXT_VARIATION_PASSWORD);
							againpassword
									.setInputType(InputType.TYPE_CLASS_TEXT
											| InputType.TYPE_TEXT_VARIATION_PASSWORD);
						}
					}
				});

	}

	// 点击修改密码
	public void showAlertDialog(String str, final String str2) {

		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(str);
		builder.setTitle(str2);
		builder.setPositiveButton("确 定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (str2.equals("修改成功")) {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(),
							FindPJLoginActivity.class);
					startActivity(intent);
				}
			}
		});
		builder.create().show();
	}

	@OnClick(R.id.regin)
	public void reginOnclick(View v) {
		passwordStr = password.getText().toString().trim();
		againpasswordStr = againpassword.getText().toString().trim();
		if (check()) {
			showWait(true);
			regin.setClickable(false);
			StringRequest request = new StringRequest(Request.Method.POST, url,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							showWait(false);
							int status = 9999999;
							try {
								JSONObject js;
								js = new JSONObject(response);
								JSONObject sd = js
										.getJSONObject("ResponseStatus");
								status = sd.getInt("status");
								String msg = sd.getString("msg");
								if (status == 2) {
									showToast(msg);
									Editor edit = sp.edit();
									edit.putString("remember_pwd",
											againpasswordStr);
									edit.commit();
									finish();
								} else {
									regin.setClickable(true);
									showToast(msg);
								}
							} catch (JSONException e) {
								e.printStackTrace();
								regin.setClickable(true);
								showToast("修改异常,请重试 ");
							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError volleyError) {
							showWait(false);
							regin.setClickable(true);
							showToast("你的网络不够给力，再试一次吧！");
						}
					}) {
				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					Map<String, String> map = new HashMap<String, String>();
					map.put("old_password", JiaoyanUtil.MD5(passwordStr));
					map.put("password", JiaoyanUtil.MD5(againpasswordStr));
					map.put("user_id", user_id);
					return map;
				}
			};
			queue.add(request);
			request.setRetryPolicy(new DefaultRetryPolicy(
					ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

		}
	}

	public boolean check() {
		passwordStr = password.getText().toString();
		againpasswordStr = againpassword.getText().toString();

		if (passwordStr.trim().length() < 6
				|| againpasswordStr.trim().length() < 6) {
			showToast("密码不能少于6个字符");
			return false;
		}

		return true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		queue.cancelAll(TAG);
	}

}
