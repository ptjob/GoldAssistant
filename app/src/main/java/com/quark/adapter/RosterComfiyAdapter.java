package com.quark.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;
import com.quark.model.RosterComfiy;

/**
 * 商家端：花名册-确认报名 adapter
 * 
 * @dade
 * @author C罗
 * 
 */
public class RosterComfiyAdapter extends BaseAdapter {

	private ViewHolder holder;
	private List<RosterComfiy> list;
	private Context context;

	public RosterComfiyAdapter(Context context, List<RosterComfiy> roster) {
		this.list = roster;
		this.context = context;
	}
	@Override
	public int getCount(){
		return (list==null)?0:list.size();
	}
	
	@Override
	public Object getItem(int position){
		return list.get(position);
	}

	@Override
	public long getItemId(int position){
		return position;
	}
	
	@Override
	public View getView(int position,View convertView,ViewGroup parent){
		if(convertView==null){
			holder = new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.company_partjob_comfiy, null);
			holder.comfiy_title = (TextView)convertView.findViewById(R.id.comfiy_title);
			holder.comfiy_status=(TextView)convertView.findViewById(R.id.comfiy_status);
			holder.total_num =(TextView)convertView.findViewById(R.id.total_num);
			holder.cim_num =(TextView)convertView.findViewById(R.id.cim_num);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.comfiy_title.setText(list.get(position).getTitle());
		TextView txt = (TextView)convertView.findViewById(R.id.comfiy_status);
		//已确认人数=报名人数
		if(list.get(position).getHead_count()==list.get(position).getConfirmed_count()){
			txt.setVisibility(View.VISIBLE);
		}else{
			txt.setVisibility(View.GONE);
		}
		holder.total_num.setText(list.get(position).getHead_count()+"");
		holder.cim_num.setText(list.get(position).getConfirmed_count()+"");
		return convertView;
	}
	public static class ViewHolder{
		TextView comfiy_title;
		TextView comfiy_status;
		TextView total_num;
		TextView cim_num;
	}
}
