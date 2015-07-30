package com.parttime.addresslist.userdetail;

import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;

/**
 * 简历容器
 */
public  class ResumeContentContainer implements View.OnClickListener{
    public RelativeLayout appraiseContainer; //评价title容器

    public LinearLayout appraiseValueContainer,//评价容器
            resumeBottomContainer,//招聘底部容器
            callContainer,
            sendMsgContainer,
            unhandleContainer; //简历没有处理的，显示招聘，拒绝

    public CheckBox checkBox; //评论展开

    public TextView cancelResume,//取消录取
            summaryValue, //简介
            reject,
            pass;

    //数据部分
    UserDetailPagerAdapter.UserDetailFragment userDetailFragment;
    UserDetailPagerAdapter userDetailPagerAdapter;

    public ResumeContentContainer(UserDetailPagerAdapter.UserDetailFragment userDetailFragment,
                                  UserDetailPagerAdapter userDetailPagerAdapter) {
        this.userDetailFragment = userDetailFragment;
        this.userDetailPagerAdapter = userDetailPagerAdapter;
    }

    public void initView(View view){
        appraiseContainer = (RelativeLayout)view.findViewById(R.id.appraise_container);

        appraiseValueContainer = (LinearLayout)view.findViewById(R.id.appraise_value_container);
        resumeBottomContainer = (LinearLayout)view.findViewById(R.id.resume_bottom);
        callContainer = (LinearLayout)view.findViewById(R.id.call_container);
        sendMsgContainer = (LinearLayout)view.findViewById(R.id.send_msg_container);
        unhandleContainer = (LinearLayout)view.findViewById(R.id.unhandle_container);

        checkBox = (CheckBox)view.findViewById(R.id.expend_checked);

        summaryValue = (TextView)view.findViewById(R.id.summary_value);
        cancelResume = (TextView)view.findViewById(R.id.cancel_resume);
        reject = (TextView)view.findViewById(R.id.reject);
        pass = (TextView)view.findViewById(R.id.pass);

        appraiseContainer.setVisibility(View.VISIBLE);
        appraiseValueContainer.setVisibility(View.VISIBLE);
        resumeBottomContainer.setVisibility(View.VISIBLE);

        callContainer.setOnClickListener(this);
        sendMsgContainer.setOnClickListener(this);
        cancelResume.setOnClickListener(this);
        reject.setOnClickListener(this);
        pass.setOnClickListener(this);
        checkBox.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.call_container:
                break;
            case R.id.send_msg_container:
                break;
            case R.id.cancel_resume:
                break;
            case R.id.reject:
                break;
            case R.id.pass:
                break;
            case R.id.expend_checked:
                break;
        }
    }
}