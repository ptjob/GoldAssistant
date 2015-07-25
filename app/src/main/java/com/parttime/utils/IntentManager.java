package com.parttime.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.parttime.common.activity.ChooseListActivity;
import com.parttime.main.MainTabActivity;
import com.parttime.pojo.PartJob;
import com.parttime.publish.JobDetailActivity;

/**
 * Intent启动辅助类
 */
public class IntentManager {
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

    public static void openJobDetailActivity(Context context, int jobId, int companyId) {
        Intent intent = new Intent(context, JobDetailActivity.class);
        intent.putExtra(JobDetailActivity.EXTRA_ID, jobId);
        intent.putExtra(JobDetailActivity.EXTRA_COMPANY_ID, companyId);
        context.startActivity(intent);
    }

    public static void goToMainTabActivity(Context context){
        Intent intent = new Intent(context, MainTabActivity.class);
        context.startActivity(intent);
    }
}
