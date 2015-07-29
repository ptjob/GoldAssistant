package com.parttime.login;

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
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoApplication;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.parttime.constants.SharedPreferenceConstants;
import com.parttime.main.MainTabActivity;
import com.parttime.net.ResponseBaseCommonError;
import com.parttime.utils.SharePreferenceUtil;
import com.qingmu.jianzhidaren.BuildConfig;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.jianzhidaren.ApplicationControl;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.jianzhidaren.FeiJiPageActivity;
import com.quark.jianzhidaren.ForgetPwdActivity;
import com.quark.jianzhidaren.RegisterActivity;
import com.quark.ui.widget.LineEditText;
import com.quark.utils.Util;
import com.umeng.analytics.MobclickAgent;

/**
 * 登陆
 * 
 * @author carson
 * 
 */
public class FindPJLoginActivity extends BaseActivity {

    public static final String SP_JRDR_SETTING = "jrdr.setting";
    public static final String EXTRA_USER_ID = "userId";
    public static final String EXTRA_REMEMBER_TELE = "remember_tele";
    public static final String EXTRA_REMEMBER_PWD = "remember_pwd";
    private Button login, regin, forgetPwd;
	static String url;
	private ImageView logoImv;// 图标 商家端的头像换为红色、用户端蓝色
	private TextView logoTextView;// 找兼职、招兼职
	private String telephoneStr;
	private String passwordStr;
	LineEditText telephoneView;
	LineEditText passwordView;
	public static FindPJLoginActivity instance;
	private SharedPreferences sp;
	// 环信
	private boolean progressShow;
	private boolean autoLogin = false;
	private String carson_user_id;// 判断是否保存的用户

	@Override
	protected void onResume() {
		super.onResume();
		if (autoLogin) {
			return;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences(SP_JRDR_SETTING, MODE_PRIVATE);
		// 如果用户名密码都有，直接进入主页面
		if (DemoHXSDKHelper.getInstance().isLogined()) {
			carson_user_id = sp.getString(EXTRA_USER_ID, "");
			if (carson_user_id != null && !"".equals(carson_user_id)) {
				autoLogin = true;
				startActivity(new Intent(FindPJLoginActivity.this,
						MainTabActivity.class));
				FindPJLoginActivity.this.finish();
				return;
			}
		}
		setContentView(R.layout.entry_findpartjob);
		getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// 隐藏软键盘
		ViewUtils.inject(this);
		instance = this;
		logoImv = (ImageView) findViewById(R.id.login_logo_imv);
		logoTextView = (TextView) findViewById(R.id.lognText);
		logoTextView.setText(R.string.recruit_part_time);
		logoImv.setBackgroundResource(R.drawable.login_logo_c);
		Button button = (Button) findViewById(R.id.look);
		button.setVisibility(View.GONE);
		url = Url.COMPANY_LOGIN ;

		telephoneView = (LineEditText) findViewById(R.id.telephone);
		passwordView = (LineEditText) findViewById(R.id.password);
		// 设置默认帐号、密码
		String remember_tele = sp.getString(EXTRA_REMEMBER_TELE, "");
		String remember_pwd = sp.getString(EXTRA_REMEMBER_PWD, "");
		if (remember_tele != null && !"".equals(remember_tele)
				&& remember_pwd != null && !"".equals(remember_pwd)) {
			telephoneView.setText(remember_tele);
			passwordView.setText(remember_pwd);
		}

		// 登陆
		login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				telephoneStr = telephoneView.getText().toString();
				passwordStr = passwordView.getText().toString();
				if (check()) {
					login.setClickable(false);
                    if(BuildConfig.DEBUG){
                        login.setClickable(true);
                    }
					login();
				}
			}
		});
		// 注册
		regin = (Button) findViewById(R.id.regin);
		regin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(FindPJLoginActivity.this,
						RegisterActivity.class);
				startActivityForResult(intent, 211);
			}
		});

		// 忘记密码
		forgetPwd = (Button) findViewById(R.id.forgetPwd);
		forgetPwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(FindPJLoginActivity.this,
//						ForgetPwdActivity.class);
						com.parttime.login.ForgetPwdActivity.class);
						startActivity(intent);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
			FindPJLoginActivity.this.finish();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			// 监控/拦截菜单键
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {
			// 由于Home键为系统键，此处不能捕获，需要重写onAttachedToWindow()
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 211) {
			if (resultCode == RESULT_OK) {
				// FindPJLoginActivity.this.finish();
			}
		}
	}

	private String token;
	private int user_id;
	private String IM_PASSWORD;// 环信登陆密码
	private String IM_USERID;// 环信登陆账户
	private String IM_AVATAR;// 环信头像
	private String IM_NIKENAME;// 环信昵称

	public void login() {
		showWait(true);
		StringRequest request = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject json = new JSONObject(response);
							int status = json.getInt("status");
							if (status == 1) {
                                // 记录用户id 环信登陆id 密码 昵称 头像
                                JSONObject jsonts = json
                                        .getJSONObject("loginResponse");
                                token = jsonts.getString("token");
                                user_id = jsonts.getInt("company_id");
                                IM_PASSWORD = jsonts.getString("IM_PASSWORD");
                                IM_USERID = jsonts.getString("IM_USERID");
                                IM_AVATAR = jsonts.getString("IM_AVATAR");
                                IM_NIKENAME = jsonts.getString("IM_NIKENAME");
								int type = jsonts.getInt("type");
								SharePreferenceUtil.getInstance(FindPJLoginActivity.this).saveSharedPreferences(SharedPreferenceConstants.USER_TYPE, type);
								loginIM(IM_USERID, IM_PASSWORD);

							} else {
                                login.setClickable(true);
                                ResponseBaseCommonError error = new Gson().fromJson(response, ResponseBaseCommonError.class);
                                if(error != null) {
                                    showToast(error.msg);
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
						showToast("请求服务器失败");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<>();
				map.put("telephone", telephoneStr);
				map.put("password", JiaoyanUtil.MD5(passwordStr));
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
	}

	public boolean check() {
		/*if (!Util.isMobileNO(telephoneStr)) {
			showToast("请输入正确手机号码");
			return false;
		}
		if (!Util.isEmpty(passwordStr)) {
			showToast("请输入密码");
			return false;
		}*/
		return true;
	}

	// 随便看看
	@OnClick(R.id.look)
	public void lookOnclick(View v) {
		Intent intent = new Intent();
		intent.setClass(FindPJLoginActivity.this, MainTabActivity.class);
		startActivity(intent);
		FindPJLoginActivity.this.finish();
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
			final ProgressDialog pd = new ProgressDialog(
					FindPJLoginActivity.this);
			pd.setCanceledOnTouchOutside(false);
			pd.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					progressShow = false;
				}
			});
			pd.setMessage("正在登录...");
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
									pd.setMessage("正在获取未读信息列表...");
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
								Map<String, com.easemob.chatuidemo.domain.User> userlist = new HashMap<>();
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
								newFriends.setNick(getString(R.string.apply_notify));
								newFriends.setHeader("");
								userlist.put(Constant.NEW_FRIENDS_USERNAME,
										newFriends);
								// 添加"群聊"
								com.easemob.chatuidemo.domain.User groupUser = new com.easemob.chatuidemo.domain.User();
								groupUser.setUsername(Constant.GROUP_USERNAME);
								groupUser.setNick(getString(R.string.group_chat));
								groupUser.setHeader("");
								userlist.put(Constant.GROUP_USERNAME, groupUser);
								// 添加"官方账号"
								com.easemob.chatuidemo.domain.User publicCount = new com.easemob.chatuidemo.domain.User();
                                publicCount.setUsername(Constant.PUBLIC_COUNT);
                                publicCount.setNick(getString(R.string.public_count));
                                publicCount.setHeader("");
								userlist.put(Constant.PUBLIC_COUNT, publicCount);
								// 存入内存
								ApplicationControl.getInstance()
										.setContactList(userlist);
								// 存入db
								UserDao dao = new UserDao(
										FindPJLoginActivity.this);
								List<com.easemob.chatuidemo.domain.User> users = new ArrayList<>(
										userlist.values());
								dao.saveContactList(users);
								// 获取群聊列表(群聊里只有groupid和groupname等简单信息，不包含members),sdk会把群组存入到内存和db中
								EMGroupManager.getInstance()
										.getGroupsFromServer();
							} catch (Exception e) {
								e.printStackTrace();
								runOnUiThread(new Runnable() {
									public void run() {
										if (!FindPJLoginActivity.this
												.isFinishing())
											pd.dismiss();
										ApplicationControl.getInstance().logout(
												null);
										Toast.makeText(getApplicationContext(),
												"登录失败", Toast.LENGTH_SHORT).show();

									}
								});
								return;
							}
							// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
							boolean updatenick = EMChatManager.getInstance()
									.updateCurrentUserNick(
											ApplicationControl.currentUserNick);
							if (!updatenick) {
								EMLog.e("LoginActivity",
										"update current user nick fail");
							}

							if (!FindPJLoginActivity.this.isFinishing())
								pd.dismiss();
							// 进入主页面

							SharedPreferences sp = getSharedPreferences(
									"jrdr.setting", MODE_PRIVATE);
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
							intent.setClass(FindPJLoginActivity.this,
									MainTabActivity.class);
							startActivity(intent);
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
									showToast("登录失败");
								}
							});
						}
					});

		}
	}

	/**
	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
	 * 
	 * @param username String
	 * @param user com.easemob.chatuidemo.domain.User
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
				MobclickAgent.onEventValue(FindPJLoginActivity.this, "login1",
						params, (int) costTime);
				MobclickAgent.onEventDuration(FindPJLoginActivity.this,
						"login1", (int) costTime);
			}
		});
	}

	private void loginFailure2Umeng(final long start, final int code,
			final String message) {
		runOnUiThread(new Runnable() {
			@SuppressWarnings("deprecation")
			public void run() {
				long costTime = System.currentTimeMillis() - start;
				Map<String, String> params = new HashMap<String, String>();
				params.put("status", "failure");
				params.put("error_code", code + "");
				params.put("error_description", message);
				MobclickAgent.onEventValue(FindPJLoginActivity.this, "login1",
						params, (int) costTime);
				
				MobclickAgent.onEventDuration(FindPJLoginActivity.this,
						"login1", (int) costTime);

			}
		});
	}

}
