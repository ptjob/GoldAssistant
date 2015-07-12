package com.easemob.chatuidemo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qingmu.jianzhidaren.R;
import com.quark.utils.NetWorkCheck;

public class EditActivity extends BaseActivity {
	private EditText editText;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_edit);
		SharedPreferences sp = getSharedPreferences("jrdr.setting",
				MODE_PRIVATE);
		
			RelativeLayout topLayout = (RelativeLayout) findViewById(R.id.top_layout);
			topLayout.setBackgroundColor(getResources().getColor(
					R.color.guanli_common_color));

		editText = (EditText) findViewById(R.id.edittext);
		String title = getIntent().getStringExtra("title");
		String data = getIntent().getStringExtra("data");
		if (title != null)
			((TextView) findViewById(R.id.tv_title)).setText(title);
		if (data != null)
			editText.setText(data);
		editText.setSelection(editText.length());
		TextView savetext = (TextView) findViewById(R.id.savetext);

		savetext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (NetWorkCheck.isOpenNetwork(EditActivity.this)) {
					setResult(RESULT_OK, new Intent().putExtra("data", editText
							.getText().toString()));
					finish();
				} else {
					Toast.makeText(getApplicationContext(), "请检查网络连接设置", 0)
							.show();
				}

			}
		});
	}

	// public void save(View view){
	//
	// }
}
