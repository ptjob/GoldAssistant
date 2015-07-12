package com.quark.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carson.constant.ConstantForSaveList;
import com.easemob.EMCallBack;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.jianzhidaren.ApplicationControl;
import com.quark.jianzhidaren.FindPJLoginActivity;
import com.quark.jianzhidaren.MainCompanyActivity;
import com.quark.jianzhidaren.ModifyPwdActivity;
import com.quark.model.HuanxinUser;
import com.quark.setting.EditPhoneActivity;
import com.quark.setting.SuggestActivity;
import com.quark.ui.widget.CommonWidget;
import com.quark.ui.widget.CustomDialog;
import com.quark.us.FeiJichiActivity;
import com.quark.utils.WaitDialog;
import com.quark.volley.VolleySington;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * 设置 商家 用户公用
 * 
 * @author howe
 * 
 */
public class MyFragment extends Fragment implements OnClickListener {

	private String user_id;
	private String miandarao_url;// 设置商家免打扰
	private String get_miandarao_status_url;// 获取商家免打扰状态
	private int moneyPool, tip;// 活动提醒
	private int miandarao;// 商家免打扰
	protected WaitDialog dialog;
	protected RequestQueue queue = VolleySington.getInstance()
			.getRequestQueue();
	private View view;
	SharedPreferences sp;

	public static MyFragment newInstance(String param1, String param2) {
		MyFragment fragment = new MyFragment();

		return fragment;
	}

	public MyFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	// 检查更新
	@ViewInject(R.id.checkUpdate)
	private LinearLayout checkUpdate;
	// 商家隐藏
	@ViewInject(R.id.company_hit1)
	private LinearLayout company_hit1;
	@ViewInject(R.id.company_hit2)
	private Button company_hit2;
	@ViewInject(R.id.company_hit3)
	private Button company_hit3;
	@ViewInject(R.id.company_hit4)
	private Button company_hit4;
	@ViewInject(R.id.kaiguan_on)
	private ImageView kaiguan_on;
	@ViewInject(R.id.kaiguan_off)
	private ImageView kaiguan_off;
	@ViewInject(R.id.update_info)
	private TextView update_info;
	// 商家免打扰(仅接收好友消息)
	@ViewInject(R.id.checkMianDaRao)
	private LinearLayout checkMianDaRao;
	@ViewInject(R.id.miandarao_kaiguan_on)
	private ImageView miandarao_kaiguan_on;
	@ViewInject(R.id.miandarao_kaiguan_off)
	private ImageView miandarao_kaiguan_off;
	@ViewInject(R.id.miandarao_butn)
	private Button miandarao_butn;// 分割线

	// carson添加点击监听
	private LinearLayout check_lineLayout, editTelephone_linelayout,
			editPassword_linelayout, jianyi_linelayout, loginOut_linelayout;

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Editor edt = sp.edit();
		edt.putInt("tip", tip);
		edt.putInt("miandarao", miandarao);
		edt.commit();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_my, container, false);
		ViewUtils.inject(this, view);
		// carson初始化控件
		check_lineLayout = (LinearLayout) view.findViewById(R.id.checkUpdate);
		editTelephone_linelayout = (LinearLayout) view
				.findViewById(R.id.editTelephone);
		editPassword_linelayout = (LinearLayout) view
				.findViewById(R.id.editPassword);
		jianyi_linelayout = (LinearLayout) view.findViewById(R.id.jianyi);
		loginOut_linelayout = (LinearLayout) view.findViewById(R.id.loginOut);
		check_lineLayout.setOnClickListener(this);
		editTelephone_linelayout.setOnClickListener(this);
		editPassword_linelayout.setOnClickListener(this);
		jianyi_linelayout.setOnClickListener(this);
		loginOut_linelayout.setOnClickListener(this);

		// 免打扰url
		miandarao_url = Url.COMPANY_MIANDARAO + "?token="
				+ MainCompanyActivity.token;
		// 获取免打扰url
		get_miandarao_status_url = Url.COMPANY_MIANDARAO_STATUS + "?token="
				+ MainCompanyActivity.token;

		sp = getActivity().getSharedPreferences("jrdr.setting",
				android.content.Context.MODE_PRIVATE);
		user_id = sp.getString("userId", "");
		// *********预加载************
		// 设置奖金池
		moneyPool = sp.getInt("pool_money", 0);
		miandarao = sp.getInt("miandarao", 0);
		tip = sp.getInt("tip", 0);
		TextView text = (TextView) view.findViewById(R.id.moneyPool);
		text.setText(moneyPool + "");
		// 商家端时隐藏
		getMiandarao();
		company_hit1.setVisibility(View.GONE);
		company_hit2.setVisibility(View.GONE);
		company_hit3.setVisibility(View.GONE);
		company_hit4.setVisibility(View.GONE);
		init_miandarao();// 获取免打扰状态
		miandarao_kaiguan_on.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 开启免打扰时,点击监听
				miandarao_kaiguan_off.setVisibility(View.VISIBLE);
				miandarao_kaiguan_on.setVisibility(View.GONE);
				miandarao = 0;// 表示关闭了商家免打扰,任何人都能与商家聊天
				miandarao_switch_btn();// 设置商家免打扰
			}
		});
		miandarao_kaiguan_off.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 免打扰关闭时,点击监听
				miandarao_kaiguan_off.setVisibility(View.GONE);
				miandarao_kaiguan_on.setVisibility(View.VISIBLE);
				miandarao = 1;// 表示开启了商家免打扰,只允许好友与商家聊天
				miandarao_switch_btn();
			}
		});

		// 飞机轮播池
		LinearLayout feijichi = (LinearLayout) view.findViewById(R.id.feijichi);
		feijichi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), FeiJichiActivity.class);
				startActivity(intent);
			}
		});

		ImageView infoOperatingIV = (ImageView) view
				.findViewById(R.id.infoOperating);
		Animation operatingAnim = AnimationUtils.loadAnimation(getActivity(),
				R.anim.roraterepeat);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		if (operatingAnim != null) {
			infoOperatingIV.startAnimation(operatingAnim);
		}
		// 免打扰url
		miandarao_url = Url.COMPANY_MIANDARAO + "?token="
				+ MainCompanyActivity.token;
		return view;
	}

	/**
	 * sava info
	 */
	private void saveInfor(int moneypool) {
		Editor pool_edt = sp.edit();
		pool_edt.putInt("pool_money", moneypool);
		pool_edt.commit();
	}

	// 默认消息免打扰
	private void init_miandarao() {
		// showWait(true);
		StringRequest stringRequest = new StringRequest(Method.POST,
				get_miandarao_status_url, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// showWait(false);
						try {
							JSONObject js = new JSONObject(response);
							miandarao = js.getInt("disturb");
							// 处理money返回null的情况
							try {
								moneyPool = js.getInt("money");
							} catch (JSONException e) {
								moneyPool = 0;
							}
							getMoneyPool();
							getMiandarao();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// showWait(false);
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> map = new HashMap<String, String>();
				map.put("company_id", user_id);
				return map;
			}
		};
		queue.add(stringRequest);
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));
	}

	/**
	 * 预加载免打扰开关
	 * 
	 */
	private void getMiandarao() {
		miandarao_kaiguan_on = (ImageView) view
				.findViewById(R.id.miandarao_kaiguan_on);
		miandarao_kaiguan_off = (ImageView) view
				.findViewById(R.id.miandarao_kaiguan_off);

		if (miandarao == 1) {
			miandarao_kaiguan_on.setVisibility(View.VISIBLE);
			miandarao_kaiguan_off.setVisibility(View.GONE);
		} else {
			miandarao_kaiguan_on.setVisibility(View.GONE);
			miandarao_kaiguan_off.setVisibility(View.VISIBLE);
		}
	}

	protected void getTixing() {
		kaiguan_on = (ImageView) view.findViewById(R.id.kaiguan_on);
		kaiguan_off = (ImageView) view.findViewById(R.id.kaiguan_off);

		if (tip == 1) {
			kaiguan_on.setVisibility(View.VISIBLE);
			kaiguan_off.setVisibility(View.GONE);
		} else {
			kaiguan_on.setVisibility(View.GONE);
			kaiguan_off.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 是否开启商家免打扰
	 */
	private void miandarao_switch_btn() {
		StringRequest stringRequest = new StringRequest(Method.POST,
				miandarao_url, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {

					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> map = new HashMap<String, String>();
				map.put("company_id", user_id);
				map.put("disturb", miandarao + "");
				return map;
			}
		};
		queue.add(stringRequest);
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(
				ConstantForSaveList.DEFAULTRETRYTIME * 1000, 1, 1.0f));

	}

	// 登出
	public void loginOut() {
		showAlertDialog();
	}

	public void showAlertDialog() {

		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
		builder.setMessage("确认退出后，将无法接收新消息");
		builder.setTitle("退出提示");
		builder.setNegativeButton("确 定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				ApplicationControl.getInstance().logout(new EMCallBack() {
					@Override
					public void onSuccess() {
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Editor edit = sp.edit();
								edit.putString("userId", "");
								edit.putString("token", "");
								edit.commit();
								ConstantForSaveList.usersNick = new ArrayList<HuanxinUser>();// 置空
								getActivity().finish();
								// carson 点击退出账号时，关闭之前的界面

								if (FindPJLoginActivity.instance != null) {
									FindPJLoginActivity.instance.finish();
								}

								if (MainCompanyActivity.instens != null) {
									MainCompanyActivity.instens.finish();
								}
							}

						});
					}

					@Override
					public void onProgress(int arg0, String arg1) {

					}

					@Override
					public void onError(int arg0, String arg1) {

					}
				});

			}
		});

		builder.setPositiveButton("点错了", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});
		builder.create().show();
	}

	protected void getMoneyPool() {
		TextView text = (TextView) view.findViewById(R.id.moneyPool);
		text.setText(moneyPool + "");
		saveInfor(moneyPool);
	}

	protected void showWait(boolean isShow) {
		if (isShow) {
			if (null == dialog) {
				dialog = new WaitDialog(getActivity());
			}
			dialog.show();
		} else {
			if (null != dialog) {
				dialog.dismiss();
			}
		}
	}

	/**
	 * 检查更新
	 */
	// carson 屏蔽点击更新

	/*
	 * @OnClick(R.id.checkUpdate) public void checkUpdateOnclick(View v) {
	 * String ui = update_info.getText().toString(); if (ui.equals("发现新版本")) {
	 * UmengUpdateAgent.setUpdateAutoPopup(true);
	 * UmengUpdateAgent.setUpdateOnlyWifi(false);
	 * UmengUpdateAgent.forceUpdate(getActivity()); } else {
	 * showAlertDialog("您当前的版本已是最新版本，无需升级", "已是最新版本"); } }
	 */

	public void showAlertDialog(String str, final String str2) {

		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
		builder.setMessage(str);
		builder.setTitle(str2);

		builder.setPositiveButton("加油吧", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	/**
	 * carson设置点击监听
	 */
	@Override
	public void onClick(View arg0) {
		if (user_id.equals("")) {
			CommonWidget.showAlertDialog(getActivity(), getActivity(),
					"您还没有登录，注册登录后才可以查看哦！", "温馨提示", "随便看看");
		} else {
			Intent intent = new Intent();
			switch (arg0.getId()) {
			case R.id.checkUpdate:
				showAlertDialog("当前版本已是最新版本，敬请期待下一版本", "温馨提示");
				break;
			case R.id.editTelephone:
				intent.setClass(getActivity(), EditPhoneActivity.class);
				startActivity(intent);
				break;
			case R.id.editPassword:
				intent.setClass(getActivity(), ModifyPwdActivity.class);
				startActivity(intent);
				break;
			case R.id.jianyi:
				intent.setClass(getActivity(), SuggestActivity.class);
				startActivity(intent);
				break;
			case R.id.loginOut:
				loginOut();
				break;
			default:
				break;
			}

		}

	}

}
