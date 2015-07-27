package com.parttime.publish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parttime.publish.vo.JobManageListVo;
import com.qingmu.jianzhidaren.R;

import java.util.List;

/**
 * Created by wyw on 2015/7/26.
 */
public class JobManageListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<JobManageListVo> mData;

    public JobManageListAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setAll(List<JobManageListVo> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.item_job_manage_list, null);
            viewHolder = new ViewHolder();
            viewHolder.mTxtJobTitle = (TextView) view.findViewById(R.id.txt_job_title);
            viewHolder.mTxtView = (TextView) view.findViewById(R.id.txt_view);
            viewHolder.mTxtHand = (TextView) view.findViewById(R.id.txt_hand);
            viewHolder.mTxtHire = (TextView) view.findViewById(R.id.txt_hire);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        return null;
    }

    private class ViewHolder {
        private TextView mTxtJobTitle;
        private TextView mTxtView;
        private TextView mTxtHand;
        private TextView mTxtHire;
    }
}
