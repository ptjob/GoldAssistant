package com.quark.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carson.constant.ConstantForSaveList;
import com.qingmu.jianzhidaren.R;
import com.quark.model.BankCardBean;

public class WalletBankCardAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<BankCardBean> list;
	private ViewHolder viewHolder;
	private Drawable[] iconDrawables;

	public WalletBankCardAdapter(Context context, ArrayList<BankCardBean> list) {
		this.context = context;
		this.list = list;
		iconDrawables = new Drawable[] {
				context.getResources().getDrawable(R.drawable.bank_01),
				context.getResources().getDrawable(R.drawable.bank_02),
				context.getResources().getDrawable(R.drawable.bank_03),
				context.getResources().getDrawable(R.drawable.bank_04),
				context.getResources().getDrawable(R.drawable.bank_05),
				context.getResources().getDrawable(R.drawable.bank_06),
				context.getResources().getDrawable(R.drawable.bank_07),
				context.getResources().getDrawable(R.drawable.bank_08),
				context.getResources().getDrawable(R.drawable.bank_09),
				context.getResources().getDrawable(R.drawable.bank_10),
				context.getResources().getDrawable(R.drawable.bank_11),
				context.getResources().getDrawable(R.drawable.bank_12),
				context.getResources().getDrawable(R.drawable.bank_13),
				context.getResources().getDrawable(R.drawable.bank_14),
				context.getResources().getDrawable(R.drawable.bank_15),
				context.getResources().getDrawable(R.drawable.bank_16),
				context.getResources().getDrawable(R.drawable.bank_17),
				context.getResources().getDrawable(R.drawable.bank_18),
				context.getResources().getDrawable(R.drawable.bank_19),
				context.getResources().getDrawable(R.drawable.bank_20) };
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_bank_card, null);
			viewHolder.imv = (ImageView) convertView
					.findViewById(R.id.item_bank_card_imv);
			viewHolder.nameTv = (TextView) convertView
					.findViewById(R.id.item_bank_card_name_tv);
			viewHolder.bankNameTv = (TextView) convertView
					.findViewById(R.id.item_bank_card_bank_tv);
			viewHolder.bankNumTv = (TextView) convertView
					.findViewById(R.id.item_bank_card_cardnum_tv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.nameTv.setText(list.get(arg0).getAccount_name());
		String bankCardNum = list.get(arg0).getAccount_num();
		if (bankCardNum != null) {
			if (bankCardNum.length() >= 8) {
				String temp = bankCardNum.substring(0, 2);
				bankCardNum = temp + "******" + bankCardNum.substring(8);
			}
			viewHolder.bankNumTv.setText(bankCardNum);
		}
		int type = list.get(arg0).getType();
		if (type == 1) {
			viewHolder.bankNameTv.setText("-支付宝提现");
			viewHolder.imv.setImageDrawable(context.getResources().getDrawable(
					R.drawable.zfb));
		} else if (type == 2) {
			if (list.get(arg0).getBank() != null) {
				for (int i = 0; i < 20; i++) {
					if (ConstantForSaveList.nativeBankList[i].equals(list
							.get(arg0).getBank().trim())) {
						viewHolder.imv.setImageDrawable(iconDrawables[i]);
					}
				}
			}
			viewHolder.bankNameTv
					.setText("-" + list.get(arg0).getBank() + "提现");
		}

		return convertView;
	}

	class ViewHolder {
		ImageView imv;
		TextView nameTv, bankNameTv, bankNumTv;
	}

}
