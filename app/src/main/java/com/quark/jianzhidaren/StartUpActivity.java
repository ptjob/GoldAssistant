package com.quark.jianzhidaren;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.qingmu.jianzhidaren.R;
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
			Toast.makeText(getApplicationContext(), "网络连接失败，请确认网络连接", 0).show();
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
