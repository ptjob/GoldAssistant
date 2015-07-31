package com.parttime.addresslist.userdetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carson.constant.ConstantForSaveList;
import com.parttime.IM.ChatActivity;
import com.parttime.net.DefaultCallback;
import com.parttime.net.GroupSettingRequest;
import com.parttime.pojo.UserDetailVO;
import com.qingmu.jianzhidaren.R;
import com.quark.ui.widget.CustomDialog;

import java.util.ArrayList;

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

    UserDetailActivity activity;

    //数据部分
    UserDetailPagerAdapter.UserDetailFragment userDetailFragment;
    UserDetailPagerAdapter userDetailPagerAdapter;

    UserDetailVO userDetailVO;

    public ResumeContentContainer(UserDetailPagerAdapter.UserDetailFragment userDetailFragment,
                                  UserDetailPagerAdapter userDetailPagerAdapter) {
        this.userDetailFragment = userDetailFragment;
        this.userDetailPagerAdapter = userDetailPagerAdapter;
        this.activity = userDetailPagerAdapter.userDetailActivity;
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
                if(userDetailVO != null && ! TextUtils.isEmpty(userDetailVO.telephone)) {
                    showDialog(null,String.format("确认呼叫%s",userDetailVO.telephone),
                            Action.CALL,userDetailVO,
                            R.string.ok, R.string.cancel);
                }else{
                    Toast.makeText(activity,"电话号码为空",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.send_msg_container:
                activity.startActivity(new Intent(activity,
                        ChatActivity.class).putExtra("userId",
                        userDetailFragment.userId));
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

    public void reflesh(UserDetailVO vo) {
        this.userDetailVO = vo;
        summaryValue.setText(vo.summary);
    }

    public void showDialog(String title, String message,final Action action,
                           final UserDetailVO userDetailVO,
                           int positiveRes, int negativeRes) {

        CustomDialog.Builder builder = new CustomDialog.Builder(activity);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(positiveRes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //activity.showWait(true);
                if(action == Action.CALL){
                    Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + userDetailVO.telephone));
                    activity.startActivity(intent);
                }else if(action == Action.MSG){

                }
            }
        });

        builder.setNegativeButton(negativeRes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.create().show();
    }

    private static enum Action{
        MSG, CALL
    }

}