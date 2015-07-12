package com.quark.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;
import com.quark.model.MyAchievementModel;

/**
 * @ClassName: 我 兼职成就
 * 
 * @Description:
 * 
 * @author:howe
 * 
 * @date: 2014-11-15 上午10:15:52
 */
public class MyAchAdapter extends BaseAdapter {

	private ViewHolder holder;
	private List<MyAchievementModel> list;
	private Context context;

	public MyAchAdapter(Context context, List<MyAchievementModel> list) {
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_me_achievement, null);
			holder.type = (TextView) convertView.findViewById(R.id.type);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.pingyu = (TextView) convertView.findViewById(R.id.pingyu);
			holder.pingfen = (TextView) convertView.findViewById(R.id.pingfen);
			holder.icon_warn = (ImageView) convertView
					.findViewById(R.id.icon_warn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
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
			holder.type.setText("服务");
			holder.type.setBackgroundResource(R.color.type_fuwuyuan);
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
			holder.type.setText("工作");
			holder.type.setBackgroundResource(R.color.type_gongzuorenyuan);
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
		holder.pingyu.setText(list.get(i).getRemark());
		String pf = list.get(i).getComment();
		holder.pingfen.setText(pf);
		if (pf.equals("优秀")) {
			holder.pingfen.setTextColor(context.getResources().getColor(
					R.color.item_youxiu)); // 评分
		} else if (pf.equals("好评")) {
			holder.pingfen.setTextColor(context.getResources().getColor(
					R.color.item_haoping)); // 评分
		} else if (pf.equals("差评")) {
			holder.pingfen.setTextColor(context.getResources().getColor(
					R.color.item_chaping)); // 评分
		} else if (pf.equals("放飞机")) {
			holder.pingfen.setTextColor(context.getResources().getColor(
					R.color.item_feiji)); // 评分
			holder.icon_warn.setVisibility(View.VISIBLE);
		} else if (pf.equals("中评")) {
			holder.pingfen.setTextColor(context.getResources().getColor(
					R.color.item_zhongping)); // 评分
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView type;
		TextView title;
		TextView pingyu;
		TextView pingfen;
		ImageView icon_warn;
	}

}
