package com.parttime.utils;

import android.app.Activity;
import android.content.Intent;

import com.parttime.common.activity.ChooseListActivity;

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
}
