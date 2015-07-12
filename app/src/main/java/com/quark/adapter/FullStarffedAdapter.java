package com.quark.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;
import com.quark.common.Url;
import com.quark.http.image.CircularImage;
import com.quark.http.image.LoadImage;
import com.quark.model.RosterUser;

/**
*
* @ClassName: 
* @Description: TODO
* @author C罗
* @date 2015-1-22 下午8:40:37
*
*/
public class FullStarffedAdapter extends BaseAdapter {

	private ViewHolder holder;
	private List<RosterUser> list;
	private Context context;
	 
	public FullStarffedAdapter(Context context,List<RosterUser> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.company_person_list, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.cover_user_photo = (CircularImage)convertView.findViewById(R.id.cover_user_photo);
			holder.sex = (TextView) convertView.findViewById(R.id.sex);
			holder.age = (TextView) convertView.findViewById(R.id.age);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(list.get(i).getName());
		if(list.get(i).getSex()==-1){
			holder.sex.setText("未知");
		}else if(list.get(i).getSex()==0){
			holder.sex.setText("女");
		}else{
			holder.sex.setText("男");
		}
		
		holder.age.setText(list.get(i).getAge()+"");
		
		if(list.get(i).getPicture_1()==null||list.get(i).getPicture_1().equals("")){
			
		}else{
			LoadImage.loadImage(Url.GETPIC+list.get(i).getPicture_1(),holder.cover_user_photo);
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView name ;
		CircularImage cover_user_photo ;
		TextView sex ;
		TextView age;		
	}

}
