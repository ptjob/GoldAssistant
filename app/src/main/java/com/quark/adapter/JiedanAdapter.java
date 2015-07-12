package com.quark.adapter;

import java.util.ArrayList;
import com.android.volley.RequestQueue;
import com.qingmu.jianzhidaren.R;
import com.quark.model.JiedanBean;
import com.quark.volley.VolleySington;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class JiedanAdapter extends BaseAdapter {

	private ArrayList<JiedanBean> list;
	private Context context;
	protected RequestQueue queue;
	ViewHolder holder;

	public JiedanAdapter(Context context, ArrayList<JiedanBean> list) {
		this.context = context;
		this.list = list;
		queue = VolleySington.getInstance().getRequestQueue();
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list == null ? null : list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int i, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_jiedan, null);
			holder.typeTv = (TextView) convertView.findViewById(R.id.type);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.time = (TextView) convertView.findViewById(R.id.date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String typestr = list.get(i).getType();
		holder.typeTv.setText(list.get(i).getType());
		if (typestr.equals("派发")) {
			holder.typeTv.setBackgroundResource(R.color.type_paifa);
		}
		if (typestr.equals("促销")) {
			holder.typeTv.setBackgroundResource(R.color.type_chuxiao);
		}
		if (typestr.equals("其他")) {
			holder.typeTv.setBackgroundResource(R.color.type_qita);
		}
		if (typestr.equals("家教")) {
			holder.typeTv.setBackgroundResource(R.color.type_jiajiao);
		}
		if (typestr.equals("服务员")) {
			holder.typeTv.setBackgroundResource(R.color.type_fuwuyuan);
			holder.typeTv.setText("服务");
		}
		if (typestr.equals("礼仪")) {
			holder.typeTv.setBackgroundResource(R.color.type_liyi);
		}
		if (typestr.equals("安保人员")) {
			holder.typeTv.setText("安保");
			holder.typeTv.setBackgroundResource(R.color.type_baoanrenyuan);
		}
		if (typestr.equals("模特")) {
			holder.typeTv.setBackgroundResource(R.color.type_mote);
		}
		if (typestr.equals("主持")) {
			holder.typeTv.setBackgroundResource(R.color.type_zhuchi);
		}
		if (typestr.equals("翻译")) {
			holder.typeTv.setBackgroundResource(R.color.type_fanyi);
		}
		if (typestr.equals("工作人员")) {
			holder.typeTv.setBackgroundResource(R.color.type_gongzuorenyuan);
			holder.typeTv.setText("工作");
		}
		if (typestr.equals("话务")) {
			holder.typeTv.setBackgroundResource(R.color.type_huawu);
		}
		if (typestr.equals("充场")) {
			holder.typeTv.setBackgroundResource(R.color.type_chongchang);
		}
		if (typestr.equals("演艺")) {
			holder.typeTv.setBackgroundResource(R.color.type_yanyi);
		}
		if (typestr.equals("访谈")) {
			holder.typeTv.setBackgroundResource(R.color.type_fangtan);
		}
		holder.title.setText(list.get(i).getTitle());
		String createTime = list.get(i).getCreate_time();
		if (createTime != null && createTime.length() > 10) {
			holder.time.setText(createTime.substring(5, 10));
		}
		return convertView;
	}

	class ViewHolder {
		TextView typeTv, title, time;// 分类、活动标题、发布日期

	}

}
