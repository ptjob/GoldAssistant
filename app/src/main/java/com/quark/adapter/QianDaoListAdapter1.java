package com.quark.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;
import com.quark.model.QianDaoListBean;

public class QianDaoListAdapter1 extends BaseAdapter {
	private Context context;
	private ArrayList<QianDaoListBean> qiandaoList;

	public QianDaoListAdapter1(Context context,
			ArrayList<QianDaoListBean> qiandaoList) {
		this.context = context;
		this.qiandaoList = qiandaoList;
	}

	@Override
	public int getCount() {
		return qiandaoList == null ? 0 : qiandaoList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return qiandaoList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_qiandaolist1, null);
			viewHolder.indexTv = (TextView) convertView
					.findViewById(R.id.item1_index_tv);
			viewHolder.nameTv = (TextView) convertView
					.findViewById(R.id.item1_name_tv);
			viewHolder.countNumTv = (TextView) convertView
					.findViewById(R.id.item1_countnum_tv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (arg0 == 0) {
			viewHolder.indexTv.setText("");
			viewHolder.nameTv.setText("姓名");
			viewHolder.countNumTv.setText("次数");
		} else {
			viewHolder.indexTv.setText(arg0 + "");
			viewHolder.nameTv.setText(qiandaoList.get(arg0).getName());
			viewHolder.countNumTv.setText(qiandaoList.get(arg0).getSign_count()
					+ "");
		}
		return convertView;
	}

	class ViewHolder {
		TextView indexTv, nameTv, countNumTv;
	}

}
