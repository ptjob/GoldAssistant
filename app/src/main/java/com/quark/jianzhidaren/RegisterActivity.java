package com.quark.jianzhidaren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.carson.constant.JiaoyanUtil;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.ui.widget.CustomDialog;
import com.quark.utils.Util;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @ClassName: RegisterActivity
 * @Description: TODO
 * @author howe
 * @date 2015-1-9 下午5:11:49
 * 
 */
public class RegisterActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.telephone)
	private EditText telephone;
	// 发送验证码按钮
	@ViewInject(R.id.code)
	private Button code;
	@ViewInject(R.id.inputcode)
	private EditText inputcode;
	@ViewInject(R.id.name)
	private EditText name;
	@ViewInject(R.id.password)
	private EditText password;
	@ViewInject(R.id.againpassword)
	private EditText againpassword;
	@ViewInject(R.id.checkbox)
	private CheckBox checkbox;
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
	String url;// 注册url
	String sendMSMUrl;
	String jiaoyanUrl;// 校验验证码是否正确

	private String token;
	private int user_id;
	private String loginUrl;// 登陆url
	private String IM_PASSWORD;// 环信登陆密码
	private String IM_USERID;// 环信登陆账户
	private String IM_AVATAR;// 环信头像
	private String IM_NIKENAME;// 环信昵称
	// 环信
	private boolean progressShow;
	private SharedPreferences sp;
	private long current_time;// 点击发送验证码的时候
	private boolean codeFlag;// 验证码是否正确

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		ViewUtils.inject(this);
		setTitle(getResources().getString(R.string.regist_regist));
		setBackButton();
		setTopTitle(getResources().getString(R.string.regist_regist));
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		url = Url.COMPANY_REGIST + "?token=" + MainCompanyActivity.token;
		loginUrl = Url.COMPANY_LOGIN + "?token=" + MainCompanyActivity.token;
		sendMSMUrl = Url.COMPANY_SENDMSM + "?token="
				+ MainCompanyActivity.token;
		jiaoyanUrl = Url.MESSAGE_VALIDATE + "?token="
				+ MainCompanyActivity.token;
		TextView tx = (TextView) findViewById(R.id.rgtext);
		tx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(RegisterActivity.this, AgreementActivity.class);
				startActivity(intent);
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
						RegisterActivity.this.startActivity(intent);
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
				if (str2.equals(getResources().getString(
						R.string.regist_regist_success))) {
					login();// 注册成功直接登陆
				}
			}
		});
		builder.create().show();
	}

	public void login() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST,
				loginUrl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject json = new JSONObject(response);
							JSONObject jsont = json
									.getJSONObject("ResponseStatus");
							int status = jsont.getInt("status");
							if (status == 1) {
								showToast(getResources().getString(
										R.string.regist_not_regist_company));
							} else if (status == 2) {
								showToast(getResources().getString(
										R.string.regist_login_pwd_error));
							} else if (status == 4) { // 放飞机
								String activity_title = jsont
										.getString("run_over_activity_title");
								Intent intent = new Intent();
								intent.setClass(RegisterActivity.this,
										FeiJiPageActivity.class);
								intent.putExtra("title", activity_title + "");
								startActivity(intent);
								finish();
							} else {
								// 记录用户id 环信登陆id 密码 昵称 头像
								JSONObject jsonts = json
										.getJSONObject("LoginResponse");
								token = jsonts.getString("token");
								user_id = jsonts.getInt("company_id");
								IM_PASSWORD = jsonts.getString("IM_PASSWORD");
								IM_USERID = jsonts.getString("IM_USERID");
								IM_AVATAR = jsonts.getString("IM_AVATAR");
								IM_NIKENAME = jsonts.getString("IM_NIKENAME");
								loginIM(IM_USERID, IM_PASSWORD);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						showWait(false);
						showToast(getResources().getString(
								R.string.regist_request_server_fail));
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("telephone", telephoneStr);
				map.put("password", JiaoyanUtil.MD5(passwordStr));
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
	}

	// 环信 登陆
	private void loginIM(String userName, String passWord) {
		ApplicationControl.currentUserNick = "ydt01";
		// 如果用户名密码都有，直接进入主页面
		/*
		 * if (DemoHXSDKHelper.getInstance().isLogined()) { autoLogin = true;
		 * startActivity(new Intent(Login.this, Main.class)); return; }
		 */
		// ApplicationControl.currentUserNick = "ydt01";
		final String username = userName;
		final String password = passWord;
		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
			progressShow = true;
			final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
			pd.setCanceledOnTouchOutside(false);
			pd.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					progressShow = false;
				}
			});
			pd.setMessage(getResources().getString(R.string.regist_islogin)
					+ "...");
			pd.show();

			final long start = System.currentTimeMillis();
			// 调用sdk登陆方法登陆聊天服务器
			EMChatManager.getInstance().login(username, password,
					new EMCallBack() {

						@Override
						public void onSuccess() {
							// umeng自定义事件，开发者可以把这个删掉
							loginSuccess2Umeng(start);

							if (!progressShow) {
								return;
							}
							// 登陆成功，保存用户名密码
							ApplicationControl.getInstance().setUserName(
									username);
							ApplicationControl.getInstance().setPassword(
									password);
							runOnUiThread(new Runnable() {
								public void run() {
									pd.setMessage(getResources().getString(
											R.string.regist_getunreadinfor)
											+ "...");
								}
							});
							try {
								// ** 第一次登录或者之前logout后，加载所有本地群和回话
								// ** manually load all local groups and
								// conversations in case we are auto login
								EMGroupManager.getInstance().loadAllGroups();
								EMChatManager.getInstance()
										.loadAllConversations();

								// demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
								List<String> usernames = EMContactManager
										.getInstance().getContactUserNames();
								Map<String, com.easemob.chatuidemo.domain.User> userlist = new HashMap<String, com.easemob.chatuidemo.domain.User>();
								for (String username : usernames) {
									com.easemob.chatuidemo.domain.User user = new com.easemob.chatuidemo.domain.User();
									user.setUsername(username);
									setUserHearder(username, user);
									userlist.put(username, user);
								}
								// 添加user"申请与通知"
								com.easemob.chatuidemo.domain.User newFriends = new com.easemob.chatuidemo.domain.User();
								newFriends
										.setUsername(Constant.NEW_FRIENDS_USERNAME);
								newFriends.setNick("申请与通知");
								newFriends.setHeader("");
								userlist.put(Constant.NEW_FRIENDS_USERNAME,
										newFriends);
								// 添加"群聊"
								com.easemob.chatuidemo.domain.User groupUser = new com.easemob.chatuidemo.domain.User();
								groupUser.setUsername(Constant.GROUP_USERNAME);
								groupUser.setNick("群聊");
								groupUser.setHeader("");
								userlist.put(Constant.GROUP_USERNAME, groupUser);

								// 存入内存
								ApplicationControl.getInstance()
										.setContactList(userlist);
								// 存入db
								UserDao dao = new UserDao(RegisterActivity.this);
								List<com.easemob.chatuidemo.domain.User> users = new ArrayList<com.easemob.chatuidemo.domain.User>(
										userlist.values());
								dao.saveContactList(users);

								// 获取群聊列表(群聊里只有groupid和groupname等简单信息，不包含members),sdk会把群组存入到内存和db中
								EMGroupManager.getInstance()
										.getGroupsFromServer();
							} catch (Exception e) {
								e.printStackTrace();
							}
							// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
							boolean updatenick = EMChatManager.getInstance()
									.updateCurrentUserNick(
											ApplicationControl.currentUserNick);
							if (!updatenick) {
								EMLog.e("LoginActivity",
										"update current user nick fail");
							}

							if (!RegisterActivity.this.isFinishing())
								pd.dismiss();
							// 进入主页面

							Editor edit = sp.edit();
							edit.putString("userId", "" + user_id);
							edit.putString("token", token);
							edit.putString("IM_PASSWORD", IM_PASSWORD);
							edit.putString("IM_USERID", IM_USERID);
							edit.putString("IM_AVATAR", IM_AVATAR);
							edit.putString("IM_NIKENAME", IM_NIKENAME);
							// 记住密码供下次登陆
							edit.putString("remember_tele", telephoneStr);
							edit.putString("remember_pwd", passwordStr);
							edit.commit();
							Intent intent = new Intent();
							intent.setClass(RegisterActivity.this,
									MainCompanyActivity.class);
							startActivity(intent);
							// setResult(Activity.RESULT_OK);
							finish();
						}

						@Override
						public void onProgress(int progress, String status) {

						}

						@Override
						public void onError(final int code, final String message) {
							loginFailure2Umeng(start, code, message);

							if (!progressShow) {
								return;
							}
							runOnUiThread(new Runnable() {
								public void run() {
									pd.dismiss();
									showToast(getResources().getString(
											R.string.regist_login_fail));
								}
							});
						}
					});

		}
	}

	/**
	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
	 * 
	 * @param username
	 * @param user
	 */
	protected void setUserHearder(String username,
			com.easemob.chatuidemo.domain.User user) {
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance()
					.get(headerName.substring(0, 1)).get(0).target.substring(0,
					1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
	}

	private void loginSuccess2Umeng(final long start) {
		runOnUiThread(new Runnable() {
			public void run() {
				long costTime = System.currentTimeMillis() - start;
				Map<String, String> params = new HashMap<String, String>();
				params.put("status", "success");
				MobclickAgent.onEventValue(RegisterActivity.this, "login1",
						params, (int) costTime);
				MobclickAgent.onEventDuration(RegisterActivity.this, "login1",
						(int) costTime);
			}
		});
	}

	private void loginFailure2Umeng(final long start, final int code,
			final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				long costTime = System.currentTimeMillis() - start;
				Map<String, String> params = new HashMap<String, String>();
				params.put("status", "failure");
				params.put("error_code", code + "");
				params.put("error_description", message);
				MobclickAgent.onEventValue(RegisterActivity.this, "login1",
						params, (int) costTime);
				MobclickAgent.onEventDuration(RegisterActivity.this, "login1",
						(int) costTime);

			}
		});
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
		nameStr = name.getText().toString();
		codeStr = inputcode.getText().toString();
		if (check()) {
			showWait(true);
			MobclickAgent.onEvent(RegisterActivity.this, "onclick3", "立即注册");
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

								if (status == 1) {
									showAlertDialog(
											getResources().getString(
													R.string.regist_login_now),
											getResources()
													.getString(
															R.string.regist_regist_success));
								} else {
									String msg = sd.getString("msg");
									if (msg == null || "".equals(msg)) {
										msg = "注册失败";
									}
									showAlertDialog(
											msg,
											getResources()
													.getString(
															R.string.regist_regist_fail));
								}
								recLen = 1;
								code.setClickable(true);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError volleyError) {
							showWait(false);
							showAlertDialog(
									getResources().getString(
											R.string.regist_net_bad),
									getResources().getString(
											R.string.regist_regist_fail));
						}
					}) {
				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					Map<String, String> map = new HashMap<String, String>();
					map.put("telephone", telephoneStr);
					map.put("password", JiaoyanUtil.MD5(passwordStr));// md5
					map.put("name", nameStr);
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
		if (nameStr == null || !Util.isUserName(nameStr)) {
			showToast(getResources().getString(R.string.regist_name_not_hefa));
			return false;
		}
		// codeStr = inputcode.getText().toString();
		// if (codeStr == null || (!codeStr.equals(codeStrget))) {
		// showToast(getResources().getString(R.string.regist_code_error));
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
			showToast(getResources().getString(R.string.regist_name_not_same));
			return false;
		}
		if (passwordStr.trim().length() < 6) {
			showToast(getResources().getString(
					R.string.regist_pwd_not_below_six));
			return false;
		} else {
			if (passwordStr.trim().length() < 6) {
				showToast(getResources().getString(
						R.string.regist_pwd_not_below_six));
				return false;
			}
		}

		if (!checkbox.isChecked()) {
			showToast(getResources().getString(
					R.string.regist_confirm_shengming));
			return false;
		}
		if (telephoneStrTemp == null || telephoneStr == null
				|| !telephoneStrTemp.equals(telephoneStr)) {
			showToast(getResources().getString(
					R.string.regist_tel_change_get_code));
			code.setText(getResources().getString(R.string.regist_sendcode));
			recLen = 1;
			code.setClickable(true);
			return false;
		}

		return true;
	}

	public void sendMSM() {
		MobclickAgent.onEvent(RegisterActivity.this, "onclick2", "注册发送验证码");
		StringRequest request2 = new StringRequest(Request.Method.POST,
				sendMSMUrl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jsstatus = js
									.getJSONObject("ResponseStatus");
							int status = jsstatus.getInt("status");
							String msg = jsstatus.getString("msg");
							if (status == 1) {
								// 成功
								ConstantForSaveList.regist_time = current_time;// 保存进入界面的时间
							} else {
								showToast(msg);
							}

						} catch (JSONException e) {
							showToast(getResources().getString(
									R.string.regist_getcode_fail));
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
				code.setText(getResources().getString(R.string.regist_wait)
						+ recLen
						+ getResources().getString(R.string.regist_second));
				handler.postDelayed(this, 1000);
			} else {
				code.setText(getResources().getString(R.string.regist_sendcode));
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

	@Override
	public void onClick(View v) {

	}
}
