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
import com.quark.common.ToastUtil;
import com.quark.utils.NetWorkCheck;
import com.quark.utils.Util;

public class ExportGroupListActivity extends BaseActivity {
	private EditText editText;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_export_list);
		SharedPreferences sp = getSharedPreferences("jrdr.setting",
				MODE_PRIVATE);

		RelativeLayout topLayout = (RelativeLayout) findViewById(R.id.top_layout);
		topLayout.setBackgroundColor(getResources().getColor(
				R.color.guanli_common_color));
		editText = (EditText) findViewById(R.id.edittext);
		TextView savetext = (TextView) findViewById(R.id.savetext);

		savetext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (editText.getText().toString() != null
						&& !"".equals(editText.getText().toString().trim())) {
					if (Util.isEmail(editText.getText().toString().trim())) {
						if (NetWorkCheck
								.isOpenNetwork(ExportGroupListActivity.this)) {
							setResult(
									RESULT_OK,
									new Intent().putExtra("data", editText
											.getText().toString().trim()));
							finish();
						} else {
							Toast.makeText(getApplicationContext(),
									"请检查网络连接设置", 0).show();
						}

					} else {
						Toast.makeText(getApplicationContext(), "邮箱格式不正确...", 0)
								.show();
					}

				} else {
					Toast.makeText(getApplicationContext(), "邮箱不能为空...", 0)
							.show();
				}

			}
		});
	}
}
