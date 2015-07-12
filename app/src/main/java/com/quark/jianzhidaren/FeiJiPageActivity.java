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

/**
*
* @ClassName: FeiJiPageActivity
* @Description: 放飞机页面
* @author howe
* @date 2015-2-12 下午5:17:59
*
*/
public class FeiJiPageActivity extends Activity {

	public static FeiJiPageActivity instance;
	private final static String telephone = "0755-23742220";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MobclickAgent.updateOnlineConfig( this );
		instance = this;
		setContentView(R.layout.feiji_view);
		String title = getIntent().getStringExtra("title");
		if (title==null||title.equals("")) {
			title = "";
		}
		TextView text = (TextView)findViewById(R.id.title);
		text.setText("非常难过，您报名参加的<"+title+">,未提前通知商家，且未取消本次活动报名，被系统封号处理。");
		TextView phone = (TextView)findViewById(R.id.phone);
		phone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ telephone));  
				startActivity(intent);	
			}
		});
		
	TextView exit = (TextView)findViewById(R.id.exit);
	exit.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(EnterActivity.instance!=null){
				EnterActivity.instance.finish();
			}
				finish();
		}
	});
}	
	
}
