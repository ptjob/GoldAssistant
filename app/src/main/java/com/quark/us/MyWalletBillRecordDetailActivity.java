package com.quark.us;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;
import com.quark.model.BillRecordBean;

public class MyWalletBillRecordDetailActivity extends Activity {

	private ImageView bill_record_detail_imv,
			bill_record_detail_charge_status_imv;
	private TextView bill_record_detail_company_name_tv,
			bill_record_detail_money_tv, bill_record_detail_pay_type_tv,
			bill_record_detail_charge_type_tv,
			bill_record_detail_create_time_tv,
			bill_record_detail_charge_num_tv,
			bill_record_detail_charge_status_tv;
	private RelativeLayout fukuanfangsiLayout;// 付款方式 提现时展示,充值时隐藏
	private BillRecordBean billRecordBean;// 上个界面传递过来的bean

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill_record_detail);
		setBackButton();
		billRecordBean = (BillRecordBean) getIntent().getSerializableExtra(
				"BillRecordBean");
		initView();
		initData();
	}

	private void initView() {
		fukuanfangsiLayout = (RelativeLayout) findViewById(R.id.fukuanfangsi_relayout);// 付款方式
																						// 提现时展示,充值时隐藏
		bill_record_detail_imv = (ImageView) findViewById(R.id.bill_record_detail_imv);// 提现或者充值icon
		bill_record_detail_company_name_tv = (TextView) findViewById(R.id.bill_record_detail_company_name_tv);// 公司名称/标题
		bill_record_detail_money_tv = (TextView) findViewById(R.id.bill_record_detail_money_tv);// 金额
		bill_record_detail_pay_type_tv = (TextView) findViewById(R.id.bill_record_detail_pay_type_tv);// 付款方式
		bill_record_detail_charge_type_tv = (TextView) findViewById(R.id.bill_record_detail_charge_type_tv);// 交易类型
		bill_record_detail_create_time_tv = (TextView) findViewById(R.id.bill_record_detail_create_time_tv);// 交易创建时间
		bill_record_detail_charge_num_tv = (TextView) findViewById(R.id.bill_record_detail_charge_num_tv);// 交易号
		bill_record_detail_charge_status_tv = (TextView) findViewById(R.id.bill_record_detail_charge_status_tv);// 交易状态
		bill_record_detail_charge_status_imv = (ImageView) findViewById(R.id.bill_record_detail_charge_status_imv);// 交易状态icon
	}

	private void initData() {
		if (billRecordBean != null) {
			if (billRecordBean.getType() == 1) {
				// 1提现 2充值
				bill_record_detail_imv
						.setImageResource(R.drawable.my_wallet_withdrawal);
				bill_record_detail_money_tv.setText("-"
						+ billRecordBean.getMoney());
				bill_record_detail_charge_type_tv.setText("2个工作日内到账");
				fukuanfangsiLayout.setVisibility(View.VISIBLE);
				// 支付方式：1-支付宝 2-银行卡 3-钱包余额 -1退款
				if (billRecordBean.getPay_type() == 1) {
					bill_record_detail_pay_type_tv.setText("支付宝提现");
				} else if (billRecordBean.getPay_type() == 2) {
					bill_record_detail_pay_type_tv.setText("银行卡提现");
				}
			} else if (billRecordBean.getType() == 2) {
				fukuanfangsiLayout.setVisibility(View.GONE);
				bill_record_detail_imv
						.setImageResource(R.drawable.my_wallet_topup);
				bill_record_detail_money_tv.setText("+"
						+ billRecordBean.getMoney());
				bill_record_detail_charge_type_tv.setText("即时到账");
			}
			bill_record_detail_company_name_tv.setText(billRecordBean
					.getTitle());
			bill_record_detail_create_time_tv.setText(billRecordBean
					.getPost_time());
			bill_record_detail_charge_num_tv.setText(billRecordBean
					.getBill_flow());
			// 状态 0-删除 1-处理中 2-成功 3-失败
			String status_temp = "";
			if (billRecordBean.getStatus() == 0) {
				status_temp = "已删除";
			} else if (billRecordBean.getStatus() == 1) {
				status_temp = "处理中";
				bill_record_detail_charge_status_imv
						.setImageResource(R.drawable.pop_btn_fail);
			} else if (billRecordBean.getStatus() == 2) {
				status_temp = "交易成功";
				bill_record_detail_charge_status_imv
						.setImageResource(R.drawable.pop_btn_ok);
			} else if (billRecordBean.getStatus() == 3) {
				status_temp = "交易失败";
				bill_record_detail_charge_status_imv
						.setImageResource(R.drawable.pop_btn_fail);
			}
			bill_record_detail_charge_status_tv.setText(status_temp);

		}

	}

	/**
	 * 设置返回按钮
	 */
	public void setBackButton() {
		TextView titiTv = (TextView) findViewById(R.id.title);
		titiTv.setText("账单详情");
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
