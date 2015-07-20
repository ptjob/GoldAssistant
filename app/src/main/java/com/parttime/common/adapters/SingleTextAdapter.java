package com.parttime.common.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;

import java.util.List;

/**
 * Created by cjz on 2015/7/14.
 */
public class SingleTextAdapter extends BaseAdapter{

    private Context context;
    private List<Object> datas;
    private LayoutInflater inflater;
    public SingleTextAdapter(Context context, List<Object> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_single_text, null);
            holder.tvName = (TextView) convertView;
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Object item = getItem(position);
        holder.tvName.setText(item.toString());
        return convertView;
    }

    public class ViewHolder {
        public TextView tvName;
    }
}
