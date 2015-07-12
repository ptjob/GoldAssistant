package com.quark.us;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;
import com.quark.localslideshowview.LocalSlideShowView;

/**
 * 飞机池
 * 
 * @author cluo
 * 
 */
public class FeiJichiActivity extends Activity {

	private ImageButton imageBtn;
	private LocalSlideShowView mSlideShowView;
	private Button liaojie;
	private String role;
	private TextView title;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_re_reput_authority);
		title = (TextView) findViewById(R.id.title);
		// 返回
		liaojie = (Button) findViewById(R.id.liaojie);
		liaojie.setOnClickListener(linstener);
		// 返回
		imageBtn = (ImageButton) findViewById(R.id.back);
		imageBtn.setOnClickListener(linstener);

		List<Integer> imageUris = new ArrayList<Integer>();

		title.setText(R.string.fangfeiji);
		imageUris.add(R.drawable.intruduction_moneypool_1);
		imageUris.add(R.drawable.intruduction_moneypool_2);
		imageUris.add(R.drawable.intruduction_moneypool_3);
		imageUris.add(R.drawable.intruduction_moneypool_4);

		/**
		 * 获取控件
		 */
		mSlideShowView = (LocalSlideShowView) findViewById(R.id.slideshowView);
		/**
		 * 为控件设置图片
		 */
		mSlideShowView.setImageUris(imageUris);
	}

	OnClickListener linstener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			FeiJichiActivity.this.finish();
		}
	};

}
