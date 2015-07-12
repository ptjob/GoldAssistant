package com.quark.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;
import com.quark.model.RosterCancel;

/**
 * 商家端：花名册-取消报名 adapter
 * 
 * @dade
 * @author C罗
 * 
 */
public class RosterCalcelAdapter extends BaseAdapter {

	private ViewHolder holder;
	private List<RosterCancel> rosters;
	private Context context;

	public RosterCalcelAdapter(Context context, List<RosterCancel> roster) {
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
		if(convertView==null){
			holder = new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.company_partjob_cancel, null);
			holder.cancel_name = (TextView)convertView.findViewById(R.id.cancel_name);
			holder.cancel_title=(TextView)convertView.findViewById(R.id.cancel_title);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.cancel_title.setText(rosters.get(position).getTitle());
		holder.cancel_name.setText(rosters.get(position).getName());
		return convertView;
	}
	public static class ViewHolder{
		TextView cancel_name;
		TextView cancel_title;
	}
}
