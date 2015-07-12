package com.quark.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;
import com.quark.model.BillRecordBean;

public class WalletLiushuiAdapter extends BaseAdapter {
	private Context context;
	private ViewHolder viewHolder;
	private ArrayList<BillRecordBean> list;

	public WalletLiushuiAdapter(Context context, ArrayList<BillRecordBean> list) {
		this.context = context;
		this.list = (ArrayList<BillRecordBean>) list.clone();// 解决content of
																// adapter has
																// changed 的bug
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_wallet_liushui_record, null);
			convertView.setTag(viewHolder);

			viewHolder.imv = (ImageView) convertView
					.findViewById(R.id.item_bill_record_imv);
			viewHolder.typeTv = (TextView) convertView
					.findViewById(R.id.item_bill_record_type_tv);
			viewHolder.moneyTv = (TextView) convertView
					.findViewById(R.id.item_bill_record_money_tv);
			viewHolder.timeTv = (TextView) convertView
					.findViewById(R.id.item_bill_record_time_tv);
			viewHolder.statusTv = (TextView) convertView
					.findViewById(R.id.item_bill_record_status_tv);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (1 == list.get(arg0).getType()) {
			viewHolder.imv.setImageResource(R.drawable.my_wallet_withdrawal);
			viewHolder.moneyTv.setText("-" + list.get(arg0).getMoney());
			viewHolder.moneyTv.setTextColor(context.getResources().getColor(
					R.color.head_color));
		} else if (2 == list.get(arg0).getType()) {
			viewHolder.imv.setImageResource(R.drawable.my_wallet_topup);
			viewHolder.moneyTv.setText("+" + list.get(arg0).getMoney());
			viewHolder.moneyTv.setTextColor(context.getResources().getColor(
					R.color.btn_green_noraml));
		}
		// 账单类型 1提现2充值(显示title)
		viewHolder.typeTv.setText(list.get(arg0).getTitle());
		viewHolder.timeTv.setText(list.get(arg0).getPost_time());
		String status = "";
		if (0 == list.get(arg0).getStatus()) {
			status = "";
		} else if (1 == list.get(arg0).getStatus()) {

			status = "处理中";
		} else if (2 == list.get(arg0).getStatus()) {
			status = "成功";

		} else if (3 == list.get(arg0).getStatus()) {
			status = "失败";

		}
		viewHolder.statusTv.setText(status);// 状态 0-删除 1-处理中
		// 2-成功 3-失败

		return convertView;
	}

	class ViewHolder {
		ImageView imv;
		TextView typeTv;// 提现，充值
		TextView timeTv, moneyTv, statusTv;// 时间、金钱、状态
	}

}
