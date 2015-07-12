package com.quark.adapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carson.constant.ConstantForSaveList;
import com.qingmu.jianzhidaren.R;
import com.quark.model.GuangchangModle;

/**
 * 
 * @ClassName: GuangchangListAdapter
 * @Description: 广场适配器
 * @author howe
 * @date 2015-1-26 下午2:55:11
 * 
 */
public class GuangchangListAdapter extends BaseAdapter {

	private ViewHolder holder;
	private List<GuangchangModle> list;
	private Context context;
	private SharedPreferences sp;

	public GuangchangListAdapter(Context context, List<GuangchangModle> list) {
		this.list = list;
		this.context = context;
		sp = context.getSharedPreferences("jrdr.setting", Context.MODE_PRIVATE);
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_guangchang, null);
			holder.type = (TextView) convertView.findViewById(R.id.type);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.addreess = (TextView) convertView
					.findViewById(R.id.addreess);
			holder.dates = (TextView) convertView.findViewById(R.id.dates);
			holder.freeNumber = (TextView) convertView
					.findViewById(R.id.freeNumber);
			holder.salary = (TextView) convertView.findViewById(R.id.salary);
			holder.baozhengjinImv = (ImageView) convertView
					.findViewById(R.id.baozhengjin_icon_img);
			holder.chaojiImv = (ImageView) convertView
					.findViewById(R.id.chaoji_icon_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 判断是否浏览了
		int act_id = list.get(i).getActivity_id();
		if (sp.getBoolean(ConstantForSaveList.userId + act_id, false)) {
			convertView.setBackgroundResource(R.color.liulanhenji);
		} else {
			convertView.setBackgroundResource(R.color.body_color);
		}
		String typestr = list.get(i).getType();

		holder.type.setText(typestr);
		if (typestr.equals("派发")) {
			holder.type.setBackgroundResource(R.color.type_paifa);
		}
		if (typestr.equals("促销")) {
			holder.type.setBackgroundResource(R.color.type_chuxiao);
		}
		if (typestr.equals("其他")) {
			holder.type.setBackgroundResource(R.color.type_qita);
		}
		if (typestr.equals("家教")) {
			holder.type.setBackgroundResource(R.color.type_jiajiao);
		}
		if (typestr.equals("服务员")) {
			holder.type.setBackgroundResource(R.color.type_fuwuyuan);
			holder.type.setText("服务");
		}
		if (typestr.equals("礼仪")) {
			holder.type.setBackgroundResource(R.color.type_liyi);
		}
		if (typestr.equals("安保人员")) {
			holder.type.setText("安保");
			holder.type.setBackgroundResource(R.color.type_baoanrenyuan);
		}
		if (typestr.equals("模特")) {
			holder.type.setBackgroundResource(R.color.type_mote);
		}
		if (typestr.equals("主持")) {
			holder.type.setBackgroundResource(R.color.type_zhuchi);
		}
		if (typestr.equals("翻译")) {
			holder.type.setBackgroundResource(R.color.type_fanyi);
		}
		if (typestr.equals("工作人员")) {
			holder.type.setBackgroundResource(R.color.type_gongzuorenyuan);
			holder.type.setText("工作");
		}
		if (typestr.equals("话务")) {
			holder.type.setBackgroundResource(R.color.type_huawu);
		}
		if (typestr.equals("充场")) {
			holder.type.setBackgroundResource(R.color.type_chongchang);
		}
		if (typestr.equals("演艺")) {
			holder.type.setBackgroundResource(R.color.type_yanyi);
		}
		if (typestr.equals("访谈")) {
			holder.type.setBackgroundResource(R.color.type_fangtan);
		}
		

		holder.title.setText(list.get(i).getTitle());
		String datestr = list.get(i).getPublish_time();
		if (datestr.length() > 5) {
			datestr = datestr.substring(5, datestr.length());
		}
		String startdatestr = list.get(i).getStart_time();
		if (startdatestr.length() > 5) {
			startdatestr = startdatestr.substring(5, startdatestr.length());
		}
		holder.date.setText(datestr);
		// 地址显示有判断,若是按最近搜索出来的记录显示距离xxxkm,其它则显示城市、区
		if (list.get(i).getDistance() > 0) {
			int distance = list.get(i).getDistance();
			if (distance < 1000) {
				holder.addreess.setText(distance + "m");
			} else {
				double temp_distance = distance;
				NumberFormat nf = new DecimalFormat("0.0");
				temp_distance = Double.parseDouble(nf
						.format(temp_distance / 1000));
				holder.addreess.setText(temp_distance + "km");
			}
		} else {
			holder.addreess.setText(list.get(i).getCounty());
		}
		holder.dates.setText(startdatestr + "(" + list.get(i).getDays() + "天)");
		if (list.get(i).getSource() == 0) {
			holder.freeNumber.setText("代招");
			holder.freeNumber.setTextSize(17);
		} else {
			holder.freeNumber.setText(list.get(i).getLeft_count() + "");
		}
		// carson 添加薪资单位
		String carson_pay = null;
		if (0 == list.get(i).getPay_type()) {
			carson_pay = "元/天";
		} else if (1 == list.get(i).getPay_type()) {
			carson_pay = "元/时";
		}
		holder.salary.setText(list.get(i).getPay() + "" + carson_pay);
		// 是否显示保证金，超级标签 0表示没有,1表示有
		if (list.get(i).getGuarantee() == 0) {
			holder.baozhengjinImv.setVisibility(View.INVISIBLE);
		} else {
			holder.baozhengjinImv.setVisibility(View.VISIBLE);
		}
		if (list.get(i).getSuperJob() == 0) {
			holder.chaojiImv.setVisibility(View.INVISIBLE);
		} else {
			holder.chaojiImv.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	private static class ViewHolder {
		TextView type;
		TextView title;
		TextView date;
		TextView addreess;
		TextView dates;
		TextView freeNumber;
		TextView salary;
		TextView source;
		ImageView baozhengjinImv;
		ImageView chaojiImv;

	}

}
