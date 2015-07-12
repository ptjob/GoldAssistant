package com.carson.broker;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.model.JiedanBean;

public class JiedanDetailActivity extends BaseActivity {

	private JiedanBean bean;
	private TextView jz_type, jz_title, jz_publish_time, jz_work_info,jz_telephone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jiedan_detail);
		bean = (JiedanBean) getIntent().getExtras().get("jiedanbean");
		initView();

	}

	private void initView() {
		setTopTitle(bean.getTitle());
		setBackButton();
		// 头部显示灰色
		RelativeLayout reLayout = (RelativeLayout) findViewById(R.id.top_title_layout);
		reLayout.setBackgroundColor(getResources().getColor(
				R.color.guanli_common_color));
		jz_type = (TextView) findViewById(R.id.jz_type);
		jz_title = (TextView) findViewById(R.id.jz_title);
		jz_publish_time = (TextView) findViewById(R.id.jz_publish_time);
		jz_work_info = (TextView) findViewById(R.id.jz_work_info);
		jz_telephone = (TextView) findViewById(R.id.jz_telephone);

		// 初始化
		jz_type.setText(bean.getType());
		if (bean.getType().equals("派发")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_1);
		}
		if (bean.getType().equals("促销")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_2);
		}
		if (bean.getType().equals("其他")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_3);
		}
		if (bean.getType().equals("家教")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_4);
		}
		if (bean.getType().equals("服务员")) {
			// jz_type.setText("服务");
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_5);
		}
		if (bean.getType().equals("礼仪")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_6);
		}
		if (bean.getType().equals("安保人员")) {
			// jz_type.setText("安保");
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_7);
		}
		if (bean.getType().equals("模特")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_8);
		}
		if (bean.getType().equals("主持")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_9);
		}
		if (bean.getType().equals("翻译")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_10);
		}
		if (bean.getType().equals("工作人员")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_11);
		}
		if (bean.getType().equals("话务")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_12);
		}
		if (bean.getType().equals("充场")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_13);
		}
		if (bean.getType().equals("演艺")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_14);
		}
		if (bean.getType().equals("访谈")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_15);
		}
		jz_title.setText(bean.getTitle());
		jz_publish_time.setText("发布时间   " + bean.getCreate_time());
		jz_telephone.setText("联系电话   " + bean.getContacts());
		jz_work_info.setText(bean.getContent());

	}

	public void setTopTitle(String titlestr) {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(titlestr);
	}

	/**
	 * 设置返回按钮
	 */
	public void setBackButton() {

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
