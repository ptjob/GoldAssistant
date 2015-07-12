package com.quark.jianzhidaren;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.parttime.main.MainTabActivity;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.ui.widget.CustomDialog;
import com.quark.utils.Util;

/**
 * 忘记密码
 * 
 * @author Administrator
 * 
 */
public class ForgetPwdActivity extends BaseActivity {

	private LinearLayout imageBtn;
	@ViewInject(R.id.telephone)
	private EditText telephone;
	// 发送验证码按钮
	@ViewInject(R.id.code)
	private Button code;
	@ViewInject(R.id.inputcode)
	private EditText inputcode;
	@ViewInject(R.id.password)
	private EditText password;
	@ViewInject(R.id.againpassword)
	private EditText againpassword;
	@ViewInject(R.id.regin)
	private Button regin;
	@ViewInject(R.id.code_vetify_imv)
	private ImageView codeVetifyImv;

	String telephoneStr;
	String telephoneStrTemp;
	String nameStr;
	String inputcodeStr;
	String passwordStr;
	String againpasswordStr;
	String codeStr;
	String codeStrget;
	String url;
	String sendMSMUrl;
	SharedPreferences sp;
	private long current_time;// 点击发送验证码的时候
	String jiaoyanUrl;// 校验验证码是否正确
	private boolean codeFlag;// 验证码是否正确

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_password);
		ViewUtils.inject(this);
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

		url = Url.COMPANY_FORGETPASSWORD + "?token="
				+ MainTabActivity.token;
		sendMSMUrl = Url.COMPANY_SENDMSM_FORGETPASSWORD + "?token="
				+ MainTabActivity.token;
		jiaoyanUrl = Url.MESSAGE_VALIDATE + "?token="
				+ MainTabActivity.token;

		// 返回
		imageBtn = (LinearLayout) findViewById(R.id.back);
		imageBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ForgetPwdActivity.this.finish();
			}
		});

		// 监听验证码框内容改变
		inputcode.addTextChangedListener(new TextWatcher() {

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
				if (arg0 != null && !"".equals(arg0.toString())) {
					if (arg0.toString().length() != 6) {
						// 验证码不是6位
						codeVetifyImv.setImageResource(R.drawable.vertify_no);
						codeVetifyImv.setVisibility(View.VISIBLE);
					} else {
						// 验证码是6位的时候先判断是否符合规则
						if (JiaoyanUtil.vertifyCode(arg0.toString())) {
							// 访问服务端验证验证码是否正确
							// 先判断是否输入了正确的手机
							telephoneStr = telephone.getText().toString();
							if (Util.isMobileNO(telephoneStr)) {
								vertifyCode(arg0.toString());
							} else {
								showToast(getResources().getString(
										R.string.regist_edt_right_tel));
							}
						} else {
							showToast(getResources().getString(
									R.string.regist_code_error));
						}

					}

				} else {
					codeVetifyImv.setVisibility(View.GONE);
				}

			}
		});
		// 收不到验证码
		TextView cant_get_code_tv = (TextView) findViewById(R.id.cant_get_code_tv);
		cant_get_code_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showAlertKefuDialog("什么，收不到短信验证码？请联系客服！", "温馨提示");
			}
		});
	}

	/**
	 * 验证验证码是否正确
	 * 
	 */
	private void vertifyCode(final String code) {
		StringRequest request = new StringRequest(Request.Method.POST,
				jiaoyanUrl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						JSONObject json;
						try {
							json = new JSONObject(response);
							JSONObject jsont = json
									.getJSONObject("ResponseStatus");
							int status = jsont.getInt("status");
							// 0:失败 1:成功
							if (status == 0) {
								codeFlag = false;
								codeVetifyImv
										.setImageResource(R.drawable.vertify_no);
								codeVetifyImv.setVisibility(View.VISIBLE);
							} else if (status == 1) {
								codeFlag = true;
								codeVetifyImv
										.setImageResource(R.drawable.vertify_yes);
								codeVetifyImv.setVisibility(View.VISIBLE);
							} else {
								codeFlag = false;
								codeVetifyImv
										.setImageResource(R.drawable.vertify_no);
								codeVetifyImv.setVisibility(View.VISIBLE);
							}
						} catch (JSONException e) {
							e.printStackTrace();
							codeFlag = false;
							codeVetifyImv
									.setImageResource(R.drawable.vertify_no);
							codeVetifyImv.setVisibility(View.VISIBLE);
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						codeFlag = false;
						showToast(getResources().getString(
								R.string.regist_request_server_fail));
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("telephone", telephoneStr);
				map.put("code", code);
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
	}

	/**
	 * 联系客服
	 */
	public void showAlertKefuDialog(String str, final String str2) {

		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(str);
		builder.setTitle(str2);
		builder.setPositiveButton("联系客服",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent intent = new Intent(
								Intent.ACTION_CALL,
								Uri.parse("tel:"
										+ ConstantForSaveList.CARSON_CALL_NUMBER));
						ForgetPwdActivity.this.startActivity(intent);
					}
				});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});
		builder.create().show();
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
					// Intent intent = new Intent();
					// intent.setClass(getApplicationContext(),
					// FindPJLoginActivity.class);
					// startActivity(intent);
					Editor edit = sp.edit();
					edit.putString("remember_pwd", "");
					edit.commit();
					ForgetPwdActivity.this.finish();
				}
			}
		});
		builder.create().show();
	}

	@OnClick(R.id.code)
	public void sendMSM(View view) {
		telephoneStr = telephone.getText().toString();
		if (Util.isMobileNO(telephoneStr)) {
			telephoneStrTemp = telephoneStr;
			current_time = System.currentTimeMillis();
			if (current_time - ConstantForSaveList.regist_time > 60 * 1000) {
				code.setClickable(false);
				handler.postDelayed(runnable, 1000);
				sendMSM();
			} else {
				showToast("一分钟内请勿重复提交^_^");
				code.setClickable(false);
				handler.postDelayed(runnable2, 10);
			}

		} else {
			showToast(getResources().getString(R.string.regist_edt_right_tel));
		}
	}

	@OnClick(R.id.regin)
	public void reginOnclick(View v) {
		telephoneStr = telephone.getText().toString();
		passwordStr = password.getText().toString().trim();
		codeStr = inputcode.getText().toString();
		if (check()) {
			showWait(true);
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
								// Toast.makeText(ForgetPwdActivity.this,
								// ""+status, 0).show();
								if (status == 2) {
									showAlertDialog("现在就去登录吧", "修改成功");
								} else {
									String msg = sd.getString("msg");
									if (msg == null || "".equals(msg))
										msg = "修改失败";
									showToast(msg);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError volleyError) {
							showWait(false);
							showAlertDialog("你的网络不够给力，再试一次吧！", "找回失败");
						}
					}) {
				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					Map<String, String> map = new HashMap<String, String>();
					map.put("telephone", telephoneStr);
					map.put("password", JiaoyanUtil.MD5(passwordStr));
					map.put("code", codeStr);
					return map;
				}
			};
			queue.add(request);
			request.setRetryPolicy(new DefaultRetryPolicy(
					ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

		}
	}

	public boolean check() {
		// codeStr = inputcode.getText().toString();
		// if (codeStr == null
		// || (!codeStr.equals(codeStrget) && (!codeStr.equals("9999999")))) {
		// showToast("验证码错误");
		// return false;
		// }
		if (!codeFlag) {
			showToast(getResources().getString(R.string.regist_code_error));
			return false;
		}
		passwordStr = password.getText().toString();
		againpasswordStr = againpassword.getText().toString();
		if (passwordStr == null || againpasswordStr == null
				|| !passwordStr.equals(againpasswordStr)) {
			showToast("两次输入的密码不相同");
			return false;
		}
		if (passwordStr.trim().length() < 6) {
			showToast("密码不能少于6个字符");
			return false;
		}
		if (telephoneStrTemp == null || telephoneStr == null
				|| !telephoneStrTemp.equals(telephoneStr)) {
			showToast("手机号码改变请重新获取验证码");
			code.setText("发送验证码");
			recLen = 1;
			code.setClickable(true);
			return false;
		}

		return true;
	}

	public void sendMSM() {
		StringRequest request2 = new StringRequest(Request.Method.POST,
				sendMSMUrl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jsstatus = js
									.getJSONObject("ResponseStatus");
							String msg = jsstatus.getString("msg");
							if (jsstatus.getInt("status") == 1) {
								ConstantForSaveList.regist_time = current_time;// 保存进入界面的时间
							} else {
								showToast(msg);
							}
						} catch (JSONException e) {
							showToast("获取验证码失败");
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("获取验证码失败,请检查网络环境");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("telephone", telephoneStr);
				return map;
			}
		};
		queue.add(request2);
		request2.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	// 倒计时
	int recLen = 60;
	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			recLen--;
			if (recLen > 0) {
				code.setText("等待" + recLen + "秒");
				handler.postDelayed(this, 1000);
			} else {
				code.setText("发送验证码");
				recLen = 60;
				code.setClickable(true);
			}
		}
	};
	long recLen2 = 60;
	Runnable runnable2 = new Runnable() {
		@Override
		public void run() {
			recLen2--;
			if (recLen2 > 0) {
				code.setText(getResources().getString(R.string.regist_wait)
						+ recLen2
						+ getResources().getString(R.string.regist_second));
				handler.postDelayed(this, 1000);
			} else {
				code.setText(getResources().getString(R.string.regist_sendcode));
				recLen2 = 60;
				code.setClickable(true);
			}
		}
	};

	// ====倒計時end===========
	@Override
	protected void onStop() {
		super.onStop();
		queue.cancelAll(TAG);
	}

}
