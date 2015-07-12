package com.parttime.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.carson.https.Tools;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.EMNotifier;
import com.easemob.chat.GroupChangeListener;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.Constant;
import com.parttime.IM.ChatActivity;
import com.easemob.chatuidemo.activity.GroupsActivity;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.InviteMessage;
import com.easemob.chatuidemo.domain.InviteMessage.InviteMesageStatus;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.EasyUtils;
import com.easemob.util.HanziToPinyin;
import com.easemob.util.NetUtils;
import com.parttime.common.update.UpdateUtils;
import com.qingmu.jianzhidaren.BuildConfig;
import com.qingmu.jianzhidaren.R;
import com.quark.common.JsonUtil;
import com.quark.common.Url;
import com.quark.fragment.company.ManageFragmentCompany;
import com.quark.jianzhidaren.ApplicationControl;
import com.quark.jianzhidaren.EnterActivity;
import com.parttime.login.FindPJLoginActivity;
import com.quark.jianzhidaren.LaheiPageActivity;
import com.quark.model.Function;
import com.quark.model.HuanxinUser;
import com.quark.ui.widget.CustomDialog;
import com.quark.utils.ConfigDataUtil;
import com.quark.utils.NetWorkCheck;
import com.quark.utils.WaitDialog;
import com.quark.volley.VolleySington;
import com.umeng.analytics.MobclickAgent;

public class MainTabActivity extends FragmentActivity implements
		AMapLocationListener {


    public static final String PINGBI = "pingbi";
    protected RequestQueue queue;
	protected WaitDialog dialog;
	// 极光推送
	private MessageReceiver jpushMessageReceiver;
	
	protected static final String TAG = "MainTabActivity";
	public static final String MESSAGE_RECEIVED_ACTION = "com.company.jpush.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	
	Function function = new Function();
	private String company_infor_url;// 商家信息
	//private TextView unread_guanli_msg_tv;// 未处理报名信息
	private TextView unread_msg_tv;// 未查看消息条数
	private String company_id;// 商家id
	
	// ===========高德地图=================
	private LocationManagerProxy mLocationManagerProxy;
	
	// =========环信===========
	protected NotificationManager notificationManager;
	private static final int notifiId = 11;
	
	private MessageAndAddressFragment messageAndAddressFragment;
	// 当前fragment的index
	private int currentTabIndex;
	private NewMessageBroadcastReceiver msgReceiver;
	
	// 账号在别处登录
	public boolean isConflict = false;
	
	// 账号被移除
	public static boolean isForeground = false;// 界面是否在前端运行
	private boolean isCurrentAccountRemoved = false;
	private SharedPreferences sp;
	private MyGroupChangeListener myGroupChangeListener;
	private String getFriendListUrl;// 获取服务端好友列表url
	List<String> usernames = new ArrayList<>();// 好友列表显示的是uid
														// u1007或者c100之类

	/**
	 * 检查当前用户是否被删除
	 */
	public boolean getCurrentAccountRemoved() {
		return isCurrentAccountRemoved;
	}

	// =========环信end===========
	public static MainTabActivity instens;
	private TextView  tv2, tv3, tv4;
	private ImageView  tv2_boom, tv3_boom, tv4_boom;
	private FragmentManager fm;
	private static PAGER mPager = PAGER.MESSAGE;
	private List<TextView> lists = new ArrayList<>();
	private List<ImageView> lists_boom = new ArrayList<>();
	public static String token = "notoken";
	private int[] resIdActive = new int[] { 
			R.drawable.tab_btn_manage_sel,
			R.drawable.tab_btn_group_sel, 
			R.drawable.tab_btn_setting_sel };
	private int[] resId = new int[] { 
			R.drawable.tab_btn_manage_nor,
			R.drawable.tab_btn_group_nor, 
			R.drawable.tab_btn_settings_nor };

	private int currentVerCode;// 当前应用版本号
	private String isForce, isAlert;// 是否强制更新,是否弹框更新 : 1表示是
	private String server_apk_downloadUrl, update_contentStr;
	private int server_vercode;// 服务器端版本号
	private ProgressDialog pd;
	private boolean downFlag = false;// 开始下载的标志

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
		setContentView(R.layout.activity_main_company);
		createNoMediaFile();// 隐藏图片
		
		queue = VolleySington.getInstance().getRequestQueue();
		initSpAndParam();
		initUrl();
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		fm = getFragmentManager();
		instens = this;
		initGaoDe();
		
		initJiGuangPush();

		selectedFragment(PAGER.MESSAGE);
		updatebNav(PAGER.MESSAGE.getResId());
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		ConfigDataUtil.getInstance().setDisplayMetrics(dm);
		
		// 检测更新
		updateLog();
		// login();

		// ==========================环信=============================
		if (savedInstanceState != null
				&& savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED,
						false)) {
			// 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// 三个fragment里加的判断同理
			ApplicationControl.getInstance().logout(null);
			Editor edit = sp.edit();
			edit.putString("userId", "");
			edit.putString("token", "");
			edit.commit();

			finish();
			return;
		} else if (savedInstanceState != null
				&& savedInstanceState.getBoolean("isConflict", false)) {
			// 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// 三个fragment里加的判断同理
			Editor edit = sp.edit();
			edit.putString("userId", "");
			edit.putString("token", "");
			edit.commit();
			finish();
			return;
		}
		
		initView();
		
		// MobclickAgent.setDebugMode( true );
		MobclickAgent.updateOnlineConfig(this);

		if (getIntent().getBooleanExtra("conflict", false)
				&& !isConflictDialogShow) {
			showConflictDialog();
		} else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
				&& !isAccountRemovedDialogShow) {
			showAccountRemovedDialog();
		}

		inviteMessgeDao = new InviteMessgeDao(this);
		userDao = new UserDao(this);
		messageAndAddressFragment = new MessageAndAddressFragment();
		
		initEasemob();

		// ************************如果登陆过，app长期在后台再进的时候也可能会导致加载到内存的群组和会话为空*************************************
		if (company_id != null && !"".equals(company_id)) {
			new Thread() {
				public void run() {
					EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();
					// demo中每次登陆都去获取好友username，开发者自己根据情况而定
					try {
						if (EMContactManager.getInstance() != null) {
							usernames = EMContactManager.getInstance()
									.getContactUserNames();
						}
						Map<String, com.easemob.chatuidemo.domain.User> userlist = new HashMap<>();
						for (String username : usernames) {
							com.easemob.chatuidemo.domain.User user = new com.easemob.chatuidemo.domain.User();
							user.setUsername(username);
							setUserHearder(username, user);
							userlist.put(username, user);
						}
						// 添加user"申请与通知"
						com.easemob.chatuidemo.domain.User newFriends = new com.easemob.chatuidemo.domain.User();
						newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
						newFriends.setNick("申请与通知");
						newFriends.setHeader("");
						userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
						// 添加"群聊"
						com.easemob.chatuidemo.domain.User groupUser = new com.easemob.chatuidemo.domain.User();
						groupUser.setUsername(Constant.GROUP_USERNAME);
						groupUser.setNick("群聊");
						groupUser.setHeader("");
						userlist.put(Constant.GROUP_USERNAME, groupUser);

						// 存入内存
						ApplicationControl.getInstance().setContactList(
								userlist);
						// 存入db
						UserDao dao = new UserDao(MainTabActivity.this);
						List<com.easemob.chatuidemo.domain.User> users = new ArrayList<com.easemob.chatuidemo.domain.User>(
								userlist.values());
						dao.saveContactList(users);
						//
						// 获取群聊列表(群聊里只有groupid和groupname等简单信息，不包含members),sdk会把群组存入到内存和db中
						EMGroupManager.getInstance().getGroupsFromServer();
					} catch (EaseMobException e) {
						e.printStackTrace();
					}
					// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
					EMChatManager.getInstance().updateCurrentUserNick(
							ApplicationControl.currentUserNick);
					// todo 访问自己服务器获取最新的好友列表
					mHandler.sendEmptyMessage(115);
				}
			}.start();

		}
	}


	private void initUrl() {
		company_infor_url = Url.COMPANY_function + "?token=" + MainTabActivity.token;
		getFriendListUrl = Url.FRIEND_LIST + "?token=" + MainTabActivity.token;
	}


	private void initSpAndParam() {
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		company_id = sp.getString("userId", "");// 获取商家id
		token = sp.getString("token", "notoken");
	}


	private void initEasemob() {
		// 注册一个接收消息的BroadcastReceiver
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);

		// 注册一个ack回执消息的BroadcastReceiver
		IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager
				.getInstance().getAckMessageBroadcastAction());
		ackMessageIntentFilter.setPriority(3);
		registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

		// 注册一个透传消息的BroadcastReceiver
		IntentFilter cmdMessageIntentFilter = new IntentFilter(EMChatManager
				.getInstance().getCmdMessageBroadcastAction());
		cmdMessageIntentFilter.setPriority(3);
		registerReceiver(cmdMessageReceiver, cmdMessageIntentFilter);
		// setContactListener监听联系人的变化等
		EMContactManager.getInstance().setContactListener(
				new MyContactListener());
		// 注册一个监听连接状态的listener
		EMChatManager.getInstance().addConnectionListener(
				new MyConnectionListener());

		// if (!sp.getBoolean("hasGroupChangeListener", false)) {
		myGroupChangeListener = new MyGroupChangeListener();
		// 注册群聊相关的listener
		EMGroupManager.getInstance().addGroupChangeListener(
				myGroupChangeListener);

		// 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
		EMChat.getInstance().setAppInited();
		// 获取本地所有群组并且设置保存的消息免打扰
		setMessageMainDaRao();
	}
	

	private void initView() {
		
		// 未查看消息记录条数
		unread_msg_tv = (TextView) findViewById(R.id.unread_company_quanzi_number);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		tv4 = (TextView) findViewById(R.id.tv4);
		tv2.setOnClickListener(bNavClickListner);
		tv3.setOnClickListener(bNavClickListner);
		tv4.setOnClickListener(bNavClickListner);

		tv2_boom = (ImageView) findViewById(R.id.tv2_boomt);
		tv3_boom = (ImageView) findViewById(R.id.tv3_boomt);
		tv4_boom = (ImageView) findViewById(R.id.tv4_boomt);

		if (lists.size() > 0) {
			lists.clear();
		}
		lists.add(tv2);
		lists.add(tv3);
		lists.add(tv4);

		if (lists_boom.size() > 0) {
			lists_boom.clear();
		}
		lists_boom.add(tv2_boom);
		lists_boom.add(tv3_boom);
		lists_boom.add(tv4_boom);
	}

	private View.OnClickListener bNavClickListner = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mPager = PAGER.getPager(v.getId());
			selectedFragment(mPager);
		}
	};

	
	// 初始化极光
	private void initJiGuangPush() {
		registerMessageReceiver(); // used for receive msg
		initPush();// 初始化
		ConstantForSaveList.userId = "c" + company_id;// 记录用户名
		JPushInterface.setAlias(getApplicationContext(), "c" + company_id,
				new TagAliasCallback() {
					@Override
					public void gotResult(int code, String arg1,
							Set<String> arg2) {
						switch (code) {
						case 0:
							break;
						case 6002:
							break;
						default:
							break;
						}

					}
				});
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

	// =============极光的推送jpush=======================================
	// 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
	private void initPush() {
		//JPushInterface.init(getApplicationContext());
	}

	/**
	 * 获取消息免打扰群组并设置
	 * 
	 */
	private void setMessageMainDaRao() {
        //屏蔽组
		List<String> blockedListGroup = new ArrayList<>();
		List<EMGroup> groupList = EMGroupManager.getInstance().getAllGroups();
		if (groupList != null) {
			for (EMGroup emGroup : groupList) {
				if (!"".equals(emGroup.getGroupId())) {
					if (sp.getBoolean(
							ConstantForSaveList.userId + emGroup.getGroupId()
									+ PINGBI, false)) {
						blockedListGroup.add(emGroup.getGroupId());
					}
				}
			}
			EMChatManager.getInstance().getChatOptions()
					.setReceiveNotNoifyGroup(blockedListGroup);
		}
	}

	/**
	 * 自定义消息推送
	 */
	private void registerMessageReceiver() {
		jpushMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(jpushMessageReceiver, filter);
	}

	/**
	 * 极光自定义消息推送
	 */

	private class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				Editor edt = sp.edit();
				int target = 0;
				// String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				String activity_id = null;
				String comment_activity_id = null;// 花名册小红点
				String todo = null;// 未处理
				String type = "";// 1:报名极光、2:新的接单
				try {
					JSONObject extrasJson = new JSONObject(extras);
					todo = extrasJson.optString("todo");
					type = extrasJson.optString("type");
					activity_id = extrasJson.optString("activity_id");
					comment_activity_id = extrasJson
							.optString("comment_activity_id");
					// 接单推送
					if (type != null) {
						if ("2".equals(type)) {
							edt.putBoolean(ConstantForSaveList.userId + "type",
									true);
						} else if ("1".equals(type)) {
							edt.putBoolean(ConstantForSaveList.userId + "type",
									false);
						}
					}

					if (null != todo) {
						try {
							target = Integer.parseInt(todo);
							edt.putInt(ConstantForSaveList.userId + "todo",
									target);
						} catch (Exception e) {

						}
					} else {

					}
					if (null != comment_activity_id) {
						try {
							edt.putBoolean(ConstantForSaveList.userId
									+ comment_activity_id, true);
						} catch (Exception e) {

						}
					} else {

					}
					edt.commit();
				} catch (Exception e) {
					return;
				}
				// 若当前页面在前台,马上更新
				update_gongneng_xiaohongdian();
				ManageFragmentCompany.updateTodoAndjieDan();

			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// 默认值为home页
		int resId = intent.getIntExtra("resId", R.id.tv1);
		if (resId == R.id.tv1) {
			// login();
		}
		selectedFragment(PAGER.getPager(resId));
		if (getIntent().getBooleanExtra("conflict", false)
				&& !isConflictDialogShow) {
			showConflictDialog();
		} else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
				&& !isAccountRemovedDialogShow) {
			showAccountRemovedDialog();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
		VolleySington.getInstance().getRequestQueue().cancelAll(TAG);
	}

	@SuppressLint("ResourceAsColor")
	public void updatebNav(int vid) {
		int len = lists.size();
		TextView view = null;
		for (int i = 0; i < len; i++) {
			view = lists.get(i);
			if (view.getId() == vid) {
				view.setCompoundDrawablesWithIntrinsicBounds(0, resIdActive[i],
						0, 0);
				lists.get(i).setTextColor(
						android.graphics.Color.parseColor("#F8943C"));
				lists_boom.get(i).setBackgroundColor(
						android.graphics.Color.parseColor("#F8943C"));
			} else {
				view.setCompoundDrawablesWithIntrinsicBounds(0, resId[i], 0, 0);
				lists.get(i).setTextColor(
						android.graphics.Color.parseColor("#5B5B5B"));
				lists_boom.get(i).setBackgroundColor(
						android.graphics.Color.parseColor("#ffffff"));
			}
		}
	}

    Fragment introduceFragment = null;
	Fragment myFragment = null;

	public void selectedFragment(PAGER pager) {
		Fragment f = null;
		switch (pager) {
		case MESSAGE:
			currentTabIndex = 0;
			f = messageAndAddressFragment = new MessageAndAddressFragment();
			break;
        case PUBLISH:
            currentTabIndex = 1;
            if (introduceFragment == null) {
                introduceFragment = PublishFragment.newInstance(null, null);
            }
            f = introduceFragment;
            break;
		case MINE:
			currentTabIndex = 2;
			if (myFragment == null) {
				myFragment = MyFragment.newInstance(null, null);
			}
			f = myFragment;
			break;

		default:
			break;
		}
		if (!f.isAdded()) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.content, f);
			ft.commit();
			updatebNav(pager.getResId());
		}
	}

	private void exitApp() {
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.setTitle(R.string.backTip);
		dlg.setMessage(getString(R.string.backMessage));
		dlg.setButton(DialogInterface.BUTTON_POSITIVE,
				getString(R.string.yesExit),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						if (FindPJLoginActivity.instance != null) {
							FindPJLoginActivity.instance.finish();
						}
						ConstantForSaveList.usersNick = new ArrayList<HuanxinUser>();
						finish();
					}
				});
		dlg.setButton(DialogInterface.BUTTON_NEGATIVE,
				getString(R.string.noExit),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dlg.cancel();
					}
				});
		dlg.show();

	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	// ====================================环信
	// start==============================

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 注销广播接收者
		try {
			unregisterReceiver(msgReceiver);
		} catch (Exception ignore) {
		}
		try {
			unregisterReceiver(ackMessageReceiver);
		} catch (Exception ignore) {
		}
		try {
			unregisterReceiver(cmdMessageReceiver);
		} catch (Exception ignore) {
		}
		try {
			unregisterReceiver(jpushMessageReceiver);
		} catch (Exception ignore) {

		}
		if (myGroupChangeListener != null) {
			EMGroupManager.getInstance().removeGroupChangeListener(
					myGroupChangeListener);
		}

		if (conflictBuilder != null) {
			conflictBuilder.create().dismiss();
			conflictBuilder = null;
		}
		// 销毁定位
		if (mLocationManagerProxy != null) {
			mLocationManagerProxy.destroy();
		}

	}

	/**
	 * 更新我的小红点信息和管理数目
	 */
	public void update_gongneng_xiaohongdian() {
		// 更新管理数目
		/*if (sp.getInt(ConstantForSaveList.userId + "todo", 0) > 0) {
			unread_guanli_msg_tv.setVisibility(View.VISIBLE);
			if (sp.getInt(ConstantForSaveList.userId + "todo", 0) > 99) {
				unread_guanli_msg_tv.setText("99");
			} else {
				unread_guanli_msg_tv.setText(sp.getInt(
						ConstantForSaveList.userId + "todo", 0) + "");
			}
		} else {
			unread_guanli_msg_tv.setVisibility(View.GONE);
		}*/

	}

	/**
	 * 更新圈子图标的字数 carson
	 */
	public void update_unread_msg() {
		int carson_unread_msg_count = EMChatManager.getInstance()
				.getUnreadMsgsCount();
		if (carson_unread_msg_count > 0) {
			if (carson_unread_msg_count > 99) {
				unread_msg_tv.setText("99");
			} else {
				unread_msg_tv.setText(carson_unread_msg_count + "");
			}
			unread_msg_tv.setVisibility(View.VISIBLE);
		} else {
			unread_msg_tv.setVisibility(View.GONE);
		}
	}

	/**
	 * 新消息广播接收者
	 * 
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看
			String from = intent.getStringExtra("from");
			// 消息id
			String msgId = intent.getStringExtra("msgid");
			EMMessage message = EMChatManager.getInstance().getMessage(msgId);
			// fix: logout crash， 如果正在接收大量消息
			// 因为此时已经logout，消息队列已经被清空， broadcast延时收到，所以会出现message为空的情况
			if (message == null) {
				return;
			}
			// 2014-10-22 修复在某些机器上，在聊天页面对方发消息过来时不立即显示内容的bug
			if (ChatActivity.activityInstance != null) {
				if (message.getChatType() == ChatType.GroupChat) {
					if (message.getTo().equals(
							ChatActivity.activityInstance.getToChatUsername()))
						return;
				} else {
					if (from.equals(ChatActivity.activityInstance
							.getToChatUsername()))
						return;
				}
			}

			// 注销广播接收者，否则在ChatActivity中会收到这个广播
			abortBroadcast();
			// message.getTo表示消息来自对象(单聊是uid,群聊是群组id)
			// 解决消息免打扰时还是弹出通知栏的bug
			if (!sp.getBoolean(ConstantForSaveList.userId + message.getTo()
					+ "pingbi", false)) {
				// 显示群聊通知
				notifyNewMessage(message);
			}
			update_unread_msg();
			if (currentTabIndex == 1) {
				// 当前页面如果为聊天历史页面，刷新此页面
				if (messageAndAddressFragment != null) {
					messageAndAddressFragment.message.refresh();
				}
			}

		}
	}

	/**
	 * 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下 如果不需要，注释掉即可
	 * 
	 * @param message
	 */
	protected void notifyNewMessage(EMMessage message) {
		// 如果是设置了不提醒只显示数目的群组(这个是app里保存这个数据的，demo里不做判断)
		// 以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广播)
		if (!EasyUtils.isAppRunningForeground(this)) {
			return;
		}
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(getApplicationInfo().icon)
				.setWhen(System.currentTimeMillis()).setAutoCancel(true);

		String ticker = CommonUtils.getMessageDigest(message, this);
		if (message.getType() == Type.TXT)
			ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
		// 设置状态栏提示
		// mBuilder.setTicker(message.getFrom()+": " + ticker);
		mBuilder.setTicker(ticker);
		// 必须设置pendingintent，否则在2.3的机器上会有bug
		Intent intent = new Intent(this, MainTabActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, notifiId,
				intent, PendingIntent.FLAG_ONE_SHOT);
		mBuilder.setContentIntent(pendingIntent);

		Notification notification = mBuilder.build();
		notificationManager.notify(notifiId, notification);
		notificationManager.cancel(notifiId);
	}

	/**
	 * 消息回执BroadcastReceiver
	 */
	private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			abortBroadcast();

			String msgid = intent.getStringExtra("msgid");
			String from = intent.getStringExtra("from");

			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(from);
			if (conversation != null) {
				// 把message设为已读
				EMMessage msg = conversation.getMessage(msgid);

				if (msg != null) {

					// 2014-11-5 修复在某些机器上，在聊天页面对方发送已读回执时不立即显示已读的bug
					if (ChatActivity.activityInstance != null) {
						if (msg.getChatType() == ChatType.Chat) {
							if (from.equals(ChatActivity.activityInstance
									.getToChatUsername()))
								return;
						}
					}

					msg.isAcked = true;
				}
			}

		}
	};

	/**
	 * 透传消息BroadcastReceiver
	 */
	private BroadcastReceiver cmdMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			abortBroadcast();
			EMLog.d(TAG, "收到透传消息");
			// 获取cmd message对象
			// String msgId = intent.getStringExtra("msgid");
			EMMessage message = intent.getParcelableExtra("message");
			// 获取消息body
			CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
			String action = cmdMsgBody.action;// 获取自定义action

			// 获取扩展属性 此处省略
			// message.getStringAttribute("");
			EMLog.d(TAG,
					String.format("透传消息：action:%s,message:%s", action,
							message.toString()));
			Toast.makeText(MainTabActivity.this, "收到透传：action：" + action,
					Toast.LENGTH_SHORT).show();
		}
	};

	/**
	 * 离线消息BroadcastReceiver sdk 登录后，服务器会推送离线消息到client，这个receiver，是通知UI
	 * 有哪些人发来了离线消息 UI 可以做相应的操作，比如下载用户信息
	 */
	// private BroadcastReceiver offlineMessageReceiver = new
	// BroadcastReceiver() {
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// String[] users = intent.getStringArrayExtra("fromuser");
	// String[] groups = intent.getStringArrayExtra("fromgroup");
	// if (users != null) {
	// for (String user : users) {
	// System.out.println("收到user离线消息：" + user);
	// }
	// }
	// if (groups != null) {
	// for (String group : groups) {
	// System.out.println("收到group离线消息：" + group);
	// }
	// }
	// }
	// };

	private InviteMessgeDao inviteMessgeDao;
	private UserDao userDao;

	/***
	 * 好友变化listener
	 * 
	 */
	private class MyContactListener implements EMContactListener {

		@Override
		public void onContactAdded(List<String> usernameList) {
			// 保存增加的联系人
			Map<String, User> localUsers = ApplicationControl.getInstance()
					.getContactList();
			Map<String, User> toAddUsers = new HashMap<String, User>();
			for (String username : usernameList) {
				User user = setUserHead(username);
				// 添加好友时可能会回调added方法两次
				if (!localUsers.containsKey(username)) {
					userDao.saveContact(user);
				}
				toAddUsers.put(username, user);
			}
			localUsers.putAll(toAddUsers);
			// 刷新ui
			// if (currentTabIndex == 1)
			// contactListFragment.refresh();

		}

		@Override
		public void onContactDeleted(final List<String> usernameList) {
			// 被删除
			Map<String, User> localUsers = ApplicationControl.getInstance()
					.getContactList();
			for (String username : usernameList) {
				localUsers.remove(username);
				userDao.deleteContact(username);
				inviteMessgeDao.deleteMessage(username);
			}
			runOnUiThread(new Runnable() {
				public void run() {
					// 如果正在与此用户的聊天页面
					if (ChatActivity.activityInstance != null
							&& usernameList
									.contains(ChatActivity.activityInstance
											.getToChatUsername())) {
						Toast mToast = Toast.makeText(
								MainTabActivity.this,
								ChatActivity.activityInstance
										.getToChatUsername() + "已把你从他好友列表里移除",
								Toast.LENGTH_SHORT);
						mToast.setGravity(Gravity.CENTER, 0, 0);
						mToast.show();

						ChatActivity.activityInstance.finish();

					}
					update_unread_msg();// 刷新圈子
					if (currentTabIndex == 1)
						messageAndAddressFragment.message.refresh();
				}
			});

		}

		@Override
		public void onContactInvited(String username, String reason) {
			// 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getGroupId() == null
						&& inviteMessage.getFrom().equals(username)) {
					inviteMessgeDao.deleteMessage(username);
				}
			}

			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			msg.setReason(reason);
			// 设置相应status
			msg.setStatus(InviteMesageStatus.BEINVITEED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactAgreed(String username) {
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			msg.setStatus(InviteMesageStatus.BEAGREED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactRefused(String username) {
			// 参考同意，被邀请实现此功能,未实现
		}

	}

	/**
	 * 保存提示新消息
	 * 
	 * @param msg
	 */
	private void notifyNewIviteMessage(InviteMessage msg) {
		saveInviteMsg(msg);
		// 提示有新消息
		EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

		// 刷新bottom bar消息未读数
		// updateUnreadAddressLable();
		// 刷新好友页面ui
		// if (currentTabIndex == 1)
		// contactListFragment.refresh();
	}

	/**
	 * 保存邀请等msg
	 * 
	 * @param msg
	 */
	private void saveInviteMsg(InviteMessage msg) {
		// 保存msg
		inviteMessgeDao.saveMessage(msg);
		// 未读数加1
		User user = ApplicationControl.getInstance().getContactList()
				.get(Constant.NEW_FRIENDS_USERNAME);
		if (user.getUnreadMsgCount() == 0)
			user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
	}

	/**
	 * set head
	 * 
	 * @param username String
	 * @return User
	 */
	User setUserHead(String username) {
		User user = new User();
		user.setUsername(username);
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
		return user;
	}

	/**
	 * 连接监听listener
	 * 
	 */
	private class MyConnectionListener implements EMConnectionListener {

		@Override
		public void onConnected() {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
				}

			});
		}

		@Override
		public void onDisconnected(final int error) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (error == EMError.USER_REMOVED) {
						// 显示帐号已经被移除
						showAccountRemovedDialog();
					} else if (error == EMError.CONNECTION_CONFLICT) {
						// 显示帐号在其他设备登陆dialog
						showConflictDialog();
					} else {
						// chatHistoryFragment.errorItem.setVisibility(View.VISIBLE);
						if (NetUtils.hasNetwork(MainTabActivity.this)) {
							if (isConflict) {
								// 账号在别处登陆时 重新打开app需要重新登陆
								// Toast mToast = Toast.makeText(
								// MainTabActivity.this, "请重新登陆", 1);
								// mToast.setGravity(Gravity.CENTER, 0, 0);
								// mToast.show();
								// Editor edit = sp.edit();
								// edit.putString("userId", "");
								// edit.putString("token", "");
								// edit.commit();
								// finish();

							}
						} else {
							Toast mToast = Toast.makeText(
									MainTabActivity.this,
									"当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT);
							mToast.setGravity(Gravity.CENTER, 0, 0);
							mToast.show();
						}
					}
				}

			});
		}
	}

	/**
	 * MyGroupChangeListener
	 */
	private class MyGroupChangeListener implements GroupChangeListener {

		@Override
		public void onInvitationReceived(String groupId, String groupName,
				String inviter, String reason) {
			boolean hasGroup = false;
			for (EMGroup group : EMGroupManager.getInstance().getAllGroups()) {
				if (group.getGroupId().equals(groupId)) {
					hasGroup = true;
					break;
				}
			}
			if (!hasGroup)
				return;
			// 防止多次接收到邀请信息
			// List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			// for (InviteMessage inviteMessage : msgs) {
			// if (inviteMessage.getFrom().equals(inviter)) {
			// return;
			// }
			// }

			// 被邀请
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(inviter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(inviter + "邀请你加入了群聊"));
			// 保存邀请消息
			EMChatManager.getInstance().saveMessage(msg);
			// 提醒新消息
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

			runOnUiThread(new Runnable() {
				public void run() {
					// updateUnreadLabel();
					// 刷新ui
					// 有新群建立时
					if (currentTabIndex == 1) {
						messageAndAddressFragment.message.refresh();
					}
					if (CommonUtils.getTopActivity(MainTabActivity.this)
							.equals(GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}
			});

		}

		@Override
		public void onInvitationAccpted(String groupId, String inviter,
				String reason) {

		}

		@Override
		public void onInvitationDeclined(String groupId, String invitee,
				String reason) {

		}

		@Override
		public void onUserRemoved(String groupId, String groupName) {
			// 提示用户被T了，demo省略此步骤
			// 刷新ui
			runOnUiThread(new Runnable() {
				public void run() {
					try {
						// updateUnreadLabel();
						if (currentTabIndex == 1)
							messageAndAddressFragment.message.refresh();
						if (CommonUtils
								.getTopActivity(MainTabActivity.this)
								.equals(GroupsActivity.class.getName())) {
							GroupsActivity.instance.onResume();
						}
					} catch (Exception e) {
						EMLog.e(TAG, "refresh exception " + e.getMessage());
					}

				}
			});
		}

		@Override
		public void onGroupDestroy(String groupId, String groupName) {
			// 群被解散
			// 提示用户群被解散,demo省略
			// 刷新ui
			runOnUiThread(new Runnable() {
				public void run() {
					// updateUnreadLabel();
					if (currentTabIndex == 1)
						messageAndAddressFragment.message.refresh();
					if (CommonUtils.getTopActivity(MainTabActivity.this)
							.equals(GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}
			});

		}

		@Override
		public void onApplicationReceived(String groupId, String groupName,
				String applyer, String reason) {

			// 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
			// List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			// for (InviteMessage inviteMessage : msgs) {
			// if (inviteMessage.getGroupId() != null
			// && inviteMessage.getFrom().equals(applyer)) {
			// inviteMessgeDao.deleteMessage(applyer);
			// }
			// }

			// 用户申请加入群聊
			InviteMessage msg = new InviteMessage();
			msg.setFrom(applyer);
			msg.setTime(System.currentTimeMillis());
			msg.setGroupId(groupId);
			msg.setGroupName(groupName);
			msg.setReason(reason);
			msg.setStatus(InviteMesageStatus.BEAPPLYED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onApplicationAccept(String groupId, String groupName,
				String accepter) {
			// List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			// for (InviteMessage inviteMessage : msgs) {
			// if (inviteMessage.getFrom().equals(accepter)) {
			// return;
			// }
			// }
			// 加群申请被同意
			// ********************申请入群有时候会提示几条同意群聊申请的消息,先屏蔽提示消息*************************************
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(accepter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody("群主同意了你的群聊申请"));
			// 保存同意消息
			EMChatManager.getInstance().saveMessage(msg);
			// 提醒新消息
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

			runOnUiThread(new Runnable() {
				public void run() {
					// updateUnreadLabel();
					// 刷新ui
					if (currentTabIndex == 1) {
						messageAndAddressFragment.message.refresh();
					}
					if (CommonUtils.getTopActivity(MainTabActivity.this)
							.equals(GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}
			});

		}

		@Override
		public void onApplicationDeclined(String groupId, String groupName,
				String decliner, String reason) {
			// 加群申请被拒绝未实现
		}

	}

	/**
	 * 获取商家信息
	 * 
	 */
	private void initMy() {
		showWait(true);
		StringRequest stringRequest = new StringRequest(Method.POST,
				company_infor_url, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							function = (Function) JsonUtil.jsonToBean(js,
									Function.class);
							update_gongneng_xiaohongdian();// 隐藏或显示小红点
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						showWait(false);
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> map = new HashMap<>();
				map.put("company_id", company_id);
				return map;
			}
		};
		queue.add(stringRequest);
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
	}

	@Override
	protected void onResume() {
		isForeground = true;
		super.onResume();
		if (!isConflict || !isCurrentAccountRemoved) {
			EMChatManager.getInstance().activityResumed();
		}
		MobclickAgent.onPause(this);// 友盟
		update_unread_msg();// 刷新圈子
		initMy();// 刷新功能小红点
	}

	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();
		MobclickAgent.onPause(this);// 友盟
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isConflict", isConflict);
		outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
			// moveTaskToBack(false);
			exitApp();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			// 监控/拦截菜单键
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {
			// 由于Home键为系统键，此处不能捕获，需要重写onAttachedToWindow()
		}
		return super.onKeyDown(keyCode, event);
	}

	private android.app.AlertDialog.Builder conflictBuilder;
	private android.app.AlertDialog.Builder accountRemovedBuilder;
	private boolean isConflictDialogShow;
	private boolean isAccountRemovedDialogShow;

	/**
	 * 显示帐号在别处登录dialog
	 */
	private void showConflictDialog() {
		isConflictDialogShow = true;
		ApplicationControl.getInstance().logout(null);

		if (!MainTabActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (conflictBuilder == null)
					conflictBuilder = new android.app.AlertDialog.Builder(
							MainTabActivity.this);
				conflictBuilder.setTitle("下线通知");
				conflictBuilder.setMessage(R.string.connect_conflict);
				conflictBuilder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								conflictBuilder = null;

								Editor edit = sp.edit();
								edit.putString("userId", "");
								edit.putString("token", "");
								edit.commit();
								finish();
								// startActivity(new
								// Intent(MainTabActivity.this,
								// EnterActivity.class));
							}
						});
				conflictBuilder.setCancelable(false);
				conflictBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
				EMLog.e(TAG,
						"---------color conflictBuilder error" + e.getMessage());
			}

		}

	}

	/**
	 * 帐号被移除的dialog
	 */
	private void showAccountRemovedDialog() {
		isAccountRemovedDialogShow = true;
		ApplicationControl.getInstance().logout(null);

		if (!MainTabActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (accountRemovedBuilder == null)
					accountRemovedBuilder = new android.app.AlertDialog.Builder(
							MainTabActivity.this);
				accountRemovedBuilder.setTitle("移除通知");
				accountRemovedBuilder.setMessage(R.string.em_user_remove);
				accountRemovedBuilder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								accountRemovedBuilder = null;

								Editor edit = sp.edit();
								edit.putString("userId", "");
								edit.putString("token", "");
								edit.commit();
								finish();
								// startActivity(new
								// Intent(MainTabActivity.this,
								// EnterActivity.class));
							}
						});
				accountRemovedBuilder.setCancelable(false);
				accountRemovedBuilder.create().show();
				isCurrentAccountRemoved = true;
			} catch (Exception e) {
				EMLog.e(TAG,
						"---------color userRemovedBuilder error"
								+ e.getMessage());
			}

		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

	// ===========环信end================

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		String c_company_id = sp.getString("userId", "");
		if (!"".equals(c_company_id.trim())) {
			if (NetWorkCheck.isOpenNetwork(MainTabActivity.this)) {
				checkForbidden(c_company_id);
			}
		}

	}

	/**
	 * 检测商家是否被拉黑
	 */
	public void checkForbidden(final String c_company_id) {
		StringRequest request = new StringRequest(Request.Method.POST,
				Url.COMPANY_FORBIDDEN + "?token=" + MainTabActivity.token,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject js = new JSONObject(response);
							int zt = js.getInt("zt");
							if (zt == 2) {
								// zt 2表示商家已经被拉黑了
								Intent intent = new Intent();
								Editor edit = sp.edit();
								edit.putString("userId", "");
								edit.putString("token", "");
								edit.commit();
								intent.setClass(MainTabActivity.this,
										LaheiPageActivity.class);
								startActivity(intent);
								finish();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("company_id", c_company_id);
				return map;
			}
		};
		queue.add(request);
		request.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	/**
	 * 等待框
	 */
	protected void showWait(boolean isShow) {
		if (isShow) {
			if (null == dialog) {
				dialog = new WaitDialog(this);
			}
			dialog.show();
		} else {
			if (null != dialog) {
				dialog.dismiss();
			}
		}
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
				boolean flag = sp.getBoolean("firstdingwei", true);
				String curCity = amapLocation.getCity();
				if (curCity.endsWith("市")) {
					curCity = curCity.substring(0, curCity.length() - 1);
				}
				Editor edt_city = sp.edit();
				edt_city.putBoolean("firstdingwei", false);
				edt_city.putString("dingweicity", curCity);
				edt_city.commit();
				final String thisCity = curCity;
				if (flag) {
					// 弹出第一次定位的城市弹出框
					showAlertDialog(curCity, "温馨提示");
				} else {
					if (!sp.getString("city", "深圳").equals(thisCity)) {
						showAlertDialog2("您当前定位城市:" + curCity, "定位城市有改变",
								thisCity);
					} else {
						// 如果从没上传过个人位置
						if (!"".equals(company_id)) {
							// 切换到指定城市,访问后台传输城市
							StringRequest request = new StringRequest(
									Request.Method.POST, Url.CHANGE_CITY_CUSTOM
											+ "?token="
											+ MainTabActivity.token,
									new Response.Listener<String>() {
										@Override
										public void onResponse(String response) {

										}
									}, new Response.ErrorListener() {
										@Override
										public void onErrorResponse(
												VolleyError volleyError) {
										}
									}) {
								@Override
								protected Map<String, String> getParams()
										throws AuthFailureError {
									Map<String, String> map = new HashMap<String, String>();
									map.put("company_id", company_id);
									map.put("city", thisCity);
									return map;
								}
							};
							queue.add(request);
							request.setRetryPolicy(new DefaultRetryPolicy(
									ConstantForSaveList.DEFAULTRETRYTIME * 1000,
									1, 1.0f));

						}
					}
				}
			} else {
			}
		}
	}

	/**
	 * 第一次弹出城市定位框
	 */
	public void showAlertDialog(final String str, final String str2) {

		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage("当前定位城市:" + str);
		builder.setTitle(str2);

		builder.setPositiveButton("我知道了",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Editor edt = sp.edit();
						edt.putString("city", str);
						edt.commit();
						Intent intent = new Intent(); // Itent就是我们要发送的内容
						intent.setAction("com.carson.company.changgecity"); // 设置你这个广播的action
						intent.putExtra("changgecity", str);
						sendBroadcast(intent); // 发送广播
					}
				});

		builder.create().show();
	}

	/**
	 * 弹出城市改变弹出框 str3:city
	 */
	public void showAlertDialog2(String str, final String str2,
			final String str3) {
		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(str);
		builder.setTitle(str2);
		builder.setPositiveButton("现在切换",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Editor edt = sp.edit();
						edt.putString("city", str3);
						edt.commit();
						Intent intent = new Intent(); // Itent就是我们要发送的内容
						intent.setAction("com.carson.company.changgecity"); // 设置你这个广播的action
						intent.putExtra("changgecity", str3);
						sendBroadcast(intent); // 发送广播
						// 切换到指定城市,访问后台传输城市
						StringRequest request = new StringRequest(
								Request.Method.POST,
								Url.CHANGE_CITY_CUSTOM + "?token="
										+ MainTabActivity.token,
								new Response.Listener<String>() {
									@Override
									public void onResponse(String response) {
									}
								}, new Response.ErrorListener() {
									@Override
									public void onErrorResponse(
											VolleyError volleyError) {
									}
								}) {
							@Override
							protected Map<String, String> getParams()
									throws AuthFailureError {
								Map<String, String> map = new HashMap<String, String>();
								map.put("company_id", company_id);
								map.put("city", str3);
								return map;
							}
						};
						queue.add(request);
						request.setRetryPolicy(new DefaultRetryPolicy(
								ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1,
								1.0f));

					}
				});
		builder.setNegativeButton("暂不切换",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
					}
				});

		builder.create().show();
	}

	// ************************弹出提示更新的pop框***********************************
	/**
	 * 弹出更新日志
	 */
	private void updateLog() {
		final String updateUrl = Url.IS_UPDATE_APK;// 检测apk更新接口
		if (NetWorkCheck.isOpenNetwork(MainTabActivity.this)) {
			new Thread() {
				public void run() {
					currentVerCode = getAPKVersion();// 获取当前版本
					String result = new UpdateUtils().getJsonByPhp(updateUrl);
					if (!"connect_fail".equals(result)) {
						jsonjiexi(result);
						mHandler.sendEmptyMessage(1);
					}
				}
			}.start();
		}

	}


	/**
	 * DOWNLOAD APK FILE BY URL
	 * 
	 * @param url String
	 */
	private void downloadApkFile(final String url) {
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				// 请求超时
				client.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
				// 读取超时
				client.getParams().setParameter(
						CoreConnectionPNames.SO_TIMEOUT, 5000);
				// params[0]代表连接的
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					pd.setMax((int) length);
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(Tools.getSdPath()
								+ "/JianZhiDaren.apk");
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							if (downFlag) {
								if (fileOutputStream != null) {
									fileOutputStream.close();
								}
								if (file.exists())
									file.delete();
								return;
							}
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							pd.setProgress(count);
							if (length > 0) {
							}
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();// 下载完了
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * 初始化下载进度框
	 */
	@SuppressWarnings("deprecation")
	private void initPd() {
		// 初始化下载的进度框
		pd = new ProgressDialog(MainTabActivity.this);
		pd.setMessage("请稍后...");
		pd.setTitle("正在下载兼职达人");
		pd.setCancelable(false);
		pd.setIndeterminate(false);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// 设置一个取消的按钮取消下载
		pd.setButton("取消下载", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				downFlag = true;
				dialog.cancel();
				if (isForce != null && "1".equals(isForce)) {
					if (FindPJLoginActivity.instance != null) {
						FindPJLoginActivity.instance.finish();
					}
					finish();
				}
			}
		});
		pd.show();

	}

	private void down() {
		mHandler.post(new Runnable() {
			public void run() {
				pd.cancel();
				installApk();
			}
		});
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (currentVerCode < server_vercode) {
					if (isAlert != null && "1".equals(isAlert)) {
						downFlag = false;// 重置下载标志
						new AlertDialog.Builder(MainTabActivity.this)
								.setTitle("检测到金牌助理有更新了")
								.setMessage(update_contentStr)
								.setPositiveButton("马上更新",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												initPd();
												downloadApkFile(server_apk_downloadUrl);

											}
										})
								.setCancelable(false)
								.setNegativeButton("暂不更新",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
                                                if(BuildConfig.DEBUG){
                                                    isForce = "2";
                                                }
												if (isForce != null
														&& "1".equals(isForce)) {
													if (EnterActivity.instance != null) {
														EnterActivity.instance
																.finish();
													}
													if (FindPJLoginActivity.instance != null) {
														FindPJLoginActivity.instance
																.finish();
													}
													finish();
												}

											}
										}).create().show();
					}
				}
				break;
			case 115:
				// 访问自己服务器获取最新的好友列表信息写入内存和db中
				getFriendListFromServer();
				break;
			default:
				break;
			}

		}

	};

	/**
	 * 重新加载本地db和内存中的好友列表 同步服务器最新数据
	 */
	private void updateFriendFromServer(final List<String> serverList) {
		new Thread() {
			public void run() {
				try {
					Map<String, com.easemob.chatuidemo.domain.User> userlist = new HashMap<String, com.easemob.chatuidemo.domain.User>();
					for (String username : serverList) {
						com.easemob.chatuidemo.domain.User user = new com.easemob.chatuidemo.domain.User();
						user.setUsername(username);
						setUserHearder(username, user);
						userlist.put(username, user);
					}
					// 添加user"申请与通知"
					com.easemob.chatuidemo.domain.User newFriends = new com.easemob.chatuidemo.domain.User();
					newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
					newFriends.setNick("申请与通知");
					newFriends.setHeader("");
					userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
					// 添加"群聊"
					com.easemob.chatuidemo.domain.User groupUser = new com.easemob.chatuidemo.domain.User();
					groupUser.setUsername(Constant.GROUP_USERNAME);
					groupUser.setNick("群聊");
					groupUser.setHeader("");
					userlist.put(Constant.GROUP_USERNAME, groupUser);

					// 存入内存
					ApplicationControl.getInstance().setContactList(userlist);
					// 存入db
					UserDao dao = new UserDao(MainTabActivity.this);
					List<com.easemob.chatuidemo.domain.User> users = new ArrayList<com.easemob.chatuidemo.domain.User>(
							userlist.values());
					dao.saveContactList(users);
					//
					// 获取群聊列表(群聊里只有groupid和groupname等简单信息，不包含members),sdk会把群组存入到内存和db中
					EMGroupManager.getInstance().getGroupsFromServer();
				} catch (EaseMobException e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	/**
	 * 获取服务器端好友列表
	 * 
	 */
	private void getFriendListFromServer() {
		StringRequest stringRequest = new StringRequest(Method.POST,
				getFriendListUrl, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject js = new JSONObject(response);
							JSONObject jss = js.getJSONObject("responseStatus");
							String status = jss.getString("status");
							if ("1".equals(status)) {
								JSONArray friendListArray = jss
										.getJSONArray("list");
								if (friendListArray != null
										&& friendListArray.length() > 0) {
									List<String> serverFriendList = new ArrayList<String>();
									for (int i = 0; i < friendListArray
											.length(); i++) {
										serverFriendList.add(friendListArray
												.getString(i));
									}
									if (usernames != null) {
										if (serverFriendList.size() > usernames
												.size()) {
											updateFriendFromServer(serverFriendList);
										}
									}
								}
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", "c" + company_id);
				return map;
			}
		};
		queue.add(stringRequest);
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	/**
	 * 得到本地应用的版本信息
	 * 
	 * @return
	 */
	private int getAPKVersion() {
		// APK版本判断
		int sdcardVersion = 1;
		PackageManager pm = this.getPackageManager();
		PackageInfo info;
		try {
			info = pm.getPackageInfo(this.getPackageName(), 0);
			if (info != null) {
				sdcardVersion = info.versionCode; // 得到版本信息
			}
		} catch (NameNotFoundException e) {
			return sdcardVersion;

		}
		return sdcardVersion;

	}

	/**
	 * json解析
	 */
	private void jsonjiexi(String jsonStr) {
		try {
			JSONObject js = new JSONObject(jsonStr);
			JSONObject jss = js.getJSONObject("responseStatus");
			String status = jss.getString("status");
			if (status != null && "1".equals(status)) {
				// 服务端获取的都是string类型,需要转换
				JSONObject appInfo = jss.getJSONObject("apkInfo");
				// String server_code_str = appInfo.getString("version");
				// server_vercode = Integer.parseInt(server_code_str);
				// update_contentStr = appInfo.getString("update_msg");
				// isForce = appInfo.getString("is_force");// 是否强制更新1是强制更新
				// isAlert = appInfo.getString("is_alert");// 是否弹出更新框
				// server_apk_downloadUrl = appInfo.getString("update_url");
				String server_code_str = appInfo.getString("agent_version");
				server_vercode = Integer.parseInt(server_code_str);
				update_contentStr = appInfo.getString("agent_update_msg");
				isForce = appInfo.getString("agent_is_force");// 是否强制更新1是强制更新
				isAlert = appInfo.getString("agent_is_alert");// 是否弹出更新框
				server_apk_downloadUrl = appInfo.getString("agent_update_url");
			}

		} catch (Exception e1) {
			server_vercode = 1;
			e1.printStackTrace();
		}
	}

	/**
	 * 将下载的apk安装
	 */
	private void installApk() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(
				Uri.fromFile(new File(Tools.getSdPath() + "/JianZhiDaren.apk")),
				"application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	

	public enum PAGER {
		/*HOME(R.id.tv1),*/ MESSAGE(R.id.tv2), PUBLISH(R.id.tv3), MINE(R.id.tv4);

		private PAGER(int resId) {
			this.resId = resId;
		}

		private int resId;

		public int getResId() {
			return resId;
		}

		public static PAGER getPager(int resId) {
			/*if (resId == HOME.getResId()) {
				return HOME;
			} else */if (resId == MESSAGE.getResId()) {
				return MESSAGE;
			} else if (resId == PUBLISH.getResId()) {
				return PUBLISH;
			} else if (resId == MINE.getResId()) {
				return MINE;
			}
			throw new RuntimeException("not has redId (" + resId + ") pager.");
		}
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

	private void createNoMediaFile() {
		File mePhotoFold = new File(Environment.getExternalStorageDirectory()
				+ "/" + "jzdr/" + "image");
		if (!mePhotoFold.exists()) {
			mePhotoFold.mkdirs();
		}
		File f = new File(Environment.getExternalStorageDirectory() + "/"
				+ "jzdr/" + "image/" + ".nomedia");
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
