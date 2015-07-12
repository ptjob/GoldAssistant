package com.quark.us;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;

/**
 * 如何提高信誉值
 * 
 * @author cluo
 * 
 */
public class HowRaiseAuthorityActivity extends Activity {

	private ImageButton imageBtn;
	private View done_job, go_shiming, wanshan, jiaona;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_re_how_reputa_value);
		// 返回
		imageBtn = (ImageButton) findViewById(R.id.back);
		imageBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HowRaiseAuthorityActivity.this.finish();
			}
		});
		// 完成兼职任务
		done_job = findViewById(R.id.done_job);
		initComontView(done_job, R.string.done_job2);
		// 实名认证
		go_shiming = findViewById(R.id.go_shiming);
		initComontView(go_shiming, R.string.go_shiming2);
		// 完善个人资料
		wanshan = findViewById(R.id.wanshan);
		initComontView(wanshan, R.string.wanshan2);
		// 缴纳放飞机保证金
		jiaona = findViewById(R.id.jiaona);
		initComontView(jiaona, R.string.jiaona2);
	}

	private void initComontView(View view, int titleId) {
		view.setOnClickListener(layoutListener);
		((TextView) view.findViewById(R.id.me_title)).setText(titleId);
		((ImageView) view.findViewById(R.id.me_enter_activity)).setTag(view);
		((ImageView) view.findViewById(R.id.me_enter_activity))
				.setOnClickListener(layoutListener);
	}

	private View.OnClickListener layoutListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			handlerClickIntent(v.getId());
		}
	};

	// 响应点击事件
	private void handlerClickIntent(int viewId) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (viewId) {
		case R.id.done_job:
			intent = new Intent(HowRaiseAuthorityActivity.this,
					LocalCarouselActivity.class);
			intent.putExtra("type", 1 + "");
			break;
		case R.id.go_shiming:
			intent = new Intent(HowRaiseAuthorityActivity.this,
					LocalCarouselActivity.class);
			intent.putExtra("type", 2 + "");
			break;
		case R.id.wanshan:
			intent = new Intent(HowRaiseAuthorityActivity.this,
					MyResumeActivity.class);
			break;
		// 缴纳诚意金
		case R.id.jiaona:
			intent = new Intent(HowRaiseAuthorityActivity.this,
					LocalCarouselActivity.class);
			intent.putExtra("type", 3 + "");
			break;
		}
		if (null != intent) {
			startActivity(intent);
		}
	}

}
