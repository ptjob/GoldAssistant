package com.parttime.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.easemob.chat.EMChatManager;
import com.parttime.IM.activitysetting.GroupResumeSettingActivity;
import com.parttime.addresslist.userdetail.UserDetailActivity;
import com.parttime.common.activity.ChooseListActivity;
import com.parttime.constants.ActivityExtraAndKeys;
import com.parttime.main.MainTabActivity;
import com.parttime.net.GroupSettingRequest;
import com.parttime.pojo.PartJob;
import com.parttime.publish.JobDetailActivity;

import java.util.ArrayList;

/**
 * Intent启动辅助类
 */
public class IntentManager {

    /**
     * 活动群跳转到用户详情界面
     */
    public static void toUserDetailFromActivityGroup(GroupResumeSettingActivity activity,
                                                     String groupId,
                                                     GroupSettingRequest.UserVO userVO,
                                                     ArrayList<String> userIds,
                                                     String groupOwner){
        Intent intent = new Intent(activity, UserDetailActivity.class);
        intent.putExtra(ActivityExtraAndKeys.GroupSetting.GROUPID , groupId);
        if(userVO != null) {
            intent.putExtra(ActivityExtraAndKeys.UserDetail.SELECTED_USER_ID, String.valueOf(userVO.userId));
            if(userVO.ableComment == GroupSettingRequest.UserVO.ABLECOMMENT_NO) {
                intent.putExtra(ActivityExtraAndKeys.UserDetail.FROM_AND_STATUS, UserDetailActivity.FromAndStatus.FROM_ACTIVITY_GROUP_AND_NOT_FINISH);
            }else if(userVO.ableComment == GroupSettingRequest.UserVO.ABLECOMMENT_OK) {
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

    public static void openJobDetailActivity(Context context, int jobId) {
        Intent intent = new Intent(context, JobDetailActivity.class);
        intent.putExtra(JobDetailActivity.EXTRA_ID, jobId);
        context.startActivity(intent);
    }

    public static void goToMainTabActivity(Context context){
        Intent intent = new Intent(context, MainTabActivity.class);
        context.startActivity(intent);
    }
}
