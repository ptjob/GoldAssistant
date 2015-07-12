package com.quark.jianzhidaren;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.parttime.login.FindPJLoginActivity;
import com.qingmu.jianzhidaren.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 系统入口
 * 
 * @author Administrator
 * 
 */
public class EnterActivity extends Activity {

	private Button findJian, zhaoJian;
	public static EnterActivity instance;
	String user_id;
	String role;
	boolean from_startupact_flag = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		from_startupact_flag = getIntent().getBooleanExtra("from_startupact",
				false);
		MobclickAgent.updateOnlineConfig(this);
		instance = this;
		setContentView(R.layout.entry);
		findJian = (Button) findViewById(R.id.findJian);// 找兼职
		zhaoJian = (Button) findViewById(R.id.zhaoJian);// 招兼职
		SharedPreferences sp = getSharedPreferences("jrdr.setting",
				MODE_PRIVATE);
		user_id = sp.getString("userId", "");
		role = sp.getString("role", "");

		findJian.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setClass(EnterActivity.this, FindPJLoginActivity.class);
				SharedPreferences sp = getSharedPreferences("jrdr.setting",
						MODE_PRIVATE);
				Editor edit = sp.edit();
				edit.putString("role", "user");
				edit.commit();
				startActivity(intent);
				// 若是从findandlogin跳转过来则销毁当前界面
				if (!from_startupact_flag) {
					EnterActivity.this.finish();
				}
			}
		});

		zhaoJian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(EnterActivity.this, FindPJLoginActivity.class);
				SharedPreferences sp = getSharedPreferences("jrdr.setting",
						MODE_PRIVATE);
				Editor edit = sp.edit();
				edit.putString("role", "company");
				edit.commit();
				startActivity(intent);
				// 若是从findandlogin跳转过来则销毁当前界面
				if (!from_startupact_flag) {
					EnterActivity.this.finish();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences sp = getSharedPreferences("jrdr.setting",
				MODE_PRIVATE);
		user_id = sp.getString("userId", "");
		role = sp.getString("role", "");
		from_startupact_flag = getIntent().getBooleanExtra("from_startupact",
				false);// 判断是从startup传来还是从findandlogin界面传来
		if (from_startupact_flag) {
			if (!user_id.equals("")) {
				Intent intent = new Intent();
				// if (role.equals("company")) {
				// intent.setClass(EnterActivity.this,
				// MainTabActivity.class);
				// } else {
				// intent.setClass(EnterActivity.this, MainActivity.class);
				// }
				intent.setClass(EnterActivity.this, FindPJLoginActivity.class);
				startActivity(intent);
				finish();
			} else {
				// 是否记住了密码
				String remember_tele = sp.getString("remember_tele", "");
				String remember_pwd = sp.getString("remember_pwd", "");
				String remember_role = sp.getString("remember_role", "");
				if (remember_tele != null && !"".equals(remember_tele)
						&& remember_pwd != null && !"".equals(remember_pwd)
						&& remember_role != null && !"".equals(remember_role)) {
					Intent intent = new Intent();
					intent.setClass(EnterActivity.this,
							FindPJLoginActivity.class);
					Editor edit = sp.edit();
					if ("company".equals(remember_role)) {
						edit.putString("role", "company");
					} else {
						edit.putString("role", "user");
					}
					edit.commit();
					startActivity(intent);
					finish();
				} else {

				}
			}
		} else {
			// 若不是从startupactivity传来则正常oncreate
		}
	}

}
