package com.quark.guanli;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.BaseActivity;
import com.quark.model.PublishJianzhi;

/**
 * 发布简历 简历预览
 * 
 * @author cluo
 * 
 */
public class JianzhiShowActivity extends BaseActivity {
	PublishJianzhi jianzhi;

	@ViewInject(R.id.bootm_layout)
	LinearLayout bootm_layout;

	@ViewInject(R.id.jz_type)
	TextView jz_type;
	@ViewInject(R.id.jz_title)
	TextView jz_title;
	@ViewInject(R.id.jz_pay)
	TextView jz_pay;

	@ViewInject(R.id.jz_pay_type)
	TextView jz_pay_type;

	@ViewInject(R.id.mini_title)
	RelativeLayout mini_title;

	@ViewInject(R.id.jz_shijian)
	TextView jz_shijian;
	@ViewInject(R.id.work_zone)
	TextView work_zone;
	@ViewInject(R.id.jz_jieshuan_type)
	TextView jz_jieshuan_type;
	@ViewInject(R.id.jz_worker_number)
	TextView jz_worker_number;

	@ViewInject(R.id.jz_layout_height)
	LinearLayout jz_layout_height;
	@ViewInject(R.id.jz_heigh)
	TextView jz_heigh;

	@ViewInject(R.id.jz_layout_shoe)
	LinearLayout jz_layout_shoe;
	@ViewInject(R.id.jz_shoe)
	TextView jz_shoe;
	@ViewInject(R.id.jz_layout_close)
	LinearLayout jz_layout_close;
	@ViewInject(R.id.jz_close)
	TextView jz_close;

	@ViewInject(R.id.jz_layout_sanwei)
	LinearLayout jz_layout_sanwei;
	@ViewInject(R.id.jz_sanwei)
	TextView jz_sanwei;

	@ViewInject(R.id.jz_health_layout)
	LinearLayout jz_health_layout;
	@ViewInject(R.id.jz_health_card)
	TextView jz_health_card;

	@ViewInject(R.id.jz_layout_language)
	LinearLayout jz_layout_language;
	@ViewInject(R.id.jz_language)
	TextView jz_language;

	@ViewInject(R.id.jz_addressdetail)
	TextView jz_addressdetail;
	@ViewInject(R.id.jz_work_info)
	TextView jz_work_info;
	@ViewInject(R.id.jz_layout_tip)
	RelativeLayout jz_layout_tip;
	private RelativeLayout topLayout;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
		setContentView(R.layout.activity_detail);
		ViewUtils.inject(this);
		setTopTitle("活动详细");
		setBackButton();
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		topLayout = (RelativeLayout) findViewById(R.id.top_title_layout);
			topLayout.setBackgroundColor(getResources().getColor(
					R.color.guanli_common_color));
		bootm_layout.setVisibility(View.GONE);
		mini_title.setVisibility(View.GONE);
		jz_layout_tip.setVisibility(View.GONE);

		jianzhi = (PublishJianzhi) getIntent().getSerializableExtra("jianzhi");
		initView();
	}

	public void initView() {
		jz_type.setText(jianzhi.getType());
		if (jianzhi.getType().equals("派发")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_1);
		}
		if (jianzhi.getType().equals("促销")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_2);
		}
		if (jianzhi.getType().equals("其他")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_3);
		}
		if (jianzhi.getType().equals("家教")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_4);
		}
		if (jianzhi.getType().equals("服务员")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_5);
		}
		if (jianzhi.getType().equals("礼仪")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_6);
		}
		if (jianzhi.getType().equals("安保人员")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_7);
		}
		if (jianzhi.getType().equals("模特")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_8);
		}
		if (jianzhi.getType().equals("主持")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_9);
		}
		if (jianzhi.getType().equals("翻译")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_10);
		}
		if (jianzhi.getType().equals("工作人员")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_11);
		}
		if (jianzhi.getType().equals("话务")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_12);
		}
		if (jianzhi.getType().equals("充场")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_13);
		}
		if (jianzhi.getType().equals("演艺")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_14);
		}
		if (jianzhi.getType().equals("访谈")) {
			jz_type.setBackgroundResource(R.drawable.icon_jobtype_15);
		}
		jz_title.setText(jianzhi.getTitle());
		jz_pay.setText(jianzhi.getPay() + "");
		if (jianzhi.getPay_type() == 0) {
			jz_pay_type.setText("元/日");
		} else {
			jz_pay_type.setText("元/时");
		}
		// jz_company.setText(jianzhi.getName());// 商家名称
		String timeStr = "";
		if (jianzhi.getStart_time().length() > 5) {
			timeStr = jianzhi.getStart_time().substring(5,
					jianzhi.getStart_time().length());
		}
		if (jianzhi.getEnd_time().length() > 5) {
			timeStr += "至"
					+ jianzhi.getEnd_time().substring(5,
							jianzhi.getEnd_time().length());
		}
		jz_shijian.setText(timeStr);
		work_zone.setText(jianzhi.getCounty());
		jz_jieshuan_type.setText(jianzhi.getPay_form());
		jz_worker_number.setText(jianzhi.getHead_count() + "人");

		if (jianzhi.getRequire_height() != -1) {
			jz_heigh.setText(jianzhi.getRequire_height() + "");
		} else {
			jz_layout_height.setVisibility(View.GONE);
		}

		if ((jianzhi.getRequire_shoe_weigth() != -1)
				&& (jianzhi.getRequire_shoe_weigth() != 0)) {
			jz_shoe.setText(String.valueOf(jianzhi.getRequire_shoe_weigth()));
		} else {
			jz_layout_shoe.setVisibility(View.GONE);
		}

		if (!jianzhi.getRequire_cloth_weight().equals("-1")) {
			jz_close.setText(jianzhi.getRequire_cloth_weight() + "");
		} else {
			jz_layout_close.setVisibility(View.GONE);
		}

		String sanweiStr = "";
		if (jianzhi.getRequire_bust() != -1
				&& jianzhi.getRequire_beltline() != -1
				&& jianzhi.getRequire_hipline() != -1) {
			sanweiStr = "胸 " + jianzhi.getRequire_bust() + "cm";
			sanweiStr += "  腰 " + jianzhi.getRequire_beltline() + "cm";
			sanweiStr += "  臀 " + jianzhi.getRequire_hipline() + "cm";
			jz_sanwei.setText(sanweiStr);
		} else {
			jz_layout_sanwei.setVisibility(View.GONE);
		}

		if (jianzhi.getRequire_health_record() == 0) {
			jz_health_card.setText("不需要");
		} else if (jianzhi.getRequire_health_record() == 1) {
			jz_health_card.setText("需要");
		} else {
			jz_health_layout.setVisibility(View.GONE);
		}

		if (jianzhi.getRequire_language() != null) {
			jz_language.setText(jianzhi.getRequire_language() + "");
		} else {
			jz_layout_language.setVisibility(View.GONE);
		}

		jz_addressdetail.setText(jianzhi.getAddress());
		jz_work_info.setText(jianzhi.getRequire_info());

	}
}
