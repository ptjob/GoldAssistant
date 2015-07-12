package com.quark.jianzhidaren;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;
import com.umeng.analytics.MobclickAgent;

public class LaheiPageActivity extends Activity {
	public static LaheiPageActivity instance;
	private final static String telephone = "0755-23742220";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MobclickAgent.updateOnlineConfig(this);
		instance = this;
		setContentView(R.layout.activity_lahei);
		TextView phone = (TextView) findViewById(R.id.phone);
		phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ telephone));
				startActivity(intent);
			}
		});

		TextView exit = (TextView) findViewById(R.id.exit);
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (EnterActivity.instance != null) {
					EnterActivity.instance.finish();
				}
				finish();
			}
		});

	}

}
