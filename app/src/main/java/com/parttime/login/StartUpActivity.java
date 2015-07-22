package com.parttime.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.utils.NetWorkCheck;

/**
 * C罗
 * 
 * @author Administrator
 * 
 */
public class StartUpActivity extends BaseActivity {
	private final int SPLASH_DISPLAY_LENGHT = 1000; // 延迟1秒

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.load);
		if (NetWorkCheck.isOpenNetwork(this)) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent mainIntent = new Intent(StartUpActivity.this,
							FindPJLoginActivity.class);
					mainIntent.putExtra("from_startupact", true);// 从启动页传来
					StartUpActivity.this.startActivity(mainIntent);
					StartUpActivity.this.finish();
				}
			}, SPLASH_DISPLAY_LENGHT);
		} else {
			Toast.makeText(getApplicationContext(), getString(R.string.no_net_tip), Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(StartUpActivity.this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(StartUpActivity.this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
