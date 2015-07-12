package com.droid.carson;

import com.qingmu.jianzhidaren.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class BankActivity extends Activity {

	private ListView listview;
	private BankAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bank);
		setBackButton();
		listview = (ListView) findViewById(R.id.list_view);
		adapter = new BankAdapter(BankActivity.this);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.putExtra("bank", arg2);
				setResult(RESULT_OK, intent);
				finish();
			}

		});
	}

	/**
	 * 设置返回按钮
	 */
	public void setBackButton() {
		TextView titiTv = (TextView) findViewById(R.id.title);
		titiTv.setText("选择开户银行");
		LinearLayout back_lay = (LinearLayout) findViewById(R.id.left);
		back_lay.setVisibility(View.VISIBLE);
		back_lay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				finish();
			}
		});
	}

}
