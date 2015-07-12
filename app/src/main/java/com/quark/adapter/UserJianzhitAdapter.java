package com.quark.adapter;

import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;
import com.quark.model.MyJianZhiModle;

/**
 * 
 * @ClassName: UserJianzhitAdapter
 * @Description: 我的兼职 比广场多了状态
 * @author howe
 * @date 2015-2-2 下午4:08:18
 * 
 */
public class UserJianzhitAdapter extends BaseAdapter {

	private ViewHolder holder;
	private List<MyJianZhiModle> list;
	private Context context;

	public UserJianzhitAdapter(Context context, List<MyJianZhiModle> list) {
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
			holder.status = (ImageView) convertView.findViewById(R.id.status);
			holder.status.setVisibility(View.VISIBLE);
			holder.baozhengjinImv = (ImageView) convertView
					.findViewById(R.id.baozhengjin_icon_img);
			holder.chaojiImv = (ImageView) convertView
					.findViewById(R.id.chaoji_icon_img);
			// 签到记录
			holder.my_jianzhi_item_qiandao_relayout = (LinearLayout) convertView
					.findViewById(R.id.my_jianzhi_item_qiandao_relayout);
			holder.allCountTv = (TextView) convertView
					.findViewById(R.id.my_jianzhi_item_qiandao_all_count_tv);
			holder.mySignCountTv = (TextView) convertView
					.findViewById(R.id.my_jianzhi_item_qiandao_person_count_tv);
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
		String datestr = list.get(i).getStart_time();
		if (datestr.length() > 5) {
			datestr = datestr.substring(5, datestr.length());
		}
		holder.date.setText(datestr);
		holder.addreess.setText(list.get(i).getCounty());
		holder.dates.setText(datestr + "(" + list.get(i).getDays() + "天)");
		holder.freeNumber.setText(list.get(i).getLeft_count() + "");
		if (list.get(i).getPay_type() == 1) {
			holder.salary.setText(list.get(i).getPay() + "元/时");
		} else {
			holder.salary.setText(list.get(i).getPay() + "元/天");
		}
		if (list.get(i).getApply() == 2) {
			holder.status.setImageDrawable(context.getResources().getDrawable(
					R.drawable.myjob_icon_refuse));
		} else if (list.get(i).getApply() == 1) {
			holder.status.setImageDrawable(context.getResources().getDrawable(
					R.drawable.myjob_icon_pass));
			// 如果已经签到过一次才展示签到记录
			JSONObject qiandaoJsonObject = list.get(i).getSignMap();
			if (qiandaoJsonObject != null && qiandaoJsonObject.length() > 0) {
				holder.my_jianzhi_item_qiandao_relayout
						.setVisibility(View.VISIBLE);
				holder.allCountTv.setText(list.get(i).getAll_count() + "");
				holder.mySignCountTv.setText(qiandaoJsonObject.length() + "");
			}

		} else {
			// 人员已招满,则显示备选,没招满显示待确认
			if (list.get(i).getLeft_count() <= 0) {
				holder.status.setImageDrawable(context.getResources()
						.getDrawable(R.drawable.beitai));
			} else {
				holder.status.setImageDrawable(context.getResources()
						.getDrawable(R.drawable.myjob_icon_wait));
			}
		}
		// 是否显示保证金，超级标签 0表示没有,1表示有
		if (list.get(i).getGuarantee() == 0) {
			holder.baozhengjinImv.setVisibility(View.GONE);
		} else {
			holder.baozhengjinImv.setVisibility(View.VISIBLE);
		}
		if (list.get(i).getSuperJob() == 0) {
			holder.chaojiImv.setVisibility(View.GONE);
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
		ImageView status;
		ImageView baozhengjinImv;
		ImageView chaojiImv;
		LinearLayout my_jianzhi_item_qiandao_relayout;// 签到记录
		TextView allCountTv;// 发起的总签到次数
		TextView mySignCountTv;// 我的签到次数
	}

}
