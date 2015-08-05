package com.parttime.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.easemob.chat.EMChatManager;
import com.parttime.IM.activitysetting.GroupResumeSettingActivity;
import com.parttime.addresslist.userdetail.UserDetailActivity;
import com.parttime.common.Image.ImageShowActivity;
import com.parttime.common.activity.ChooseListActivity;
import com.parttime.constants.ActivityExtraAndKeys;
import com.parttime.main.MainTabActivity;
import com.parttime.net.GroupSettingRequest;
import com.parttime.pojo.PartJob;
import com.parttime.publish.JobBrokerDetailActivity;
import com.parttime.publish.JobDetailActivity;
import com.quark.jianzhidaren.ApplicationControl;

import java.util.ArrayList;

/**
 * Intent启动辅助类
 */
public class IntentManager {

    /**
     * 活动群跳转到用户详情界面
     */
    public static void toUserDetailFromActivityGroup(Activity activity,
                                                     int isEnd,
                                                     String groupId,
                                                     GroupSettingRequest.UserVO userVO,
                                                     ArrayList<String> userIds,
                                                     String groupOwner){
        Intent intent = new Intent(activity, UserDetailActivity.class);
        intent.putExtra(ActivityExtraAndKeys.GroupSetting.GROUPID , groupId);
        if(userVO != null) {
            intent.putExtra(ActivityExtraAndKeys.UserDetail.SELECTED_USER_ID, String.valueOf(userVO.userId));
            if(isEnd == GroupSettingRequest.AppliantResult.NO_END) {
                intent.putExtra(ActivityExtraAndKeys.UserDetail.FROM_AND_STATUS, UserDetailActivity.FromAndStatus.FROM_ACTIVITY_GROUP_AND_NOT_FINISH);
            }else{
                intent.putExtra(ActivityExtraAndKeys.UserDetail.FROM_AND_STATUS, UserDetailActivity.FromAndStatus.FROM_ACTIVITY_GROUP_AND_IS_FINISH);
            }
        }
        if(userIds != null){
            intent.putStringArrayListExtra(ActivityExtraAndKeys.USER_ID, userIds);
        }
        if(EMChatManager.getInstance().getCurrentUser()
                .equals(groupOwner)){
            intent.putExtra(ActivityExtraAndKeys.GroupSetting.GROUPOWNER, true);
        }

        activity.startActivity(intent);
    }

    /**
     * 普通群跳转到联系人详情
     * @param activity Activity
     * @param username String
     * @param groupId String
     * @param objects ArrayList<String>
     * @param groupOwner String
     */
    public static void intentToUseDetail(Activity activity, String username,
                                   String groupId, ArrayList<String> objects,String groupOwner) {
        Intent intent = new Intent(activity,UserDetailActivity.class);
        intent.putExtra(ActivityExtraAndKeys.GroupSetting.GROUPID , groupId);
        intent.putStringArrayListExtra(ActivityExtraAndKeys.USER_ID, objects);
        intent.putExtra(ActivityExtraAndKeys.UserDetail.SELECTED_USER_ID,username );
        if(EMChatManager.getInstance().getCurrentUser()
                .equals(groupOwner)){
            intent.putExtra(ActivityExtraAndKeys.GroupSetting.GROUPOWNER, true);
        }
        intent.putExtra(ActivityExtraAndKeys.UserDetail.FROM_AND_STATUS, UserDetailActivity.FromAndStatus.FROM_NORMAL_GROUP_AND_FRIEND);
        activity.startActivity(intent);
    }


    /**
     * 打开公共选择列表界面
     *
     * @param data 列表显示的内容
     */
    public static void openChoooseListActivity(Activity activity, String title, String[] data, int requestCode) {
        Intent intent = new Intent(activity, ChooseListActivity.class);
        intent.putExtra(ChooseListActivity.EXTRA_TITLE, title);
        intent.putExtra(ChooseListActivity.EXTRA_DATA, data);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void openJobDetailActivity(Context context, PartJob partJob) {
        Intent intent = new Intent(context, JobDetailActivity.class);
        intent.putExtra(JobDetailActivity.EXTRA_PART_JOB, partJob);
        context.startActivity(intent);
    }

    /**
     * 跳完活动详情页面
     * @param context
     * @param jobId 活动ID （二个传一个，不传时带 <= 0 的值）
     * @param groupId 群组ID （二个传一个，不传时带""或者null ）
     */
    public static void openJobDetailActivity(Context context, int jobId, String groupId) {
        Intent intent = new Intent(context, JobDetailActivity.class);
        intent.putExtra(JobDetailActivity.EXTRA_ID, jobId);
        intent.putExtra(JobDetailActivity.EXTRA_GROUP_ID, groupId);
        context.startActivity(intent);
    }

    /**
     * 打开经纪人详情页面
     * @param context
     * @param companyId
     */
    public static void openBrokerDetailActivity(Context context, int companyId) {
        Intent intent = new Intent(context, JobBrokerDetailActivity.class);
        intent.putExtra(JobBrokerDetailActivity.EXTRA_COMPANY_ID, companyId);
        context.startActivity(intent);
    }

    public static void goToMainTabActivity(Context context){
        Intent intent = new Intent(context, MainTabActivity.class);
        context.startActivity(intent);
    }

    public static void intentToImageShow(Context context,ArrayList<String> pictures, ArrayList<String> userIds) {
        Intent intent = new Intent(context, ImageShowActivity.class);
        intent.putStringArrayListExtra(ActivityExtraAndKeys.USER_ID,userIds);
        intent.putStringArrayListExtra(ActivityExtraAndKeys.ImageShow.PICTURES,pictures);
        context.startActivity(intent);
    }
}
