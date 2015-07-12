package com.quark.setting;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
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
import com.quark.jianzhidaren.BaseActivity;
import com.parttime.login.FindPJLoginActivity;
import com.parttime.main.MainTabActivity;
import com.quark.ui.widget.CustomDialog;
import com.quark.utils.Util;

/**
 * 修改登陆手机号码
 * 
 * @author Administrator
 * 
 */
public class EditPhoneActivity extends BaseActivity {
	@ViewInject(R.id.telephoen_erro)
	private TextView telephoen_erro;
	@ViewInject(R.id.telephone)
	private EditText telephone;
	// 发送验证码按钮
	@ViewInject(R.id.code)
	private Button code;
	@ViewInject(R.id.inputcode)
	private EditText inputcode;
	@ViewInject(R.id.password)
	private EditText password;
	@ViewInject(R.id.telephone_new)
	private EditText telephone_new;
	@ViewInject(R.id.code_vetify_imv)
	private ImageView codeVetifyImv;

	private String telephoneStr;
	private String telephoneStrTemp;
	private String telephoneNewStr;
	private String passwordStr;
	private ImageButton imageBtn;
	private String url;
	private String sendMSMUrl;
	private String codeStr;
	private String codeStrget;
	private long current_time;// 点击发送验证码的时候
	String jiaoyanUrl;// 校验验证码是否正确
	private boolean codeFlag;// 验证码是否正确

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit_telephone);
		ViewUtils.inject(this);
		url = Url.COMPANY_modifyTelephon + "?token="
				+ MainTabActivity.token;
		sendMSMUrl = Url.COMPANY_SEND_TEL_CODE + "?token="
				+ MainTabActivity.token;
		RelativeLayout topLayout = (RelativeLayout) findViewById(R.id.top_title_layout);
		topLayout.setBackgroundColor(getResources().getColor(
				R.color.guanli_common_color));
		jiaoyanUrl = Url.MESSAGE_VALIDATE + "?token="
				+ MainTabActivity.token;

		// 返回
		imageBtn = (ImageButton) findViewById(R.id.back);
		imageBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
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
				map.put("telephone", telephoneNewStr);
				map.put("code", code);
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
	}

	@OnClick({ R.id.submit, R.id.code })
	public void telephoneOnclick(View view) {
		switch (view.getId()) {
		case R.id.submit:
			modifyTelephone();
			break;
		case R.id.code:
			telephoneNewStr = telephone_new.getText().toString();
			if (Util.isMobileNO(telephoneNewStr)) {
				telephoneStrTemp = telephoneNewStr;
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
				showToast("请输入正确的新手机号码");
			}
			break;
		default:
			break;
		}
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
							// 1 成功 其它 失败
							if (jsstatus.getInt("status") == 1) {
								// 发送成功
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
						showToast("获取验证码失败，请检查网络环境");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("telephone", telephoneNewStr);
				return map;
			}
		};
		queue.add(request2);
		request2.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	public void modifyTelephone() {
		telephoneStr = telephone.getText().toString();
		passwordStr = password.getText().toString();
		telephoneNewStr = telephone_new.getText().toString();
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
								// Toast.makeText(EditPhoneActivity.this,
								// ""+status, 0).show();
								if (status == 2) {// 用户不存在的时候
									// 修改成功
									Toast.makeText(EditPhoneActivity.this,
											"修改手机号码成功", Toast.LENGTH_SHORT)
											.show();
									SharedPreferences sp = getSharedPreferences(
											"jrdr.setting", MODE_PRIVATE);
									Editor edit = sp.edit();
									edit.putString("remember_tele",
											telephoneNewStr);
									edit.commit();
									EditPhoneActivity.this.finish();

								} else {
									String msg = sd.getString("msg");
									if (msg == null || "".equals(msg)) {
										msg = "修改失败";
									}
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
							showAlertDialog("你的网络不够给力，再试一次吧！", "修改失败");
						}
					}) {
				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					Map<String, String> map = new HashMap<String, String>();
					map.put("telephone", telephoneStr);
					map.put("password", JiaoyanUtil.MD5(passwordStr));
					map.put("new_telephone", telephoneNewStr);
					map.put("code", codeStr);
					return map;
				}
			};
			queue.add(request);
			request.setRetryPolicy(new DefaultRetryPolicy(
					ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

		}
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
						EditPhoneActivity.this.startActivity(intent);
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

	public boolean check() {
		if (!Util.isMobileNO(telephoneStr)) {
			showToast("请输入正确的手机号码");
			return false;
		}
		// codeStr = inputcode.getText().toString();
		// if (codeStr == null || (!codeStr.equals(codeStrget))) {
		// showToast("验证码错误");
		// return false;
		// }
		if (!codeFlag) {
			showToast(getResources().getString(R.string.regist_code_error));
			return false;
		}
		passwordStr = password.getText().toString();
		if (!Util.isEmpty(passwordStr)) {
			showToast("请输入密码");
			return false;
		}
		if (telephoneStrTemp == null || telephoneNewStr == null
				|| !telephoneStrTemp.equals(telephoneNewStr)) {
			showToast("手机号码改变请重新获取验证码");
			code.setText("发送验证码");
			recLen = 1;
			code.setClickable(true);
			return false;
		}

		return true;
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
}
