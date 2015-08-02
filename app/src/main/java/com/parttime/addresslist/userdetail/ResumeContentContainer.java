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

import com.parttime.IM.ChatActivity;
import com.parttime.IM.activitysetting.GroupResumeSettingActivity;
import com.parttime.IM.activitysetting.GroupSettingUtils;
import com.parttime.net.DefaultCallback;
import com.parttime.net.GroupSettingRequest;
import com.parttime.pojo.CommentVO;
import com.parttime.pojo.UserDetailVO;
import com.parttime.widget.CommentView;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.ApplicationControl;
import com.quark.ui.widget.CustomDialog;

import java.util.ArrayList;
import java.util.List;

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
            pass,
            loadingMore;

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
        loadingMore = (TextView)view.findViewById(R.id.loading_more);

        appraiseContainer.setVisibility(View.VISIBLE);
        appraiseValueContainer.setVisibility(View.GONE);
        resumeBottomContainer.setVisibility(View.VISIBLE);
        loadingMore.setVisibility(View.GONE);

        callContainer.setOnClickListener(this);
        sendMsgContainer.setOnClickListener(this);
        cancelResume.setOnClickListener(this);
        reject.setOnClickListener(this);
        pass.setOnClickListener(this);
        checkBox.setOnClickListener(this);
        loadingMore.setOnClickListener(this);

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
                        "c"+userDetailFragment.userId));
                break;
            case R.id.cancel_resume:
            case R.id.reject:{
                GroupResumeSettingActivity.Action action = null ;
                if(v.getId() == R.id.cancel_resume){
                    action = GroupResumeSettingActivity.Action.UNRESUME;
                }else if(v.getId() == R.id.reject){
                    action = GroupResumeSettingActivity.Action.REJECT;
                }
                GroupSettingRequest.UserVO vo = userDetailVO.toUserVO();
                //取消录取  已录用的人员，点击取消录用，弹窗提示“确认取消录用改用，取消后该用户将被移除聊天群组”——取消，确认
                new GroupSettingUtils().showAlertDialog(activity,
                        null,
                        ApplicationControl.getInstance().getString(R.string.cacel_resume_or_not_and_remove_from_group),
                        action,vo ,
                        R.string.ok, R.string.cancel,
                        userDetailFragment.userId, activity.groupId, activity.queue,
                        new DefaultCallback(){
                            @Override
                            public void success(Object obj) {
                                userDetailPagerAdapter.userIds.remove(userDetailFragment.userId);
                                userDetailPagerAdapter.cache.remove(userDetailFragment.userId);
                                activity.adapter.notifyDataSetChanged();
                            }
                        });
                break;}
            case R.id.pass:{
                GroupSettingRequest.UserVO vo = userDetailVO.toUserVO();
                //录取   确认后可取消录用该用户，信息中心会提醒用户‘已被商家取消录用’。同时该用户也将被移除聊天群组
                new GroupSettingUtils().showAlertDialog(activity,
                        null ,
                        ApplicationControl.getInstance().getString(R.string.cacel_resume_or_not),
                        GroupResumeSettingActivity.Action.RESUME, vo,
                        R.string.yes , R.string.no,
                        userDetailFragment.userId, activity.groupId, activity.queue,
                        new DefaultCallback(){
                            @Override
                            public void success(Object obj) {
                                showResumedView();
                                activity.adapter.notifyDataSetChanged();
                            }
                        });
                break;}
            case R.id.loading_more:
                loadData();
                break;
            case R.id.expend_checked:
                //网络加载数据，动态加载
                appraiseValueContainer.setVisibility(View.VISIBLE);
                loadingMore.setVisibility(View.VISIBLE);
                loadData();
                break;
        }
    }

    private void loadData() {
        Pager pager = new Pager();
        activity.showWait(true);
        addCommentView(new ArrayList<CommentVO>());
        activity.showWait(false);
    }

    private void addCommentView(List<CommentVO> commentVoList){
        if(commentVoList == null){
            return ;
        }
        for(CommentVO commentVO : commentVoList){
            if(commentVO == null){
                continue;
            }
            addCommentView(commentVO);
        }

    }

    private void addCommentView(CommentVO commonVO) {
        String comment = commonVO.comment;
        String groupName = commonVO.groupName;
        String remark = commonVO.remark;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        CommentView commentView = new CommentView(activity);
        commentView.bindData(activity.getString(R.string.comment_excellent),
                "活动" + groupName, "remark " + remark);
        appraiseValueContainer.addView(commentView,params);
    }

    public void reflesh(UserDetailVO vo) {
        this.userDetailVO = vo;
        summaryValue.setText(vo.summary);
        int apply = vo.apply;
        if(apply == 0 || apply == 3){//（0-没查看，1-已录取，2-、已拒绝，3-已查看）
            showUnResumeView();
        }else if(apply == 1){//已录取
            showResumedView();
        }
    }

    private void showUnResumeView() {
        unhandleContainer.setVisibility(View.VISIBLE);
        cancelResume.setVisibility(View.GONE);
    }

    private void showResumedView() {
        unhandleContainer.setVisibility(View.GONE);
        cancelResume.setVisibility(View.VISIBLE);
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

    private class Pager{
        int currentPage = 0;
        int pageCount = 10 ;
        int currentId = 0;
    }

}