package com.droid.carson;

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

public class BankAdapter extends BaseAdapter {
	private Context context;
	private Drawable[] iconDrawables;

	public BankAdapter(Context context) {
		this.context = context;
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
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.bank_item, null);
			viewHolder.iconImv = (ImageView) convertView
					.findViewById(R.id.bank_item_icon_imv);
			viewHolder.bankNameTv = (TextView) convertView
					.findViewById(R.id.bank_item_bankname_tv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.bankNameTv.setText(ConstantForSaveList.nativeBankList[arg0]);
		viewHolder.iconImv.setImageDrawable(iconDrawables[arg0]);
		return convertView;
	}

	class ViewHolder {
		ImageView iconImv;
		TextView bankNameTv;
	}

}
