package com.quark.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;
import com.quark.model.Roster;

/**
 * 商家端：花名册-取消报名 adapter
 * 
 * @dade
 * @author C罗
 * 
 */
public class RosterAdapter extends BaseAdapter {

	private ViewHolder holder;
	
	private List<Roster> rosters;
	private Context context;

	public RosterAdapter(Context context, List<Roster> roster) {
		this.rosters = roster;
		this.context = context;
	}
	@Override
	public int getCount(){
		return (rosters==null)?0:rosters.size();
	}
	
	@Override
	public Object getItem(int position){
		return rosters.get(position);
	}

	@Override
	public long getItemId(int position){
		return position;
	}
	
	@Override
	public View getView(int position,View convertView,ViewGroup parent){
		int id =  rosters.get(position).getId();
		if(id>0){
			convertView=LayoutInflater.from(context).inflate(R.layout.company_partjob_cancel, null);
			TextView cancel_name = (TextView)convertView.findViewById(R.id.cancel_name);
			TextView cancel_title=(TextView)convertView.findViewById(R.id.cancel_title);
			cancel_title.setText(rosters.get(position).getTitle());
			cancel_name.setText(rosters.get(position).getName());
		}else{
			convertView = LayoutInflater.from(context).inflate(R.layout.company_partjob_comfiy, null);
			TextView comfiy_title = (TextView) convertView.findViewById(R.id.comfiy_title);
			TextView total_num = (TextView) convertView.findViewById(R.id.total_num);
			TextView cim_num = (TextView) convertView.findViewById(R.id.cim_num);
			comfiy_title.setText(rosters.get(position).getTitle());
			TextView txt = (TextView)convertView.findViewById(R.id.comfiy_status);
			//已确认人数=报名人数
			if(rosters.get(position).getHead_count()==rosters.get(position).getConfirmed_count()){
				txt.setVisibility(View.VISIBLE);
			}else{
				txt.setVisibility(View.GONE);
			}
			total_num.setText(rosters.get(position).getHead_count()+"");
			cim_num.setText(rosters.get(position).getConfirmed_count()+"");
		}
		return convertView;
	
	}
	public static class ViewHolder{
		TextView cancel_name;
		TextView cancel_title;
		//
		TextView comfiy_title;
		TextView comfiy_status;
		TextView total_num;
		TextView cim_num;
	}
}
