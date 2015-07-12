package com.quark.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;

/**
*
* @ClassName: RechargeLogAdapter
* @Description: 充值记录
* @author howe
* @date 2015-1-24 下午6:05:35
*
*/
public class RechargeLogAdapter extends BaseAdapter {

	private ViewHolder holder;
	private List<String> list;
	private Context context;
	 
	public RechargeLogAdapter(Context context,List<String> list) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int i, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_rechargelog, null);
			holder.title = (TextView) convertView.findViewById(R.id.contenx);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.title.setText(list.get(i));
		
		return convertView;
	}

	private static class ViewHolder {
		TextView title ;
	}

}
