package com.parttime.publish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parttime.publish.vo.JobPlazaListVo;
import com.qingmu.jianzhidaren.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wyw on 2015/8/3.
 */
public class JobPlazaListAdapter extends BaseAdapter {
    private Context mContext;
    List<JobPlazaListVo> mData;
    private LayoutInflater mLayoutInflater;

    public JobPlazaListAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mData = new ArrayList<>();
    }

    public void setAll(List<JobPlazaListVo> jobPlazaListVoList) {
        mData = jobPlazaListVoList;
        notifyDataSetChanged();
    }

    public void addAll(List<JobPlazaListVo> jobPlazaListVoList) {
        mData.addAll(jobPlazaListVoList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return mData != null ? mData.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return mData != null ? mData.get(i).jobId : 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.item_job_plaza_list, null);
            viewHolder = new ViewHolder();
            viewHolder.mTxtJobTitle = (TextView) view.findViewById(R.id.txt_job_title);
            viewHolder.mImgViType = (ImageView) view.findViewById(R.id.imgvi_tag);
            viewHolder.mTxtType = (TextView) view.findViewById(R.id.txt_type);
            viewHolder.mImgViIsGuarantee = (ImageView) view.findViewById(R.id.imgvi_promiss);
            viewHolder.mImgViIsSuper = (ImageView) view.findViewById(R.id.imgvi_super);
            viewHolder.mImgViIsTime = (ImageView) view.findViewById(R.id.imgvi_summer);
            viewHolder.mImgViIsExpedited = (ImageView) view.findViewById(R.id.imgvi_expedited);
            viewHolder.mTxtTime = (TextView) view.findViewById(R.id.txt_time);
            viewHolder.mTxtArea = (TextView) view.findViewById(R.id.txt_work_area);
            viewHolder.mTxtSalary = (TextView) view.findViewById(R.id.txt_salary);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        JobPlazaListVo jobManageListVo = mData.get(i);
        viewHolder.mTxtJobTitle.setText(jobManageListVo.jobTitle);
        viewHolder.mImgViType.setImageDrawable(mContext.getResources().getDrawable(jobManageListVo.typeDrawableId));
        viewHolder.mTxtType.setText(jobManageListVo.type);
        viewHolder.mImgViIsGuarantee.setVisibility(jobManageListVo.isGuarantee ? View.VISIBLE : View.GONE);
        viewHolder.mImgViIsSuper.setVisibility(jobManageListVo.isSuper ? View.VISIBLE : View.GONE);
        viewHolder.mImgViIsTime.setVisibility(jobManageListVo.isTime ? View.VISIBLE : View.GONE);
        viewHolder.mImgViIsExpedited.setVisibility(jobManageListVo.isExpedited ? View.VISIBLE : View.GONE);
        viewHolder.mTxtTime.setText(jobManageListVo.time);
        viewHolder.mTxtArea.setText(jobManageListVo.area);
        viewHolder.mTxtSalary.setText(jobManageListVo.salary);

        return view;
    }

    private class ViewHolder {
        private TextView mTxtJobTitle;
        private TextView mTxtType;
        private ImageView mImgViType;
        private ImageView mImgViIsGuarantee;
        private ImageView mImgViIsSuper;
        private ImageView mImgViIsTime;
        private ImageView mImgViIsExpedited;
        private TextView mTxtTime;
        private TextView mTxtArea;
        private TextView mTxtSalary;
    }
}
