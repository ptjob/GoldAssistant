package com.quark.guanli;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;

public class GuanliListActivity extends Activity{
	
	private TextView textView;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_guanli_main);
		textView = (TextView)findViewById(R.id.right);
		textView.setText("发布");
		textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(GuanliListActivity.this, PublishActivity.class);
				startActivity(intent);
			}
		});
		
	}

}
