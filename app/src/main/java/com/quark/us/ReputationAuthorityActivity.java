package com.quark.us;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;
import com.quark.localslideshowview.LocalSlideShowView;

/**
 * 关于信誉值和诚意金
 * 
 * @author cluo
 * 
 */
public class ReputationAuthorityActivity extends Activity {

	private ImageButton imageBtn;
	private LocalSlideShowView mSlideShowView;
	private Button liaojie;
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

		SharedPreferences sp = getSharedPreferences("jrdr.setting",
				android.content.Context.MODE_PRIVATE);
			title.setText(R.string.xinyizhiqx);
			RelativeLayout topLayout = (RelativeLayout) findViewById(R.id.top_title_layout);
			topLayout.setBackgroundColor(getResources().getColor(
					R.color.guanli_common_color));
			/**
			 * 设置图片主要是传入图片路径，可以是int类型的，也 可以像博客中的一样，传入String类型的
			 */

			imageUris.add(R.drawable.intruduction_aboutmoney_1);
			imageUris.add(R.drawable.intruduction_aboutmoney_2);
			imageUris.add(R.drawable.intruduction_aboutmoney_3);
			imageUris.add(R.drawable.intruduction_aboutmoney_4);
			imageUris.add(R.drawable.intruduction_aboutmoney_5);

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
			ReputationAuthorityActivity.this.finish();
		}
	};

}
